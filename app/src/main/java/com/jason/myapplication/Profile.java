package com.jason.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class Profile extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static final int REQUEST_CALL_PERMISSION = 1;
    private String emergencyContactNumber;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);

        // Get Emergency Contact from SharedPreferences
        emergencyContactNumber = sharedPreferences.getString("emergencyContact", null);

        if (emergencyContactNumber == null) {
            Toast.makeText(Profile.this, "No emergency contact found", Toast.LENGTH_SHORT).show();
        }

        // Edit Profile
        Button editProfile = findViewById(R.id.editProfileButton);
        editProfile.setOnClickListener(v -> {
            Intent intent = new Intent(Profile.this, EditProfile.class);
            startActivity(intent);
        });

        // Call Emergency Contact
        Button emergencyContact = findViewById(R.id.callContactButton);
        emergencyContact.setOnClickListener(v -> {
            if (emergencyContactNumber != null && !emergencyContactNumber.isEmpty()) {
                makeEmergencyCall();
            } else {
                Toast.makeText(Profile.this, "Emergency contact not available", Toast.LENGTH_SHORT).show();
            }
        });

        // Assessment
        Button assessmentButton = findViewById(R.id.takeAssessmentButton);
        assessmentButton.setOnClickListener(v -> {
            Intent intent = new Intent(Profile.this, Assessment.class);
            startActivity(intent);
        });

        // Logout
        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> logoutUser());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
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

    private void makeEmergencyCall() {
        // Check if CALL_PHONE permission is granted
        if (ContextCompat.checkSelfPermission(Profile.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // Request permission
            ActivityCompat.requestPermissions(Profile.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
        } else {
            // Make the call
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + emergencyContactNumber));
            startActivity(callIntent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);  // Call the superclass method

        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makeEmergencyCall();  // Permission granted, proceed with the call
            } else {
                Toast.makeText(this, "Permission DENIED to make calls", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void logoutUser() {
        // Sign out the user
        mAuth.signOut();
        // Redirect to the login screen
        Intent intent = new Intent(Profile.this, AccountIntro.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // Finish the current activity
    }
}
