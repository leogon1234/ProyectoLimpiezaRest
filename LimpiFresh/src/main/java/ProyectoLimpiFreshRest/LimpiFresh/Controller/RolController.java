package ProyectoLimpiFreshRest.LimpiFresh.Controller;

import ProyectoLimpiFreshRest.LimpiFresh.Modelo.Rol;
import ProyectoLimpiFreshRest.LimpiFresh.Service.RolService;
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
@CrossOrigin(origins = "http://pagina-limpifresh.s3-website-us-east-1.amazonaws.com")
@RestController
@RequestMapping("/api/rol")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Roles", description = "Gestión de roles de usuario")
public class RolController {

    private final RolService rolService;

    public RolController(RolService rolService) {
        this.rolService = rolService;
    }

    @Operation(
            summary = "Crear un nuevo rol",
            description = "Crea un nuevo rol en el sistema (ej: CLIENTE, ADMIN, VENDEDOR). " +
                    "El nombre del rol debe ser único. " +
                    "⚠️ En producción, este endpoint debería requerir autenticación de super administrador."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Rol creado exitosamente",
                    content = @Content(schema = @Schema(implementation = Rol.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error: nombre de rol duplicado o datos inválidos"
            )
    })
    @PostMapping
    public ResponseEntity<?> crearRol(
            @RequestBody(
                    description = "Datos del rol. Solo se requiere el campo 'nombreRol' (ej: 'ADMIN', 'CLIENTE').",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Rol.class))
            )
            @org.springframework.web.bind.annotation.RequestBody Rol rol) {
        try {
            Rol nuevo = rolService.crearRol(rol.getNombreRol());
            return ResponseEntity.ok(nuevo);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @Operation(
            summary = "Listar todos los roles",
            description = "Obtiene una lista de todos los roles disponibles en el sistema. " +
                    "Útil para mostrar opciones de selección de roles en interfaces de administración."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de roles obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = Rol.class))
    )
    @GetMapping
    public List<Rol> listarRoles() {
        return rolService.listarRoles();
    }

    @Operation(
            summary = "Eliminar rol por ID",
            description = "Elimina un rol del sistema. " +
                    "⚠️ Cuidado: Si hay usuarios asociados a este rol, la eliminación puede fallar o causar problemas. " +
                    "En producción, considera agregar validaciones adicionales."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Rol eliminado exitosamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error: no se puede eliminar el rol (probablemente tiene usuarios asociados)"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Rol no encontrado con el ID proporcionado"
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarRol(
            @Parameter(description = "ID del rol a eliminar", required = true, example = "1")
            @PathVariable Integer id) {
        try {
            rolService.eliminarRol(id);
            return ResponseEntity.ok("Rol eliminado con éxito");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}