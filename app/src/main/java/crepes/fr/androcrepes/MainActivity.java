package crepes.fr.androcrepes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import crepes.fr.androcrepes.commons.EnumReceiveWord;
import crepes.fr.androcrepes.commons.EnumSendWord;
import crepes.fr.androcrepes.commons.Tools;
import crepes.fr.androcrepes.entity.Plat;
import crepes.fr.androcrepes.entity.Plats;
import crepes.fr.androcrepes.network.Client;
import crepes.fr.androcrepes.network.Client.ClientCallBack;

//import crepes.fr.androcrepes.ListAdapter.ListAdapterCallBack;

public class MainActivity extends AppCompatActivity implements ClientCallBack {

    private static final String TAG = MainActivity.class.getSimpleName();

    //fixme: deux variables suivantes à changer via le menu connection
    private static final String SERVER_IP = "10.0.3.2";
//    private static final String SERVER_IP = "10.0.2.2";
    private static final int SERVER_PORT = 7777;


    private ListView mListViewMain = null;
    private ListAdapter mListAdapter;

    private ProgressBar progressBar;
    private int progressStatus = 0;

    private Client mClient;
    private Plats mPlats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListViewMain = (ListView) findViewById(R.id.listMain);
       // mListViewMain.setDivider(null);
        //Pour la séparation entre les differents elements de la listview sans : xml prends la main

        //fixme: HomeActivity.EXTRA_ACTION cuisine ou salle (enum)
        Intent data = getIntent();
        //mAction = data.getStringExtra(HomeActivity.EXTRA_ACTION);


        mPlats = Plats.getInstance();
        mListAdapter = new ListAdapter(this, mPlats);
        mListViewMain.setAdapter(mListAdapter);

        // lance une connexion au serveur
        //fixme: définir plan B si serveur hors d'atteinte
        mClient = new Client(this, SERVER_IP, SERVER_PORT);
        mClient.connect();

    } // void

    @Override
    public void connectedFromClient() { // callback d'une action de type PUT, POST ou DELETE
        // par choix, on décide de mettre à jour l'ihm via la commande QUANTITE
        mClient.send(EnumSendWord.QUANTITE, "");
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
            // commande réalisée avec succés ('commandé' trouvé en fin de message)
            // ou ajout réalisé avec succés (valeur numérique trouvée en fin de message)
            // mise à jour de l'information
            mClient.send(EnumSendWord.QUANTITE, "");

        } else {
            // cas non répertorié: ceinture et bretelles
        //fixme: afficher popup message: prévenir l'administrateur
        }
    } // void


    @Override
    public void listeFromClient(List<String> pListData) {
//fixme: pas utilisé pour le moment pas toucher
        for (int nLen = pListData.size(), i = 1; i < nLen; i++) {
            String nPlat = pListData.get(i);

            Log.d(TAG, "listeFromClient for item " + i + " : " + nPlat);
        } // for
    }


    @Override
    public void quantiteFromClient(List<String> pListData) { // callback d'une action de type GET (LISTE ou QUANTITE)

        for (int nLen = pListData.size(), i = 1; i < (nLen-1); i+=2) {
            String nNom = pListData.get(i);
            int nQuantite = Integer.parseInt(pListData.get(i + 1));

            Plat nPlat = mPlats.getPlat(nNom);

            // nouveau plat
            if (null == nPlat) {
                nPlat= new Plat(mPlats.getSize(), nNom, nQuantite);
                mPlats.addPlat(nPlat);

            } else { // update quantité
                nPlat.setQuantite(nQuantite);
            }

            // maj de l'ihm
            mListAdapter.notifyDataSetChanged();

            Log.d(TAG, "quantiteFromClient for item " + i + " : " + nNom + " is " + nQuantite);
        } // for
    } // void


//    @Override
    public void addFromListAdapter(Plat pPlat) {

        mClient.send(EnumSendWord.AJOUT, "1 " + pPlat.getNom());

        Log.d(TAG, "addFromListePlat callback");
    } // void


//    @Override
    public void removeFromListAdapter(Plat pPlat) {

        mClient.send(EnumSendWord.COMMANDE, pPlat.getNom());

        Log.d(TAG, "removeFromListePlat callback");
    }


    // event associé au bouton btnLogout
    public void onLogout(View v) {

        mClient.logout();
            finish();
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
        Log.i("MainActivity", "onRestart");
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

    public void goHome(View view) {
        finish();
    }
} // class
