package com.arturr300.currencyconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //components variables
    EditText etUSD;
    EditText etEUR;
    EditText etPLN;
    Button btnClear;
    Button btnConvert;

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
                boolean enteredUSD
                break;
        }
    }
}