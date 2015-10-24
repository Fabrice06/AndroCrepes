package crepes.fr.androcrepes.entity;

/**
 * Created by vince on 11/10/2015.
 */
public class Plat {

    private String mNom;
    private int mId;
    private int mQuantite;


    public Plat(final int pId, final String pNom, final int pQuantite) {
        this.mId = pId;
        this.mNom = pNom;
        this.mQuantite = pQuantite;
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

    public void setQuantite(final int pQuantite) {
        this.mQuantite = pQuantite;
    } // void

//    public int getQuantite() {
//        return mQuantite;
//    } // int

} // class
