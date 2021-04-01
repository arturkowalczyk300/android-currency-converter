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

        USD2PLN = getCurrencyRateFromAPI("USD", "PLN");
        USD2EUR = getCurrencyRateFromAPI("USD", "EUR");
        EUR2PLN = getCurrencyRateFromAPI("EUR", "PLN");

        DecimalFormat df = new DecimalFormat("#.###");
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
                getCurrencyRateFromAPI("USD", "PLN");
                break;
            case R.id.buttonConvert:
                if (etUSD.getText().toString().trim().length() > 0) {
                    double USD = Double.parseDouble(etUSD.getText().toString());
                    double PLN = USD * USD2PLN;
                    double EUR = USD * USD2EUR;
                    etPLN.setText(Double.toString(PLN));
                    etEUR.setText(Double.toString(EUR));
                } else if (etEUR.getText().toString().trim().length() > 0) {
                    double EUR = Double.parseDouble(etEUR.getText().toString());
                    double PLN = EUR * EUR2PLN;
                    double USD = EUR * (1.0f / USD2EUR);
                    etUSD.setText(Double.toString(USD));
                    etPLN.setText(Double.toString(PLN));
                } else if (etPLN.getText().toString().trim().length() > 0) {
                    double PLN = Double.parseDouble(etPLN.getText().toString());
                    double EUR = PLN * (1.0f / EUR2PLN);
                    double USD = PLN * (1.0f / USD2PLN);
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
                Toast.makeText(appContext, "first:"+currencyFirst+" second:"+currencySecond, Toast.LENGTH_SHORT).show();
                if (etFirstCurrencyValue.getText().toString().trim().length() > 0) {
                    double first = Double.parseDouble(etFirstCurrencyValue.getText().toString());
                    double second = first * getCurrencyRateFromAPI(currencyFirst, currencySecond);
                    Toast.makeText(appContext, "val,first:"+Double.toString(first)+" second:"+Double.toString(second), Toast.LENGTH_SHORT).show();
                    etSecondCurrencyValue.setText(Double.toString(second));
                } else if (etSecondCurrencyValue.getText().toString().trim().length() > 0) {
                    double second = Double.parseDouble(etSecondCurrencyValue.getText().toString());
                    double first = second* getCurrencyRateFromAPI(currencySecond, currencyFirst);
                    etFirstCurrencyValue.setText(Double.toString(first));
                }

                break;
        }
    }

    void fillSpinners()
    {
        double jTarget;
        //String url1 = "https://api.exchangeratesapi.io/latest?base=" + base;
        String url3 = "https://api.frankfurter.app/latest";
        try {
            URL url2 = new URL(url3);
            BufferedReader br = new BufferedReader(new InputStreamReader(url2.openStream()));
            String output = "";
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            output += sb.toString();
            //Toast.makeText(getApplicationContext(), output, Toast.LENGTH_LONG).show();
            JSONObject jMain = new JSONObject(output);
            //String jBase = jMain.getString("base");
            //String jDate = jMain.getString("date");
            JSONObject jRates = jMain.getJSONObject("rates");
            //todo: create here list with rates
            List<String> listRates = new ArrayList<>();
            for (Iterator<String> iterator = jRates.keys(); iterator.hasNext(); ) {
                String key = iterator.next();
                listRates.add(key);
            }
            //fill spinners
            ArrayAdapter<String> aa = new ArrayAdapter<>(appContext, android.R.layout.simple_spinner_dropdown_item, listRates);
            spinnerFirstCurrency.setAdapter(aa);
            spinnerSecondCurrency.setAdapter(aa);
        }
        catch (IOException | JSONException e) {
            Toast.makeText(appContext, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    double getCurrencyRateFromAPI(String base, String target) {
        double jTarget;
        //String url1 = "https://api.exchangeratesapi.io/latest?base=" + base;
        String url1 = "https://api.frankfurter.app/latest?from=" + base;
        try {
            URL url2 = new URL(url1);
            BufferedReader br = new BufferedReader(new InputStreamReader(url2.openStream()));
            String output = "";
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            output += sb.toString();
            //Toast.makeText(getApplicationContext(), output, Toast.LENGTH_LONG).show();
            JSONObject jMain = new JSONObject(output);
            //String jBase = jMain.getString("base");
            //String jDate = jMain.getString("date");
            JSONObject jRates = jMain.getJSONObject("rates");
            //todo: create here list with rates
            List<String> listRates = new ArrayList<>();
            for (Iterator<String> iterator = jRates.keys(); iterator.hasNext(); ) {
                String key = iterator.next();
                listRates.add(key);
            }
            //fill spinners
            ArrayAdapter<String> aa = new ArrayAdapter<>(appContext, android.R.layout.simple_spinner_dropdown_item, listRates);
            //spinnerFirstCurrency.setAdapter(aa);
            //spinnerSecondCurrency.setAdapter(aa);
            //Toast.makeText(appContext, listRates.toString(), Toast.LENGTH_SHORT).show();
            jTarget = jRates.getDouble(target);
            //String toastStr = "base=" + jBase + ", target{" + target + "}=" + jTarget;
            //Toast.makeText(appContext, toastStr, Toast.LENGTH_LONG).show();
            return jTarget;
        } catch (IOException | JSONException e) {
            Toast.makeText(appContext, e.toString(), Toast.LENGTH_LONG).show();
            return -1.0f;
        }
    }
}
