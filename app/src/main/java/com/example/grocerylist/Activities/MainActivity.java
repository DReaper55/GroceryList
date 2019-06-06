package com.example.grocerylist.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.example.grocerylist.Data.DatabaseHandler;
import com.example.grocerylist.Model.Grocery;
import com.example.grocerylist.R;

public class MainActivity extends AppCompatActivity {
    private AlertDialog.Builder alertBuilder;
    private AlertDialog dialog;
    private EditText enterItem;
    private EditText enterQty;
    private Button saveButton;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new DatabaseHandler(this);

        if(db.totalGrocery() > 0){
            startActivity(new Intent(this, ListActivity.class));
            this.finish();
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUpDialog();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void popUpDialog(){
        alertBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.add_item, null);

        enterItem = view.findViewById(R.id.groceryItem);
        enterQty = view.findViewById(R.id.groceryQty);
        saveButton = view.findViewById(R.id.grocerySave);

        alertBuilder.setView(view);
        dialog = alertBuilder.create();

        dialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!enterItem.getText().toString().isEmpty() && !enterQty.getText().toString().isEmpty()){
                    saveGrocery(v);
                }
            }
        });

    }

    private void saveGrocery(View v) {
        Grocery grocery = new Grocery();

        String newGrocery = enterItem.getText().toString();
        String newQuantity = enterQty.getText().toString();

        grocery.setName(newGrocery);
        grocery.setQuantity(newQuantity);

        db.addGrocery(grocery);

        Snackbar.make(v, "item saved", Snackbar.LENGTH_LONG).show();

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();

                startActivity(new Intent(MainActivity.this, ListActivity.class));
            }
        }, 1500);

//        Log.d("Item ID", String.valueOf(db.totalGrocery()));
    }
}
