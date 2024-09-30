package com.jason.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "PinPointHealth";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkLoginStatus();
    }

    void checkLoginStatus(){
        // Check if user is logged in

        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean(KEY_IS_LOGGED_IN, false);

        if (!isLoggedIn) {
            Intent intent = new Intent(MainActivity.this, account_intro.class);
            startActivity(intent);
            finish(); // Close MainActivity
        } else {
            // Set the content view for the logged-in user
            setContentView(R.layout.activity_main);
            // Initialize your UI components and logic here
        }
    }
}