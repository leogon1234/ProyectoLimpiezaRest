package ProyectoLimpiFreshRest.LimpiFresh.Controller;

import ProyectoLimpiFreshRest.LimpiFresh.Config.JwtService;
import ProyectoLimpiFreshRest.LimpiFresh.Modelo.AuthResponse;
import ProyectoLimpiFreshRest.LimpiFresh.Modelo.LoginRequest;
import ProyectoLimpiFreshRest.LimpiFresh.Modelo.Usuario;
import ProyectoLimpiFreshRest.LimpiFresh.Repository.UsuarioRepository;
import ProyectoLimpiFreshRest.LimpiFresh.Service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@io.swagger.v3.oas.annotations.tags.Tag(name = "Autenticación", description = "Registro y autenticación de usuarios")
public class AuthController {

    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;

    // --- DEPENDENCIAS PARA SEGURIDAD ---
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Operation(
            summary = "Registrar nuevo usuario",
            description = "Registra un nuevo usuario en el sistema con rol CLIENTE. " +
                    "La contraseña se guardará encriptada y se devolverá un Token JWT para acceso inmediato."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario registrado correctamente. Devuelve el Token de acceso.",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error de validación: email ya registrado o datos inválidos"
            )
    })
    @PostMapping("/registro")
    public ResponseEntity<?> registrar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del usuario. Rol se asigna automático.",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Usuario.class))
            )
            @RequestBody Usuario usuario) {
        try {
            // 1. Encriptamos la contraseña
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

            // 2. Guardamos el usuario (Cliente por defecto)
            Usuario creado = usuarioService.registrarCliente(usuario);

            // 3. Generamos el token JWT
            String jwtToken = jwtService.generateToken(creado);

            // 4. Devolvemos token, false (no es admin) y el NOMBRE
            return ResponseEntity.ok(new AuthResponse(jwtToken, false, creado.getNombre()));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @Operation(
            summary = "Iniciar sesión",
            description = "Autentica mediante email y contraseña. Devuelve un Token JWT si es exitoso."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Login exitoso. Devuelve el Token JWT.",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Credenciales incorrectas."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado."
            )
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Credenciales (Email y Password)",
                    required = true,
                    content = @Content(schema = @Schema(implementation = LoginRequest.class))
            )
            @RequestBody LoginRequest request) {

        // 1. Autenticación automática de Spring Security
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // 2. Buscamos al usuario
        Usuario user = usuarioRepository.findByEmail(request.getEmail()).orElseThrow();

        // 3. Generamos el token
        String jwtToken = jwtService.generateToken(user);

        // 4. Verificamos si es ADMIN
        boolean isAdmin = user.getRol() != null &&
                "ADMIN".equalsIgnoreCase(user.getRol().getNombreRol());

        // 5. Devolvemos token, rol y el NOMBRE real de la base de datos
        return ResponseEntity.ok(new AuthResponse(jwtToken, isAdmin, user.getNombre()));
    }

    @Operation(
            summary = "Crear usuario administrador",
            description = "Crea un nuevo usuario con rol ADMIN y devuelve su Token."
    )
    @PostMapping("/crear-admin")
    public ResponseEntity<?> crearAdmin(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del admin a crear",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Usuario.class))
            )
            @RequestBody Usuario usuario) {
        try {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            Usuario creado = usuarioService.registrarAdmin(usuario);
            String jwtToken = jwtService.generateToken(creado);

            // Devolvemos token, true (es admin) y el NOMBRE
            return ResponseEntity.ok(new AuthResponse(jwtToken, true, creado.getNombre()));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}