package crepes.fr.androcrepes.view;

import android.content.res.Resources;
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
import crepes.fr.androcrepes.commons.framework.CustomTextView;

/**
 * <b>Classe dédiée à la description de l'ihm Aide.</b>
 */
public class AideActivity extends AppCompatActivity {

    private static final String TAG = AideActivity.class.getSimpleName();

    private WebView webwiew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aide);
        //Log.d(TAG, "onCreate");

        webwiew = (WebView) findViewById(R.id.webwiew);

        CustomTextView nCustomTextView = (CustomTextView) findViewById(R.id.aide_customTextViewTitle);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Log.d("***** Langue :", Locale.getDefault().getDisplayLanguage());

        Resources res = getResources();

        String uri = String.format(res.getString(R.string.aide_URI));

        webwiew.loadUrl(uri);

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
