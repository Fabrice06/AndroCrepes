package crepes.fr.androcrepes.view;

import android.os.Bundle;

import crepes.fr.androcrepes.R;
import crepes.fr.androcrepes.commons.framework.CustomActivity;
import crepes.fr.androcrepes.commons.java.EnumSendWord;
import crepes.fr.androcrepes.model.Plat;

/**
 * <b>Classe dédiée à la description de l'ihm Salle.</b>
 */

public class TableActivity
        extends CustomActivity {

    //ici les futures view pour la gestion des commandes


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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //ici les futures view pour la gestion des commandes
    } // void

    protected void updateAfterClientAjout() {//final String pNewPlatName) {
        super.clientSendQuantity();

    } // void

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
        super.debugLog("clicLeftFromListAdapter callback");

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
        super.debugLog("clicRightFromListAdapter callback");

        super.clientSendCommande(pPlat.getNom(), true);
    } // void

    // callback listAdapter
    //******************************************************************************

} // class
