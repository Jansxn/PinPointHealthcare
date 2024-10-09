package com.jason.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
        setOnClickListenerForVideo(videoThumbnailLayout1, "https://www.youtube.com/watch?v=jwWpTAXu-Sg");
        setOnClickListenerForVideo(videoThumbnailLayout2, "https://www.youtube.com/watch?v=eTxO5ZMxcsc");
        setOnClickListenerForVideo(videoThumbnailLayout3, "https://www.youtube.com/watch?v=3QIfkeA6HBY");
        setOnClickListenerForVideo(videoThumbnailLayout4, "https://www.youtube.com/watch?v=fk-_SwHhLLc");

        Button viewMore = view.findViewById(R.id.viewMoreVideosButton);
        viewMore.setOnClickListener(v -> {
            // Create an intent to open a web browser
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/results?search_query=nutrition+experts"));
            startActivity(intent);
        });
        return view;
    }

    // Reusable function to set click listener for opening videos
    private void setOnClickListenerForVideo(LinearLayout layout, String url) {
        layout.setOnClickListener(v -> {
            // Create an intent to open a web browser
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });
    }
}
