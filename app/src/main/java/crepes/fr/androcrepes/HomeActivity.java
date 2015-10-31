package crepes.fr.androcrepes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    public final static String EXTRA_ACTION = "EXTRA_ACTION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("HomeActivity", "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


    } // void

    // event associé au bouton btnHomeCuisine
    public void goCuisine(View v) {
        Intent intent = new Intent(this, CuisineActivity.class);
        startSelectedActivity(intent);
        //startMainActivity("cuisine");
    }

    // event associé au bouton btnHomeSalle
    public void goSalle(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startSelectedActivity(intent);
        //startMainActivity("salle");
    }


    // démarre l'activity MainActivity
    //fixme mettre une enum à la plase du paramètre String
    private void startMainActivity(final String pAction) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(EXTRA_ACTION, pAction);
        startActivity(intent);
    }

    private void startSelectedActivity(Intent intent){
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("HomeActivity", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("HomeActivity", "onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("HomeActivity", "onRestart");
    }

    @Override
    protected void onPause() {
        Log.i("HomeActivity", "onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.i("HomeActivity", "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i("HomeActivity", "onDestroy");
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i("HomeActivity", "onCreateOptionMenu");
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i("HomeActivity", "onOptionsItemSelected");
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void goAide(View view) {
        Intent intent = new Intent(this, AideActivity.class);
        startSelectedActivity(intent);
    }
} // class
