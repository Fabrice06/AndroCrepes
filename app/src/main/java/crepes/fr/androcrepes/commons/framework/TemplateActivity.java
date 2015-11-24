package crepes.fr.androcrepes.commons.framework;

import android.app.ProgressDialog;
import android.content.Intent;
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


public abstract class TemplateActivity
        extends AppCompatActivity
        implements Client.ClientCallBack {

    private static final String TAG = TemplateActivity.class.getSimpleName();

    private Controller mController;
    private Client mClient;

    private ProgressDialog mProgressDialog = null;

    protected abstract int getLayoutId();
    protected abstract int getTextViewTitleId();

    private CustomTextView mCustomTextViewTitle = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        this.createLogD("onCreate");

        mController = (Controller) getApplicationContext();

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.activity_progressDialogWait));
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);

        mCustomTextViewTitle = (CustomTextView) findViewById(getTextViewTitleId());

        //fixme: définir plan B si serveur hors d'atteinte
        mClient = Client.getInstance(this, mController.getServerIp(), mController.getServerPort());

    } // void

    protected void connectClient() {
        mProgressDialog.show();
        mClient.connect();
    }

    protected void toggleClient() {
        this.createLogD("toggleClient");

        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        if (mClient.isRunning()) {
            mClient.disconnect();

        } else {
            connectClient();
        } // else
    } // void

    protected void setTitle(final String pTitle) {
        mCustomTextViewTitle.setText(pTitle);
    } // void

    protected Controller getController() {
        return mController;
    } // Controller

    //******************************************************************************
    // callback client: connexion

    /**
     * Implémentation de ClientCallback: la connection est établie.
     *
     * @see Client
     */
    @Override
    public abstract void connectedFromClient();


    /**
     * Implémentation de ClientCallback: la déconnection est effective.
     *
     * @see Client
     */
    @Override
    public abstract void disconnectedFromClient();

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
    public abstract void errorFromClient(String pError);

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
    public abstract void singleFromClient(final String pResponseFromServer);


    /**
     * Implémentation de ClientCallback: données sont reçues du serveur suite à une requête LISTE.
     *
     * @param pListData
     *      Données sous forme d'une collection de String.
     */
    @Override
    public void listeFromClient(List<String> pListData) {
        //this.createLogD("listeFromClient callback");
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

    /**
     * Evènement associé au bouton imageButtonCuisineGoHome pour naviguer vers l'ihm Home
     *
     * @param pView
     *      Objet de type View
     */
    public void goHome(View pView) {
        finish();
    }

    protected void createLogD(final String pMessage) {

        Log.d(TAG, pMessage);
    } // void

    protected void toastMessage(final String pMessage, final boolean pHideProgressDialog) {

        if (pHideProgressDialog) {
            mProgressDialog.hide();
        } // if

        Toast.makeText(getApplicationContext(), pMessage, Toast.LENGTH_SHORT).show();
    } // void

    protected void startSelectedActivity(final Class pClass) {
        this.createLogD("startSelectedActivity");

        Intent nIntent = new Intent(this, pClass);
        startActivity(nIntent);
    } // void

    protected void hideProgressDialog() {

        mProgressDialog.hide();
    } // void

    //******************************************************************************
    // cycle de vie activity

    @Override
    protected void onStart() {
        super.onStart();
        this.createLogD("onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.createLogD("onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        this.createLogD("onRestart");

        // retour d'une autre activity via finish
        mClient.setCallBack(this);
    }

    @Override
    protected void onPause() {
        this.createLogD("onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        this.createLogD("onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        this.createLogD("onDestroy");
        super.onDestroy();
    }
} // class

