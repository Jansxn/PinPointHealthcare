package com.jason.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jason.myapplication.R; // Ensure this is correct

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkLoginStatus();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Use addOnNavigationItemSelectedListener
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_home) {// Already in MainActivity, do nothing or handle as needed
                return true;
            } else if (itemId == R.id.menu_assessment) {
                Intent assessmentIntent = new Intent(MainActivity.this, Assessment.class);
                startActivity(assessmentIntent);
                return true;
            } else if (itemId == R.id.menu_profile) {
                Intent profileIntent = new Intent(MainActivity.this, Profile.class);
                startActivity(profileIntent);
                return true;
            }
            return false;
        });
    }

    void checkLoginStatus() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(MainActivity.this, AccountIntro.class);
            startActivity(intent);
            finish();
        } else {
            setContentView(R.layout.activity_main);
        }
    }

}
