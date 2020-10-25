package botigaprop.servidor.Controllers;

import botigaprop.servidor.Exceptions.BadRequestException;
import botigaprop.servidor.Models.DadesAcces;
import botigaprop.servidor.Models.PeticioCanviContrasenya;
import botigaprop.servidor.Models.Rol;
import botigaprop.servidor.Models.Usuari;
import botigaprop.servidor.Persistence.UsuariRepository;
import botigaprop.servidor.Services.GestorContrasenyes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class UsuariController {
    private static final Logger log = LoggerFactory.getLogger(UsuariController.class.getName());
    private final UsuariRepository repository;
    private final GestorContrasenyes gestorContrasenyes;

    private Map<String, String> codisAccesAcreditats = new HashMap<>();

    public UsuariController(UsuariRepository repository) {
        this.repository = repository;
        this.gestorContrasenyes = new GestorContrasenyes();
    }

    @PostMapping("/registre")
    public String registrarUsuari(@RequestBody Usuari nouUsuari) {

        log.trace("Rebuda petició de registre d'usuari");

        ValidarCampsNouUsuari(nouUsuari);
        InicialitzarCampsNouUsuari(nouUsuari);

        //nouUsuari.setContrasenya(gestorContrasenyes.EncriptarContrasenya(nouUsuari.getContrasenya()));
        //TODO gestionar error si falla l'encriptació
        //TODO no permetre usuaris amb el mateix email?
        //TODO canviar tipo petició i fer map a BD

        repository.save(nouUsuari);

        log.trace("Usuari registrat amb l'identificador " + nouUsuari.getIdUsuari());

        return "Usuari registrat correctament amb el id " + nouUsuari.getIdUsuari();
    }

    @GetMapping("/login")
    public String iniciarSessio(@RequestBody DadesAcces dadesAccesUsuari) {

        //TODO revisar si ja te codi d'acces
        log.trace("Petició de inici de sessió del usuari amb email " + dadesAccesUsuari.getEmail());
        ValidarCampsIniciSessio(dadesAccesUsuari);

        //String contrasenyaEncriptada = gestorContrasenyes.EncriptarContrasenya(dadesAccesUsuari.getContrasenya());
        List<Usuari> usuaris = repository.findUsuariByEmailAndContrasenya(dadesAccesUsuari.getEmail(), dadesAccesUsuari.getContrasenya());

        if (usuaris == null || usuaris.size() != 1)
        {
            throw new BadRequestException("L'usuari no existeix");
            //TODO gestionar error més d'un usuari
        }

        Usuari usuari = usuaris.get(0);
        if (usuari.isDeshabilitat())
        {
            throw new BadRequestException("L'usuari està deshabilitat");
        }

        usuari.setUltimAcces(new Date());
        repository.save(usuari);

        String codiAcces = UUID.randomUUID().toString();
        codisAccesAcreditats.put(codiAcces, usuari.getIdUsuari());

         return codiAcces;
    }

    @GetMapping("/logout/{codiAcces}")
    public String finalitzarSessio(@PathVariable String codiAcces) {
        ValidarCodiAcces(codiAcces);

        codisAccesAcreditats.remove(codiAcces);

        return "Sessió finalitzada";
    }

    @PutMapping("/canviarContrasenya/{codiAcces}")
    public String canviarContrasenya(@RequestBody PeticioCanviContrasenya peticio, @PathVariable String codiAcces)
    {
        //TODO revisar restriccions
        String idUsuari = ValidarCodiAcces(codiAcces);
        Usuari usuari = repository.findByIdUsuari(idUsuari);

        //TODO encriptar contrasenya nova
        usuari.setContrasenya(peticio.getContrasenya());
        repository.save(usuari);

        return "Contrassenya modificada";
    }

    @DeleteMapping("/baixa/{codiAcces}")
    public String deshabilitarUsuari( @PathVariable String codiAcces)
    {
        //TODO revisar restriccions
        String idUsuari = ValidarCodiAcces(codiAcces);
        Usuari usuari = repository.findByIdUsuari(idUsuari);

        usuari.setDeshabilitat(true);
        repository.save(usuari);

        return "Usuari deshabilitat";
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

    private void ValidarCampsIniciSessio(DadesAcces dadesAccesUsuari) {

        if (dadesAccesUsuari.getEmail() == null || dadesAccesUsuari.getEmail().isEmpty())
        {
            throw new BadRequestException("El camp email és obligatori");
        }

        if (dadesAccesUsuari.getContrasenya() == null || dadesAccesUsuari.getContrasenya().isEmpty())
        {
            throw new BadRequestException("El camp contrasenya és obligatori");
        }
    }

}
