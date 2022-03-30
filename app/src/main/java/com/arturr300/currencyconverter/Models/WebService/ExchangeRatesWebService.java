package com.arturr300.currencyconverter.Models.WebService;

import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExchangeRatesWebService {

    private class DataToReturn //temp
    {
        MutableLiveData<Boolean> apiWorking;
        MutableLiveData<List<String>> availableCurrencies = new MutableLiveData<>();
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

        public DataToReturn(){
            apiWorking = new MutableLiveData<>();
        }
    }

    ExchangeRatesApiHandle apiHandle;
    DataToReturn dataToReturn;

    public ExchangeRatesWebService() {
        apiHandle = ExchangeRatesRetrofitClient.getExchangeRatesApiInstance();
        dataToReturn = new DataToReturn();
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
                    dataToReturn.apiWorking.setValue( true);

                    ArrayList<String> currencies = new ArrayList<>();

                    currencies.clear();
                    currencies.addAll(response.body().getRates().keySet());
                    dataToReturn.availableCurrencies.setValue(currencies);

                    dataToReturn.currenciesRates.clear();
                    dataToReturn.currenciesRates = response.body().getRates();
                }


            }


            @Override
            public void onFailure(Call<ExchangeRateFromApiEntity> call, Throwable t) {
                sendErrorCallEnqueueFailure();
            }
        });

    }

    public MutableLiveData<Boolean> getApiWorking() {
        return dataToReturn.apiWorking;
    }

    public MutableLiveData<List<String>> getAvailableCurrencies() {
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

    public void refreshData()
    {
        getApiReading(false, "");
    }
}
