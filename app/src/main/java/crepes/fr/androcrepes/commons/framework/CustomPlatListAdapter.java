package crepes.fr.androcrepes.commons.framework;

import android.content.Context;
import android.graphics.Paint;
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
public class CustomPlatListAdapter
        extends ArrayAdapter<Plat> {

    public interface PlatListAdapterCallBack {
        void clicLeftFromListAdapter(Plat pPlat);
        void clicRightFromListAdapter(Plat pPlat);
    } // interface

    private PlatListAdapterCallBack mCallBack;

    private static final String TAG = CustomPlatListAdapter.class.getSimpleName();

    private LayoutInflater mInflater = null;

    private boolean mIsCuisine = false;

    //private int mCount = 0;

    public CustomPlatListAdapter(final Context pContext, Plats pPlats) {
        super(pContext, 0, pPlats);
        this.mCallBack = (PlatListAdapterCallBack) pContext;

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
            final TextView nTextViewListAdapterInfo = (TextView) nView.findViewById(R.id.plat_textViewRowInfo);
            final TextView nTextViewListAdapterQuantite = (TextView) nView.findViewById(R.id.plat_textViewRowQuantite);

            if (nTextViewListAdapterInfo != null) {
                final Button nButtonListAdapterLeft = (Button) nView.findViewById(R.id.plat_buttonRowLeft);
                nButtonListAdapterLeft.setText(mIsCuisine ? R.string.cuisine_buttonRowLeft : R.string.table_buttonRowLeft);
                nButtonListAdapterLeft.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        //mCount--;
                        //Log.d(TAG, "nBtnRemove click " + mCount);
                        //Log.d(TAG, "nBtnRemove click " + nPlat.getId());

                        mCallBack.clicLeftFromListAdapter(nPlat);
                    }
                });

                nTextViewListAdapterQuantite.setText(String.valueOf(nPlat.getQuantite()));
                nTextViewListAdapterInfo.setText(nPlat.getNom());

                if (0 == nPlat.getQuantite()) {
                    nTextViewListAdapterInfo.setPaintFlags(nTextViewListAdapterInfo.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                } else {
                    nTextViewListAdapterInfo.setPaintFlags(nTextViewListAdapterInfo.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));

                }

                final Button nButtonListAdapterRight = (Button) nView.findViewById(R.id.plat_buttonRowRight);
                nButtonListAdapterRight.setText(mIsCuisine ? R.string.cuisine_buttonRowRight : R.string.table_buttonRowRight);
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