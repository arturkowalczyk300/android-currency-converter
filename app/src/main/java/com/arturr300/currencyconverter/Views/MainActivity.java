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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
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
import com.arturr300.currencyconverter.Preferences.PreferencesRepository;
import com.arturr300.currencyconverter.R;
import com.arturr300.currencyconverter.ViewModels.ExchangeRatesViewModel;
import com.arturr300.currencyconverter.ViewModels.ExchangeRatesViewModelFactory;
import com.google.android.material.tabs.TabLayout;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;
//todo(4): show API state, or at least prepare it to exception occur
//todo(6): add history (graph, trend)
//todo(7): landscape orientation
//todo(8): refactor
//         -merge duplicate listeners
//         -
//todo(9): implement MVVM/MVC

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //static variables
    public static final String INTENT_ID_SETTINGS_DISPLAY_DEBUG_TOASTS = "com.arturkowalczyk300.currencyconverter.INTENT_ID_SETTINGS_DISPLAY_DEBUG_TOASTS";
    public static final int REQUEST_CODE_SETTINGS_ACTIVITY = 110;

    //non-static variables
    ExchangeRatesViewModel viewModel;
    PreferencesRepository preferencesRepository;
    Context appContext;
    boolean isApiWorking;
    View viewFragmentNetworkError;

    //      tab layout part
    TabLayout tabLayout;
    ViewPager viewPager;
    AllCurrenciesFragment listSelectFragment;
    MostUsedCurrenciesFragment fragmentMostUsedFragment;
    NetworkErrorFragment fragmentNetworkError;
    MenuItem menuItemDarkMode;

    //      other variables
    String appBuildDate;
    DecimalFormat df;
    Handler handlerApiResponseTimeout;

    //handle data request and response


    //constructor

    //methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        listSelectFragment.setMainLifecycleOwner(this);
        fragmentMostUsedFragment = new MostUsedCurrenciesFragment();
        fragmentMostUsedFragment.setMainLifecycleOwner(this);
        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        viewPagerAdapter.addFragment(listSelectFragment, getString(R.string.list_select));
        viewPagerAdapter.addFragment(fragmentMostUsedFragment, getString(R.string.most_used));

        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_list_select);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_most_used);

        //timeout handler
        handlerApiResponseTimeout = new Handler(Looper.getMainLooper());

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

        //preferences repository
        preferencesRepository = new PreferencesRepository(getApplicationContext());

        //observe live data
        viewModel.isApiWorking().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                isApiWorking = aBoolean.booleanValue();
                handleNetworkState();
            }
        });
        handleNetworkState();
        viewModel.getMutableLiveDataInfoResponseSuccess().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean.booleanValue() && preferencesRepository.isSettingDisplayDebugToasts())
                    DynamicToast.makeSuccess(appContext, getString(R.string.toastSuccessApiResponse)).show();
            }
        });
        viewModel.getMutableLiveDataErrorResponseBodyNull().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean.booleanValue() && preferencesRepository.isSettingDisplayDebugToasts())
                    DynamicToast.makeError(appContext, getString(R.string.toastErrorApiReponseBodyNull)).show();
            }
        });
        viewModel.getMutableLiveDataErrorCallEnqueueFailure().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean.booleanValue() && preferencesRepository.isSettingDisplayDebugToasts())
                    DynamicToast.makeError(appContext, getString(R.string.toastErrorApiCallEnqueue)).show();
            }
        });

        viewModel.getMutableLiveDataErrorCurrencyRatesTreeMapEmpty().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean.booleanValue() && preferencesRepository.isSettingDisplayDebugToasts())
                    DynamicToast.makeError(appContext, getString(R.string.toastErrorCurrencyRatesTreeMapEmpty)).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SETTINGS_ACTIVITY && resultCode == RESULT_OK) {
            boolean settingDisplayDebugToasts = data.getBooleanExtra(this.INTENT_ID_SETTINGS_DISPLAY_DEBUG_TOASTS, false);
            preferencesRepository.setSettingDisplayDebugToasts(settingDisplayDebugToasts);

            if (preferencesRepository.isSettingDisplayDebugToasts())
                DynamicToast.makeSuccess(this, "Settings saved!");
        }
    }

    public void handleNetworkState() {
        if (isApiWorking)
            hideNetworkErrorScreen();
        else {
            //start looking for internet change state
            handlerApiResponseTimeout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isApiWorking == false)
                        showNetworkErrorScreen();
                }
            }, 1000);
        }
    }

    public void showNetworkErrorScreen() {
        if (viewFragmentNetworkError != null) {
            tabLayout.setVisibility(View.GONE);
            viewPager.setVisibility(View.GONE);
            viewFragmentNetworkError.setVisibility(View.VISIBLE);
        }
    }

    public void hideNetworkErrorScreen() {
        if (viewFragmentNetworkError != null && isApiWorking) {
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
                Intent mainActivityIntent = new Intent(MainActivity.this, MainActivity.this.getClass());
                startActivity(mainActivityIntent);
                return true;
            case R.id.action_settings:
                Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                settingsIntent.putExtra(this.INTENT_ID_SETTINGS_DISPLAY_DEBUG_TOASTS, preferencesRepository.isSettingDisplayDebugToasts());
                startActivityForResult(settingsIntent, REQUEST_CODE_SETTINGS_ACTIVITY);
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
        DateFormat dateFormat = new SimpleDateFormat(getString(R.string.DEFAULT_DATE_FORMAT));
        return dateFormat.format(buildDate);
    }

    public void fetchData(String baseCurrency) {
        viewModel.getCurrenciesList(); //todo: refactor
    }

}
