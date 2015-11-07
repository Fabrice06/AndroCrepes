package crepes.fr.androcrepes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import crepes.fr.androcrepes.commons.EnumReceiveWord;
import crepes.fr.androcrepes.commons.Tools;
import crepes.fr.androcrepes.entity.Plat;
import crepes.fr.androcrepes.entity.Plats;
import crepes.fr.androcrepes.network.Client;

public class SalleActivity extends AppCompatActivity implements Client.ClientCallBack {

    private static final String TAG = SalleActivity.class.getSimpleName();

    private ListView mListViewSalle = null;
    private ListAdapter mListAdapter;

    private Client mClient;
    private Plats mPlats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salle);

        mListViewSalle = (ListView) findViewById(R.id.listSalle);
        mPlats = Plats.getInstance();
        mListAdapter = new ListAdapter(this, mPlats);
        mListViewSalle.setAdapter(mListAdapter);

        //fixme: définir plan B si serveur hors d'atteinte
        mClient = Client.getInstance(this, HomeActivity.SERVER_IP, HomeActivity.SERVER_PORT);

    } // void

    @Override
    public void connectedFromClient() { // callback d'une connexion client si réussite
        Log.d(TAG, "connectedFromClient callback");
        //mClient.send(EnumSendWord.QUANTITE, "");
    }

    @Override
    public void disconnectedFromClient() { // callback d'une déconnexion
        Log.d(TAG, "notConnectedFromClient");
        //fixme: prévenir l'utilisateur
    }

    @Override
    public void errorFromClient(String pError) { // callback d'une connexion client si réussite
        Log.d(TAG, "errorFromClient");
        //fixme: prévenir l'utilisateur
    }

    @Override
    public void singleFromClient(final String pString) { // callback d'une action de type PUT, POST ou DELETE

        Log.d(TAG, "singleFromClient callback: " + pString);
        // recherche du dernier mot/chiffre pour identifier la réponse
        String nReponse = pString.substring(pString.lastIndexOf(" ")+1);

        if (nReponse.equals(EnumReceiveWord.EPUISE.getValue()) || (nReponse.equals(EnumReceiveWord.INCONNU.getValue()))) {
        // échec d'une commande ('épuisé' ou 'inconnu' trouvé en fin de message)
            Toast.makeText(getApplicationContext(), pString, Toast.LENGTH_SHORT).show();
//fixme: afficher popup message: pString + " !"

        } else if (nReponse.equals(EnumReceiveWord.COMMANDE.getValue()) || Tools.isInteger(nReponse)) {

        } else {
            // cas non répertorié: ceinture et bretelles
        //fixme: afficher popup message: prévenir l'administrateur
        }
    } // void


    @Override
    public void listeFromClient(List<String> pListData) {
        Log.d(TAG, "listeFromClient callback");
//fixme: pas utilisé pour le moment pas toucher
        for (int nLen = pListData.size(), i = 1; i < nLen; i++) {
            String nPlat = pListData.get(i);

            Log.d(TAG, "listeFromClient for item " + i + " : " + nPlat);
        } // for
    }


    @Override
    public void quantiteFromClient(List<String> pListData) { // callback d'une action de type GET (LISTE ou QUANTITE)
        Log.d(TAG, "quantiteFromClient callback");

        for (int nLen = pListData.size(), i = 1; i < (nLen-1); i+=2) {
            String nNom = pListData.get(i);
            int nQuantite = Integer.parseInt(pListData.get(i + 1));

            Plat nPlat = mPlats.getPlat(nNom);
            // nouveau plat
            if (null == nPlat) {
                Log.d(TAG, "quantiteFromClient callback if plat ");
                nPlat= new Plat(nNom, nQuantite);
                mPlats.addPlat(nPlat);

            } else { // update quantité
                Log.d(TAG, "quantiteFromClient callback esle plat ");
                nPlat.setQuantite(nQuantite);
            }

            // maj de l'ihm
            //mListAdapter.notifyDataSetChanged();

            Log.d(TAG, "quantiteFromClient for item " + nNom + " " + nQuantite);
        }

        // maj de l'ihm
        mListAdapter.notifyDataSetChanged();

    } // void


//    @Override
    public void addFromListAdapter(Plat pPlat) {

        Log.d(TAG, "addFromListePlat callback");
    } // void


//    @Override
    public void removeFromListAdapter(Plat pPlat) {

        Log.d(TAG, "removeFromListePlat callback");
    }


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
        mClient.disconnect();
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

//    public void goHome(View view) {
//        finish();
//    }
} // class
