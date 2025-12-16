package ProyectoLimpiFreshRest.LimpiFresh.Controller;

import ProyectoLimpiFreshRest.LimpiFresh.Dto.LoginRequest;
import ProyectoLimpiFreshRest.LimpiFresh.Dto.LoginResponse;
import ProyectoLimpiFreshRest.LimpiFresh.Modelo.Usuario;
import ProyectoLimpiFreshRest.LimpiFresh.Repository.UsuarioRepository;
import ProyectoLimpiFreshRest.LimpiFresh.Security.JwtService;
import ProyectoLimpiFreshRest.LimpiFresh.Service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@CrossOrigin(origins = "http://limpifresh-pagina.s3-website-us-east-1.amazonaws.com")
@RestController
@RequestMapping("/api/auth")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Autenticación", description = "Registro y autenticación de usuarios")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final UsuarioService usuarioService;

    public AuthController(AuthenticationManager authenticationManager,
                          UsuarioRepository usuarioRepository,
                          JwtService jwtService,
                          UsuarioService usuarioService) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
            );

            Usuario u = usuarioRepository.findByEmail(req.getEmail())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            boolean isAdmin = u.getRol() != null && "ADMIN".equalsIgnoreCase(u.getRol().getNombreRol());

            String token = jwtService.generateToken(
                    u,
                    Map.of(
                            "role", (u.getRol() != null ? u.getRol().getNombreRol() : "CLIENTE"),
                            "admin", isAdmin,
                            "userId", u.getId()
                    )
            );

            return ResponseEntity.ok(new LoginResponse(token, isAdmin, u.getId(), u.getEmail()));

        } catch (Exception e) {
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registro(@RequestBody Usuario usuario) {
        try {
            Usuario creado = usuarioService.registrarCliente(usuario);
            return ResponseEntity.ok(creado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}