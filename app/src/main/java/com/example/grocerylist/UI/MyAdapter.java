package com.example.grocerylist.UI;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grocerylist.Activities.DetailsActivity;
import com.example.grocerylist.Data.DatabaseHandler;
import com.example.grocerylist.Model.Grocery;
import com.example.grocerylist.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private Context context;
    private List<Grocery> groceries;
    private EditText enterItem;
    private EditText enterQty;
    private AlertDialog dialog;

    public MyAdapter(Context context, List<Grocery> groceries) {
        this.context = context;
        this.groceries = groceries;
    }

    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, viewGroup, false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder viewHolder, int i) {
        Grocery grocery = groceries.get(i);

        viewHolder.groceryName.setText(grocery.getName());
        viewHolder.quantity.setText(grocery.getQuantity());
        viewHolder.dateAdded.setText(grocery.getDateItemAdded());
    }

    @Override
    public int getItemCount() {
        return groceries.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView groceryName;
        private TextView quantity;
        private TextView dateAdded;
        private Button editItem;
        private Button deleteItem;
        public int id;

        private ViewHolder(@NonNull View view, Context ctext) {
            super(view);
            context = ctext;

           groceryName = view.findViewById(R.id.groceryNameID);
           quantity = view.findViewById(R.id.groceryQtyID);
           dateAdded = view.findViewById(R.id.groceryDateID);
           editItem = view.findViewById(R.id.editID);
           deleteItem = view.findViewById(R.id.deleteID);

           editItem.setOnClickListener(this);
           deleteItem.setOnClickListener(this);

           view.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {

                   int position = getAdapterPosition();

                   Grocery grocery = groceries.get(position);
                   Intent intent = new Intent(context, DetailsActivity.class);

                   intent.putExtra("id", grocery.getId());
                   intent.putExtra("name", grocery.getName());
                   intent.putExtra("quantity", grocery.getQuantity());
                   intent.putExtra("date", grocery.getDateItemAdded());

                   context.startActivity(intent);

               }
           });
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.editID:
                    int position = getAdapterPosition();
                    Grocery grocery1 = groceries.get(position);

                    setUpAlert(grocery1);

                    break;

                case R.id.deleteID:
                    int pos = getAdapterPosition();
                    Grocery grocery = groceries.get(pos);

                    deleteItem(grocery.getId());

                    Toast.makeText(v.getContext(), "item removed", Toast.LENGTH_LONG).show();
                    break;
            }
        }

        private void deleteItem(int id){
            DatabaseHandler db = new DatabaseHandler(context);
            int pos = getAdapterPosition();

            db.deleteGrocery(id);
            groceries.remove(pos);
            notifyItemRemoved(pos);

        }


        private void setUpAlert(final Grocery grocery){
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.add_item, null);

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


                        updateDB(grocery);
                        dialog.dismiss();
                    }
                }
            });


        }

        private void updateDB(Grocery grocery){
            DatabaseHandler db = new DatabaseHandler(context);
            int pos = getAdapterPosition();

            grocery.setName(enterItem.getText().toString());
            grocery.setQuantity(enterQty.getText().toString());

            db.updateGrocery(grocery);

            notifyItemChanged(pos);

        }
    }



}
