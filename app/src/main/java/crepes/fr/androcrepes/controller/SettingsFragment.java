package crepes.fr.androcrepes.controller;


import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.util.Log;

import crepes.fr.androcrepes.R;

public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {

    private static final String TAG = SettingsFragment.class.getSimpleName();

    public SettingsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        // Appel de la méthode de la super-classe
        super.onCreate(savedInstanceState);
        // Chargement de l'interface XML
        addPreferencesFromResource(R.xml.preferences);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        Log.d(TAG, "onSharedPreferenceChanged");

        if (key.equals("ip")) {
            // Récupération de la préférence associée à la clé
            EditTextPreference ip = (EditTextPreference) findPreference(key);
            // Action à effectuer
            Controller.SERVER_IP = ip.getText();
            Log.d("*************** SettingFragment", "IP : "+ip.getText());
        }

        if (key.equals("port")){
            EditTextPreference port = (EditTextPreference) findPreference(key);

            Controller.SERVER_PORT = Integer.parseInt(port.getText());
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
