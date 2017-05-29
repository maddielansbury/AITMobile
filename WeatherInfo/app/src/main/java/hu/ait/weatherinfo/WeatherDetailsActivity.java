package hu.ait.weatherinfo;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Text;

import hu.ait.weatherinfo.data.WeatherResult;
import hu.ait.weatherinfo.network.WeatherAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherDetailsActivity extends AppCompatActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_details);
        context = this;

        final String cityName = getIntent().getStringExtra(MainActivity.KEY_DETAILS);

        Retrofit retrofit =
                new Retrofit.Builder().baseUrl(getString(R.string.api_openweathermap_org))
                .addConverterFactory(GsonConverterFactory.create()).build();
        final WeatherAPI weatherAPI = retrofit.create(WeatherAPI.class);

        final TextView tvCityName = (TextView) findViewById(R.id.tvCityName);
        final TextView tvTemperature = (TextView) findViewById(R.id.tvTemperature);
        final TextView tvCoords = (TextView) findViewById(R.id.tvCoords);
        final TextView tvDescription = (TextView) findViewById(R.id.tvDescription);
        final TextView tvSunrise = (TextView) findViewById(R.id.tvSunrise);
        final TextView tvSunset = (TextView) findViewById(R.id.tvSunset);
        final TextView tvIconSrc = (TextView) findViewById(R.id.tvIconSrc);
        final TextView tvHumidity = (TextView) findViewById(R.id.tvHumidity);
        final ImageView ivIcon = (ImageView) findViewById(R.id.ivIcon);
        final GoogleMap map = getSupportFragmentManager().findFragmentById(R.id.map).;

        Call<WeatherResult> callWeather =
                weatherAPI.getWeatherInCity(cityName, getString(R.string.units), getString(R.string.weather_api_key));
        callWeather.enqueue(new Callback<WeatherResult>() {
            @Override
            public void onResponse(Call<WeatherResult> call, Response<WeatherResult> response) {
                tvCityName.setText(cityName);
                ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
                Double latitude = response.body().getCoord().getLat();
                Double longitude = response.body().getCoord().getLon();
                //mapFrag.setMapPosition(new LatLng(latitude, longitude));
                tvCoords.setText("(" +
                        latitude + getString(R.string.deg_north) +
                        longitude + getString(R.string.deg_west));
                tvTemperature.setText(
                        String.format("%s°", String.format("%.1f", response.body().getMain().getTemp())));
                tvDescription.setText(response.body().getWeather().get(0).getDescription() + ".");
                tvHumidity.setText(getString(R.string.humidity) +
                        response.body().getMain().getHumidity());
                Long sunriseTime = response.body().getSys().getSunrise() * 1000;
                String sun = new java.text.SimpleDateFormat("HH:mm").format(
                        new java.util.Date(sunriseTime));
                tvSunrise.setText(getString(R.string.sunrise) + sun + getString(R.string.timezone));
                Long sunsetTime = response.body().getSys().getSunset() * 1000;
                sun = new java.text.SimpleDateFormat("HH:mm").format(
                        new java.util.Date(sunsetTime));
                tvSunset.setText(getString(R.string.sunset) + sun + getString(R.string.timezone));
                tvIconSrc.setText(getString(R.string.openweathermap_org_img_w) +
                        response.body().getWeather().get(0).getIcon() + getString(R.string.png));
                Glide.with(context).load(tvIconSrc.getText().toString()).into(ivIcon);
            }

            @Override
            public void onFailure(Call<WeatherResult> call, Throwable t) {
                tvTemperature.setText(t.getLocalizedMessage());
            }
        });
    }
}
