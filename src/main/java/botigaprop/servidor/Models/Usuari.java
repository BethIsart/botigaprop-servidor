package botigaprop.servidor.Models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "Usuaris")
public class Usuari {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idUsuari;
    private String email;
    private String contrasenya;
    private Rol rol;
    private String nom;
    private String cifEmpresa;
    private String direccio;
    private String poblacio;
    private String provincia;
    private Date dataCreacio;
    private Date ultimAcces;

    public Usuari() { }

    public Integer getIdUsuari() {
        return idUsuari;
    }

    public void setIdUsuari(Integer idUsuari) {
        this.idUsuari = idUsuari;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasenya() {
        return contrasenya;
    }

    public void setContrasenya(String contrasenya) {
        this.contrasenya = contrasenya;
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

    public Date getDataCreacio() {
        return dataCreacio;
    }

    public void setDataCreacio(Date dataCreacio) {
        this.dataCreacio = dataCreacio;
    }

    public Date getUltimAcces() {
        return ultimAcces;
    }

    public void setUltimAcces(Date ultimAcces) {
        this.ultimAcces = ultimAcces;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
