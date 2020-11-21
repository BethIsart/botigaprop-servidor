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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ProducteController.class)
public class ProducteControllerHaDe {

    private final String idUsuari = "iddUsuari";
    private final String codiAcces = "unCodiAcces";
    private final String nom = "unProducte";
    private final String descripcio = "unaDescripcio";
    private final Categoria tipus = Categoria.Peix;
    private final int preu = 10;
    private final String unitat = "bossa";
    private final int quantitat = 1;
    private final boolean disponible = true;

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
        Producte nouProducte = donatUnNouProducte(nom, tipus, preu, unitat, quantitat, disponible);
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
        Producte nouProducte = donatUnNouProducte(nom, tipus, preu, unitat, quantitat, disponible);
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
        Producte nouProducte = donatUnNouProducte(null, tipus, preu, unitat, quantitat, disponible);
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
        Producte nouProducte = donatUnNouProducte(nom, null, preu, unitat, quantitat, disponible);
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
        Producte nouProducte = donatUnNouProducte(nom, tipus, -1, unitat, quantitat, disponible);
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
        Producte nouProducte = donatUnNouProducte(nom, tipus, 0, unitat, quantitat, disponible);
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
        Producte nouProducte = donatUnNouProducte(nom, tipus, preu, null, quantitat, disponible);
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
        Producte nouProducte = donatUnNouProducte(nom, tipus, preu, unitat, -2, disponible);
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
        Producte nouProducte = donatUnNouProducte(nom, tipus, preu, unitat, quantitat, disponible);
        PeticioAltraProducte peticio = donadaUnaPeticioDAltaDeProducte(nouProducte);

        mvc.perform(post("/altaproducte")
                .content(objectmapper.writeValueAsString(peticio))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Producte donat d'alta amb l'identificador ")));

        Mockito.verify(producteRepository).save(Mockito.any());
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

    private Producte donatUnNouProducte(String nom, Categoria tipus, int preu, String unitat, int quantitat, Boolean disponible) throws IOException {
        BufferedImage bImage = ImageIO.read(new File("src/test/java/botigaprop/servidor/imatgepeix.png"));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bImage, "png", bos );
        byte [] imatgeEnByte = bos.toByteArray();

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

    private Usuari donatUnUsuari(Rol rol) {
        Usuari usuari = new Usuari();
        usuari.setIdUsuari(idUsuari);
        usuari.setRol(rol);

        Mockito.when(usuariRepository.findByIdUsuari(idUsuari)).thenReturn(usuari);

        return usuari;
    }
}
