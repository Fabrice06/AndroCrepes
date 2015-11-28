package crepes.fr.androcrepes.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import java.util.List;

import crepes.fr.androcrepes.R;
import crepes.fr.androcrepes.commons.framework.CustomListActivity;
import crepes.fr.androcrepes.commons.java.EnumSendWord;
import crepes.fr.androcrepes.model.Plat;
import crepes.fr.androcrepes.model.Plats;

/**
 * <b>Classe dédiée à la description de l'ihm Salle.</b>
 */

public class TableActivity
        extends CustomListActivity {

    private Plat mCurrentPlat = null;

    protected int getLayoutId() {
        return R.layout.activity_table;
    } // int

    protected int getTextViewTitleId() {
        return R.id.table_customTextViewTitle;
    } // int

    protected int getListViewId() {
        return R.id.table_listView;
    } // int

    protected Plats getPlats() {
        return super.getController().getCurrentCommande().getPlats();
    } // Plats

    private ImageButton mImageButtonFilter;

    // associé à table_imageButtonFilter
    public void doFilter(View pView) {
        
        boolean nIsFilterOn = ! super.getController().getCurrentCommande().getFilter();
        super.getController().getCurrentCommande().setFilter(nIsFilterOn);

        int nId = super.getController().getCurrentCommande().getId();
        super.getController().getCommandes().getCommande(nId).setFilter(nIsFilterOn);

        if (nIsFilterOn) {
            mImageButtonFilter.setImageResource(R.drawable.list);
        } else {
            mImageButtonFilter.setImageResource(R.drawable.checked);
        }
        super.doFilter(nIsFilterOn);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String nTitle = getString(R.string.table_customTextViewTitle);
        nTitle = nTitle + super.getController().getCurrentCommande().getValueOfId();
        super.setTitle(nTitle);

        mImageButtonFilter = (ImageButton) findViewById(R.id.table_imageButtonFilter);

        super.doFilter(super.getController().getCurrentCommande().getFilter());
    } // void

    protected void updateCurrentPlatAfterCommande(final String pResponseFromServer) {
        if (null != mCurrentPlat) {
            if (pResponseFromServer.indexOf(mCurrentPlat.getNom()) >= 0) {
                mCurrentPlat.setQuantite(mCurrentPlat.getQuantite() + 1);
            }
        }
        super.clientSendQuantity();
    } // void

    protected void updateCurrentPlatAfterAjout(final String pResponseFromServer) {
        if (null != mCurrentPlat) {
            if (pResponseFromServer.indexOf(mCurrentPlat.getNom()) >= 0) {
                if (mCurrentPlat.getQuantite() >= 1) {
                    mCurrentPlat.setQuantite(mCurrentPlat.getQuantite() - 1);
                } else {
                    mCurrentPlat.setQuantite(0);
                }
            }
        }
        super.clientSendQuantity();
    } // void


    //******************************************************************************
    // callback client: data

    public void quantiteFromClient(List<String> pListData) {
        super.createLogD("quantiteFromClient callback");

        Plats nPlats = super.getController().getCurrentCommande().getPlats();

        boolean nIsNewPlat = false;

        for (int nLen = pListData.size(), i = 1; i < (nLen-1); i+=2) {
            String nNom = pListData.get(i);
            int nStock = Integer.parseInt(pListData.get(i + 1));

            Plat nPlat = nPlats.getPlatByName(nNom);

            // nouveau plat
            if (null == nPlat) {
                nPlat= new Plat(nNom, 0, nStock);
                nPlats.addPlat(nPlat);

                if (!nIsNewPlat) {
                    nIsNewPlat = true;
                } // if

            } else { // update stock
                nPlat.setStock(nStock);
            } // else

        } // for

        // tri par nom de plat si nouvel ajout
        if (nIsNewPlat) {
            nPlats.sort();
        } // if

        super.updateAfterClientQuantite("");
    } // void

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
    public void clicLeftFromListAdapter(Plat pPlat) {
        //super.createLogD("clicLeftFromListAdapter callback");

        mCurrentPlat = pPlat;
        super.clientSendAjout("1 " + pPlat.getNom(), true);
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
    public void clicRightFromListAdapter(Plat pPlat) {
        //super.createLogD("clicRightFromListAdapter callback");

        mCurrentPlat = pPlat;
        super.clientSendCommande(pPlat.getNom(), true);
    } // void

    // callback listAdapter
    //******************************************************************************

} // class
