package com.example.ergasia2;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;


public class register extends AppCompatActivity {

    public static final int AUDIO_RECORD_REQUEST_CODE = 1;
    EditText mailT, passT;
    Button signButton;
    TextView logText;
    FirebaseAuth fireAuth;
    boolean email = false; // for checking if email edittext is empty or not
    boolean pass = false; // for checking if pass edittext is empty or not
    private Integer code;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mailT = findViewById(R.id.mailField);
        passT = findViewById(R.id.passField);
        signButton = findViewById(R.id.signButton);
        logText = findViewById(R.id.goLog);

        fireAuth = FirebaseAuth.getInstance();
        signButton.setEnabled(false); // disable so it cannot be pressed before entering info on the text fields


        //takes you to the log in page
        logText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), login.class));
            }
        });


        // listeners to check if both text fields (mail and password) are empty or not. if at least one pf them is empty the log in button does not work
        mailT.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    signButton.setEnabled(false);
                    email = false;
                } else {
                    email = true;
                    if (pass) {
                        signButton.setEnabled(true);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        passT.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    signButton.setEnabled(false);
                    pass = false;
                } else {
                    pass = true;
                    if (email) {
                        signButton.setEnabled(true);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    // sign up method.takes the info given by the user and creates them an account on the database. if something goes wrong it displays the appropriate message
    public void signup(View view) {
        fireAuth.createUserWithEmailAndPassword(mailT.getText().toString(), passT.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            showMessage("Success", "User Created");
                            clearPref();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            showMessage("Error", task.getException().getLocalizedMessage());
                        }
                    }
                });
    }


    // called when user presses speech recognition btn
    public void voice_rec(View view) {
        code = Integer.valueOf(view.getTag().toString()); // takes id of button
        checkPerm(Manifest.permission.RECORD_AUDIO, AUDIO_RECORD_REQUEST_CODE);
    }

    //check the permission for audio record if is already given starts speech recognition if not asks for perm
    void checkPerm(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(register.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(register.this, new String[]{permission}, requestCode);
        } else {
            voiceStart();
        }
    }

    // if permission given starts speech rec if not notifies the user that the perm must be given
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == AUDIO_RECORD_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                voiceStart();
            } else {
                showMessage("Permission Denied", "Please Give The Permission To Use This Feature");
            }
        }
    }

    // initiates speech recognition
    void voiceStart() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "For '@' say At Sign for '_' underscore and for '.' say dot");
        startActivityForResult(intent, code);
    }

    // takes the result and inputs it at the right textfield using the id var code
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);

            // removes unwanted characters from result
            String txt = matches.toString().replace(" ", "").replace("[", "").replace("]", "").replace("atsign", "@");

            if (requestCode == 456) {
                mailT.setText(txt);
            } else {
                passT.setText(txt);
            }
        } else {
            showMessage("Error", "Something Went Wrong. Please Try Again");
        }

    }


    // method to show messages on screen
    void showMessage(String title, String message) {
        new AlertDialog.Builder(this).setTitle(title).setMessage(message).setCancelable(true).show();
    }

    // clears the save preferences whenever a new user logs in
    private void clearPref() {
        SharedPreferences mPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.clear();
        preferencesEditor.apply();
    }
}