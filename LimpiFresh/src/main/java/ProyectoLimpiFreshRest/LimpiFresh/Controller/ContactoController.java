package ProyectoLimpiFreshRest.LimpiFresh.Controller;

import ProyectoLimpiFreshRest.LimpiFresh.Modelo.Contacto;
import ProyectoLimpiFreshRest.LimpiFresh.Service.ContactoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contacto")
public class ContactoController {

    private final ContactoService contactoService;

    public ContactoController(ContactoService service) {
        this.contactoService = service;
    }

    @PostMapping
    public ResponseEntity<Contacto> enviarMensaje(@RequestBody Contacto contacto) {
        Contacto guardado = contactoService.guardarMensaje(contacto);
        return ResponseEntity.ok(guardado);
    }
}