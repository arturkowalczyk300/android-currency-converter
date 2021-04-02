package com.arturr300.currencyconverter;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.DecimalFormat;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListSelect#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListSelect extends Fragment {
    //second part
    Spinner spinnerFirstCurrency;
    Spinner spinnerSecondCurrency;
    EditText etFirstCurrencyValue;
    EditText etSecondCurrencyValue;
    Button btnClear2;
    Button btnConvert2;
    DecimalFormat df;
    CurrencyUtils mCurrencyUtils = new CurrencyUtils();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListSelect.
     */
    // TODO: Rename and change types and number of parameters
    public static ListSelect newInstance(String param1, String param2) {
        ListSelect fragment = new ListSelect();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ListSelect() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        df = new DecimalFormat("#.###");

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
        return view;
    }
    void fillSpinners() {
        List<String> listRates = mCurrencyUtils.getAvailableCurrencies();
        ArrayAdapter<String> aa = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listRates);
        spinnerFirstCurrency.setAdapter(aa);
        spinnerSecondCurrency.setAdapter(aa);
    }
}