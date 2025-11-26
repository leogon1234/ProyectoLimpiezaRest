package ProyectoLimpiFreshRest.LimpiFresh.Service;

import ProyectoLimpiFreshRest.LimpiFresh.Modelo.Contacto;
import ProyectoLimpiFreshRest.LimpiFresh.Repository.ContactoRepository;
import org.springframework.stereotype.Service;

@Service
public class ContactoService {

    private final ContactoRepository contactoRepository;

    public ContactoService(ContactoRepository repo) {
        this.contactoRepository = repo;
    }

    public Contacto guardarMensaje(Contacto contacto) {
        return contactoRepository.save(contacto);
    }
}
