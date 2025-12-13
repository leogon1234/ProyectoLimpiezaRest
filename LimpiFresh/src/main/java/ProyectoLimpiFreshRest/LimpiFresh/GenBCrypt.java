package ProyectoLimpiFreshRest.LimpiFresh;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenBCrypt {
    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("admin123"));
    }
}