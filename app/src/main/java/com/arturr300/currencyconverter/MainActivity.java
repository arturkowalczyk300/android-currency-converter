package com.arturr300.currencyconverter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.arturr300.currencyconverter.CurrencyUtils;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Context appContext;



    //tablayout part
    TabLayout tabLayout;
    ViewPager viewPager;
    ListSelect fragmentListSelect;
    MostUsed fragmentMostUsed;

    //other variables
    double USD2PLN;
    double USD2EUR;
    double EUR2PLN;
    DecimalFormat df;
    CurrencyUtils mCurrencyUtils = new CurrencyUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appContext = getApplicationContext();

        //tab layout part
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        fragmentListSelect = new ListSelect();
        fragmentMostUsed = new MostUsed();
        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        viewPagerAdapter.addFragment(fragmentMostUsed, "Najczęściej używane");
        viewPagerAdapter.addFragment(fragmentListSelect, "Wybór walut");
        viewPager.setAdapter(viewPagerAdapter);







        //thread responsibility fuses override
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        USD2PLN = mCurrencyUtils.getCurrencyRate("USD", "PLN");
        USD2EUR = mCurrencyUtils.getCurrencyRate("USD", "EUR");
        EUR2PLN = mCurrencyUtils.getCurrencyRate("EUR", "PLN");

        df = new DecimalFormat("#.###");
        //tvUSD2PLN.setText(df.format(USD2PLN));
        //tvUSD2EUR.setText(df.format(USD2EUR));
        //tvEUR2PLN.setText(df.format(EUR2PLN));

    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();
        private List<String> fragmentTitle = new ArrayList<>();


        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(Fragment fragment, String title)
        {
        fragments.add(fragment);
        fragmentTitle.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitle.get(position);
        }
    }

    @Override
    public void onClick(View v) {

    }




}
