package com.example.ergasia2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class cartAdapter extends RecyclerView.Adapter<cartAdapter.MyViewHolder2> {

    private Context context;
    private ArrayList<CartModel> cartList;
    private OnPriceValueChange onPriceValueChange;
    DatabaseReference fbase;

    // constructor
    public cartAdapter(Context context, ArrayList<CartModel> cartList, OnPriceValueChange onPriceValueChange) {
        this.context = context;
        this.cartList = cartList;
        this.onPriceValueChange = onPriceValueChange;
    }


    // the heart of the recycler view. here data are added to each view and click listeners and other are added
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder2 holder, int position) {

        // takes the object from the list of objects
        CartModel model = cartList.get(position);

        // adds the data to the views fields
        holder.name.setText(model.getName());
        holder.price.setText("Price: " + model.getPrice());
        holder.nump.setValue(Integer.parseInt(model.getQuantity()));
        int p = holder.nump.getValue() * Integer.valueOf(model.getPrice());
        holder.totalNum.setText(Integer.toString(p));


        // listens to changes on the number picker and calls the onPriceValueChange to update the total price
        holder.nump.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                int p = newVal * Integer.valueOf(model.getPrice());
                int pOld = oldVal * Integer.valueOf(model.getPrice());
                holder.totalNum.setText(Integer.toString(p));
                onPriceValueChange.onPriceChange(p, pOld);
            }
        });

        // connects to the db and searches for the given item with the id
        String id = model.getId();
        String cat = "books_" + model.getCat();
        fbase = FirebaseDatabase.getInstance("https://ergasia-aleph-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("books").child(cat).child(id).child("availability");

        //listens when stock of the book has changed and adjusts the number picker for the quantity and the total price accordingly
        fbase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int val = holder.nump.getValue();
                int pOld = val * Integer.valueOf(model.getPrice());
                int availability = Integer.valueOf(snapshot.getValue(String.class));
                holder.nump.setMaxValue(availability);
                if (holder.nump.getValue()!=0) Toast.makeText(context, "Stock has Changed for Book " + model.getId() + "!", Toast.LENGTH_SHORT).show();

                if (val > availability){
                    int p = availability * Integer.valueOf(model.getPrice());
                    holder.nump.setValue(availability);
                    holder.totalNum.setText(Integer.toString(p));
                    onPriceValueChange.onPriceChange(p, pOld);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    // interface to call method from class shop
    public interface OnPriceValueChange {
        void onPriceChange(Integer price, Integer priceOld);
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }




    @NonNull
    @Override
    public MyViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_list, parent, false);
        return new cartAdapter.MyViewHolder2(view);
    }


    // is like a normal activity where we link the xml fields to objects
    public static class MyViewHolder2 extends RecyclerView.ViewHolder{

       TextView name, price, total, totalNum;
       NumberPicker nump;
       ArrayList<CartModel> cartList;



        public MyViewHolder2(@NonNull View itemView) {
            super(itemView);

            nump = itemView.findViewById(R.id.numpick);
            name = itemView.findViewById(R.id.textView13);
            price = itemView.findViewById(R.id.textView14);
            total = itemView.findViewById(R.id.textView15);
            totalNum = itemView.findViewById(R.id.textView16);

            nump.setMinValue(0);
            nump.setMaxValue(15);



        }
    }
}
