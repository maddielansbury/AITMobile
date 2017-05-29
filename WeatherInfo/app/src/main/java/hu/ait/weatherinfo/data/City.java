package hu.ait.weatherinfo.data;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class City extends RealmObject {
    @PrimaryKey
    private String cityID;

    private String cityName;

    public City() {

    }

    public City(String cityName, String description) {
        this.cityName = cityName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityID() {
        return cityID;
    }
}

