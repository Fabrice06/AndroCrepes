package crepes.fr.androcrepes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import crepes.fr.androcrepes.commons.EnumReceiveWord;
import crepes.fr.androcrepes.commons.EnumSendWord;
import crepes.fr.androcrepes.commons.Tools;
import crepes.fr.androcrepes.entity.Plat;
import crepes.fr.androcrepes.entity.Plats;
import crepes.fr.androcrepes.network.Client;

public class CuisineActivity
        extends AppCompatActivity
        implements Client.ClientCallBack, ListAdapter.ListAdapterCallBack {

    private static final String TAG = CuisineActivity.class.getSimpleName();

    private ListView mListViewCuisine = null;
    private ListAdapter mListAdapter;

    private EditText mEditTextQte;
    private EditText mEditTextName;

    private Client mClient;
    private Plats mPlats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuisine);
        //Log.d(TAG, "onCreate");

        mPlats = Plats.getInstance();
        mListAdapter = new ListAdapter(this, mPlats);

        mListViewCuisine = (ListView) findViewById(R.id.listViewCuisine);
        mListViewCuisine.setAdapter(mListAdapter);

        mEditTextQte =(EditText) findViewById(R.id.editTextQte);
        mEditTextName =(EditText) findViewById(R.id.editTextPlat);

        //fixme: définir plan B si serveur hors d'atteinte
        mClient = Client.getInstance(this, HomeActivity.SERVER_IP, HomeActivity.SERVER_PORT);
        mClient.connect();
        mClient.send(EnumSendWord.QUANTITE, "");

    } // void

    //******************************************************************************
    // callback Client: connexion

    @Override
    public void connectedFromClient() { // callback d'une connexion client si réussite
        Log.d(TAG, "connectedFromClient callback");
        mClient.send(EnumSendWord.QUANTITE, "");
    }

    @Override
    public void disconnectedFromClient() { // callback d'une déconnexion client
        Log.d(TAG, "disconnectedFromClient");
        //fixme: prévenir l'utilisateur ??
    }

    @Override
    public void errorFromClient(String pError) { // callback d'une connexion client si réussite
        Log.d(TAG, "errorFromClient");
        //fixme: prévenir l'utilisateur
        toastErrorMessage(pError);
    }

    // callback Client: connexion
    //******************************************************************************


    //******************************************************************************
    // callback Client: data

    @Override
    public void singleFromClient(final String pString) { // callback d'une action de type PUT, POST ou DELETE
        Log.d(TAG, "singleFromClient callback: " + pString);

        // recherche du dernier mot/chiffre pour identifier la réponse
        String nReponse = pString.substring(pString.lastIndexOf(" ")+1);

        if (nReponse.equals(EnumReceiveWord.EPUISE.getValue()) || (nReponse.equals(EnumReceiveWord.INCONNU.getValue()))) {
            // échec d'une commande ('épuisé' ou 'inconnu' trouvé en fin de message)
            toastErrorMessage(pString + " !");

        } else if (nReponse.equals(EnumReceiveWord.COMMANDE.getValue()) || Tools.isInteger(nReponse)) {

        } else {
            // cas non répertorié: ceinture et bretelles
            toastErrorMessage(pString + "Erreur inconnue: merci de prévenir l'administrateur !");
            //fixme: mettre en place un fichier de log pour ce cas ??
        } // else
    } // void


    @Override
    public void listeFromClient(List<String> pListData) {
        //Log.d(TAG, "quantiteFromClient callback");

        //fixme: pas utilisé pour le moment pas toucher
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
                nPlat= new Plat(nNom, nQuantite);
                mPlats.addPlat(nPlat);

            } else { // update quantité
                nPlat.setQuantite(nQuantite);
            } // else

            //Log.d(TAG, "quantiteFromClient for item " + nNom + " " + nQuantite);
        } // for

        // maj de l'ihm
        mListAdapter.notifyDataSetChanged();
    } // void

    // callback client: data
    //******************************************************************************


    //******************************************************************************
    // callback listAdapter

    @Override
    public void addFromListAdapter(Plat pPlat) {
        Log.d(TAG, "addFromListAdapter callback");

        mClient.send(EnumSendWord.AJOUT, "1 " + pPlat.getNom());
    } // void

    @Override
    public void removeFromListAdapter(Plat pPlat) {
        Log.d(TAG, "removeFromListAdapter callback");

        mClient.send(EnumSendWord.COMMANDE, pPlat.getNom());
    } // void

    //    public void addFromListAdapterWithQuantity(int qte, String pPlat) {
//
////        mClient.send(EnumSendWord.AJOUT, qte + " " + pPlat);
//        // maj de l'ihm
//        mListAdapter.notifyDataSetChanged();
//
//        Log.d(TAG, "addFromListePlatWithQuantity callback");
//    } // void

    // callback listAdapter
    //******************************************************************************


    public void toastErrorMessage(String pError) {
        Toast.makeText(getApplicationContext(), pError, Toast.LENGTH_SHORT).show();
    }

    // event associé à imageButtonCuisineGoHome
    public void goHome(View pView) {
        finish();
    } // void

    // event associé au bouton roundedbuttonplus
    public void addNewDish(View view) {
        String qteString = mEditTextQte.getText().toString();
        int qte;

        if (qteString.length() != 0) {

            qte = Integer.parseInt(qteString);
            String nomPlat = mEditTextName.getText().toString();


            Log.d(TAG, "addNewDish " + qte + " " + nomPlat);

//        if ((null != nomPlat) || ("" != nomPlat) || "plat" != nomPlat) {

            if (nomPlat.length() != 0) {
                if (1 >= qte) {
                    qte = 1;
                }

//                addFromListAdapterWithQuantity(qte, nomPlat);

            } else {
                Toast.makeText(getApplicationContext(), "Renseigner le nom du plat pour valider", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "Renseigner la quantité pour valider", Toast.LENGTH_SHORT).show();
        }


        connectedFromClient();
    } // void


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Log.d(TAG, "onCreateOptionsMenu");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cuisine, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Log.d(TAG, "onOptionsItemSelected");
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
