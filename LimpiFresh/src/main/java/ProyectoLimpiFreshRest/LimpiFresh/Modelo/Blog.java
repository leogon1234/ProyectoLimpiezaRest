package ProyectoLimpiFreshRest.LimpiFresh.Modelo;

import jakarta.persistence.*;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "blog")
@Schema(description = "Modelo de entrada de blog")
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del blog", example = "1")
    private Integer  id;
    
    @Schema(description = "Título de la entrada de blog", example = "Consejos para una limpieza eficiente", required = true)
    private String titulo;
    
    @Column(length = 4000)
    @Schema(description = "Contenido completo del blog (máx 4000 caracteres)", example = "En este artículo te enseñamos...", required = true)
    private String contenido;
    
    @Column(name = "imagen_url", length = 1000)
    @Schema(description = "URL o ruta de la imagen asociada al blog", example = "/img/blog/limpieza.jpg")
    private String imagenUrl;

    // Setter explícito para evitar problemas con Lombok al compilar
    public void setId(Integer id) {
        this.id = id;
    }
}