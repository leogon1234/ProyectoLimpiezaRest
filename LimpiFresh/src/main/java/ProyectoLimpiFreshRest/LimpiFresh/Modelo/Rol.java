package ProyectoLimpiFreshRest.LimpiFresh.Modelo;

import jakarta.persistence.*;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Table(name = "rol")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Schema(description = "Modelo de rol de usuario")
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del rol", example = "1")
    private Integer  id;
    
    @Schema(description = "Nombre del rol (CLIENTE, ADMIN, etc.)", example = "CLIENTE", required = true)
    private String nombreRol;

}