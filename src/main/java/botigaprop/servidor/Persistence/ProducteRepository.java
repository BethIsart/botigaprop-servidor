package botigaprop.servidor.Persistence;

import botigaprop.servidor.Models.Producte;
import botigaprop.servidor.Models.Usuari;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author Elisabet Isart
 */
public interface ProducteRepository extends JpaRepository<Producte, String>, JpaSpecificationExecutor<Producte> {

    List<Producte> findProducteByIdUsuariAndEliminatIsFalse(Usuari usuari);

    List<Producte> findProducteByEliminatIsFalse();
    Producte findByIdProducte(String idProducte);
}
