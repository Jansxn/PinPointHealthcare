package com.jason.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainGuidelines extends Fragment {

    public MainGuidelines() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_guidelines, container, false);

        // Find the buttons for dietary guidelines
        Button btnVegetarian = view.findViewById(R.id.btn_vegetarian_guidelines);
        Button btnVegan = view.findViewById(R.id.btn_vegan_guidelines);
        Button btnIndian = view.findViewById(R.id.btn_indian_guidelines);

        // Set click listeners for each button
        btnVegetarian.setOnClickListener(v -> openGuidelines("https://www.mayoclinic.org/healthy-lifestyle/nutrition-and-healthy-eating/in-depth/vegetarian-diet/art-20046446"));

        btnVegan.setOnClickListener(v -> openGuidelines("https://www.nhs.uk/live-well/eat-well/how-to-eat-a-balanced-diet/the-vegan-diet/"));

        btnIndian.setOnClickListener(v -> openGuidelines("https://main.icmr.nic.in/sites/default/files/upload_documents/DGI_07th_May_2024_fin.pdf"));

        return view;
    }

    // Helper method to open a web link
    private void openGuidelines(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}
