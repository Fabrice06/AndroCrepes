package crepes.fr.androcrepes.view;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import java.util.Locale;

import crepes.fr.androcrepes.R;

/**
 * <b>Classe dédiée à la description de l'ihm Aide.</b>
 */
public class AideActivity extends AppCompatActivity {

    private static final String TAG = AideActivity.class.getSimpleName();

    //Uri uri = Uri.parse("http://www.google.fr");
    String uri = "http://www.google.fr";
    String local = "file:///android_asset/aideHtml/html/index.html";
    String local_en = "file:///android_asset/aideHtml/html/index_en.html";
    String local_es = "file:///android_asset/aideHtml/html/index_es.html";

    private WebView webwiew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aide);
        //Log.d(TAG, "onCreate");

        webwiew = (WebView) findViewById(R.id.webwiew);

        TextView myTextView = (TextView) findViewById(R.id.textAide);
        Typeface myFont = Typeface.createFromAsset(getAssets(), "Milasian.ttf");
        myTextView.setTypeface(myFont);
        myTextView.setTextSize(30);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Log.d(TAG, "onCreateOptionMenu");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_aide, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Log.d(TAG, "onOptionsItemSelected");
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Log.d(TAG, "onStart"); français English español

        Log.d("***** Langue :", Locale.getDefault().getDisplayLanguage());

        String langue = Locale.getDefault().getDisplayLanguage();

        if ("français".equals(langue)) {
            webwiew.loadUrl(local);
        }
        if ("English".equals(langue)) {
            webwiew.loadUrl(local_en);
        }
        if ("español".equals(langue)) {
            webwiew.loadUrl(local_es);
        }


        webwiew.setWebViewClient(new WebViewClient());
    }

    /**
     * Evènement associé au bouton aide_imageButtonGoHome pour naviguer vers l'ihm Home
     *
     * @param pView
     *      Objet de type View
     */
    public void goHome(View view) {
        //Log.d(TAG, "goHome");
        finish();
    }
}
