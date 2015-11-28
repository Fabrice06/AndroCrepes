package crepes.fr.androcrepes.view;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

import crepes.fr.androcrepes.R;

public class SettingActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        Preference nPreference = (Preference)getPreferenceManager().findPreference("preferenceClose");
        if (nPreference != null) {
            nPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference arg0) {
                    finish();
                    return true;
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
} // class
