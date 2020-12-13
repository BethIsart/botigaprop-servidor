package botigaprop.servidor;

import botigaprop.servidor.Models.Domain.*;
import botigaprop.servidor.Models.Requests.ComandaVisualitzacio;
import botigaprop.servidor.Models.Requests.LiniaComandaVisualitzacio;
import botigaprop.servidor.Models.Requests.ProducteVisualitzacio;
import botigaprop.servidor.Models.Requests.ValoracioProducteVisualitzacio;
import botigaprop.servidor.Services.Mapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private final String email = "unEmail";
    private final Date dataCreacio = new Date();
    private final String idComanda = "idComanda";
    private final boolean cancellat = true;
    private final int unitats = 2;
    private final String direccioDEnviament = "unaDireccioDEnviament";
    private final int puntuacio = 3;
    private final String comentari = "unComentari";

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

    @Test
    public void RetornarUnaComandaAMostrar() {
        Comanda comanda = donatUnaComanda();
        ComandaVisualitzacio comandaEsperada = donatUnaComandaAMostrarEsperada();

        ComandaVisualitzacio resultat = mapper.ComandaAMostrar(comanda);

        assertThat(resultat.getIdComanda()).isEqualTo(comandaEsperada.getIdComanda());
        assertThat(resultat.getEmailClient()).isEqualTo(comandaEsperada.getEmailClient());
        assertThat(resultat.getDataCreacio()).isEqualTo(comandaEsperada.getDataCreacio());
        assertThat(resultat.isCancellat()).isEqualTo(comandaEsperada.isCancellat());
        assertThat(resultat.getLiniesComanda().get(0).getNomProducte()).isEqualTo(comandaEsperada.getLiniesComanda().get(0).getNomProducte());
        assertThat(resultat.getLiniesComanda().get(0).getUnitats()).isEqualTo(comandaEsperada.getLiniesComanda().get(0).getUnitats());
    }

    @Test
    public void RetornarUnaComandaAMostrarAmbDadesDeDistribucio() {
        Comanda comanda = donatUnaComandaAmbDistribucio();
        ComandaVisualitzacio comandaEsperada = donatUnaComandaAmbDistribucioAMostrarEsperada();

        ComandaVisualitzacio resultat = mapper.ComandaAMostrar(comanda);

        assertThat(resultat.getIdComanda()).isEqualTo(comandaEsperada.getIdComanda());
        assertThat(resultat.getEmailClient()).isEqualTo(comandaEsperada.getEmailClient());
        assertThat(resultat.getDataCreacio()).isEqualTo(comandaEsperada.getDataCreacio());
        assertThat(resultat.isCancellat()).isEqualTo(comandaEsperada.isCancellat());
        assertThat(resultat.getLiniesComanda().get(0).getNomProducte()).isEqualTo(comandaEsperada.getLiniesComanda().get(0).getNomProducte());
        assertThat(resultat.getLiniesComanda().get(0).getUnitats()).isEqualTo(comandaEsperada.getLiniesComanda().get(0).getUnitats());
        assertThat(resultat.getDireccioEnviament()).isEqualTo(comandaEsperada.getDireccioEnviament());
        assertThat(resultat.getHorariEnviament()).isEqualTo(comandaEsperada.getHorariEnviament());
    }

    @Test
    public void RetornarUnaValoracioDeProducteAMostrar() {
        ValoracioProducte valoracioProducte = donatUnaValoracioDeProducte();
        ValoracioProducteVisualitzacio valoracioEsperada = donatUnaValoracioDeProducteAMostrarEsperada();

        ValoracioProducteVisualitzacio resultat = mapper.ValoracioAMostrar(valoracioProducte);

        assertThat(resultat.getComentari()).isEqualTo(valoracioEsperada.getComentari());
        assertThat(resultat.getDataCreacio()).isEqualTo(valoracioEsperada.getDataCreacio());
        assertThat(resultat.getPuntuacio()).isEqualTo(valoracioEsperada.getPuntuacio());
    }

    private ValoracioProducteVisualitzacio donatUnaValoracioDeProducteAMostrarEsperada() {
        ValoracioProducteVisualitzacio valoracio = new ValoracioProducteVisualitzacio(dataCreacio, puntuacio, comentari);
        return valoracio;
    }

    private ValoracioProducte donatUnaValoracioDeProducte() {
        ValoracioProducte valoracio = new ValoracioProducte();
        valoracio.setDataCreacio(dataCreacio);
        valoracio.setPuntuacio(puntuacio);
        valoracio.setComentari(comentari);
        valoracio.setClient(donatUnUsuari());
        return valoracio;
    }

    private ComandaVisualitzacio donatUnaComandaAmbDistribucioAMostrarEsperada() {
        Producte producte = donatUnProducte();
        LiniaComandaVisualitzacio liniaComandaVisualitzacio = new LiniaComandaVisualitzacio(producte.getNom(), unitats);
        List<LiniaComandaVisualitzacio> linies = new ArrayList<>();
        linies.add(liniaComandaVisualitzacio);
        ComandaVisualitzacio comandaVisualitzacio = new ComandaVisualitzacio(idComanda, email, dataCreacio, cancellat, linies);
        comandaVisualitzacio.setDireccioEnviament(direccioDEnviament);
        return comandaVisualitzacio;
    }

    private Comanda donatUnaComandaAmbDistribucio() {
        Comanda comanda = new Comanda();
        comanda.setIdComanda(idComanda);
        comanda.setClient(donatUnUsuari());
        comanda.setDataCreacio(dataCreacio);
        comanda.setCancellat(cancellat);
        LiniaComanda linia = new LiniaComanda();
        linia.setUnitats(unitats);
        linia.setProducte(donatUnProducte());
        List<LiniaComanda> linies = new ArrayList<>();
        linies.add(linia);
        comanda.setLinies(linies);
        Enviament enviament = new Enviament();
        enviament.setDireccio(direccioDEnviament);
        comanda.setEnviament(enviament);
        return comanda;
    }

    private ComandaVisualitzacio donatUnaComandaAMostrarEsperada() {
        Producte producte = donatUnProducte();
        LiniaComandaVisualitzacio liniaComandaVisualitzacio = new LiniaComandaVisualitzacio(producte.getNom(), unitats);
        List<LiniaComandaVisualitzacio> linies = new ArrayList<>();
        linies.add(liniaComandaVisualitzacio);
        ComandaVisualitzacio comandaVisualitzacio = new ComandaVisualitzacio(idComanda, email, dataCreacio, cancellat, linies);
        return comandaVisualitzacio;
    }

    private Comanda donatUnaComanda() {
        Comanda comanda = new Comanda();
        comanda.setIdComanda(idComanda);
        comanda.setClient(donatUnUsuari());
        comanda.setDataCreacio(dataCreacio);
        comanda.setCancellat(cancellat);
        LiniaComanda linia = new LiniaComanda();
        linia.setUnitats(unitats);
        linia.setProducte(donatUnProducte());
        List<LiniaComanda> linies = new ArrayList<>();
        linies.add(linia);
        comanda.setLinies(linies);
        return comanda;
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
        usuari.setEmail(email);
        return usuari;
    }
}
