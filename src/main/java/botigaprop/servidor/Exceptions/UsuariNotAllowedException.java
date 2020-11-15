package botigaprop.servidor.Exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Elisabet Isart
 */
@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
public class UsuariNotAllowedException extends RuntimeException {

    public UsuariNotAllowedException(String missatgeError) {
        super(missatgeError);
    }
}
