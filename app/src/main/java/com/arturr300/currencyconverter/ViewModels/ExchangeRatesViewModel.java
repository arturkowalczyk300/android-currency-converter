package com.arturr300.currencyconverter.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.arturr300.currencyconverter.Models.ExchangeRatesRepository;

import java.util.List;

public class ExchangeRatesViewModel extends ViewModel {
    private ExchangeRatesRepository repository;
    private MutableLiveData<List<String>> mutableLiveDataAvailableCurrencies;

    public ExchangeRatesViewModel(@NonNull Application application) {
        repository = new ExchangeRatesRepository();
        mutableLiveDataAvailableCurrencies = new MutableLiveData<>();
    }

    public LiveData<Boolean> getApiWorking() {
        return repository.getApiWorking();
    }

    public LiveData<List<String>> getAvailableCurrencies() {
        return repository.getAvailableCurrencies();
    }

    public double getCurrencyRate(String base, String target) {
        return repository.getCurrencyRate(base, target);
    }

    public double getConvertedCurrency(String baseCurrency, double baseValue, String targetCurrency) {
        return repository.getConvertedCurrency(baseCurrency, baseValue, targetCurrency);
    }

    public void refreshData()
    {
        repository.refreshData();
    }
}
