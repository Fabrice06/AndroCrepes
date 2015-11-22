package crepes.fr.androcrepes.model;

import java.util.ArrayList;

/**
 * Created by vince on 12/11/2015.
 */
public class Commandes extends ArrayList<Commande> {

    public Commandes() {
    } // constructeur

    public Commande getCommande(final int pId) {

        Commande nReturn = null;

        for (Commande nItem : this) {
            if (pId == nItem.getId()) {
                nReturn = nItem;
            } // if
        } // for
        return nReturn;
    } // Commande

    public String getValueOfSize() {

        return String.valueOf(this.size());
    } // String

} // class
