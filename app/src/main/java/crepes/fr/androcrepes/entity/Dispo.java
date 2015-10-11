package crepes.fr.androcrepes.entity;

// Plat disponible en cuisine si quantite >=1
public class Dispo extends Plat {

    private int quantite;

    public Dispo() {
        super();
        quantite= 0;

    } // constructeur par défaut

    public Dispo(final String pNom) {
        super(pNom);
        this.quantite= 0;

    } // constructeur

    public Dispo(final String pNom, final int pQuantite) {
        super(pNom);
        this.quantite= pQuantite;

    } // constructeur

    public void setQuantite(final int pQuantite) {
        this.quantite = pQuantite;
    } // void

} // class
