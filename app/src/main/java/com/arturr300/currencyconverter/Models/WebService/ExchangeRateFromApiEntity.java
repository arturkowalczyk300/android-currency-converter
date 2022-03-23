
package com.arturr300.currencyconverter.Models.WebService;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class ExchangeRateFromApiEntity {

    @Expose
    private Double amount;
    @Expose
    private String base;
    @Expose
    private String date;
    @Expose
    private Rates rates;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Rates getRates() {
        return rates;
    }

    public void setRates(Rates rates) {
        this.rates = rates;
    }

}
