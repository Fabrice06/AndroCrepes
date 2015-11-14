package crepes.fr.androcrepes.view;


import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import crepes.fr.androcrepes.R;
import crepes.fr.androcrepes.commons.framework.CustomProgressDialog;
import crepes.fr.androcrepes.commons.framework.ListAdapter;
import crepes.fr.androcrepes.commons.java.EnumReceiveWord;
import crepes.fr.androcrepes.commons.java.EnumSendWord;
import crepes.fr.androcrepes.commons.java.Tools;
import crepes.fr.androcrepes.commons.network.Client;
import crepes.fr.androcrepes.controller.Controller;
import crepes.fr.androcrepes.model.Plat;
import crepes.fr.androcrepes.model.Plats;

/**
 * <b>Classe dédiée à la description de l'ihm Cuisine.</b>
 */
public class CuisineActivity
        extends AppCompatActivity
        implements Client.ClientCallBack, ListAdapter.ListAdapterCallBack {

    private static final String TAG = CuisineActivity.class.getSimpleName();

    private CustomProgressDialog mProgressDialog = null;

    private ListView mListViewCuisine = null;
    private ListAdapter mListAdapter;

    private EditText mEditTextQte;
    private EditText mEditTextName;

    private Client mClient;
    private Plats mPlats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuisine);
        //Log.d(TAG, "onCreate");

        //Get Global Controller Class object (see application tag in AndroidManifest.xml)
        final Controller nController = (Controller) getApplicationContext();

        mProgressDialog = nController.getProgressDialog(this);
        mProgressDialog.showMessage(Controller.WAIT, false);

        TextView nTextViewInfo = (TextView) findViewById(R.id.cuisine_textViewInfoId);
        Typeface nFont = Typeface.createFromAsset(getAssets(), "Milasian.ttf");
        nTextViewInfo.setTypeface(nFont);
        nTextViewInfo.setTextSize(30);

        mPlats = nController.getPlats();
        mListAdapter = new ListAdapter(this, mPlats);

        mListViewCuisine = (ListView) findViewById(R.id.listViewCuisine);
        mListViewCuisine.setAdapter(mListAdapter);

        mEditTextQte = (EditText) findViewById(R.id.cuisine_editTextQuantiteId);
        mEditTextName = (EditText) findViewById(R.id.cuisine_editTextPlatId);

        //fixme: définir plan B si serveur hors d'atteinte
        mClient = Client.getInstance(this, Controller.SERVER_IP, Controller.SERVER_PORT);
        mClient.connect();
        mClient.send(EnumSendWord.QUANTITE, "");

    } // void

    //******************************************************************************
    // callback Client: connexion

    /**
     * Implémentation de ClientCallback: la connection est établie.
     *
     * @see Client
     */
    @Override
    public void connectedFromClient() { // callback d'une connexion client si réussite
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
    public void disconnectedFromClient() { // callback d'une déconnexion client
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
    public void errorFromClient(String pError) { // callback d'une connexion client si réussite
        //Log.d(TAG, "errorFromClient");
        mProgressDialog.hideMessage();
        toastMessage(pError);
    }

    // callback Client: connexion
    //******************************************************************************


    //******************************************************************************
    // callback Client: data

    /**
     * Implémentation de ClientCallback: réponse reçue du serveur suite à une requête AJOUT ou COMMANDE.
     *
     * @param pString
     *      Réponse de type String
     */
    @Override
    public void singleFromClient(final String pString) {
        Log.d(TAG, "singleFromClient callback: " + pString);

        // recherche du dernier mot/chiffre pour identifier la réponse
        String nReponse = pString.substring(pString.lastIndexOf(" ")+1);

        if (nReponse.equals(EnumReceiveWord.EPUISE.getValue()) || (nReponse.equals(EnumReceiveWord.INCONNU.getValue()))) {
            // échec d'une commande ('épuisé' ou 'inconnu' trouvé en fin de message)
            mProgressDialog.hideMessage();
            toastMessage(pString + " !");

        } else if (nReponse.equals(EnumReceiveWord.COMMANDE.getValue())) { // en réponse à l'ordre COMMANDE
            mClient.send(EnumSendWord.QUANTITE, "");

        } else if (Tools.isInteger(nReponse)) { // en réponse à l'ordre AJOUT
            mClient.send(EnumSendWord.QUANTITE, "");

            mEditTextQte.setText("1");
            mEditTextName.setText("");

            mProgressDialog.hideMessage();

            // fixme scroll seulement sur un nouveau plat ???

            // fixme affichage seulement sur un nouveau plat ???
            toastMessage("Le plat a été ajouté à la carte !");

        } else {
            // cas non répertorié: ceinture et bretelles
            mProgressDialog.hideMessage();
            toastMessage(pString + "Erreur inconnue: merci de prévenir l'administrateur !");
            //fixme: mettre en place un fichier de log pour ce cas ??
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
        //Log.d(TAG, "quantiteFromClient callback");
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
        //Log.d(TAG, "clicLeftFromListAdapter callback");
        mProgressDialog.showMessage(Controller.WAIT, true);
        mClient.send(EnumSendWord.COMMANDE, pPlat.getNom());
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
        //Log.d(TAG, "clicRightFromListAdapter callback");
        mProgressDialog.showMessage(Controller.WAIT, true);
        mClient.send(EnumSendWord.AJOUT, "1 " + pPlat.getNom());
    } // void

    // callback listAdapter
    //******************************************************************************

    /**
     * Réalise l'affichage du message passé en paramètre.
     *
     * @param pMessage
     *      Message de type String.
     */
    public void toastMessage(final String pMessage) {
        Toast.makeText(getApplicationContext(), pMessage, Toast.LENGTH_SHORT).show();
    }

    /**
     * Evènement associé au bouton imageButtonCuisineGoHome pour naviguer vers l'ihm Home
     *
     * @param pView
     *      Objet de type View
     */
    public void goHome(View pView) {
        finish();
    } // void

    /**
     * Evènement associé au bouton buttonCuisineAddNewPlat pour ajouter un nouveau plat
     *
     * @param pView
     *      Objet de type View
     */
    public void addNewPlat(View pView) {

        String nQuantite = mEditTextQte.getText().toString();
        String nName = mEditTextName.getText().toString().trim();

        if (!Tools.isInteger(nQuantite)) { // check si digit
            toastMessage("Merci de préciser une quantité correcte !");

        } else if (0 == nName.length()) { // check si nom plat valide
            toastMessage("Merci de préciser un nom de plat !");

        } else {
            mProgressDialog.showMessage(Controller.WAIT, true);
            mClient.send(EnumSendWord.AJOUT, nQuantite + " " + nName);
        } // else
    } // void


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Log.d(TAG, "onCreateOptionsMenu");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cuisine, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Log.d(TAG, "onOptionsItemSelected");
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
} // class
