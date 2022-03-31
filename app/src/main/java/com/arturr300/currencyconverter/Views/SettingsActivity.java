package com.arturr300.currencyconverter.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;

import com.arturr300.currencyconverter.R;

public class SettingsActivity extends AppCompatActivity {
    //static variables
    public static final int DEFAULT_RESULT_CODE = 0;

    //non-static variables
    private boolean settingDisplayDebugToasts;
    private CheckBox checkBoxDisplayDebugToasts;

    //constructor

    //methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkBoxDisplayDebugToasts = findViewById(R.id.settings_checkbox_display_debug_toasts);

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.LightTheme);
        }

        setContentView(R.layout.activity_settings);

        Bundle bundle = getIntent().getExtras();
        settingDisplayDebugToasts = bundle.getBoolean(MainActivity.INTENT_ID_SETTINGS_DISPLAY_DEBUG_TOASTS);
        checkBoxDisplayDebugToasts.setChecked(settingDisplayDebugToasts);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveSettingsAndFinish();
    }

    private void saveSettingsAndFinish()
    {
        Intent intentWithData = new Intent();
        intentWithData.putExtra(MainActivity.INTENT_ID_SETTINGS_DISPLAY_DEBUG_TOASTS, settingDisplayDebugToasts);
        setResult(DEFAULT_RESULT_CODE, intentWithData);
        finish();
    }
}