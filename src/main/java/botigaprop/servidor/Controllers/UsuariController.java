package botigaprop.servidor.Controllers;

import botigaprop.servidor.Exceptions.BadRequestException;
import botigaprop.servidor.Models.Rol;
import botigaprop.servidor.Models.Usuari;
import botigaprop.servidor.Persistence.UsuariRepository;
import botigaprop.servidor.Services.GestorContrasenyes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

@RestController
public class UsuariController {
    private static final Logger log = LoggerFactory.getLogger(UsuariController.class.getName());
    private final UsuariRepository repository;
    private final GestorContrasenyes gestorContrasenyes;

    public UsuariController(UsuariRepository repository) {
        this.repository = repository;
        this.gestorContrasenyes = new GestorContrasenyes();
    }

    @PostMapping("/registre")
    public String registrarUsuari(@RequestBody Usuari nouUsuari) {

        log.trace("Rebuda petició de registre d'usuari");

        ValidarCampsNouUsuari(nouUsuari);
        InicialitzarCampsNouUsuari(nouUsuari);

        nouUsuari.setContrasenya(gestorContrasenyes.EncriptarContrasenya(nouUsuari.getContrasenya()));
        //TODO gestionar error si falla l'encriptació

        repository.save(nouUsuari);

        log.trace("Usuari registrat amb l'identificador " + nouUsuari.getIdUsuari());

        return "Usuari registrat correctament amb el id " + nouUsuari.getIdUsuari();
    }

    private void InicialitzarCampsNouUsuari(Usuari nouUsuari) {
        nouUsuari.setIdUsuari(UUID.randomUUID().toString());
        nouUsuari.setDataCreacio(new Date());
    }

    private void ValidarCampsNouUsuari(Usuari nouUsuari) {

        if (nouUsuari.getEmail() == null || nouUsuari.getEmail().isEmpty())
        {
            throw new BadRequestException("El camp email és obligatori");
        }

        if (nouUsuari.getContrasenya() == null || nouUsuari.getContrasenya().isEmpty())
        {
            throw new BadRequestException("El camp contrasenya és obligatori");
        }

        if (nouUsuari.getRol() == null)
        {
            throw new BadRequestException("El camp rol és obligatori");
        }

        if (nouUsuari.getRol() == Rol.PROVEIDOR)
        {
            ValidarCampsObligatorisPerProveidors(nouUsuari);
        }
    }

    private void ValidarCampsObligatorisPerProveidors(Usuari nouProveidor) {

        if (nouProveidor.getNom() == null || nouProveidor.getNom().isEmpty())
        {
            throw new BadRequestException("El camp nom és obligatori");
        }

        if (nouProveidor.getCifEmpresa() == null || nouProveidor.getCifEmpresa().isEmpty())
        {
            throw new BadRequestException("El camp CIF és obligatori");
        }

        if (nouProveidor.getDireccio() == null || nouProveidor.getDireccio().isEmpty())
        {
            throw new BadRequestException("El camp direcció és obligatori");
        }

        if (nouProveidor.getPoblacio() == null || nouProveidor.getPoblacio().isEmpty())
        {
            throw new BadRequestException("El camp població és obligatori");
        }

        if (nouProveidor.getProvincia() == null || nouProveidor.getProvincia().isEmpty())
        {
            throw new BadRequestException("El camp provincia és obligatori");
        }
    }


    //TODO set last access
}
