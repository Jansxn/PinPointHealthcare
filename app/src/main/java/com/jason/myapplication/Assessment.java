package com.jason.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Assessment extends AppCompatActivity {

    // Declare UI elements
    private EditText inputWeight, inputHeight, inputExerciseFrequency;
    private SeekBar inputEnergyLevels, inputSleepQuality;
    private Button submitButton;
    private TextView resultWeight, resultHeight, resultExerciseFrequency, resultEnergyLevels, resultSleepQuality;

    // SQLite Database instance
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment);

        // Initialize SQLite helper
        dbHelper = new DatabaseHelper(this);

        // Initialize UI elements
        inputWeight = findViewById(R.id.inputWeight);
        inputHeight = findViewById(R.id.inputHeight);
        inputExerciseFrequency = findViewById(R.id.inputExerciseFrequency);
        inputEnergyLevels = findViewById(R.id.inputEnergyLevels);
        inputSleepQuality = findViewById(R.id.inputSleepQuality);
        submitButton = findViewById(R.id.submitButton);
        resultWeight = findViewById(R.id.resultWeight);
//        resultHeight = findViewById(R.id.resultHeight);
        resultExerciseFrequency = findViewById(R.id.resultExerciseFrequency);
        resultEnergyLevels = findViewById(R.id.resultEnergyLevels);
        resultSleepQuality = findViewById(R.id.resultSleepQuality);

        // Set up the submit button click listener
        submitButton.setOnClickListener(v -> handleSubmit());

        // Bottom Navigation setup
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.menu_assessment);
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

        // Load initial values from the last assessment in the database
        loadLastAssessment();
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

        try {
            float weightValue = Float.parseFloat(weight);
            float heightValue = Float.parseFloat(height);

            // Compare with the last assessment
            compareAssessments(weightValue, heightValue, exerciseFrequency, energyLevels, sleepQuality);

            // Insert new assessment into SQLite database
            dbHelper.insertAssessment(weightValue, heightValue, exerciseFrequency, energyLevels, sleepQuality);

            Toast.makeText(Assessment.this, "Assessment saved successfully", Toast.LENGTH_LONG).show();

        } catch (NumberFormatException e) {
            Toast.makeText(Assessment.this, "Please enter valid numbers for weight and height", Toast.LENGTH_SHORT).show();
        }
    }

    private void compareAssessments(float currentWeight, float currentHeight, String currentExerciseFrequency, int currentEnergyLevels, int currentSleepQuality) {
        Cursor cursor = dbHelper.getLastAssessment();

        if (cursor != null && cursor.moveToFirst()) {
            // Verify column names from the cursor for debugging (optional)
            String[] columnNames = cursor.getColumnNames();
            for (String column : columnNames) {
                System.out.println("Column: " + column);  // Log the columns to check their names
            }

            // Retrieve the last assessment values, ensuring correct column names are used
            int weightIndex = cursor.getColumnIndex("weight");
            int heightIndex = cursor.getColumnIndex("height");
            int exerciseFrequencyIndex = cursor.getColumnIndex("exerciseFrequency");
            int energyLevelsIndex = cursor.getColumnIndex("energyLevels");
            int sleepQuality = cursor.getColumnIndex("sleepQuality");

            // Check if columns exist in the cursor (getColumnIndex returns -1 if the column is not found)
            if (weightIndex >= 0 && heightIndex >= 0 && exerciseFrequencyIndex >= 0 && energyLevelsIndex >= 0) {
                float lastWeight = cursor.getFloat(weightIndex);
                float lastHeight = cursor.getFloat(heightIndex);
                String lastExerciseFrequency = cursor.getString(exerciseFrequencyIndex);
                int lastSleep = cursor.getInt(sleepQuality);
                int lastEnergyLevels = cursor.getInt(energyLevelsIndex);

                // Check for improvements
                String weightImprovement;
                if (currentWeight < lastWeight) {
                    weightImprovement = "You have improved your weight by " + (lastWeight - currentWeight) + " kg!";
                    resultWeight.setTextColor(ContextCompat.getColor(this, R.color.green));
                } else {
                    weightImprovement = "No improvement in weight.";
                    resultWeight.setTextColor(ContextCompat.getColor(this, R.color.red));
                }

                if (Integer.parseInt(currentExerciseFrequency) > Integer.parseInt(lastExerciseFrequency)) {
                    resultExerciseFrequency.setText("Exercise Frequency has improved");
                    resultExerciseFrequency.setTextColor(ContextCompat.getColor(this, R.color.green));
                }
                else {
                    resultExerciseFrequency.setText("Exercise Frequency has worsened");
                    resultExerciseFrequency.setTextColor(ContextCompat.getColor(this, R.color.red));
                }

                if (currentSleepQuality > lastSleep){
                    resultSleepQuality.setText("Sleep Quality has improved");
                    resultSleepQuality.setTextColor(ContextCompat.getColor(this, R.color.green));
                }
                else {
                    resultSleepQuality.setText("Sleep Quality has not improved ");
                    resultSleepQuality.setTextColor(ContextCompat.getColor(this, R.color.red));
                }

                // Update result TextViews
                resultWeight.setText("Weight: " + currentWeight + " kg (" + weightImprovement + ")");

                // Check for energy level improvement
                if (currentEnergyLevels > lastEnergyLevels) {
                    resultEnergyLevels.setText("Energy levels have improved!");
                    resultEnergyLevels.setTextColor(ContextCompat.getColor(this, R.color.green));
                } else {
                    resultEnergyLevels.setText("No improvement in energy levels.");
                    resultEnergyLevels.setTextColor(ContextCompat.getColor(this, R.color.red));
                }
            } else {
                resultEnergyLevels.setText("Error: One or more columns are missing.");
            }

            cursor.close();
        } else {
            resultEnergyLevels.setText("No previous assessment data found.");
        }
    }

    private void loadLastAssessment() {
        Cursor cursor = dbHelper.getLastAssessment();

        if (cursor != null && cursor.moveToFirst()) {
            // Retrieve the last assessment values
            int weightIndex = cursor.getColumnIndex("weight");
            int heightIndex = cursor.getColumnIndex("height");
            int exerciseFrequencyIndex = cursor.getColumnIndex("exerciseFrequency");
            int energyLevelsIndex = cursor.getColumnIndex("energyLevels");
            int sleepQualityIndex = cursor.getColumnIndex("sleepQuality");

            // Check if columns exist
            if (weightIndex >= 0 && heightIndex >= 0 && exerciseFrequencyIndex >= 0 && energyLevelsIndex >= 0 && sleepQualityIndex >= 0) {
                float lastWeight = cursor.getFloat(weightIndex);
                float lastHeight = cursor.getFloat(heightIndex);
                String lastExerciseFrequency = cursor.getString(exerciseFrequencyIndex);
                int lastEnergyLevels = cursor.getInt(energyLevelsIndex);
                int lastSleepQuality = cursor.getInt(sleepQualityIndex);

                // Set the values of the input fields with the last assessment data
                inputWeight.setText(String.valueOf(lastWeight));
                inputHeight.setText(String.valueOf(lastHeight));
                inputExerciseFrequency.setText(lastExerciseFrequency);
                inputEnergyLevels.setProgress(lastEnergyLevels);
                inputSleepQuality.setProgress(lastSleepQuality);
            } else {
                // If no data found, set defaults or handle it accordingly
                Toast.makeText(this, "No previous assessment found.", Toast.LENGTH_SHORT).show();
            }

            cursor.close();
        } else {
            // If no previous assessment is found, you can set default values
            Toast.makeText(this, "No previous assessment found.", Toast.LENGTH_SHORT).show();
        }
    }
}
