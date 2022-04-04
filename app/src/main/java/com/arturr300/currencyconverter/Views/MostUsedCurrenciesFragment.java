package com.arturr300.currencyconverter.Views;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.Toast;

import com.arturr300.currencyconverter.R;
import com.arturr300.currencyconverter.ViewModels.ConversionResult;
import com.arturr300.currencyconverter.ViewModels.ExchangeRatesViewModel;
import com.arturr300.currencyconverter.ViewModels.ExchangeRatesViewModelFactory;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MostUsedCurrenciesFragment extends Fragment {
    ExchangeRatesViewModel viewModel;
    AppCompatActivity mainLifecycleOwner;

    //components variables
    EditText etUSD;
    EditText etEUR;
    EditText etPLN;
    Button btnClear;
    Button btnConvert;
    DecimalFormat df;

    boolean isApiWorking;
    String baseCurrency;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        df = new DecimalFormat("#.###");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //components variables assignment
        View view = inflater.inflate(R.layout.fragment_most_used, container, false);
        etUSD = view.findViewById(R.id.editTextUSD);
        etEUR = view.findViewById(R.id.editTextEUR);
        etPLN = view.findViewById(R.id.editTextPLN);
        btnClear = view.findViewById(R.id.buttonClear);
        btnConvert = view.findViewById(R.id.buttonConvert);

        //viewmodel
        ExchangeRatesViewModelFactory viewModelFactory = new ExchangeRatesViewModelFactory(requireActivity().getApplication());
        viewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(ExchangeRatesViewModel.class);

        viewModel.isApiWorking().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                isApiWorking = aBoolean.booleanValue();
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etUSD.setText("");
                etEUR.setText("");
                etPLN.setText("");
            }
        });
        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayConversionResults();
            }
        });
        // Inflate the layout for this fragment


        return view;
    }

    private void displayConversionResults() {
        ArrayList<String> targetCurrencies = new ArrayList<>(Arrays.asList());
        baseCurrency = "";
        Double sourceAmount = Double.MIN_VALUE;
        if (etUSD.getText().toString().trim().length() > 0) {
            baseCurrency = "USD";
            sourceAmount = Double.parseDouble(etUSD.getText().toString());
            targetCurrencies = new ArrayList<>(Arrays.asList("EUR", "PLN"));
        } else if (etEUR.getText().toString().trim().length() > 0) {
            baseCurrency = "EUR";
            sourceAmount = Double.parseDouble(etEUR.getText().toString());
            targetCurrencies = new ArrayList<>(Arrays.asList("USD", "PLN"));
        } else if (etPLN.getText().toString().trim().length() > 0) {
            baseCurrency = "PLN";
            sourceAmount = Double.parseDouble(etPLN.getText().toString());
            targetCurrencies = new ArrayList<>(Arrays.asList("USD", "EUR"));
        }

        viewModel.convertMultipleCurrencies(baseCurrency, sourceAmount, targetCurrencies).observe(mainLifecycleOwner, new Observer<List<ConversionResult>>() {
            @Override
            public void onChanged(List<ConversionResult> conversionResults) {
                fillFields(conversionResults);
            }
        });


    }

    private void fillFields(List<ConversionResult> conversionResultsList) {
        if (baseCurrency == "USD") { //todo: remove code repetitions
            double PLN = conversionResultsList.get(conversionResultsList.indexOf("PLN")).resultAmount;
            double EUR = conversionResultsList.get(conversionResultsList.indexOf("EUR")).resultAmount;
            etPLN.setText(Double.toString(PLN));
            etEUR.setText(Double.toString(EUR));
        } else if (baseCurrency == "EUR") {
            double PLN = conversionResultsList.get(conversionResultsList.indexOf("PLN")).resultAmount;
            double USD = conversionResultsList.get(conversionResultsList.indexOf("USD")).resultAmount;
            etUSD.setText(Double.toString(USD));
            etPLN.setText(Double.toString(PLN));
        } else if (baseCurrency == "PLN") {
            double EUR = conversionResultsList.get(conversionResultsList.indexOf("EUR")).resultAmount;
            double USD = conversionResultsList.get(conversionResultsList.indexOf("USD")).resultAmount;
            etUSD.setText(Double.toString(USD));
            etEUR.setText(Double.toString(EUR));
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Enter value", Toast.LENGTH_SHORT).show();
        }
    }

    public void setMainLifecycleOwner(AppCompatActivity mainLifecycleOwner) {
        this.mainLifecycleOwner = mainLifecycleOwner;
    }
}