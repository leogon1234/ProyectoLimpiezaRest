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
@RequiredArgsConstructor // Esto genera el constructor automáticamente para todas las dependencias 'final'
@io.swagger.v3.oas.annotations.tags.Tag(name = "Autenticación", description = "Registro y autenticación de usuarios")
public class AuthController {

    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;

    // --- NUEVAS DEPENDENCIAS PARA SEGURIDAD ---
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
            // 1. Encriptamos la contraseña antes de pasarla al servicio
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

            // 2. Guardamos el usuario
            Usuario creado = usuarioService.registrarCliente(usuario);

            // 3. Generamos el token JWT
            String jwtToken = jwtService.generateToken(creado);

            // 4. Devolvemos el token
            return ResponseEntity.ok(new AuthResponse(jwtToken));
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
            @RequestBody LoginRequest request) { // Usamos LoginRequest que es más limpio

        // 1. Autenticación automática de Spring Security (valida pass encriptada)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // 2. Buscamos al usuario para generar el token
        Usuario user = usuarioRepository.findByEmail(request.getEmail()).orElseThrow();

        // 3. Generamos el token
        String jwtToken = jwtService.generateToken(user);

        // 4. Devolvemos el token
        return ResponseEntity.ok(new AuthResponse(jwtToken));
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
            // 1. Encriptar contraseña
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

            // 2. Guardar
            Usuario creado = usuarioService.registrarAdmin(usuario);

            // 3. Generar Token
            String jwtToken = jwtService.generateToken(creado);

            return ResponseEntity.ok(new AuthResponse(jwtToken));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}