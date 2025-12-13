package ProyectoLimpiFreshRest.LimpiFresh.Modelo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "boleta_item")
public class BoletaItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer  id;
    @ManyToOne
    @JoinColumn(name = "boleta_id")
    @JsonIgnore
    private Boleta boleta;
    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;
    private String nombreProducto;
    private Integer cantidad;
    private Integer precioUnitario;
    private Boolean ofertaAplicada;
    private Integer precioOfertaUnitario;
}