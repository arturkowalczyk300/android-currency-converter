package com.arturr300.currencyconverter.ViewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.arturr300.currencyconverter.Models.ExchangeRatesRepository;
import java.util.TreeMap;

public class ExchangeRatesViewModel extends ViewModel {
    private ExchangeRatesRepository repository;
    private MutableLiveData<TreeMap<String, Double>> currenciesRates;

    public ExchangeRatesViewModel(@NonNull Application application) {
        repository = new ExchangeRatesRepository();
        currenciesRates = getCurrenciesRates();
        refreshData();
    }

    public LiveData<Boolean> getApiWorking() {
        return repository.getApiWorking();
    }

    public MutableLiveData<TreeMap<String, Double>> getCurrenciesRates() {
        return repository.getCurrenciesRates();
    }

    public double getCurrencyRate(String base, String target) {
        try{
            return currenciesRates.getValue().get(target);
        }
        catch(Exception ex)
        {
            Log.e("", ex.toString());
        }
        return -1.0;
    }

    public double getConvertedCurrency(String baseCurrency, double baseValue, String targetCurrency) {
        return baseValue * getCurrencyRate(baseCurrency, targetCurrency);
    }

    public void refreshData()
    {
        repository.refreshData();
    }

    public MutableLiveData<Boolean> getMutableLiveDataInfoResponseSuccess() {
        return repository.getMutableLiveDataInfoResponseSuccess();
    }
    public MutableLiveData<Boolean> getMutableLiveDataErrorResponseBodyNull() {
        return repository.getMutableLiveDataErrorResponseBodyNull();
    }
    public MutableLiveData<Boolean> getMutableLiveDataErrorCallEnqueueFailure() {
        return repository.getMutableLiveDataErrorCallEnqueueFailure();
    }
}
