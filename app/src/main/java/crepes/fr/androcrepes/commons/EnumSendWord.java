package crepes.fr.androcrepes.commons;

/**
 * Created by vince on 23/10/2015.
 */
public enum EnumSendWord {

    LISTE("LISTE"),
    QUANTITE("QUANTITE"),
    COMMANDE("COMMANDE"),
    AJOUT("AJOUT"),
    LOGOUT("LOGOUT");

    private String mValue = "";

    EnumSendWord(String pValue) {
        this.mValue = pValue;
    }

    public String getValue() {
        return mValue;
    }

    public String toString(){
        return mValue;
    }
} // enum
