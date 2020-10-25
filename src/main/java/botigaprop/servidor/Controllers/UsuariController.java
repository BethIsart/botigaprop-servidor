package botigaprop.servidor.Controllers;

import botigaprop.servidor.Exceptions.BadRequestException;
import botigaprop.servidor.Exceptions.UsuariNotFoundException;
import botigaprop.servidor.Models.*;
import botigaprop.servidor.Persistence.UsuariRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class UsuariController {
    private static final Logger log = LoggerFactory.getLogger(UsuariController.class.getName());
    private final UsuariRepository repository;
    private final Map<String, String> codisAccesAcreditats = new HashMap<>();

    public UsuariController(UsuariRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/registre")
    public String registrarUsuari(@RequestBody Usuari nouUsuari) {

        log.trace("Rebuda petició de registre d'usuari");

        ValidarCampsNouUsuari(nouUsuari);
        InicialitzarCampsNouUsuari(nouUsuari);

        repository.save(nouUsuari);

        log.info("Nou usuari registrat amb l'identificador " + nouUsuari.getIdUsuari());

        return "Usuari registrat correctament amb el id " + nouUsuari.getIdUsuari();
    }

    @GetMapping("/login")
    public String iniciarSessio(@RequestBody DadesAcces dadesAccesUsuari) {

        log.trace("Petició d'inici de sessió del usuari amb email " + dadesAccesUsuari.getEmail());

        ValidarCampsObligatorisPeticioLogin(dadesAccesUsuari);
        Usuari usuari = ValidarUsuariICredencials(dadesAccesUsuari);
        ValidarUsuariSenseAccesPrevi(usuari);
        ActualitzarDataUltimAcces(usuari);
        String codiAcces = GenerarCodiAcces(usuari);

        log.info("Retornat codi d'accés per l'usuari amb identificador "+ usuari.getIdUsuari() +" : " + codiAcces);

         return codiAcces;
    }

    @GetMapping("/logout/{codiAcces}")
    public String finalitzarSessio(@PathVariable String codiAcces) {

        log.trace("Petició de finalitzar sessió del codi " + codiAcces);

        ValidarCodiAcces(codiAcces);
        codisAccesAcreditats.remove(codiAcces);

        log.info("Sessió finalitzada correctament pel codi " + codiAcces);
        return "Sessió finalitzada";
    }

    @PutMapping("/canviarContrasenya/{codiAcces}")
    public String canviarContrasenya(@RequestBody PeticioCanviContrasenya peticio, @PathVariable String codiAcces)
    {
        log.trace("Petició de canvi de contrasenya del codi " + codiAcces);

        String idUsuari = ValidarCodiAcces(codiAcces);
        ValidarCampsObligatorisPeticioCanviarContrasenya(peticio);
        ActualitzarContrasenya(peticio, idUsuari);

        return "Contrasenya modificada";
    }

    @DeleteMapping("/baixa/{codiAcces}")
    public String deshabilitarUsuari(@RequestBody PeticioDeshabilitarUsuari peticio, @PathVariable String codiAcces)
    {
        log.trace("Petició de deshabilitar usuari amb identificador del codi "+ codiAcces);

        String idUsuari = ValidarCodiAcces(codiAcces);
        ValidarCampsObligatorisPeticioDeshabilitarUsuari(peticio);
        ValidarUsuariAmbPermisosAdministrador(idUsuari);
        DeshabilitarUsuari(peticio);

        return "Usuari deshabilitat";
    }

    @GetMapping("/usuaris/{codiAcces}")
    public List<Usuari> llistarUsuaris(@PathVariable String codiAcces) {

        log.trace("Petició de llistar usuaris del codi " + codiAcces);

        String idUsuari = ValidarCodiAcces(codiAcces);
        ValidarUsuariAmbPermisosAdministrador(idUsuari);

        List<Usuari> usuaris = repository.findAll();

        log.trace("Retornada llista d'usuaris");
        return usuaris;
    }

    private String ValidarCodiAcces(String codiAcces) {
        if (!codisAccesAcreditats.containsKey(codiAcces)){
            throw new BadRequestException("Codi d'accés no vàlid");
        }
        return codisAccesAcreditats.get(codiAcces);
    }

    private void InicialitzarCampsNouUsuari(Usuari nouUsuari) {
        nouUsuari.setIdUsuari(UUID.randomUUID().toString());
        nouUsuari.setDataCreacio(new Date());
        nouUsuari.setContrasenya(BCrypt.hashpw(nouUsuari.getContrasenya(), BCrypt.gensalt()));
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

        if (!repository.findUsuariByEmailAndDeshabilitatIsFalse(nouUsuari.getEmail()).isEmpty())
        {
            throw new BadRequestException("Ja existeix un usuari actiu amb aquest email");
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

    private void ValidarCampsObligatorisPeticioLogin(DadesAcces dadesAccesUsuari) {

        if (dadesAccesUsuari.getEmail() == null || dadesAccesUsuari.getEmail().isEmpty())
        {
            throw new BadRequestException("El camp email és obligatori");
        }

        if (dadesAccesUsuari.getContrasenya() == null || dadesAccesUsuari.getContrasenya().isEmpty())
        {
            throw new BadRequestException("El camp contrasenya és obligatori");
        }
    }

    private Usuari ValidarUsuariICredencials(DadesAcces dadesAccesUsuari) {

        List<Usuari> usuaris = repository.findUsuariByEmailAndDeshabilitatIsFalse(dadesAccesUsuari.getEmail());

        if (usuaris == null || usuaris.size() != 1)
        {
            throw new BadRequestException("L'usuari no existeix");
        }
        Usuari usuari = usuaris.get(0);

        if (!BCrypt.checkpw(dadesAccesUsuari.getContrasenya(), usuari.getContrasenya()))
        {
            throw new BadRequestException("La contrasenya no és vàlida");
        }

        if (usuari.isDeshabilitat())
        {
            throw new BadRequestException("L'usuari està deshabilitat");
        }

        return usuari;
    }

    private void ActualitzarDataUltimAcces(Usuari usuari) {
        usuari.setUltimAcces(new Date());
        repository.save(usuari);
    }

    private String GenerarCodiAcces(Usuari usuari) {
        String codiAcces = UUID.randomUUID().toString();
        codisAccesAcreditats.put(codiAcces, usuari.getIdUsuari());
        return codiAcces;
    }

    private void ValidarCampsObligatorisPeticioCanviarContrasenya(PeticioCanviContrasenya peticio) {
        if (peticio.getContrasenya() == null || peticio.getContrasenya().isEmpty())
        {
            throw new BadRequestException("El camp contrasenya és obligatori");
        }
    }

    private void ActualitzarContrasenya(PeticioCanviContrasenya peticio, String idUsuari) {
        Usuari usuari = repository.findByIdUsuari(idUsuari);
        usuari.setContrasenya(BCrypt.hashpw(peticio.getContrasenya(), BCrypt.gensalt()));
        repository.save(usuari);
        log.info("Contrasenya modificada correctament per l'usuari amb identificador " + usuari.getIdUsuari());
    }

    private void ValidarUsuariSenseAccesPrevi(Usuari usuari) {
        if (codisAccesAcreditats.containsValue(usuari.getIdUsuari()))
        {
            throw new BadRequestException("Aquest usuari ja te accés. Per obtenir un nou codi primer ha de finalitzar sessió");
        }
    }

    private void ValidarCampsObligatorisPeticioDeshabilitarUsuari(PeticioDeshabilitarUsuari peticio) {
        if (peticio.getIdUsuari() == null || peticio.getIdUsuari().isEmpty())
        {
            throw new BadRequestException("El camp id usuari és obligatori");
        }
    }

    private void ValidarUsuariAmbPermisosAdministrador(String idUsuari) {
        Usuari usuari = repository.findByIdUsuari(idUsuari);
        if (usuari.getRol() != Rol.ADMINISTRADOR)
        {
            throw new BadRequestException("Aquest usuari no té permís d'administrador");
        }
    }

    private void DeshabilitarUsuari(PeticioDeshabilitarUsuari peticio) {
        Usuari usuari = repository.findByIdUsuari(peticio.getIdUsuari());

        if (usuari == null)
        {
            throw new UsuariNotFoundException(peticio.getIdUsuari());
        }

        usuari.setDeshabilitat(true);
        repository.save(usuari);
        log.info("Deshabilitat l'usuari amb identificador "+usuari.getIdUsuari());
    }
}
