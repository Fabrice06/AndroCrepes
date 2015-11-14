package crepes.fr.androcrepes.view;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import crepes.fr.androcrepes.R;
import crepes.fr.androcrepes.commons.framework.CustomProgressDialog;
import crepes.fr.androcrepes.commons.framework.ListAdapter;
import crepes.fr.androcrepes.commons.java.EnumSendWord;
import crepes.fr.androcrepes.commons.network.Client;
import crepes.fr.androcrepes.controller.Controller;
import crepes.fr.androcrepes.model.Plat;
import crepes.fr.androcrepes.model.Plats;


public abstract class CustomActivity
        extends AppCompatActivity
        implements Client.ClientCallBack, ListAdapter.ListAdapterCallBack {

    private static final String TAG = CustomActivity.class.getSimpleName();

    private CustomProgressDialog mProgressDialog = null;

    private ListView mListViewSalle = null;
    private ListAdapter mListAdapter;

    private Client mClient;
    private Plats mPlats;

    protected abstract int getLayoutResourceId();
    protected abstract int getTextViewInfoResourceId();
    protected abstract int getListViewResourceId();
    protected abstract int getMenuResourceId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        Log.d(TAG, "onCreate");

        //Get Global Controller Class object (see application tag in AndroidManifest.xml)
        final Controller nController = (Controller) getApplicationContext();

        mProgressDialog = nController.getProgressDialog(this);
        mProgressDialog.showMessage(Controller.WAIT, false);

        TextView nTextViewInfo = (TextView) findViewById(getTextViewInfoResourceId());
        Typeface nFont = Typeface.createFromAsset(getAssets(), "Milasian.ttf");
        nTextViewInfo.setTypeface(nFont);
        nTextViewInfo.setTextSize(30);

        mPlats = nController.getPlats();
        mListAdapter = new ListAdapter(this, mPlats);

        mListViewSalle = (ListView) findViewById(getListViewResourceId());
        mListViewSalle.setAdapter(mListAdapter);

        //fixme: définir plan B si serveur hors d'atteinte
        mClient = Client.getInstance(this, Controller.SERVER_IP, Controller.SERVER_PORT);
        mClient.connect();
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
        mProgressDialog.hideMessage();
        mClient.send(EnumSendWord.QUANTITE, "");
    }

    /**
     * Implémentation de ClientCallback: la déconnection est effective.
     *
     * @see Client
     */
    @Override
    public void disconnectedFromClient() {
        //Log.d(TAG, "disconnectedFromClient");
        //fixme: prévenir l'utilisateur ??
        mProgressDialog.hideMessage();
    }

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
        toastMessage(pError, true);
    } // void

    // callback client: connexion
    //******************************************************************************


    //******************************************************************************
    // callback client: data

    /**
     * Implémentation de ClientCallback: réponse reçue du serveur suite à une requête AJOUT ou COMMANDE.
     *
     * @param pString
     *      Réponse de type String
     */
    @Override
    public abstract void singleFromClient(final String pString);


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
        mProgressDialog.hideMessage();
    } // void

    /**
     * Implémentation de ClientCallback: données sont reçues du serveur suite à une requête QUANTITE.
     *
     * @param pListData
     *      Données sous forme d'une collection de String.
     */
    @Override
    public void quantiteFromClient(List<String> pListData) {
        //Log.d(TAG, "quantiteFromClient callback");

        //fixme: le retrait d'un plat de la carte n'est pas pris en compte
        for (int nLen = pListData.size(), i = 1; i < (nLen-1); i+=2) {
            String nNom = pListData.get(i);
            int nQuantite = Integer.parseInt(pListData.get(i + 1));

            Plat nPlat = mPlats.getPlat(nNom);
            // nouveau plat
            if (null == nPlat) {
                nPlat= new Plat(nNom, nQuantite);
                mPlats.addPlat(nPlat);

            } else { // update quantité
                nPlat.setQuantite(nQuantite);
            } // else

            //Log.d(TAG, "quantiteFromClient for item " + nNom + " " + nQuantite);
        } // for

        // maj de l'ihm
        mListAdapter.notifyDataSetChanged();

        mProgressDialog.hideMessage();
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
            mProgressDialog.showMessage(Controller.WAIT, true);
        } // if

        mClient.send(EnumSendWord.AJOUT, pInfoPlat);
    } // void

    protected void clientSendCommande(final String pInfoPlat, final boolean pShowProgressDialog) {

        if (pShowProgressDialog) {
            mProgressDialog.showMessage(Controller.WAIT, true);
        } // if

        mClient.send(EnumSendWord.COMMANDE, pInfoPlat);
    } // void

    protected void debugLog(final String pMessage) {

        Log.d(TAG, pMessage);
    } // void


    /**
     * Réalise l'affichage du message passé en paramètre.
     *
     * @param pMessage
     *      Message de type String.
     */
    protected void toastMessage(final String pMessage, final boolean pHideProgressDialog) {

        if (pHideProgressDialog) {
            mProgressDialog.hideMessage();
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

