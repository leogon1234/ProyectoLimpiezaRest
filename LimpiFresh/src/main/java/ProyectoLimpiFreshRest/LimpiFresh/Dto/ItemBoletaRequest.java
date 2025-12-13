package ProyectoLimpiFreshRest.LimpiFresh.Dto;

import lombok.Data;

@Data
public class ItemBoletaRequest {
    private Integer productoId;
    private Integer cantidad;
}