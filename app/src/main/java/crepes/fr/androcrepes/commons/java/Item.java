package crepes.fr.androcrepes.commons.java;

/**
 * Created by vince on 13/11/2015.
 */
public class Item {

    private static int mCount = 0;

    private int mId;

    public void Item() {
        this.mId = ++mCount;
    } // constructeur

    public int getId() {

        return mId;
    } // int

} // class
