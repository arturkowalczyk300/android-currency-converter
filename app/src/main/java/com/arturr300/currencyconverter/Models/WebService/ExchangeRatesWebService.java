package com.arturr300.currencyconverter.Models.WebService;

import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExchangeRatesWebService {

    private class DataToReturn //temp
    {
        boolean apiWorking;
        List<String> availableCurrencies = new ArrayList<String>();
        Map<String, Double> currenciesRates = new Map<String, Double>() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean containsKey(@Nullable Object key) {
                return false;
            }

            @Override
            public boolean containsValue(@Nullable Object value) {
                return false;
            }

            @Nullable
            @Override
            public Double get(@Nullable Object key) {
                return null;
            }

            @Nullable
            @Override
            public Double put(String key, Double value) {
                return null;
            }

            @Nullable
            @Override
            public Double remove(@Nullable Object key) {
                return null;
            }

            @Override
            public void putAll(@NonNull Map<? extends String, ? extends Double> m) {

            }

            @Override
            public void clear() {

            }

            @NonNull
            @Override
            public Set<String> keySet() {
                return null;
            }

            @NonNull
            @Override
            public Collection<Double> values() {
                return null;
            }

            @NonNull
            @Override
            public Set<Entry<String, Double>> entrySet() {
                return null;
            }
        } ;
    }

    ExchangeRatesApiHandle apiHandle;
    DataToReturn dataToReturn;

    public ExchangeRatesWebService() {
        apiHandle = ExchangeRatesRetrofitClient.getExchangeRatesApiInstance();
        dataToReturn = new DataToReturn();
        getApiReading(false, "");
    }

    public void getApiReading(boolean setBase, String base) {
        Call<ExchangeRateFromApiEntity> apiReading;
        if (!setBase)
            apiReading = apiHandle.getReading("USD");
        else
            apiReading = apiHandle.getReading(base);

        apiReading.enqueue(new Callback<ExchangeRateFromApiEntity>() {
            @Override
            public void onResponse(Call<ExchangeRateFromApiEntity> call,
                                   Response<ExchangeRateFromApiEntity> response) {
                if (response.body() == null) {
                    sendErrorResponseBodyNull();
                }

                if (response.isSuccessful()) {
                    //here tinkering with data
                    dataToReturn.apiWorking = true;

                    dataToReturn.availableCurrencies.clear();
                    dataToReturn.currenciesRates.clear();
                    dataToReturn.availableCurrencies.addAll(response.body().getRates().keySet());
                    dataToReturn.currenciesRates = response.body().getRates();
                }


            }


            @Override
            public void onFailure(Call<ExchangeRateFromApiEntity> call, Throwable t) {
                sendErrorCallEnqueueFailure();
            }
        });

    }

    public boolean testAPI() {
        return dataToReturn.apiWorking;
    }

    public List<String> getAvailableCurrencies() {
        return dataToReturn.availableCurrencies;
    }

    public double getCurrencyRate(String base, String target) {
        try{
            return dataToReturn.currenciesRates.get(target);
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

    public void sendErrorResponseBodyNull() {

    }

    public void sendErrorCallEnqueueFailure() {

    }
}
