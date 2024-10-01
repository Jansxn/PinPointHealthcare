package com.jason.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AccountIntro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_intro);

        Button logIn = findViewById(R.id.logIn);
        Button signUp = findViewById(R.id.signUp);

        logIn.setOnClickListener(v -> {
            startActivity(new Intent(this, Login.class));
            finish();
        });

        signUp.setOnClickListener(v -> {
            startActivity(new Intent(this, Signup.class));
            finish();
        });
    }
}