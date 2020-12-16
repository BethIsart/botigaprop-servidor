package botigaprop.servidor.Models.Requests;

import java.util.Date;

/**
 * @author Elisabet Isart
 */
public class ValoracioProducteVisualitzacio {
    private Date dataCreacio;
    private Integer puntuacio;
    private String comentari;

    public ValoracioProducteVisualitzacio(Date dataCreacio, Integer puntuacio, String comentari) {
        this.dataCreacio = dataCreacio;
        this.puntuacio = puntuacio;
        this.comentari = comentari;
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
}
