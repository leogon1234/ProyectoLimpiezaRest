package ProyectoLimpiFreshRest.LimpiFresh.Service;

import ProyectoLimpiFreshRest.LimpiFresh.Modelo.AuthResponse;
import ProyectoLimpiFreshRest.LimpiFresh.Modelo.LoginRequest;
import ProyectoLimpiFreshRest.LimpiFresh.Modelo.Usuario;
import ProyectoLimpiFreshRest.LimpiFresh.Repository.UsuarioRepository;
import ProyectoLimpiFreshRest.LimpiFresh.Security.JwtService;
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
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        String jwtToken = jwtService.generateToken(usuarioGuardado);

        boolean isAdmin = usuarioGuardado.getRol() != null
                && "ADMIN".equalsIgnoreCase(usuarioGuardado.getRol().getNombreRol());
        return new AuthResponse(jwtToken, isAdmin, usuarioGuardado.getNombre());
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        Usuario user = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String jwtToken = jwtService.generateToken(user);

        boolean isAdmin = user.getRol() != null
                && "ADMIN".equalsIgnoreCase(user.getRol().getNombreRol());

        return new AuthResponse(jwtToken, isAdmin, user.getNombre());
    }
}
