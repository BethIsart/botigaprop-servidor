package botigaprop.servidor.Services;

import botigaprop.servidor.Exceptions.BadRequestException;
import botigaprop.servidor.Models.Domain.Usuari;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Elisabet Isart
 */
@Service
@Primary
public class ControlAcces {

    private final Map<String, String> codisAccesAcreditats = new HashMap<>();

    public String GenerarCodiAcces(Usuari usuari)
    {
        String codiAcces = UUID.randomUUID().toString();
        codisAccesAcreditats.put(codiAcces, usuari.getIdUsuari());
        return codiAcces;
    }

    public String ValidarCodiAcces(String codiAcces) {
        if (codiAcces == null || !codisAccesAcreditats.containsKey(codiAcces)){
            throw new BadRequestException("Codi d'accés no vàlid");
        }
        return codisAccesAcreditats.get(codiAcces);
    }

    public void EliminarCodiAcces(String codiAcces) {
        codisAccesAcreditats.remove(codiAcces);
    }

    public void ValidarUsuariSenseAccesPrevi(Usuari usuari) {
        if (codisAccesAcreditats.containsValue(usuari.getIdUsuari()))
        {
            throw new BadRequestException("Aquest usuari ja te accés. Per obtenir un nou codi primer ha de finalitzar sessió");
        }
    }
}
