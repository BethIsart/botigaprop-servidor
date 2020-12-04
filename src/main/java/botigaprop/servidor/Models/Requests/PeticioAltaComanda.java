package botigaprop.servidor.Models.Requests;

import botigaprop.servidor.Models.Domain.LiniaComanda;

import java.util.List;

/**
 * @author Elisabet Isart
 */
public class PeticioAltaComanda {

    private String codiAcces;
    private List<PeticioLiniaComanda> linies;

    public String getCodiAcces() {
        return codiAcces;
    }

    public void setCodiAcces(String codiAcces) {
        this.codiAcces = codiAcces;
    }

    public List<PeticioLiniaComanda> getLinies() {
        return linies;
    }

    public void setLinies(List<PeticioLiniaComanda> linies) {
        this.linies = linies;
    }


}
