package botigaprop.servidor.Persistence;

import botigaprop.servidor.Models.Comanda;
import botigaprop.servidor.Models.Usuari;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author Elisabet Isart
 */
public interface ComandaRepository extends JpaRepository<Comanda, String>, JpaSpecificationExecutor<Comanda>  {
    List<Comanda> findComandaByProveidorAndCancellatIsFalse(Usuari usuari);
    Comanda findByIdComanda(String idComanda);
}
