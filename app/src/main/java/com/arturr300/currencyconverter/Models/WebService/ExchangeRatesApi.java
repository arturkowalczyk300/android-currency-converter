package com.arturr300.currencyconverter.Models.WebService;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ExchangeRatesApi {
    @GET("/latest")
    Call<ExchangeRateFromApiEntity> getReading(@Query("base") String baseCurrencyCode);
}
