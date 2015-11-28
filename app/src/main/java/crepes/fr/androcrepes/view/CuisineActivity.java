package crepes.fr.androcrepes.view;


import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.List;

import crepes.fr.androcrepes.R;
import crepes.fr.androcrepes.commons.framework.CustomListActivity;
import crepes.fr.androcrepes.commons.java.EnumSendWord;
import crepes.fr.androcrepes.commons.java.Tools;
import crepes.fr.androcrepes.model.Plat;
import crepes.fr.androcrepes.model.Plats;

/**
 * <b>Classe dédiée à la description de l'ihm Cuisine.</b>
 */
public class CuisineActivity
        extends CustomListActivity {

    private EditText mEditTextQte;
    private EditText mEditTextName;

    /**
     * Implémentation de la méthode abstraite issue de la super classe CustomListActivity
     *
     * @see CustomListActivity
     *
     * @return
     *      L'identifiant de l'activity de type int
     */
    protected int getLayoutId() {
        return R.layout.activity_cuisine;
    } // int

    /**
     * Implémentation de la méthode abstraite issue de la super classe CustomListActivity
     *
     * @see CustomListActivity
     *
     * @return
     *      L'identifiant du titre de l'activity de type int
     */
    protected int getTextViewTitleId() {
        return R.id.cuisine_customTextViewTitle;
    } // int

    /**
     * Implémentation de la méthode abstraite issue de la super classe CustomListActivity
     *
     * @see CustomListActivity
     *
     * @return
     *      L'identifiant du listview de l'activity de type int
     */
    protected int getListViewId() {
        return R.id.cuisine_listView;
    } // int

    /**
     * Implémentation de la méthode abstraite issue de la super classe CustomListActivity
     *
     * @see CustomListActivity
     *
     * @return super.getController().getPlats()
     *      Une collection de type Plats destinée à contenir l'ensemble des plats cuisinés.
     */
    protected Plats getPlats() {
        return super.getController().getPlats();
    } // Plats

    /**
     * Méthode appelée à la création de l\'activité Cuisine
     * <p>
     *     L'exécution de cette méthode se déroule en 3 phases:
     *     <ul>
     *         <li>appel de la méthode onCreate() sur la super classe CustomListActivity,</li>
     *         <li>éléments présents dans le layout XML initialisés,</li>
     *         <li>fitre du ListView désactivé.</li>
     *     </ul>
     * </p>
     *
     * @see CustomListActivity
     *
     * @param pSavedInstanceState
     *      Objet de type Bundle contenant l’état de sauvegarde enregistré lors de la dernière exécution de cette activité.
     */
    @Override
    protected void onCreate(Bundle pSavedInstanceState) {
        super.onCreate(pSavedInstanceState);

        mEditTextQte = (EditText) findViewById(R.id.cuisine_editTextQuantite);
        mEditTextName = (EditText) findViewById(R.id.cuisine_editTextPlat);

        super.doFilter(false);
    } // void

    /**
     * Implémentation de la méthode abstraite issue de la super classe CustomListActivity
     * <p>
     *     La mise à jour des données est réalisée via l'exécution de la méthode super.clientSendQuantity().
     * </p>
     *
     * @see CustomListActivity
     *
     * @param pResponseFromServer
     *      Réponse serveur de type String.
     */
    protected void updateCurrentPlatAfterCommande(final String pResponseFromServer) {
        super.clientSendQuantity();
    } // void

    /**
     * Implémentation de la méthode abstraite issue de la super classe CustomListActivity
     * <p>
     *     La mise à jour des données est réalisée via l'exécution de la méthode super.clientSendQuantity().
     * </p>
     *
     * @see CustomListActivity
     *
     * @param pResponseFromServer
     *      Réponse serveur de type String.
     */
    protected void updateCurrentPlatAfterAjout(final String pResponseFromServer) {

        mEditTextQte.setText("1");
        mEditTextName.setText("");

        super.clientSendQuantity();
    } // void


    //******************************************************************************
    // callback client: data

    /**
     * Implémentation de l\'interface ClientCallback: données sont reçues du serveur suite à une requête QUANTITE.
     *
     * @param pListData
     *      Données sous forme d'une collection de String.
     */
    public void quantiteFromClient(List<String> pListData) {
        super.createLogD("quantiteFromClient callback");

        Plats nPlats = super.getController().getPlats();

        boolean nIsNewPlat = false;

        int nPlatSize = nPlats.size();

        String nNewPlatNom = "";

        //fixme: le retrait d'un plat de la carte n'est pas pris en compte
        for (int nLen = pListData.size(), i = 1; i < (nLen-1); i+=2) {
            String nNom = pListData.get(i);
            int nStock = Integer.parseInt(pListData.get(i + 1));

            Plat nPlat = nPlats.getPlatByName(nNom);

            // nouveau plat
            if (null == nPlat) {
                nPlat= new Plat(nNom, 0, nStock);
                nPlats.addPlat(nPlat);

                if (!nIsNewPlat) {
                    nIsNewPlat = true;
                    nNewPlatNom = nNom;
                } // if

            } else { // update stock
                nPlat.setStock(nStock);
            } // else

            //super.createLogD("quantiteFromClient for item " + nNom + " " + nQuantite);
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
     * Implémentation de l\'interface ListAdapterCallBack: clic sur bouton gauche.
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
        //super.createLogD("clicLeftFromListAdapter callback");

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
        //super.createLogD("clicRightFromListAdapter callback");

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
        //super.createLogD("addPlat");

        String nStock = mEditTextQte.getText().toString();
        String nName = mEditTextName.getText().toString().trim();

        if (!Tools.isInteger(nStock)) { // check si digit
            super.toastMessage(getString(R.string.activity_toastMessageBadQuantite), false);

        } else if (0 == nName.length()) { // check si nom plat valide
            super.toastMessage(getString(R.string.activity_toastMessageBadPlat), false);

        } else {
            super.clientSendAjout(nStock + " " + nName, true);
        } // else
    } // void

} // class
