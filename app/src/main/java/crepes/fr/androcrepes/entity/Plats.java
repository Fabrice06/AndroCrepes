package crepes.fr.androcrepes.entity;

import java.util.ArrayList;

/**
 * <b>Collection des différents plats disponibles en cuisine.</b>
 * <p>
 *     Le pattern design singleton est appliqué à cette classe.
 * </p>
 * * <p><b>Attention:</b><br>
 *     La taille de la collection n'est pas vérifiée en fonction des capacités de stockage du device.
 *     On suppose que le nom du plat est unique, mais aucun contrôle n'a été mis en oeuvre en ce sens.
 * </p>
 */
public class Plats extends ArrayList<Plat> {

    /**
     * Instance singleton
     */
    protected static Plats mInstance;

    /**
     * Constructeur privé
     *
     * @see getInstance
     */
    private Plats() {
    } // constructeur

    /**
     * Retourne une instance unique de la classe Plats.
     *
     * @return La collection des différents plats disponibles en cuisine, sous forme d'ArrayList<Plat>.
     */
    public static Plats getInstance() {

        if (null == mInstance) {
            mInstance = new Plats();
        }
        return mInstance;
    } // Plats

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
    public Plat getPlat(final int pId) {

        Plat nReturn = null;

        for (Plat nItem : this) {
            if (pId == nItem.getId()) {
                nReturn = nItem;
            } // if
        } // for
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
    public Plat getPlat(final String pNom) {

        Plat nReturn = null;

        for (Plat nItem : this) {
            if (nItem.getNom().equals(pNom)) {
                nReturn = nItem;
            } // if
        } // for
        return nReturn;
    } // Plat

    /**
     *
     * @param pPlat
     */
    public void addPlat(final Plat pPlat) {

        //fixme: trouver et checker la taille max

        //fixme: a tester containsPlat pas sûr que ça marche
        // on ne veux pas de doubles
        if (!containsPlat(pPlat)) {
            mInstance.add(pPlat.getId(), pPlat);
        } // if
    } // void

    /**
     *
     * @return
     */
    public int getSize() {
        return mInstance.size();
    } // int

    /**
     *
     * @param pPlat
     * @return
     */
    //fixme: a tester containsPlat pas sûr que ça marche
    public boolean containsPlat(final Plat pPlat) {
        return mInstance.contains(pPlat);
    } // boolean

} //class
