package botigaprop.servidor.Controllers;

import botigaprop.servidor.Exceptions.BadRequestException;
import botigaprop.servidor.Exceptions.ProducteNotFoundException;
import botigaprop.servidor.Exceptions.UsuariNotAllowedException;
import botigaprop.servidor.Models.*;
import botigaprop.servidor.Persistence.ProducteRepository;
import botigaprop.servidor.Persistence.ProducteSpecs;
import botigaprop.servidor.Persistence.UsuariRepository;
import botigaprop.servidor.Services.ControlAcces;
import botigaprop.servidor.Services.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

        List<Producte> productes = new ArrayList<>();
        if (usuari.getRol() == Rol.PROVEIDOR)
        {
            productes = producteRepository.findProducteByIdUsuariAndEliminatIsFalse(usuari);
        }

        if (usuari.getRol() == Rol.CLIENT)
        {
            productes = producteRepository.findProducteByEliminatIsFalse();
        }

        if (usuari.getRol() == Rol.ADMINISTRADOR)
        {
            productes = producteRepository.findAll();
        }

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
        Producte producte = ValidarProducteExistentIDelUsuari(peticio.getIdProducte(), idUsuari);
        BaixaProducte(producte);

        return "Producte donat de baixa";
    }

    @PutMapping("/editarproducte/{codiAcces}")
    public ProducteVisualitzacio editarProducte(@RequestBody ProducteVisualitzacio producteEditat, @PathVariable String codiAcces)
    {
        log.trace("Petició d'edició de producte del codi " + codiAcces);

        String idUsuari = controlAcces.ValidarCodiAcces(codiAcces);
        ValidarUsuariProveidor(idUsuari);
        ValidarCampsObligatorisEdicioProducte(producteEditat);
        Producte producteGuardat = ValidarProducteExistentIDelUsuari(producteEditat.getIdProducte(), idUsuari);
        ProducteVisualitzacio producteActualitzat = ActualitzarProducte(producteGuardat, producteEditat);

        return producteActualitzat;
    }

    @PostMapping("/filtrarproductes")
    public List<ProducteVisualitzacio> filtrarProductes(@RequestBody PeticioFiltrarProductes peticio) {

        log.trace("Petició de filtrar productes");

        String idUsuari = controlAcces.ValidarCodiAcces(peticio.getCodiAcces());
        Usuari usuari = usuariRepository.findByIdUsuari(idUsuari);

        List<Producte> productes = FiltrarProductes(peticio, usuari);
        List<ProducteVisualitzacio> productesAMostrar = mapper.ProductesAMostrar(productes);

        log.trace("Retornada llista de productes");
        return productesAMostrar;
    }

    private void ValidarCampsNouProducte(Producte nouProducte) {

        if (nouProducte == null)
        {
            throw new BadRequestException("Producte no informat");
        }

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
            throw new BadRequestException("El camp preu és obligatori i no pot ser igual o inferior a 0");
        }

        if (nouProducte.getUnitats() == null || nouProducte.getUnitats().isEmpty())
        {
            throw new BadRequestException("El camp unitats és obligatori");
        }

        if (nouProducte.getQuantitatPerUnitat() < 0)
        {
            throw new BadRequestException("El valor del camp quantitat per unitat no pot ser negatiu");
        }
    }

    private void InicialitzarCampsNouProducte(Producte nouProducte, String idUsuari) {
        Usuari usuari = usuariRepository.findByIdUsuari(idUsuari);
        nouProducte.setUsuari(usuari);
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
            throw new UsuariNotAllowedException("Aquesta funcionalitat requereix el rol de proveïdor");
        }
    }

    private void ValidarCampsObligatorisPeticioBaixaProducte(PeticioBaixaProducte peticio) {
        if (peticio.getIdProducte() == null || peticio.getIdProducte().isEmpty())
        {
            throw new BadRequestException("El camp id producte és obligatori");
        }
    }

    private void BaixaProducte(Producte producte) {
        producte.setEliminat(true);
        producte.setDataUltimaEdicio(new Date());
        producteRepository.save(producte);
        log.info("Donat de baixa el producte amb identificador "+producte.getIdProducte());
    }

    private void ValidarCampsObligatorisEdicioProducte(ProducteVisualitzacio producteEditat) {

        if (producteEditat.getIdProducte() == null || producteEditat.getIdProducte().isEmpty())
        {
            throw new BadRequestException("El camp id producte és obligatori");
        }
    }

    private Producte ValidarProducteExistentIDelUsuari(String idProducte, String idUsuari) {

        Producte producte = producteRepository.findByIdProducte(idProducte);

        if (producte == null)
        {
            throw new ProducteNotFoundException(idProducte);
        }

        if (!producte.getUsuari().getIdUsuari().equals(idUsuari))
        {
            throw new ProducteNotFoundException(idProducte);
        }

        return producte;
    }

    private ProducteVisualitzacio ActualitzarProducte(Producte producte, ProducteVisualitzacio producteEditat) {

        if (producteEditat.getNom() != null && !producteEditat.getNom().isEmpty())
        {
            producte.setNom(producteEditat.getNom());
        }

        if (producteEditat.getDescripcio() != null && !producteEditat.getDescripcio().isEmpty())
        {
            producte.setDescripcio(producteEditat.getDescripcio());
        }

        if (producteEditat.getUnitats() != null && !producteEditat.getUnitats().isEmpty())
        {
            producte.setUnitats(producteEditat.getUnitats());
        }

        if (producteEditat.getTipus() != null)
        {
            producte.setTipus(producteEditat.getTipus());
        }

        if (producteEditat.getPreu() != null)
        {
            if (producteEditat.getPreu() <= 0)
            {
                throw new BadRequestException("El valor del preu no pot ser negatiu");
            }
            producte.setPreu(producteEditat.getPreu());
        }

        if (producteEditat.getQuantitatPerUnitat() != null)
        {
            if (producteEditat.getQuantitatPerUnitat() <= 0)
            {
                throw new BadRequestException("El valor del camp quantitat per unitat no pot ser negatiu");
            }
            producte.setQuantitatPerUnitat(producteEditat.getQuantitatPerUnitat());
        }

        if (producteEditat.getDisponible() != null && producteEditat.getDisponible() != producte.isDisponible())
        {
            producte.setDisponible(producteEditat.getDisponible());
        }

        if (producteEditat.getImatge() != null)
        {
            producte.setImatge(producteEditat.getImatge());
        }

        producte.setDataUltimaEdicio(new Date());

        producteRepository.save(producte);

        return mapper.ProducteAMostrar(producte);
    }

    private List<Producte> FiltrarProductes(PeticioFiltrarProductes peticio, Usuari usuari) {

        Specification productesNoEliminats = ProducteSpecs.eliminatIsFalse();
        Specification spec = Specification.where(ProducteSpecs.tipusEquals(peticio.getTipus()))
                .and(ProducteSpecs.preuIsGreaterThan(peticio.getPreuMin()))
                .and(ProducteSpecs.preuIsLessThan(peticio.getPreuMax()));


        Usuari proveidor;
        if (usuari.getRol() == Rol.PROVEIDOR)
        {
            proveidor = usuari;
        }
        else
        {
            proveidor = TrobarProveidor(peticio.getIdProveidor());
        }

        if (usuari.getRol() == Rol.PROVEIDOR || usuari.getRol() == Rol.CLIENT)
        {
            spec = Specification.where(spec).and(productesNoEliminats).and(ProducteSpecs.usuariEquals(proveidor));

        }

        if (usuari.getRol() == Rol.ADMINISTRADOR)
        {
            spec = Specification.where(spec).and(ProducteSpecs.usuariEquals(proveidor));
        }

        return producteRepository.findAll(spec);
        //TODO return producteRepository.findAll(spec, pageable);
    }

    private Usuari TrobarProveidor(String idProveidor) {

        if (idProveidor != null && !idProveidor.isEmpty())
        {
            Usuari usuari = usuariRepository.findByIdUsuari(idProveidor);
            if (usuari == null) {
                throw new BadRequestException("No existeix cap usuari amb l'identificador indicat");
            }

            return usuari;
        }

        return null;
    }
}
