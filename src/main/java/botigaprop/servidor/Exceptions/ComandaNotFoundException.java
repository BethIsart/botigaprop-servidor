package botigaprop.servidor.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Elisabet Isart
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ComandaNotFoundException extends RuntimeException {
    public ComandaNotFoundException(String idComanda) {
        super("No s'ha trobat cap comanda amb l'identificador " + idComanda);
    }
}
