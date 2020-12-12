package botigaprop.servidor.Models.Requests;

import java.util.Date;
import java.util.List;

/**
 * @author Elisabet Isart
 */
public class ComandaVisualitzacio {
    private String idComanda;
    private String emailClient;
    private Date dataCreacio;
    private boolean cancellat;
    private List<LiniaComandaVisualitzacio> liniesComanda;
    private String direccioEnviament;
    private String horariEnviament;

    public ComandaVisualitzacio(String idComanda, String emailClient, Date dataCreacio, boolean cancellat, List<LiniaComandaVisualitzacio> liniesComanda) {
        this.idComanda = idComanda;
        this.emailClient = emailClient;
        this.dataCreacio = dataCreacio;
        this.cancellat = cancellat;
        this.liniesComanda = liniesComanda;
    }

    public String getEmailClient() {
        return emailClient;
    }

    public void setEmailClient(String emailClient) {
        this.emailClient = emailClient;
    }

    public Date getDataCreacio() {
        return dataCreacio;
    }

    public void setDataCreacio(Date dataCreacio) {
        this.dataCreacio = dataCreacio;
    }

    public List<LiniaComandaVisualitzacio> getLiniesComanda() {
        return liniesComanda;
    }

    public void setLiniesComanda(List<LiniaComandaVisualitzacio> liniesComanda) {
        this.liniesComanda = liniesComanda;
    }

    public String getIdComanda() {
        return idComanda;
    }

    public void setIdComanda(String idComanda) {
        this.idComanda = idComanda;
    }

    public boolean isCancellat() {
        return cancellat;
    }

    public void setCancellat(boolean cancellat) {
        this.cancellat = cancellat;
    }

    public String getDireccioEnviament() {
        return direccioEnviament;
    }

    public void setDireccioEnviament(String direccioEnviament) {
        this.direccioEnviament = direccioEnviament;
    }

    public String getHorariEnviament() {
        return horariEnviament;
    }

    public void setHorariEnviament(String horariEnviament) {
        this.horariEnviament = horariEnviament;
    }
}
