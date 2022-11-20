package com.arturkowalczyk300.currencyconverter.Models.WebService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ExchangeRatesRetrofitClient {
    private static final String BASE_URL = "https://api.frankfurter.app";
    private static Retrofit retrofitInstance;
    private static OkHttpClient okHttpClientInstance;
    private static ExchangeRatesApiHandle exchangeRatesApiInstance;

    private static OkHttpClient getOkHttpClientInstance() {
        if (okHttpClientInstance == null) {
            okHttpClientInstance = new OkHttpClient.Builder()
                    .connectTimeout(2, TimeUnit.SECONDS)
                    .readTimeout(2, TimeUnit.SECONDS)
                    .writeTimeout(2, TimeUnit.SECONDS)
                    .build();

        }
        return okHttpClientInstance;
    }

    private static Retrofit getRetrofitInstance() {
        if (retrofitInstance == null) {
            retrofitInstance = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(getOkHttpClientInstance())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitInstance;
    }

    public static ExchangeRatesApiHandle getExchangeRatesApiInstance() {
        if (exchangeRatesApiInstance == null) {
            exchangeRatesApiInstance = getRetrofitInstance().create(ExchangeRatesApiHandle.class);
        }
        return exchangeRatesApiInstance;
    }
}
