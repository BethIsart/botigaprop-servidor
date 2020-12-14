package botigaprop.servidor;

import botigaprop.servidor.Controllers.ComandaController;
import botigaprop.servidor.Models.Domain.*;
import botigaprop.servidor.Models.Requests.PeticioAltaComanda;
import botigaprop.servidor.Models.Requests.PeticioLiniaComanda;
import botigaprop.servidor.Persistence.ComandaRepository;
import botigaprop.servidor.Persistence.EnviamentRepository;
import botigaprop.servidor.Persistence.ProducteRepository;
import botigaprop.servidor.Persistence.UsuariRepository;
import botigaprop.servidor.Services.ControlAcces;
import botigaprop.servidor.Services.Mapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ComandaController.class)
public class ComandaControllerHaDe {

    private final String idUsuari = "idUsuari";
    private final String codiAcces = "unCodiAcces";
    private final String idProducte = "idProducte";
    private final int unitats = 2;
    private final String idUsuariProveidor = "idUsuariProveidor";
    private final PageRequest pageRequest = PageRequest.of(0, 5);

    @Autowired
    private MockMvc mvc;
    @Autowired
    ObjectMapper objectmapper;
    @MockBean
    private UsuariRepository usuariRepository;
    @MockBean
    private ProducteRepository producteRepository;
    @MockBean
    private ComandaRepository comandaRepository;
    @MockBean
    private EnviamentRepository enviamentRepository;
    @MockBean
    private ControlAcces controlAcces;
    @MockBean
    private Mapper mapper;

