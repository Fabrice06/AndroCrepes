package crepes.fr.androcrepes.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import crepes.fr.androcrepes.commons.java.EnumSendWord;
import crepes.fr.androcrepes.commons.network.Client;
import crepes.fr.androcrepes.controller.Controller;
import crepes.fr.androcrepes.model.Commande;
import crepes.fr.androcrepes.model.Commandes;
import crepes.fr.androcrepes.model.Plat;


public class SalleActivity
        extends AppCompatActivity
        implements Client.ClientCallBack, CustomSalleListAdapter.SalleListAdapterCallBack {

    private static final String TAG = SalleActivity.class.getSimpleName();

    private ProgressDialog mProgressDialog = null;

    private ListView mListView = null;
    private CustomSalleListAdapter mListAdapter;

    private Client mClient;
    private Commandes mCommandes;

    private TextView mEditTextTotal;
    private TextView mEditTextCommande;

    private Controller mController;

    private int mTotalPlat = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salle);
        Log.d(TAG, "onCreate");

        //Get Global Controller Class object (see application tag in AndroidManifest.xml)
        mController = (Controller) getApplicationContext();

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.activity_progressDialogWait));
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);

        CustomTextView nCustomTextView = (CustomTextView) findViewById(R.id.salle_customTextViewTitle);
        mEditTextTotal = (TextView) findViewById(R.id.salle_textViewTotal);
        mEditTextCommande = (TextView) findViewById(R.id.salle_textViewCommande);

        mCommandes = mController.getCommandes();
        mListAdapter = new CustomSalleListAdapter(this, mCommandes);

        mListView = (ListView) findViewById(R.id.salle_listView);
        mListView.setAdapter(mListAdapter);

        mProgressDialog.show();

        //fixme: définir plan B si serveur hors d'atteinte
        mClient = Client.getInstance(this, mController.getServerIp(), mController.getServerPort());
        mClient.connect();

        //this.updateHeaderInfo();
    } // void


    //******************************************************************************
    // callback client: connexion

    /**
     * Implémentation de ClientCallback: la connection est établie.
     *
     * @see Client
     */
    @Override
    public void connectedFromClient() {
        //Log.d(TAG, "connectedFromClient callback");
        mProgressDialog.hide();
        this.updateHeaderInfo();
    } // void

    /**
     * Implémentation de ClientCallback: la déconnection est effective.
     *
     * @see Client
     */
    @Override
    public void disconnectedFromClient() {
        //Log.d(TAG, "disconnectedFromClient callback");
        //fixme: prévenir l'utilisateur ??
        mProgressDialog.hide();
    } // void

    /**
     * Implémentation de ClientCallback: une erreur est transmise.
     *
     * @param pError
     *      Message d'erreur à afficher de type String
     *
     * @see Client
     * @see toastMessage
     */
    @Override
    public void errorFromClient(String pError) {
        //Log.d(TAG, "errorFromClient");

        //fixme pError pas utilisé
        String nMessage = getString(R.string.activity_toastMessageNoConnection);

        mProgressDialog.hide();
        toastMessage(nMessage, true);
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
            mProgressDialog.hide();
        } // else
    }

    /**
     * Implémentation de ClientCallback: données sont reçues du serveur suite à une requête LISTE.
     *
     * @param pListData
     *      Données sous forme d'une collection de String.
     */
    @Override
    public void listeFromClient(List<String> pListData) {
        //Log.d(TAG, "listeFromClient callback");
        //fixme: listeFromClient pas utilisé ici pour le moment
        mProgressDialog.hide();
    } // void

    /**
     * Implémentation de ClientCallback: données sont reçues du serveur suite à une requête QUANTITE.
     *
     * @param pListData
     *      Données sous forme d'une collection de String.
     */
    @Override
    public void quantiteFromClient(List<String> pListData) {
        mProgressDialog.hide();
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
        Log.d(TAG, "clicLeftFromListAdapter callback");

        // fixme remettre en stock les crepes de la commande ???
        mTotalPlat = pCommande.getPlats().size();
        Iterator<Plat> nIterator = pCommande.getPlats().iterator();
        while (nIterator.hasNext()) {
            Plat nPlat = nIterator.next();
            mClient.send(EnumSendWord.AJOUT, "1 " + nPlat.getNom());
        } // while

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
        Log.d(TAG, "clicRightFromListAdapter callback " + pCommande.getId());

        //identifier la commande courante
        mController.setCurrentCommande(pCommande.getId());

        startSelectedActivity(TableActivity.class);
    } // void

    // callback listAdapter
    //******************************************************************************

    private void toastMessage(final String pMessage, final boolean pHideProgressDialog) {

        if (pHideProgressDialog) {
            mProgressDialog.hide();
        } // if

        Toast.makeText(getApplicationContext(), pMessage, Toast.LENGTH_SHORT).show();
    } // void

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

    public void goHome(View pView) {
        finish();
    } // void

    public void clearAll(View pView) {
        Log.d(TAG, "clearAll");

        //fixme message attention !

        mTotalPlat = 0;
        Iterator<Commande> nIterCommande = mCommandes.iterator();
        while (nIterCommande.hasNext()) {
            Commande nCommande = nIterCommande.next();
            mTotalPlat+= nCommande.getPlats().size();
        } // while

        nIterCommande = mCommandes.iterator();
        while (nIterCommande.hasNext()) {
            Commande nCommande = nIterCommande.next();
            Iterator<Plat> nIterPlat = nCommande.getPlats().iterator();
            while (nIterPlat.hasNext()) {
                Plat nPlat = nIterPlat.next();
                mClient.send(EnumSendWord.AJOUT, "1 " + nPlat.getNom());
            } // while
        } // while

        //enlever toutes les commandes de la liste
        mCommandes.clear();

        // maj de l'ihm
        updateHeaderInfo();
        mListAdapter.notifyDataSetChanged();
    } // void

    public void addCommande(View pView) {
        Log.d(TAG, "addCommande");

        //ajouter une commande dans de la liste
        Commande nCommande = new Commande();
        mCommandes.add(nCommande);

        // maj de l'ihm
        updateHeaderInfo();
        mListAdapter.notifyDataSetChanged();

        //identifier la commande courante
        mController.setCurrentCommande(nCommande.getId());

        startSelectedActivity(TableActivity.class);
    } // void

    private void startSelectedActivity(final Class pClass) {
        Log.d(TAG, "startSelectedActivity");
        Intent nIntent = new Intent(this, pClass);
        startActivity(nIntent);
    } // void

    //******************************************************************************
    // cycle de vie activity

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }
} // class
