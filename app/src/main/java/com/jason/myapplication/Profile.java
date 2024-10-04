package com.jason.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class Profile extends AppCompatActivity {

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();

        //Edit Profile


        //Edit Medicine Info


        //Assessment


        //Logout
        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> {
            logoutUser();
        });


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        // Use addOnNavigationItemSelectedListener
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_home) {
                Intent profileIntent = new Intent(Profile.this, MainActivity.class);
                startActivity(profileIntent);
                return true;
            } else if (itemId == R.id.menu_assessment) {
                Intent assessmentIntent = new Intent(Profile.this, Assessment.class);
                startActivity(assessmentIntent);
                return true;
            } else if (itemId == R.id.menu_profile) {
                return true;
            }
            return false;
        });
    }

    void logoutUser(){
        // Sign out the user
        mAuth.signOut();
        // Redirect to the login screen
        Intent intent = new Intent(Profile.this, AccountIntro.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // Finish the current activity
    }
}