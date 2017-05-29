package hu.ait.weatherinfo;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainApplication extends Application {

    private Realm realmSavedCities;

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
    }

    public void openRealm() {
        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        realmSavedCities = Realm.getInstance(config);
    }

    public void closeRealm() {
        realmSavedCities.close();
    }

    public Realm getRealmSavedCities() {
        return realmSavedCities;
    }
}
