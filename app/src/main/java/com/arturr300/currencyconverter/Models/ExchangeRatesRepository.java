package com.arturr300.currencyconverter.Models;

import com.arturr300.currencyconverter.Models.WebService.ExchangeRatesWebService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExchangeRatesRepository {
    ExchangeRatesWebService webService;

    public ExchangeRatesRepository() {
        webService = new ExchangeRatesWebService();
    }

    public boolean testAPI() {
     return webService.testAPI();
    }

    public List<String> getAvailableCurrencies() {
        return webService.getAvailableCurrencies();
    }

    public double getCurrencyRate(String base, String target) {
       return webService.getCurrencyRate(base, target);
    }

    public double getConvertedCurrency(String baseCurrency, double baseValue, String targetCurrency) {
        return webService.getConvertedCurrency(baseCurrency, baseValue, targetCurrency);
    }
}
