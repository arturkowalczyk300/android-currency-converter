package com.arturkowalczyk300.currencyconverter.ViewModels;

public class CurrenciesRateFetchingResult {
    public static final Double ERROR_VALUE = Double.MIN_VALUE;

    public String sourceCurrency;
    public String targetCurrency;
    public Double rate;

    public CurrenciesRateFetchingResult(String sourceCurrency, String targetCurrency, Double rate) {
        this.sourceCurrency = sourceCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
    }
}
