package botigaprop.servidor.Controllers;

import botigaprop.servidor.Exceptions.BadRequestException;
import botigaprop.servidor.Exceptions.ProducteNotFoundException;
import botigaprop.servidor.Models.*;
import botigaprop.servidor.Persistence.ProducteRepository;
import botigaprop.servidor.Persistence.UsuariRepository;
import botigaprop.servidor.Services.ControlAcces;
import botigaprop.servidor.Services.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
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
    private final Mapper mapper;

    public ProducteController(UsuariRepository usuariRepository, ProducteRepository producteRepository, ControlAcces controlAcces, Mapper mapper) {
        this.usuariRepository = usuariRepository;
        this.producteRepository = producteRepository;
        this.controlAcces = controlAcces;
        this.mapper = mapper;
    }

    @PostMapping("/altaproducte")
    public String altaProducte(@RequestBody PeticioAltraProducte altraProducte) {

        log.trace("Rebuda petició d'alta de producte");

        String idUsuari = controlAcces.ValidarCodiAcces(altraProducte.getCodiAcces());
        ValidarUsuariProveidor(idUsuari);
        ValidarCampsNouProducte(altraProducte.getProducte());
        InicialitzarCampsNouProducte(altraProducte.getProducte(), idUsuari);

        producteRepository.save(altraProducte.getProducte());

        log.info("Producte donat d'alta amb l'identificador " + altraProducte.getProducte().getIdProducte());

        return "Producte donat d'alta amb l'identificador " + altraProducte.getProducte().getIdProducte();
    }

    @GetMapping("/productes/{codiAcces}")
    public List<ProducteVisualitzacio> llistarProductes(@PathVariable String codiAcces) {

        log.trace("Petició de llistar productes del codi " + codiAcces);

        String idUsuari = controlAcces.ValidarCodiAcces(codiAcces);
        Usuari usuari = usuariRepository.findByIdUsuari(idUsuari);

        List<Producte> productes = producteRepository.findProducteByIdUsuariAndEliminatIsFalse(usuari);
        List<ProducteVisualitzacio> productesAMostrar = mapper.ProductesAMostrar(productes);

        log.trace("Retornada llista de productes");
        return productesAMostrar;
    }

    @DeleteMapping("/baixaproducte/{codiAcces}")
    public String baixaProducte(@RequestBody PeticioBaixaProducte peticio, @PathVariable String codiAcces)
    {
        log.trace("Petició de baixa de producte del codi "+ codiAcces);

        String idUsuari = controlAcces.ValidarCodiAcces(codiAcces);
        ValidarCampsObligatorisPeticioBaixaProducte(peticio);
        ValidarUsuariProveidor(idUsuari);
        BaixaProducte(peticio);

        return "Producte donat de baixa";
    }

    private void ValidarCampsNouProducte(Producte nouProducte) {

        if (nouProducte.getNom() == null || nouProducte.getNom().isEmpty())
        {
            throw new BadRequestException("El camp nom és obligatori");
        }

        if (nouProducte.getTipus() == null)
        {
            throw new BadRequestException("El camp tipus és obligatori");
        }

        if (nouProducte.getPreu() <= 0)
        {
            throw new BadRequestException("El camp preu és obligatori");
        }

        if (nouProducte.getUnitats() == null || nouProducte.getUnitats().isEmpty())
        {
            throw new BadRequestException("El camp unitats és obligatori");
        }
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

    private void ValidarUsuariProveidor(String idUsuari) {
        Usuari usuari = usuariRepository.findByIdUsuari(idUsuari);
        if (usuari.getRol() != Rol.PROVEIDOR)
        {
            throw new BadRequestException("Aquesta funcionalitat requereix el rol de proveïdor");
        }
    }

    private void ValidarCampsObligatorisPeticioBaixaProducte(PeticioBaixaProducte peticio) {
        if (peticio.getIdProducte() == null || peticio.getIdProducte().isEmpty())
        {
            throw new BadRequestException("El camp id producte és obligatori");
        }
    }

    private void BaixaProducte(PeticioBaixaProducte peticio) {
        Producte producte = producteRepository.findByIdProducte(peticio.getIdProducte());

        if (producte == null)
        {
            throw new ProducteNotFoundException(peticio.getIdProducte());
        }

        producte.setEliminat(true);
        producteRepository.save(producte);
        log.info("Donat de baixa el producte amb identificador "+producte.getIdProducte());
    }
}
