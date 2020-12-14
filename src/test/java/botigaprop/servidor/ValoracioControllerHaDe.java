package botigaprop.servidor;

import botigaprop.servidor.Controllers.ValoracioController;
import botigaprop.servidor.Models.Domain.*;
import botigaprop.servidor.Models.Requests.PeticioAltaValoracio;
import botigaprop.servidor.Persistence.*;
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
@WebMvcTest(ValoracioController.class)
public class ValoracioControllerHaDe {

    private final String idUsuari = "idUsuari";
    private final String codiAcces = "unCodiAcces";
    private final String idProducte = "idProducte";
    private final String comentari = "unComentari";
    private final int puntuacio = 5;

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
    private ValoracioRepository valoracioRepository;
    @MockBean
    private ControlAcces controlAcces;
    @MockBean
    private Mapper mapper;

    @Test
    public void donarDAltaUnaNovaValoracioDeProducte()
            throws Exception {

        Usuari usuari = donatUnUsuari(Rol.CLIENT);
        donatUnCodiDAccesValid();
        donatUnProducteExistent(idProducte, idUsuari);
        donadaUnaComandaDelClientAmbElProducteAValorar(usuari);
        PeticioAltaValoracio peticio = donadaUnaPeticioDAltaDeValoracioDeProducte(comentari, puntuacio, idProducte);

        mvc.perform(post("/altavaloracioproducte")
                .content(objectmapper.writeValueAsString(peticio))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Valoració donada d'alta amb l'identificador ")));
    }

    @Test
    public void retornarErrorSiUnUsuariNoClientIntentaDonarDAltaUnaValoracioDeProducte()
            throws Exception {

        donatUnUsuari(Rol.PROVEIDOR);
        donatUnCodiDAccesValid();
        donatUnProducteExistent(idProducte, idUsuari);
        PeticioAltaValoracio peticio = donadaUnaPeticioDAltaDeValoracioDeProducte(comentari, puntuacio, idProducte);

        mvc.perform(post("/altavaloracioproducte")
                .content(objectmapper.writeValueAsString(peticio))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(result -> assertEquals("Aquesta funcionalitat requereix el rol de CLIENT", result.getResolvedException().getMessage()));
    }

    @Test
    public void retornarErrorSiNoSInformaElProducteALaPeticioDAltaDeNovaValoracioDeProducte()
            throws Exception {

        donatUnUsuari(Rol.CLIENT);
        donatUnCodiDAccesValid();
        donatUnProducteExistent(idProducte, idUsuari);
        PeticioAltaValoracio peticio = donadaUnaPeticioDAltaDeValoracioDeProducte(comentari, puntuacio, null);

        mvc.perform(post("/altavaloracioproducte")
                .content(objectmapper.writeValueAsString(peticio))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("El camp producte és obligatori", result.getResolvedException().getMessage()));
    }

    @Test
    public void retornarErrorSiNoSInformaLaPuntuacioALaPeticioDAltaDeNovaValoracioDeProducte()
            throws Exception {

        donatUnUsuari(Rol.CLIENT);
        donatUnCodiDAccesValid();
        donatUnProducteExistent(idProducte, idUsuari);
        PeticioAltaValoracio peticio = donadaUnaPeticioDAltaDeValoracioDeProducte(comentari, null, idProducte);

        mvc.perform(post("/altavaloracioproducte")
                .content(objectmapper.writeValueAsString(peticio))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("El camp puntuació és obligatori i ha de ser un número enter entre 0 i 5", result.getResolvedException().getMessage()));
    }

    @Test
    public void retornarErrorSiSInformaUnaPuntuacioMésGranQue5ALaPeticioDAltaDeNovaValoracioDeProducte()
            throws Exception {

        donatUnUsuari(Rol.CLIENT);
        donatUnCodiDAccesValid();
        donatUnProducteExistent(idProducte, idUsuari);
        PeticioAltaValoracio peticio = donadaUnaPeticioDAltaDeValoracioDeProducte(comentari, 6, idProducte);

        mvc.perform(post("/altavaloracioproducte")
                .content(objectmapper.writeValueAsString(peticio))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("El camp puntuació és obligatori i ha de ser un número enter entre 0 i 5", result.getResolvedException().getMessage()));
    }

    @Test
    public void retornarErrorSiSInformaUnaPuntuacioNegativaALaPeticioDAltaDeNovaValoracioDeProducte()
            throws Exception {

        donatUnUsuari(Rol.CLIENT);
        donatUnCodiDAccesValid();
        donatUnProducteExistent(idProducte, idUsuari);
        PeticioAltaValoracio peticio = donadaUnaPeticioDAltaDeValoracioDeProducte(comentari, -1, idProducte);

        mvc.perform(post("/altavaloracioproducte")
                .content(objectmapper.writeValueAsString(peticio))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("El camp puntuació és obligatori i ha de ser un número enter entre 0 i 5", result.getResolvedException().getMessage()));
    }

    @Test
    public void retornarErrorSiLUsuariVolValorarUnProducteQueNoEstaEnCapDeLesSevesComandes()
            throws Exception {

        donatUnUsuari(Rol.CLIENT);
        donatUnCodiDAccesValid();
        donatUnProducteExistent(idProducte, idUsuari);
        PeticioAltaValoracio peticio = donadaUnaPeticioDAltaDeValoracioDeProducte(comentari, puntuacio, idProducte);

        mvc.perform(post("/altavaloracioproducte")
                .content(objectmapper.writeValueAsString(peticio))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("L'usuari no ha fet cap comanda del producte que vol valorar", result.getResolvedException().getMessage()));
    }

    @Test
    public void retornarErrorSiElProducteAValorarNoExisteix()
            throws Exception {

        Usuari usuari = donatUnUsuari(Rol.CLIENT);
        donatUnCodiDAccesValid();
        donadaUnaComandaDelClientAmbElProducteAValorar(usuari);
        PeticioAltaValoracio peticio = donadaUnaPeticioDAltaDeValoracioDeProducte(comentari, puntuacio, idProducte);

        mvc.perform(post("/altavaloracioproducte")
                .content(objectmapper.writeValueAsString(peticio))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals("No s'ha trobat cap producte amb l'identificador idProducte", result.getResolvedException().getMessage()));
    }

    @Test
    public void guardarUnaNovaComanda()
            throws Exception {

        Usuari usuari = donatUnUsuari(Rol.CLIENT);
        donatUnCodiDAccesValid();
        donatUnProducteExistent(idProducte, idUsuari);
        donadaUnaComandaDelClientAmbElProducteAValorar(usuari);
        PeticioAltaValoracio peticio = donadaUnaPeticioDAltaDeValoracioDeProducte(comentari, puntuacio, idProducte);

        mvc.perform(post("/altavaloracioproducte")
                .content(objectmapper.writeValueAsString(peticio))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Valoració donada d'alta amb l'identificador ")));

        Mockito.verify(valoracioRepository).save(Mockito.any());
    }

    @Test
    public void retornarLaLlistaDeValoracionsDUnProducte()
            throws Exception {

        donatUnUsuari(Rol.CLIENT);
        donatUnCodiDAccesValid();
        donatUnProducteExistent(idProducte, idUsuari);

        mvc.perform(get("/valoracionsproducte/" + codiAcces + "/" + idProducte)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(valoracioRepository).findByProducte(Mockito.any());
    }

    @Test
    public void retornarErrorAlDemanarLaLlistaDeValoracionsDUnProducteNoExistent()
            throws Exception {

        donatUnUsuari(Rol.CLIENT);
        donatUnCodiDAccesValid();

        mvc.perform(get("/valoracionsproducte/" + codiAcces + "/" + idProducte)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals("No s'ha trobat cap producte amb l'identificador idProducte", result.getResolvedException().getMessage()));
    }

    @Test
    public void retornarErrorSiUnUsuariProveidorDemanaLaLlistaDeValoracionsDUnProducteQueNoEsSeu()
            throws Exception {

        donatUnUsuari(Rol.PROVEIDOR);
        donatUnCodiDAccesValid();
        donatUnProducteExistent(idProducte, "idUnAltreUsuari");

        mvc.perform(get("/valoracionsproducte/" + codiAcces + "/" + idProducte)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals("No s'ha trobat cap producte amb l'identificador idProducte", result.getResolvedException().getMessage()));
    }

    private void donadaUnaComandaDelClientAmbElProducteAValorar(Usuari usuari) {
        Producte producte = new Producte();
        producte.setIdProducte(idProducte);
        List<LiniaComanda> liniesComanda = new ArrayList<>();
        LiniaComanda linia = new LiniaComanda();
        linia.setProducte(producte);
        liniesComanda.add(linia);
        Comanda comanda = new Comanda();
        comanda.setLinies(liniesComanda);
        List<Comanda> comandes = new ArrayList<>();
        comandes.add(comanda);
        Page<Comanda> pageComanda = new PageImpl<Comanda>(comandes);
        Mockito.when(comandaRepository.findComandaByClient(usuari, Mockito.any())).thenReturn(pageComanda);
    }

    private PeticioAltaValoracio donadaUnaPeticioDAltaDeValoracioDeProducte(String comentari, Integer puntuacio, String idProducte) {
        PeticioAltaValoracio peticio = new PeticioAltaValoracio();
        peticio.setCodiAcces(codiAcces);
        peticio.setComentari(comentari);
        if (puntuacio != null) {
            peticio.setPuntuacio(puntuacio);
        }
        peticio.setIdProducte(idProducte);
        return peticio;
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

    private void donatUnProducteExistent(String idProducte, String idUsuari) {
        Usuari usuari = new Usuari();
        usuari.setIdUsuari(idUsuari);
        Producte producte = new Producte();
        producte.setUsuari(usuari);
        producte.setIdProducte(idProducte);
        Mockito.when(producteRepository.findByIdProducte(idProducte)).thenReturn(producte);
    }
}
