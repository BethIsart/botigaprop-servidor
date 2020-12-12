package botigaprop.servidor.Models.Domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author Elisabet Isart
 */
@Entity
@Table(name = "ValoracionsProducte")
public class ValoracioProducte {

    @Id
    private String idValoracio;
    private Date dataCreacio;
    private Integer puntuacio;
    private String comentari;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "idClient", referencedColumnName = "idUsuari")
    private Usuari client;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "idProducte", referencedColumnName = "idProducte")
    private Producte producte;

    public String getIdValoracio() {
        return idValoracio;
    }

    public void setIdValoracio(String idComanda) {
        this.idValoracio = idComanda;
    }

    public Date getDataCreacio() {
        return dataCreacio;
    }

    public void setDataCreacio(Date dataCreacio) {
        this.dataCreacio = dataCreacio;
    }

    public Integer getPuntuacio() {
        return puntuacio;
    }

    public void setPuntuacio(Integer puntuacio) {
        this.puntuacio = puntuacio;
    }

    public String getComentari() {
        return comentari;
    }

    public void setComentari(String comentari) {
        this.comentari = comentari;
    }

    public Usuari getClient() {
        return client;
    }

    public void setClient(Usuari client) {
        this.client = client;
    }

    public Producte getProducte() {
        return producte;
    }

    public void setProducte(Producte producte) {
        this.producte = producte;
    }
}
