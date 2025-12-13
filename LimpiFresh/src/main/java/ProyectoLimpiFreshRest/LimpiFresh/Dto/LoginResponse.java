package ProyectoLimpiFreshRest.LimpiFresh.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private boolean admin;
    private Integer userId;
    private String email;
}
