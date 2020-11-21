package botigaprop.servidor;

import botigaprop.servidor.Controllers.ProducteController;
import botigaprop.servidor.Models.*;
import botigaprop.servidor.Persistence.ProducteRepository;
import botigaprop.servidor.Persistence.UsuariRepository;
import botigaprop.servidor.Services.ControlAcces;
import botigaprop.servidor.Services.Mapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ProducteController.class)
public class ProducteControllerHaDe {

    private final String idUsuari = "idUsuari";
    private final String codiAcces = "unCodiAcces";
    private final String nom = "unProducte";
    private final String descripcio = "unaDescripcio";
    private final Categoria tipus = Categoria.Peix;
    private final double preu = 10;
    private final String unitat = "bossa";
    private final double quantitat = 1;
    private final boolean disponible = true;
    private final String idProducte = "idProducte";

    @Autowired
    private MockMvc mvc;
    @Autowired
    ObjectMapper objectmapper;
    @MockBean
    private UsuariRepository usuariRepository;
    @MockBean
    private ProducteRepository producteRepository;
    @MockBean
    private ControlAcces controlAcces;
    @MockBean
    private Mapper mapper;

    @Test
    public void DonarDAltaUnNouProducte() throws Exception {

        donatUnUsuari(Rol.PROVEIDOR);
        donatUnCodiDAccesValid();
        Producte nouProducte = donatUnProducte(nom, tipus, preu, unitat, quantitat, disponible);
        PeticioAltraProducte peticio = donadaUnaPeticioDAltaDeProducte(nouProducte);

        mvc.perform(post("/altaproducte")
                .content(objectmapper.writeValueAsString(peticio))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Producte donat d'alta amb l'identificador ")));
    }

    @Test
    public void retornarErrorSiUnUsuariNoProveidorIntentaDonarDAltaUnProducte()
            throws Exception {

        donatUnUsuari(Rol.CLIENT);
        donatUnCodiDAccesValid();
        Producte nouProducte = donatUnProducte(nom, tipus, preu, unitat, quantitat, disponible);
        PeticioAltraProducte peticio = donadaUnaPeticioDAltaDeProducte(nouProducte);

        mvc.perform(post("/altaproducte")
                .content(objectmapper.writeValueAsString(peticio))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(result -> assertEquals("Aquesta funcionalitat requereix el rol de proveïdor", result.getResolvedException().getMessage()));
    }

    @Test
    public void retornarErrorSiNoSInformaElProducteALaPeticioDAltaDeProducte()
            throws Exception {

        donatUnUsuari(Rol.PROVEIDOR);
        donatUnCodiDAccesValid();
        PeticioAltraProducte peticio = donadaUnaPeticioDAltaDeProducte(null);

        mvc.perform(post("/altaproducte")
                .content(objectmapper.writeValueAsString(peticio))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("Producte no informat", result.getResolvedException().getMessage()));
    }

    @Test
    public void retornarErrorSiNoSInformaElNomAlCrearUnNouProducte()
            throws Exception {

        donatUnUsuari(Rol.PROVEIDOR);
        donatUnCodiDAccesValid();
        Producte nouProducte = donatUnProducte(null, tipus, preu, unitat, quantitat, disponible);
        PeticioAltraProducte peticio = donadaUnaPeticioDAltaDeProducte(nouProducte);

        mvc.perform(post("/altaproducte")
                .content(objectmapper.writeValueAsString(peticio))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("El camp nom és obligatori", result.getResolvedException().getMessage()));
    }

    @Test
    public void retornarErrorSiNoSInformaElTipusAlCrearUnNouProducte()
            throws Exception {

        donatUnUsuari(Rol.PROVEIDOR);
        donatUnCodiDAccesValid();
        Producte nouProducte = donatUnProducte(nom, null, preu, unitat, quantitat, disponible);
        PeticioAltraProducte peticio = donadaUnaPeticioDAltaDeProducte(nouProducte);

        mvc.perform(post("/altaproducte")
                .content(objectmapper.writeValueAsString(peticio))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("El camp tipus és obligatori", result.getResolvedException().getMessage()));
    }

    @Test
    public void retornarErrorSiSInformaElPreuAmbValorNegatiuAlCrearUnNouProducte()
            throws Exception {

        donatUnUsuari(Rol.PROVEIDOR);
        donatUnCodiDAccesValid();
        Producte nouProducte = donatUnProducte(nom, tipus, -1, unitat, quantitat, disponible);
        PeticioAltraProducte peticio = donadaUnaPeticioDAltaDeProducte(nouProducte);

        mvc.perform(post("/altaproducte")
                .content(objectmapper.writeValueAsString(peticio))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("El camp preu és obligatori i no pot ser igual o inferior a 0", result.getResolvedException().getMessage()));
    }

    @Test
    public void retornarErrorSiSInformaElPreuIgualA0AlCrearUnNouProducte()
            throws Exception {

        donatUnUsuari(Rol.PROVEIDOR);
        donatUnCodiDAccesValid();
        Producte nouProducte = donatUnProducte(nom, tipus, 0, unitat, quantitat, disponible);
        PeticioAltraProducte peticio = donadaUnaPeticioDAltaDeProducte(nouProducte);

        mvc.perform(post("/altaproducte")
                .content(objectmapper.writeValueAsString(peticio))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("El camp preu és obligatori i no pot ser igual o inferior a 0", result.getResolvedException().getMessage()));
    }

    @Test
    public void retornarErrorSiNoSInformaLesUnitatsAlCrearUnNouProducte()
            throws Exception {

        donatUnUsuari(Rol.PROVEIDOR);
        donatUnCodiDAccesValid();
        Producte nouProducte = donatUnProducte(nom, tipus, preu, null, quantitat, disponible);
        PeticioAltraProducte peticio = donadaUnaPeticioDAltaDeProducte(nouProducte);

        mvc.perform(post("/altaproducte")
                .content(objectmapper.writeValueAsString(peticio))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("El camp unitats és obligatori", result.getResolvedException().getMessage()));
    }

    @Test
    public void retornarErrorSiSInformaLaQuantitatPerUnitatAmbValorNegatiuAlCrearUnNouProducte()
            throws Exception {

        donatUnUsuari(Rol.PROVEIDOR);
        donatUnCodiDAccesValid();
        Producte nouProducte = donatUnProducte(nom, tipus, preu, unitat, -2, disponible);
        PeticioAltraProducte peticio = donadaUnaPeticioDAltaDeProducte(nouProducte);

        mvc.perform(post("/altaproducte")
                .content(objectmapper.writeValueAsString(peticio))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("El valor del camp quantitat per unitat no pot ser negatiu", result.getResolvedException().getMessage()));
    }

    @Test
    public void guardarElNoProducte()
            throws Exception {

        donatUnUsuari(Rol.PROVEIDOR);
        donatUnCodiDAccesValid();
        Producte nouProducte = donatUnProducte(nom, tipus, preu, unitat, quantitat, disponible);
        PeticioAltraProducte peticio = donadaUnaPeticioDAltaDeProducte(nouProducte);

        mvc.perform(post("/altaproducte")
                .content(objectmapper.writeValueAsString(peticio))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Producte donat d'alta amb l'identificador ")));

        Mockito.verify(producteRepository).save(Mockito.any());
    }

    @Test
    public void retornarLaLlistaDelsSeusProductesAUnProveidor()
            throws Exception {
        Usuari usuari = donatUnUsuari(Rol.PROVEIDOR);
        donatUnCodiDAccesValid();

        mvc.perform(get("/productes/" + codiAcces)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(producteRepository).findProducteByIdUsuariAndEliminatIsFalse(usuari);
    }

    @Test
    public void retornarLaLlistaDeProductesNoEliminatsAUnClient()
            throws Exception {
        donatUnUsuari(Rol.CLIENT);
        donatUnCodiDAccesValid();

        mvc.perform(get("/productes/" + codiAcces)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(producteRepository).findProducteByEliminatIsFalse();
    }

    @Test
    public void retornarLaLlistaDeTotsElsProductesAUnAdministrador()
            throws Exception {
        donatUnUsuari(Rol.ADMINISTRADOR);
        donatUnCodiDAccesValid();

        mvc.perform(get("/productes/" + codiAcces)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(producteRepository).findAll();
    }

    @Test
    public void DonarDeBaixaUnProducte()
            throws Exception {
        Usuari usuari = donatUnUsuari(Rol.PROVEIDOR);
        donatUnCodiDAccesValid();
        Producte producte = donatUnProducteDUnProveidor(usuari);

        mvc.perform(delete("/baixaproducte/" + codiAcces + "/" + idProducte)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Producte donat de baixa")));

        assertThat(producte.getEliminat()).isTrue();
    }

    @Test
    public void retornarErrorSiUnUusariClientVolDonarDeBaixaUnProducte()
            throws Exception {

        donatUnUsuari(Rol.CLIENT);
        donatUnCodiDAccesValid();

        mvc.perform(delete("/baixaproducte/" + codiAcces + "/" + idProducte)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(result -> assertEquals("Aquesta funcionalitat requereix el rol de proveïdor", result.getResolvedException().getMessage()));
    }

    @Test
    public void retornarErrorSiSIntentaDonarDeBaixaUnProducteQueNoExisteix()
            throws Exception {

        donatUnUsuari(Rol.PROVEIDOR);
        donatUnCodiDAccesValid();

        mvc.perform(delete("/baixaproducte/" + codiAcces + "/unIdProducteNoExistent")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals("No s'ha trobat cap producte amb l'identificador unIdProducteNoExistent", result.getResolvedException().getMessage()));
    }

    @Test
    public void retornarErrorSiSIntentaDonarDeBaixaUnProducteDUnAltreProveidor()
            throws Exception {

        donatUnUsuari(Rol.PROVEIDOR);
        donatUnCodiDAccesValid();
        Usuari unAltreProveidor = donatUnUsuari(Rol.PROVEIDOR);
        unAltreProveidor.setIdUsuari("unAltreIdUsuari");
        donatUnProducteDUnProveidor(unAltreProveidor);

        mvc.perform(delete("/baixaproducte/" + codiAcces + "/" + idProducte)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals("No s'ha trobat cap producte amb l'identificador idProducte", result.getResolvedException().getMessage()));
    }

    @Test
    public void guardarElProducteComEliminatAlDonarloDeBaixa()
            throws Exception {
        Usuari usuari = donatUnUsuari(Rol.PROVEIDOR);
        donatUnCodiDAccesValid();
        donatUnProducteDUnProveidor(usuari);

        mvc.perform(delete("/baixaproducte/" + codiAcces + "/" + idProducte)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Producte donat de baixa")));

        Mockito.verify(producteRepository).save(Mockito.any());
    }

    @Test
    public void retornarErrorSiUnUsuariClientVolEditarUnProducte()
            throws Exception {
        donatUnUsuari(Rol.CLIENT);
        donatUnCodiDAccesValid();
        ProducteVisualitzacio producte = donatUnProducteAEditar(idProducte, nom, tipus, unitat, quantitat, preu, descripcio, disponible, null);

        mvc.perform(put("/editarproducte/" + codiAcces)
                .content(objectmapper.writeValueAsString(producte))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(result -> assertEquals("Aquesta funcionalitat requereix el rol de proveïdor", result.getResolvedException().getMessage()));
    }

    @Test
    public void retornarErrorSiNoSInformaElIdDeProducteAlEditarUnProducte()
            throws Exception {
        donatUnUsuari(Rol.PROVEIDOR);
        donatUnCodiDAccesValid();
        ProducteVisualitzacio producte = donatUnProducteAEditar(null, null, null, null, null, null, null, null, null);

        mvc.perform(put("/editarproducte/" + codiAcces)
                .content(objectmapper.writeValueAsString(producte))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("El camp id producte és obligatori", result.getResolvedException().getMessage()));
    }

    @Test
    public void retornarErrorSiSIntentaEditarUnProducteQueNoExisteix()
            throws Exception {

        donatUnUsuari(Rol.PROVEIDOR);
        donatUnCodiDAccesValid();
        ProducteVisualitzacio producte = donatUnProducteAEditar("unIdDeProducteNoExistent", nom, tipus, unitat, quantitat, preu, descripcio, disponible, null);

        mvc.perform(put("/editarproducte/" + codiAcces)
                .content(objectmapper.writeValueAsString(producte))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals("No s'ha trobat cap producte amb l'identificador unIdDeProducteNoExistent", result.getResolvedException().getMessage()));
    }

    @Test
    public void retornarErrorSiSIntentaEditarUnProducteDUnAltreProveidor()
            throws Exception {

        donatUnUsuari(Rol.PROVEIDOR);
        donatUnCodiDAccesValid();
        donatUnProducteDUnAltreProveidor();
        ProducteVisualitzacio producte = donatUnProducteAEditar(idProducte, nom, tipus, unitat, quantitat, preu, descripcio, disponible, null);

        mvc.perform(put("/editarproducte/" + codiAcces)
                .content(objectmapper.writeValueAsString(producte))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals("No s'ha trobat cap producte amb l'identificador idProducte", result.getResolvedException().getMessage()));
    }

    @Test
    public void editarElNomDUnProducte()
            throws Exception {

        String nouNom = "nouNom";
        Usuari usuari = donatUnUsuari(Rol.PROVEIDOR);
        donatUnCodiDAccesValid();
        donatUnProducteDUnProveidor(usuari);
        ProducteVisualitzacio producteAEditar = donatUnProducteAEditar(idProducte, nouNom, null, null, null, null, null, null, null);

        mvc.perform(put("/editarproducte/" + codiAcces)
                .content(objectmapper.writeValueAsString(producteAEditar))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        ArgumentCaptor<Producte> argument = ArgumentCaptor.forClass(Producte.class);
        Mockito.verify(producteRepository).save(argument.capture());
        assertThat(argument.getValue().getNom()).isEqualTo(nouNom);
    }

    @Test
    public void editarLaDescripcioDUnProducte()
            throws Exception {

        String novaDescripcio = "novaDescripcio";
        Usuari usuari = donatUnUsuari(Rol.PROVEIDOR);
        donatUnCodiDAccesValid();
        donatUnProducteDUnProveidor(usuari);
        ProducteVisualitzacio producteAEditar = donatUnProducteAEditar(idProducte, null, null, null, null, null, novaDescripcio, null, null);

        mvc.perform(put("/editarproducte/" + codiAcces)
                .content(objectmapper.writeValueAsString(producteAEditar))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        ArgumentCaptor<Producte> argument = ArgumentCaptor.forClass(Producte.class);
        Mockito.verify(producteRepository).save(argument.capture());
        assertThat(argument.getValue().getDescripcio()).isEqualTo(novaDescripcio);
    }

    @Test
    public void editarLesUnitatsDUnProducte()
            throws Exception {

        String novesUnitats = "novesUnitats";
        Usuari usuari = donatUnUsuari(Rol.PROVEIDOR);
        donatUnCodiDAccesValid();
        donatUnProducteDUnProveidor(usuari);
        ProducteVisualitzacio producteAEditar = donatUnProducteAEditar(idProducte, null, null, novesUnitats, null, null, null, null, null);

        mvc.perform(put("/editarproducte/" + codiAcces)
                .content(objectmapper.writeValueAsString(producteAEditar))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        ArgumentCaptor<Producte> argument = ArgumentCaptor.forClass(Producte.class);
        Mockito.verify(producteRepository).save(argument.capture());
        assertThat(argument.getValue().getUnitats()).isEqualTo(novesUnitats);
    }

    @Test
    public void editarElTipusDUnProducte()
            throws Exception {

        Categoria nouTipus = Categoria.Carn;
        Usuari usuari = donatUnUsuari(Rol.PROVEIDOR);
        donatUnCodiDAccesValid();
        donatUnProducteDUnProveidor(usuari);
        ProducteVisualitzacio producteAEditar = donatUnProducteAEditar(idProducte, null, nouTipus, null, null, null, null, null, null);

        mvc.perform(put("/editarproducte/" + codiAcces)
                .content(objectmapper.writeValueAsString(producteAEditar))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        ArgumentCaptor<Producte> argument = ArgumentCaptor.forClass(Producte.class);
        Mockito.verify(producteRepository).save(argument.capture());
        assertThat(argument.getValue().getTipus()).isEqualTo(nouTipus);
    }

    @Test
    public void editarElPreuDUnProducte()
            throws Exception {

        double nouPreu = 23.4;
        Usuari usuari = donatUnUsuari(Rol.PROVEIDOR);
        donatUnCodiDAccesValid();
        donatUnProducteDUnProveidor(usuari);
        ProducteVisualitzacio producteAEditar = donatUnProducteAEditar(idProducte, null, null, null, null, nouPreu, null, null, null);

        mvc.perform(put("/editarproducte/" + codiAcces)
                .content(objectmapper.writeValueAsString(producteAEditar))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        ArgumentCaptor<Producte> argument = ArgumentCaptor.forClass(Producte.class);
        Mockito.verify(producteRepository).save(argument.capture());
        assertThat(argument.getValue().getPreu()).isEqualTo(nouPreu);
    }

    @Test
    public void retornarErrorSiEsVolEditarElPreuDUnProducteAmbUnValorNegatiu()
            throws Exception {

        double nouPreu = -23.4;
        Usuari usuari = donatUnUsuari(Rol.PROVEIDOR);
        donatUnCodiDAccesValid();
        donatUnProducteDUnProveidor(usuari);
        ProducteVisualitzacio producteAEditar = donatUnProducteAEditar(idProducte, null, null, null, null, nouPreu, null, null, null);

        mvc.perform(put("/editarproducte/" + codiAcces)
                .content(objectmapper.writeValueAsString(producteAEditar))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("El valor del preu no pot ser negatiu", result.getResolvedException().getMessage()));
    }

    @Test
    public void editarLaQuantitatPerUnitatDUnProducte()
            throws Exception {

        double novaQuantitatPerUnitat = 5.7;
        Usuari usuari = donatUnUsuari(Rol.PROVEIDOR);
        donatUnCodiDAccesValid();
        donatUnProducteDUnProveidor(usuari);
        ProducteVisualitzacio producteAEditar = donatUnProducteAEditar(idProducte, null, null, null, novaQuantitatPerUnitat, null, null, null, null);

        mvc.perform(put("/editarproducte/" + codiAcces)
                .content(objectmapper.writeValueAsString(producteAEditar))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        ArgumentCaptor<Producte> argument = ArgumentCaptor.forClass(Producte.class);
        Mockito.verify(producteRepository).save(argument.capture());
        assertThat(argument.getValue().getQuantitatPerUnitat()).isEqualTo(novaQuantitatPerUnitat);
    }

    @Test
    public void retornarErrorSiEsVolEditarLaQuantitatPerUnitatDUnProducteAmbUnValorNegatiu()
            throws Exception {

        double novaQuantitatPerUnitat = -5.7;
        Usuari usuari = donatUnUsuari(Rol.PROVEIDOR);
        donatUnCodiDAccesValid();
        donatUnProducteDUnProveidor(usuari);
        ProducteVisualitzacio producteAEditar = donatUnProducteAEditar(idProducte, null, null, null, novaQuantitatPerUnitat, null, null, null, null);

        mvc.perform(put("/editarproducte/" + codiAcces)
                .content(objectmapper.writeValueAsString(producteAEditar))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("El valor del camp quantitat per unitat no pot ser negatiu", result.getResolvedException().getMessage()));
    }

    @Test
    public void editarLaDisponibilitatDUnProducte()
            throws Exception {

        Boolean disponible = false;
        Usuari usuari = donatUnUsuari(Rol.PROVEIDOR);
        donatUnCodiDAccesValid();
        donatUnProducteDUnProveidor(usuari);
        ProducteVisualitzacio producteAEditar = donatUnProducteAEditar(idProducte, null, null, null, null, null, null, disponible, null);

        mvc.perform(put("/editarproducte/" + codiAcces)
                .content(objectmapper.writeValueAsString(producteAEditar))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        ArgumentCaptor<Producte> argument = ArgumentCaptor.forClass(Producte.class);
        Mockito.verify(producteRepository).save(argument.capture());
        assertThat(argument.getValue().isDisponible()).isFalse();
    }

    @Test
    public void editarLaImatgeDUnProducte()
            throws Exception {

        byte[] novaImatge = donatUnaImatgeEnBytes();
        Usuari usuari = donatUnUsuari(Rol.PROVEIDOR);
        donatUnCodiDAccesValid();
        donatUnProducteDUnProveidor(usuari);
        ProducteVisualitzacio producteAEditar = donatUnProducteAEditar(idProducte, null, null, null, null, null, null, null, novaImatge);

        mvc.perform(put("/editarproducte/" + codiAcces)
                .content(objectmapper.writeValueAsString(producteAEditar))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        ArgumentCaptor<Producte> argument = ArgumentCaptor.forClass(Producte.class);
        Mockito.verify(producteRepository).save(argument.capture());
        assertThat(argument.getValue().getImatge()).isEqualTo(novaImatge);
    }

    @Test
    public void obtenirProductesFiltratsDelProveidor()
            throws Exception {

        donatUnUsuari(Rol.PROVEIDOR);
        donatUnCodiDAccesValid();
        PeticioFiltrarProductes peticio = new PeticioFiltrarProductes();
        peticio.setCodiAcces(codiAcces);

        mvc.perform(post("/filtrarproductes")
                .content(objectmapper.writeValueAsString(peticio))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void obtenirProductesFiltratsPerUnUsuariClient()
            throws Exception {

        donatUnUsuari(Rol.CLIENT);
        donatUnCodiDAccesValid();
        PeticioFiltrarProductes peticio = new PeticioFiltrarProductes();
        peticio.setCodiAcces(codiAcces);

        mvc.perform(post("/filtrarproductes")
                .content(objectmapper.writeValueAsString(peticio))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void obtenirProductesFiltratsPerUnUsuariAdministrador()
            throws Exception {

        donatUnUsuari(Rol.ADMINISTRADOR);
        donatUnCodiDAccesValid();
        PeticioFiltrarProductes peticio = new PeticioFiltrarProductes();
        peticio.setCodiAcces(codiAcces);

        mvc.perform(post("/filtrarproductes")
                .content(objectmapper.writeValueAsString(peticio))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private void donatUnProducteDUnAltreProveidor() throws IOException {
        Usuari unAltreProveidor = donatUnUsuari(Rol.PROVEIDOR);
        unAltreProveidor.setIdUsuari("unAltreIdUsuari");
        donatUnProducteDUnProveidor(unAltreProveidor);
    }

    private ProducteVisualitzacio donatUnProducteAEditar(String idProducte, String nom, Categoria tipus, String unitat, Double quantitat, Double preu, String descripcio, Boolean disponible, byte[] imatge) {
        return new ProducteVisualitzacio(idProducte, idUsuari, nom, tipus, unitat, quantitat, preu, descripcio, disponible, imatge);
    }

    private Producte donatUnProducteDUnProveidor(Usuari usuari) throws IOException {
        Producte producte = donatUnProducte(nom, tipus, preu, unitat, quantitat, disponible);
        producte.setUsuari(usuari);
        Mockito.when(producteRepository.findByIdProducte(idProducte)).thenReturn(producte);
        return producte;
    }

    private void donatUnCodiDAccesValid() {
        Mockito.when(controlAcces.ValidarCodiAcces(codiAcces)).thenReturn(idUsuari);
    }

    private PeticioAltraProducte donadaUnaPeticioDAltaDeProducte(Producte producte) {
        PeticioAltraProducte peticio = new PeticioAltraProducte();
        peticio.setCodiAcces(codiAcces);
        peticio.setProducte(producte);
        return peticio;
    }

    private Producte donatUnProducte(String nom, Categoria tipus, double preu, String unitat, double quantitat, Boolean disponible) throws IOException {
        byte[] imatgeEnByte = donatUnaImatgeEnBytes();

        Producte producte = new Producte();
        producte.setNom(nom);
        producte.setDescripcio(descripcio);
        producte.setTipus(tipus);
        producte.setPreu(preu);
        producte.setUnitats(unitat);
        producte.setQuantitatPerUnitat(quantitat);
        producte.setDisponible(disponible);
        producte.setImatge(imatgeEnByte);

        return producte;
    }

    private byte[] donatUnaImatgeEnBytes() throws IOException {
        BufferedImage bImage = ImageIO.read(new File("src/test/java/botigaprop/servidor/imatgepeix.png"));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bImage, "png", bos );
        byte [] imatgeEnByte = bos.toByteArray();
        return imatgeEnByte;
    }

    private Usuari donatUnUsuari(Rol rol) {
        Usuari usuari = new Usuari();
        usuari.setIdUsuari(idUsuari);
        usuari.setRol(rol);

        Mockito.when(usuariRepository.findByIdUsuari(idUsuari)).thenReturn(usuari);

        return usuari;
    }
}
