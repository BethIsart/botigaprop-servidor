package botigaprop.servidor.Services;

import botigaprop.servidor.Models.Domain.*;
import botigaprop.servidor.Models.Requests.ComandaVisualitzacio;
import botigaprop.servidor.Models.Requests.LiniaComandaVisualitzacio;
import botigaprop.servidor.Models.Requests.ProducteVisualitzacio;
import botigaprop.servidor.Models.Requests.ValoracioProducteVisualitzacio;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Elisabet Isart
 */
@Service
@Primary
public class Mapper {

    public List<ProducteVisualitzacio> ProductesAMostrar(List<Producte> productes)
    {
        List<ProducteVisualitzacio> productesAMostrar = new ArrayList<>();
        for (Producte producte: productes) {
            productesAMostrar.add(ProducteAMostrar(producte));
        }
        return productesAMostrar;
    }

    public ProducteVisualitzacio ProducteAMostrar(Producte producte)
    {
        return new ProducteVisualitzacio(
                producte.getIdProducte(), producte.getUsuari().getIdUsuari(), producte.getNom(), producte.getTipus(), producte.getUnitats(),
                producte.getQuantitatPerUnitat(), producte.getPreu(), producte.getDescripcio(), producte.isDisponible(),
                producte.getImatge());
    }

    public List<ComandaVisualitzacio> ComandesAMostrar(List<Comanda> comandes)
    {
        List<ComandaVisualitzacio> comandesAMostrar = new ArrayList<>();
        for (Comanda comanda: comandes) {
            comandesAMostrar.add(ComandaAMostrar(comanda));
        }
        return comandesAMostrar;
    }

    public ComandaVisualitzacio ComandaAMostrar(Comanda comanda)
    {
        List<LiniaComandaVisualitzacio> liniesComanda = new ArrayList<>();
        for (LiniaComanda linia:comanda.getLinies())
        {
            liniesComanda.add(new LiniaComandaVisualitzacio(linia.getProducte().getNom(), linia.getUnitats()));
        }

        ComandaVisualitzacio comandaVisualitzacio = new ComandaVisualitzacio(comanda.getIdComanda(), comanda.getClient().getEmail(), comanda.getDataCreacio(), comanda.isCancellat(), liniesComanda);

        if (comanda.getEnviament() != null)
        {
            Enviament enviament = comanda.getEnviament();
            comandaVisualitzacio.setDireccioEnviament(enviament.getDireccio());
            comandaVisualitzacio.setHorariEnviament(enviament.getHorari());
        }

        return comandaVisualitzacio;
    }

    public List<ValoracioProducteVisualitzacio> ValoracionsAMostrar(List<ValoracioProducte> valoracions)
    {
        List<ValoracioProducteVisualitzacio> valoracionsAMostrar = new ArrayList<>();
        for (ValoracioProducte valoracion: valoracions) {
            valoracionsAMostrar.add(ValoracioAMostrar(valoracion));
        }
        return valoracionsAMostrar;
    }

    public ValoracioProducteVisualitzacio ValoracioAMostrar(ValoracioProducte valoracio)
    {
        return new ValoracioProducteVisualitzacio(valoracio.getDataCreacio(), valoracio.getPuntuacio(), valoracio.getComentari());
    }
}
