package ProyectoLimpiFreshRest.LimpiFresh.Controller;

import ProyectoLimpiFreshRest.LimpiFresh.Dto.CrearBoletaRequest;
import ProyectoLimpiFreshRest.LimpiFresh.Modelo.Boleta;
import ProyectoLimpiFreshRest.LimpiFresh.Service.BoletaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = "http://limpifresh-pagina.s3-website-us-east-1.amazonaws.com")
@RestController
@RequestMapping("/api/boletas")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Boletas", description = "Gestión de boletas de compra")
public class BoletaController {

    private final BoletaService boletaService;

    public BoletaController(BoletaService service) {
        this.boletaService = service;
    }

    @Operation(summary = "Checkout: crear boleta desde compra", description = "Recibe usuario/datos/envío + items (productoId, cantidad) y crea boleta en BD.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Boleta creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping("/checkout")
    public ResponseEntity<Boleta> checkout(@RequestBody CrearBoletaRequest req) {
        return ResponseEntity.ok(boletaService.crearDesdeCompra(req));
    }

    @Operation(summary = "Listar boletas", description = "Devuelve todas las boletas (para Admin).")
    @GetMapping
    public ResponseEntity<List<Boleta>> listar() {
        return ResponseEntity.ok(boletaService.listarTodas());
    }

    @Operation(summary = "Obtener boleta por ID", description = "Obtiene una boleta específica por ID.")
    @GetMapping("/{id}")
    public ResponseEntity<Boleta> getPorId(
            @Parameter(description = "ID numérico de la boleta", required = true, example = "1")
            @PathVariable int id) {
        return boletaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtener boleta por número", description = "Busca boleta por numeroBoleta (INV-...).")
    @GetMapping("/numero/{numero}")
    public ResponseEntity<Boleta> getPorNumero(
            @Parameter(description = "Número único de la boleta", required = true, example = "INV-1234567890")
            @PathVariable String numero) {
        Optional<Boleta> boleta = boletaService.buscarPorNumero(numero);
        return boleta.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
