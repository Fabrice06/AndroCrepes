package crepes.fr.androcrepes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements  OnClickListener {

    private ArrayList<LinearLayout> mLayouts = new ArrayList<LinearLayout>();
    private ArrayList<Button> mBoutonsAdd = new ArrayList<Button>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("MainActivity", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout row = null;
        for(int i = 0 ; i<4; i++){
            row = new LinearLayout(this);
            row.setId(i);

            Button bt = new Button(this);
            bt.setText("+");

            Button bp = new Button(this);
            bp.setText("-");

            TextView tv = new TextView(this);
            tv.setWidth(500);

            row.addView(bt);
            row.addView(tv);
            row.addView(bp);


            mLayouts.add(row);
        }



/*        Button cb = null;
        for (int i =0; i<12; i++) {
            cb = new Button(this);
            cb.setText("Button #" + Integer.toString(i));
            //cb.setBackgroundResource(R.drawable.fancy_button_selector);
            cb.setOnClickListener(this);
            cb.setId(i);
            //registerForContextMenu(cb);
            mButtons.add(cb);
        }
*/
        GridView gridView = (GridView) findViewById(R.id.gridview);
        gridView.setAdapter(new CustomLayoutAdapter(mLayouts));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i("MainActivity", "onCreateOptionMenu");
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i("MainActivity", "onOptionsItemSelected");
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Log.i("MainActivity", "onClick");

    }
}
