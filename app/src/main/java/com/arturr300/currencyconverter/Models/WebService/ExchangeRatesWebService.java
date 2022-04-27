package com.arturr300.currencyconverter.Models.WebService;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExchangeRatesWebService {
    //static variables
    public static String DEFAULT_BASE_CURRENCY = "USD";

    //non-static variables
    private MutableLiveData<ExchangeRateFromApiEntity> currenciesRates;
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

        //currenciesRates.setValue(new ExchangeRateFromApiEntity());
        apiHandle = ExchangeRatesRetrofitClient.getExchangeRatesApiInstance();
    }

    //methods

    public MutableLiveData<Boolean> getApiWorking() {
        return apiWorking;
    }

    public void requestRatesData(String baseCurrency) {
        requestApiReading(baseCurrency);
    }

    private Callback<ExchangeRateFromApiEntity> responseCallback = new Callback<ExchangeRateFromApiEntity>() {
        @Override
        public void onResponse(Call<ExchangeRateFromApiEntity> call,
                               Response<ExchangeRateFromApiEntity> response) {

            if (response.body() == null) {
                sendErrorResponseBodyNull();
            }

            if (response.isSuccessful()) {
                sendInfoResponseSuccess();
                apiWorking.setValue(true);
                currenciesRates.setValue(response.body());
            }
        }

        @Override
        public void onFailure(Call<ExchangeRateFromApiEntity> call, Throwable t) {
            sendErrorCallEnqueueFailure();
        }
    };

    public void requestApiReading(String baseCurrency) {
        Call<ExchangeRateFromApiEntity> apiReading;
        apiReading = apiHandle.getReading(baseCurrency);

        apiReading.enqueue(responseCallback);
    }

    public MutableLiveData<ExchangeRateFromApiEntity> getCurrenciesRates() {
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
