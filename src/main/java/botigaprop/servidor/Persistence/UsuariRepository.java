package botigaprop.servidor.Persistence;

import botigaprop.servidor.Models.Usuari;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsuariRepository extends JpaRepository<Usuari, Integer>{
    List<Usuari> findUsuariByEmailAndDeshabilitatIsFalse(String email);
    Usuari findByIdUsuari(String idUsuari);
}
