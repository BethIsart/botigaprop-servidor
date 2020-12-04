package botigaprop.servidor.Services;

import botigaprop.servidor.Models.Domain.Comanda;
import botigaprop.servidor.Models.Domain.LiniaComanda;
import botigaprop.servidor.Models.Domain.Producte;
import botigaprop.servidor.Models.Requests.ComandaVisualitzacio;
import botigaprop.servidor.Models.Requests.ProducteVisualitzacio;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Map<String, Integer> liniesComanda = new HashMap<>();
        for (LiniaComanda linia:comanda.getLinies())
        {
            liniesComanda.put(linia.getProducte().getNom(), linia.getUnitats());
        }

        return new ComandaVisualitzacio(comanda.getClient().getEmail(), comanda.getDataCreacio(), liniesComanda);
    }
}
