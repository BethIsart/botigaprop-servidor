package botigaprop.servidor.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Elisabet Isart
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ValoracionsNotFoundException extends RuntimeException {
    public ValoracionsNotFoundException() {
        super("No s'ha trobat cap valoració");
    }
}
