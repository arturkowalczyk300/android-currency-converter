package com.arturr300.currencyconverter.ViewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.arturr300.currencyconverter.Models.ExchangeRatesRepository;
import com.arturr300.currencyconverter.R;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class ExchangeRatesViewModel extends ViewModel {
    //static variables

    //non-static variables
    private ExchangeRatesRepository repository;
    private boolean responseReceived;
    private boolean pendingConversionMultipleCurrencies;
    private List<String> currenciesToBeConverted;
    private MutableLiveData<Boolean> mutableLiveDataErrorCurrencyRatesTreeMapEmpty;

    //  data to be returned
    private MutableLiveData<TreeMap<String, Double>> newestCurrenciesRates;
    private MutableLiveData<CurrenciesRateFetchingResult> resultOfCurrenciesRatesFetching;
    private MutableLiveData<ConversionResult> currenciesConversionResult;
    private MutableLiveData<List<ConversionResult>> multipleCurrenciesConversionResult;
    private MutableLiveData<List<String>> currenciesList;

    //constructor
    public ExchangeRatesViewModel(@NonNull Application application) {
        repository = new ExchangeRatesRepository();
        resultOfCurrenciesRatesFetching = new MutableLiveData<>();
        resultOfCurrenciesRatesFetching.setValue(new CurrenciesRateFetchingResult
                (application.getString(R.string.DEFAULT_CURRENCY),
                        application.getString(R.string.DEFAULT_TARGET_CURRENCY),
                        CurrenciesRateFetchingResult.ERROR_VALUE));
        currenciesConversionResult = new MutableLiveData<>();
        currenciesConversionResult.setValue(new ConversionResult
                (application.getString(R.string.DEFAULT_CURRENCY),
                        application.getString(R.string.DEFAULT_TARGET_CURRENCY),
                        ConversionResult.ERROR_VALUE,
                        ConversionResult.ERROR_VALUE,
                        ConversionResult.ERROR_VALUE));
        multipleCurrenciesConversionResult = new MutableLiveData<>();
        multipleCurrenciesConversionResult.setValue(new ArrayList<ConversionResult>());
        currenciesList = new MutableLiveData<>();
        mutableLiveDataErrorCurrencyRatesTreeMapEmpty = new MutableLiveData<>();

        newestCurrenciesRates = getCurrenciesRates();
        newestCurrenciesRates.observeForever(currenciesRatesChangedObserver);

        requestRatesData(application.getString(R.string.DEFAULT_CURRENCY));
    }

    //methods
    //  public
    public LiveData<Boolean> isApiWorking() {
        return repository.isApiWorking();
    }

    public LiveData<CurrenciesRateFetchingResult> getCurrencyRate(String sourceCurrency, String targetCurrency) {
        this.requestRatesData(sourceCurrency);
        resultOfCurrenciesRatesFetching.setValue(new CurrenciesRateFetchingResult(
                sourceCurrency,
                targetCurrency,
                CurrenciesRateFetchingResult.ERROR_VALUE));
        return resultOfCurrenciesRatesFetching;
    }

    public LiveData<List<String>> getCurrenciesList() {
        return currenciesList;
    }

    public LiveData<ConversionResult> convertCurrency(String sourceCurrency, Double sourceAmount, String targetCurrency) {
        this.requestRatesData(sourceCurrency);
        currenciesConversionResult.
                setValue(
                        new ConversionResult(sourceCurrency,
                                targetCurrency,
                                sourceAmount,
                                ConversionResult.ERROR_VALUE,
                                ConversionResult.ERROR_VALUE));
        return currenciesConversionResult;
    }

    public LiveData<List<ConversionResult>> convertMultipleCurrencies(String sourceCurrency, Double sourceAmount, List<String> targetCurrencies) {
        this.requestRatesData(sourceCurrency);
        this.pendingConversionMultipleCurrencies = true; //flag

        for (String targetCurrency : targetCurrencies) {
            multipleCurrenciesConversionResult.
                    getValue().
                    add(new ConversionResult(sourceCurrency,
                            targetCurrency,
                            sourceAmount,
                            ConversionResult.ERROR_VALUE,
                            ConversionResult.ERROR_VALUE));


        }
        return multipleCurrenciesConversionResult;

    }

    public MutableLiveData<Boolean> getMutableLiveDataInfoResponseSuccess() {
        return repository.getMutableLiveDataInfoResponseSuccess();
    }

    public MutableLiveData<Boolean> getMutableLiveDataErrorResponseBodyNull() {
        return repository.getMutableLiveDataErrorResponseBodyNull();
    }

    public MutableLiveData<Boolean> getMutableLiveDataErrorCallEnqueueFailure() {
        return repository.getMutableLiveDataErrorCallEnqueueFailure();
    }

    public MutableLiveData<Boolean> getMutableLiveDataErrorCurrencyRatesTreeMapEmpty() {
        return this.mutableLiveDataErrorCurrencyRatesTreeMapEmpty;
    }
    //  private

    private Observer<TreeMap<String, Double>> currenciesRatesChangedObserver = new Observer<TreeMap<String, Double>>() {
        @Override
        public void onChanged(TreeMap<String, Double> currencyRate) {
            if (currencyRate.size() > 0) {
                responseReceived = true;

                //fill currencies list
                currenciesList.setValue(new ArrayList<String>(currencyRate.keySet()));

                //fill fetching currency rate result data structure
                CurrenciesRateFetchingResult resultToFillRates = resultOfCurrenciesRatesFetching.getValue();
                if (resultToFillRates != null) {
                    resultToFillRates.rate = currencyRate.get(resultToFillRates.targetCurrency);

                    //fill currency conversion result data structure
                    ConversionResult resultToFillConversion = currenciesConversionResult.getValue();
                    resultToFillConversion.rate = currencyRate.get(resultToFillConversion.targetCurrency);

                    if (resultToFillConversion.rate != null) {
                        Log.e("myApp",
                                "currency conversion rate is OK! [source="
                                        + resultToFillConversion.sourceCurrency
                                        + ",target="
                                        + resultToFillConversion.targetCurrency
                                        + "]");

                        if (!pendingConversionMultipleCurrencies) {
                            resultToFillConversion.resultAmount = resultToFillConversion.sourceAmount * resultToFillConversion.rate;
                            currenciesConversionResult.setValue(resultToFillConversion);
                        } else {
                            List<ConversionResult> listResultToFillConversion = multipleCurrenciesConversionResult.getValue();
                            for (ConversionResult result : listResultToFillConversion) {
                                result.rate = currencyRate.get(result.sourceCurrency);
                                result.resultAmount = result.sourceAmount * result.rate;
                                multipleCurrenciesConversionResult.setValue(listResultToFillConversion);
                            }
                        }
                    } else {
                        Log.e("myApp",
                                "currency conversion rate is null! [source="
                                        + resultToFillConversion.sourceCurrency
                                        + ",target="
                                        + resultToFillConversion.targetCurrency
                                        + "]");
                    }
                    resultOfCurrenciesRatesFetching.setValue(resultToFillRates);
                }
            } else {
                mutableLiveDataErrorCurrencyRatesTreeMapEmpty.setValue(Boolean.TRUE);
            }
        }
    };

    private MutableLiveData<TreeMap<String, Double>> getCurrenciesRates() {
        return repository.getCurrenciesRates();
    }

    private void requestRatesData(String baseCurrency) {
        Log.e("myApp", "req, base=" + baseCurrency);
        repository.requestRatesData(baseCurrency);
    }
}
