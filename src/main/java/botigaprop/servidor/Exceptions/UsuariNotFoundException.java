package botigaprop.servidor.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Elisabet Isart
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UsuariNotFoundException extends RuntimeException {
    public UsuariNotFoundException(String idUsuari)  {
        super("No s'ha trobat cap usuari amb l'identificador " + idUsuari);
    }
}
