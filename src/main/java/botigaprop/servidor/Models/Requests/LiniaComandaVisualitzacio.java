package botigaprop.servidor.Models.Requests;

/**
 * @author Elisabet Isart
 */
public class LiniaComandaVisualitzacio {
    private String nomProducte;
    private int unitats;

    public LiniaComandaVisualitzacio(String nomProducte, int unitats) {
        this.nomProducte = nomProducte;
        this.unitats = unitats;
    }

    public String getNomProducte() {
        return nomProducte;
    }

    public void setNomProducte(String nomProducte) {
        this.nomProducte = nomProducte;
    }

    public int getUnitats() {
        return unitats;
    }

    public void setUnitats(int unitats) {
        this.unitats = unitats;
    }
}
