package botigaprop.servidor.Models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Elisabet Isart
 */
@Entity
@Table(name = "Comandes")
public class Comanda {

    @Id
    private String idComanda;
    private Date dataCreacio;
    private boolean cancellat;
    @ManyToOne
    @JoinColumn(name = "idUsuari", referencedColumnName = "idUsuari")
    private Usuari idUsuari;

    @JoinTable(
            name = "linies_comanda",
            joinColumns = @JoinColumn(name = "idComanda", nullable = false),
            inverseJoinColumns = @JoinColumn(name="idProducte", nullable = false)
    )
    @ManyToMany()
    private List<Producte> productes;

    public String getIdComanda() {
        return idComanda;
    }

    public void setIdComanda(String idComanda) {
        this.idComanda = idComanda;
    }

    public Date getDataCreacio() {
        return dataCreacio;
    }

    public void setDataCreacio(Date dataCreacio) {
        this.dataCreacio = dataCreacio;
    }

    public boolean isCancellat() {
        return cancellat;
    }

    public void setCancellat(boolean cancellat) {
        this.cancellat = cancellat;
    }

    public Usuari getIdUsuari() {
        return idUsuari;
    }

    public void setIdUsuari(Usuari idUsuari) {
        this.idUsuari = idUsuari;
    }

    public List<Producte> getProductes() {
        return productes;
    }

    public void setProductes(List<Producte> productes) {
        this.productes = productes;
    }
}


