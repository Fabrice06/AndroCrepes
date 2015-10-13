package crepes.fr.androcrepes.entity;

import java.util.ArrayList;

/**
 * Created by vince on 13/10/2015.
 */
public class Plats {

    private ArrayList<Plat> mPlats = new ArrayList<Plat>();

    // mettre ici une gestion d'exception?? si l'id n'existe pas?
    public Plat getPlat(final int pId) {
        return mPlats.get(pId);
    }

    public void addPlat(final Plat pPlat) {
        mPlats.add(pPlat.getId(), pPlat);
    }

    public int getSize() {
        return mPlats.size();
    }

    public boolean containsPlat(final Plat pPlat) {
        return mPlats.contains(pPlat);
    }

} //class
