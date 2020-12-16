package botigaprop.servidor.Models.Requests;

/**
 * @author Elisabet Isart
 */
public class PeticioEdicioUsuari {

    private String nom;
    private String cifEmpresa;
    private String direccio;
    private String poblacio;
    private String provincia;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCifEmpresa() {
        return cifEmpresa;
    }

    public void setCifEmpresa(String cifEmpresa) {
        this.cifEmpresa = cifEmpresa;
    }

    public String getDireccio() {
        return direccio;
    }

    public void setDireccio(String direccio) {
        this.direccio = direccio;
    }

    public String getPoblacio() {
        return poblacio;
    }

    public void setPoblacio(String poblacio) {
        this.poblacio = poblacio;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }
}
