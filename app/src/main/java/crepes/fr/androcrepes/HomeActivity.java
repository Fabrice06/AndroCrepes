package crepes.fr.androcrepes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();

    public final static String EXTRA_ACTION = "EXTRA_ACTION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


    } // void

    // event associé au bouton btnHomeCuisine
    public void goCuisine(View v) {
        Log.d(TAG, "goCuisine");
        Intent intent = new Intent(this, CuisineActivity.class);
        startSelectedActivity(intent);
        //startMainActivity("cuisine");
    }

    // event associé au bouton btnHomeSalle
    public void goSalle(View v) {
        Log.d(TAG, "goSalle");
        Intent intent = new Intent(this, MainActivity.class);
        startSelectedActivity(intent);
        //startMainActivity("salle");
    }


    // démarre l'activity MainActivity
    //fixme mettre une enum à la plase du paramètre String
    private void startMainActivity(final String pAction) {
        Log.d(TAG, "startMainActivity");
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(EXTRA_ACTION, pAction);
        startActivity(intent);
    }

    private void startSelectedActivity(Intent intent){
        Log.d(TAG, "startSelectedActivity");
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionMenu");
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected");
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void goAide(View view) {
        Log.d(TAG, "goAide");
        Intent intent = new Intent(this, AideActivity.class);
        startSelectedActivity(intent);
    }
} // class
