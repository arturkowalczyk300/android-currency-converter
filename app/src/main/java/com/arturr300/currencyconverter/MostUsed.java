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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MostUsed#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MostUsed extends Fragment {

    //components variables
    EditText etUSD;
    EditText etEUR;
    EditText etPLN;
    TextView tvUSD2PLN;
    TextView tvUSD2EUR;
    TextView tvEUR2PLN;
    Button btnClear;
    Button btnConvert;
    DecimalFormat df;
    CurrencyUtils mCurrencyUtils = new CurrencyUtils();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MostUsed() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MostUsed.
     */
    // TODO: Rename and change types and number of parameters
    public static MostUsed newInstance(String param1, String param2) {
        MostUsed fragment = new MostUsed();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        //components variables assignment
        View view=inflater.inflate(R.layout.fragment_most_used, container, false);
        etUSD = view.findViewById(R.id.editTextUSD);
        etEUR = view.findViewById(R.id.editTextEUR);
        etPLN = view.findViewById(R.id.editTextPLN);
        btnClear = view.findViewById(R.id.buttonClear);
        btnConvert = view.findViewById(R.id.buttonConvert);
        tvUSD2PLN = view.findViewById(R.id.tvUSD2PLN);
        tvUSD2EUR = view.findViewById(R.id.tvUSD2EUR);
        tvEUR2PLN = view.findViewById(R.id.tvEUR2PLN);
btnClear.setOnClickListener(new View.OnClickListener()
{
    @Override
    public void onClick(View v) {
        etUSD.setText("");
        etEUR.setText("");
        etPLN.setText("");
        mCurrencyUtils.getCurrencyRate("USD", "PLN");
    }
});
        btnConvert.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
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