package botigaprop.servidor.Controllers;

import botigaprop.servidor.Exceptions.BadRequestException;
import botigaprop.servidor.Exceptions.ComandaNotFoundException;
import botigaprop.servidor.Exceptions.UsuariNotAllowedException;
import botigaprop.servidor.Models.*;
import botigaprop.servidor.Persistence.ComandaRepository;
import botigaprop.servidor.Persistence.ProducteRepository;
import botigaprop.servidor.Persistence.UsuariRepository;
import botigaprop.servidor.Services.ControlAcces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author Elisabet Isart
 */
@RestController
public class ComandaController {
    private static final Logger log = LoggerFactory.getLogger(UsuariController.class.getName());
    private final UsuariRepository usuariRepository;
    private final ProducteRepository producteRepository;
    private final ComandaRepository comandaRepository;
    private final ControlAcces controlAcces;

    public ComandaController(UsuariRepository usuariRepository, ProducteRepository producteRepository, ComandaRepository comandaRepository, ControlAcces controlAcces) {
        this.usuariRepository = usuariRepository;
        this.producteRepository = producteRepository;
        this.comandaRepository = comandaRepository;
        this.controlAcces = controlAcces;
    }

    @PostMapping("/altacomanda")
    public String altaComanda(@RequestBody PeticioAltaComanda altaComanda) {

        log.trace("Rebuda petició d'alta de comanda");

        String idUsuari = controlAcces.ValidarCodiAcces(altaComanda.getCodiAcces());
        ValidarRolUsuari(idUsuari, Rol.CLIENT);
        List<Producte> productes = ValidarProductesDeLaNovaComanda(altaComanda.getIdProductes());
        Comanda comanda = CrearNovaComanda(productes, idUsuari);

        comandaRepository.save(comanda);

        log.info("Comanda creada amb l'identificador " + comanda.getIdComanda());
        return "Comanda creada amb l'identificador " + comanda.getIdComanda();
    }

    @GetMapping("/comandes/{codiAcces}")
    public List<Comanda> llistarComandes(@PathVariable String codiAcces) {

        log.trace("Petició de llistar comandes del codi " + codiAcces);

        String idUsuari = controlAcces.ValidarCodiAcces(codiAcces);
        ValidarRolUsuari(idUsuari, Rol.PROVEIDOR);
        Usuari usuari = usuariRepository.findByIdUsuari(idUsuari);
        List<Comanda> comandes = comandaRepository.findComandaByProveidorAndCancellatIsFalse(usuari);

        log.trace("Retornada llista de comandes");
        return comandes;
    }

    @DeleteMapping("/cancellarcomanda/{codiAcces}/{idComanda}")
    public String cancellarComanda(@PathVariable String codiAcces, @PathVariable String idComanda)
    {
        log.trace("Petició de cancel·lar comanda del codi "+ codiAcces);

        String idUsuari = controlAcces.ValidarCodiAcces(codiAcces);
        ValidarRolUsuari(idUsuari, Rol.CLIENT);
        Comanda comanda = ValidarComandaExistentIDelUsuari(idComanda, idUsuari);
        CancellarComanda(comanda);

        return "Producte donat de baixa";
    }

    private void CancellarComanda(Comanda comanda) {
        comanda.setCancellat(true);
        comanda.setDataCancellacio(new Date());
        comandaRepository.save(comanda);
        log.info("Cancel·lada la comanda amb identificador "+comanda.getIdComanda());
    }

    private Comanda ValidarComandaExistentIDelUsuari(String idComanda, String idUsuari) {

        Comanda comanda = comandaRepository.findByIdComanda(idComanda);

        if (comanda == null)
        {
            throw new ComandaNotFoundException(idComanda);
        }

        if (!comanda.getClient().getIdUsuari().equals(idUsuari))
        {
            throw new ComandaNotFoundException(idComanda);
        }

        return comanda;
    }

    private Comanda CrearNovaComanda(List<Producte> productes, String idUsuari) {
        Usuari usuari = usuariRepository.findByIdUsuari(idUsuari);
        Comanda comanda = new Comanda();
        comanda.setIdComanda(UUID.randomUUID().toString());
        comanda.setClient(usuari);
        comanda.setProveidor(productes.get(0).getUsuari());
        comanda.setProductes(productes);
        comanda.setDataCreacio(new Date());

        return comanda;
    }

    private void ValidarRolUsuari(String idUsuari, Rol rol) {
        Usuari usuari = usuariRepository.findByIdUsuari(idUsuari);
        if (usuari.getRol() != rol)
        {
            throw new UsuariNotAllowedException("Aquesta funcionalitat requereix el rol de " + rol);
        }
    }

    private List<Producte> ValidarProductesDeLaNovaComanda(List<String> idProductes) {

        if (idProductes == null || idProductes.isEmpty())
        {
            throw new BadRequestException("No es pot crear una comanda sense productes");
        }

        List<Producte> productes = new ArrayList<>();
        for (String idProducte:idProductes)
        {
            Usuari proveidor = new Usuari();
            Producte producte = producteRepository.findByIdProducte(idProducte);
            if (producte == null)
            {
                throw new BadRequestException("No existeix cap producte amb l'identificador " +idProducte);
            }

            productes.add(producte);
            ValidarProveidorProducte(proveidor, producte);
        }

        return productes;
    }

    private void ValidarProveidorProducte(Usuari proveidor, Producte producte) {

        if (proveidor.getIdUsuari() == null)
        {
            proveidor = producte.getUsuari();
        }

        if (proveidor != producte.getUsuari())
        {
            throw new BadRequestException("No es pot crear una comanda amb productes de diferents proveïdors");
        }
    }
}
