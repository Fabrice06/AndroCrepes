package crepes.fr.androcrepes.commons;

/**
 * <b>EnumSendWord est un type énuméré.</b>
 * <p>
 *     Il existe 5 types de membres utilisés pour identifier les requêtes envoyées au serveur:
 *     <ul>
 *         <li>LISTE</li>
 *         <li>QUANTITE</li>
 *         <li>COMMANDE</li>
 *         <li>AJOUT</li>
 *         <li>LOGOUT</li>
 *     </ul>
 * </p>
 */
public enum EnumSendWord {

    LISTE("LISTE"),
    QUANTITE("QUANTITE"),
    COMMANDE("COMMANDE"),
    AJOUT("AJOUT"),
    LOGOUT("LOGOUT");

    /**
     * Valeur d'un des mots de la requête
     */
    private String mValue = "";

    /**
     * Constructeur
     *
     * @param pValue
     *          Valeur d'un des mots de la requête
     */
    EnumSendWord(String pValue) {
        this.mValue = pValue;
    }

    /**
     * Retourne le mot utilisé pour identifier la requête.
     *
     * @return Le mot correspondant, sous forme d'une chaîne de caractères.
     */
    public String getValue() {
        return mValue;
    }

} // enum
