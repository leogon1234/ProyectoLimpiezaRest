package ProyectoLimpiFreshRest.LimpiFresh.Modelo;

import jakarta.persistence.*;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "producto")
@Schema(description = "Modelo de producto de limpieza")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del producto (generado automáticamente)", example = "1")
    private Integer  id;
    
    @Schema(description = "Nombre del producto", example = "Detergente Premium", required = true)
    private String nombre;
    
    @Column(name = "descripcion_corta")
    @Schema(description = "Descripción breve del producto", example = "Detergente concentrado para ropa blanca y de color")
    private String descripcionCorta;
    
    @Column(name = "descripcion_larga", length = 2000)
    @Schema(description = "Descripción detallada del producto (máx 2000 caracteres)", example = "Detergente concentrado con tecnología avanzada...")
    private String descripcionLarga;
    
    @Schema(description = "Precio regular del producto en pesos chilenos", example = "5990", required = true)
    private Integer precio;
    
    @Schema(description = "Indica si el producto está en oferta", example = "true")
    private Boolean oferta = false;
    
    @Schema(description = "Precio con descuento cuando oferta=true", example = "4490")
    private Integer precioOferta;
    
    @Schema(description = "Categoría del producto", example = "Detergentes")
    private String categoria;
    
    @Schema(description = "URL o ruta de la imagen del producto", example = "/img/detergente.jpg")
    private String img;
    
    @Schema(description = "Cantidad disponible en stock", example = "50", required = true)
    private Integer stock;
    
    @Schema(description = "Porcentaje de IVA aplicado", example = "19")
    private Integer iva;

    // Setter explícito para evitar problemas con Lombok al compilar
    public void setId(Integer id) {
        this.id = id;
    }
}

