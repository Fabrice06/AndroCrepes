package crepes.fr.androcrepes.view;


import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.List;

import crepes.fr.androcrepes.R;
import crepes.fr.androcrepes.commons.framework.CustomActivity;
import crepes.fr.androcrepes.commons.java.EnumReceiveWord;
import crepes.fr.androcrepes.commons.java.EnumSendWord;
import crepes.fr.androcrepes.commons.java.Tools;
import crepes.fr.androcrepes.controller.Controller;
import crepes.fr.androcrepes.model.Plat;
import crepes.fr.androcrepes.model.Plats;

/**
 * <b>Classe dédiée à la description de l'ihm Cuisine.</b>
 */
public class CuisineActivity
        extends CustomActivity {

    private Controller mController = null;

    private EditText mEditTextQte;
    private EditText mEditTextName;

    protected int getLayoutResourceId() {
        return R.layout.activity_cuisine;
    } // int

    protected int getTextViewInfoResourceId() {
        return R.id.cuisine_customTextViewTitle;
    } // int

    protected int getListViewResourceId() {
        return R.id.cuisine_listView;
    } // int

    protected int getMenuResourceId() {
        return R.menu.menu_cuisine;
    } // int

    protected Plats getPlats() {
        return mController.getPlats();
    } // Plats

    protected Controller getController() {
        if ( null == mController) {
            //Get Global Controller Class object (see application tag in AndroidManifest.xml)
            mController = (Controller) getApplicationContext();
        }
        return mController;
    } // Controller

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mEditTextQte = (EditText) findViewById(R.id.cuisine_editTextQuantite);
        mEditTextName = (EditText) findViewById(R.id.cuisine_editTextPlat);

    } // void

    protected void updateAfterClientAjout() {

        mEditTextQte.setText("1");
        mEditTextName.setText("");

        super.clientSendQuantity();
    } // void


    //******************************************************************************
    // callback client: data

    public void singleFromClient(final String pResponseFromServer) {
        //super.debugLog("singleFromClient callback: " + pResponseFromServer);

        // recherche du dernier mot/chiffre pour identifier la réponse
        String nReponse = pResponseFromServer.substring(pResponseFromServer.lastIndexOf(" ")+1);

        if (nReponse.equals(EnumReceiveWord.EPUISE.getValue()) || (nReponse.equals(EnumReceiveWord.INCONNU.getValue()))) {
            // échec d'une commande ('épuisé' ou 'inconnu' trouvé en fin de message)
            super.toastMessage(pResponseFromServer + " !", true);

        } else if (nReponse.equals(EnumReceiveWord.COMMANDE.getValue())) { // en réponse à l'ordre COMMANDE
            super.clientSendQuantity();

        } else if (Tools.isInteger(nReponse)) { // en réponse à l'ordre AJOUT
            this.updateAfterClientAjout();

        } else {
            // cas non répertorié: ceinture et bretelles
            String nMessage = getString(R.string.activity_toastMessageUnknownError);
            super.toastMessage(nMessage, true);
        } // else
    } // void

    public void quantiteFromClient(List<String> pListData) {
        //super.debugLog("quantiteFromClient callback");

        Plats nPlats = mController.getPlats();

        boolean nIsNewPlat = false;

        int nPlatSize = nPlats.size();

        String nNewPlatNom = "";

        //fixme: le retrait d'un plat de la carte n'est pas pris en compte
        for (int nLen = pListData.size(), i = 1; i < (nLen-1); i+=2) {
            String nNom = pListData.get(i);
            int nQuantite = Integer.parseInt(pListData.get(i + 1));

            Plat nPlat = nPlats.getPlatByName(nNom);

            // nouveau plat
            if (null == nPlat) {
                nPlat= new Plat(nNom, nQuantite);
                nPlats.addPlat(nPlat);

                if (!nIsNewPlat) {
                    nIsNewPlat = true;
                    nNewPlatNom = nNom;
                } // if

            } else { // update quantité
                nPlat.setQuantite(nQuantite);
            } // else

            //Log.d(TAG, "quantiteFromClient for item " + nNom + " " + nQuantite);
        } // for

        // tri par nom de plat si nouvel ajout
        if (nIsNewPlat) {
            nPlats.sort();
        } // if

        if (nPlats.size() != (nPlatSize + 1)) {
            nNewPlatNom = "";
        } // if

        super.updateAfterClientQuantite(nNewPlatNom);
    } // void

    // callback client: data
    //******************************************************************************


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
        //super.debugLog("clicLeftFromListAdapter callback");

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
        //super.debugLog("clicRightFromListAdapter callback");

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
    public void addPlat(View pView) {
        //super.debugLog("addPlat");

        String nQuantite = mEditTextQte.getText().toString();
        String nName = mEditTextName.getText().toString().trim();

        if (!Tools.isInteger(nQuantite)) { // check si digit
            super.toastMessage(getString(R.string.activity_toastMessageBadQuantite), false);

        } else if (0 == nName.length()) { // check si nom plat valide
            super.toastMessage(getString(R.string.activity_toastMessageBadPlat), false);

        } else {
            super.clientSendAjout(nQuantite + " " + nName, true);
        } // else
    } // void
} // class
