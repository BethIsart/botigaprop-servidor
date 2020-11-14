package botigaprop.servidor.Persistence;

import botigaprop.servidor.Models.Producte;
import botigaprop.servidor.Models.Usuari;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Elisabet Isart
 */
public interface ProducteRepository extends JpaRepository<Producte, String> {

    List<Producte> findProducteByIdUsuariAndEliminatIsFalse(Usuari usuari);
    Producte findByIdProducte(String idProducte);
}
