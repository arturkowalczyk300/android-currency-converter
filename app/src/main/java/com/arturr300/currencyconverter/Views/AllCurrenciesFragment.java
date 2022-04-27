package com.arturr300.currencyconverter.Views;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.arturr300.currencyconverter.R;
import com.arturr300.currencyconverter.ViewModels.ConversionResult;
import com.arturr300.currencyconverter.ViewModels.CurrenciesRateFetchingResult;
import com.arturr300.currencyconverter.ViewModels.ExchangeRatesViewModel;
import com.arturr300.currencyconverter.ViewModels.ExchangeRatesViewModelFactory;

import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

public class AllCurrenciesFragment extends Fragment {
    ExchangeRatesViewModel viewModel;
    AppCompatActivity mainLifecycleOwner;

    final String settingsFileName = "mySettings";
    //second part
    Spinner spinnerSourceCurrency;
    Spinner spinnerTargetCurrency;
    ArrayAdapter<String> spinnerSourceCurrenciesListAdapter;
    ArrayAdapter<String> spinnerTargetCurrenciesListAdapter;
    EditText etSourceCurrencyValue;
    EditText etTargetCurrencyValue;
    Button btnClear;
    Button btnConvert;
    Button btnReverse;

    TextView textViewRate;
    TextView textViewRateDebugInfo;

    String lastSourceCurrencyInRequest;

    DecimalFormat df;

    boolean isApiWorking;
    TreeMap<String, Double> newestCurrenciesRates;
    List<String> currenciesList;

