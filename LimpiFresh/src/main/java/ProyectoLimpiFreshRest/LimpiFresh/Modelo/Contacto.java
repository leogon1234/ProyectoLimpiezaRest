package ProyectoLimpiFreshRest.LimpiFresh.Modelo;

import jakarta.persistence.*;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contacto")
@Schema(description = "Modelo de mensaje de contacto")
public class Contacto{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del mensaje", example = "1")
    private Integer  id;
    
    @Schema(description = "Nombre de la persona que envía el mensaje", example = "María González", required = true)
    private String nombre;
    
    @Schema(description = "Email de contacto", example = "maria@example.com", required = true)
    private String email;
    
    @Schema(description = "Asunto del mensaje", example = "Consulta sobre productos", required = true)
    private String asunto;
    
    @Column(length = 2000)
    @Schema(description = "Contenido del mensaje (máx 2000 caracteres)", example = "Me gustaría saber más sobre...", required = true)
    private String mensaje;

}