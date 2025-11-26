package ProyectoLimpiFreshRest.LimpiFresh.Controller;

import ProyectoLimpiFreshRest.LimpiFresh.Modelo.Producto;
import ProyectoLimpiFreshRest.LimpiFresh.Service.ProductoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService service) {
        this.productoService = service;
    }

    @GetMapping
    public List<Producto> getTodos() {
        return productoService.listarTodos();
    }

    @GetMapping("/ofertas")
    public List<Producto> getOfertas() {
        return productoService.listarOfertas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> getPorId(@PathVariable int id) {
        return productoService.buscarPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Producto> crear(@RequestBody Producto producto) {
        Producto creado = productoService.guardar(producto);
        return ResponseEntity.ok(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable int id, @RequestBody Producto producto) {
        if (productoService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        producto.setId(id);
        Producto actualizado = productoService.guardar(producto);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        if (productoService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
