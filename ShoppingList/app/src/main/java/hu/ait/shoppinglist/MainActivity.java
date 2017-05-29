package hu.ait.shoppinglist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hu.ait.shoppinglist.adapter.ShoppingRecyclerAdapter;
import hu.ait.shoppinglist.data.Item;
import hu.ait.shoppinglist.touch.ShoppingItemTouchHelperCallback;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_SHOPPING_ID = "KEY_SHOPPING_ID";
    private ShoppingRecyclerAdapter shoppingRecyclerAdapter;
    private RecyclerView recyclerShopping;
    public static final int REQUEST_NEW_ITEM = 101;
    public static final int REQUEST_EDIT_ITEM = 102;
    public static final String KEY_EDIT = "KEY_EDIT";
    private int placeToEditPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((MainApplication)getApplication()).openRealm();


        setupUI();
    }

    private void setupUI() {
        setUpToolBar();
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        recyclerShopping = (RecyclerView) findViewById(R.id.recyclerShopping);
        recyclerShopping.setHasFixedSize(true);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerShopping.setLayoutManager(layoutManager);
        RealmResults<Item> allItems = getRealm().where(Item.class).findAll();
        Item itemsArray[] = new Item[allItems.size()];
        List<Item> itemsResult = new ArrayList<Item>(Arrays.asList(allItems.toArray(itemsArray)));

        shoppingRecyclerAdapter = new ShoppingRecyclerAdapter(itemsResult, this, this.getRealm());

        recyclerShopping.setAdapter(shoppingRecyclerAdapter);

        ItemTouchHelper.Callback callback = new ShoppingItemTouchHelperCallback(shoppingRecyclerAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerShopping);
    }


    private void setUpToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.add_item) {
            openNewItemActivity();
        } else if (id == R.id.delete_all) {
            shoppingRecyclerAdapter.clearItems();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((MainApplication)getApplication()).closeRealm();
    }

    private void openNewItemActivity() {
        Intent intentStart = new Intent(MainActivity.this,
                NewItemActivity.class);
        startActivityForResult(intentStart, REQUEST_NEW_ITEM);
    }

    public void openEditActivity(String placeID, int position) {
        Intent intentStart = new Intent(MainActivity.this,
                NewItemActivity.class);
        placeToEditPosition = position;

        intentStart.putExtra(KEY_EDIT, placeID);
        startActivityForResult(intentStart, REQUEST_EDIT_ITEM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                String itemID  = data.getStringExtra(
                        NewItemActivity.KEY_ITEM);

                Item item = getRealm().where(Item.class)
                        .equalTo("itemID", itemID)
                        .findFirst();

                if (requestCode == REQUEST_NEW_ITEM) {
                    shoppingRecyclerAdapter.addItem(item);
                } else if (requestCode == REQUEST_EDIT_ITEM) {


                    shoppingRecyclerAdapter.updateItem(placeToEditPosition, item);
                }
                break;
            case RESULT_CANCELED:
                break;
        }
    }

    public Realm getRealm() {
        return ((MainApplication)getApplication()).getRealmShopping();
    }
}
