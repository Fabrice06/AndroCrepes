package crepes.fr.androcrepes;

import android.util.Log;
import android.view.Gravity;
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

        // create the layout params that will be used to define how 'add' Button will be displayed
        final LinearLayout.LayoutParams nParamsAdd = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        nParamsAdd.weight = 2.0f;
        nParamsAdd.gravity = Gravity.RIGHT;

        // create the layout params that will be used to define how 'info' TextView will be displayed
        final LinearLayout.LayoutParams nParamsTxt = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        nParamsTxt.weight = 96.0f;
        nParamsTxt.gravity = Gravity.CENTER;

        // create the layout params that will be used to define how 'remove' Button will be displayed
        final LinearLayout.LayoutParams nParamsRemove = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        nParamsRemove.weight = 2.0f;
        nParamsRemove.gravity = Gravity.LEFT;


        // create the layout params that will be used to define how layout will be displayed
        final LinearLayout.LayoutParams nParamsLayout = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        //LinearLayout nLayout =  null;

        //if (null == pConvertView) {
            //nLayout = mLayouts.get(pId);
        //} else {
        //    nLayout = (LinearLayout) pConvertView;
        //}

        LinearLayout nLayout = mLayouts.get(pId);
        nLayout.setOrientation(LinearLayout.HORIZONTAL);
        nLayout.setLayoutParams(nParamsLayout);


        final Button nBtnAdd = new Button(nLayout.getContext());
            nBtnAdd.setText("+");
            nBtnAdd.setLayoutParams(nParamsAdd);
            nBtnAdd.setOnClickListener(mListenerAdd);
            nLayout.addView(nBtnAdd);

        TextView nTxtInfo = new TextView(nLayout.getContext());
            nTxtInfo.setText("test plat");
            nTxtInfo.setLayoutParams(nParamsTxt);
            nLayout.addView(nTxtInfo);

        final Button nBtnRemove = new Button(nLayout.getContext());
            nBtnRemove.setText("-");
            nBtnRemove.setLayoutParams(nParamsRemove);
            nBtnRemove.setOnClickListener(mListenerRemove);
            nLayout.addView(nBtnRemove);

        return nLayout;
    } // View
} // class