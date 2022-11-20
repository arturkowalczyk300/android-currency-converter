package com.arturkowalczyk300.currencyconverter.Preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesRepository {
    //static variables
    public static final String PREFERENCES_ID = "com.arturkowalczyk300.currencyconverter.MAIN_PREFERENCES";
    public static final String SETTING_STRING_DISPLAY_DEBUG_TOASTS = "com.arturkowalczyk300.currencyconverter.SETTING_DISPLAY_DEBUG_TOASTS";

    //non-static variables
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private boolean settingDisplayDebugToasts;

    //constructor
    public PreferencesRepository(Context context)
    {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(PREFERENCES_ID, Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();

        loadSettings();
    }

    //methods

    public boolean isSettingDisplayDebugToasts() {
        return settingDisplayDebugToasts;
    }

    public void setSettingDisplayDebugToasts(boolean settingDisplayDebugToasts) {
        this.settingDisplayDebugToasts = settingDisplayDebugToasts;
        saveSettings();
    }

    private void loadSettings()
    {
        this.settingDisplayDebugToasts = sharedPreferences.getBoolean(SETTING_STRING_DISPLAY_DEBUG_TOASTS, false);
    }

    private void saveSettings()
    {
        editor.putBoolean(SETTING_STRING_DISPLAY_DEBUG_TOASTS, settingDisplayDebugToasts);
        editor.apply();
    }
}
