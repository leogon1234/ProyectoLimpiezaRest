package ProyectoLimpiFreshRest.LimpiFresh.Modelo;
import jakarta.persistence.*;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "boleta")
@Schema(description = "Modelo de boleta de compra")
public class Boleta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la boleta", example = "1")
    private Integer  id;
    
    @Column(name = "numero_boleta", unique = true)
    @Schema(description = "Número único de la boleta (ej: INV-1234567890)", example = "INV-1234567890", required = true)
    private String numeroBoleta;
    
    @Schema(description = "Fecha y hora de emisión de la boleta", example = "2025-01-15T10:30:00")
    private LocalDateTime fechaBoleta = LocalDateTime.now();
    
    @Schema(description = "Nombre completo del cliente", example = "Juan Pérez", required = true)
    private String nombreCliente;
    
    @Schema(description = "Dirección de entrega", example = "Av. Providencia 123", required = true)
    private String direccion;
    
    @Schema(description = "Ciudad de entrega", example = "Santiago", required = true)
    private String ciudad;
    
    @Schema(description = "Teléfono de contacto del cliente", example = "+56912345678")
    private String telefono;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    @Schema(description = "Usuario asociado a la boleta (opcional)")
    private Usuario usuario;
    
    @Schema(description = "Subtotal sin IVA en pesos chilenos", example = "10000", required = true)
    private Integer subtotal;
    
    @Schema(description = "Monto del IVA en pesos chilenos", example = "1900", required = true)
    private Integer iva;
    
    @Schema(description = "Total a pagar (subtotal + IVA) en pesos chilenos", example = "11900", required = true)
    private Integer total;
    
    @OneToMany(mappedBy = "boleta", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "Lista de items/productos incluidos en la boleta")
    private List<BoletaItem> items;
}
