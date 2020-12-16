package botigaprop.servidor.Controllers;

import botigaprop.servidor.Exceptions.BadRequestException;
import botigaprop.servidor.Exceptions.ProducteNotFoundException;
import botigaprop.servidor.Exceptions.UsuariNotAllowedException;
import botigaprop.servidor.Exceptions.ValoracionsNotFoundException;
import botigaprop.servidor.Models.Domain.*;
import botigaprop.servidor.Models.Requests.PeticioAltaValoracio;
import botigaprop.servidor.Models.Requests.ValoracioProducteVisualitzacio;
import botigaprop.servidor.Persistence.ComandaRepository;
import botigaprop.servidor.Persistence.ProducteRepository;
import botigaprop.servidor.Persistence.UsuariRepository;
import botigaprop.servidor.Persistence.ValoracioRepository;
import botigaprop.servidor.Services.ControlAcces;
import botigaprop.servidor.Services.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author Elisabet Isart
 */
@RestController
public class ValoracioController {
    private static final Logger log = LoggerFactory.getLogger(UsuariController.class.getName());
    private final UsuariRepository usuariRepository;
    private final ProducteRepository producteRepository;
    private final ComandaRepository comandaRepository;
    private final ValoracioRepository valoracioRepository;
    private final ControlAcces controlAcces;
    private final Mapper mapper;

    public ValoracioController(UsuariRepository usuariRepository, ProducteRepository producteRepository, ComandaRepository comandaRepository, ValoracioRepository valoracioRepository, ControlAcces controlAcces, Mapper mapper) {
        this.usuariRepository = usuariRepository;
        this.producteRepository = producteRepository;
        this.comandaRepository = comandaRepository;
        this.valoracioRepository = valoracioRepository;
        this.controlAcces = controlAcces;
        this.mapper = mapper;
    }

    @PostMapping("/altavaloracioproducte")
    public String altaValoracioProducte(@RequestBody PeticioAltaValoracio altaValoracio) {

        log.trace("Rebuda petició d'alta de nova valoració de producte");

        String idUsuari = controlAcces.ValidarCodiAcces(altaValoracio.getCodiAcces());
        Usuari usuari = usuariRepository.findByIdUsuari(idUsuari);
        ValidarRolUsuari(usuari, Rol.CLIENT);
        ValidarPeticioValoracio(altaValoracio, usuari);
        Producte producte = ObtenirProducte(altaValoracio.getIdProducte());
        ValoracioProducte valoracio = CrearValoracioProducte(altaValoracio, usuari, producte);

        valoracioRepository.save(valoracio);

        log.info("Valoració donada d'alta amb l'identificador " + valoracio.getIdValoracio() + "pel producte " + producte.getIdProducte());

        return "Valoració donada d'alta amb l'identificador " + valoracio.getIdValoracio();
    }

    @GetMapping("/valoracionsproducte/{codiAcces}/{idProducte}")
    public Map<String, Object> valoracionsProducte(@PathVariable String codiAcces, @PathVariable String idProducte, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {

        log.trace("Petició de visualització de les valoracions de producte del " + codiAcces);

        Pageable paging = PageRequest.of(page, size);
        String idUsuari = controlAcces.ValidarCodiAcces(codiAcces);
        Producte producte = ObtenirProducte(idProducte);
        Usuari usuari = usuariRepository.findByIdUsuari(idUsuari);

        if (usuari.getRol() == Rol.PROVEIDOR)
        {
            ValidarProducteDelProveidor(producte, usuari);
        }

        Page<ValoracioProducte> pageValoracions = valoracioRepository.findByProducte(producte, paging);
        List<ValoracioProducte> valoracions;
        if (pageValoracions != null)
        {
            valoracions = pageValoracions.getContent();
        }
        else
        {
            throw new ValoracionsNotFoundException();
        }
        List<ValoracioProducteVisualitzacio> valoracoinsAMostrar = mapper.ValoracionsAMostrar(valoracions);

        Map<String, Object> response = new HashMap<>();
        response.put("valoracions", valoracoinsAMostrar);
        response.put("paginaActual", pageValoracions.getNumber());
        response.put("totalValoracions", pageValoracions.getTotalElements());
        response.put("totalPagines", pageValoracions.getTotalPages());

        log.trace("Retornada valoracions del producte " + idProducte);
        return response;
    }

    private Usuari ValidarRolUsuari(Usuari usuari, Rol rol) {
        if (usuari.getRol() != rol)
        {
            throw new UsuariNotAllowedException("Aquesta funcionalitat requereix el rol de " + rol);
        }

        return usuari;
    }

    private void ValidarPeticioValoracio(PeticioAltaValoracio valoracio, Usuari usuari) {

        if (valoracio.getIdProducte() == null || valoracio.getIdProducte().isEmpty())
        {
            throw new BadRequestException("El camp producte és obligatori");
        }

        if (valoracio.getPuntuacio() == null || valoracio.getPuntuacio() > 5 || valoracio.getPuntuacio() < 0)
        {
            throw new BadRequestException("El camp puntuació és obligatori i ha de ser un número enter entre 0 i 5");
        }

        ValidarProducteEnComandaDelUsuari(valoracio, usuari);
    }

    private void ValidarProducteEnComandaDelUsuari(PeticioAltaValoracio valoracio, Usuari usuari) {
        Page<Comanda> pageComandes = comandaRepository.findComandaByClient(usuari, PageRequest.of(0, 5));
        List<Comanda> comandes = pageComandes.getContent();
        if (!comandes.stream().anyMatch(c -> c.getLinies().stream().anyMatch(l -> l.getProducte().getIdProducte().equals(valoracio.getIdProducte()))))
        {
            throw new BadRequestException("L'usuari no ha fet cap comanda del producte que vol valorar");
        }
    }

    private Producte ObtenirProducte(String idProducte) {
        Producte producte = producteRepository.findByIdProducte(idProducte);
        if (producte == null)
        {
            throw new ProducteNotFoundException(idProducte);
        }
        return producte;
    }

    private ValoracioProducte CrearValoracioProducte(PeticioAltaValoracio altaValoracio, Usuari usuari, Producte producte)
    {
        ValoracioProducte valoracio = new ValoracioProducte();
        valoracio.setIdValoracio(UUID.randomUUID().toString());
        valoracio.setDataCreacio(new Date());
        valoracio.setClient(usuari);
        valoracio.setProducte(producte);
        valoracio.setPuntuacio(altaValoracio.getPuntuacio());
        valoracio.setComentari(altaValoracio.getComentari());
        return valoracio;
    }

    private void ValidarProducteDelProveidor(Producte producte, Usuari usuari) {

        if (producte.getUsuari().getIdUsuari() != usuari.getIdUsuari())
        {
            throw new ProducteNotFoundException(producte.getIdProducte());
        }
    }
}
