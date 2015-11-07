package crepes.fr.androcrepes;

import android.app.ProgressDialog;
import android.content.Intent;
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

import crepes.fr.androcrepes.network.Client;

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
        mProgressDialog.setCancelable(false);
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


    // event associé au bouton btnHomeLog
    public void goLog(View v) {
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


    private void startSelectedActivity(final Class pClass) {
        //Log.d(TAG, "startSelectedActivity");
        Intent nIntent = new Intent(this, pClass);
        startActivity(nIntent);
    } // void


    //******************************************************************************
    // callback Client: connexion

    @Override
    public void connectedFromClient() { // callback d'une connexion client si réussite
        //Log.d(TAG, "connectedFromClient");

        mProgressDialog.hide();
        updateAfterConnection(true);
    } // void

    @Override
    public void disconnectedFromClient() { // callback d'une déconnexion client
        //Log.d(TAG, "disconnectedFromClient");

        mProgressDialog.hide();
        updateAfterConnection(false);
    } // void

    @Override
    public void errorFromClient(String pError) { // callback pour traitement des erreurs
        //Log.d(TAG, "errorFromClient");

        mProgressDialog.hide();
        updateAfterConnection(false);
        toastMessage(pError);
    } // void

    // callback Client: connexion
    //******************************************************************************


    //******************************************************************************
    // callback Client: data

    @Override
    public void singleFromClient(final String pString) { // callback d'une action de type PUT, POST ou DELETE
        //Log.d(TAG, "singleFromClient");
        //fixme: singleFromClient pas utilisé ici pour le moment
        mProgressDialog.hide();
    } // void

    @Override
    public void listeFromClient(List<String> pListData) {
        //Log.d(TAG, "listeFromClient");
        //fixme: listeFromClient pas utilisé ici pour le moment
        mProgressDialog.hide();
    }

    @Override
    public void quantiteFromClient(List<String> pListData) { // callback d'une action de type GET (LISTE ou QUANTITE)
        //Log.d(TAG, "quantiteFromClient");
        //fixme: quantiteFromClient pas utilisé ici pour le moment
        mProgressDialog.hide();
    } // void

    // callback Client: data
    //******************************************************************************


    public void toastMessage(final String pMessage) {
        Toast.makeText(getApplicationContext(), pMessage, Toast.LENGTH_SHORT).show();
    }

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
