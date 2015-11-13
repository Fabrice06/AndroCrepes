package crepes.fr.androcrepes.commons.java;

/**
 * <b>EnumReceiveWord est un type énuméré.</b>
 * <p>
 *     Il existe 4 types de membres utilisés pour identifier les réponses envoyées par le serveur:
 *     <ul>
 *         <li>FINLISTE</li>
 *         <li>commandé</li>
 *         <li>épuisé</li>
 *         <li>inconnu</li>
 *     </ul>
 * </p>
 */
public enum EnumReceiveWord {

    FINLISTE("FINLISTE"),
    COMMANDE("commandé"),
    EPUISE("épuisé"),
    INCONNU("inconnu");

    /**
     * Valeur d'un des mots de la réponse serveur
     */
    private String mValue = "";

    /**
     * Constructeur
     *
     * @param pValue
     *          Valeur d'un des mots de la réponse serveur
     */
    EnumReceiveWord(String pValue) {
        this.mValue = pValue;
    }

    /**
     * Retourne le mot utilisé pour identifier la réponse serveur.
     *
     * @return Le mot correspondant, sous forme d'une chaîne de caractères.
     */
    public String getValue() {
        return mValue;
    }

} // enum
