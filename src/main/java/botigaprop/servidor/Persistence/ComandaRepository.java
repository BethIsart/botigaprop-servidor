package botigaprop.servidor.Persistence;

import botigaprop.servidor.Models.Domain.Comanda;
import botigaprop.servidor.Models.Domain.Usuari;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Elisabet Isart
 */
public interface ComandaRepository extends JpaRepository<Comanda, String> {
    Page<Comanda> findComandaByProveidor(Usuari usuari, Pageable page);
    Page<Comanda> findComandaByClient(Usuari usuari, Pageable page);
    Comanda findByIdComanda(String idComanda);
}
