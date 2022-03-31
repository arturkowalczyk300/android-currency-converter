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
import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExchangeRatesWebService {
    //static variables
    public static String DEFAULT_BASE_CURRENCY = "USD";

    //non-static variables
    private MutableLiveData<TreeMap<String, Double>> currenciesRates;
    private MutableLiveData<Boolean> apiWorking;

    private MutableLiveData<Boolean> mutableLiveDataInfoResponseSuccess;

    private MutableLiveData<Boolean> mutableLiveDataErrorResponseBodyNull;
    private MutableLiveData<Boolean> mutableLiveDataErrorCallEnqueueFailure;
    private ExchangeRatesApiHandle apiHandle;
    //constructor

    public ExchangeRatesWebService() {
        currenciesRates = new MutableLiveData<>();
        apiWorking = new MutableLiveData<>();
        mutableLiveDataInfoResponseSuccess = new MutableLiveData<>();
        mutableLiveDataErrorResponseBodyNull = new MutableLiveData<>();
        mutableLiveDataErrorCallEnqueueFailure = new MutableLiveData<>();

        currenciesRates.setValue(new TreeMap<>());
        apiHandle = ExchangeRatesRetrofitClient.getExchangeRatesApiInstance();
    }
    //methods

    public MutableLiveData<Boolean> getApiWorking() {
        return apiWorking;
    }
    public void refreshData() {
        getApiReading(DEFAULT_BASE_CURRENCY);
    }

    public void getApiReading(String base) {
        Call<ExchangeRateFromApiEntity> apiReading;
        apiReading = apiHandle.getReading(base);

        apiReading.enqueue(new Callback<ExchangeRateFromApiEntity>() {
            @Override
            public void onResponse(Call<ExchangeRateFromApiEntity> call,
                                   Response<ExchangeRateFromApiEntity> response) {
                if (response.body() == null) {
                    sendErrorResponseBodyNull();
                }

                if (response.isSuccessful()) {
                    sendInfoResponseSuccess();
                    apiWorking.setValue(true);

                    ArrayList<String> currencies = new ArrayList<>();

                    currenciesRates.getValue().clear();
                    currenciesRates.getValue().putAll(response.body().getRates());
                }
            }


            @Override
            public void onFailure(Call<ExchangeRateFromApiEntity> call, Throwable t) {
                sendErrorCallEnqueueFailure();
            }
        });

    }

    public MutableLiveData<TreeMap<String, Double>> getCurrenciesRates() {
        return currenciesRates;
    }

    public void sendInfoResponseSuccess() {
        mutableLiveDataInfoResponseSuccess.setValue(true);
    }

    public void sendErrorResponseBodyNull() {
        mutableLiveDataErrorResponseBodyNull.setValue(true);
    }

    public void sendErrorCallEnqueueFailure() {
        mutableLiveDataErrorCallEnqueueFailure.setValue(true);
    }

    public MutableLiveData<Boolean> getMutableLiveDataInfoResponseSuccess() {
        return mutableLiveDataInfoResponseSuccess;
    }

    public MutableLiveData<Boolean> getMutableLiveDataErrorResponseBodyNull() {
        return mutableLiveDataErrorResponseBodyNull;
    }

    public MutableLiveData<Boolean> getMutableLiveDataErrorCallEnqueueFailure() {
        return mutableLiveDataErrorCallEnqueueFailure;
    }
}
