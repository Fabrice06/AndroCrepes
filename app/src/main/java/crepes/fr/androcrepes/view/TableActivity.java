package crepes.fr.androcrepes.view;

import android.os.Bundle;

import java.util.List;

import crepes.fr.androcrepes.R;
import crepes.fr.androcrepes.commons.framework.CustomActivity;
import crepes.fr.androcrepes.commons.java.EnumSendWord;
import crepes.fr.androcrepes.controller.Controller;
import crepes.fr.androcrepes.model.Plat;
import crepes.fr.androcrepes.model.Plats;

/**
 * <b>Classe dédiée à la description de l'ihm Salle.</b>
 */

public class TableActivity
        extends CustomActivity {

    //ici les futures view pour la gestion des commandes
    private Controller mController = null;

    private Plat mCurrentPlat = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String nTitle = getString(R.string.table_customTextViewTitle);
        nTitle = nTitle + mController.getCurrentCommande().getValueOfId();
        super.getCustomTextViewTitle().setText(nTitle);
        super.debugLog("onCreate " + nTitle);

        //ici les futures view pour la gestion des commandes
    } // void

    protected int getLayoutResourceId() {
        return R.layout.activity_table;
    } // int

    protected int getTextViewInfoResourceId() {
        return R.id.table_customTextViewTitle;
    } // int

    protected int getListViewResourceId() {
        return R.id.table_listView;
    } // int

    protected int getMenuResourceId() {
        return R.menu.menu_table;
    } // int

    protected Plats getPlats() {
        return mController.getCurrentCommande().getPlats();
    } // Plats

    protected Controller getController() {
        if (null == mController) {
            //Get Global Controller Class object (see application tag in AndroidManifest.xml)
            mController = (Controller) getApplicationContext();
        }
        return mController;
    } // Controller

    protected void updateCurrentPlatAfterCommande(final String pResponseFromServer) {
        if (null != mCurrentPlat) {
            if (pResponseFromServer.indexOf(mCurrentPlat.getNom()) >= 0) {
                mCurrentPlat.setQuantite(mCurrentPlat.getQuantite() + 1);
            }
        }
        super.clientSendQuantity();
    } // void

    protected void updateCurrentPlatAfterAjout(final String pResponseFromServer) {
        if (null != mCurrentPlat) {
            if (pResponseFromServer.indexOf(mCurrentPlat.getNom()) >= 0) {
                if (mCurrentPlat.getQuantite() >= 1) {
                    mCurrentPlat.setQuantite(mCurrentPlat.getQuantite() - 1);
                } else {
                    mCurrentPlat.setQuantite(0);
                }
            }
        }
        super.clientSendQuantity();
    } // void


    //******************************************************************************
    // callback client: data

    public void quantiteFromClient(List<String> pListData) {
        super.debugLog("quantiteFromClient callback");

        Plats nPlats = mController.getCurrentCommande().getPlats();

        boolean nIsNewPlat = false;

//        int nPlatSize = nPlats.size();

//        String nNewPlatNom = "";

        for (int nLen = pListData.size(), i = 1; i < (nLen-1); i+=2) {
            String nNom = pListData.get(i);
            //int nQuantite = Integer.parseInt(pListData.get(i + 1));

            Plat nPlat = nPlats.getPlatByName(nNom);

            // nouveau plat
            if (null == nPlat) {
                //nPlat= new Plat(nNom, nQuantite);
                nPlat= new Plat(nNom, 0);
                nPlats.addPlat(nPlat);

                if (!nIsNewPlat) {
                    nIsNewPlat = true;
//                    nNewPlatNom = nNom;
                } // if

//            } else { // update quantité
//                nPlat.setQuantite(nQuantite);
            } // else

            //Log.d(TAG, "quantiteFromClient for item " + nNom + " " + nQuantite);
        } // for

        // tri par nom de plat si nouvel ajout
        if (nIsNewPlat) {
            nPlats.sort();
        } // if

//        if (nPlats.size() != (nPlatSize + 1)) {
//            nNewPlatNom = "";
//        } // if

        super.updateAfterClientQuantite("");
    } // void

    // callback client: data
    //******************************************************************************


    //******************************************************************************
    // callback listAdapter

    /**
     * Implémentation de ListAdapterCallBack: clic sur bouton gauche.
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
    public void clicLeftFromListAdapter(Plat pPlat) {
        //super.debugLog("clicLeftFromListAdapter callback");

        mCurrentPlat = pPlat;
        super.clientSendAjout("1 " + pPlat.getNom(), true);
    } // void

    /**
     * Implémentation de ListAdapterCallBack: clic sur bouton droit.
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
    public void clicRightFromListAdapter(Plat pPlat) {
        //super.debugLog("clicRightFromListAdapter callback");

        mCurrentPlat = pPlat;
        super.clientSendCommande(pPlat.getNom(), true);
    } // void

    // callback listAdapter
    //******************************************************************************

} // class
