package com.example.grocerylist.Activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grocerylist.R;

public class DetailsActivity extends AppCompatActivity {
    private TextView groceryName;
    private TextView groceryQty;
    private TextView dateAdded;
    private Bundle bundle;
    private FloatingActionButton shareButt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        bundle = getIntent().getExtras();

        groceryName = findViewById(R.id.groceryNameID2);
        groceryQty = findViewById(R.id.groceryQtyID2);
        dateAdded = findViewById(R.id.groceryDateID2);
        shareButt = findViewById(R.id.shareID);

        shareButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setShareButt();
            }
        });

        if(bundle != null){
            groceryName.setText(bundle.getString("name"));
            groceryQty.setText(bundle.getString("quantity"));
            dateAdded.setText(bundle.getString("date"));
        }

    }

    public void setShareButt(){
        StringBuilder dataString = new StringBuilder();

            String name = groceryName.getText().toString();
            String quantity = groceryQty.getText().toString();
            String date = dateAdded.getText().toString();

            dataString.append(" Grocery: " + name + "\n");
            dataString.append(" Quantity: " + quantity + "\n");
            dataString.append(" Date Added: " + date);

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("message/rfc822");
        share.putExtra(Intent.EXTRA_SUBJECT, "My Grocery");
        share.putExtra(Intent.EXTRA_EMAIL, new String[]{"receipeint@example.com"});
        share.putExtra(Intent.EXTRA_TEXT, dataString.toString());

        try{
            startActivity(Intent.createChooser(share, "Send mail..."));
        }catch(ActivityNotFoundException e){
            Toast.makeText(getApplicationContext(), "Install an email client", Toast.LENGTH_LONG).show();
        }
    }
}
