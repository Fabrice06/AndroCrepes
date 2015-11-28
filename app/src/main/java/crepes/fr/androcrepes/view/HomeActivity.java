package crepes.fr.androcrepes.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.List;

import crepes.fr.androcrepes.R;
import crepes.fr.androcrepes.commons.framework.TemplateActivity;
import crepes.fr.androcrepes.commons.network.Client;

/**
 * <b>Classe dédiée à la description de l'ihm Home.</b>
 * <p>
 *     Cette ihm constitue l'accueil de l'application.
 * </p>
 */
public class HomeActivity
        extends TemplateActivity {

    private static final int RESULT_SETTINGS = 1;

    private SharedPreferences mSharedPreferences;

    private Button mBtnHomeSalle;
    private Button mBtnHomeCuisine;
    private Button mBtnHomeLog;

    /**
     * Implémentation de la méthode abstraite issue de la super classe TemplateActivity
     *
     * @see TemplateActivity
     *
     * @return
     *      L'identifiant de l'activity de type int
     */
    protected int getLayoutId() {
        return R.layout.activity_home;
    } // int

    /**
     * Implémentation de la méthode abstraite issue de la super classe TemplateActivity
     *
     * @see TemplateActivity
     *
     * @return
     *      L'identifiant du titre de l'activity de type int
     */
    protected int getTextViewTitleId() {
        return R.id.home_customTextViewTitle;
    } // int

    /**
     * Méthode appelée à la création de l\'activité Home
     * <p>
     *     L'exécution de cette méthode se déroule en 4 phases:
     *     <ul>
     *         <li>appel de la méthode onCreate() sur la super classe TemplateActivity,</li>
     *         <li>éléments présents dans le layout XML initialisés,</li>
     *         <li>mise à jour de l'affichage des boutons de navigation,</li>
     *         <li>création d'une connexion avec le serveur avec TemplateActivity.connectClient().</li>
     *     </ul>
     * </p>
     *
     * @see TemplateActivity
     *
     * @param pSavedInstanceState
     *      Objet de type Bundle contenant l’état de sauvegarde enregistré lors de la dernière exécution de cette activité.
     */
    @Override
    protected void onCreate(Bundle pSavedInstanceState) {
        // les lignes commmentées suivantes sont dédiées à des manipulations de test
        //mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //SharedPreferences.Editor nEditor = mSharedPreferences.edit();
        //nEditor.clear();
        //nEditor.commit();
        super.onCreate(pSavedInstanceState);


        mBtnHomeSalle = (Button) findViewById(R.id.home_buttonSalle);
        mBtnHomeCuisine = (Button) findViewById(R.id.home_buttonCuisine);
        mBtnHomeLog = (Button) findViewById(R.id.home_buttonLog);

        this.updateButtonsAfterConnection(false);

        super.connectClient();
    } // void

    /**
     * Evènement associé au bouton btnHomeCuisine pour naviguer vers l'ihm Cuisine
     *
     * @param pView
     *      Objet de type View
     *
     * @see startSelectedActivity()
     */
    public void goCuisine(View pView) {
        //super.createLogD("goCuisine");
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
        //super.createLogD("goSalle");
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
        //super.createLogD("goAide");
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
     * Implémentation de l\'interface ClientCallback: la connection est établie.
     *
     * @see Client
     * @see updateAfterConnection()
     */
    @Override
    public void connectedFromClient() {
        //super.createLogD("connectedFromClient");

        super.hideProgressDialog();
        updateButtonsAfterConnection(true);
    } // void

    /**
     * Implémentation de l\'interface ClientCallback: la déconnection est effective.
     *
     * @see Client
     * @see updateAfterConnection()
     */
    @Override
    public void disconnectedFromClient() {
        super.createLogD("disconnectedFromClient");

        super.hideProgressDialog();
        updateButtonsAfterConnection(false);
    } // void

    /**
     * Implémentation de l\'interface ClientCallback: une erreur est transmise.
     *
     * @param pError
     *      Message d'erreur à afficher de type String
     *
     * @see Client
     * @see updateAfterConnection()
     */
    @Override
    public void errorFromClient(String pError) {
        //super.createLogD("errorFromClient");

        String nMessage = pError.isEmpty() ? getString(R.string.activity_toastMessageNoConnection) : pError;

        updateButtonsAfterConnection(false);
        super.toastMessage(nMessage, true);
    } // void

    // callback Client: connexion
    //******************************************************************************


    //******************************************************************************
    // callback Client: data

    /**
     * Implémentation de l\'interface ClientCallback: réponse reçue du serveur suite à une requête AJOUT ou COMMANDE.
     *
     * @param pResponseFromServer
     *      Réponse serveur de type String
     */
    public void singleFromClient(final String pResponseFromServer) {
        //super.createLogD("singleFromClient");
        super.hideProgressDialog();
    }

    /**
     * Implémentation de l\'interface ClientCallback: données sont reçues du serveur suite à une requête QUANTITE.
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
     * Réalise la mise à jour de l'affichage des boutons de navigation
     *
     * @param pIsConnected
     *      Vrai: la connexion est établie et active.
     */
    private void updateButtonsAfterConnection(final boolean pIsConnected) {
        mBtnHomeLog.setText(pIsConnected ? R.string.home_buttonLog_logout : R.string.home_buttonLog_logon);
        mBtnHomeSalle.setEnabled(pIsConnected);
        mBtnHomeCuisine.setEnabled(pIsConnected);
    } // void

    /**
     * Méthode appelée à la création du menu Préférences
     * <p>
     *     Cette méthode permets de charger le contenu du fichier menu_main.xml.
     * </p>
     *
     * @param pMenu
     *      Objet de type Menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu pMenu) {
        //super.createLogD("onCreateOptionMenu");
        getMenuInflater().inflate(R.menu.menu_main, pMenu);
        return true;
    }

    /**
     * Méthode appelée à la sélection d'un élément des Préférences
     * <p>
     *     Cette méthode permets d\'initier le lancement de l'activity associée.
     * </p>
     *
     * @param pItem
     *      Objet de type MenuItem
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem pItem) {
        //super.createLogD("onOptionsItemSelected");

        boolean nReturn = true;

        switch (pItem.getItemId()) {

            case R.id.menu_settings:
                Intent nIntent = new Intent(this, SettingActivity.class);
                startActivityForResult(nIntent, RESULT_SETTINGS);
                nReturn = true;
                break;

            default:
                nReturn = super.onOptionsItemSelected(pItem);

        } // switch

        return nReturn;
    }

    /**
     * Réalise la mise à jour de la connexion serveur en fonction des données issues du SharedPreferences
     * <p>
     *     L'exécution de cette méthode se déroule en 3 phases:
     *     <ul>
     *         <li>stockage des valeurs initiales,</li>
     *         <li>mise à jour des données issues du SharedPreferences,</li>
     *         <li>fin de la connexion serveur si les valeurs initiales ont été modifiées..</li>
     *     </ul>
     * </p>
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //super.createLogD("onActivityResult");

        switch (requestCode) {
            case RESULT_SETTINGS:

                String nIp = super.getController().getServerIp();
                int nPort = super.getController().getServerPort();

                this.updatePreference();

                if (!(nIp.equals(super.getController().getServerIp()) && (nPort == super.getController().getServerPort()))) {
                    super.setNewInstanceClient();
                }
                break;

        } // switch
    }

    /**
     * Réalise la mise à jour des données issues du SharedPreferences
     * <p>
     *     Le menu préférence propose de modifier les valeurs suivantes:
     *     <ul>
     *         <li>l\'adresse IP du serveur distant,</li>
     *         <li>le port d\'écoute du serveur distant.</li>
     *     </ul>
     * </p>
     */
    protected void updatePreference() {
        if (mSharedPreferences == null) {
            mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        }

        String nIp = mSharedPreferences.getString("ip", super.getController().SERVER_IP);
        super.getController().setServerIp(nIp);

        String nPort = mSharedPreferences.getString("port", String.valueOf(super.getController().SERVER_PORT));
        super.getController().setServerPort(Integer.valueOf(nPort));
    }
} // class
