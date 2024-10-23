package com.jason.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditProfile extends AppCompatActivity {
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int CAMERA_IMAGE_REQUEST = 101;

    private EditText usernameEditText, passwordEditText, weightEditText, heightEditText, emergencyContactEditText;
    private ImageView profilePictureImageView;
    private Button saveButton, changePictureButton;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private Uri imageUri;
    private final int PICK_IMAGE_REQUEST = 71;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        storageReference = FirebaseStorage.getInstance().getReference("ProfilePictures");

        // Bind UI components
        usernameEditText = findViewById(R.id.inputUsername);
        passwordEditText = findViewById(R.id.inputPassword);
        weightEditText = findViewById(R.id.inputWeight);
        heightEditText = findViewById(R.id.inputHeight);
        emergencyContactEditText = findViewById(R.id.inputEmergencyContact);
        profilePictureImageView = findViewById(R.id.profilePicture);
        saveButton = findViewById(R.id.btnSaveChanges);
        changePictureButton = findViewById(R.id.changeProfilePictureButton);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);

        // Load current user details
        loadUserDetails();

        // Button to capture profile picture
        changePictureButton.setOnClickListener(v -> askCameraPermissions());

        // Button to save the updated details
        saveButton.setOnClickListener(v -> saveUserDetails());

        // Bottom Navigation setup
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.menu_profile);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_home) {
                startActivity(new Intent(EditProfile.this, MainActivity.class));
                return true;
            } else if (itemId == R.id.menu_assessment) {
                startActivity(new Intent(EditProfile.this, Assessment.class));
                return true;
            } else if (itemId == R.id.menu_profile) {
                startActivity(new Intent(EditProfile.this, Profile.class));
                return true;
            }
            return false;
        });
    }

    // Method to request camera permissions
    private void askCameraPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        } else {
            openCamera();
        }
    }

    // Method to open camera and take a picture
    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            // Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Handle errors during file creation
                Toast.makeText(this, "Error while creating image file", Toast.LENGTH_SHORT).show();
            }
            // Continue only if the file was successfully created
            if (photoFile != null) {
                imageUri = FileProvider.getUriForFile(this, "com.example.app.fileprovider", photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(cameraIntent, CAMERA_IMAGE_REQUEST);
            }
        }
    }

    // Method to load current user details from Firebase
    private void loadUserDetails() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            databaseReference.child(userId).get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult().exists()) {
                    // Retrieve existing user details
                    DataSnapshot snapshot = task.getResult();
                    String username = snapshot.child("username").getValue(String.class);
                    String weight = snapshot.child("weight").getValue(String.class);
                    String height = snapshot.child("height").getValue(String.class);
                    String emergencyContact = snapshot.child("emergencyContact").getValue(String.class);

                    // Populate the fields with current data
                    usernameEditText.setText(username);
                    weightEditText.setText(weight);
                    heightEditText.setText(height);
                    emergencyContactEditText.setText(emergencyContact);
                } else {
                    Toast.makeText(EditProfile.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    // Method to create a unique file for the camera image
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        String currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            profilePictureImageView.setImageURI(imageUri);  // Display the captured image in the ImageView
        }
    }

    // Method to save user details to Firebase
    private void saveUserDetails() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String weight = weightEditText.getText().toString();
            String height = heightEditText.getText().toString();
            String emergencyContact = emergencyContactEditText.getText().toString();

            // Update the profile details in the database
            if (!TextUtils.isEmpty(username)) {
                databaseReference.child(userId).child("username").setValue(username);
            }

            if (!TextUtils.isEmpty(password)){
                // Update the password in Firebase Authentication
                user.updatePassword(password);
            }

            if (!TextUtils.isEmpty(weight)) {
                databaseReference.child(userId).child("weight").setValue(weight);
            }

            if (!TextUtils.isEmpty(height)) {
                databaseReference.child(userId).child("height").setValue(height);
            }

            if (!TextUtils.isEmpty(emergencyContact)) {
                databaseReference.child(userId).child("emergencyContact").setValue(emergencyContact);
                // Save to SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("emergencyContact", emergencyContact);
                editor.apply();  // Save changes asynchronously
            }

            // If a profile picture is taken, upload it
            if (imageUri != null) {
                uploadProfilePicture(userId);
            } else {
                Toast.makeText(this, "Profile details saved successfully", Toast.LENGTH_SHORT).show();
            }
        }
    }




    // Method to upload profile picture to Firebase Storage
    private void uploadProfilePicture(String userId) {
        StorageReference fileReference = storageReference.child(userId + ".jpg");
        fileReference.putFile(imageUri).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(EditProfile.this, "Profile picture uploaded successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(EditProfile.this, "Failed to upload profile picture", Toast.LENGTH_SHORT).show();
            }
        });
    }
}