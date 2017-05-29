package hu.ait.shoppinglist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.UUID;

import hu.ait.shoppinglist.data.Item;
import io.realm.Realm;

public class NewItemActivity extends AppCompatActivity {
    public static final String KEY_ITEM = "KEY_ITEM";
    private Spinner spinnerItemCategory;
    private EditText etItem;
    private EditText etItemDescription;
    private EditText etItemCost;
    private Button btnSave;
    private Button btnCancel;
    private Item itemToEdit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        setupUI();

        if (getIntent().getSerializableExtra(MainActivity.KEY_EDIT) != null) {
            initEdit();
        } else {
            initCreate();
        }
    }

    private void initCreate() {
        getRealm().beginTransaction();
        itemToEdit = getRealm().createObject(Item.class, UUID.randomUUID().toString());
        getRealm().commitTransaction();
    }

    private void initEdit() {
        String itemID = getIntent().getStringExtra(MainActivity.KEY_EDIT);
        itemToEdit = getRealm().where(Item.class)
                .equalTo("itemID", itemID)
                .findFirst();

        etItem.setText(itemToEdit.getItemTitle());
        etItemDescription.setText(itemToEdit.getItemDescription());
        etItemCost.setText(itemToEdit.getItemCost());
        spinnerItemCategory.setSelection(itemToEdit.getItemCategory().getValue());
    }

    private void setupUI() {
        setupSpinner();
        setupEditTexts();
        setupButtons();
    }

    private void setupSpinner() {
        spinnerItemCategory = (Spinner) findViewById(R.id.spinnerItemCategory);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.itemcategories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerItemCategory.setAdapter(adapter);
    }

    private void setupEditTexts() {
        etItem = (EditText) findViewById(R.id.etItemTitle);
        etItemDescription = (EditText) findViewById(R.id.etItemDescription);
        etItemCost = (EditText) findViewById(R.id.etItemCost);
    }

    private void setupButtons() {
        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveItem();
            }
        });
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });
    }

    public Realm getRealm() {
        return ((MainApplication)getApplication()).getRealmShopping();
    }

    private void saveItem() {
        if ("".equals(etItem.getText().toString())) {
            etItem.setError(getString(R.string.error_empty_item_name));
        } else {
            Intent intentResult = new Intent();
            saveNewValues();
            intentResult.putExtra(KEY_ITEM, itemToEdit.getItemID());
            setResult(RESULT_OK, intentResult);
            finish();
        }
    }

    private void saveNewValues() {
        getRealm().beginTransaction();
        itemToEdit.setItemTitle(etItem.getText().toString());
        itemToEdit.setItemDescription(etItemDescription.getText().toString());
        itemToEdit.setItemCategory(spinnerItemCategory.getSelectedItemPosition());
        itemToEdit.setItemCost(etItemCost.getText().toString());
        getRealm().commitTransaction();
    }

    private void cancel() {
        Intent intentResult = new Intent();
        intentResult.putExtra(KEY_ITEM, itemToEdit.getItemID());
        setResult(RESULT_CANCELED, intentResult);
        finish();
    }
}
