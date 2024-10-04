package com.jason.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class MainVideos extends Fragment {

    public MainVideos() {
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
        View view = inflater.inflate(R.layout.fragment_main_videos, container, false);

        // Find the LinearLayouts by their IDs
        LinearLayout videoThumbnailLayout1 = view.findViewById(R.id.vid1);
        LinearLayout videoThumbnailLayout2 = view.findViewById(R.id.vid2);
        LinearLayout videoThumbnailLayout3 = view.findViewById(R.id.vid3);
        LinearLayout videoThumbnailLayout4 = view.findViewById(R.id.vid4);

        // Set click listeners using the reusable function
        setOnClickListenerForVideo(videoThumbnailLayout1, "https://www.example.com/healthy-eating-tips");
        setOnClickListenerForVideo(videoThumbnailLayout2, "https://www.example.com/exercise-tips");
        setOnClickListenerForVideo(videoThumbnailLayout3, "https://www.example.com/sleep-better-tips");
        setOnClickListenerForVideo(videoThumbnailLayout4, "https://www.example.com/mental-health-tips");

        return view;
    }

    // Reusable function to set click listener for opening videos
    private void setOnClickListenerForVideo(LinearLayout layout, String url) {
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to open a web browser
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });
    }
}
