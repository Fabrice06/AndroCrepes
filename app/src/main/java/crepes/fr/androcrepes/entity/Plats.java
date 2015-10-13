package crepes.fr.androcrepes.entity;

import java.util.ArrayList;

/**
 * Created by vince on 13/10/2015.
 */
public class Plats extends ArrayList<Plat> {

    // instance singleton
    protected static Plats mInstance;

    private Plats() {
    } // constructeur privé


    public static Plats getInstance() {

        if (null == mInstance) {
            mInstance = new Plats();
        }
        return mInstance;
    } // Plats


    public Plat getPlat(final int pId) {

        Plat nReturn = null;

        for (Plat nItem : this) {
            if (nItem.getId() == pId) {
                nReturn = nItem;
            } // if
        } // for
        return nReturn;
    } // Plat

    public Plat getPlat(final String pNom) {

        Plat nReturn = null;

        for (Plat nItem : this) {
            if (nItem.getNom() == pNom) {
                nReturn = nItem;
            } // if
        } // for
        return nReturn;
    } // Plat


    public void addPlat(final Plat pPlat) {

        // on ne veux pas de doubles
        if (!containsPlat(pPlat)) {
            mInstance.add(pPlat.getId(), pPlat);
        } // if
    } // void


    public int getSize() {
        return mInstance.size();
    }


    public boolean containsPlat(final Plat pPlat) {
        return mInstance.contains(pPlat);
    }

} //class
