package botigaprop.servidor.Models;

import java.util.List;

/**
 * @author Elisabet Isart
 */
public class PeticioAltaComanda {

    private String codiAcces;

    private List<String> idProductes;

    public String getCodiAcces() {
        return codiAcces;
    }

    public void setCodiAcces(String codiAcces) {
        this.codiAcces = codiAcces;
    }

    public List<String> getIdProductes() {
        return idProductes;
    }

    public void setIdProductes(List<String> idProductes) {
        this.idProductes = idProductes;
    }
}
