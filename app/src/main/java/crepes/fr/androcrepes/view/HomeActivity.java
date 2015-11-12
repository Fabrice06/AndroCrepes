package crepes.fr.androcrepes.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import crepes.fr.androcrepes.R;
import crepes.fr.androcrepes.network.Client;

/**
 * <b>Classe dédiée à la description de l'ihm Home.</b>
 * <p>
 *     Cette ihm constitue l'accueil de l'application.
 * </p>
 */
public class HomeActivity extends AppCompatActivity implements Client.ClientCallBack {

    private static final String TAG = HomeActivity.class.getSimpleName();

    //fixme: deux variables suivantes à changer via le menu settings
//    public static final String SERVER_IP = "10.0.3.2";
    public static final String SERVER_IP = "10.0.2.2";
    public static final int SERVER_PORT = 7777;

    private static final String LOGOUT = "logout";
    private static final String LOGON = "logon";
    private static final String WAIT = "Thinking...";

    private ProgressDialog mProgressDialog = null;
    private Button mBtnHomeSalle = null;
    private Button mBtnHomeCuisine = null;
    private Button mBtnHomeLog = null;

    private Client mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //Log.d(TAG, "onCreate");

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(WAIT);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(true);
        mProgressDialog.show();

        TextView myTextView = (TextView) findViewById(R.id.laBonneCrepe);
        Typeface myFont = Typeface.createFromAsset(getAssets(), "Milasian.ttf");
        myTextView.setTypeface(myFont);
        myTextView.setTextSize(30);

        mBtnHomeSalle = (Button) findViewById(R.id.btnHomeSalle);
        mBtnHomeCuisine = (Button) findViewById(R.id.btnHomeCuisine);
        mBtnHomeLog = (Button) findViewById(R.id.btnHomeLog);

        updateAfterConnection(false);

        //fixme: définir plan B si serveur hors d'atteinte
        mClient = Client.getInstance(this, SERVER_IP, SERVER_PORT);
        mClient.connect();

    } // void

    /**
     * Evènement associé au bouton btnHomeCuisine pour naviguer vers l'ihm Cuisine
     *
     * @param pView
     *      Objet de type View
     *
     * @see startSelectedActivity
     */
    public void goCuisine(View pView) {
        //Log.d(TAG, "goCuisine");
        startSelectedActivity(CuisineActivity.class);
    }

    /**
     * Evènement associé au bouton btnHomeSalle pour naviguer vers l'ihm Salle
     *
     * @param pView
     *      Objet de type View
     *
     * @see startSelectedActivity
     */
    public void goSalle(View pView) {
        //Log.d(TAG, "goSalle");
        startSelectedActivity(SalleActivity.class);
    }

    /**
     * Evènement associé au bouton btnHomeAide pour naviguer vers l'ihm Aide
     *
     * @param pView
     *      Objet de type View
     *
     * @see startSelectedActivity
     */
    public void goAide(View pView) {
        //Log.d(TAG, "goAide");
        startSelectedActivity(AideActivity.class);
    }

    /**
     * Evènement associé au bouton btnHomeLog pour gérer la connexion au serveur distant
     *
     * @param pView
     *      Objet de type View
     */
    public void goLog(View pView) {
        //Log.d(TAG, "goLog");

        mProgressDialog.show();

        if (mClient.isRunning()) {
            mClient.disconnect();

            //fixme pour palier au bug: pas de callback disconnectedFromClient ???
            mProgressDialog.hide();
            updateAfterConnection(false);

        } else {
            mClient = Client.getInstance(this, SERVER_IP, SERVER_PORT);
            mClient.connect();
        } // else
    } // void

    /**
     * Réalise le lancement de l'activité
     *
     * @param pClass
     *      Activité à lancer de type Class.
     */
    private void startSelectedActivity(final Class pClass) {
        //Log.d(TAG, "startSelectedActivity");
        Intent nIntent = new Intent(this, pClass);
        startActivity(nIntent);
    } // void


    //******************************************************************************
    // callback Client: connexion

    /**
     * Implémentation de ClientCallback: la connection est établie.
     *
     * @see Client
     * @see updateAfterConnection
     */
    @Override
    public void connectedFromClient() {
        //Log.d(TAG, "connectedFromClient");

        mProgressDialog.hide();
        updateAfterConnection(true);
    } // void

    /**
     * Implémentation de ClientCallback: la déconnection est effective.
     *
     * @see Client
     * @see updateAfterConnection
     */
    @Override
    public void disconnectedFromClient() {
        //Log.d(TAG, "disconnectedFromClient");

        mProgressDialog.hide();
        updateAfterConnection(false);
    } // void

    /**
     * Implémentation de ClientCallback: une erreur est transmise.
     *
     * @param pError
     *      Message d'erreur à afficher de type String
     *
     * @see Client
     * @see updateAfterConnection
     * @see toastMessage
     */
    @Override
    public void errorFromClient(String pError) {
        //Log.d(TAG, "errorFromClient");

        mProgressDialog.hide();
        updateAfterConnection(false);
        toastMessage(pError);
    } // void

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
    public void singleFromClient(final String pString) { // callback d'une action de type PUT, POST ou DELETE
        //Log.d(TAG, "singleFromClient");
        //fixme: singleFromClient pas utilisé ici pour le moment
        mProgressDialog.hide();
    } // void

    /**
     * Implémentation de ClientCallback: données sont reçues du serveur suite à une requête LISTE.
     *
     * @param pListData
     *      Données sous forme d'une collection de String.
     */
    @Override
    public void listeFromClient(List<String> pListData) {
        //Log.d(TAG, "listeFromClient");
        //fixme: listeFromClient pas utilisé ici pour le moment
        mProgressDialog.hide();
    }

    /**
     * Implémentation de ClientCallback: données sont reçues du serveur suite à une requête QUANTITE.
     *
     * @param pListData
     *      Données sous forme d'une collection de String.
     */
    @Override
    public void quantiteFromClient(List<String> pListData) {
        //Log.d(TAG, "quantiteFromClient");
        //fixme: quantiteFromClient pas utilisé ici pour le moment
        mProgressDialog.hide();
    } // void

    // callback Client: data
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
     * Réalise la mise à jour de l'affichage
     *
     * @param pIsConnected
     *      Vrai: la connexion est établie et active.
     */
    private void updateAfterConnection(final boolean pIsConnected) {
        mBtnHomeLog.setText(pIsConnected ? LOGOUT : LOGON);
        mBtnHomeSalle.setEnabled(pIsConnected);
        mBtnHomeCuisine.setEnabled(pIsConnected);
    } // void

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
