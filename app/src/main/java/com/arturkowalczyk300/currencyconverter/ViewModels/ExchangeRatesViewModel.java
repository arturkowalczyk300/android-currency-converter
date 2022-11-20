package com.arturkowalczyk300.currencyconverter.ViewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.arturkowalczyk300.currencyconverter.Models.ExchangeRatesRepository;
import com.arturkowalczyk300.currencyconverter.Models.WebService.ExchangeRateFromApiEntity;
import com.arturkowalczyk300.currencyconverter.R;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class ExchangeRatesViewModel extends ViewModel {
    //static variables

    //non-static variables
    private Application application;
    private ExchangeRatesRepository repository;
    private boolean responseReceived;
    private boolean pendingConversionMultipleCurrencies;
    private List<String> currenciesToBeConverted;
    private MutableLiveData<Boolean> mutableLiveDataErrorCurrencyRatesTreeMapEmpty;

    //  data to be returned
    private MutableLiveData<ExchangeRateFromApiEntity> newestCurrenciesRates;
    private MutableLiveData<CurrenciesRateFetchingResult> resultOfCurrenciesRatesFetching;
    private MutableLiveData<ConversionResult> currenciesConversionResult;
    private MutableLiveData<List<ConversionResult>> multipleCurrenciesConversionResult;
    private MutableLiveData<List<String>> currenciesList;

    //constructor
    public ExchangeRatesViewModel(@NonNull Application application) {
        this.application = application;

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
        requestRatesData(application.getString(R.string.DEFAULT_CURRENCY));
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

        multipleCurrenciesConversionResult.
                getValue().clear();

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
    private void FillCurrencyRateResultDataStructure(CurrenciesRateFetchingResult resultToFillRates, TreeMap<String, Double> currencyRate) {
        if (resultToFillRates.sourceCurrency == resultToFillRates.targetCurrency) {
            return;
        }

        if (resultToFillRates != null)
            resultToFillRates.rate = currencyRate.get(resultToFillRates.targetCurrency);
        else {
            mutableLiveDataErrorCurrencyRatesTreeMapEmpty.setValue(Boolean.TRUE);
        }

        if (resultToFillRates.rate == null)
        resultOfCurrenciesRatesFetching.setValue(resultToFillRates);
    }

    private Observer<ExchangeRateFromApiEntity> currenciesRatesChangedObserver = new Observer<ExchangeRateFromApiEntity>() {
        @Override
        public void onChanged(ExchangeRateFromApiEntity currencyRate) {
            if (currencyRate.getRates() == null) {
                return;
            }

            if (currencyRate.getRates().size() > 0) {
                responseReceived = true;

                //fill currencies list
                currenciesList.setValue(new ArrayList<String>(currencyRate.getRates().keySet()));
                FillCurrencyRateResultDataStructure(
                        resultOfCurrenciesRatesFetching.getValue(),
                        currencyRate.getRates());


                    if (pendingConversionMultipleCurrencies) {
                        List<ConversionResult> listResultToFillConversion = multipleCurrenciesConversionResult.getValue();
                        for (ConversionResult result : listResultToFillConversion) {
                            result.rate = currencyRate.getRates().get(result.targetCurrency);
                            if (result.rate == null) {
                                return;
                            }
                            result.resultAmount = result.sourceAmount * result.rate;
                            multipleCurrenciesConversionResult.setValue(listResultToFillConversion);
                        }
                    } else { //convert only one currency

                        //fill currency conversion result data structure
                        ConversionResult resultToFillConversion = currenciesConversionResult.getValue();
                        resultToFillConversion.rate = currencyRate.getRates().get(resultToFillConversion.targetCurrency);

                        if (resultToFillConversion.rate != null) {
                            resultToFillConversion.resultAmount = resultToFillConversion.sourceAmount * resultToFillConversion.rate;
                            currenciesConversionResult.setValue(resultToFillConversion);
                        }
                        else {

                        }
                    }
                }

            }
        };

    private MutableLiveData<ExchangeRateFromApiEntity> getCurrenciesRates() {
        return repository.getCurrenciesRates();
    }

    private void requestRatesData(String baseCurrency) {
        repository.requestRatesData(baseCurrency);
    }
}
