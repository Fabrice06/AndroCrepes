package crepes.fr.androcrepes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import crepes.fr.androcrepes.network.Client;

public class HomeActivity extends AppCompatActivity implements Client.ClientCallBack {

    private static final String TAG = HomeActivity.class.getSimpleName();

    //fixme: deux variables suivantes à changer via le menu settings

    public final static String EXTRA_ACTION = "EXTRA_ACTION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

    } // void

    // event associé au bouton btnHomeCuisine
    public void goCuisine(View v) {
        Log.d(TAG, "goCuisine");
        Intent intent = new Intent(this, CuisineActivity.class);
        startSelectedActivity(intent);
        //startMainActivity("cuisine");
    }

    // event associé au bouton btnHomeSalle
    public void goSalle(View v) {
        Log.d(TAG, "goSalle");
        Intent intent = new Intent(this, SalleActivity.class);
        startSelectedActivity(intent);
        //startMainActivity("salle");
    }
    
    public void goAide(View view) {
        Log.d(TAG, "goAide");
        Intent intent = new Intent(this, AideActivity.class);
        startSelectedActivity(intent);
    }

    // event associé au bouton btnLogout
    public void goLogout(View v) {
        Log.d(TAG, "goLogout");
        finish();
    } // void

    // démarre l'activity SalleActivity
    //fixme mettre une enum à la plase du paramètre String
    private void startMainActivity(final String pAction) {
        Log.d(TAG, "startMainActivity");
        Intent intent = new Intent(this, SalleActivity.class);
        intent.putExtra(EXTRA_ACTION, pAction);
        startActivity(intent);
    }

    private void startSelectedActivity(Intent intent){
        Log.d(TAG, "startSelectedActivity");
        startActivity(intent);
    }

    @Override
    public void connectedFromClient() { // callback d'une action de type PUT, POST ou DELETE
        //mClient.send(EnumSendWord.QUANTITE, "");
        Log.d(TAG, "connectedFromClient");
    }

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
