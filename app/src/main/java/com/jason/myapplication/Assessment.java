//package com.jason.myapplication;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.SeekBar;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//
//public class Assessment extends AppCompatActivity {
//
//    // Declare UI elements
//    private EditText inputWeight, inputHeight, inputExerciseFrequency;
//    private SeekBar inputEnergyLevels, inputSleepQuality;
//    private Button submitButton;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_assessment);
//
//        // Initialize UI elements
//        inputWeight = findViewById(R.id.inputWeight);
//        inputHeight = findViewById(R.id.inputHeight);
//        inputExerciseFrequency = findViewById(R.id.inputExerciseFrequency);
//        inputEnergyLevels = findViewById(R.id.inputEnergyLevels);
//        inputSleepQuality = findViewById(R.id.inputSleepQuality);
//        submitButton = findViewById(R.id.submitButton);
//
//        // Set default progress for SeekBars
//        inputEnergyLevels.setProgress(5);
//        inputSleepQuality.setProgress(5);
//
//        // Set up the submit button click listener
//        submitButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                handleSubmit();
//            }
//        });
//
//
//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
//        // Use addOnNavigationItemSelectedListener
//        bottomNavigationView.setOnItemSelectedListener(item -> {
//            int itemId = item.getItemId();
//            if (itemId == R.id.menu_home) {
//                Intent profileIntent = new Intent(Assessment.this, MainActivity.class);
//                startActivity(profileIntent);
//                return true;
//            } else if (itemId == R.id.menu_assessment) {
//                return true;
//            } else if (itemId == R.id.menu_profile) {
//                Intent profileIntent = new Intent(Assessment.this, Profile.class);
//                startActivity(profileIntent);
//                return true;
//            }
//            return false;
//        });
//    }
//
//    // Method to handle the submission logic
//    private void handleSubmit() {
//        // Retrieve inputs from EditTexts and SeekBars
//        String weight = inputWeight.getText().toString();
//        String height = inputHeight.getText().toString();
//        String exerciseFrequency = inputExerciseFrequency.getText().toString();
//        int energyLevels = inputEnergyLevels.getProgress();
//        int sleepQuality = inputSleepQuality.getProgress();
//
//        // Validate inputs
//        if (weight.isEmpty() || height.isEmpty() || exerciseFrequency.isEmpty()) {
//            Toast.makeText(Assessment.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Validate if height and weight inputs are valid numbers
//        try {
//            float weightValue = Float.parseFloat(weight);
//            float heightValue = Float.parseFloat(height);
//
//            // Proceed with further logic, e.g., calculations or sending data
//            String summary = "Weight: " + weightValue + " kg\n" +
//                    "Height: " + heightValue + " cm\n" +
//                    "Exercise Frequency: " + exerciseFrequency + " times/week\n" +
//                    "Energy Levels: " + energyLevels + "/10\n" +
//                    "Sleep Quality: " + sleepQuality + "/10";
//
//            // Show a confirmation Toast with the assessment summary
//            Toast.makeText(Assessment.this, "Assessment Submitted:\n" + summary, Toast.LENGTH_LONG).show();
//
//        } catch (NumberFormatException e) {
//            // Handle the case when height or weight are not valid numbers
//            Toast.makeText(Assessment.this, "Please enter valid numbers for height and weight", Toast.LENGTH_SHORT).show();
//        }
//    }
//}
package com.jason.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Assessment extends AppCompatActivity {

    // Declare UI elements
    private EditText inputWeight, inputHeight, inputExerciseFrequency;
    private SeekBar inputEnergyLevels, inputSleepQuality;
    private Button submitButton;

    // Firebase instances
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize UI elements
        inputWeight = findViewById(R.id.inputWeight);
        inputHeight = findViewById(R.id.inputHeight);
        inputExerciseFrequency = findViewById(R.id.inputExerciseFrequency);
        inputEnergyLevels = findViewById(R.id.inputEnergyLevels);
        inputSleepQuality = findViewById(R.id.inputSleepQuality);
        submitButton = findViewById(R.id.submitButton);

        // Set default progress for SeekBars
        inputEnergyLevels.setProgress(5);
        inputSleepQuality.setProgress(5);

        // Set up the submit button click listener
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSubmit();
            }
        });

        // Bottom Navigation setup
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_home) {
                startActivity(new Intent(Assessment.this, MainActivity.class));
                return true;
            } else if (itemId == R.id.menu_assessment) {
                return true;
            } else if (itemId == R.id.menu_profile) {
                startActivity(new Intent(Assessment.this, Profile.class));
                return true;
            }
            return false;
        });
    }

    private void handleSubmit() {
        // Retrieve inputs from EditTexts and SeekBars
        String weight = inputWeight.getText().toString();
        String height = inputHeight.getText().toString();
        String exerciseFrequency = inputExerciseFrequency.getText().toString();
        int energyLevels = inputEnergyLevels.getProgress();
        int sleepQuality = inputSleepQuality.getProgress();

        // Validate inputs
        if (weight.isEmpty() || height.isEmpty() || exerciseFrequency.isEmpty()) {
            Toast.makeText(Assessment.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate if height and weight inputs are valid numbers
        try {
            float weightValue = Float.parseFloat(weight);
            float heightValue = Float.parseFloat(height);

            // Get current authenticated user
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                // Create a map to store the assessment details
                Map<String, Object> assessmentData = new HashMap<>();
                assessmentData.put("weight", weightValue);
                assessmentData.put("height", heightValue);
                assessmentData.put("exerciseFrequency", exerciseFrequency);
                assessmentData.put("energyLevels", energyLevels);
                assessmentData.put("sleepQuality", sleepQuality);
                assessmentData.put("userId", user.getUid());  // Add user ID to the data

                // Save assessment data to Firestore
                db.collection("Assessments")
                        .document(user.getUid())  // Use user's UID as document ID
                        .set(assessmentData)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(Assessment.this, "Assessment saved successfully", Toast.LENGTH_LONG).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(Assessment.this, "Error saving assessment: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        });
            } else {
                Toast.makeText(Assessment.this, "User not authenticated", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            // Handle the case when height or weight are not valid numbers
            Toast.makeText(Assessment.this, "Please enter valid numbers for height and weight", Toast.LENGTH_SHORT).show();
        }
    }
}
