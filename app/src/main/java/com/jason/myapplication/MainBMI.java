package com.jason.myapplication;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MainBMI extends Fragment {

    private EditText heightInput;
    private EditText weightInput;
    private TextView bmiResult;
    private TextView bmiCategory;
    private Button calculateBmiButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_b_m_i, container, false);

        // Initialize UI components
        heightInput = view.findViewById(R.id.heightInput);
        weightInput = view.findViewById(R.id.weightInput);
        bmiResult = view.findViewById(R.id.bmiResult);
        bmiCategory = view.findViewById(R.id.bmiCategory);
        calculateBmiButton = view.findViewById(R.id.calculateBmiButton);

        // Set button click listener
        calculateBmiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateBMI();
            }
        });

        return view;
    }

    // Method to calculate BMI
    private void calculateBMI() {
        String heightStr = heightInput.getText().toString();
        String weightStr = weightInput.getText().toString();

        if (TextUtils.isEmpty(heightStr) || TextUtils.isEmpty(weightStr)) {
            Toast.makeText(getActivity(), "Please enter both height and weight", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert height and weight to float
        float height = Float.parseFloat(heightStr) / 100; // converting cm to meters
        float weight = Float.parseFloat(weightStr);

        // Calculate BMI
        float bmi = weight / (height * height);

        // Update the UI with the BMI result
        bmiResult.setText(String.format("%.1f", bmi));

        // Determine the BMI category
        String category = getBMICategory(bmi);
        bmiCategory.setText(category);
    }

    // Method to get the BMI category based on the BMI value
    private String getBMICategory(float bmi) {
        if (bmi < 18.5) {
            return "Underweight";
        } else if (bmi >= 18.5 && bmi < 24.9) {
            return "Normal weight";
        } else if (bmi >= 25 && bmi < 29.9) {
            return "Overweight";
        } else {
            return "Obesity";
        }
    }
}
