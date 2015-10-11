package crepes.fr.androcrepes.entity;

// Plat demandé en salle si quantite >=1
public class Demande extends Plat {

    private int quantite;

    public Demande() {
        super();
        quantite= 0;

    } // constructeur par défaut

    public Demande(final String pNom, final int pQuantite) {
        super(pNom);
        this.quantite= pQuantite;

    } // constructeur

    public void setQuantite(final int pQuantite) {
        this.quantite = pQuantite;
    } // void

} // class
