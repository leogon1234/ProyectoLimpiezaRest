package ProyectoLimpiFreshRest.LimpiFresh.Dto;

import lombok.Data;
import java.util.List;

@Data
public class CrearBoletaRequest {
    private Integer usuarioId;        // opcional
    private String nombreCliente;
    private String direccion;
    private String ciudad;
    private String telefono;
    private List<ItemBoletaRequest> items;
}