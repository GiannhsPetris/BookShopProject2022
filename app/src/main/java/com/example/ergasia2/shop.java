package com.example.ergasia2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class shop extends AppCompatActivity implements cartAdapter.OnPriceValueChange {

    TextView priceF;
    Button mainP, logP, purchase;
    RecyclerView recy2;
    cartAdapter adapter;
    ArrayList<CartModel> cartList ;
    Integer totalPrice;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        priceF = findViewById(R.id.textView17);
        mainP = findViewById(R.id.button3);
        logP = findViewById(R.id.button7);
        purchase = findViewById(R.id.button8);
        purchase.setEnabled(false);

        recy2 = findViewById(R.id.recy2);

        totalPrice = 0; // initialise total price of books

        // calling method to load data
        // from shared prefs.
        loadData();

        // calling method to build
        // recycler view.
        buildRecyclerView();


        // button to main page
        mainP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

        //button to log in page
        logP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),login.class));
            }
        });

        // buy button. checks if logged in. if yew proceeds with purchase. shows a message and clears the shared preferences
        purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null){
                    showMessage("Purchase Completed",  priceF.getText() + "\nThank you for choosing our shop");
                    clearPref();

                    // adds a delay between the message and the redirection to the front page
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }
                    }, 5000);

                }else{
                    showMessage("Error", "You must be logged in to make a purchase!");
                }
            }
        });
    }

    // method to show messages on screen
    void showMessage(String title, String message){
        new AlertDialog.Builder(this).setTitle(title).setMessage(message).setCancelable(true).show();
    }

    // clears the save preferences whenever a new user logs in
    private void clearPref(){
        SharedPreferences mPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.clear();
        preferencesEditor.apply();
    }


    private void buildRecyclerView() {
        // initializing our adapter class.
        adapter = new cartAdapter(shop.this, cartList, this);

        // adding layout manager to our recycler view.
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recy2.setHasFixedSize(true);

        // setting layout manager to our recycler view.
        recy2.setLayoutManager(manager);

        // setting adapter to our recycler view.
        recy2.setAdapter(adapter);
    }

    private void loadData() {
        // method to load arraylist from shared prefs
        // initializing our shared prefs with name as
        // shared preferences.
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);

        // creating a variable for gson.
        Gson gson = new Gson();

        // below line is to get to string present from our
        // shared prefs if not present setting it as null.
        String json = sharedPreferences.getString("cart", null);

        // below line is to get the type of our array list.
        Type type = new TypeToken<ArrayList<CartModel>>() {}.getType();

        // in below line we are getting data from gson
        // and saving it to our array list
        cartList = gson.fromJson(json, type);

        // checking below if the array list is empty or not
        if (cartList == null) {
            // if the array list is empty
            // creating a new array list.
            cartList = new ArrayList<>();
        }
    }

    // computes the final price. each time a price of a book changes because of a change in quantity adds the new cost and redacts the old one
    @Override
    public void onPriceChange(Integer price, Integer priceOld) {
        totalPrice = totalPrice + price - priceOld;
        priceF.setText("Total Price is : "+ Integer.toString(totalPrice));
        if (totalPrice != 0){
            purchase.setEnabled(true);
        }else{
            purchase.setEnabled(false);
        }

    }
}