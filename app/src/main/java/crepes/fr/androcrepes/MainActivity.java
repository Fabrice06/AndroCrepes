package crepes.fr.androcrepes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import crepes.fr.androcrepes.entity.Plat;
import crepes.fr.androcrepes.entity.Plats;

public class MainActivity extends AppCompatActivity {

    ListView mListViewMain = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("MainActivity", "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListViewMain = (ListView) findViewById(R.id.listViewMain);
        mListViewMain.setDivider(null);

        Plats nPlats = Plats.getInstance();

        for (int i=0; i<20; i++) {
            final Plat nPlat= new Plat(i, "test" + i);

            nPlats.addPlat(nPlat);
        }

        ListAdapter mListAdapter = new ListAdapter(this, nPlats);

        mListViewMain.setAdapter(mListAdapter);

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
