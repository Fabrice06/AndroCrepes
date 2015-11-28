package crepes.fr.androcrepes.commons.framework;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import crepes.fr.androcrepes.R;
import crepes.fr.androcrepes.model.Commande;
import crepes.fr.androcrepes.model.Commandes;

/**
 * <b>Cette classe permet la réalisation d'une liste de données scrollable.</b>
 */
public class CustomSalleListAdapter
        extends ArrayAdapter<Commande> {

    public interface SalleListAdapterCallBack {
        void clicLeftFromListAdapter(Commande pCommande);
        void clicRightFromListAdapter(Commande pCommande);
    } // interface

    private SalleListAdapterCallBack mCallBack;

    private static final String TAG = CustomSalleListAdapter.class.getSimpleName();

    private LayoutInflater mInflater = null;


    public CustomSalleListAdapter(final Context pContext, Commandes pCommandes) {
        super(pContext, 0, pCommandes);
        this.mCallBack = (SalleListAdapterCallBack) pContext;
    } // constructeur

    @Override
    public View getView(final int pPosition, final View pConvertView, final ViewGroup pParent) {
        //Log.d(TAG, "getView");

        View nView = pConvertView;

        if (null == nView) {
            mInflater = LayoutInflater.from(getContext());
            nView = mInflater.inflate(R.layout.row_salle, pParent, false);
        } // if

        final Commande nCommande = getItem(pPosition);

        if (null != nCommande) {
            final TextView nTextViewRowNumero = (TextView) nView.findViewById(R.id.salle_TextViewRowNumero);
            final ImageView nImageViewListAdapterCheck = (ImageView) nView.findViewById(R.id.salle_ImageViewRowCheck);

            if (nTextViewRowNumero != null) {
                final Button nButtonLeft = (Button) nView.findViewById(R.id.salle_buttonRowLeft);
                nButtonLeft.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        mCallBack.clicLeftFromListAdapter(nCommande);
                    }
                });

                nImageViewListAdapterCheck.setImageResource(nCommande.getFilter() ? R.drawable.checked : R.drawable.list);

                nTextViewRowNumero.setText(nCommande.getValueOfId());

                final Button nButtonRight = (Button) nView.findViewById(R.id.salle_buttonRowRight);
                nButtonRight.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        mCallBack.clicRightFromListAdapter(nCommande);
                    }
                });

            } // if
        } // if

        return nView;
    } // View
} // class