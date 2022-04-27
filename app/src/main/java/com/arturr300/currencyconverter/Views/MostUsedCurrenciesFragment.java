package com.arturr300.currencyconverter.Views;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.Toast;

import com.arturr300.currencyconverter.R;
import com.arturr300.currencyconverter.ViewModels.ConversionResult;
import com.arturr300.currencyconverter.ViewModels.CurrenciesRateFetchingResult;
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
                //check validity of results
                for (ConversionResult conversionResult : conversionResults) {
                    if (conversionResult.rate == ConversionResult.ERROR_VALUE) {
                        return;
                    }
                }

                fillFields(conversionResults);
            }
        });


    }

    private void fillFields(List<ConversionResult> conversionResultsList) {
        class TargetCurrency {
            public String name;
            public double rate;
            public EditText layoutView;

            public TargetCurrency(String name, double rate, EditText layoutView) {
                this.name = name;
                this.rate = rate;
                this.layoutView = layoutView;
            }

            public String getDump() {
                StringBuilder sb = new StringBuilder();
                sb.append("name=");
                sb.append(name);
                sb.append(", rate=");
                sb.append(rate);
                sb.append(", layoutViewID=");
                sb.append(layoutView.getId());

                return sb.toString();
            }
        }

        double ERROR_VALUE = CurrenciesRateFetchingResult.ERROR_VALUE;

        List<TargetCurrency> listOfTargetCurrencies = Arrays.asList(
                new TargetCurrency("USD", ERROR_VALUE, etUSD),
                new TargetCurrency("EUR", ERROR_VALUE, etEUR),
                new TargetCurrency("PLN", ERROR_VALUE, etPLN));

        //main part
        for (TargetCurrency targetCurrency : listOfTargetCurrencies) {
            if (targetCurrency.name != baseCurrency) {

                int index=-1;
                for (ConversionResult conversionResult : conversionResultsList) {
                    if (conversionResult.targetCurrency == targetCurrency.name) {
                        index = conversionResultsList.indexOf(conversionResult);
                        continue;
                    }
                }

                if (index < 0) {
                    return;
                }
                targetCurrency.rate = conversionResultsList.get(index).resultAmount;
                targetCurrency.layoutView.setText(Double.toString(targetCurrency.rate));
            }
        }
    }

    public void setMainLifecycleOwner(AppCompatActivity mainLifecycleOwner) {
        this.mainLifecycleOwner = mainLifecycleOwner;
    }
}