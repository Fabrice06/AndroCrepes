package crepes.fr.androcrepes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.List;

import crepes.fr.androcrepes.commons.EnumSendWord;
import crepes.fr.androcrepes.entity.Plat;
import crepes.fr.androcrepes.entity.Plats;
import crepes.fr.androcrepes.network.Client;

public class CuisineActivity extends AppCompatActivity implements Client.ClientCallBack {

    private static final String TAG = CuisineActivity.class.getSimpleName();

    //private static final String SERVER_IP = "10.0.3.2";
    private static final String SERVER_IP = "10.0.2.2";
    private static final int SERVER_PORT = 7777;

    private ListView mListViewMain = null;
    private ListAdapter mListAdapter;

    private Client mClient;
    private Plats mPlats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuisine);

        mListViewMain = (ListView) findViewById(R.id.listMain);
        mPlats = Plats.getInstance();
        mListAdapter = new ListAdapter(this, mPlats);
        mListViewMain.setAdapter(mListAdapter);

        mClient = new Client(this, SERVER_IP, SERVER_PORT);
        mClient.connect();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cuisine, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected");
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

    public void goDeb(View view) {
        Log.d(TAG, "goDeb");
        finish();
    }

    //    @Override
    public void addFromListAdapter(Plat pPlat) {

//        mClient.send(EnumSendWord.AJOUT, "1 " + pPlat.getNom());

        Log.d(TAG, "addFromListePlat callback");
    } // void


    //    @Override
    public void removeFromListAdapter(Plat pPlat) {

//        mClient.send(EnumSendWord.COMMANDE, pPlat.getNom());

        Log.d(TAG, "removeFromListePlat callback");
    }

    @Override
    public void connectedFromClient() { // callback d'une action de type PUT, POST ou DELETE
        Log.d(TAG, "connectedFromClient callback");
        mClient.send(EnumSendWord.QUANTITE, "");
    }

    @Override
    public void singleFromClient(final String pString) { // callback d'une action de type PUT, POST ou DELETE
        Log.d(TAG, "singleFromClient callback: " + pString);
    } // void

    @Override
    public void listeFromClient(List<String> pListData) {
        Log.d(TAG, "quantiteFromClient callback");
//fixme: pas utilisé pour le moment pas toucher
    }

    @Override
    public void quantiteFromClient(List<String> pListData) { // callback d'une action de type GET (LISTE ou QUANTITE)
        Log.d(TAG, "quantiteFromClient callback");
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
        Log.i("SalleActivity", "onRestart");
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        mClient.logout();
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
}
