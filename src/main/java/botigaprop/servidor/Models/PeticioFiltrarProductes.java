package botigaprop.servidor.Models;

public class PeticioFiltrarProductes {

    private String codiAcces;
    private Categoria tipus;
    private String idProveidor;
    private Double preuMin;
    private Double preuMax;

    public String getCodiAcces() {
        return codiAcces;
    }

    public void setCodiAcces(String codiAcces) {
        this.codiAcces = codiAcces;
    }

    public Categoria getTipus() {
        return tipus;
    }

    public void setTipus(Categoria tipus) {
        this.tipus = tipus;
    }

    public String getIdProveidor() {
        return idProveidor;
    }

    public void setIdProveidor(String idProveidor) {
        this.idProveidor = idProveidor;
    }

    public Double getPreuMin() {
        return preuMin;
    }

    public void setPreuMin(Double preuMin) {
        this.preuMin = preuMin;
    }

    public Double getPreuMax() {
        return preuMax;
    }

    public void setPreuMax(Double preuMax) {
        this.preuMax = preuMax;
    }
}