    @Test
    public void DonarDAltaUnaNovaComanda() throws Exception {

        donatUnUsuari(Rol.CLIENT);
        donatUnCodiDAccesValid();
        donatUnProducteExistent(idUsuariProveidor, idProducte);
        PeticioAltaComanda peticio = donadaUnaPeticioDAltaDeComanda(false, null, null, donadaUnaLiniaDeComanda(unitats, idProducte));

        mvc.perform(post("/altacomanda")
                .content(objectmapper.writeValueAsString(peticio))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Comanda creada amb l'identificador ")));
    }

    @Test
    public void DonarDAltaUnaNovaComandaAmbDistribucio() throws Exception {

        donatUnUsuari(Rol.CLIENT);
        donatUnCodiDAccesValid();
        donatUnProducteExistent(idUsuariProveidor, idProducte);
        PeticioAltaComanda peticio = donadaUnaPeticioDAltaDeComanda(true, "unaDireccio", "unHorari", donadaUnaLiniaDeComanda(unitats, idProducte));

        mvc.perform(post("/altacomanda")
                .content(objectmapper.writeValueAsString(peticio))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Comanda creada amb l'identificador ")));
    }

    @Test
    public void retornarErrorSiUnUsuariNoClientIntentaDonarDAltaUnaComanda()
            throws Exception {

        donatUnUsuari(Rol.PROVEIDOR);
        donatUnCodiDAccesValid();
        donatUnProducteExistent(idUsuariProveidor, idProducte);
        PeticioAltaComanda peticio = donadaUnaPeticioDAltaDeComanda(true, "unaDireccio", "unHorari", donadaUnaLiniaDeComanda(unitats, idProducte));

        mvc.perform(post("/altacomanda")
                .content(objectmapper.writeValueAsString(peticio))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(result -> assertEquals("Aquesta funcionalitat requereix el rol de CLIENT", result.getResolvedException().getMessage()));
    }

    @Test
    public void retornarErrorSiNoSInformaliniesDeComandaALaPeticioDAltaDeNovaComanda()
            throws Exception {

        donatUnUsuari(Rol.CLIENT);
        donatUnCodiDAccesValid();
        donatUnProducteExistent(idUsuariProveidor, idProducte);
        PeticioAltaComanda peticio = donadaUnaPeticioDAltaDeComanda(true, "unaDireccio", "unHorari", null);

        mvc.perform(post("/altacomanda")
                .content(objectmapper.writeValueAsString(peticio))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("No es pot crear una comanda sense productes", result.getResolvedException().getMessage()));
    }

    @Test
    public void retornarErrorSiNoSInformaLesUnitatsDUnaliniaDeComandaALaPeticioDAltaDeNovaComanda()
            throws Exception {

        donatUnUsuari(Rol.CLIENT);
        donatUnCodiDAccesValid();
        donatUnProducteExistent(idUsuariProveidor, idProducte);
        PeticioAltaComanda peticio = donadaUnaPeticioDAltaDeComanda(true, "unaDireccio", "unHorari", donadaUnaLiniaDeComanda(null, idProducte));

        mvc.perform(post("/altacomanda")
                .content(objectmapper.writeValueAsString(peticio))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("El camp unitats és obligatori i no pot ser igual o inferior a 0", result.getResolvedException().getMessage()));
    }

    @Test
    public void retornarErrorSiSInforma0UnitatsDUnaliniaDeComandaALaPeticioDAltaDeNovaComanda()
            throws Exception {

        donatUnUsuari(Rol.CLIENT);
        donatUnCodiDAccesValid();
        donatUnProducteExistent(idUsuariProveidor, idProducte);
        PeticioAltaComanda peticio = donadaUnaPeticioDAltaDeComanda(true, "unaDireccio", "unHorari", donadaUnaLiniaDeComanda(0, idProducte));

        mvc.perform(post("/altacomanda")
                .content(objectmapper.writeValueAsString(peticio))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("El camp unitats és obligatori i no pot ser igual o inferior a 0", result.getResolvedException().getMessage()));
    }

    @Test
    public void retornarErrorSiSInformaUnitatsNegativesDUnaliniaDeComandaALaPeticioDAltaDeNovaComanda()
            throws Exception {

        donatUnUsuari(Rol.CLIENT);
        donatUnCodiDAccesValid();
        donatUnProducteExistent(idUsuariProveidor, idProducte);
        PeticioAltaComanda peticio = donadaUnaPeticioDAltaDeComanda(true, "unaDireccio", "unHorari", donadaUnaLiniaDeComanda(-1, idProducte));

        mvc.perform(post("/altacomanda")
                .content(objectmapper.writeValueAsString(peticio))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("El camp unitats és obligatori i no pot ser igual o inferior a 0", result.getResolvedException().getMessage()));
    }

    @Test
    public void retornarErrorSiSInformaUnaliniaDeComandaAmbUnProducteQueNoExisteixALaPeticioDAltaDeNovaComanda()
            throws Exception {

        donatUnUsuari(Rol.CLIENT);
        donatUnCodiDAccesValid();
        PeticioAltaComanda peticio = donadaUnaPeticioDAltaDeComanda(true, "unaDireccio", "unHorari", donadaUnaLiniaDeComanda(unitats, "idProducteInexistent"));

        mvc.perform(post("/altacomanda")
                .content(objectmapper.writeValueAsString(peticio))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("No existeix cap producte amb l'identificador idProducteInexistent", result.getResolvedException().getMessage()));
    }

    @Test
    public void retornarErrorSiEsVolCrearUnaComandaAmbProductesDeDiferentesProveidors()
            throws Exception {

        donatUnUsuari(Rol.CLIENT);
        donatUnCodiDAccesValid();
        donatUnProducteExistent(idUsuariProveidor, idProducte);
        donatUnProducteExistent("unAltreId", "unAltreId");
        PeticioAltaComanda peticio = donadaUnaPeticioDAltaDeComanda(true, "unaDireccio", "unHorari",donadesDuesLiniesDeComanda(unitats, idProducte, "unAltreId"));

        mvc.perform(post("/altacomanda")
                .content(objectmapper.writeValueAsString(peticio))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("No es pot crear una comanda amb productes de diferents proveïdors", result.getResolvedException().getMessage()));
    }

    @Test
    public void retornarErrorSiSIndicaQueEsVolDistribucioPeroNoSInformaLaDireccioDEnviamentALaPeticioDAltaDeNovaComanda()
            throws Exception {

        donatUnUsuari(Rol.CLIENT);
        donatUnCodiDAccesValid();
        donatUnProducteExistent(idUsuariProveidor, idProducte);
        PeticioAltaComanda peticio = donadaUnaPeticioDAltaDeComanda(true, null, "unHorari", donadaUnaLiniaDeComanda(unitats, idProducte));

        mvc.perform(post("/altacomanda")
                .content(objectmapper.writeValueAsString(peticio))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("El camp direcció és obligatori quan es demana la distribució", result.getResolvedException().getMessage()));
    }

    @Test
    public void guardarLaNovaComanda()
            throws Exception {

        donatUnUsuari(Rol.CLIENT);
        donatUnCodiDAccesValid();
        donatUnProducteExistent(idUsuariProveidor, idProducte);
        PeticioAltaComanda peticio = donadaUnaPeticioDAltaDeComanda(false, null, null, donadaUnaLiniaDeComanda(unitats, idProducte));

        mvc.perform(post("/altacomanda")
                .content(objectmapper.writeValueAsString(peticio))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Comanda creada amb l'identificador ")));

        Mockito.verify(comandaRepository).save(Mockito.any());
    }

    @Test
    public void guardarLesDadesDeDistribucioDeLaNovaComanda() throws Exception {

        donatUnUsuari(Rol.CLIENT);
        donatUnCodiDAccesValid();
        donatUnProducteExistent(idUsuariProveidor, idProducte);
        PeticioAltaComanda peticio = donadaUnaPeticioDAltaDeComanda(true, "unaDireccio", "unHorari", donadaUnaLiniaDeComanda(unitats, idProducte));

        mvc.perform(post("/altacomanda")
                .content(objectmapper.writeValueAsString(peticio))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Comanda creada amb l'identificador ")));

        Mockito.verify(enviamentRepository).save(Mockito.any());
    }

    @Test
    public void retornarLaLlistaDeComandesDUnClient()
            throws Exception {
        Usuari usuari = donatUnUsuari(Rol.CLIENT);
        donatUnCodiDAccesValid();
        donadesUnesComandes();

        mvc.perform(get("/comandes/" + codiAcces)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(comandaRepository).findComandaByClient(usuari, pageRequest);
    }

    @Test
    public void retornarLaLlistaDeComandesAUnProveidor()
            throws Exception {
        Usuari usuari = donatUnUsuari(Rol.PROVEIDOR);
        donatUnCodiDAccesValid();
        donadesUnesComandes();

        mvc.perform(get("/comandes/" + codiAcces)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(comandaRepository).findComandaByProveidor(usuari, pageRequest);
    }

    @Test
    public void retornarLaLlistaDeTotesLesComandesAUnAdministrador()
            throws Exception {
        donatUnUsuari(Rol.ADMINISTRADOR);
        donatUnCodiDAccesValid();
        donadesUnesComandes();

        mvc.perform(get("/comandes/" + codiAcces)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(comandaRepository).findAll(pageRequest);
    }

    private PeticioAltaComanda donadaUnaPeticioDAltaDeComanda(boolean distribucio, String unaDireccio, String unHorari, List<PeticioLiniaComanda> liniesComanda) {
        PeticioAltaComanda peticio = new PeticioAltaComanda();
        peticio.setCodiAcces(codiAcces);
        peticio.setLinies(liniesComanda);
        peticio.setDistribucio(distribucio);
        peticio.setDireccioEnviament(unaDireccio);
        peticio.setHorariEnviament(unHorari);
        return peticio;
    }

    private List<PeticioLiniaComanda> donadaUnaLiniaDeComanda(Integer unitats, String idProducte) {
        List<PeticioLiniaComanda> linies = new ArrayList<>();
        PeticioLiniaComanda linia = new PeticioLiniaComanda();
        linia.setIdProducte(idProducte);
        if (unitats != null)
        {
            linia.setUnitats(unitats);
        }
        linies.add(linia);
        return linies;
    }

    private List<PeticioLiniaComanda> donadesDuesLiniesDeComanda(Integer unitats, String idPrimerProducte, String idSegonProducte) {
        List<PeticioLiniaComanda> linies = new ArrayList<>();
        PeticioLiniaComanda linia = new PeticioLiniaComanda();
        linia.setIdProducte(idPrimerProducte);
        linia.setUnitats(unitats);
        PeticioLiniaComanda linia2 = new PeticioLiniaComanda();
        linia2.setIdProducte(idSegonProducte);
        linia2.setUnitats(unitats);
        linies.add(linia);
        linies.add(linia2);
        return linies;
    }

    private Usuari donatUnUsuari(Rol rol) {
        Usuari usuari = new Usuari();
        usuari.setIdUsuari(idUsuari);
        usuari.setRol(rol);

        Mockito.when(usuariRepository.findByIdUsuari(idUsuari)).thenReturn(usuari);

        return usuari;
    }

    private void donatUnCodiDAccesValid() {
        Mockito.when(controlAcces.ValidarCodiAcces(codiAcces)).thenReturn(idUsuari);
    }

    private void donatUnProducteExistent(String idUsuariProveidor, String idProducte) {
        Usuari usuari = new Usuari();
        usuari.setIdUsuari(idUsuariProveidor);
        Producte producte = new Producte();
        producte.setUsuari(usuari);
        Mockito.when(producteRepository.findByIdProducte(idProducte)).thenReturn(producte);
    }

    private void donadesUnesComandes() {
        List<Comanda> comandes = new ArrayList<>();
        comandes.add(new Comanda());
        Page<Comanda> pageComanda = new PageImpl<Comanda>(comandes);
        Mockito.when(comandaRepository.findComandaByClient(Mockito.any(), Mockito.any())).thenReturn(pageComanda);
        Mockito.when(comandaRepository.findComandaByProveidor(Mockito.any(), Mockito.any())).thenReturn(pageComanda);
        Mockito.when(comandaRepository.findAll(pageRequest)).thenReturn(pageComanda);
    }
}
