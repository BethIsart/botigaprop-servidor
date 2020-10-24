package botigaprop.servidor.Persistence;

import botigaprop.servidor.Models.Usuari;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuariRepository extends JpaRepository<Usuari, Integer>{
}
