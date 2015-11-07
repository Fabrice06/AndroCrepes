package crepes.fr.androcrepes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.List;

import crepes.fr.androcrepes.network.Client;

public class HomeActivity extends AppCompatActivity implements Client.ClientCallBack {

    private static final String TAG = HomeActivity.class.getSimpleName();

    //fixme: deux variables suivantes à changer via le menu settings
//    public static final String SERVER_IP = "10.0.3.2";
    public static final String SERVER_IP = "10.0.2.2";
    public static final int SERVER_PORT = 7777;

    private static boolean mConnected = false;

    private Button mBtnHomeSalle = null;
    private Button mBtnHomeCuisine = null;
    private Button mBtnHomeLog = null;

    private static final String LOGOUT = "logout";
    private static final String LOGON = "logon";

    private Client mClient;

    public final static String EXTRA_ACTION = "EXTRA_ACTION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //Log.d(TAG, "onCreate");

        mBtnHomeSalle = (Button) findViewById(R.id.btnHomeSalle);
        mBtnHomeCuisine = (Button) findViewById(R.id.btnHomeCuisine);
        mBtnHomeLog = (Button) findViewById(R.id.btnHomeLog);

        updateAfterConnection(false);

        //fixme: définir plan B si serveur hors d'atteinte
        mClient = Client.getInstance(this, SERVER_IP, SERVER_PORT);
        mClient.connect();
    } // void


    // event associé au bouton btnHomeCuisine
    public void goCuisine(View v) {
        //Log.d(TAG, "goCuisine");
        startSelectedActivity(CuisineActivity.class);
    }


    // event associé au bouton btnHomeSalle
    public void goSalle(View v) {
        //Log.d(TAG, "goSalle");
        startSelectedActivity(SalleActivity.class);
    }


    // event associé au bouton btnHomeAide
    public void goAide(View view) {
        //Log.d(TAG, "goAide");
        startSelectedActivity(AideActivity.class);
    }


    // event associé au bouton btnLogClient
    public void goLog(View v) {
        //Log.d(TAG, "goLog");

        if (mConnected) {
            mClient.disconnect();

        } else {
            mClient = Client.getInstance(this, SERVER_IP, SERVER_PORT);
            if (! mConnected) {
                mClient.connect();
            }
        }
    } // void


    private void startSelectedActivity(final Class pClass) {
        //Log.d(TAG, "startSelectedActivity");
        Intent nIntent = new Intent(this, pClass);
        startActivity(nIntent);
    } // void


    @Override
    public void connectedFromClient() { // callback d'une connexion client si réussite
        //Log.d(TAG, "connectedFromClient");

        updateAfterConnection(true);
    } // void

    @Override
    public void disconnectedFromClient() { // callback d'une déconnexion client
        //Log.d(TAG, "disconnectFromClient");

        updateAfterConnection(false);
    } // void

    private void updateAfterConnection(final boolean pIsConnected) {
        mConnected = pIsConnected;
        mBtnHomeLog.setText(pIsConnected ? LOGOUT : LOGON);
        mBtnHomeSalle.setEnabled(pIsConnected);
        mBtnHomeCuisine.setEnabled(pIsConnected);
    } // void

    @Override
    public void errorFromClient(String pError) { // callback pour traitement des erreurs
        Log.d(TAG, "notConnectedFromClient");
        //fixme: prévenir l'utilisateur

        // ?? pas sûr ??
        //updateAfterConnection(false);
    } // void

    @Override
    public void singleFromClient(final String pString) { // callback d'une action de type PUT, POST ou DELETE
        Log.d(TAG, "singleFromClient");
    } // void

    @Override
    public void listeFromClient(List<String> pListData) {
//fixme: pas utilisé pour le moment pas toucher
        Log.d(TAG, "listeFromClient");
    }

    @Override
    public void quantiteFromClient(List<String> pListData) { // callback d'une action de type GET (LISTE ou QUANTITE)
        Log.d(TAG, "quantiteFromClient");
    } // void

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionMenu");
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected");
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
} // class
