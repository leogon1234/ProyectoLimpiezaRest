package ProyectoLimpiFreshRest.LimpiFresh.Controller;

import ProyectoLimpiFreshRest.LimpiFresh.Modelo.Blog;
import ProyectoLimpiFreshRest.LimpiFresh.Service.BlogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = "http://limpifresh-pagina.s3-website-us-east-1.amazonaws.com")
@RestController
@RequestMapping("/api/blogs")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Blogs", description = "Gestión de entradas de blog")
public class BlogController {

    private final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @Operation(
            summary = "Listar todos los blogs",
            description = "Obtiene todas las entradas de blog publicadas. " +
                    "Endpoint público, no requiere autenticación."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de blogs obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = Blog.class))
    )
    @GetMapping
    public List<Blog> listarTodos() {
        return blogService.listarTodos();
    }

    @Operation(
            summary = "Obtener blog por ID",
            description = "Obtiene una entrada de blog específica mediante su ID. " +
                    "Incluye el título y contenido completo (hasta 4000 caracteres)."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Blog encontrado exitosamente",
                    content = @Content(schema = @Schema(implementation = Blog.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Blog no encontrado con el ID proporcionado"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Blog> obtenerPorId(
            @Parameter(description = "ID del blog", required = true, example = "1")
            @PathVariable int id) {
        return blogService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Crear nueva entrada de blog",
            description = "Crea una nueva entrada de blog. Requiere título y contenido. " +
                    "⚠️ En producción, este endpoint debería requerir autenticación de administrador."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Blog creado exitosamente con ID asignado",
                    content = @Content(schema = @Schema(implementation = Blog.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos o campos obligatorios faltantes"
            )
    })
    @PostMapping
    public ResponseEntity<Blog> crear(
            @RequestBody(
                    description = "Datos del blog. Campos requeridos: titulo, contenido (máx 4000 caracteres). El ID se genera automáticamente.",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Blog.class))
            )
            @org.springframework.web.bind.annotation.RequestBody Blog blog) {
        Blog creado = blogService.guardar(blog);
        return ResponseEntity.ok(creado);
    }

    @Operation(
            summary = "Actualizar entrada de blog",
            description = "Actualiza una entrada de blog existente. El ID en la URL debe coincidir con el blog a actualizar."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Blog actualizado exitosamente",
                    content = @Content(schema = @Schema(implementation = Blog.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Blog no encontrado con el ID proporcionado"
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<Blog> actualizar(
            @Parameter(description = "ID del blog a actualizar", required = true, example = "1")
            @PathVariable int id,
            @RequestBody(
                    description = "Datos actualizados del blog",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Blog.class))
            )
            @org.springframework.web.bind.annotation.RequestBody Blog blog) {
        if (blogService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        blog.setId(id);
        Blog actualizado = blogService.guardar(blog);
        return ResponseEntity.ok(actualizado);
    }

    @Operation(
            summary = "Eliminar entrada de blog",
            description = "Elimina permanentemente una entrada de blog. " +
                    "Esta operación no se puede deshacer."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Blog eliminado exitosamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Blog no encontrado con el ID proporcionado"
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del blog a eliminar", required = true, example = "1")
            @PathVariable int id) {
        if (blogService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        blogService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}


