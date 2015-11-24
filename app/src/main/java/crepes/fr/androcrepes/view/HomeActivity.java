package crepes.fr.androcrepes.view;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import crepes.fr.androcrepes.R;
import crepes.fr.androcrepes.commons.framework.CustomTextView;
import crepes.fr.androcrepes.commons.framework.TemplateActivity;
import crepes.fr.androcrepes.commons.network.Client;
import crepes.fr.androcrepes.controller.Controller;
import crepes.fr.androcrepes.controller.SettingsFragment;

/**
 * <b>Classe dédiée à la description de l'ihm Home.</b>
 * <p>
 *     Cette ihm constitue l'accueil de l'application.
 * </p>
 */
public class HomeActivity
        extends TemplateActivity {

    private static SettingsFragment mSettingsFragment = new SettingsFragment();
    public static FragmentManager mFragmentManager;
    private SharedPreferences mSharedPreferences;

    private Button mBtnHomeSalle = null;
    private Button mBtnHomeCuisine = null;
    private Button mBtnHomeLog = null;

    protected int getLayoutId() {
        return R.layout.activity_home;
    } // int

    protected int getTextViewTitleId() {
        return R.id.home_customTextViewTitle;
    } // int


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBtnHomeSalle = (Button) findViewById(R.id.home_buttonSalle);
        mBtnHomeCuisine = (Button) findViewById(R.id.home_buttonCuisine);
        mBtnHomeLog = (Button) findViewById(R.id.home_buttonLog);

        this.updateButtonsAfterConnection(false);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mFragmentManager = getFragmentManager();

        // La première fois que l'application est lancée, on lit les préférences par défaut du
        // fichier XML
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        super.connectClient();

    } // void


    /**
     * Evènement associé au bouton btnHomeCuisine pour naviguer vers l'ihm Cuisine
     *
     * @param pView
     *      Objet de type View
     *
     * @see startSelectedActivity
     */
    public void goCuisine(View pView) {
        super.createLogD("goCuisine");
        super.startSelectedActivity(CuisineActivity.class);
    }

    /**
     * Evènement associé au bouton btnHomeSalle pour naviguer vers l'ihm Salle
     *
     * @param pView
     *      Objet de type View
     *
     * @see startSelectedActivity
     */
    public void goSalle(View pView) {
        super.createLogD("goSalle");
        super.startSelectedActivity(SalleActivity.class);
    }

    /**
     * Evènement associé au bouton btnHomeAide pour naviguer vers l'ihm Aide
     *
     * @param pView
     *      Objet de type View
     *
     * @see startSelectedActivity
     */
    public void goAide(View pView) {
        super.createLogD("goAide");
        super.startSelectedActivity(AideActivity.class);
    }

    /**
     * Evènement associé au bouton btnHomeLog pour gérer la connexion au serveur distant
     *
     * @param pView
     *      Objet de type View
     */
    public void goLog(View pView) {
        super.toggleClient();

    } // void


    //******************************************************************************
    // callback Client: connexion

    /**
     * Implémentation de ClientCallback: la connection est établie.
     *
     * @see Client
     * @see updateAfterConnection
     */
    @Override
    public void connectedFromClient() {
        super.createLogD("connectedFromClient");

        super.hideProgressDialog();
        updateButtonsAfterConnection(true);
    } // void

    /**
     * Implémentation de ClientCallback: la déconnection est effective.
     *
     * @see Client
     * @see updateAfterConnection
     */
    @Override
    public void disconnectedFromClient() {
        super.createLogD("disconnectedFromClient");

        super.hideProgressDialog();
        updateButtonsAfterConnection(false);
    } // void

    /**
     * Implémentation de ClientCallback: une erreur est transmise.
     *
     * @param pError
     *      Message d'erreur à afficher de type String
     *
     * @see Client
     * @see updateAfterConnection
     * @see toastMessage
     */
    @Override
    public void errorFromClient(String pError) {
        //super.createLogD("errorFromClient");

        //fixme pError pas utilisé
        String nMessage = getString(R.string.activity_toastMessageNoConnection);

        updateButtonsAfterConnection(false);
        super.toastMessage(nMessage, true);
    } // void

    // callback Client: connexion
    //******************************************************************************


    //******************************************************************************
    // callback Client: data

    public void singleFromClient(final String pResponseFromServer) {
        //super.createLogD("singleFromClient");
        super.hideProgressDialog();
    }

    /**
     * Implémentation de ClientCallback: données sont reçues du serveur suite à une requête QUANTITE.
     *
     * @param pListData
     *      Données sous forme d'une collection de String.
     */
    @Override
    public void quantiteFromClient(List<String> pListData) {
        //super.createLogD("quantiteFromClient");
        super.hideProgressDialog();
    } // void

    // callback Client: data
    //******************************************************************************


    /**
     * Réalise la mise à jour de l'affichage
     *
     * @param pIsConnected
     *      Vrai: la connexion est établie et active.
     */
    private void updateButtonsAfterConnection(final boolean pIsConnected) {
        mBtnHomeLog.setText(pIsConnected ? R.string.home_buttonLog_logout : R.string.home_buttonLog_logon);
        mBtnHomeSalle.setEnabled(pIsConnected);
        mBtnHomeCuisine.setEnabled(pIsConnected);
    } // void

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //super.createLogD("onCreateOptionMenu");
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.createLogD("********  onOptionsItemSelected");
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            super.createLogD(" IF ");
            FragmentTransaction transaction = mFragmentManager.beginTransaction();

            if (!mSettingsFragment.isAdded()) {

                transaction.add(R.id.fragmentSettings, mSettingsFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }



 //           fragmentManager.executePendingTransactions();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        super.createLogD("onBackPressed");

        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        if (mSettingsFragment.isAdded()) {

//            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
//            transaction.remove(frag);
            mFragmentManager.popBackStack();
        } else {

            super.onBackPressed();
        }
        transaction.commit();
    }

    //******************************************************************************
    // cycle de vie activity

//    lancement appli
//    HomeActivity: onCreate
//    HomeActivity: onStart
//    HomeActivity: onResume

//    home -> salle
//    HomeActivity: onPause
//    TableActivity: onCreate
//    TableActivity: onStart
//    TableActivity: onResume
//    HomeActivity: onStop

//    salle -> home
//    TableActivity: onPause
//    HomeActivity: onRestart
//    HomeActivity: onStart
//    HomeActivity: onResume
//    TableActivity: onStop
//    TableActivity: onDestroy

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//
//        // retour d'une autre activity via finish
//        mClient.setCallBack(this);
//    }

} // class
