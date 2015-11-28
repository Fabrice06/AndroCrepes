package crepes.fr.androcrepes.model;

import java.util.ArrayList;

/**
 * <b>Collection des différentes commandes passées en cuisine.</b>
 *
 * <p><b>Attention:</b><br>
 *     La taille de la collection n'est pas vérifiée en fonction des capacités de stockage du device.
 * </p>
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
