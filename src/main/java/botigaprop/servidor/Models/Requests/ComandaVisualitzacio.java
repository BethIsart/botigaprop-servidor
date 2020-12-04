package botigaprop.servidor.Models.Requests;

import botigaprop.servidor.Models.Domain.Producte;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Elisabet Isart
 */
public class ComandaVisualitzacio {
    private String emailClient;
    private Date dataCreacio;
    private Map<String, Integer> liniesComanda;

    public ComandaVisualitzacio(String emailClient, Date dataCreacio, Map<String, Integer> productes) {
        this.emailClient = emailClient;
        this.dataCreacio = dataCreacio;
        this.liniesComanda = productes;
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

    public Map<String, Integer> getLiniesComanda() {
        return liniesComanda;
    }

    public void setLiniesComanda(Map<String, Integer> liniesComanda) {
        this.liniesComanda = liniesComanda;
    }
}
