package crepes.fr.androcrepes.view;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import crepes.fr.androcrepes.R;
import crepes.fr.androcrepes.commons.framework.ListAdapter;
import crepes.fr.androcrepes.commons.java.EnumReceiveWord;
import crepes.fr.androcrepes.commons.java.EnumSendWord;
import crepes.fr.androcrepes.commons.java.Tools;
import crepes.fr.androcrepes.controller.Controller;
import crepes.fr.androcrepes.model.Commandes;
import crepes.fr.androcrepes.model.Plat;
import crepes.fr.androcrepes.model.Plats;
import crepes.fr.androcrepes.commons.network.Client;

/**
 * <b>Classe dédiée à la description de l'ihm Salle.</b>
 */

public class SalleActivity
        extends AppCompatActivity
        implements Client.ClientCallBack, ListAdapter.ListAdapterCallBack {

    private static final String TAG = SalleActivity.class.getSimpleName();

    private static final String WAIT = "Thinking...";

    private ProgressDialog mProgressDialog = null;

    private ListView mListViewSalle = null;
    private ListAdapter mListAdapter;

    private Client mClient;
    private Plats mPlats;
    private Commandes mCommandes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salle);
        //Log.d(TAG, "onCreate");

        //Get Global Controller Class object (see application tag in AndroidManifest.xml)
        final Controller nController = (Controller) getApplicationContext();

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(WAIT);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        TextView nTextViewInfo = (TextView) findViewById(R.id.salle_textViewInfoId);
        Typeface nFont = Typeface.createFromAsset(getAssets(), "Milasian.ttf");
        nTextViewInfo.setTypeface(nFont);
        nTextViewInfo.setTextSize(30);

        mPlats = nController.getPlats();
        mListAdapter = new ListAdapter(this, mPlats);

        mListViewSalle = (ListView) findViewById(R.id.listViewSalle);
        mListViewSalle.setAdapter(mListAdapter);

        //fixme: définir plan B si serveur hors d'atteinte
        mClient = Client.getInstance(this, HomeActivity.SERVER_IP, HomeActivity.SERVER_PORT);
        mClient.connect();
        mClient.send(EnumSendWord.QUANTITE, "");

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
        mProgressDialog.show();
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
        mProgressDialog.hide();
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
        mProgressDialog.hide();
        toastMessage(pError);
    }

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
    public void singleFromClient(final String pString) { // callback d'une action de type PUT, POST ou DELETE
        //Log.d(TAG, "singleFromClient callback: " + pString);

        // recherche du dernier mot/chiffre pour identifier la réponse
        String nReponse = pString.substring(pString.lastIndexOf(" ")+1);

        if (nReponse.equals(EnumReceiveWord.EPUISE.getValue()) || (nReponse.equals(EnumReceiveWord.INCONNU.getValue()))) {
            // échec d'une commande ('épuisé' ou 'inconnu' trouvé en fin de message)
            mProgressDialog.hide();
            toastMessage(pString + " !");

        } else if (nReponse.equals(EnumReceiveWord.COMMANDE.getValue()) || Tools.isInteger(nReponse)) {
            mClient.send(EnumSendWord.QUANTITE, "");

        } else {
            // cas non répertorié: ceinture et bretelles
            mProgressDialog.hide();
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
    public void quantiteFromClient(List<String> pListData) { // callback d'une action de type GET (LISTE ou QUANTITE)
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

        mProgressDialog.hide();
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
        //Log.d(TAG, "clicLeftFromListAdapter callback");
        mProgressDialog.show();
        mClient.send(EnumSendWord.AJOUT, "1 " + pPlat.getNom());
    }

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
        //Log.d(TAG, "clicRightFromListAdapter callback");
        mProgressDialog.show();
        mClient.send(EnumSendWord.COMMANDE, pPlat.getNom());
    }

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Log.d(TAG, "onCreateOptionMenu");
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
} // class
