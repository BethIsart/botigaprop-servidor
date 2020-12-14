package botigaprop.servidor.Persistence;

import botigaprop.servidor.Models.Domain.Producte;
import botigaprop.servidor.Models.Domain.Usuari;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author Elisabet Isart
 */
public interface ProducteRepository extends JpaRepository<Producte, String>, JpaSpecificationExecutor<Producte> {
    Page<Producte> findProducteByIdUsuariAndEliminatIsFalse(Usuari usuari, Pageable page);
    Page<Producte> findProducteByEliminatIsFalse(Pageable page);
    Producte findByIdProducte(String idProducte);
}
