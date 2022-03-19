package com.arturr300.currencyconverter.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.arturr300.currencyconverter.Models.ExchangeRatesRepository;

import java.util.List;

public class ExchangeRatesViewModel extends ViewModel {
    private ExchangeRatesRepository repository;

    public ExchangeRatesViewModel(@NonNull Application application) {
        repository = new ExchangeRatesRepository();
    }

    public boolean testAPI() {
        return repository.testAPI();
    }

    public List<String> getAvailableCurrencies() {
        return repository.getAvailableCurrencies();
    }

    public double getCurrencyRate(String base, String target) {
        return repository.getCurrencyRate(base, target);
    }

    public double getConvertedCurrency(String baseCurrency, double baseValue, String targetCurrency) {
        return repository.getConvertedCurrency(baseCurrency, baseValue, targetCurrency);
    }
}
