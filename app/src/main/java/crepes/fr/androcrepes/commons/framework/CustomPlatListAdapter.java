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

    private String mStringStock;

    //private int mCount = 0;

    public CustomPlatListAdapter(final Context pContext, Plats pPlats) {
        super(pContext, 0, pPlats);
        this.mCallBack = (PlatListAdapterCallBack) pContext;

        mIsCuisine = (pContext instanceof CuisineActivity);

        mStringStock = pContext.getString(R.string.table_textViewRowStock);
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

            final TextView nTextViewListAdapterStock = (TextView) nView.findViewById(R.id.plat_textViewRowStock);
            final ImageView nImageViewListAdapterCheck = (ImageView) nView.findViewById(R.id.plat_ImageViewRowCheck);


            if (nTextViewListAdapterInfo != null) {

                boolean nIsQuantiteEmpty = (0 == nPlat.getQuantite());
                boolean nIsStockEmpty = (0 == nPlat.getStock());

                final Button nButtonListAdapterLeft = (Button) nView.findViewById(R.id.plat_buttonRowLeft);
                final Button nButtonListAdapterRight = (Button) nView.findViewById(R.id.plat_buttonRowRight);

                if(mIsCuisine) {
                    nTextViewListAdapterQuantite.setText(String.valueOf(nPlat.getStock()));

                    nTextViewListAdapterStock.setText("");

                    nImageViewListAdapterCheck.setImageResource(R.drawable.check_empty);

                    nButtonListAdapterLeft.setText(R.string.cuisine_buttonRowLeft);
                    nButtonListAdapterLeft.setEnabled(!nIsStockEmpty);

                    nButtonListAdapterRight.setText(R.string.cuisine_buttonRowRight);

                } else {
                    nTextViewListAdapterQuantite.setText(String.valueOf(nPlat.getQuantite()));

                    final StringBuffer nBuffer = new StringBuffer();
                    nBuffer.append(mStringStock);
                    nBuffer.append(nPlat.getStock());
                    nTextViewListAdapterStock.setText(nBuffer.toString());

                    if (nIsQuantiteEmpty) { // pas check
                        nImageViewListAdapterCheck.setImageResource(R.drawable.check_off);

                    } else { // check
                        nImageViewListAdapterCheck.setImageResource(R.drawable.check_on);
                    }

                    nButtonListAdapterLeft.setText(R.string.table_buttonRowLeft);
                    nButtonListAdapterLeft.setEnabled(!nIsQuantiteEmpty);

                    nButtonListAdapterRight.setText(R.string.table_buttonRowRight);
                    nButtonListAdapterRight.setEnabled(!nIsStockEmpty);
                }

                nButtonListAdapterLeft.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        mCallBack.clicLeftFromListAdapter(nPlat);
                    }
                });

                nButtonListAdapterRight.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        mCallBack.clicRightFromListAdapter(nPlat);
                    }
                });

                nTextViewListAdapterInfo.setText(nPlat.getNom());

//                if (nIsEmpty) {
//                    nTextViewListAdapterInfo.setPaintFlags(nTextViewListAdapterInfo.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//                } else {
//                    nTextViewListAdapterInfo.setPaintFlags(nTextViewListAdapterInfo.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
//                }
            } // if
        } // if

    return nView;
    } // View
} // class