    LiveData<ConversionResult> conversionResult;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        df = new DecimalFormat("#.###");
    }

    void saveSpinnerValue() {
        String currencySource = spinnerSourceCurrency.getSelectedItem().toString();
        String currencyTarget = spinnerTargetCurrency.getSelectedItem().toString();
        SharedPreferences.Editor editor = getActivity().getApplicationContext().
                getSharedPreferences(settingsFileName, Context.MODE_PRIVATE).edit();
        editor.putString("currencySource", currencySource);
        editor.putString("currencyTarget", currencyTarget);
        editor.apply();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_select, container, false);
        spinnerSourceCurrency = view.findViewById(R.id.fragmentListSelectSpinnerSourceCurrency);
        spinnerTargetCurrency = view.findViewById(R.id.fragmentListSelectSpinnerTargetCurrency);
        etSourceCurrencyValue = view.findViewById(R.id.fragmentListSelectEditTextSourceCurrencyValue);
        etTargetCurrencyValue = view.findViewById(R.id.fragmentListSelectEditTextTargetCurrencyValue);
        btnClear = view.findViewById(R.id.fragmentListSelectButtonClear);
        btnConvert = view.findViewById(R.id.fragmentListSelectButtonConvert);
        btnReverse = view.findViewById(R.id.fragmentListSelectButtonReverse);

        textViewRate = view.findViewById(R.id.fragmentListSelectTextViewRate);
        textViewRateDebugInfo = view.findViewById(R.id.fragmentListSelectTextViewRateDebugInfo);
        newestCurrenciesRates = new TreeMap<String, Double>();

        //viewmodel
        ExchangeRatesViewModelFactory viewModelFactory = new ExchangeRatesViewModelFactory(requireActivity().getApplication());
        viewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(ExchangeRatesViewModel.class);

        //observe live data objects
        viewModel.isApiWorking().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                isApiWorking = aBoolean.booleanValue();
            }
        });
        viewModel.getCurrenciesList().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                currenciesList = strings;
                fillSpinners();
            }
        });

        //add button listeners
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSourceCurrencyValue.setText("");
                etTargetCurrencyValue.setText("");
            }
        });

        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currencySource = spinnerSourceCurrency.getSelectedItem().toString();
                String currencyTarget = spinnerTargetCurrency.getSelectedItem().toString();
                if (etSourceCurrencyValue.getText().toString().trim().length() > 0) {
                    double sourceAmount = Double.parseDouble(etSourceCurrencyValue.getText().toString());
                    conversionResult = viewModel.convertCurrency(currencySource, sourceAmount, currencyTarget);
                    lastSourceCurrencyInRequest = currencySource; //todo: refactor
                    conversionResult.observe(mainLifecycleOwner, new Observer<ConversionResult>() {
                        @Override
                        public void onChanged(ConversionResult conversionResult) {
                            etTargetCurrencyValue.setText(df.format(conversionResult.resultAmount));
                        }
                    });
                }
            }
        });

        btnReverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedBase = spinnerSourceCurrency.getSelectedItem().toString();
                String selectedTarget = spinnerTargetCurrency.getSelectedItem().toString();

                try {
                    spinnerSourceCurrency.setSelection(spinnerSourceCurrenciesListAdapter.getPosition(selectedTarget));
                    spinnerTargetCurrency.setSelection(spinnerTargetCurrenciesListAdapter.getPosition(selectedBase));
                } catch (Exception ex) {
                    Log.e("myApp", ex.toString());
                }
            }
        });

        AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                saveSpinnerValue();
                String stringSource = spinnerSourceCurrency.getSelectedItem().toString();
                String stringTarget = spinnerTargetCurrency.getSelectedItem().toString();
                lastSourceCurrencyInRequest = stringSource; //todo: refactor
                viewModel.getCurrencyRate(stringSource,
                        stringTarget)
                        .observe(mainLifecycleOwner, new Observer<CurrenciesRateFetchingResult>() {
                            @Override
                            public void onChanged(CurrenciesRateFetchingResult currenciesRateFetchingResult) {
                                if (currenciesRateFetchingResult.rate == null) {
                                    Log.e("myApp", "currenciesRateFetchingResult.rate is null!");
                                    return;
                                }

                                if (currenciesRateFetchingResult.rate != Double.MIN_VALUE) //todo: refactor
                                    fillRateValue(currenciesRateFetchingResult);
                            }
                        });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        spinnerSourceCurrency.setOnItemSelectedListener(onItemSelectedListener);
        spinnerTargetCurrency.setOnItemSelectedListener(onItemSelectedListener);
        return view;
    }

    void fillRateValue(CurrenciesRateFetchingResult currencyConversionRate) {
        String conversionResultText = Double.toString(currencyConversionRate.rate);
        textViewRate.setText(conversionResultText);
        StringBuilder stringBuilder = new StringBuilder()
                .append("source=")
                .append(currencyConversionRate.sourceCurrency)
                .append(",target=")
                .append(currencyConversionRate.targetCurrency);

        textViewRateDebugInfo.setText(stringBuilder.toString());
    }

    void fillSpinners() {
        if (currenciesList == null || currenciesList.isEmpty()) {
            Toast.makeText(getActivity().getApplicationContext(), "Getting available currencies failed!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (spinnerSourceCurrenciesListAdapter == null || spinnerTargetCurrenciesListAdapter == null) {
            spinnerSourceCurrenciesListAdapter = new ArrayAdapter<>
                    (getActivity()
                            .getApplicationContext(),
                            android.R.layout.simple_spinner_dropdown_item, currenciesList);

            spinnerTargetCurrenciesListAdapter = new ArrayAdapter<>
                    (getActivity()
                            .getApplicationContext(),
                            android.R.layout.simple_spinner_dropdown_item, currenciesList);

            spinnerSourceCurrency.setAdapter(spinnerSourceCurrenciesListAdapter);
            spinnerTargetCurrency.setAdapter(spinnerTargetCurrenciesListAdapter);

            SharedPreferences settings = getActivity().getApplicationContext().getSharedPreferences(settingsFileName, Context.MODE_PRIVATE);
            String currencySource = settings.getString("currencySource", "AUD");
            String currencyTarget = settings.getString("currencyTarget", "AUD");
            selectSpinnerItemByValue(spinnerSourceCurrency, currencySource);
            selectSpinnerItemByValue(spinnerTargetCurrency, currencyTarget);
        } else {
            spinnerSourceCurrenciesListAdapter.clear();
            spinnerSourceCurrenciesListAdapter.addAll(currenciesList);
            spinnerSourceCurrenciesListAdapter.notifyDataSetChanged();

            spinnerTargetCurrenciesListAdapter.clear();
            spinnerTargetCurrenciesListAdapter.add(lastSourceCurrencyInRequest);
            spinnerTargetCurrenciesListAdapter.addAll(currenciesList);
            spinnerTargetCurrenciesListAdapter.sort(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return o1.compareToIgnoreCase(o2);
                }
            });
            spinnerTargetCurrenciesListAdapter.notifyDataSetChanged();
        }
    }

    void selectSpinnerItemByValue(Spinner spin, String value) {
        SpinnerAdapter adapter = spin.getAdapter();
        for (int pos = 0; pos < adapter.getCount(); pos++) {
            String item = (String) adapter.getItem(pos);
            if (item.equals(value)) {
                spin.setSelection(pos);
                return;
            }
        }
    }

    public void setMainLifecycleOwner(AppCompatActivity mainLifecycleOwner) {
        this.mainLifecycleOwner = mainLifecycleOwner;
    }

}