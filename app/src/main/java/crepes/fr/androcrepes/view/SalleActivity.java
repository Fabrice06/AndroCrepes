package crepes.fr.androcrepes.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Iterator;
import java.util.List;

import crepes.fr.androcrepes.R;
import crepes.fr.androcrepes.commons.framework.CustomListActivity;
import crepes.fr.androcrepes.commons.framework.CustomSalleListAdapter;
import crepes.fr.androcrepes.commons.framework.TemplateActivity;
import crepes.fr.androcrepes.commons.java.EnumSendWord;
import crepes.fr.androcrepes.commons.network.Client;
import crepes.fr.androcrepes.model.Commande;
import crepes.fr.androcrepes.model.Commandes;
import crepes.fr.androcrepes.model.Plat;

/**
 * <b>Classe dédiée à la description de l'ihm Salle.</b>
 */
public class SalleActivity
        extends TemplateActivity
        implements CustomSalleListAdapter.SalleListAdapterCallBack {

    private ListView mListView;
    private CustomSalleListAdapter mListAdapter;

    private Commandes mCommandes;

    private TextView mEditTextTotal;
    private TextView mEditTextCommande;
    private Button mButtonClearAll;

    private int mTotalPlat = 0;

    /**
     * Implémentation de la méthode abstraite issue de la super classe TemplateActivity
     *
     * @see TemplateActivity
     *
     * @return
     *      L'identifiant de l'activity de type int
     */
    protected int getLayoutId() {
        return R.layout.activity_salle;
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
        return R.id.salle_customTextViewTitle;
    } // int

    /**
     * Méthode appelée à la création de l\'activité Salle
     * <p>
     *     L'exécution de cette méthode se déroule en 4 phases:
     *     <ul>
     *         <li>appel de la méthode onCreate() sur la super classe TemplateActivity,</li>
     *         <li>éléments présents dans le layout XML initialisés,</li>
     *         <li>mise en oeuvre du listview,</li>
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mEditTextTotal = (TextView) findViewById(R.id.salle_textViewTotal);
        mEditTextCommande = (TextView) findViewById(R.id.salle_textViewCommande);
        mButtonClearAll = (Button) findViewById(R.id.salle_buttonClearAll);

        mCommandes = super.getController().getCommandes();
        mListAdapter = new CustomSalleListAdapter(this, mCommandes);

        mListView = (ListView) findViewById(R.id.salle_listView);
        mListView.setAdapter(mListAdapter);

        mButtonClearAll.setEnabled(mCommandes.size() >= 1);

        super.connectClient();

    } // void

    /**
     * Réalise la mise à jour des données contenues dans le listview
     *
     */
    @Override
    protected void onRestart() {
        super.onRestart();

        mListAdapter.notifyDataSetChanged();
    }

    //******************************************************************************
    // callback client: connexion

    public void disconnectedFromClient() {
        //super.createLogD("disconnectedFromClient");
        super.hideProgressDialog();
    } // void

    /**
     * Implémentation de ClientCallback: la connection est établie.
     *
     * @see Client
     */
    @Override
    public void connectedFromClient() {
        //super.createLogD("connectedFromClient callback");
        super.hideProgressDialog();
        this.updateHeaderInfo();
    } // void

    public void errorFromClient(String pError) {
        //super.createLogD( "errorFromClient");

        String nMessage = pError.isEmpty() ? getString(R.string.activity_toastMessageNoConnection) : pError;

        super.toastMessage(nMessage, true);
    } // void

    // callback client: connexion
    //******************************************************************************


    //******************************************************************************
    // callback client: data

    /**
     * Implémentation de ClientCallback: réponse reçue du serveur suite à une requête AJOUT ou COMMANDE.
     *
     * @param pResponseFromServer
     *      Réponse de type String
     */
    @Override
    public void singleFromClient(final String pResponseFromServer) {
        if (mTotalPlat > 0) {
            mTotalPlat--;

        } else {
            super.hideProgressDialog();
        } // else
    }

    /**
     * Implémentation de ClientCallback: données sont reçues du serveur suite à une requête QUANTITE.
     *
     * @param pListData
     *      Données sous forme d'une collection de String.
     */
    @Override
    public void quantiteFromClient(List<String> pListData) {
        super.hideProgressDialog();
    }

    // callback client: data
    //******************************************************************************


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
        super.createLogD("clicLeftFromListAdapter callback");

        //approvisionner le stock avec les crepes issues de la commande
        mTotalPlat = pCommande.getTotalPlat();
        this.annulerCommande(pCommande);

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
        super.createLogD("clicRightFromListAdapter callback " + pCommande.getId());

        //identifier la commande courante
        super.getController().setCurrentCommande(pCommande.getId());

        super.startSelectedActivity(TableActivity.class);
    } // void

    // callback listAdapter
    //******************************************************************************

    private void updateHeaderInfo() {

        switch (mCommandes.size()) {
            case 0:
                mEditTextTotal.setText("");
                mEditTextCommande.setText(R.string.salle_textViewCommande_none);
                mButtonClearAll.setEnabled(false);
                break;

            case 1:
                mEditTextTotal.setText(R.string.salle_textViewTotal_one);
                mEditTextCommande.setText(R.string.salle_textViewCommande_one);
                mButtonClearAll.setEnabled(true);
                break;

            default:
                mEditTextTotal.setText(mCommandes.getValueOfSize());
                mEditTextCommande.setText(R.string.salle_textViewCommande_more);
                mButtonClearAll.setEnabled(true);
        } // switch
    } // void

    private void annulerCommande(final Commande pCommande) {
        Iterator<Plat> nIterator = pCommande.getPlats().iterator();

        while (nIterator.hasNext()) {
            Plat nPlat = nIterator.next();
            if (nPlat.getQuantite() >= 1) {
                super.clientSendAjout(nPlat.getQuantite() + " " + nPlat.getNom(), false);
            } // if
        } // while
    } // void

    // associé à salle_buttonLeft
    public void warnBeforeClearAll(View pView) {

        AlertDialog.Builder nAlertDialogBuilder = new AlertDialog.Builder(this);
        nAlertDialogBuilder.setTitle(R.string.salle_alertDialogTitle);
        nAlertDialogBuilder.setMessage(R.string.salle_alertDialogMessage);

        nAlertDialogBuilder.setPositiveButton(R.string.salle_alertDialogYes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                clearAll();
            }
        });

        nAlertDialogBuilder.setNegativeButton(R.string.salle_alertDialogNo, null);

        AlertDialog nAlertDialog = nAlertDialogBuilder.create();
        nAlertDialog.show();
    }

    public void clearAll() {
        super.createLogD("clearAll");

        mTotalPlat = 0;
        Iterator<Commande> nIterator = mCommandes.iterator();
        while (nIterator.hasNext()) {
            mTotalPlat = mTotalPlat + nIterator.next().getTotalPlat();
        } // while

        nIterator = mCommandes.iterator();
        while (nIterator.hasNext()) {
            this.annulerCommande(nIterator.next());
        } // while

        //enlever toutes les commandes de la liste
        mCommandes.clear();

        // maj de l'ihm
        updateHeaderInfo();
        mListAdapter.notifyDataSetChanged();
    } // void

    // associé à salle_buttonRight
    public void addCommande(View pView) {
        //super.createLogD("addCommande");

        //ajouter une commande dans de la liste
        Commande nCommande = new Commande();
        mCommandes.add(nCommande);

        // maj de l'ihm
        updateHeaderInfo();
        mListAdapter.notifyDataSetChanged();

        //identifier la commande courante
        super.getController().setCurrentCommande(nCommande.getId());

        startSelectedActivity(TableActivity.class);
    } // void



    /**
     * Réalise la mise à jour des données issues du SharedPreferences
     * <p>
     *     L'implémentation de cette méthode abstraite est inutile ici.
     * </p>
     */
    protected void updatePreference() {

    }
} // class
