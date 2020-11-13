package botigaprop.servidor.Models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author Elisabet Isart
 */
@Entity
@Table(name = "Productes")
public class Producte {
    @Id
    private String idProducte;
    @ManyToOne
    @JoinColumn(name = "idUsuari", referencedColumnName = "idUsuari")
    private Usuari idUsuari;
    @NotNull
    private String nom;
    private String descripcio;
    @NotNull
    private Categoria tipus;
    @NotNull
    private double preu;
    @NotNull
    private String unitats;
    private double quantitatPerUnitat;
    private Boolean disponible;
    private byte[] imatge;
    private Date dataCreacio;
    private Date dataUltimaEdicio;

    public String getIdProducte() {
        return idProducte;
    }

    public void setIdProducte(String idProducte) {
        this.idProducte = idProducte;
    }

    public Usuari getIdUsuari() {
        return idUsuari;
    }

    public void setIdUsuari(Usuari idUsuari) {
        this.idUsuari = idUsuari;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }

    public Categoria getTipus() {
        return tipus;
    }

    public void setTipus(Categoria categoria) {
        this.tipus = categoria;
    }

    public double getPreu() {
        return preu;
    }

    public void setPreu(double preu) {
        this.preu = preu;
    }

    public String getUnitats() {
        return unitats;
    }

    public void setUnitats(String unitat) {
        this.unitats = unitat;
    }

    public double getQuantitatPerUnitat() {
        return quantitatPerUnitat;
    }

    public void setQuantitatPerUnitat(double quantitat) {
        this.quantitatPerUnitat = quantitat;
    }

    public Boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    public Date getDataCreacio() {
        return dataCreacio;
    }

    public void setDataCreacio(Date dataCreacio) {
        this.dataCreacio = dataCreacio;
    }

    public Date getDataUltimaEdicio() {
        return dataUltimaEdicio;
    }

    public void setDataUltimaEdicio(Date dataUltimaEdicio) {
        this.dataUltimaEdicio = dataUltimaEdicio;
    }

    public byte[] getImatge() {
        return imatge;
    }

    public void setImatge(byte[] imatge) {
        this.imatge = imatge;
    }
}
