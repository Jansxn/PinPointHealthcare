package com.jason.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class account_intro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_intro);

        Button logIn = findViewById(R.id.logIn);
        Button signUp = findViewById(R.id.signUp);

        logIn.setOnClickListener(v -> {
            startActivity(new Intent(this, login.class));
            finish();
        });

        signUp.setOnClickListener(v -> {
            startActivity(new Intent(this, signup.class));
            finish();
        });
    }
}