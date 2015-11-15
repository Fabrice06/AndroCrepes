package crepes.fr.androcrepes.view;


import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import crepes.fr.androcrepes.R;
import crepes.fr.androcrepes.commons.framework.CustomActivity;
import crepes.fr.androcrepes.commons.java.EnumSendWord;
import crepes.fr.androcrepes.commons.java.Tools;
import crepes.fr.androcrepes.model.Plat;

/**
 * <b>Classe dédiée à la description de l'ihm Cuisine.</b>
 */
public class CuisineActivity
        extends CustomActivity {

    private EditText mEditTextQte;
    private EditText mEditTextName;

    protected int getLayoutResourceId() {
        return R.layout.activity_cuisine;
    } // int

    protected int getTextViewInfoResourceId() {
        return R.id.cuisine_textViewInfoId;
    } // int

    protected int getListViewResourceId() {
        return R.id.cuisine_listView;
    } // int

    protected int getMenuResourceId() {
        return R.menu.menu_cuisine;
    } // int

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mEditTextQte = (EditText) findViewById(R.id.cuisine_editTextQuantiteId);
        mEditTextName = (EditText) findViewById(R.id.cuisine_editTextPlatId);

    } // void

    protected void updateAfterClientAjout(final String pNewPlatName) {

        mEditTextQte.setText("1");
        mEditTextName.setText("");

        super.clientSendQuantity();

        if (!pNewPlatName.isEmpty()) {

            super.toastMessage("Le plat " + pNewPlatName + " a été ajouté à la carte !", true);

            // fixme scroll seulement sur un nouveau plat ???
            //marche pas
            //super.scrollListViewByName(pNewPlatName);
        } // if
    } // void


    //******************************************************************************
    // callback listAdapter

    /**
     * Implémentation de ListAdapterCallBack: clic sur bouton gauche.
     * <p>
     *     Une requête COMMANDE est envoyée au serveur distant.
     * </p>
     *
     * @param pPlat
     *      Objet de type Plat
     *
     * @see Plat
     * @see EnumSendWord
     */
    @Override
    public void clicLeftFromListAdapter(Plat pPlat) {
        super.debugLog("clicLeftFromListAdapter callback");

        super.clientSendCommande(pPlat.getNom(), true);
    } // void

    /**
     * Implémentation de ListAdapterCallBack: clic sur bouton droit.
     * <p>
     *     Une requête AJOUT est envoyée au serveur distant.
     * </p>
     *
     * @param pPlat
     *      Objet de type Plat
     *
     * @see Plat
     * @see EnumSendWord
     */
    @Override
    public void clicRightFromListAdapter(Plat pPlat) {
        super.debugLog("clicRightFromListAdapter callback");

        super.clientSendAjout("1 " + pPlat.getNom(), true);
    } // void

    // callback listAdapter
    //******************************************************************************

    /**
     * Evènement associé au bouton buttonCuisineAddNewPlat pour ajouter un nouveau plat
     *
     * @param pView
     *      Objet de type View
     */
    public void addNewPlat(View pView) {
        super.debugLog("addNewPlat");

        String nQuantite = mEditTextQte.getText().toString();
        String nName = mEditTextName.getText().toString().trim();

        if (!Tools.isInteger(nQuantite)) { // check si digit
            super.toastMessage("Merci de préciser une quantité correcte !", false);

        } else if (0 == nName.length()) { // check si nom plat valide
            super.toastMessage("Merci de préciser un nom de plat !", false);

        } else {
            super.clientSendAjout(nQuantite + " " + nName, true);
        } // else
    } // void
} // class
