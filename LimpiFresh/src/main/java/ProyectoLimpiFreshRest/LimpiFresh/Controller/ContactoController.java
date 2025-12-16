package ProyectoLimpiFreshRest.LimpiFresh.Controller;

import ProyectoLimpiFreshRest.LimpiFresh.Modelo.Contacto;
import ProyectoLimpiFreshRest.LimpiFresh.Service.ContactoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = "http://pagina-limpifresh.s3-website-us-east-1.amazonaws.com")
@RestController
@RequestMapping("/api/contacto")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Contacto", description = "Gestión de mensajes de contacto")
public class ContactoController {

    private final ContactoService contactoService;

    public ContactoController(ContactoService service) {
        this.contactoService = service;
    }

    @Operation(
            summary = "Enviar mensaje de contacto",
            description = "Endpoint público para que los usuarios envíen mensajes de contacto. " +
                    "No requiere autenticación. Los mensajes se almacenan para que los administradores los revisen."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Mensaje de contacto guardado correctamente",
            content = @Content(schema = @Schema(implementation = Contacto.class))
    )
    @PostMapping
    public ResponseEntity<Contacto> enviarMensaje(
            @RequestBody(
                    description = "Datos del mensaje. Campos requeridos: nombre, email, asunto, mensaje (máx 2000 caracteres).",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Contacto.class))
            )
            @org.springframework.web.bind.annotation.RequestBody Contacto contacto) {
        Contacto guardado = contactoService.guardarMensaje(contacto);
        return ResponseEntity.ok(guardado);
    }

    @Operation(
            summary = "Listar todos los mensajes de contacto",
            description = "Obtiene todos los mensajes de contacto almacenados. " +
                    "⚠️ En producción, este endpoint debería requerir autenticación de administrador."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de mensajes de contacto",
            content = @Content(schema = @Schema(implementation = Contacto.class))
    )
    @GetMapping
    public List<Contacto> listarMensajes() {
        return contactoService.listarTodos();
    }

    @Operation(
            summary = "Obtener mensaje de contacto por ID",
            description = "Obtiene un mensaje de contacto específico mediante su ID. " +
                    "Útil para ver los detalles completos de un mensaje."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Mensaje encontrado exitosamente",
                    content = @Content(schema = @Schema(implementation = Contacto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Mensaje no encontrado con el ID proporcionado"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Contacto> obtenerMensaje(
            @Parameter(description = "ID del mensaje de contacto", required = true, example = "1")
            @PathVariable int id) {
        return contactoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Eliminar mensaje de contacto",
            description = "Elimina permanentemente un mensaje de contacto del sistema. " +
                    "Esta operación no se puede deshacer."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Mensaje eliminado exitosamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Mensaje no encontrado con el ID proporcionado"
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMensaje(
            @Parameter(description = "ID del mensaje a eliminar", required = true, example = "1")
            @PathVariable int id) {
        if (contactoService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        contactoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}