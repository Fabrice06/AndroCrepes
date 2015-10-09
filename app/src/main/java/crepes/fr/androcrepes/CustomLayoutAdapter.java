package crepes.fr.androcrepes;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.ArrayList;


public class CustomLayoutAdapter extends BaseAdapter {

    private ArrayList<LinearLayout> mLayouts = null;


    public CustomLayoutAdapter(ArrayList<LinearLayout> l) {
        mLayouts = l;
    }

    @Override
    public int getCount() {
        return mLayouts.size();
    }

    @Override
    public Object getItem(int position) {
        return (Object) mLayouts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout layout;
        if (convertView == null) {
            layout = mLayouts.get(position);
        } else {
            layout = (LinearLayout) convertView;
        }
        layout.setOrientation(LinearLayout.HORIZONTAL);

        //Button bAdd = new Button(this);
        //bAdd.setText("+");

        //layout.addView(bAdd);


        return layout;
    }
}