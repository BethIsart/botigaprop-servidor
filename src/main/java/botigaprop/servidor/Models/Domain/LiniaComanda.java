package botigaprop.servidor.Models.Domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author Elisabet Isart
 */
@Entity
@Table(name = "LiniesComanda")
public class LiniaComanda {

    @Id
    private String idLiniaComanda;

    @ManyToOne()
    private Comanda comanda;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "idProducte", referencedColumnName = "idProducte")
    private Producte producte;
    @NotNull
    private int unitats;

    public String getIdLiniaComanda() {
        return idLiniaComanda;
    }

    public void setIdLiniaComanda(String idLiniaComanda) {
        this.idLiniaComanda = idLiniaComanda;
    }

    public Comanda getComanda() {
        return comanda;
    }

    public void setComanda(Comanda comanda) {
        this.comanda = comanda;
    }

    public Producte getProducte() {
        return producte;
    }

    public void setProducte(Producte producte) {
        this.producte = producte;
    }

    public int getUnitats() {
        return unitats;
    }

    public void setUnitats(int unitats) {
        this.unitats = unitats;
    }
}
