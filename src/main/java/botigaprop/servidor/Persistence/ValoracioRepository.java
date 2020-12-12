package botigaprop.servidor.Persistence;

import botigaprop.servidor.Models.Domain.Producte;
import botigaprop.servidor.Models.Domain.ValoracioProducte;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Elisabet Isart
 */
public interface ValoracioRepository extends JpaRepository<ValoracioProducte, String> {
    List<ValoracioProducte> findByProducte(Producte producte);
}
