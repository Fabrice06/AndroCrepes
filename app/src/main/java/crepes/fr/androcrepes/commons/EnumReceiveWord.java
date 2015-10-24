package crepes.fr.androcrepes.commons;

/**
 * Created by vince on 23/10/2015.
 */
public enum EnumReceiveWord {

    FINLISTE("FINLISTE"),
    COMMANDE("commandé"),
    EPUISE("épuisé"),
    INCONNU("inconnu");

    private String mValue = "";

    EnumReceiveWord(String pValue) {
        this.mValue = pValue;
    }

    public String getValue() {
        return mValue;
    }

    public String toString(){
        return mValue;
    }
} // enum
