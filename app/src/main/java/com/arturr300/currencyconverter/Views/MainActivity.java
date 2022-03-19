package com.arturr300.currencyconverter.Views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.arturr300.currencyconverter.BuildConfig;
import com.arturr300.currencyconverter.R;
import com.arturr300.currencyconverter.ViewModels.ExchangeRatesViewModel;
import com.arturr300.currencyconverter.ViewModels.ExchangeRatesViewModelFactory;
import com.google.android.material.tabs.TabLayout;
//todo(4): show API state, or at least prepare it to exception occur
//todo(2): settings activity
//todo(3): about activity
//todo(6): add history (graph, trend)
//todo(7): landscape orientation
//todo(8): refactor
//         -merge duplicate listeners
//         -
//todo(9): implement MVVM/MVC

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ExchangeRatesViewModel viewModel;
    Context appContext;

    public void showNetworkErrorScreen() {
        if (viewFragmentNetworkError != null) {
            tabLayout.setVisibility(View.GONE);
            viewPager.setVisibility(View.GONE);
            viewFragmentNetworkError.setVisibility(View.VISIBLE);
        }
    }

    public void hideNetworkErrorScreen() {
        if (viewFragmentNetworkError != null
                && viewModel.testAPI()) {
            viewFragmentNetworkError.setVisibility(View.GONE);
            viewPager.setVisibility(View.VISIBLE);
            tabLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        menuItemDarkMode = menu.findItem(R.id.action_darkmode);
        menuItemDarkMode.setChecked((AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES));
        return true;
    }

    View viewFragmentNetworkError;

    //tab layout part
    TabLayout tabLayout;
    ViewPager viewPager;
    AllCurrenciesFragment listSelectFragment;
    MostUsedCurrenciesFragment fragmentMostUsedFragment;
    NetworkErrorFragment fragmentNetworkError;
    MenuItem menuItemDarkMode;

    //other variables
    String appBuildDate;
    DecimalFormat df;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //HandleItem selection
        switch (item.getItemId()) {
            case R.id.action_darkmode:
                if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    item.setChecked(false);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    item.setChecked(true);
                }
                finish(); //destroy activity
                startActivity(new Intent(MainActivity.this, MainActivity.this.getClass()));
                return true;
            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            case R.id.action_about:

                AlertDialog.Builder alert = new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.menu_about))
                        .setMessage(getString(R.string.content_about, appBuildDate))
                        .setNeutralButton("OK", null);
                alert.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.LightTheme);
        }

        setContentView(R.layout.activity_main);
        appContext = getApplicationContext();
        //add toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        appBuildDate = this.getAppBuildDate();

        //tab layout part
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        listSelectFragment = new AllCurrenciesFragment();
        fragmentMostUsedFragment = new MostUsedCurrenciesFragment();
        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        viewPagerAdapter.addFragment(fragmentMostUsedFragment, getString(R.string.most_used));
        viewPagerAdapter.addFragment(listSelectFragment, getString(R.string.list_select));

        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_most_used);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_list_select);

        //thread responsibility fuses override
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        df = new DecimalFormat("#.###");
        viewFragmentNetworkError = findViewById(R.id.fragmentNetworkError);
        viewFragmentNetworkError.setVisibility(View.INVISIBLE);
        //tabLayout.setVisibility(View.GONE);

        //view model handling section
        ExchangeRatesViewModelFactory viewModelFactory = new ExchangeRatesViewModelFactory(getApplication());
        viewModel = new ViewModelProvider(this, viewModelFactory).get(ExchangeRatesViewModel.class);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();
        private List<String> fragmentTitle = new ArrayList<>();


        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(Fragment fragment, String title) {
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

    private String getAppBuildDate() {
        Date buildDate = BuildConfig.BUILD_TIME;
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss 'GMT'Z");
        return dateFormat.format(buildDate);
    }

}
