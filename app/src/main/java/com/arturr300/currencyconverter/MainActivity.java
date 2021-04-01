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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
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
        etUSD = (EditText) findViewById(R.id.editTextUSD);
        etEUR = (EditText) findViewById(R.id.editTextEUR);
        etPLN = (EditText) findViewById(R.id.editTextPLN);
        btnClear = (Button) findViewById(R.id.buttonClear);
        btnConvert = (Button) findViewById(R.id.buttonConvert);
        tvUSD2PLN = (TextView) findViewById(R.id.tvUSD2PLN);
        tvUSD2EUR = (TextView) findViewById(R.id.tvUSD2EUR);
        tvEUR2PLN = (TextView) findViewById(R.id.tvEUR2PLN);
        spinnerFirstCurrency = (Spinner)findViewById(R.id.spinnerFirstCurrency);
         spinnerSecondCurrency= (Spinner)findViewById(R.id.spinnerSecondCurrency);
         etFirstCurrencyValue= (EditText) findViewById(R.id.editTextFirstCurrencyValue);
         etSecondCurrencyValue= (EditText)findViewById(R.id.editTextSecondCurrencyValue);
         btnClear2= (Button) findViewById(R.id.buttonClear2);
        btnConvert2= (Button) findViewById(R.id.buttonConvert2);

        //assignment of listeners
        btnClear.setOnClickListener(this);
        btnConvert.setOnClickListener(this);

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
                Toast.makeText(appContext, "Not available", Toast.LENGTH_LONG).show();
                break;
        }
    }

    double getCurrencyRateFromAPI(String base, String target) {
        double jTarget;
        //String url1 = "https://api.exchangeratesapi.io/latest?base=" + base;
        String url1 = "https://api.frankfurter.app/latest?from=" + base;
        String jsonStr = "";
        try {
            URL url2 = new URL(url1);
            BufferedReader br = new BufferedReader(new InputStreamReader(url2.openStream()));
            String output = "";
            String line = "";
            while ((line = br.readLine()) != null) {
                output += line;
            }
            //Toast.makeText(getApplicationContext(), output, Toast.LENGTH_LONG).show();
            JSONObject jMain = new JSONObject(output);
            String jBase = jMain.getString("base");
            String jDate = jMain.getString("date");
            JSONObject jRates = jMain.getJSONObject("rates");
            //todo: create here list with rates
            List listRates = new ArrayList<String>();
            for(Iterator<String> iter = jRates.keys();iter.hasNext();) {
                String key = iter.next();
                listRates.add(key);
            }
            //fill spinners
            ArrayAdapter<String> aa = new ArrayAdapter<>(appContext, android.R.layout.simple_spinner_dropdown_item, listRates);
            spinnerFirstCurrency.setAdapter(aa);
            spinnerSecondCurrency.setAdapter(aa);
            Toast.makeText(appContext, listRates.toString(), Toast.LENGTH_SHORT).show();
            jTarget = jRates.getDouble(target);
            String strr = "base=" + jBase + ", target{" + target + "}=" + jTarget;
            //Toast.makeText(appContext, strr, Toast.LENGTH_LONG).show();
            return jTarget;
        } catch (IOException | JSONException e) {
            Toast.makeText(appContext, e.toString(), Toast.LENGTH_LONG).show();
            return -1.0f;
        }
    }
}
