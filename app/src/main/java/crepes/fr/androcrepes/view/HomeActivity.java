package crepes.fr.androcrepes.view;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import crepes.fr.androcrepes.R;
import crepes.fr.androcrepes.commons.network.Client;
import crepes.fr.androcrepes.controller.Controller;
import crepes.fr.androcrepes.controller.SettingsFragment;
import android.preference.PreferenceManager;

/**
 * <b>Classe dédiée à la description de l'ihm Home.</b>
 * <p>
 *     Cette ihm constitue l'accueil de l'application.
 * </p>
 */
public class HomeActivity
        extends AppCompatActivity
        implements Client.ClientCallBack {

    private static final String TAG = HomeActivity.class.getSimpleName();

    private static SettingsFragment frag = new SettingsFragment();
    public static FragmentManager fragmentManager;
    private SharedPreferences sharedPref;

    private ProgressDialog mProgressDialog = null;
    
    private Button mBtnHomeSalle = null;
    private Button mBtnHomeCuisine = null;
    private Button mBtnHomeLog = null;

    private Client mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG, "onCreate");

        //Get Global Controller Class object (see application tag in AndroidManifest.xml)
        final Controller nController = (Controller) getApplicationContext();

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(Controller.WAIT);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(true);

        TextView myTextView = (TextView) findViewById(R.id.laBonneCrepe);
        Typeface myFont = Typeface.createFromAsset(getAssets(), "Milasian.ttf");
        myTextView.setTypeface(myFont);
        myTextView.setTextSize(30);

        mBtnHomeSalle = (Button) findViewById(R.id.btnHomeSalle);
        mBtnHomeCuisine = (Button) findViewById(R.id.btnHomeCuisine);
        mBtnHomeLog = (Button) findViewById(R.id.btnHomeLog);

        updateButtonsAfterConnection(false);

        mProgressDialog.show();

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        fragmentManager = getFragmentManager();


        // La première fois que l'application est lancée, on lit les préférences par défaut du
        // fichier XML
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        Log.d("******* HomeActivity", "IP : "+Controller.SERVER_IP+" Port : "+Controller.SERVER_PORT);

        //fixme: définir plan B si serveur hors d'atteinte
        mClient = Client.getInstance(this, Controller.SERVER_IP, Controller.SERVER_PORT);
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
        Log.d(TAG, "goLog");

        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        Log.d("***************** goLog", "IP : " + Controller.SERVER_IP + " Port : " + Controller.SERVER_PORT);

        if (mClient.isRunning()) {
            mClient.disconnect();

            //fixme pour palier au bug: pas de callback disconnectedFromClient ???
//            mProgressDialog.hide();
//            updateAfterConnection(false);

        } else {
            mClient = Client.getInstance(this, Controller.SERVER_IP, Controller.SERVER_PORT);
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
        Log.d(TAG, "startSelectedActivity");
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
        Log.d(TAG, "connectedFromClient");

        mProgressDialog.hide();
        updateButtonsAfterConnection(true);
    } // void

    /**
     * Implémentation de ClientCallback: la déconnection est effective.
     *
     * @see Client
     * @see updateAfterConnection
     */
    @Override
    public void disconnectedFromClient() {
        Log.d(TAG, "disconnectedFromClient");

        mProgressDialog.hide();
        updateButtonsAfterConnection(false);
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
        updateButtonsAfterConnection(false);
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
    private void updateButtonsAfterConnection(final boolean pIsConnected) {
        mBtnHomeLog.setText(pIsConnected ? R.string.btnLogout : R.string.btnLogon);
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
        Log.d(TAG, "********  onOptionsItemSelected");
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Log.d("********** IF 1 ******"," IF ");
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            if (!(frag.isAdded())) {
                Log.i("************** MainActivity", "onOptionItemSelected.IF");
                transaction.add(R.id.fragmentSettings, frag);
                transaction.addToBackStack(null);
                transaction.commit();
            }



 //           fragmentManager.executePendingTransactions();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        Log.d(TAG,"onBackPressed");

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (frag.isAdded()) {
            Log.i("MainActivity", "onBackPressed.isAdded");
//            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
//            transaction.remove(frag);
            fragmentManager.popBackStack();
        } else {
            Log.i("MainActivity", "onBackPressed.isNotAdded");
            super.onBackPressed();
        }
        transaction.commit();
    }

    //******************************************************************************
    // cycle de vie activity

//    lancement appli
//    HomeActivity: onCreate
//    HomeActivity: onStart
//    HomeActivity: onResume

//    home -> salle
//    HomeActivity: onPause
//    SalleActivity: onCreate
//    SalleActivity: onStart
//    SalleActivity: onResume
//    HomeActivity: onStop

//    salle -> home
//    SalleActivity: onPause
//    HomeActivity: onRestart
//    HomeActivity: onStart
//    HomeActivity: onResume
//    SalleActivity: onStop
//    SalleActivity: onDestroy

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

        // retour de cuisine ou de salle via finish
        mClient.setCallBack(this);
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
