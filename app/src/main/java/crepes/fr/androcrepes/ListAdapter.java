package crepes.fr.androcrepes;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import crepes.fr.androcrepes.entity.Plat;
import crepes.fr.androcrepes.entity.Plats;


public class ListAdapter extends ArrayAdapter<Plat> {

    public interface ListAdapterCallBack {
        void addFromListAdapter(Plat pPlat);
        void removeFromListAdapter(Plat pPlat);
    } // interface

    private ListAdapterCallBack mCallBack;

    private static final String TAG = ListAdapter.class.getSimpleName();

    private LayoutInflater mInflater = null;

    public ListAdapter(final Context pContext, Plats pPlats) {
        super(pContext, 0, pPlats);
        this.mCallBack = (ListAdapterCallBack) pContext;
    } // constructeur

    @Override
    public View getView(final int pPosition, final View pConvertView, final ViewGroup pParent) {
        Log.d(TAG, "getView");

        View nView = pConvertView;

        if (null == nView) {
            mInflater = LayoutInflater.from(getContext());
            nView = mInflater.inflate(R.layout.row_plat, pParent, false);
        } // if

        final Plat nPlat = getItem(pPosition);

        if (null != nPlat) {
            final TextView nTxtInfo = (TextView) nView.findViewById(R.id.txtRowInfo);

            if (nTxtInfo != null) {
                final Button nBtnRemove = (Button) nView.findViewById(R.id.btnRowRemove);
                nBtnRemove.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        //Log.d(TAG, "nBtnRemove click " + nPlat.getId());

                        mCallBack.removeFromListAdapter(nPlat);
                    }
                });

                nTxtInfo.setText(nPlat.getNom());

                final Button nBtnAdd = (Button) nView.findViewById(R.id.btnRowAdd);
                nBtnAdd.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        //Log.d(TAG, "nBtnAdd click " + nPlat.getId());

                        mCallBack.addFromListAdapter(nPlat);
                    }
                });

            } // if
        } // if

        return nView;
    } // View
} // class