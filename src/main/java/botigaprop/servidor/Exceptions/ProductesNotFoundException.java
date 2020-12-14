package botigaprop.servidor.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Elisabet Isart
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProductesNotFoundException extends RuntimeException {
    public ProductesNotFoundException()  {
        super("No s'ha trobat cap producte");
    }
}

