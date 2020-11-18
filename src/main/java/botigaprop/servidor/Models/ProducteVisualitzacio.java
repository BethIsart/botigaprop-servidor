package botigaprop.servidor.Models;

/**
 * @author Elisabet Isart
 */
public class ProducteVisualitzacio {

    private String idProducte;
    private String idProveidor;
    private String nom;
    private Categoria tipus;
    private String unitats;
    private Double quantitatPerUnitat;
    private Double preu;
    private String descripcio;
    private Boolean disponible;
    private byte[] imatge;

    public ProducteVisualitzacio(String idProducte, String idProveidor, String nom, Categoria tipus, String unitats, Double quantitatPerUnitat, Double preu, String descripcio, Boolean disponible, byte[] imatge) {
        this.idProducte = idProducte;
        this.idProveidor = idProveidor;
        this.nom = nom;
        this.tipus = tipus;
        this.unitats = unitats;
        this.quantitatPerUnitat = quantitatPerUnitat;
        this.preu = preu;
        this.descripcio = descripcio;
        this.disponible = disponible;
        this.imatge = imatge;
    }

    public String getIdProducte() {
        return idProducte;
    }

    public void setIdProducte(String idProducte) {
        this.idProducte = idProducte;
    }

    public String getIdProveidor() {
        return idProveidor;
    }

    public void setIdProveidor(String idProveidor) {
        this.idProveidor = idProveidor;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Categoria getTipus() {
        return tipus;
    }

    public void setTipus(Categoria tipus) {
        this.tipus = tipus;
    }

    public String getUnitats() {
        return unitats;
    }

    public void setUnitats(String unitats) {
        this.unitats = unitats;
    }

    public Double getQuantitatPerUnitat() {
        return quantitatPerUnitat;
    }

    public void setQuantitatPerUnitat(Double quantitatPerUnitat) {
        this.quantitatPerUnitat = quantitatPerUnitat;
    }

    public Double getPreu() {
        return preu;
    }

    public void setPreu(Double preu) {
        this.preu = preu;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    public byte[] getImatge() {
        return imatge;
    }

    public void setImatge(byte[] imatge) {
        this.imatge = imatge;
    }
}
