package crepes.fr.androcrepes.entity;

/**
 * Created by vince on 11/10/2015.
 */
public class Plat {

    private String mNom;
    private static int mId;

    // mettre un id ici pour rendre unique le plat dans l'application ???

    public Plat() {
        mId++;
        mNom= "";
    } // constructeur par défaut

    public Plat(final String pNom) {
        super();
        this.mNom= pNom;
    } // constructeur

    public String getNom() {
        return mNom;
    } // String

    public void setNom(final String pNom) {
        this.mNom = pNom;
    } // void

    public int getId() {
        return mId;
    }
} // class
