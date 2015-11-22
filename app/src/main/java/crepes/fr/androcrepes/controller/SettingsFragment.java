package crepes.fr.androcrepes.controller;


import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;
import android.util.Log;

import crepes.fr.androcrepes.R;

public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {

    private static final String TAG = SettingsFragment.class.getSimpleName();

//    private Controller mController;

    public SettingsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        // Appel de la méthode de la super-classe
        super.onCreate(savedInstanceState);
        // Chargement de l'interface XML
        addPreferencesFromResource(R.xml.preferences);

        //Get Global Controller Class object (see application tag in AndroidManifest.xml)
//        mController = (Controller) pContext;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        Log.d(TAG, "onSharedPreferenceChanged");

        if (key.equals("ip")) {
            // Récupération de la préférence associée à la clé
            EditTextPreference ip = (EditTextPreference) findPreference(key);
            // Action à effectuer
//            mController.setServerIp(ip.getText());
            Log.d("*************** SettingFragment", "IP : "+ip.getText());
        }

        if (key.equals("port")){
            EditTextPreference port = (EditTextPreference) findPreference(key);

//            mController.setServerPort(Integer.parseInt(port.getText()));
            Log.d("*************** SettingFragment", "Port: "+port.getText());
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

}
