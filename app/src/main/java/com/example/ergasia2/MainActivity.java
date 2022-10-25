package com.example.ergasia2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    TextView title, logoff;
    Button fantasy, mystery, scifi, cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logoff = findViewById(R.id.textView4);
        fantasy = findViewById(R.id.button4);
        mystery = findViewById(R.id.button5);
        scifi = findViewById(R.id.button);
        cart = findViewById(R.id.button6);
        title = findViewById(R.id.textView3);


        //check if the user is logged in to display their name
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String email = user.getEmail();
            int index = email.indexOf('@');
            email = email.substring(0,index);
            title.setText("Welcome " + email + " !");
            logoff.setText("Log Off");
        }else{
            title.setText("Welcome Guest!");
            logoff.setText("Log in or Register");
        }

        //button that logouts the user and returns him to the log in screen
        logoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), login.class));
            }
        });

        // button that goes to shop/cart page
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),shop.class));
            }
        });

        // buttons that send the user to the book categories pages

        fantasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),bookFan.class));
            }
        });

        mystery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),bookMys.class));
            }
        });

        scifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),bookSci.class));
            }
        });

    }
}