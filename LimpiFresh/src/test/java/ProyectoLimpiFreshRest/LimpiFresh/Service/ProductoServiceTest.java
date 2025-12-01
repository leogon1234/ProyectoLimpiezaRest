package ProyectoLimpiFreshRest.LimpiFresh.Service;

import ProyectoLimpiFreshRest.LimpiFresh.Modelo.Producto;
import ProyectoLimpiFreshRest.LimpiFresh.Repository.ProductoRepository;
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
class ProductoServiceTest {

    @Mock
    ProductoRepository productoRepository;

    @InjectMocks
    ProductoService productoService;

    @Test
    void guardarProducto() {
        Producto p = new Producto();
        p.setNombre("Detergente");
        p.setPrecio(5990);
        p.setStock(10);
        when(productoRepository.save(p)).thenReturn(p);
        Producto resultado = productoService.guardar(p);
        assertNotNull(resultado);
        assertEquals("Detergente", resultado.getNombre());
        verify(productoRepository, times(1)).save(p);
    }

    @Test
    void listarTodosDebeRetornarLista() {
        Producto p1 = new Producto(1,"D1","descC","descL",5000,false,null,"Cat1","img1",20,19);
        Producto p2 = new Producto(2,"D2","descC2","descL2",4000,false,null,"Cat2","img2",15,19);
        List<Producto> lista = Arrays.asList(p1,p2);
        when(productoRepository.findAll()).thenReturn(lista);
        List<Producto> result = productoService.listarTodos();
        assertEquals(2, result.size());
        verify(productoRepository, times(1)).findAll();
    }

    @Test
    void listarOfertasDebeRetornarSoloProductosConOfertaTrue() {
        Producto p1 = new Producto(1,"Detergente","corta1","larga1",5000,true,4500,"Limpieza","img",10,19);
        Producto p2 = new Producto(2,"Cloro","corta2","larga2",2000,true,1500,"Limpieza","img2",20,19);
        when(productoRepository.findByOfertaTrue()).thenReturn(Arrays.asList(p1,p2));
        List<Producto> result = productoService.listarOfertas();
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(Producto::getOferta));
        verify(productoRepository, times(1)).findByOfertaTrue();
    }

    @Test
    void buscarPorId() {
        Producto p = new Producto();
        p.setId(1);
        p.setNombre("Quitamanchas");
        when(productoRepository.findById(1)).thenReturn(Optional.of(p));
        Optional<Producto> result = productoService.buscarPorId(1);
        assertTrue(result.isPresent());
        assertEquals("Quitamanchas", result.get().getNombre());
        verify(productoRepository,times(1)).findById(1);
    }

    @Test
    void eliminarPorId() {
        productoService.eliminar(5);
        verify(productoRepository,times(1)).deleteById(5);
    }
}
