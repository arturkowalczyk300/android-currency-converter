package com.arturr300.currencyconverter;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MostUsed extends Fragment {

    //components variables
    EditText etUSD;
    EditText etEUR;
    EditText etPLN;
    Button btnClear;
    Button btnConvert;
    DecimalFormat df;
    CurrencyUtils mCurrencyUtils = new CurrencyUtils();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        df = new DecimalFormat("#.###");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //components variables assignment
        View view=inflater.inflate(R.layout.fragment_most_used, container, false);
        etUSD = view.findViewById(R.id.editTextUSD);
        etEUR = view.findViewById(R.id.editTextEUR);
        etPLN = view.findViewById(R.id.editTextPLN);
        btnClear = view.findViewById(R.id.buttonClear);
        btnConvert = view.findViewById(R.id.buttonConvert);
btnClear.setOnClickListener(new View.OnClickListener()
{
    @Override
    public void onClick(View v) {
        etUSD.setText("");
        etEUR.setText("");
        etPLN.setText("");
        if( mCurrencyUtils.getCurrencyRate("USD", "PLN") ==  -100.0f)
            ((MainActivity)getActivity()).showNetworkErrorScreen();
    }
});
        btnConvert.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(!mCurrencyUtils.testAPI())
                    ((MainActivity)getActivity()).showNetworkErrorScreen();

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
                    Toast.makeText(getActivity().getApplicationContext(), "Enter value", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Inflate the layout for this fragment
        return view;
    }
}