package crepes.fr.androcrepes;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class CustomLayoutAdapter extends BaseAdapter {

    private ArrayList<LinearLayout> mLayouts = null;

    private View.OnClickListener mListenerAdd = new View.OnClickListener() {
        public void onClick(View v) {
            Log.i("MainActivity", "nBtnAdd click");
        }
    };

    private View.OnClickListener mListenerRemove = new View.OnClickListener() {
        public void onClick(View v) {
            Log.i("MainActivity", "nBtnRemove click");
        }
    };

    public CustomLayoutAdapter(final ArrayList<LinearLayout> pLayout) {
        mLayouts = pLayout;
    }

    @Override
    public int getCount() {
        return mLayouts.size();
    }

    @Override
    public Object getItem(final int pId) {
        return (Object) mLayouts.get(pId);
    }

    @Override
    public long getItemId(int pId) {
        return pId;
    }

    @Override
    public View getView(final int pId, final View pConvertView, final ViewGroup pParent) {

        LinearLayout nLayout =  null;

        if (null == pConvertView) {
            nLayout = mLayouts.get(pId);
        } else {
            nLayout = (LinearLayout) pConvertView;
        }

        Button nBtnRemove = (Button) nLayout.findViewById(R.id.btnRowRemove);
        nBtnRemove.setOnClickListener(mListenerRemove);

        TextView nTxtInfo = (TextView) nLayout.findViewById(R.id.txtRowInfo);
        nTxtInfo.setText("test plat");

        Button nBtnAdd = (Button) nLayout.findViewById(R.id.btnRowAdd);
        nBtnAdd.setOnClickListener(mListenerAdd);

        return nLayout;
    } // View
} // class