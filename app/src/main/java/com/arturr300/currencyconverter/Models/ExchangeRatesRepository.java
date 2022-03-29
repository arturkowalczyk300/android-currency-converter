package com.arturr300.currencyconverter.Models;

import androidx.lifecycle.MutableLiveData;

import com.arturr300.currencyconverter.Models.WebService.ExchangeRatesWebService;

import java.util.List;

public class ExchangeRatesRepository {
    ExchangeRatesWebService webService;

    public ExchangeRatesRepository() {
        webService = new ExchangeRatesWebService();
    }

    public MutableLiveData<Boolean> getApiWorking() {
        return webService.getApiWorking();
    }

    public MutableLiveData<List<String>> getAvailableCurrencies() {
        return webService.getAvailableCurrencies();
    }

    public double getCurrencyRate(String base, String target) {
        return webService.getCurrencyRate(base, target);
    }

    public double getConvertedCurrency(String baseCurrency, double baseValue, String targetCurrency) {
        return webService.getConvertedCurrency(baseCurrency, baseValue, targetCurrency);
    }

    public void refreshData()
    {
        webService.refreshData();
    }
}
