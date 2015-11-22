package crepes.fr.androcrepes.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import crepes.fr.androcrepes.R;
import crepes.fr.androcrepes.commons.framework.CustomSalleListAdapter;
import crepes.fr.androcrepes.commons.framework.CustomTextView;
import crepes.fr.androcrepes.commons.java.EnumSendWord;
import crepes.fr.androcrepes.controller.Controller;
import crepes.fr.androcrepes.model.Commande;
import crepes.fr.androcrepes.model.Commandes;
import crepes.fr.androcrepes.model.Plat;

public class SalleActivity
        extends AppCompatActivity
        implements CustomSalleListAdapter.SalleListAdapterCallBack {

    private static final String TAG = SalleActivity.class.getSimpleName();

    private ProgressDialog mProgressDialog = null;

    private ListView mListView = null;
    private CustomSalleListAdapter mListAdapter;

    private Commandes mCommandes;

    private TextView mEditTextTotal;
    private TextView mEditTextCommande;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salle);
        Log.d(TAG, "onCreate");

        //Get Global Controller Class object (see application tag in AndroidManifest.xml)
        final Controller nController = (Controller) getApplicationContext();

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.activity_progressDialogWait));
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);

        CustomTextView nCustomTextView = (CustomTextView) findViewById(R.id.salle_customTextViewTitle);
        mEditTextTotal = (TextView) findViewById(R.id.salle_textViewTotal);
        mEditTextCommande = (TextView) findViewById(R.id.salle_textViewCommande);

        mCommandes = nController.getCommandes();
        mListAdapter = new CustomSalleListAdapter(this, mCommandes);

        mListView = (ListView) findViewById(R.id.salle_listView);
        mListView.setAdapter(mListAdapter);

        updateHeaderInfo();

    } // void

    //******************************************************************************
    // callback listAdapter

    /**
     * Implémentation de ListAdapterCallBack: clic sur bouton gauche.
     * <p>
     *     Une requête AJOUT est envoyée au serveur distant.
     * </p>
     *
     * @param pPlat
     *      Objet de type Plat
     *
     * @see Plat
     * @see EnumSendWord
     */
    @Override
    public void clicLeftFromListAdapter(Commande pCommande) {
        Log.d(TAG, "clicLeftFromListAdapter callback");


        // fixme remettre en stock les crepes de la commande

        // enlever la commande de la liste
        mCommandes.remove(pCommande);

        // maj de l'ihm
        updateHeaderInfo();
        mListAdapter.notifyDataSetChanged();
    } // void

    /**
     * Implémentation de ListAdapterCallBack: clic sur bouton droit.
     * <p>
     *     Une requête COMMANDE est envoyée au serveur distant.
     * </p>
     *
     * @param pPlat
     *      Objet de type Plat
     *
     * @see Plat
     * @see EnumSendWord
     */
    @Override
    public void clicRightFromListAdapter(Commande pCommande) {
        Log.d(TAG, "clicRightFromListAdapter callback");

        //fixme: passer la commande à l'intent voir cour
        startSelectedActivity(TableActivity.class);
    } // void

    // callback listAdapter
    //******************************************************************************

    private void toastMessage(final String pMessage, final boolean pHideProgressDialog) {

        if (pHideProgressDialog) {
            mProgressDialog.hide();
        } // if

        Toast.makeText(getApplicationContext(), pMessage, Toast.LENGTH_SHORT).show();
    } // void

    private void updateHeaderInfo() {

        switch (mCommandes.size()) {
            case 0:
                mEditTextTotal.setText("");
                mEditTextCommande.setText(R.string.salle_textViewCommande_none);
                break;

            case 1:
                mEditTextTotal.setText(R.string.salle_textViewTotal_one);
                mEditTextCommande.setText(R.string.salle_textViewCommande_one);
                break;

            default:
                mEditTextTotal.setText(mCommandes.getValueOfSize());
                mEditTextCommande.setText(R.string.salle_textViewCommande_more);
        } // switch
    } // void

    public void goHome(View pView) {
        finish();
    } // void

    public void clearAll(View pView) {
        Log.d(TAG, "clearAll");

        //fixme message attention !

        //enlever toutes les commandes de la liste
        mCommandes.clear();

        // maj de l'ihm
        updateHeaderInfo();
        mListAdapter.notifyDataSetChanged();
    } // void

    public void addCommande(View pView) {
        Log.d(TAG, "addCommande");

        //ajouter une commande dans de la liste
        Commande nCommande = new Commande();
        mCommandes.add(nCommande);

        // maj de l'ihm
        updateHeaderInfo();
        mListAdapter.notifyDataSetChanged();

        //fixme: passer la commande à l'intent voir cour
        startSelectedActivity(TableActivity.class);
    } // void

    private void startSelectedActivity(final Class pClass) {
        Log.d(TAG, "startSelectedActivity");
        Intent nIntent = new Intent(this, pClass);
        startActivity(nIntent);
    } // void

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
