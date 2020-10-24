package botigaprop.servidor.Controllers;

import botigaprop.servidor.Models.Usuari;
import botigaprop.servidor.Persistence.UsuariRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsuariController {
    private final UsuariRepository repository;

    public UsuariController(UsuariRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/registre")
    public String registrarUsuari(@RequestBody Usuari nouUsuari) {
        repository.save(nouUsuari);
        return "Usuari registrat correctament amb el id " + nouUsuari.getIdUsuari();
    }

    //TODO Add logs
    //TODO crypt psw
    //TODO set last access
}
