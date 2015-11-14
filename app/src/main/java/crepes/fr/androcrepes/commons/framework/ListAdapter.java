package crepes.fr.androcrepes.commons.framework;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import crepes.fr.androcrepes.R;
import crepes.fr.androcrepes.model.Plat;
import crepes.fr.androcrepes.model.Plats;
import crepes.fr.androcrepes.view.CuisineActivity;

/**
 * <b>Cette classe permet la réalisation d'une liste de données scrollable.</b>
 */
public class ListAdapter extends ArrayAdapter<Plat> {

    public interface ListAdapterCallBack {
        void clicLeftFromListAdapter(Plat pPlat);
        void clicRightFromListAdapter(Plat pPlat);
    } // interface

    private ListAdapterCallBack mCallBack;

    private static final String TAG = ListAdapter.class.getSimpleName();

    private LayoutInflater mInflater = null;

    private boolean mIsCuisine = false;

    //private int mCount = 0;

    public ListAdapter(final Context pContext, Plats pPlats) {
        super(pContext, 0, pPlats);
        this.mCallBack = (ListAdapterCallBack) pContext;

        mIsCuisine = (pContext instanceof CuisineActivity);
    } // constructeur

    @Override
    public View getView(final int pPosition, final View pConvertView, final ViewGroup pParent) {
        //Log.d(TAG, "getView");

        View nView = pConvertView;

        if (null == nView) {
            mInflater = LayoutInflater.from(getContext());
            nView = mInflater.inflate(R.layout.row_plat, pParent, false);
        } // if

        final Plat nPlat = getItem(pPosition);

        if (null != nPlat) {
            final TextView nTextViewListAdapterInfo = (TextView) nView.findViewById(R.id.listadapter_textViewInfoId);

            if (nTextViewListAdapterInfo != null) {
                final Button nButtonListAdapterLeft = (Button) nView.findViewById(R.id.buttonListAdapterLeft);
                nButtonListAdapterLeft.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        //mCount--;
                        //Log.d(TAG, "nBtnRemove click " + mCount);
                        //Log.d(TAG, "nBtnRemove click " + nPlat.getId());

                        mCallBack.clicLeftFromListAdapter(nPlat);
                    }
                });

                nTextViewListAdapterInfo.setText(nPlat.getNom());

                final Button nButtonListAdapterRight = (Button) nView.findViewById(R.id.buttonListAdapterRight);
                nButtonListAdapterRight.setText(mIsCuisine ? "ajouter" : "commander");
                nButtonListAdapterRight.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        //mCount++;
                        //Log.d(TAG, "nBtnAdd click " + mCount);

                        mCallBack.clicRightFromListAdapter(nPlat);
                    }
                });

            } // if
        } // if

    return nView;
    } // View
} // class