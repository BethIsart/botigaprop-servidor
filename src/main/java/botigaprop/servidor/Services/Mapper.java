package botigaprop.servidor.Services;

import botigaprop.servidor.Models.Producte;
import botigaprop.servidor.Models.ProducteVisualitzacio;
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
                producte.getIdProducte(), producte.getNom(), producte.getTipus(), producte.getUnitats(),
                producte.getQuantitatPerUnitat(), producte.getPreu(), producte.getDescripcio(), producte.isDisponible(),
                producte.getImatge());
    }
}
