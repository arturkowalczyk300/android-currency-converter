package com.arturr300.currencyconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //components variables 
    EditText etUSD;
    EditText etEUR;
    EditText etPLN;
    Button btnClear;
    Button btnConvert;

    //other variables
    final float USD2PLN = (1.0f/3.89f);
    final float USD2EUR = (1.0f/0.84f);
    final float EUR2PLN = (1.0f/4.62f);

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
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.buttonClear:
                etUSD.setText("");
                etEUR.setText("");
                etPLN.setText("");
                break;
            case R.id.buttonConvert:

                if(etUSD.getText().toString().trim().length()>0)
                {
                    float USD = Float.parseFloat(etUSD.getText().toString());
                    float PLN = USD * (1.0f/USD2PLN);
                    float EUR = USD * (1.0f/USD2EUR);
                    etPLN.setText(Float.toString(PLN));
                    etEUR.setText(Float.toString(EUR));
                }
                else if(etEUR.getText().toString().trim().length()>0)
            {
                float EUR = Float.parseFloat(etEUR.getText().toString());
                float PLN = EUR * (1.0f/EUR2PLN);
                float USD = EUR * (USD2EUR);
                etUSD.setText(Float.toString(USD));
                etPLN.setText(Float.toString(PLN));
            }
                else if(etPLN.getText().toString().trim().length()>0)
                {
                    float PLN = Float.parseFloat(etPLN.getText().toString());
                    float EUR = PLN * (EUR2PLN);
                    float USD = PLN * (USD2PLN);
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
}