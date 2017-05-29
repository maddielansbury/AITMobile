package hu.ait.shoppinglist;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainApplication extends Application {

    private Realm realmShopping;

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
        realmShopping = Realm.getInstance(config);
    }

    public void closeRealm() {
        realmShopping.close();
    }

    public Realm getRealmShopping() {
        return realmShopping;
    }
}
