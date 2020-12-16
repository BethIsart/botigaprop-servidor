package botigaprop.servidor;

import botigaprop.servidor.Models.Domain.*;
import botigaprop.servidor.Persistence.ComandaRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ComandaRepositoryIntegrationTest {
    private final String idComanda = "idComanda";
    private final String idLiniaComanda = "idLiniaComanda";    private final String idUsuari1 = "idUsuari";
    private final String idUsuari2 = "idUsuari2";
    private final String idUsuari3 = "idUsuari3";
    private final Usuari usuari = new Usuari();
    private final Usuari usuari2 = new Usuari();
    private final Usuari usuari3 = new Usuari();
    private final Producte producte = new Producte();

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private ComandaRepository comandaRepository;
    
    @Test
    public void quanBuscaPerProveidorAleshoresRetornaUnaComanda() {
        donatUnUsuariProveidor(idUsuari1);
        donatUnAltreUsuariProveidor(idUsuari2);
        donatUnUsuariClient(idUsuari3);
        donatUnProducte();
        donadesDuesComandesDeLesQualsUnaDelProveidor();

        Page<Comanda> pageComandes = comandaRepository.findComandaByProveidor(usuari, PageRequest.of(0, 5));
        List<Comanda> comandesTrobades = pageComandes.getContent();

        assertThat(comandesTrobades.size()).isEqualTo(1);
        assertThat(comandesTrobades.get(0).getProveidor()).isEqualTo(usuari);
    }

    @Test
    public void quanBuscaPerClientAleshoresRetornaUnaComanda() {
        donatUnUsuariProveidor(idUsuari1);
        donatUnUsuariClient(idUsuari3);
        donatUnAltreUsuariClient(idUsuari2);
        donatUnProducte();
        donadesDuesComandesDeLesQualsUnaDelClient();

        Page<Comanda> pageComandes = comandaRepository.findComandaByClient(usuari2, PageRequest.of(0, 5));
        List<Comanda> comandesTrobades = pageComandes.getContent();

        assertThat(comandesTrobades.size()).isEqualTo(1);
        assertThat(comandesTrobades.get(0).getProveidor()).isEqualTo(usuari);
    }

    @Test
    public void quanBuscaPerIdDeComandaAleshoresRetornaDuesComandes() {
        donatUnUsuariProveidor(idUsuari1);
        donatUnUsuariClient(idUsuari3);
        donatUnProducte();
        donadaUnaComanda(usuari, idComanda, idLiniaComanda, usuari3);

        Comanda comandaTrobada = comandaRepository.findByIdComanda(idComanda);

        assertThat(comandaTrobada.getIdComanda()).isEqualTo(idComanda);
    }

    private void donadesDuesComandesDeLesQualsUnaDelClient() {
        donadaUnaComanda(usuari, idComanda, idLiniaComanda, usuari2);
        donadaUnaComanda(usuari, "idComanda2", "idLiniaComanda2", usuari3);
    }

    private void donadesDuesComandesDeLesQualsUnaDelProveidor() {
        donadaUnaComanda(usuari, idComanda, idLiniaComanda, usuari3);
        donadaUnaComanda(usuari2, "idComanda2", "idLiniaComanda2", usuari3);
    }

    private void donadaUnaComanda(Usuari proveidor, String idComanda, String idLiniaComanda, Usuari client)
    {
        Comanda comanda = new Comanda();
        comanda.setIdComanda(idComanda);
        comanda.setClient(client);
        comanda.setProveidor(proveidor);
        comanda.setDataCreacio(new Date());
        comanda.setCancellat(false);
        LiniaComanda linia = new LiniaComanda();
        linia.setUnitats(3);
        linia.setProducte(producte);
        linia.setIdLiniaComanda(idLiniaComanda);
        List<LiniaComanda> linies = new ArrayList<>();
        linies.add(linia);
        comanda.setLinies(linies);
        entityManager.persist(comanda);
        entityManager.flush();
    }

    private void donatUnUsuariProveidor(String idUsuari) {
        usuari.setIdUsuari(idUsuari);
        usuari.setRol(Rol.PROVEIDOR);
        usuari.setEmail("unEmail@test.cat");
        usuari.setContrasenya("unaContrasenya");
        entityManager.persist(usuari);
        entityManager.flush();
    }

    private void donatUnAltreUsuariProveidor(String idUsuari) {
        usuari2.setIdUsuari(idUsuari);
        usuari2.setRol(Rol.PROVEIDOR);
        usuari2.setEmail("unEmail@test.cat");
        usuari2.setContrasenya("unaContrasenya");
        entityManager.persist(usuari2);
        entityManager.flush();
    }

    private void donatUnUsuariClient(String idUsuari) {
        usuari3.setIdUsuari(idUsuari);
        usuari3.setRol(Rol.CLIENT);
        usuari3.setEmail("unEmail@test.cat");
        usuari3.setContrasenya("unaContrasenya");
        entityManager.persist(usuari3);
        entityManager.flush();
    }

    private void donatUnAltreUsuariClient(String idUsuari) {
        usuari2.setIdUsuari(idUsuari);
        usuari2.setRol(Rol.CLIENT);
        usuari2.setEmail("unEmail@test.cat");
        usuari2.setContrasenya("unaContrasenya");
        entityManager.persist(usuari2);
        entityManager.flush();
    }

    private void donatUnProducte() {
        producte.setIdProducte("idProducte");
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
