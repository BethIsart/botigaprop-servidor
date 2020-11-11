package botigaprop.servidor.Controllers;

import botigaprop.servidor.Models.PeticioAltraProducte;
import botigaprop.servidor.Models.Producte;
import botigaprop.servidor.Models.Usuari;
import botigaprop.servidor.Persistence.ProducteRepository;
import botigaprop.servidor.Persistence.UsuariRepository;
import botigaprop.servidor.Services.ControlAcces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

/**
 * @author Elisabet Isart
 */
@RestController
public class ProducteController {
    private static final Logger log = LoggerFactory.getLogger(UsuariController.class.getName());
    private final UsuariRepository usuariRepository;
    private final ProducteRepository producteRepository;
    private final ControlAcces controlAcces;

    public ProducteController(UsuariRepository usuariRepository, ProducteRepository producteRepository, ControlAcces controlAcces) {
        this.usuariRepository = usuariRepository;
        this.producteRepository = producteRepository;
        this.controlAcces = controlAcces;
    }

    @PostMapping("/altaproducte")
    public String altaProducte(@RequestBody PeticioAltraProducte altraProducte) {

        log.trace("Rebuda petició de alta de producte");

        String idUsuari = controlAcces.ValidarCodiAcces(altraProducte.getCodiAcces());
        //Validar si categoria no existeix
        //Validar tipu usuari
        //ValidarCampsNouProducte(nouProducte);
        InicialitzarCampsNouProducte(altraProducte.getProducte(), idUsuari);

        producteRepository.save(altraProducte.getProducte());

        log.info("Producte donat d'alta amb l'identificador " + altraProducte.getProducte().getIdProducte());

        return "Producte donat d'alta amb l'identificador " + altraProducte.getProducte().getIdProducte();
    }

    private void InicialitzarCampsNouProducte(Producte nouProducte, String idUsuari) {
        Usuari usuari = usuariRepository.findByIdUsuari(idUsuari);
        nouProducte.setIdUsuari(usuari);
        nouProducte.setIdProducte(UUID.randomUUID().toString());
        nouProducte.setDataCreacio(new Date());

        if (nouProducte.isDisponible() == null)
        {
            nouProducte.setDisponible(true);
        }
    }
}
