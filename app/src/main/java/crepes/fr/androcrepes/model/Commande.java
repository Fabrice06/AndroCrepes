package crepes.fr.androcrepes.model;

import crepes.fr.androcrepes.commons.java.Item;

/**
 * Created by vince on 12/11/2015.
 */
public class Commande extends Item {

    private Plats mPlats;

    public Commande() {
        super();
        this.mPlats = new Plats();
    } // constructeur

    public Plats getPlats() {
        return mPlats;
    } // Plats

} // class
