package crepes.fr.androcrepes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import crepes.fr.androcrepes.commons.EnumReceiveWord;
import crepes.fr.androcrepes.commons.EnumSendWord;
import crepes.fr.androcrepes.commons.Tools;
import crepes.fr.androcrepes.entity.Plat;
import crepes.fr.androcrepes.entity.Plats;
import crepes.fr.androcrepes.network.Client;

public class SalleActivity
        extends AppCompatActivity
        implements Client.ClientCallBack, ListAdapter.ListAdapterCallBack {

    private static final String TAG = SalleActivity.class.getSimpleName();

    private ListView mListViewSalle = null;
    private ListAdapter mListAdapter;

    private Client mClient;
    private Plats mPlats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salle);
        //Log.d(TAG, "onCreate");

        mPlats = Plats.getInstance();
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

    @Override
    public void connectedFromClient() { // callback d'une connexion client si réussite
        Log.d(TAG, "connectedFromClient callback");
        mClient.send(EnumSendWord.QUANTITE, "");
    }

    @Override
    public void disconnectedFromClient() { // callback d'une déconnexion
        Log.d(TAG, "notConnectedFromClient");
        //fixme: prévenir l'utilisateur ??
    }

    @Override
    public void errorFromClient(String pError) { // callback d'une connexion client si réussite
        Log.d(TAG, "errorFromClient");
        //fixme: prévenir l'utilisateur
        toastErrorMessage(pError);
    }

    // callback client: connexion
    //******************************************************************************


    //******************************************************************************
    // callback client: data

    @Override
    public void singleFromClient(final String pString) { // callback d'une action de type PUT, POST ou DELETE
        Log.d(TAG, "singleFromClient callback: " + pString);

        // recherche du dernier mot/chiffre pour identifier la réponse
        String nReponse = pString.substring(pString.lastIndexOf(" ")+1);

        if (nReponse.equals(EnumReceiveWord.EPUISE.getValue()) || (nReponse.equals(EnumReceiveWord.INCONNU.getValue()))) {
            // échec d'une commande ('épuisé' ou 'inconnu' trouvé en fin de message)
            toastErrorMessage(pString + " !");

        } else if (nReponse.equals(EnumReceiveWord.COMMANDE.getValue()) || Tools.isInteger(nReponse)) {
            mClient.send(EnumSendWord.QUANTITE, "");

        } else {
            // cas non répertorié: ceinture et bretelles
            toastErrorMessage(pString + "Erreur inconnue: merci de prévenir l'administrateur !");
            //fixme: mettre en place un fichier de log pour ce cas ??
        } // else
    } // void


    @Override
    public void listeFromClient(List<String> pListData) {
        Log.d(TAG, "listeFromClient callback");

        //fixme: pas utilisé pour le moment pas toucher

//        for (int nLen = pListData.size(), i = 1; i < nLen; i++) {
//            String nPlat = pListData.get(i);
//
//            Log.d(TAG, "listeFromClient for item " + i + " : " + nPlat);
//        } // for
    } // void

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
                Log.d(TAG, "quantiteFromClient callback if plat ");
                nPlat= new Plat(nNom, nQuantite);
                mPlats.addPlat(nPlat);

            } else { // update quantité
                Log.d(TAG, "quantiteFromClient callback else plat ");
                nPlat.setQuantite(nQuantite);
            } // else

            Log.d(TAG, "quantiteFromClient for item " + nNom + " " + nQuantite);
        } // for

        // maj de l'ihm
        mListAdapter.notifyDataSetChanged();
    } // void

    // callback client: data
    //******************************************************************************


    //******************************************************************************
    // callback listAdapter

    @Override
    public void clicLeftFromListAdapter(Plat pPlat) {
        Log.d(TAG, "clicLeftFromListAdapter callback");

        mClient.send(EnumSendWord.AJOUT, "1 " + pPlat.getNom());
    }

    @Override
    public void clicRightFromListAdapter(Plat pPlat) {
        Log.d(TAG, "clicRightFromListAdapter callback");

        mClient.send(EnumSendWord.COMMANDE, pPlat.getNom());
    }

    // callback listAdapter
    //******************************************************************************


    public void toastErrorMessage(String pError) {
        Toast.makeText(getApplicationContext(), pError, Toast.LENGTH_SHORT).show();
    }

    // event associé à imageButtonSalleGoHome
    public void goHome(View pView) {
        finish();
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
        Log.i("SalleActivity", "onRestart");
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
