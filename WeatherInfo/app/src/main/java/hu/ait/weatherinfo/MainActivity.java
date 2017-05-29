package hu.ait.weatherinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hu.ait.weatherinfo.adapter.CitiesAdapter;
import hu.ait.weatherinfo.data.City;
import hu.ait.weatherinfo.touch.CitiesListTouchHelperCallback;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int REQUEST_NEW_CITY = 101;
    public static final int REQUEST_WEATHER_DETAILS = 102;
    public static final String KEY_DETAILS = "KEY_DETAILS";
    private CitiesAdapter citiesAdapter;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((MainApplication)getApplication()).openRealm();

        setUpRecyclerView();
        setUpNav();

        FloatingActionButton fabAddCity = (FloatingActionButton) findViewById(R.id.fabAddCity);
        fabAddCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddCityDialog();
            }
        });
    }

    private void setUpNav() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.layoutDrawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setUpRecyclerView() {
        RealmResults<City> allCities = getRealm().where(City.class).findAll();
        City placesArray[] = new City[allCities.size()];
        List<City> placesResult =
                new ArrayList<City>(Arrays.asList(allCities.toArray(placesArray)));

        citiesAdapter = new CitiesAdapter(placesResult, this);
        RecyclerView recyclerViewCities = (RecyclerView) findViewById(
                R.id.recyclerViewCities);
        recyclerViewCities.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCities.setAdapter(citiesAdapter);

        CitiesListTouchHelperCallback touchHelperCallback = new CitiesListTouchHelperCallback(
                citiesAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(
                touchHelperCallback);
        touchHelper.attachToRecyclerView(recyclerViewCities);
    }

    private void showAddCityDialog() {
        Intent intentStart = new Intent(MainActivity.this,
                AddCityDialog.class);
        startActivityForResult(intentStart, REQUEST_NEW_CITY);
    }

    public void showWeatherDetailsActivity(String cityName) {
        Intent intentStart = new Intent(MainActivity.this,
                WeatherDetailsActivity.class);

        intentStart.putExtra(KEY_DETAILS, cityName);
        startActivityForResult(intentStart, REQUEST_WEATHER_DETAILS);
    }

    public Realm getRealm() {
        return ((MainApplication)getApplication()).getRealmSavedCities();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                String cityID  = data.getStringExtra(
                        AddCityDialog.KEY_CITY);

                City city = getRealm().where(City.class)
                        .equalTo(getString(R.string.cityID), cityID)
                        .findFirst();

                if (requestCode == REQUEST_NEW_CITY) {
                    citiesAdapter.addCity(city);
                }
                break;
            case RESULT_CANCELED:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_add_city) {
            showAddCityDialog();
        } else if (id == R.id.nav_about) {
            Toast.makeText(this, R.string.author_name, Toast.LENGTH_LONG).show();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void deleteCity(City city) {
        getRealm().beginTransaction();
        city.deleteFromRealm();
        getRealm().commitTransaction();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((MainApplication)getApplication()).closeRealm();
    }
}
