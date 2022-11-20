package com.arturr300.currencyconverter.Views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.arturr300.currencyconverter.R;

class SourceTargetCurrencyFragment extends Fragment {

    enum INPUT_TYPE {
        SOURCE,
        TARGET
    }

    private INPUT_TYPE inputType;
    private View currentView;

    public SourceTargetCurrencyFragment(INPUT_TYPE inputType) {
        this.inputType = inputType;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        currentView = inflater.inflate(R.layout.fragment_source_target_currency, container, true);

        String titleText = "";
        if (inputType == INPUT_TYPE.SOURCE)
            titleText = "Source currency:";
        else if (inputType == INPUT_TYPE.TARGET) {
            titleText = "Target currency:";
        }
        ((TextView) currentView.findViewById(R.id.textViewFragmentTitle)).setText(titleText);


        return currentView;
    }

    public INPUT_TYPE getInputType() {
        return inputType;
    }
}

