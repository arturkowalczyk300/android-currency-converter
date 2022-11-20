package com.arturkowalczyk300.currencyconverter.Models;

import androidx.lifecycle.MutableLiveData;

import com.arturkowalczyk300.currencyconverter.Models.WebService.ExchangeRateFromApiEntity;
import com.arturkowalczyk300.currencyconverter.Models.WebService.ExchangeRatesWebService;
import java.util.TreeMap;

public class ExchangeRatesRepository {
    //static variables

    //non-static variables
    private ExchangeRatesWebService webService;

    //constructor
    public ExchangeRatesRepository() {
        webService = new ExchangeRatesWebService();
    }

    //methods
    public MutableLiveData<Boolean> isApiWorking() {
        return webService.getApiWorking();
    }
    public void requestRatesData(String baseCurrency)
    {
        webService.requestRatesData(baseCurrency);
    }
    public MutableLiveData<ExchangeRateFromApiEntity> getCurrenciesRates() {
        return webService.getCurrenciesRates();
    }

    public MutableLiveData<Boolean> getMutableLiveDataInfoResponseSuccess() {
        return webService.getMutableLiveDataInfoResponseSuccess();
    }
    public MutableLiveData<Boolean> getMutableLiveDataErrorResponseBodyNull() {
        return webService.getMutableLiveDataErrorResponseBodyNull();
    }
    public MutableLiveData<Boolean> getMutableLiveDataErrorCallEnqueueFailure() {
        return webService.getMutableLiveDataErrorCallEnqueueFailure();
    }
}
