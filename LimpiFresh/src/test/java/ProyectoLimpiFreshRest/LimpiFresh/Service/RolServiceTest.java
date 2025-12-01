package ProyectoLimpiFreshRest.LimpiFresh.Service;

import ProyectoLimpiFreshRest.LimpiFresh.Modelo.Rol;
import ProyectoLimpiFreshRest.LimpiFresh.Repository.RolRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class RolServiceTest {

    @Mock
    RolRepository rolRepository;

    @InjectMocks
    RolService rolService;

    @Test
    void crearRolDebeGuardarRolNuevo() {
        when(rolRepository.findByNombreRol(anyString())).thenReturn(Optional.empty());
        Rol rolGuardado = new Rol(1, "ADMIN");
        when(rolRepository.save(any(Rol.class))).thenReturn(rolGuardado);
        Rol result = rolService.crearRol("admin");
        assertNotNull(result);
        assertEquals("ADMIN", result.getNombreRol());
        verify(rolRepository, times(1)).findByNombreRol("admin");
        verify(rolRepository, times(1)).save(any(Rol.class));
    }

    @Test
    void crearRolDebeLanzarErrorSiExiste() {
        when(rolRepository.findByNombreRol("cliente")).thenReturn(Optional.of(new Rol(1, "CLIENTE")));
        RuntimeException e = assertThrows(RuntimeException.class, () -> rolService.crearRol("cliente"));
        assertEquals("El rol ya existe", e.getMessage());
        verify(rolRepository, times(1)).findByNombreRol("cliente");
        verify(rolRepository, never()).save(any());
    }

    @Test
    void listarRolesDebeRetornarLista() {
        List<Rol> lista = Arrays.asList(
                new Rol(1, "ADMIN"),
                new Rol(2, "CLIENTE")
        );
        when(rolRepository.findAll()).thenReturn(lista);
        List<Rol> result = rolService.listarRoles();
        assertEquals(2, result.size());
        assertEquals("ADMIN", result.get(0).getNombreRol());
        assertEquals("CLIENTE", result.get(1).getNombreRol());
        verify(rolRepository, times(1)).findAll();
    }

    @Test
    void eliminarRolDebeEliminarSiExiste() {
        when(rolRepository.existsById(3)).thenReturn(true);
        rolService.eliminarRol(3);
        verify(rolRepository, times(1)).existsById(3);
        verify(rolRepository, times(1)).deleteById(3);
    }

    @Test
    void eliminarRolDebeDarErrorSiNoExiste() {
        when(rolRepository.existsById(5)).thenReturn(false);
        RuntimeException e = assertThrows(RuntimeException.class, () -> rolService.eliminarRol(5));
        assertEquals("El rol no existe", e.getMessage());
        verify(rolRepository, times(1)).existsById(5);
        verify(rolRepository, never()).deleteById(any());
    }
}
