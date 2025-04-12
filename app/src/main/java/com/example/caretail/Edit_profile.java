package com.example.caretail;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.UUID;

public class Edit_profile extends AppCompatActivity {

    private static final int GALLERY_REQUEST_CODE = 100;
    private static final int PERMISSION_REQUEST_CODE = 101;

    ImageView profilePicture;
    EditText etPetName, etOwnerName, etWeight, etMedicalCondition, etMedication;
    Spinner spinnerBreed, spinnerAllergies, spinnerVaccination;
    Button btnSave;

    Uri selectedImageUri;
    FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        profilePicture = findViewById(R.id.profile_picture);
        etPetName = findViewById(R.id.etPetName);
        etOwnerName = findViewById(R.id.etOwnerName);
        etWeight = findViewById(R.id.etWeight);
        etMedicalCondition = findViewById(R.id.etMedicalCondition);
        etMedication = findViewById(R.id.etMedication);
        spinnerBreed = findViewById(R.id.spinnerBreed);
        spinnerAllergies = findViewById(R.id.spinnerAllergies);
        spinnerVaccination = findViewById(R.id.spinnerVaccination);
        btnSave = findViewById(R.id.btnSave);

        profilePicture.setOnClickListener(v -> checkGalleryPermission());

        btnSave.setOnClickListener(v -> {
            if (selectedImageUri != null) {
                uploadImageAndSaveProfile();
            } else {
                saveProfile(null);
            }
        });
    }

    private void checkGalleryPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_MEDIA_IMAGES}, PERMISSION_REQUEST_CODE);
            } else {
                openGallery();
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            } else {
                openGallery();
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            profilePicture.setImageURI(selectedImageUri);
        }
    }

    private void uploadImageAndSaveProfile() {
        String fileName = "images/" + UUID.randomUUID().toString();
        StorageReference imgRef = storageRef.child(fileName);

        imgRef.putFile(selectedImageUri)
                .addOnSuccessListener(taskSnapshot -> imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    saveProfile(imageUrl);
                }))
                .addOnFailureListener(e -> Toast.makeText(this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void saveProfile(String imageUrl) {
        String petName = etPetName.getText().toString().trim();
        String ownerName = etOwnerName.getText().toString().trim();
        String weight = etWeight.getText().toString().trim();
        String condition = etMedicalCondition.getText().toString().trim();
        String medication = etMedication.getText().toString().trim();

        String breed = spinnerBreed.getSelectedItem().toString();
        String allergy = spinnerAllergies.getSelectedItem().toString();
        String vaccination = spinnerVaccination.getSelectedItem().toString();

        if (petName.isEmpty() || ownerName.isEmpty()) {
            Toast.makeText(this, "Pet name and owner name required!", Toast.LENGTH_SHORT).show();
            return;
        }

        HashMap<String, Object> profile = new HashMap<>();
        profile.put("petName", petName);
        profile.put("ownerName", ownerName);
        profile.put("weight", weight);
        profile.put("condition", condition);
        profile.put("medication", medication);
        profile.put("breed", breed);
        profile.put("allergy", allergy);
        profile.put("vaccination", vaccination);

        if (imageUrl != null) {
            profile.put("imageUrl", imageUrl);
        }

        db.collection("PetProfiles")
                .add(profile)
                .addOnSuccessListener(documentReference ->
                        Toast.makeText(this, "Profile saved successfully!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error saving profile: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE &&
                grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
        }
    }
}
