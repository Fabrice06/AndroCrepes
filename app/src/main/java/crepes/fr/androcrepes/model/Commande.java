package crepes.fr.androcrepes.model;

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

} // class
