package crepes.fr.androcrepes.commons.java;

/**
 * <b>Tools regroupe l'ensemble des outils utilisés dans l'application.</b>
 * <p>
 *     Ses méthodes sont statiques: On peux les utiliser sans avoir à créer une instance de Tools.
 * </p>
 */
public class Tools {

    /**
     * Teste si le paramètre pString de type String peut être casté en Integer.
     *
     * @param pString
     *      Chaîne de caractères à tester
     *
     * @return le booléen Vrai, si pString peut être casté en Integer.
     */
    public static boolean isInteger(final String pString) {
        try {
            Integer.parseInt(pString);

        } catch(NumberFormatException e) {
            return false;

        } catch(NullPointerException e) {
            return false;
        }

        return true;
    } // boolean
} // class
