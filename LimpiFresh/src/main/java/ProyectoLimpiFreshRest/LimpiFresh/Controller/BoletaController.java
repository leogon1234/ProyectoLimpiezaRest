package ProyectoLimpiFreshRest.LimpiFresh.Controller;

import ProyectoLimpiFreshRest.LimpiFresh.Modelo.Boleta;
import ProyectoLimpiFreshRest.LimpiFresh.Service.BoletaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/boletas")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Boletas", description = "Gestión de boletas de compra")
public class BoletaController {

    private final BoletaService boletaService;

    public BoletaController(BoletaService service) {
        this.boletaService = service;
    }

    @Operation(
            summary = "Crear una nueva boleta",
            description = "Crea una boleta de compra con los datos del cliente, items y totales. " +
                    "La boleta debe incluir: número de boleta (único), datos del cliente, items con productos, " +
                    "subtotal, IVA y total. La fecha se asigna automáticamente."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Boleta creada exitosamente",
                    content = @Content(schema = @Schema(implementation = Boleta.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error de validación: número de boleta duplicado o datos inválidos"
            )
    })
    @PostMapping
    public ResponseEntity<Boleta> crear(
            @RequestBody(
                    description = "Datos completos de la boleta incluyendo items, cliente y totales",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Boleta.class))
            )
            @org.springframework.web.bind.annotation.RequestBody Boleta boleta) {
        Boleta creada = boletaService.guardar(boleta);
        return ResponseEntity.ok(creada);
    }

    @Operation(
            summary = "Obtener boleta por ID",
            description = "Obtiene una boleta específica mediante su ID numérico. " +
                    "Incluye todos los detalles: cliente, items, totales y fecha."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Boleta encontrada exitosamente",
                    content = @Content(schema = @Schema(implementation = Boleta.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Boleta no encontrada con el ID proporcionado"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Boleta> getPorId(
            @Parameter(description = "ID numérico de la boleta", required = true, example = "1")
            @PathVariable int id) {
        return boletaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Obtener boleta por número",
            description = "Busca una boleta mediante su número único (ej: 'INV-1234567890'). " +
                    "Útil para que los clientes busquen sus boletas por el número de factura."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Boleta encontrada exitosamente",
                    content = @Content(schema = @Schema(implementation = Boleta.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Boleta no encontrada con el número proporcionado"
            )
    })
    @GetMapping("/numero/{numero}")
    public ResponseEntity<Boleta> getPorNumero(
            @Parameter(description = "Número único de la boleta (ej: 'INV-1234567890')", required = true, example = "INV-1234567890")
            @PathVariable String numero) {
        Optional<Boleta> boleta = boletaService.buscarPorNumero(numero);
        return boleta.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
