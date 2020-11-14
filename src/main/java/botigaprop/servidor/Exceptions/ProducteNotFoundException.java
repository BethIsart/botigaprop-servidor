package botigaprop.servidor.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Elisabet Isart
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProducteNotFoundException extends RuntimeException {
    public ProducteNotFoundException(String idProducte)  {
        super("No s'ha trobat cap producte amb l'identificador " + idProducte);
    }
}

