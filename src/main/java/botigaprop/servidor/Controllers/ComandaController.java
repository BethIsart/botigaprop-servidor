package botigaprop.servidor.Controllers;

import botigaprop.servidor.Exceptions.BadRequestException;
import botigaprop.servidor.Exceptions.ComandaNotFoundException;
import botigaprop.servidor.Exceptions.UsuariNotAllowedException;
import botigaprop.servidor.Models.Domain.*;
import botigaprop.servidor.Models.Requests.ComandaVisualitzacio;
import botigaprop.servidor.Models.Requests.PeticioAltaComanda;
import botigaprop.servidor.Models.Requests.PeticioLiniaComanda;
import botigaprop.servidor.Persistence.ComandaRepository;
import botigaprop.servidor.Persistence.ProducteRepository;
import botigaprop.servidor.Persistence.UsuariRepository;
import botigaprop.servidor.Services.ControlAcces;
import botigaprop.servidor.Services.Mapper;
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
    private final Mapper mapper;

    public ComandaController(UsuariRepository usuariRepository, ProducteRepository producteRepository, ComandaRepository comandaRepository, ControlAcces controlAcces, Mapper mapper) {
        this.usuariRepository = usuariRepository;
        this.producteRepository = producteRepository;
        this.comandaRepository = comandaRepository;
        this.controlAcces = controlAcces;
        this.mapper = mapper;
    }

    @PostMapping("/altacomanda")
    public String altaComanda(@RequestBody PeticioAltaComanda altaComanda) {

        log.trace("Rebuda petició d'alta de comanda");

        String idUsuari = controlAcces.ValidarCodiAcces(altaComanda.getCodiAcces());
        ValidarRolUsuari(idUsuari, Rol.CLIENT);
        List<LiniaComanda> liniesComanda = ValidarLiniesDeLaNovaComanda(altaComanda.getLinies());
        Comanda comanda = CrearNovaComanda(liniesComanda, idUsuari);

        comandaRepository.save(comanda);

        log.info("Comanda creada amb l'identificador " + comanda.getIdComanda());
        return "Comanda creada amb l'identificador " + comanda.getIdComanda();
    }

    @GetMapping("/comandes/{codiAcces}")
    public List<ComandaVisualitzacio> llistarComandes(@PathVariable String codiAcces) {

        log.trace("Petició de llistar comandes del codi " + codiAcces);

        String idUsuari = controlAcces.ValidarCodiAcces(codiAcces);
        ValidarRolUsuari(idUsuari, Rol.PROVEIDOR);
        Usuari usuari = usuariRepository.findByIdUsuari(idUsuari);
        List<Comanda> comandes = comandaRepository.findComandaByProveidorAndCancellatIsFalse(usuari);
        List<ComandaVisualitzacio> comandesAMostrar = mapper.ComandesAMostrar(comandes);

        log.trace("Retornada llista de comandes");
        return comandesAMostrar;
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

    private Comanda CrearNovaComanda(List<LiniaComanda> liniesComanda, String idUsuari) {
        Usuari usuari = usuariRepository.findByIdUsuari(idUsuari);
        Comanda comanda = new Comanda();
        comanda.setIdComanda(UUID.randomUUID().toString());
        comanda.setClient(usuari);
        comanda.setProveidor(liniesComanda.get(0).getProducte().getUsuari());
        comanda.setDataCreacio(new Date());
        comanda.setLinies(liniesComanda);

        return comanda;
    }

    private void ValidarRolUsuari(String idUsuari, Rol rol) {
        Usuari usuari = usuariRepository.findByIdUsuari(idUsuari);
        if (usuari.getRol() != rol)
        {
            throw new UsuariNotAllowedException("Aquesta funcionalitat requereix el rol de " + rol);
        }
    }

    private List<LiniaComanda> ValidarLiniesDeLaNovaComanda(List<PeticioLiniaComanda> liniesComanda) {

        if (liniesComanda == null || liniesComanda.isEmpty())
        {
            throw new BadRequestException("No es pot crear una comanda sense productes");
        }

        List<LiniaComanda> liniesDeLaComanda = new ArrayList<>();
        Usuari proveidor = new Usuari();

        for (PeticioLiniaComanda linia:liniesComanda)
        {
            if (linia.getUnitats() <= 0)
            {
                throw new BadRequestException("El camp unitats és obligatori i no pot ser igual o inferior a 0");
            }

            Producte producte = producteRepository.findByIdProducte(linia.getIdProducte());
            if (producte == null)
            {
                throw new BadRequestException("No existeix cap producte amb l'identificador " +linia.getIdProducte());
            }



            if (proveidor.getIdUsuari() == null)
            {
                proveidor = producte.getUsuari();
            }

            if (proveidor != producte.getUsuari())
            {
                throw new BadRequestException("No es pot crear una comanda amb productes de diferents proveïdors");
            }

            liniesDeLaComanda.add(CrearNovaLiniaDeComanda(linia, producte));
        }

        return liniesDeLaComanda;
    }

    private LiniaComanda CrearNovaLiniaDeComanda(PeticioLiniaComanda linia, Producte producte) {
        LiniaComanda novaLinia = new LiniaComanda();
        novaLinia.setIdLiniaComanda(UUID.randomUUID().toString());
        novaLinia.setUnitats(linia.getUnitats());
        novaLinia.setProducte(producte);
        return novaLinia;
    }
}
