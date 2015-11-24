package crepes.fr.androcrepes.commons.framework;

import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

import crepes.fr.androcrepes.R;
import crepes.fr.androcrepes.commons.java.EnumReceiveWord;
import crepes.fr.androcrepes.commons.java.EnumSendWord;
import crepes.fr.androcrepes.commons.java.Tools;
import crepes.fr.androcrepes.commons.network.Client;
import crepes.fr.androcrepes.model.Plat;
import crepes.fr.androcrepes.model.Plats;


public abstract class CustomListActivity
        extends TemplateActivity
        implements CustomPlatListAdapter.PlatListAdapterCallBack {


    private ListView mListView = null;
    private CustomPlatListAdapter mListAdapter;

    private Plats mPlats;
    protected abstract Plats getPlats();

    protected abstract int getListViewId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPlats = this.getPlats();
        mListAdapter = new CustomPlatListAdapter(this, mPlats);

        mListView = (ListView) findViewById(getListViewId());
        mListView.setAdapter(mListAdapter);

        super.connectClient();

    } // void


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
        super.clientSendQuantity();
    } // void

    public void errorFromClient(String pError) {
        //super.createLogD( "errorFromClient");

        //fixme pError pas utilisé
        String nMessage = getString(R.string.activity_toastMessageNoConnection);

        super.toastMessage(nMessage, true);
    } // void

    // callback client: connexion
    //******************************************************************************


    //******************************************************************************
    // callback client: data

    protected abstract void updateCurrentPlatAfterCommande(final String pResponseFromServer);
    protected abstract void updateCurrentPlatAfterAjout(final String pResponseFromServer);

    /**
     * Implémentation de ClientCallback: réponse reçue du serveur suite à une requête AJOUT ou COMMANDE.
     *
     * @param pResponseFromServer
     *      Réponse de type String
     */
    @Override
    public void singleFromClient(final String pResponseFromServer) {
        super.createLogD("singleFromClient callback: " + pResponseFromServer);

        // recherche du dernier mot/chiffre pour identifier la réponse
        String nReponse = pResponseFromServer.substring(pResponseFromServer.lastIndexOf(" ")+1);

        if (nReponse.equals(EnumReceiveWord.EPUISE.getValue()) || (nReponse.equals(EnumReceiveWord.INCONNU.getValue()))) {
            // échec d'une commande ('épuisé' ou 'inconnu' trouvé en fin de message)
            super.toastMessage(pResponseFromServer + " !", true);

        } else if (nReponse.equals(EnumReceiveWord.COMMANDE.getValue())) { // en réponse à l'ordre COMMANDE
            this.updateCurrentPlatAfterCommande(pResponseFromServer);

        } else if (Tools.isInteger(nReponse)) { // en réponse à l'ordre AJOUT
            this.updateCurrentPlatAfterAjout(pResponseFromServer);

        } else {
            // cas non répertorié: ceinture et bretelles
            String nMessage = getString(R.string.activity_toastMessageUnknownError);
            super.toastMessage(nMessage, true);
        } // else
    } // void


    /**
     * Implémentation de ClientCallback: données sont reçues du serveur suite à une requête QUANTITE.
     *
     * @param pListData
     *      Données sous forme d'une collection de String.
     */
    @Override
    public abstract void quantiteFromClient(List<String> pListData);

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
    public abstract void clicLeftFromListAdapter(Plat pPlat);

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
    public abstract void clicRightFromListAdapter(Plat pPlat);

    // callback listAdapter
    //******************************************************************************


    private void scrollListViewByName(final String pNomPlat) {

        int nIndex = 0;
        while (nIndex < mListView.getCount()) {

            Plat nPlat = (Plat)mListView.getItemAtPosition(nIndex);
            //super.createLogD("scrollListViewByName nom: " + pNomPlat + " getNom " + nPlat.getNom() + " index "+ nIndex);
            if (nPlat.getNom().equals(pNomPlat)) {
                mListView.smoothScrollToPosition(nIndex);
                break;
            } // if
            nIndex++;
        } // while
    } // void

    protected void updateAfterClientQuantite(final String pNomPlat) {
        // maj de l'ihm
        mListAdapter.notifyDataSetChanged();

        if (!pNomPlat.isEmpty()) {
            this.scrollListViewByName(pNomPlat);

            String nMessage = getString(R.string.activity_toastMessageBeforePlat);
            nMessage = nMessage + pNomPlat;
            nMessage = nMessage + getString(R.string.activity_toastMessageAfterPlat);

            super.toastMessage(nMessage, true);
        } // if

        super.hideProgressDialog();;
    } // void

} // class

