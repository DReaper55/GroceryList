package com.example.grocerylist.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.grocerylist.R;

public class DetailsActivity extends AppCompatActivity {
    private TextView groceryName;
    private TextView groceryQty;
    private TextView dateAdded;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        bundle = getIntent().getExtras();

        groceryName = findViewById(R.id.groceryNameID2);
        groceryQty = findViewById(R.id.groceryQtyID2);
        dateAdded = findViewById(R.id.groceryDateID2);

        if(bundle != null){
            groceryName.setText(bundle.getString("name"));
            groceryQty.setText(bundle.getString("quantity"));
            dateAdded.setText(bundle.getString("date"));
        }

    }
}
