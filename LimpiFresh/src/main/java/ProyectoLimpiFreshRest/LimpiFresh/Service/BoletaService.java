package ProyectoLimpiFreshRest.LimpiFresh.Service;

import ProyectoLimpiFreshRest.LimpiFresh.Dto.CrearBoletaRequest;
import ProyectoLimpiFreshRest.LimpiFresh.Dto.ItemBoletaRequest;
import ProyectoLimpiFreshRest.LimpiFresh.Modelo.Boleta;
import ProyectoLimpiFreshRest.LimpiFresh.Modelo.BoletaItem;
import ProyectoLimpiFreshRest.LimpiFresh.Modelo.Producto;
import ProyectoLimpiFreshRest.LimpiFresh.Modelo.Usuario;
import ProyectoLimpiFreshRest.LimpiFresh.Repository.BoletaRepository;
import ProyectoLimpiFreshRest.LimpiFresh.Repository.ProductoRepository;
import ProyectoLimpiFreshRest.LimpiFresh.Repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BoletaService {

    private final BoletaRepository boletaRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    public BoletaService(BoletaRepository boletaRepository,
                         ProductoRepository productoRepository,
                         UsuarioRepository usuarioRepository) {
        this.boletaRepository = boletaRepository;
        this.productoRepository = productoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Boleta guardar(Boleta boleta) {
        return boletaRepository.save(boleta);
    }

    public Optional<Boleta> buscarPorId(int id) {
        return boletaRepository.findById(id);
    }

    public Optional<Boleta> buscarPorNumero(String numero) {
        return boletaRepository.findByNumeroBoleta(numero);
    }

    public List<Boleta> listarTodas() {
        return boletaRepository.findAll();
    }

    // ✅ ESTA ES LA CLAVE: crea boleta desde carrito/checkout
    public Boleta crearDesdeCompra(CrearBoletaRequest req) {
        if (req == null) throw new RuntimeException("Request vacío");
        if (req.getItems() == null || req.getItems().isEmpty())
            throw new RuntimeException("La boleta debe tener al menos 1 item");

        Boleta boleta = new Boleta();

        // numero boleta único (simple)
        boleta.setNumeroBoleta("INV-" + System.currentTimeMillis());
        boleta.setFechaBoleta(LocalDateTime.now());

        boleta.setNombreCliente(req.getNombreCliente());
        boleta.setDireccion(req.getDireccion());
        boleta.setCiudad(req.getCiudad());
        boleta.setTelefono(req.getTelefono());

        // usuario opcional
        if (req.getUsuarioId() != null) {
            Usuario u = usuarioRepository.findById(req.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Usuario no existe: " + req.getUsuarioId()));
            boleta.setUsuario(u);
        }

        int subtotal = 0;
        List<BoletaItem> items = new ArrayList<>();

        for (ItemBoletaRequest itReq : req.getItems()) {
            if (itReq.getProductoId() == null) throw new RuntimeException("productoId es requerido");
            if (itReq.getCantidad() == null || itReq.getCantidad() <= 0)
                throw new RuntimeException("cantidad inválida para productoId=" + itReq.getProductoId());

            Producto producto = productoRepository.findById(itReq.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no existe: " + itReq.getProductoId()));

            int cantidad = itReq.getCantidad();

            // precio base
            int precioUnitario = producto.getPrecio() != null ? producto.getPrecio() : 0;

            boolean ofertaAplicada = Boolean.TRUE.equals(producto.getOferta())
                    && producto.getPrecioOferta() != null
                    && producto.getPrecioOferta() > 0;

            Integer precioOfertaUnitario = ofertaAplicada ? producto.getPrecioOferta() : null;

            int unit = ofertaAplicada ? precioOfertaUnitario : precioUnitario;
            subtotal += (unit * cantidad);

            BoletaItem item = new BoletaItem();
            item.setBoleta(boleta); // 🔥 IMPORTANTÍSIMO para que JPA guarde
            item.setProducto(producto);
            item.setNombreProducto(producto.getNombre());
            item.setCantidad(cantidad);
            item.setPrecioUnitario(precioUnitario);
            item.setOfertaAplicada(ofertaAplicada);
            item.setPrecioOfertaUnitario(precioOfertaUnitario);

            items.add(item);
        }

        boleta.setItems(items);
        boleta.setSubtotal(subtotal);

        // IVA 19% fijo (puedes cambiarlo si quieres)
        int iva = (int) Math.round(subtotal * 0.19);
        boleta.setIva(iva);
        boleta.setTotal(subtotal + iva);

        // cascade guarda items
        return boletaRepository.save(boleta);
    }
}
