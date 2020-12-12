package botigaprop.servidor.Persistence;

import botigaprop.servidor.Models.Domain.Enviament;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Elisabet Isart
 */
public interface EnviamentRepository extends JpaRepository<Enviament, String> {
}
