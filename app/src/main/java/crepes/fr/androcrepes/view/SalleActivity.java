package crepes.fr.androcrepes.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Iterator;
import java.util.List;

import crepes.fr.androcrepes.R;
import crepes.fr.androcrepes.commons.framework.CustomSalleListAdapter;
import crepes.fr.androcrepes.commons.framework.CustomTextView;
import crepes.fr.androcrepes.commons.framework.TemplateActivity;
import crepes.fr.androcrepes.commons.java.EnumSendWord;
import crepes.fr.androcrepes.commons.network.Client;
import crepes.fr.androcrepes.controller.Controller;
import crepes.fr.androcrepes.model.Commande;
import crepes.fr.androcrepes.model.Commandes;
import crepes.fr.androcrepes.model.Plat;


public class SalleActivity
        extends TemplateActivity
        implements CustomSalleListAdapter.SalleListAdapterCallBack {

    private ListView mListView = null;
    private CustomSalleListAdapter mListAdapter;

    private Commandes mCommandes;

    private TextView mEditTextTotal;
    private TextView mEditTextCommande;

    private int mTotalPlat = 0;

    protected int getLayoutId() {
        return R.layout.activity_salle;
    } // int

    protected int getTextViewTitleId() {
        return R.id.salle_customTextViewTitle;
    } // int

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mEditTextTotal = (TextView) findViewById(R.id.salle_textViewTotal);
        mEditTextCommande = (TextView) findViewById(R.id.salle_textViewCommande);

        mCommandes = super.getController().getCommandes();
        mListAdapter = new CustomSalleListAdapter(this, mCommandes);

        mListView = (ListView) findViewById(R.id.salle_listView);
        mListView.setAdapter(mListAdapter);

        super.connectClient();

    } // void


    //******************************************************************************
    // callback client: connexion

    public void disconnectedFromClient() {
        //super.createLogD("disconnectedFromClient");
        super.hideProgressDialog();
    } // void

    /**
     * Implémentation de ClientCallback: la connection est établie.
     *
     * @see Client
     */
    @Override
    public void connectedFromClient() {
        //super.createLogD("connectedFromClient callback");
        super.hideProgressDialog();
        this.updateHeaderInfo();
    } // void

    public void errorFromClient(String pError) {
        //super.createLogD( "errorFromClient");

        //fixme pError pas utilisé
        String nMessage = getString(R.string.activity_toastMessageNoConnection);

        super.toastMessage(nMessage, true);
    } // void

    // callback client: connexion
    //******************************************************************************


    //******************************************************************************
    // callback client: data

    /**
     * Implémentation de ClientCallback: réponse reçue du serveur suite à une requête AJOUT ou COMMANDE.
     *
     * @param pResponseFromServer
     *      Réponse de type String
     */
    @Override
    public void singleFromClient(final String pResponseFromServer) {
        if (mTotalPlat > 0) {
            mTotalPlat--;

        } else {
            super.hideProgressDialog();
        } // else
    }

    /**
     * Implémentation de ClientCallback: données sont reçues du serveur suite à une requête QUANTITE.
     *
     * @param pListData
     *      Données sous forme d'une collection de String.
     */
    @Override
    public void quantiteFromClient(List<String> pListData) {
        super.hideProgressDialog();
    }

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
    public void clicLeftFromListAdapter(Commande pCommande) {
        super.createLogD("clicLeftFromListAdapter callback");

        //approvisionner le stock avec les crepes issues de la commande
        mTotalPlat = pCommande.getTotalPlat();
        this.annulerCommande(pCommande);

        // enlever la commande de la liste
        mCommandes.remove(pCommande);

        // maj de l'ihm
        updateHeaderInfo();
        mListAdapter.notifyDataSetChanged();
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
    public void clicRightFromListAdapter(Commande pCommande) {
        super.createLogD("clicRightFromListAdapter callback " + pCommande.getId());

        //identifier la commande courante
        super.getController().setCurrentCommande(pCommande.getId());

        super.startSelectedActivity(TableActivity.class);
    } // void

    // callback listAdapter
    //******************************************************************************

    private void updateHeaderInfo() {

        switch (mCommandes.size()) {
            case 0:
                mEditTextTotal.setText("");
                mEditTextCommande.setText(R.string.salle_textViewCommande_none);
                break;

            case 1:
                mEditTextTotal.setText(R.string.salle_textViewTotal_one);
                mEditTextCommande.setText(R.string.salle_textViewCommande_one);
                break;

            default:
                mEditTextTotal.setText(mCommandes.getValueOfSize());
                mEditTextCommande.setText(R.string.salle_textViewCommande_more);
        } // switch
    } // void

    private void annulerCommande(final Commande pCommande) {
        Iterator<Plat> nIterator = pCommande.getPlats().iterator();

        while (nIterator.hasNext()) {
            Plat nPlat = nIterator.next();
            if (nPlat.getQuantite() >= 1) {
                super.clientSendAjout(nPlat.getQuantite() + " " + nPlat.getNom(), false);
            } // if
        } // while
    } // void

    public void clearAll(View pView) {
        super.createLogD("clearAll");

        //fixme message attention !

        mTotalPlat = 0;
        Iterator<Commande> nIterator = mCommandes.iterator();
        while (nIterator.hasNext()) {
            mTotalPlat = mTotalPlat + nIterator.next().getTotalPlat();
        } // while

        nIterator = mCommandes.iterator();
        while (nIterator.hasNext()) {
            this.annulerCommande(nIterator.next());
        } // while

        //enlever toutes les commandes de la liste
        mCommandes.clear();

        // maj de l'ihm
        updateHeaderInfo();
        mListAdapter.notifyDataSetChanged();
    } // void

    public void addCommande(View pView) {
        //super.createLogD("addCommande");

        //ajouter une commande dans de la liste
        Commande nCommande = new Commande();
        mCommandes.add(nCommande);

        // maj de l'ihm
        updateHeaderInfo();
        mListAdapter.notifyDataSetChanged();

        //identifier la commande courante
        super.getController().setCurrentCommande(nCommande.getId());

        startSelectedActivity(TableActivity.class);
    } // void

} // class
