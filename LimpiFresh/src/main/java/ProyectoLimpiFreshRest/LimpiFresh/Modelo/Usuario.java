package ProyectoLimpiFreshRest.LimpiFresh.Modelo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Table(name = "usuario")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Schema(description = "Modelo de usuario del sistema")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del usuario (generado automáticamente)", example = "1")
    private Integer  id;
    
    @Schema(description = "Nombre completo del usuario", example = "Juan Pérez", required = true)
    private String nombre;
    
    @Schema(description = "Email del usuario (debe ser único)", example = "juan@example.com", required = true)
    private String email;
    
    @Schema(description = "Contraseña del usuario (no se devuelve en respuestas)", example = "password123", required = true)
    private String password;
    
    @Schema(description = "RUT del usuario con dígito verificador", example = "12.345.678-5")
    private String rut;
    
    @Schema(description = "Región de Chile donde reside el usuario", example = "RM - Región Metropolitana de Santiago", required = true)
    private String region;
    
    @Schema(description = "Comuna donde reside el usuario", example = "Santiago", required = true)
    private String comuna;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rol_id")
    @Schema(description = "Rol del usuario (CLIENTE, ADMIN, etc.)")
    private Rol rol;

    // Métodos explícitos para evitar problemas con Lombok en compilación
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }
}
