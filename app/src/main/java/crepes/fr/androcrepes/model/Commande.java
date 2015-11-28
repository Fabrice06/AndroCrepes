package crepes.fr.androcrepes.model;

import java.util.Iterator;

/**
 * <b>Représente une commande.</b>
 * <p>
 *     La commande est associée à une collection de plat et à un identifiant unique.
 * </p>
 * <p><b>Attention:</b><br>
 *     Aucun attribut n'est controlé.
 * </p>
 */
public class Commande {

    private static int mCount = 1;

    private int mId;

    private Plats mPlats;

    private boolean mIsFilterOn;

    public Commande() {
        this.mId = mCount++;
        this.mPlats = new Plats();
        mIsFilterOn = false;
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

    public boolean getFilter() {
        return mIsFilterOn;
    } // boolean

    public void setFilter(final boolean pIsOn) {
        mIsFilterOn = pIsOn;
    } // boolean
} // class
