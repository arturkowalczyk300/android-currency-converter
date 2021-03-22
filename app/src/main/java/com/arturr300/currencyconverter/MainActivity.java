package com.arturr300.currencyconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //components variables 
    EditText etUSD;
    EditText etEUR;
    EditText etPLN;
    Button btnClear;
    Button btnConvert;



    //other variables



    final float USD2PLN = 3.89f;
    final float USD2EUR = 0.84f;
    final float EUR2PLN = 4.62f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //components variables assignment
        etUSD = (EditText)findViewById(R.id.editTextUSD);
        etEUR = (EditText)findViewById(R.id.editTextEUR);
        etPLN = (EditText)findViewById(R.id.editTextPLN);
        btnClear = (Button) findViewById(R.id.buttonClear);
        btnConvert = (Button) findViewById(R.id.buttonConvert);

        //assignment of listeners
        btnClear.setOnClickListener(this);
        btnConvert.setOnClickListener(this);

        //thread responsibility fuses override
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.buttonClear:
                etUSD.setText("");
                etEUR.setText("");
                etPLN.setText("");
                getCurrencyRateFromAPI("USD", "");
                break;
            case R.id.buttonConvert:

                if(etUSD.getText().toString().trim().length()>0)
                {
                    float USD = Float.parseFloat(etUSD.getText().toString());
                    float PLN = USD * USD2PLN;
                    float EUR = USD * USD2EUR;
                    etPLN.setText(Float.toString(PLN));
                    etEUR.setText(Float.toString(EUR));
                }
                else if(etEUR.getText().toString().trim().length()>0)
            {
                float EUR = Float.parseFloat(etEUR.getText().toString());
                float PLN = EUR * EUR2PLN;
                float USD = EUR * (1.0f/USD2EUR);
                etUSD.setText(Float.toString(USD));
                etPLN.setText(Float.toString(PLN));
            }
                else if(etPLN.getText().toString().trim().length()>0)
                {
                    float PLN = Float.parseFloat(etPLN.getText().toString());
                    float EUR = PLN * (1.0f/EUR2PLN);
                    float USD = PLN * (1.0f/USD2PLN);
                    etUSD.setText(Float.toString(USD));
                    etEUR.setText(Float.toString(EUR));
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Enter value", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    float getCurrencyRateFromAPI(String base, String target)
    {
        String url1 = "https://api.exchangeratesapi.io/latest?base=" + base;
        String jsonStr = "";
        try {
            URL url2 = new URL(url1);
            BufferedReader br = new BufferedReader(new InputStreamReader(url2.openStream()));
            String output="";
            String line="";
            while((line=br.readLine())!=null)
            {
                output+=line;
            }
            Toast.makeText(getApplicationContext(), output, Toast.LENGTH_LONG).show();

        }
        catch(IOException e)
        {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
        return 0.0f;
    }
}
