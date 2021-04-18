package com.arturr300.currencyconverter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

public class ListSelect extends Fragment {
    final String settingsFileName = "mySettings";
    //second part
    Spinner spinnerFirstCurrency;
    Spinner spinnerSecondCurrency;
    EditText etFirstCurrencyValue;
    EditText etSecondCurrencyValue;
    Button btnClear2;
    Button btnConvert2;

    TextView textViewRate1To2;
    TextView textViewRate2To1;

    DecimalFormat df;
    CurrencyUtils mCurrencyUtils = new CurrencyUtils();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        df = new DecimalFormat("#.###");
    }

    void saveSpinnerValue()
    {
        String currencyFirst = spinnerFirstCurrency.getSelectedItem().toString();
        String currencySecond = spinnerSecondCurrency.getSelectedItem().toString();
        SharedPreferences.Editor editor = getActivity().getApplicationContext().getSharedPreferences(settingsFileName, Context.MODE_PRIVATE).edit();
        editor.putString("currencyFirst", currencyFirst);
        editor.putString("currencySecond", currencySecond);
        editor.apply();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_select, container, false);
        spinnerFirstCurrency = view.findViewById(R.id.spinnerFirstCurrency);
        spinnerSecondCurrency = view.findViewById(R.id.spinnerSecondCurrency);
        etFirstCurrencyValue = view.findViewById(R.id.editTextFirstCurrencyValue);
        etSecondCurrencyValue = view.findViewById(R.id.editTextSecondCurrencyValue);
        btnClear2 = view.findViewById(R.id.buttonClear2);
        btnConvert2 = view.findViewById(R.id.buttonConvert2);

        textViewRate1To2 = view.findViewById(R.id.textViewRate1To2);
        textViewRate2To1 = view.findViewById(R.id.textViewRate2To1);

        btnClear2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etFirstCurrencyValue.setText("");
                etSecondCurrencyValue.setText("");
            }
        });

        btnConvert2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });

        fillSpinners();

        spinnerFirstCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                saveSpinnerValue();
                fillRateValue();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerSecondCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                saveSpinnerValue();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }
    void fillRateValue()
    {
        assert(spinnerFirstCurrency.getSelectedItem().toString().length()>0 && spinnerSecondCurrency.getSelectedItem().toString().length()>0);
       textViewRate1To2.setText(Double.toString(mCurrencyUtils.getCurrencyRate(spinnerFirstCurrency.getSelectedItem().toString(), spinnerSecondCurrency.getSelectedItem().toString()) ));
        textViewRate2To1.setText(Double.toString(mCurrencyUtils.getCurrencyRate(spinnerSecondCurrency.getSelectedItem().toString(), spinnerFirstCurrency.getSelectedItem().toString()) ));
    }
    void fillSpinners() {
        List<String> listRates = mCurrencyUtils.getAvailableCurrencies();
        if(listRates == null)
        {
            Toast.makeText(getActivity().getApplicationContext(), "Getting available currencies failed!", Toast.LENGTH_SHORT).show();
            return;
        }
        listRates.add("EUR"); //it is base currency, so rates JSONObject not contains it ; it must be added manually
        Collections.sort(listRates);
        ArrayAdapter<String> aa = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listRates);
        spinnerFirstCurrency.setAdapter(aa);
        spinnerSecondCurrency.setAdapter(aa);

        SharedPreferences settings = getActivity().getApplicationContext().getSharedPreferences(settingsFileName, Context.MODE_PRIVATE);
        String currencyFirst = settings.getString("currencyFirst", "AUD");
        String currencySecond = settings.getString("currencySecond", "AUD");
        selectSpinnerItemByValue(spinnerFirstCurrency, currencyFirst);
        selectSpinnerItemByValue(spinnerSecondCurrency, currencySecond);

    }

    void selectSpinnerItemByValue(Spinner spin, String value)
    {
        SpinnerAdapter adapter = spin.getAdapter();
        for(int pos=0; pos<adapter.getCount(); pos++)
        {
            String item = (String)adapter.getItem(pos);
            if(item.equals(value))
            {
                spin.setSelection(pos);
                return;
            }
        }
    }
}