package com.example.grocerylist.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.grocerylist.Data.DatabaseHandler;
import com.example.grocerylist.Model.Grocery;
import com.example.grocerylist.R;
import com.example.grocerylist.UI.MyAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private List<Grocery> groceries;
    private List<Grocery> listItems;
    private DatabaseHandler db;
    private EditText enterItem;
    private EditText enterQty;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        alertBuild();

        setUpRecycler();


    }

    private void setUpRecycler(){
        recyclerView = findViewById(R.id.recyclerID);
        db = new DatabaseHandler(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        groceries = new ArrayList<>();
        listItems = new ArrayList<>();

        // Get all items from database
        groceries = db.getAllGrocery();

        for(Grocery c : groceries){
            Grocery grocery = new Grocery();
            grocery.setId(c.getId());
            grocery.setName(c.getName());
            grocery.setQuantity("Qty: " + c.getQuantity());
            grocery.setDateItemAdded("Added on: " + c.getDateItemAdded());

            listItems.add(grocery);
        }

        adapter = new MyAdapter(this, listItems);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void alertBuild(){
        FloatingActionButton fab = findViewById(R.id.fabID);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setUpAlert(view);


            }
        });
    }

    private void setUpAlert(View view){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(view.getContext());
        view = getLayoutInflater().inflate(R.layout.add_item, null);

        enterItem = view.findViewById(R.id.groceryItem);
        enterQty = view.findViewById(R.id.groceryQty);
        Button saveButton = view.findViewById(R.id.grocerySave);

        alertBuilder.setView(view);
        dialog = alertBuilder.create();

        dialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!enterItem.getText().toString().isEmpty() && !enterQty.getText().toString().isEmpty()){

                    saveDB(v);
                }
            }
        });


    }

    private void saveDB(View v){
        Grocery grocery = new Grocery();

        String newGrocery = enterItem.getText().toString();
        String newQuantity = enterQty.getText().toString();

        grocery.setName(newGrocery);
        grocery.setQuantity(newQuantity);

        db.addGrocery(grocery);

        Toast.makeText(v.getContext(), "Item added", Toast.LENGTH_LONG).show();

        dialog.dismiss();

        finish();
        startActivity(getIntent());
    }

}
