package ProyectoLimpiFreshRest.LimpiFresh;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "API LimpiFresh",
                version = "1.0.0",
                description = "API REST para gestión de productos de limpieza LimpiFresh. " +
                        "Proporciona endpoints para productos, usuarios, autenticación, boletas, contacto y blogs.",
                contact = @Contact(
                        name = "LimpiFresh",
                        email = "contacto@limpifresh.cl"
                ),
                license = @io.swagger.v3.oas.annotations.info.License(
                        name = "MIT License"
                )
        ),
        servers = {
                @Server(
                        url = "http://localhost:8080",
                        description = "Servidor de desarrollo local"
                )
        },
        tags = {
                @io.swagger.v3.oas.annotations.tags.Tag(name = "Productos", description = "Gestión de productos de limpieza"),
                @io.swagger.v3.oas.annotations.tags.Tag(name = "Autenticación", description = "Registro y login de usuarios"),
                @io.swagger.v3.oas.annotations.tags.Tag(name = "Boletas", description = "Gestión de boletas de compra"),
                @io.swagger.v3.oas.annotations.tags.Tag(name = "Contacto", description = "Mensajes de contacto"),
                @io.swagger.v3.oas.annotations.tags.Tag(name = "Blogs", description = "Gestión de entradas de blog"),
                @io.swagger.v3.oas.annotations.tags.Tag(name = "Roles", description = "Gestión de roles de usuario")
        }
)
public class LimpiFreshApplication {

    public static void main(String[] args) {
        SpringApplication.run(LimpiFreshApplication.class, args);
    }


    @org.springframework.context.annotation.Bean
    public org.springframework.boot.CommandLineRunner ascenderAdmin(org.springframework.jdbc.core.JdbcTemplate template) {
        return args -> {
            // Buscamos al usuario por su email y le ponemos ROL_ID = 1 (ADMIN)
            String sql = "UPDATE USUARIO SET ROL_ID = 1 WHERE EMAIL = 'admin@limpifresh.cl'";

            try {
                int filasAfectadas = template.update(sql);
                if (filasAfectadas > 0) {
                    System.out.println(" ¡ÉXITO! El usuario admin@limpifresh.cl ahora es ADMINISTRADOR (Rol 1).");
                } else {
                    System.out.println("⚠ No se encontró el usuario 'admin@limpifresh.cl'. ¿Ya lo creaste en el Registro?");
                }
            } catch (Exception e) {
                System.out.println("⚠ Error intentando actualizar admin: " + e.getMessage());
            }
        };
    }
}