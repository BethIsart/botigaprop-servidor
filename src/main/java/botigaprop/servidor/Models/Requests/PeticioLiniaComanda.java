package botigaprop.servidor.Models.Requests;

/**
 * @author Elisabet Isart
 */
public class PeticioLiniaComanda {

    private String idProducte;
    private int unitats;

    public String getIdProducte() {
        return idProducte;
    }

    public void setIdProducte(String idProducte) {
        this.idProducte = idProducte;
    }

    public int getUnitats() {
        return unitats;
    }

    public void setUnitats(int unitats) {
        this.unitats = unitats;
    }
}
