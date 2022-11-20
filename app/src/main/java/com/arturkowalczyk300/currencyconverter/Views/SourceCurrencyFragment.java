package com.arturkowalczyk300.currencyconverter.Views;

import static androidx.fragment.app.FragmentKt.setFragmentResult;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SourceCurrencyFragment extends SourceTargetCurrencyFragment {

    public static final String FRAGMENT_REQUEST_KEY = "com.arturkowalczyk300.currencyconverter.FRAGMENT_REQUEST_KEY_SOURCE";
    public static final String BUNDLE_VIEW_CREATED = "com.arturkowalczyk300.currencyconverter.BUNDLE_VIEW_CREATED_SOURCE";

    public SourceCurrencyFragment() {
        super(INPUT_TYPE.SOURCE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(this.BUNDLE_VIEW_CREATED, true);

        setFragmentResult(this, FRAGMENT_REQUEST_KEY, bundle);

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
