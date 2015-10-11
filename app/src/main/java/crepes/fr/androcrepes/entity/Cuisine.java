package crepes.fr.androcrepes.entity;

import java.util.ArrayList;

// stock en cuisine: liste des Dispo (plats + quantités) disponibles en cuisine
// alimenté et maj avec la commande serveur QUANTITE
public class Cuisine {

    private ArrayList<Dispo> mPlats = new ArrayList<Dispo>();

    public Cuisine() {

    } // constructeur par défaut

    public Cuisine(ArrayList<Dispo> pPlats) {

        mPlats = pPlats;
    } // constructeur

    public void addDispo(final Dispo pDispo) {
        mPlats.add(pDispo.getId(), pDispo);
    }

    //public void removeDispo(final Dispo pDispo) {
    //    mPlats.remove(pDispo);
    //}

    public void removeDispo(final int pId) {
        mPlats.remove(mPlats.get(pId));
    }

    // mettre ici une gestion d'exception?? si l'id n'existe pas?
    public Dispo getDispo(final int pId) {
        return mPlats.get(pId);
    }

    public void clearQuantite(final int pId) {
        mPlats.get(pId).setQuantite(0);
    }

    public void updateQuantite(final int pId, final int pQuantite) {
        mPlats.get(pId).setQuantite(pQuantite);
    }

} // class
