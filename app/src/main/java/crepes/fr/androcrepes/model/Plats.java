package crepes.fr.androcrepes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

/**
 * <b>Collection des différents plats disponibles en cuisine.</b>
 * <p>
 *     Le pattern design singleton est appliqué à cette classe.
 * </p>
 * <p><b>Attention:</b><br>
 *     La taille de la collection n'est pas vérifiée en fonction des capacités de stockage du device.
 *     On suppose que le nom du plat est unique, mais aucun contrôle n'a été mis en oeuvre en ce sens.
 * </p>
 */
public class Plats extends ArrayList<Plat> {

//    /**
//     * Instance singleton
//     */
//    protected static Plats mInstance;
//
//    /**
//     * Constructeur privé
//     *
//     * @see getInstance
//     */
//    private Plats() {
//    } // constructeur
//
//    /**
//     * Retourne une instance unique de la classe Plats.
//     *
//     * @return La collection des différents plats disponibles en cuisine, sous forme d'ArrayList<Plat>.
//     */
//    public static Plats getInstance() {
//
//        if (null == mInstance) {
//            mInstance = new Plats();
//        }
//        return mInstance;
//    } // Plats

    public Plats() {
    } // constructeur

    /**
     * Retourne le plat trouvé dans la collection à l'aide de son identifiant unique.
     *
     * @param pId
     *      Identifiant unique du plat, de type Integer.
     *
     * @return Un objet Plat ou null si le plat est absent de la collection.
     *
     * @see Plat
     */
    public Plat getPlatById(final int pId) {

        Plat nReturn = null;

        Iterator<Plat> nIterator = this.iterator();
        while (nIterator.hasNext()) {
            Plat nPlat = nIterator.next();
            if (pId == nPlat.getId()) {
                nReturn = nPlat;
                break;
            } // if
        } // while

//        for (Plat nItem : this) {
//            if (pId == nItem.getId()) {
//                nReturn = nItem;
//            } // if
//        } // for
        return nReturn;
    } // Plat

    /**
     * Retourne le premier plat trouvé dans la collection à l'aide de son nom.
     *
     * @param pNom
     *      Nom du plat, de type String.
     *
     * @return Un objet Plat ou null si le plat est absent de la collection.
     *
     * @see Plat
     */
    public Plat getPlatByName(final String pNom) {

        Plat nReturn = null;

        Iterator<Plat> nIterator = this.iterator();
        while (nIterator.hasNext()) {
            Plat nPlat = nIterator.next();
            if (nPlat.getNom().equals(pNom)) {
                nReturn = nPlat;
                break;
            } // if
        } // while

        return nReturn;
    } // Plat

//    public int getIndexByName(final String pNom) {
//
//        int nReturn = 0;
//
//        Iterator<Plat> nIterator = this.iterator();
//        while (nIterator.hasNext()) {
//            nReturn++;
//            Plat nPlat = nIterator.next();
//            if (nPlat.getNom().equals(pNom)) {
//                break;
//            } // if
//        } // while
//
//        return nReturn;
//    } // int

    public void sort() {
        Collections.sort(this, new Comparator<Plat>() {
            @Override
            public int compare(Plat pPlatA, Plat pPlatB) {

                return pPlatA.getNom().compareTo(pPlatB.getNom());
            }
        });
    } // void

    /**
     * Ajoute le plat dans la collection
     *
     * <p><b>Attention:</b><br>
     *     Si le plat est déjà présent, il n'est pas ajouté.
     * </p>
     *
     * @param pPlat
     *      Un objet de type Plat
     *
     * @see Plat
     */
    public void addPlat(final Plat pPlat) {

        //fixme: trouver et checker la taille max

        //fixme: a tester containsPlat pas sûr que ça marche
        // on ne veux pas de doubles
        if (!containsPlat(pPlat)) {
            //mInstance.add(pPlat.getId(), pPlat);
//            mInstance.add(pPlat);
            this.add(pPlat);
        } // if
    } // void


    /**
     * Détermine si le plat est déjà présent dans la collection
     *
     * @param pPlat
     *      Un objet de type Plat
     *
     * @return Vrai, si le plat est présent dans la collection.
     */
    public boolean containsPlat(final Plat pPlat) {
        return this.contains(pPlat);
    } // boolean

} //class
