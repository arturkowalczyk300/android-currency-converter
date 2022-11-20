package com.arturkowalczyk300.currencyconverter.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ExchangeRatesViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    Application application;

    public ExchangeRatesViewModelFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ExchangeRatesViewModel.class))
            return (T) new ExchangeRatesViewModel(this.application);
        return null;
    }


}
