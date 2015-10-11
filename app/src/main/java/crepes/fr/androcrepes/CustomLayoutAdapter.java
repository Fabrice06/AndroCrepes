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

    public CustomLayoutAdapter(ArrayList<LinearLayout> pLayout) {
        mLayouts = pLayout;
    }

    @Override
    public int getCount() {
        return mLayouts.size();
    }

    @Override
    public Object getItem(int pId) {
        return (Object) mLayouts.get(pId);
    }

    @Override
    public long getItemId(int pId) {
        return pId;
    }

    @Override
    public View getView(int pId, View pConvertView, ViewGroup pParent) {
        LinearLayout nLayout =  null;

        if (null == pConvertView) {
            nLayout = mLayouts.get(pId);

        } else {
            nLayout = (LinearLayout) pConvertView;
        }

        //nLayout.setOrientation(LinearLayout.HORIZONTAL);
        //nLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        Button nBtnAdd = new Button(nLayout.getContext());
        nBtnAdd.setText("+");
        nBtnAdd.setOnClickListener(mListenerAdd);

        TextView nTxtInfo = new TextView(nLayout.getContext());
        nTxtInfo.setText("test plat");

        Button nBtnRemove = new Button(nLayout.getContext());
        nBtnRemove.setText("-");
        nBtnRemove.setOnClickListener(mListenerRemove);

        nLayout.addView(nBtnAdd);
        nLayout.addView(nTxtInfo);
        nLayout.addView(nBtnRemove);

        return nLayout;
    } // View
} // class