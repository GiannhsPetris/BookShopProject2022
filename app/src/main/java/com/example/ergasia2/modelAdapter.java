package com.example.ergasia2;

import android.content.Context;
import android.content.SharedPreferences;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;

public class modelAdapter extends FirebaseRecyclerAdapter<Model, modelAdapter.MyViewHolder1>{


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    private Context context;


    public modelAdapter(@NonNull FirebaseRecyclerOptions<Model> options) {
        super(options);
        this.context = context;
    }



    // the heart of the recycler view. here data are added to each view and  listeners and other are added
    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder1 holder, int position, @NonNull Model model) {

        holder.priceNum.setText(model.getPrice());
        holder.availNum.setText(model.getAvailability());
        holder.title.setText(model.getName());
        holder.desc.setText(model.getDescription());
        context = holder.itemView.getContext();
        Glide.with(context).load(model.getImageUrl()).into(holder.imageView); // set image to image view without downloading it

        // hides the add to cart btn if there is no stock
        if (model.getAvailability().equals("0")){
            holder.cartBtn.setText("Not Available");
            holder.cartBtn.setEnabled(false);
        }

        // tts method
        holder.textToSpeech = new TextToSpeech(context.getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS){
                    int lang = holder.textToSpeech.setLanguage(Locale.ENGLISH);
                }
            }
        });

        // initiates tts when long pressing on description
        holder.desc.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                CharSequence s = holder.desc.getText();
                int speech = holder.textToSpeech.speak(s, TextToSpeech.QUEUE_FLUSH, null, null);
                return false;
            }
        });

        // clicking the image stops tts
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( holder.textToSpeech != null) {
                    holder.textToSpeech.stop();
                }
            }
        });

        // checks if book is already on cart(shared pref) if not adds it
        holder.cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData(holder);
                // if list is empty always add
                if (holder.cartList.size() == 0){
                    holder.cartList.add(new CartModel(model.getName(), model.getPrice(), "0", model.getId(), model.getCat() ));
                    saveData(holder);
                }else{
                    Boolean check = false;
                    //if not search if it exists and if it does not then add it
                    for (int i = 0; i < holder.cartList.size(); i++){
                        if(holder.cartList.get(i).getName().equals(model.getName())) check = true;
                    }
                    if(check){
                        holder.cartBtn.setEnabled(false);
                        holder.cartBtn.setText("Already Added");
                    }else{
                        holder.cartList.add(new CartModel(model.getName(), model.getPrice(), "0", model.getId(), model.getCat()));
                        saveData(holder);
                    }
                }

            }
        });
    }


    private void loadData(@NonNull MyViewHolder1 holder) {
        context = holder.itemView.getContext();
        // method to load arraylist from shared prefs
        // initializing our shared prefs with name as
        // shared preferences.
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", context.MODE_PRIVATE);

        // creating a variable for gson.
        Gson gson = new Gson();

        // below line is to get to string present from our
        // shared prefs if not present setting it as null.
        String json = sharedPreferences.getString("cart", null);

        // below line is to get the type of our array list.
        Type type = new TypeToken<ArrayList<CartModel>>() {}.getType();

        // in below line we are getting data from gson
        // and saving it to our array list
        holder.cartList = gson.fromJson(json, type);

        // checking below if the array list is empty or not
        if (holder.cartList == null) {
            // if the array list is empty
            // creating a new array list.
            holder.cartList = new ArrayList<>();
        }
    }

    private void saveData(@NonNull MyViewHolder1 holder) {
        // method for saving the data in array list.
        // creating a variable for storing data in
        // shared preferences.
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", context.MODE_PRIVATE);

        // creating a variable for editor to
        // store data in shared preferences.
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // creating a new variable for gson.
        Gson gson = new Gson();

        // getting data from gson and storing it in a string.
        String json = gson.toJson(holder.cartList);

        // below line is to save data in shared
        // prefs in the form of string.
        editor.putString("cart", json);

        // below line is to apply changes
        // and save data in shared prefs.
        editor.apply();

        // after saving data we are displaying a toast message.
        Toast.makeText(context, "Added to Cart!", Toast.LENGTH_SHORT).show();
    }



    @NonNull
    @Override
    public MyViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list, parent, false);
        return new modelAdapter.MyViewHolder1(view);
    }

    // is like a normal activity where we link the xml fields to objects
    public static class MyViewHolder1 extends RecyclerView.ViewHolder{

        ImageView imageView;
        Button cartBtn;
        TextView title, avail, availNum, price, priceNum, desc;
        ArrayList<CartModel> cartList;
        TextToSpeech textToSpeech;




        public MyViewHolder1(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            cartBtn = itemView.findViewById(R.id.button2);
            title = itemView.findViewById(R.id.name);
            avail = itemView.findViewById(R.id.textView8);
            availNum = itemView.findViewById(R.id.textView9);
            price = itemView.findViewById(R.id.textView10);
            priceNum = itemView.findViewById(R.id.textView11);
            desc = itemView.findViewById(R.id.editTextTextMultiLine);


        }
    }
}