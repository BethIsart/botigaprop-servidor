package botigaprop.servidor.Models.Requests;

import botigaprop.servidor.Models.Domain.Producte;

/**
 * @author Elisabet Isart
 */
public class PeticioAltaProducte {

    private String codiAcces;
    private Producte producte;

    public String getCodiAcces() {
        return codiAcces;
    }

    public void setCodiAcces(String codiAcces) {
        this.codiAcces = codiAcces;
    }

    public Producte getProducte() {
        return producte;
    }

    public void setProducte(Producte producte) {
        this.producte = producte;
    }
}
