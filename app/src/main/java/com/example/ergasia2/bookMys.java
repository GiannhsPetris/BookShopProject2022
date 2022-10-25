package com.example.ergasia2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class bookMys extends AppCompatActivity {

    private RecyclerView recyclerView;
    modelAdapter adapter;
    DatabaseReference fbase;
    private Button backBtn, cartBtn;
    private TextView title;
    public static Context context;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_fan);
        context = getApplicationContext(); // get context
        fbase = FirebaseDatabase.getInstance("https://ergasia-aleph-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("books").child("books_mys"); //get db reference
        recyclerView = findViewById(R.id.recy);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Model> options = new FirebaseRecyclerOptions.Builder<Model>().setQuery(fbase, Model.class).build(); // create firebase recycler view


        // Connecting object of required Adapter class to
        // the Adapter class itself
        adapter = new modelAdapter(options);
        // Connecting Adapter class with the Recycler view*/
        recyclerView.setAdapter(adapter);


        backBtn = findViewById(R.id.backBtn);
        cartBtn = findViewById(R.id.cartBtn);
        title = findViewById(R.id.textView6);


        // returns to front page
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

        //goes to cart page
        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),shop.class));
            }
        });

    }


    @Override protected void onStart()
    {
        super.onStart();
        adapter.startListening();
    }

    // Function to tell the app to stop getting
    // data from database on stopping of the activity
    @Override protected void onStop()
    {
        super.onStop();
        adapter.stopListening();
    }
}
