package crepes.fr.androcrepes.commons.framework;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import crepes.fr.androcrepes.R;
import crepes.fr.androcrepes.commons.java.EnumReceiveWord;
import crepes.fr.androcrepes.commons.java.EnumSendWord;
import crepes.fr.androcrepes.commons.java.Tools;
import crepes.fr.androcrepes.commons.network.Client;
import crepes.fr.androcrepes.controller.Controller;
import crepes.fr.androcrepes.model.Plat;
import crepes.fr.androcrepes.model.Plats;


public abstract class CustomActivity
        extends AppCompatActivity
        implements Client.ClientCallBack, CustomPlatListAdapter.PlatListAdapterCallBack {

    private static final String TAG = CustomActivity.class.getSimpleName();

    private ProgressDialog mProgressDialog = null;

    private ListView mListView = null;
    private CustomPlatListAdapter mListAdapter;

    private Client mClient;
    private Plats mPlats;

    protected abstract int getLayoutResourceId();
    protected abstract int getTextViewInfoResourceId();
    protected abstract int getListViewResourceId();
    protected abstract int getMenuResourceId();

    protected abstract Plats getPlats();

    protected abstract Controller getController();

    private CustomTextView mCustomTextViewTitle = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        Log.d(TAG, "onCreate");

        final Controller nController = getController();

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.activity_progressDialogWait));
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);

        mCustomTextViewTitle = (CustomTextView) findViewById(getTextViewInfoResourceId());

//        mPlats = mController.getPlats();
        mPlats = getPlats();
        mListAdapter = new CustomPlatListAdapter(this, mPlats);

        mListView = (ListView) findViewById(getListViewResourceId());
        mListView.setAdapter(mListAdapter);

        mProgressDialog.show();

        //Log.d("******* CustomActivity", "IP : " + nController.getServerIp() + " Port : " +  nController.getServerPort());

        //fixme: définir plan B si serveur hors d'atteinte
        mClient = Client.getInstance(this, nController.getServerIp(), nController.getServerPort());
        mClient.connect();
    } // void


    protected CustomTextView getCustomTextViewTitle() {
        return mCustomTextViewTitle;
    } // CustomTextView


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
        clientSendQuantity();
    } // void

    /**
     * Implémentation de ClientCallback: la déconnection est effective.
     *
     * @see Client
     */
    @Override
    public void disconnectedFromClient() {
        //Log.d(TAG, "disconnectedFromClient");
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

    protected abstract void updateCurrentPlatAfterCommande(final String pResponseFromServer);
    protected abstract void updateCurrentPlatAfterAjout(final String pResponseFromServer);

    /**
     * Implémentation de ClientCallback: réponse reçue du serveur suite à une requête AJOUT ou COMMANDE.
     *
     * @param pResponseFromServer
     *      Réponse de type String
     */
    @Override
    public void singleFromClient(final String pResponseFromServer) {
        Log.d(TAG, "singleFromClient callback: " + pResponseFromServer);

        // recherche du dernier mot/chiffre pour identifier la réponse
        String nReponse = pResponseFromServer.substring(pResponseFromServer.lastIndexOf(" ")+1);

        if (nReponse.equals(EnumReceiveWord.EPUISE.getValue()) || (nReponse.equals(EnumReceiveWord.INCONNU.getValue()))) {
            // échec d'une commande ('épuisé' ou 'inconnu' trouvé en fin de message)
            toastMessage(pResponseFromServer + " !", true);

        } else if (nReponse.equals(EnumReceiveWord.COMMANDE.getValue())) { // en réponse à l'ordre COMMANDE
            updateCurrentPlatAfterCommande(pResponseFromServer);

        } else if (Tools.isInteger(nReponse)) { // en réponse à l'ordre AJOUT
            updateCurrentPlatAfterAjout(pResponseFromServer);

        } else {
            // cas non répertorié: ceinture et bretelles
            String nMessage = getString(R.string.activity_toastMessageUnknownError);
            toastMessage(nMessage, true);
        } // else
    } // void


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
    public abstract void quantiteFromClient(List<String> pListData);

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
    public abstract void clicLeftFromListAdapter(Plat pPlat);

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
    public abstract void clicRightFromListAdapter(Plat pPlat);

    // callback listAdapter
    //******************************************************************************

    protected void clientSendQuantity() {

        mClient.send(EnumSendWord.QUANTITE, "");
    } // void

    protected void clientSendAjout(final String pInfoPlat, final boolean pShowProgressDialog) {

        if (pShowProgressDialog) {
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        } // if

        mClient.send(EnumSendWord.AJOUT, pInfoPlat);
    } // void

    protected void clientSendCommande(final String pInfoPlat, final boolean pShowProgressDialog) {

        if (pShowProgressDialog) {
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        } // if

        mClient.send(EnumSendWord.COMMANDE, pInfoPlat);
    } // void

    private void scrollListViewByName(final String pNomPlat) {

        int nIndex = 0;
        while (nIndex < mListView.getCount()) {

            Plat nPlat = (Plat)mListView.getItemAtPosition(nIndex);
            //Log.d(TAG, "scrollListViewByName nom: " + pNomPlat + " getNom " + nPlat.getNom() + " index "+ nIndex);
            if (nPlat.getNom().equals(pNomPlat)) {
                mListView.smoothScrollToPosition(nIndex);
                break;
            } // if
            nIndex++;
        } // while
    } // void

    protected void updateAfterClientQuantite(final String pNomPlat) {
        // maj de l'ihm
        mListAdapter.notifyDataSetChanged();

        if (!pNomPlat.isEmpty()) {
            scrollListViewByName(pNomPlat);

            String nMessage = getString(R.string.activity_toastMessageBeforePlat);
            nMessage = nMessage + pNomPlat;
            nMessage = nMessage + getString(R.string.activity_toastMessageAfterPlat);

            toastMessage(nMessage, true);
        } // if

        mProgressDialog.hide();
    } // void

    protected void debugLog(final String pMessage) {

        Log.d(TAG, pMessage);
    } // void

//    protected void hideProgressDialog() {
//
//        mProgressDialog.hide();
//    } // void


    /**
     * Réalise l'affichage du message passé en paramètre.
     *
     * @param pMessage
     *      Message de type String.
     */
    protected void toastMessage(final String pMessage, final boolean pHideProgressDialog) {

        if (pHideProgressDialog) {
            mProgressDialog.hide();
        } // if

        Toast.makeText(getApplicationContext(), pMessage, Toast.LENGTH_SHORT).show();
    } // void

    /**
     * Evènement associé au bouton imageButtonCuisineGoHome pour naviguer vers l'ihm Home
     *
     * @param pView
     *      Objet de type View
     */
    public void goHome(View pView) {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Log.d(TAG, "onCreateOptionMenu");
        getMenuInflater().inflate(getMenuResourceId(), menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Log.d(TAG, "onOptionsItemSelected");
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

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

