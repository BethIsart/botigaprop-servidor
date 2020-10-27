package botigaprop.servidor;

import botigaprop.servidor.Controllers.UsuariController;
import botigaprop.servidor.Models.Rol;
import botigaprop.servidor.Models.Usuari;
import botigaprop.servidor.Persistence.UsuariRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@WebMvcTest(UsuariController.class)
public class UsuariControllerHaDe {

    private final String nom = "unNom";
    private final String cif = "unCif";
    private final String direccio = "unaDireccio";
    private final String poblacio = "unaPoblacio";
    private final String provincia = "unaProvincia";
    @Autowired
    private MockMvc mvc;
    @Autowired
    ObjectMapper objectmapper;
    @MockBean
    private UsuariRepository usuariRepository;


    private String email = "email@test.com";
    private final String idUsuari = "unIdDeUsuari";
    private final String unaContrasenya = "unaContrasenya";

    @Test
    public void registrarUnNouUsuari()
        throws Exception {

        Usuari nouUsuari = donatUnNouUsuari(idUsuari, email, unaContrasenya, Rol.CLIENT, null, null, null, null, null);

        mvc.perform(post("/registre")
                .content(objectmapper.writeValueAsString(nouUsuari))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Usuari registrat correctament amb el id")));
    }

    @Test
    public void retornarErrorSiNoSInformaLEmailAlRegistrarUnNouUsuari()
        throws Exception {

        Usuari nouUsuari = donatUnNouUsuari(idUsuari, null, unaContrasenya, Rol.CLIENT, null, null, null, null, null);

        mvc.perform(post("/registre")
                .content(objectmapper.writeValueAsString(nouUsuari))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("El camp email és obligatori", result.getResolvedException().getMessage()));
    }

    @Test
    public void retornarErrorSiNoSInformaLaContrasenyaAlRegistrarUnNouUsuari()
            throws Exception {

        Usuari nouUsuari = donatUnNouUsuari(idUsuari, email, null, Rol.CLIENT, null, null, null, null, null);

        mvc.perform(post("/registre")
                .content(objectmapper.writeValueAsString(nouUsuari))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("El camp contrasenya és obligatori", result.getResolvedException().getMessage()));
    }

    @Test
    public void retornarErrorSiNoSInformaElRolAlRegistrarUnNouUsuari()
            throws Exception {

        Usuari nouUsuari = donatUnNouUsuari(idUsuari, email, unaContrasenya, null, null, null, null, null, null);

        mvc.perform(post("/registre")
                .content(objectmapper.writeValueAsString(nouUsuari))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("El camp rol és obligatori", result.getResolvedException().getMessage()));
    }

    @Test
    public void retornarErrorSiNoSInformaElNomAlRegistrarUnNouUsuariAmbRolProveidor()
            throws Exception {

        Usuari nouUsuari = donatUnNouUsuari(idUsuari, email, unaContrasenya, Rol.PROVEIDOR, null, cif, direccio, poblacio, provincia);

        mvc.perform(post("/registre")
                .content(objectmapper.writeValueAsString(nouUsuari))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("El camp nom és obligatori", result.getResolvedException().getMessage()));
    }

    @Test
    public void retornarErrorSiNoSInformaElCifAlRegistrarUnNouUsuariAmbRolProveidor()
            throws Exception {

        Usuari nouUsuari = donatUnNouUsuari(idUsuari, email, unaContrasenya, Rol.PROVEIDOR, nom, null, direccio, poblacio, provincia);

        mvc.perform(post("/registre")
                .content(objectmapper.writeValueAsString(nouUsuari))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("El camp CIF és obligatori", result.getResolvedException().getMessage()));
    }

    @Test
    public void retornarErrorSiNoSInformaLaDireccioAlRegistrarUnNouUsuariAmbRolProveidor()
            throws Exception {

        Usuari nouUsuari = donatUnNouUsuari(idUsuari, email, unaContrasenya, Rol.PROVEIDOR, nom, cif, null, poblacio, provincia);

        mvc.perform(post("/registre")
                .content(objectmapper.writeValueAsString(nouUsuari))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("El camp direcció és obligatori", result.getResolvedException().getMessage()));
    }

    @Test
    public void retornarErrorSiNoSInformaLaPoblacioAlRegistrarUnNouUsuariAmbRolProveidor()
            throws Exception {

        Usuari nouUsuari = donatUnNouUsuari(idUsuari, email, unaContrasenya, Rol.PROVEIDOR, nom, cif, direccio, null, provincia);

        mvc.perform(post("/registre")
                .content(objectmapper.writeValueAsString(nouUsuari))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("El camp població és obligatori", result.getResolvedException().getMessage()));
    }

    @Test
    public void retornarErrorSiNoSInformaLaProvinciaAlRegistrarUnNouUsuariAmbRolProveidor()
            throws Exception {

        Usuari nouUsuari = donatUnNouUsuari(idUsuari, email, unaContrasenya, Rol.PROVEIDOR, nom, cif, direccio, poblacio, null);

        mvc.perform(post("/registre")
                .content(objectmapper.writeValueAsString(nouUsuari))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("El camp provincia és obligatori", result.getResolvedException().getMessage()));
    }

    @Test
    public void retornarErrorSiJaExisteixUnUsuariActiuAmbElEmailIndicatAlRegistrarUnNouUsuari()
            throws Exception {

        Usuari nouUsuari = donatUnNouUsuari(idUsuari, email, unaContrasenya, Rol.CLIENT, null, null, null, null, null);
        DonatUnUsuariRegistratAmbElMateixEmail();

        mvc.perform(post("/registre")
                .content(objectmapper.writeValueAsString(nouUsuari))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("Ja existeix un usuari actiu amb aquest email", result.getResolvedException().getMessage()));
    }

    @Test
    public void inicialitzarElIdentificadorDeUsuariAlRegistarUnNouUsuari()
        throws Exception {

        Usuari nouUsuari = donatUnNouUsuari(idUsuari, email, unaContrasenya, Rol.CLIENT, null, null, null, null, null);

        mvc.perform(post("/registre")
                .content(objectmapper.writeValueAsString(nouUsuari))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Usuari registrat correctament amb el id")));

        assertThat(nouUsuari.getIdUsuari()).isNotNull();
    }

    @Test
    public void inicialitzarLaContrasenyaXifradaAlRegistarUnNouUsuari()
            throws Exception {

        Usuari nouUsuari = donatUnNouUsuari(idUsuari, email, unaContrasenya, Rol.CLIENT, null, null, null, null, null);

        mvc.perform(post("/registre")
                .content(objectmapper.writeValueAsString(nouUsuari))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Usuari registrat correctament amb el id")));

        assertThat(nouUsuari.getEmail()).isNotEqualTo(unaContrasenya);
    }

    @Test
    public void guardarElNouUsuari()
        throws Exception {

        Usuari nouUsuari = donatUnNouUsuari(idUsuari, email, unaContrasenya, Rol.CLIENT, null, null, null, null, null);

        mvc.perform(post("/registre")
                .content(objectmapper.writeValueAsString(nouUsuari))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Usuari registrat correctament amb el id")));

        Mockito.verify(usuariRepository).save(Mockito.any());
    }


    private void DonatUnUsuariRegistratAmbElMateixEmail() {
        List<Usuari> usuari = new ArrayList<Usuari>(Arrays.asList(new Usuari()));
        Mockito.when(usuariRepository.findUsuariByEmailAndDeshabilitatIsFalse(email)).thenReturn(usuari);
    }

    private Usuari donatUnNouUsuari(String idUsuari, String email, String unaContrasenya, Rol rol, String nom, String cif, String direccio, String poblacio, String provincia) {

        Usuari usuari = new Usuari();
        usuari.setIdUsuari(idUsuari);
        usuari.setEmail(email);
        usuari.setContrasenya(unaContrasenya);
        usuari.setRol(rol);
        usuari.setNom(nom);
        usuari.setCifEmpresa(cif);
        usuari.setDireccio(direccio);
        usuari.setPoblacio(poblacio);
        usuari.setProvincia(provincia);

        return usuari;
    }
}
