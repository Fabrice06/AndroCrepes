package crepes.fr.androcrepes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.List;

import crepes.fr.androcrepes.commons.EnumSendWord;
import crepes.fr.androcrepes.entity.Plats;
import crepes.fr.androcrepes.network.Client;

public class CuisineActivity extends AppCompatActivity implements Client.ClientCallBack {

    private static final String TAG = CuisineActivity.class.getSimpleName();

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

        //fixme: définir plan B si serveur hors d'atteinte
        mClient = Client.getInstance(this, HomeActivity.SERVER_IP, HomeActivity.SERVER_PORT);
        // par choix, on décide de mettre à jour l'ihm via la commande QUANTITE
        mClient.send(EnumSendWord.QUANTITE, "");
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

    @Override
    public void connectedFromClient() { // callback d'une action de type PUT, POST ou DELETE
    }

    @Override
    public void singleFromClient(final String pString) { // callback d'une action de type PUT, POST ou DELETE

    } // void

    @Override
    public void listeFromClient(List<String> pListData) {
//fixme: pas utilisé pour le moment pas toucher
    }

    @Override
    public void quantiteFromClient(List<String> pListData) { // callback d'une action de type GET (LISTE ou QUANTITE)

    } // void
}
