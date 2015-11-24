package crepes.fr.androcrepes.model;

import java.util.Iterator;

/**
 * Created by vince on 12/11/2015.
 */
public class Commande {

    private static int mCount = 1;

    private int mId;

    private Plats mPlats;

    public Commande() {
        this.mId = mCount++;
        this.mPlats = new Plats();
    } // constructeur

    public Plats getPlats() {
        return mPlats;
    } // Plats

    public String getValueOfId() {
        return String.valueOf(mId);
    } // String

    public int getId() {
        return mId;
    } // int

    public int getTotalPlat() {

        int nReturn = 0;
        Iterator<Plat> nIterator = mPlats.iterator();

        while (nIterator.hasNext()) {
            Plat nPlat = nIterator.next();
            if (nPlat.getQuantite() >= 1) {
                nReturn++;
            } // if
        } // while

        return nReturn;
    } // int

} // class
