package botigaprop.servidor.Models.Requests;

/**
 * @author Elisabet Isart
 */
public class PeticioAltaValoracio {

    private String codiAcces;
    private String idProducte;
    private Integer puntuacio;
    private String comentari;

    public String getCodiAcces() {
        return codiAcces;
    }

    public void setCodiAcces(String codiAcces) {
        this.codiAcces = codiAcces;
    }

    public String getIdProducte() {
        return idProducte;
    }

    public void setIdProducte(String idProducte) {
        this.idProducte = idProducte;
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
}
