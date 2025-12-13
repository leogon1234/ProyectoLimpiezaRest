package ProyectoLimpiFreshRest.LimpiFresh.Service;

import ProyectoLimpiFreshRest.LimpiFresh.Modelo.Rol;
import ProyectoLimpiFreshRest.LimpiFresh.Modelo.Usuario;
import ProyectoLimpiFreshRest.LimpiFresh.Repository.RolRepository;
import ProyectoLimpiFreshRest.LimpiFresh.Repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// UsuarioService.java
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, RolRepository rolRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario registrarCliente(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("El correo ya está registrado");
        }
        Rol rolCliente = rolRepository.findByNombreRol("CLIENTE")
                .orElseThrow(() -> new RuntimeException("Rol CLIENTE no existe"));

        usuario.setRol(rolCliente);

        // ✅ ENCRIPTAR
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        return usuarioRepository.save(usuario);
    }

    public Usuario registrarAdmin(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("El correo ya está registrado");
        }
        Rol rolAdmin = rolRepository.findByNombreRol("ADMIN")
                .orElseThrow(() -> new RuntimeException("Rol ADMIN no existe"));

        usuario.setRol(rolAdmin);

        // ✅ ENCRIPTAR
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        return usuarioRepository.save(usuario);
    }
}
