package botigaprop.servidor;

import botigaprop.servidor.Models.Domain.Categoria;
import botigaprop.servidor.Models.Domain.Producte;
import botigaprop.servidor.Models.Domain.Rol;
import botigaprop.servidor.Models.Domain.Usuari;
import botigaprop.servidor.Persistence.ProducteRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ProducteRepositoryIntegrationTest {

    private final String idUsuari = "idUsuari";
    private final Usuari usuari1 = new Usuari();
    private final Usuari usuari2 = new Usuari();
    private final String idProducte1 = "idProducte1";

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private ProducteRepository producteRepository;

    @Test
    public void quanBuscaPerIdUsuariINoEliminatsAleshoresRetornaUnProducte() throws IOException {
        donatUnUsuari(idUsuari);
        donatUnAltreUsuari("unAltreUsuari");
        donatTresProductesDelsQualsUnEliminatIUnDeDiferentProveidor();

        List<Producte> productesTrobats = producteRepository.findProducteByIdUsuariAndEliminatIsFalse(usuari1);

        assertThat(productesTrobats.size()).isEqualTo(1);
        assertThat(productesTrobats.get(0).getEliminat()).isFalse();
        assertThat(productesTrobats.get(0).getUsuari()).isEqualTo(usuari1);
    }

    @Test
    public void quanBuscaPerIdUsuariAleshoresRetornaDosProductes() throws IOException {
        donatUnUsuari(idUsuari);
        donatUnAltreUsuari("unAltreUsuari");
        donatTresProductesDelsQualsUnEliminatIUnDeDiferentProveidor();

        List<Producte> productesTrobats = producteRepository.findProducteByEliminatIsFalse();

        assertThat(productesTrobats.size()).isEqualTo(2);
    }

    @Test
    public void quanBuscaPerIdDeProducteAleshoresRetornaUnProducte() throws IOException {
        donatUnUsuari(idUsuari);
        donatUnAltreUsuari("unAltreUsuari");
        donatTresProductesDelsQualsUnEliminatIUnDeDiferentProveidor();

        Producte producteTrobat = producteRepository.findByIdProducte(idProducte1);

        assertThat(producteTrobat.getIdProducte()).isEqualTo(idProducte1);
    }

    private void donatTresProductesDelsQualsUnEliminatIUnDeDiferentProveidor() throws IOException {
        donatUnProducte(usuari1, false, idProducte1);
        donatUnProducte(usuari1, true, "idProducte2");
        donatUnProducte(usuari2, false, "idProducte3");
    }

    private void donatUnProducte(Usuari usuari, Boolean eliminat, String idProducte) throws IOException {
        Producte producte = new Producte();
        producte.setIdProducte(idProducte);
        producte.setUsuari(usuari);
        producte.setNom("unNom");
        producte.setDescripcio("unaDescripcio");
        producte.setTipus(Categoria.Varis);
        producte.setPreu(10);
        producte.setUnitats("bossa");
        producte.setQuantitatPerUnitat(1);
        producte.setDisponible(true);
        producte.setImatge(donatUnaImatgeEnBytes());
        producte.setEliminat(eliminat);
        entityManager.persist(producte);
        entityManager.flush();
    }

    private void donatUnUsuari(String idUsuari) {
        usuari1.setIdUsuari(idUsuari);
        usuari1.setRol(Rol.PROVEIDOR);
        usuari1.setEmail("unEmail@test.cat");
        usuari1.setContrasenya("unaContrasenya");
        entityManager.persist(usuari1);
        entityManager.flush();
    }

    private void donatUnAltreUsuari(String idUsuari) {
        usuari2.setIdUsuari(idUsuari);
        usuari2.setRol(Rol.PROVEIDOR);
        usuari2.setEmail("unAltreEmail@test.cat");
        usuari2.setContrasenya("unaContrasenya");
        entityManager.persist(usuari2);
        entityManager.flush();
    }

    private byte[] donatUnaImatgeEnBytes() throws IOException {
        BufferedImage bImage = ImageIO.read(new File("src/test/java/botigaprop/servidor/imatgepeix.png"));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bImage, "png", bos );
        byte [] imatgeEnByte = bos.toByteArray();
        return imatgeEnByte;
    }
}
