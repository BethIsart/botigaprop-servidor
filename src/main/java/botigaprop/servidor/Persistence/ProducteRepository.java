package botigaprop.servidor.Persistence;

import botigaprop.servidor.Models.Producte;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Elisabet Isart
 */
public interface ProducteRepository extends JpaRepository<Producte, String> {

}
