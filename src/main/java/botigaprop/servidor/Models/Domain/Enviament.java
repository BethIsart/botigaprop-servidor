package botigaprop.servidor.Models.Domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author Elisabet Isart
 */
@Entity
@Table(name = "Enviaments")
public class Enviament {

    @Id
    private String idEnviament;
    @NotNull
    private String direccio;
    private String horari;

    public String getIdEnviament() {
        return idEnviament;
    }

    public void setIdEnviament(String idEnviament) {
        this.idEnviament = idEnviament;
    }

    public String getDireccio() {
        return direccio;
    }

    public void setDireccio(String direccio) {
        this.direccio = direccio;
    }

    public String getHorari() {
        return horari;
    }

    public void setHorari(String horari) {
        this.horari = horari;
    }
}
