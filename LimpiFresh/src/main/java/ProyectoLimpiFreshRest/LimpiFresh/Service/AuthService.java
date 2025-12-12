package ProyectoLimpiFreshRest.LimpiFresh.Service;

import ProyectoLimpiFreshRest.LimpiFresh.Config.JwtService;
import ProyectoLimpiFreshRest.LimpiFresh.Modelo.AuthResponse;
import ProyectoLimpiFreshRest.LimpiFresh.Modelo.LoginRequest;
import ProyectoLimpiFreshRest.LimpiFresh.Modelo.Usuario;
import ProyectoLimpiFreshRest.LimpiFresh.Repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse registrar(Usuario usuario) {
        // 1. Encriptar contraseña
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        // 2. Guardar usuario
        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        // 3. Generar Token
        String jwtToken = jwtService.generateToken(usuarioGuardado);

        return new AuthResponse(jwtToken, false, usuarioGuardado.getNombre());
    }

    public AuthResponse login(LoginRequest request) {
        // 1. Autenticar (Spring valida usuario y contraseña por nosotros)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // 2. Buscar usuario en BD
        Usuario user = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 3. Generar Token
        String jwtToken = jwtService.generateToken(user);

        // 4. Verificar si es admin
        boolean isAdmin = user.getRol() != null && "ADMIN".equalsIgnoreCase(user.getRol().getNombreRol());

        return new AuthResponse(jwtToken, isAdmin, user.getNombre());
    }
}