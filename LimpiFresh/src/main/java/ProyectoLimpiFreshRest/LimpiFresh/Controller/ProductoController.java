package ProyectoLimpiFreshRest.LimpiFresh.Controller;

import ProyectoLimpiFreshRest.LimpiFresh.Modelo.Producto;
import ProyectoLimpiFreshRest.LimpiFresh.Service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Productos", description = "Operaciones CRUD para gestión de productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService service) {
        this.productoService = service;
    }
    @Operation(
            summary = "Listar todos los productos",
            description = "Obtiene una lista completa de todos los productos disponibles en el catálogo. " +
                    "Incluye productos con y sin ofertas activas."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de productos obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = Producto.class))
    )
    @GetMapping
    public List<Producto> getTodos() {
        return productoService.listarTodos();
    }

    @Operation(
            summary = "Listar productos en oferta",
            description = "Obtiene únicamente los productos que tienen oferta activa (campo 'oferta' = true). " +
                    "Útil para mostrar promociones especiales en el frontend."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de productos en oferta",
            content = @Content(schema = @Schema(implementation = Producto.class))
    )
    @GetMapping("/ofertas")
    public List<Producto> getOfertas() {
        return productoService.listarOfertas();
    }

    @Operation(
            summary = "Obtener producto por ID",
            description = "Obtiene los detalles completos de un producto específico mediante su ID. " +
                    "Incluye información de precio, stock, descripciones, categoría e imágenes."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Producto encontrado exitosamente",
                    content = @Content(schema = @Schema(implementation = Producto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Producto no encontrado con el ID proporcionado"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Producto> getPorId(
            @Parameter(description = "ID numérico del producto", required = true, example = "1")
            @PathVariable int id) {
        return productoService.buscarPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Crear un nuevo producto",
            description = "Crea un nuevo producto en el catálogo. Requiere todos los campos obligatorios: " +
                    "nombre, precio, categoría, stock. Los campos 'oferta' y 'precioOferta' son opcionales."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Producto creado exitosamente con ID asignado",
                    content = @Content(schema = @Schema(implementation = Producto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos o campos obligatorios faltantes"
            )
    })
    @PostMapping
    public ResponseEntity<Producto> crear(
            @RequestBody(
                    description = "Objeto Producto con los datos del nuevo producto. El campo 'id' será generado automáticamente.",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Producto.class))
            ) @org.springframework.web.bind.annotation.RequestBody Producto producto) {
        return ResponseEntity.ok(productoService.guardar(producto));
    }

    @Operation(
            summary = "Actualizar un producto existente",
            description = "Actualiza los datos de un producto existente. El ID en la URL debe coincidir con el producto a actualizar. " +
                    "Todos los campos del producto pueden ser modificados excepto el ID."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Producto actualizado exitosamente",
                    content = @Content(schema = @Schema(implementation = Producto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Producto no encontrado con el ID proporcionado"
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(
            @Parameter(description = "ID del producto a actualizar", required = true, example = "1")
            @PathVariable int id,
            @RequestBody(
                    description = "Datos actualizados del producto",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Producto.class))
            ) @org.springframework.web.bind.annotation.RequestBody Producto producto) {
        if (productoService.buscarPorId(id).isEmpty()) return ResponseEntity.notFound().build();
        producto.setId(id);
        return ResponseEntity.ok(productoService.guardar(producto));
    }

    @Operation(
            summary = "Eliminar un producto",
            description = "Elimina permanentemente un producto del catálogo. Esta operación no se puede deshacer. " +
                    "Asegúrate de que el producto no esté asociado a boletas antes de eliminarlo."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Producto eliminado exitosamente (sin contenido en la respuesta)"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Producto no encontrado con el ID proporcionado"
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del producto a eliminar", required = true, example = "3")
            @PathVariable int id) {
        if (productoService.buscarPorId(id).isEmpty()) return ResponseEntity.notFound().build();
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
