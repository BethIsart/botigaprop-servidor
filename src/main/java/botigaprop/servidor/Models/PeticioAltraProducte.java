package botigaprop.servidor.Models;

/**
 * @author Elisabet Isart
 */
public class PeticioAltraProducte {

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
