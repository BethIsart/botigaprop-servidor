package botigaprop.servidor.Persistence;

import botigaprop.servidor.Models.Domain.Comanda;
import botigaprop.servidor.Models.Domain.Usuari;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author Elisabet Isart
 */
public interface ComandaRepository extends JpaRepository<Comanda, String>, JpaSpecificationExecutor<Comanda>  {
    List<Comanda> findComandaByProveidor(Usuari usuari);
    List<Comanda> findComandaByClient(Usuari usuari);
    Comanda findByIdComanda(String idComanda);
}
