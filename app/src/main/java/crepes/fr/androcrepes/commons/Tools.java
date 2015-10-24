package crepes.fr.androcrepes.commons;

/**
 * Created by vince on 24/10/2015.
 */
public class Tools {

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
