package com.arturr300.currencyconverter.Models.WebService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExchangeRatesWebService {
    public JSONObject getJSONObjectFromAPI(boolean setBase, String base) {
        try {
            String urlString = "https://api.frankfurter.app/latest";
            if (setBase)
                urlString += "?base=" + base;
            URL url = new URL(urlString);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String output = "";
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            output += sb.toString();
            JSONObject jMain = new JSONObject(output);
            return jMain;
        } catch (IOException | JSONException e) {
            //Toast.makeText(appContext, e.toString(), Toast.LENGTH_LONG).show();
            return null;
        }
    }

    public boolean testAPI() {
        if(this.getJSONObjectFromAPI(true, "USD")!=null)
            return true;
        return false;
    }

    public List<String> getAvailableCurrencies() {
        try {
            JSONObject jMain = getJSONObjectFromAPI(true, "EUR");
            if (jMain == null) {
                throw new JSONException("Connection to API failed!");
            }
            JSONObject jRates = jMain.getJSONObject("rates");
            List<String> listRates = new ArrayList<>();
            for (Iterator<String> iterator = jRates.keys(); iterator.hasNext(); ) {
                String key = iterator.next();
                listRates.add(key);
            }
            return listRates;
        } catch (JSONException e) {
            //Toast.makeText(appContext, e.toString(), Toast.LENGTH_LONG).show();
            return null;
        }
    }

    public double getCurrencyRate(String base, String target) {
        double jTarget;
        try {
            JSONObject jMain = getJSONObjectFromAPI(true, base);
            if (jMain == null) {
                throw new JSONException("Connection to API failed!");
            }
            JSONObject jRates = jMain.getJSONObject("rates");
            jTarget = jRates.getDouble(target);
            return jTarget;
        } catch (JSONException e) {
            return -100.0f;
        }
    }

    public double getConvertedCurrency(String baseCurrency, double baseValue, String targetCurrency) {
        return baseValue * getCurrencyRate(baseCurrency, targetCurrency);
    }
}
