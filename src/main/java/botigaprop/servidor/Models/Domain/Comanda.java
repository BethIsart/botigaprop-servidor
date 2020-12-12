package botigaprop.servidor.Models.Domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Elisabet Isart
 */
@Entity
@Table(name = "Comandes")
public class Comanda {

    @Id
    private String idComanda;
    private Date dataCreacio;
    private boolean cancellat;
    private Date dataCancellacio;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "idClient", referencedColumnName = "idUsuari")
    private Usuari client;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "idProveidor", referencedColumnName = "idUsuari")
    private Usuari proveidor;

    @NotNull
    @OneToMany(mappedBy = "comanda", cascade=CascadeType.ALL, orphanRemoval = true)
    private List<LiniaComanda> linies;

    @ManyToOne
    @JoinColumn(name = "idEnviament", referencedColumnName = "idEnviament")
    private Enviament enviament;

    public String getIdComanda() {
        return idComanda;
    }

    public void setIdComanda(String idComanda) {
        this.idComanda = idComanda;
    }

    public Date getDataCreacio() {
        return dataCreacio;
    }

    public void setDataCreacio(Date dataCreacio) {
        this.dataCreacio = dataCreacio;
    }

    public boolean isCancellat() {
        return cancellat;
    }

    public void setCancellat(boolean cancellat) {
        this.cancellat = cancellat;
    }

    public Usuari getClient() {
        return client;
    }

    public void setClient(Usuari client) {
        this.client = client;
    }

    public Usuari getProveidor() {
        return proveidor;
    }

    public void setProveidor(Usuari proveidor) {
        this.proveidor = proveidor;
    }

    public Date getDataCancellacio() {
        return dataCancellacio;
    }

    public void setDataCancellacio(Date dataCancellacio) {
        this.dataCancellacio = dataCancellacio;
    }

    public List<LiniaComanda> getLinies() {
        return linies;
    }

    public void setLinies(List<LiniaComanda> linies) {
        if (this.linies == null)
        {
            this.linies = new ArrayList<>();
        }

        for (LiniaComanda linia:linies)
        {
            linia.setComanda(this);
            this.linies.add(linia);
        }
    }

    public Enviament getEnviament() {
        return enviament;
    }

    public void setEnviament(Enviament enviament) {
        this.enviament = enviament;
    }
}


