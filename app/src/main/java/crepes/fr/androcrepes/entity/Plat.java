package crepes.fr.androcrepes.entity;

/**
 * Created by vince on 11/10/2015.
 */
public class Plat {

    private String mNom;
    private int mId;


    public Plat(final int pId, final String pNom) {
        this.mId = pId;
        this.mNom = pNom;
    } // constructeur


    public String getNom() {
        return mNom;
    } // String


    public void setNom(final String pNom) {
        this.mNom = pNom;
    } // void


    public int getId() {
        return mId;
    } // int
} // class
