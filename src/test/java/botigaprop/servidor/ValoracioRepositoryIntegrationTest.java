package botigaprop.servidor;

import botigaprop.servidor.Models.Domain.*;
import botigaprop.servidor.Persistence.ValoracioRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ValoracioRepositoryIntegrationTest {

    private final Usuari usuari = new Usuari();
    private final Usuari usuariClient = new Usuari();
    private final Producte producte1 = new Producte();
    private final Producte producte2 = new Producte();

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private ValoracioRepository valoracioRepository;

    @Test
    public void quanBuscaPerProducteAleshoresRetornaDuesValoracions() {
        donatUnUsuariProveidor();
        donatUnUsuariClient();
        donatUnProducte(producte1, "idProducte");
        donatUnProducte(producte2, "idProducte2");
        donadesTresValoracionsDeProducteDeLesQualsDuesDelProducte();

        Page<ValoracioProducte> pageValoracions = valoracioRepository.findByProducte(producte1, PageRequest.of(0, 5));
        List<ValoracioProducte> valoracionsTrobades = pageValoracions.getContent();

        assertThat(valoracionsTrobades.size()).isEqualTo(2);
        assertThat(valoracionsTrobades.get(0).getProducte()).isEqualTo(producte1);
    }

    private void donadesTresValoracionsDeProducteDeLesQualsDuesDelProducte() {
        donadaUnaValoracioDeProducte("idValoracio", producte1);
        donadaUnaValoracioDeProducte("idValoracio2", producte2);
        donadaUnaValoracioDeProducte("idValoracio3", producte1);
    }

    private void donadaUnaValoracioDeProducte(String idValoracio, Producte producte) {
        ValoracioProducte valoracio = new ValoracioProducte();
        valoracio.setIdValoracio(idValoracio);
        valoracio.setClient(usuari);
        valoracio.setComentari("unComentari");
        valoracio.setPuntuacio(1);
        valoracio.setProducte(producte);
        entityManager.persist(valoracio);
        entityManager.flush();
    }

    private void donatUnUsuariProveidor() {
        usuari.setIdUsuari("idUsuari");
        usuari.setRol(Rol.PROVEIDOR);
        usuari.setEmail("unEmail@test.cat");
        usuari.setContrasenya("unaContrasenya");
        entityManager.persist(usuari);
        entityManager.flush();
    }

    private void donatUnUsuariClient() {
        usuariClient.setIdUsuari("idUsuariClient");
        usuariClient.setRol(Rol.CLIENT);
        usuariClient.setEmail("unEmail@test.cat");
        usuariClient.setContrasenya("unaContrasenya");
        entityManager.persist(usuariClient);
        entityManager.flush();
    }

    private void donatUnProducte(Producte producte, String idProducte) {
        producte.setIdProducte(idProducte);
        producte.setUsuari(usuari);
        producte.setNom("unNom");
        producte.setDescripcio("unaDescripcio");
        producte.setTipus(Categoria.Varis);
        producte.setPreu(10);
        producte.setUnitats("bossa");
        producte.setQuantitatPerUnitat(1);
        producte.setDisponible(true);
        producte.setEliminat(false);
        entityManager.persist(producte);
        entityManager.flush();
    }
}
