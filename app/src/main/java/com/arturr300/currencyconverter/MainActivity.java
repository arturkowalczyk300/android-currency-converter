package com.arturr300.currencyconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.arturr300.currencyconverter.CurrencyUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Context appContext;

    //components variables 
    EditText etUSD;
    EditText etEUR;
    EditText etPLN;
    TextView tvUSD2PLN;
    TextView tvUSD2EUR;
    TextView tvEUR2PLN;
    Button btnClear;
    Button btnConvert;
    //second part
    Spinner spinnerFirstCurrency;
    Spinner spinnerSecondCurrency;
    EditText etFirstCurrencyValue;
    EditText etSecondCurrencyValue;
    Button btnClear2;
    Button btnConvert2;

    //other variables
    double USD2PLN;
    double USD2EUR;
    double EUR2PLN;
    DecimalFormat df;
    CurrencyUtils mCurrencyUtils = new CurrencyUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appContext = getApplicationContext();
        //components variables assignment
        etUSD = findViewById(R.id.editTextUSD);
        etEUR = findViewById(R.id.editTextEUR);
        etPLN = findViewById(R.id.editTextPLN);
        btnClear = findViewById(R.id.buttonClear);
        btnConvert = findViewById(R.id.buttonConvert);
        tvUSD2PLN = findViewById(R.id.tvUSD2PLN);
        tvUSD2EUR = findViewById(R.id.tvUSD2EUR);
        tvEUR2PLN = findViewById(R.id.tvEUR2PLN);
        spinnerFirstCurrency = findViewById(R.id.spinnerFirstCurrency);
        spinnerSecondCurrency = findViewById(R.id.spinnerSecondCurrency);
        etFirstCurrencyValue = findViewById(R.id.editTextFirstCurrencyValue);
        etSecondCurrencyValue = findViewById(R.id.editTextSecondCurrencyValue);
        btnClear2 = findViewById(R.id.buttonClear2);
        btnConvert2 = findViewById(R.id.buttonConvert2);

        //assignment of listeners
        btnClear.setOnClickListener(this);
        btnConvert.setOnClickListener(this);
        btnClear2.setOnClickListener(this);
        btnConvert2.setOnClickListener(this);

        //thread responsibility fuses override
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        USD2PLN = mCurrencyUtils.getCurrencyRate("USD", "PLN");
        USD2EUR = mCurrencyUtils.getCurrencyRate("USD", "EUR");
        EUR2PLN = mCurrencyUtils.getCurrencyRate("EUR", "PLN");

        df = new DecimalFormat("#.###");
        tvUSD2PLN.setText(df.format(USD2PLN));
        tvUSD2EUR.setText(df.format(USD2EUR));
        tvEUR2PLN.setText(df.format(EUR2PLN));

        fillSpinners();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonClear:
                etUSD.setText("");
                etEUR.setText("");
                etPLN.setText("");
                mCurrencyUtils.getCurrencyRate("USD", "PLN");
                break;
            case R.id.buttonConvert:
                if (etUSD.getText().toString().trim().length() > 0) {
                    double USD = Double.parseDouble(etUSD.getText().toString());
                    double PLN = mCurrencyUtils.getConvertedCurrency("USD", USD, "PLN");
                    double EUR = mCurrencyUtils.getConvertedCurrency("USD", USD, "EUR");
                    etPLN.setText(Double.toString(PLN));
                    etEUR.setText(Double.toString(EUR));
                } else if (etEUR.getText().toString().trim().length() > 0) {
                    double EUR = Double.parseDouble(etEUR.getText().toString());
                    double PLN = mCurrencyUtils.getConvertedCurrency("EUR", EUR, "PLN");
                    double USD = mCurrencyUtils.getConvertedCurrency("EUR", EUR, "USD");
                    etUSD.setText(Double.toString(USD));
                    etPLN.setText(Double.toString(PLN));
                } else if (etPLN.getText().toString().trim().length() > 0) {
                    double PLN = Double.parseDouble(etPLN.getText().toString());
                    double EUR = mCurrencyUtils.getConvertedCurrency("PLN", PLN, "EUR");
                    double USD =  mCurrencyUtils.getConvertedCurrency("PLN", PLN, "USD");
                    etUSD.setText(Double.toString(USD));
                    etEUR.setText(Double.toString(EUR));
                } else {
                    Toast.makeText(appContext, "Enter value", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.buttonClear2:
                etFirstCurrencyValue.setText("");
                etSecondCurrencyValue.setText("");
                break;
            case R.id.buttonConvert2:
                String currencyFirst = spinnerFirstCurrency.getSelectedItem().toString();
                String currencySecond = spinnerSecondCurrency.getSelectedItem().toString();
                if (etFirstCurrencyValue.getText().toString().trim().length() > 0) {
                    double first = Double.parseDouble(etFirstCurrencyValue.getText().toString());
                    double second = mCurrencyUtils.getConvertedCurrency(currencyFirst, first, currencySecond);
                    etSecondCurrencyValue.setText(df.format(second));
                } else if (etSecondCurrencyValue.getText().toString().trim().length() > 0) {
                    double second = Double.parseDouble(etSecondCurrencyValue.getText().toString());
                    double first = mCurrencyUtils.getConvertedCurrency(currencySecond, second, currencyFirst);
                    etFirstCurrencyValue.setText(df.format(first));
                }

                break;
        }
    }

    void fillSpinners() {
        List<String> listRates = mCurrencyUtils.getAvailableCurrencies();
        ArrayAdapter<String> aa = new ArrayAdapter<>(appContext, android.R.layout.simple_spinner_dropdown_item, listRates);
        spinnerFirstCurrency.setAdapter(aa);
        spinnerSecondCurrency.setAdapter(aa);
    }

}
