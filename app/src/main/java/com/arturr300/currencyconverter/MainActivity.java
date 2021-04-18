package com.arturr300.currencyconverter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
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

    Context appContext;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        menuItemDarkMode = menu.findItem(R.id.action_darkmode);
        menuItemDarkMode.setChecked((AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES));
        return true;
    }

    //tab layout part
    TabLayout tabLayout;
    ViewPager viewPager;
    ListSelect fragmentListSelect;
    MostUsed fragmentMostUsed;
    MenuItem menuItemDarkMode;
    //other variables
    double USD2PLN;
    double USD2EUR;
    double EUR2PLN;
    DecimalFormat df;
    CurrencyUtils mCurrencyUtils = new CurrencyUtils();


    boolean darkMode = false;

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
                Toast.makeText(appContext, "Action_settings clicked!", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_about:
                AlertDialog.Builder alert = new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.menu_about))
                        .setMessage(getString(R.string.content_about))
                        .setNeutralButton("OK", null);
                alert.show();
                Toast.makeText(appContext, "Action_about clicked!", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
        {
            setTheme(R.style.DarkTheme);
        }
        else
        {
            setTheme(R.style.LightTheme);
        }

        setContentView(R.layout.activity_main);
        appContext = getApplicationContext();
        //add toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //tab layout part
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        fragmentListSelect = new ListSelect();
        fragmentMostUsed = new MostUsed();
        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        viewPagerAdapter.addFragment(fragmentMostUsed, getString(R.string.most_used));
        viewPagerAdapter.addFragment(fragmentListSelect, getString(R.string.list_select));
        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_most_used);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_list_select);

        //thread responsibility fuses override
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        df = new DecimalFormat("#.###");
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


}
