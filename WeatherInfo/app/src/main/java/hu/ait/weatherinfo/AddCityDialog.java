package hu.ait.weatherinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.UUID;

import hu.ait.weatherinfo.data.City;
import io.realm.Realm;


public class AddCityDialog extends AppCompatActivity {
    public static final String KEY_CITY = "KEY_CITY";
    private EditText etCityName;
    private City cityToEdit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);

        setupUI();
        initCreate();
    }

    private void initCreate() {
        getRealm().beginTransaction();
        cityToEdit= getRealm().createObject(City.class, UUID.randomUUID().toString());
        getRealm().commitTransaction();
    }

    private void setupUI() {
        etCityName = (EditText) findViewById(R.id.etCityName);

        Button btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePlace();
            }
        });
    }

    public Realm getRealm() {
        return ((MainApplication)getApplication()).getRealmSavedCities();
    }

    private void savePlace() {
        Intent intentResult = new Intent();

        getRealm().beginTransaction();
        cityToEdit.setCityName(etCityName.getText().toString());
        getRealm().commitTransaction();

        intentResult.putExtra(KEY_CITY, cityToEdit.getCityID());
        setResult(RESULT_OK, intentResult);
        finish();
    }
}


