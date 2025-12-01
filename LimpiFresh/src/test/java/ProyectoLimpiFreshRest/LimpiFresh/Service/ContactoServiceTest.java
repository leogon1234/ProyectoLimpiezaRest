package ProyectoLimpiFreshRest.LimpiFresh.Service;

import ProyectoLimpiFreshRest.LimpiFresh.Modelo.Contacto;
import ProyectoLimpiFreshRest.LimpiFresh.Repository.ContactoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactoServiceTest {

    @Mock
    ContactoRepository contactoRepository;

    @InjectMocks
    ContactoService contactoService;

    @Test
    void guardarMensajeDebeGuardarCorrectamente() {
        Contacto contacto = new Contacto();
        contacto.setNombre("Juan");
        contacto.setEmail("juan@mail.com");
        contacto.setAsunto("Consulta general");
        contacto.setMensaje("Quiero saber el estado de mi pedido");
        when(contactoRepository.save(contacto)).thenReturn(contacto);
        Contacto guardado = contactoService.guardarMensaje(contacto);
        assertNotNull(guardado);
        assertEquals("Juan", guardado.getNombre());
        verify(contactoRepository, times(1)).save(contacto);
    }

    @Test
    void buscarPorIdContacto() {
        Contacto contacto = new Contacto();
        contacto.setId(1);
        contacto.setNombre("Luisa");
        contacto.setEmail("l@mail.com");
        contacto.setAsunto("Asunto X");
        contacto.setMensaje("Mensaje X");
        when(contactoRepository.findById(1)).thenReturn(Optional.of(contacto));
        Optional<Contacto> result = contactoService.buscarPorId(1);
        assertTrue(result.isPresent());
        assertEquals("Luisa", result.get().getNombre());
        verify(contactoRepository, times(1)).findById(1);
    }

    @Test
    void eliminarContactoPorId() {
        contactoService.eliminar(3);
        verify(contactoRepository, times(1)).deleteById(3);
    }
}
