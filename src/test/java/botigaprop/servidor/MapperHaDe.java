package botigaprop.servidor;

import botigaprop.servidor.Models.*;
import botigaprop.servidor.Services.Mapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class MapperHaDe {

    private final String idUsuari = "idUsuari";
    private final String nom = "unProducte";
    private final String descripcio = "unaDescripcio";
    private final Categoria tipus = Categoria.Peix;
    private final double preu = 10;
    private final String unitat = "bossa";
    private final double quantitat = 1;
    private final boolean disponible = true;
    private final String idProducte = "idProducte";
    private final Mapper mapper;

    public MapperHaDe() { mapper = new Mapper();}

    @Test
    public void RetornarUnProducteAMostrar() {
        Producte producte = donatUnProducte();
        ProducteVisualitzacio producteEsperat = donatUnProducteAMostrarEsperat();

        ProducteVisualitzacio resultat = mapper.ProducteAMostrar(producte);

        assertThat(resultat.getNom()).isEqualTo(producteEsperat.getNom());
        assertThat(resultat.getDescripcio()).isEqualTo(producteEsperat.getDescripcio());
        assertThat(resultat.getTipus()).isEqualTo(producteEsperat.getTipus());
        assertThat(resultat.getPreu()).isEqualTo(producteEsperat.getPreu());
        assertThat(resultat.getUnitats()).isEqualTo(producteEsperat.getUnitats());
        assertThat(resultat.getDisponible()).isEqualTo(producteEsperat.getDisponible());
        assertThat(resultat.getQuantitatPerUnitat()).isEqualTo(producteEsperat.getQuantitatPerUnitat());
        assertThat(resultat.getIdProveidor()).isEqualTo(producteEsperat.getIdProveidor());
        assertThat(resultat.getIdProducte()).isEqualTo(producteEsperat.getIdProducte());
    }

    private ProducteVisualitzacio donatUnProducteAMostrarEsperat() {
        return new ProducteVisualitzacio(idProducte, idUsuari, nom, tipus, unitat, quantitat, preu, descripcio, disponible, null);
    }

    private Producte donatUnProducte() {
        Producte producte = new Producte();
        producte.setIdProducte(idProducte);
        producte.setUsuari(donatUnUsuari());
        producte.setNom(nom);
        producte.setDescripcio(descripcio);
        producte.setTipus(tipus);
        producte.setPreu(preu);
        producte.setUnitats(unitat);
        producte.setQuantitatPerUnitat(quantitat);
        producte.setDisponible(disponible);
        producte.setImatge(null);

        return producte;
    }

    private Usuari donatUnUsuari() {
        Usuari usuari = new Usuari();
        usuari.setIdUsuari(idUsuari);
        usuari.setRol(Rol.PROVEIDOR);
        return usuari;
    }
}
