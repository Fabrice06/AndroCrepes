package crepes.fr.androcrepes.entity;

//fixme: voir la doc java/dominique sur le check des données
/**
 * <b>Représente un plat identifié par son nom.</b>
 * <p>
 *     Le plat est associé à une quantité disponible en cuisine.
 * </p>
 * <p><b>Attention:</b><br>
 *     Aucun attribut n'est controlé.
 * </p>
 */
public class Plat {

    /**
     * Attribut de classe partagé par toutes les instances de la classe Plat.
     */
    private static int mCount = 0;

    /**
     * Nom du plat
     */
    private String mNom;

    /**
     * Identifiant unique du plat
     */
    private int mId;

    /**
     * Quantité disponible en cuisine
     */
    private int mQuantite;

    /**
     * <b>Constructeur</b>
     * <p>
     *     Permets la création d'un nouveau plat avec un identifiant unique et:
     *     <ul>
     *         <li>un nom,</li>
     *         <li>une quantité disponible en cuisine.</li>
     *     </ul>
     * </p>
     *
     * @param pNom
     *      Nom du plat, de type String.
     * @param pQuantite
     *      Quantité du plat, de type Integer.
     */
    public Plat(final String pNom, final int pQuantite) {
        this.mId = ++mCount;
        this.mNom = pNom;
        this.mQuantite = pQuantite;
    } // constructeur


    /**
     * Retourne le nom du plat.
     *
     * @return Le nom du plat, de type String.
     */
    public String getNom() {
        return mNom;
    } // String

    /**
     * Retourne l'identifiant unique du plat.
     *
     * @return L'identifiant unique correspondant au plat, de type Integer.
     */
    public int getId() {
        return mId;
    } // int

    /**
     * Change la quantité du plat disponible en cuisine.
     *
     * @param pQuantite
     *      Quantité du plat, de type Integer.
     */
    public void setQuantite(final int pQuantite) {
        this.mQuantite = pQuantite;
    } // void

} // class
