package botigaprop.servidor.Persistence;

import botigaprop.servidor.Models.Categoria;
import botigaprop.servidor.Models.Producte;
import botigaprop.servidor.Models.Usuari;
import org.springframework.data.jpa.domain.Specification;

public class ProducteSpecs {

    public static Specification<Producte> usuariEquals(Usuari usuari) {
        return (root, query, builder) ->
                usuari == null ?
                        builder.conjunction() :
                        builder.equal(root.get("idUsuari"), usuari);
    }

    public static Specification<Producte> tipusEquals(Categoria tipus) {
        return (root, query, builder) ->
                tipus == null ?
                        builder.conjunction() :
                        builder.equal(root.get("tipus"), tipus);
    }

    public static Specification<Producte> preuIsGreaterThan(Double preu) {
        return (root, query, builder) ->
                preu == null ?
                        builder.conjunction() :
                        builder.greaterThan(root.get("preu"), preu);
    }

    public static Specification<Producte> preuIsLessThan(Double preu) {
        return (root, query, builder) ->
                preu == null ?
                        builder.conjunction() :
                        builder.lessThan(root.get("preu"), preu);
    }

    public static Specification<Producte> eliminatIsFalse() {
        return (root, query, builder) ->
                    builder.isFalse(root.get("eliminat"));
    }
}
