package com.arturr300.currencyconverter.ViewModels;

public class ConversionResult {
    public static final Double ERROR_VALUE = Double.MIN_VALUE;

    public String sourceCurrency;
    public String targetCurrency;
    public Double sourceAmount;
    public Double rate;
    public Double resultAmount;

    public ConversionResult(String sourceCurrency,
                            String targetCurrency,
                            Double sourceAmount,
                            Double rate,
                            Double resultAmount) {
        this.sourceCurrency = sourceCurrency;
        this.targetCurrency = targetCurrency;
        this.sourceAmount = sourceAmount;
        this.rate = rate;
        this.resultAmount = resultAmount;
    }
}

