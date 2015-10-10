package crepes.fr.androcrepes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<LinearLayout> mRows = new ArrayList<LinearLayout>();

    GridView gridViewMain = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("MainActivity", "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        for(int i = 0 ; i<4; i++){
            LinearLayout nRow = new LinearLayout(this);

            nRow.setId(i);

            mRows.add(nRow);
        } // for

        gridViewMain = (GridView) findViewById(R.id.gridViewMain);
        gridViewMain.setAdapter(new CustomLayoutAdapter(mRows));

    } // void



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
} // class
