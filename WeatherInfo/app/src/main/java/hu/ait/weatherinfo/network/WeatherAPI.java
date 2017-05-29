package hu.ait.weatherinfo.network;

import hu.ait.weatherinfo.data.WeatherResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WeatherAPI {
    @GET("data/2.5/weather")
    Call<WeatherResult> getWeatherInCity(@Query("q") String q,
                                         @Query("units") String units,
                                         @Query("appid") String appid);
}
