package botigaprop.servidor.Models.Requests;

import botigaprop.servidor.Models.Domain.LiniaComanda;

import java.util.List;

/**
 * @author Elisabet Isart
 */
public class PeticioAltaComanda {

    private String codiAcces;
    private List<PeticioLiniaComanda> linies;
    private boolean distribucio;
    private String direccioEnviament;
    private String horariEnviament;

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

    public Boolean getDistribucio() {
        return distribucio;
    }

    public void setDistribucio(Boolean distribucio) {
        this.distribucio = distribucio;
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
