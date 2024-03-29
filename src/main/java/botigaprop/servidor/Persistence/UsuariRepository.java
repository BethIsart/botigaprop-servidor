package botigaprop.servidor.Persistence;

import botigaprop.servidor.Models.Domain.Usuari;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Elisabet Isart
 */
public interface UsuariRepository extends JpaRepository<Usuari, String>{
    List<Usuari> findUsuariByEmailAndDeshabilitatIsFalse(String email);
    Usuari findByIdUsuari(String idUsuari);
}
