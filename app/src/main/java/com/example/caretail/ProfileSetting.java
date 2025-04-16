package com.example.caretail;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileSetting extends AppCompatActivity {

    ImageView profileImage;
    TextView petNameView, ownerNameView, breedView, weightView, conditionView, medicationView, allergyView, vaccinationView;
    Button editProfileBtn ;
    ImageButton back;// Declare back button here

    FirebaseFirestore db;
    CollectionReference profileRef;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setting);

        // Initialize views inside onCreate()
        profileImage = findViewById(R.id.imageView5);
        petNameView = findViewById(R.id.displayPetName);
        ownerNameView = findViewById(R.id.displayOwnerName);
        breedView = findViewById(R.id.displayBreed);
        weightView = findViewById(R.id.displayWeight);
        conditionView = findViewById(R.id.displayMedicalCondition);
        medicationView = findViewById(R.id.displayMedication);
        allergyView = findViewById(R.id.displayAllergies);
        vaccinationView = findViewById(R.id.displayVaccination);
        editProfileBtn = findViewById(R.id.editProfileButton);
        back = findViewById(R.id.back_pfp); // Initialize back button here

        db = FirebaseFirestore.getInstance();
        profileRef = db.collection("PetProfiles");

        // Set listener for back button
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileSetting.this, Dashboard.class);
                startActivity(intent);
                finish();
            }
        });

        // Load profile data from Firestore
        loadProfile();

        // Handle profile edit button click
        editProfileBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileSetting.this, Edit_profile.class);
            startActivity(intent);
        });
    }

    private void loadProfile() {
        profileRef.orderBy("ownerName").limitToLast(1).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot doc = queryDocumentSnapshots.getDocuments().get(0);

                        // Set the data to the views
                        petNameView.setText(doc.getString("petName"));
                        ownerNameView.setText(doc.getString("ownerName"));
                        breedView.setText(doc.getString("breed"));
                        weightView.setText(doc.getString("weight"));
                        conditionView.setText(doc.getString("condition"));
                        medicationView.setText(doc.getString("medication"));
                        allergyView.setText(doc.getString("allergy"));
                        vaccinationView.setText(doc.getString("vaccination"));

                        // Load image if available
                        String imageUrl = doc.getString("imageUrl");
                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            Glide.with(this).load(imageUrl).into(profileImage);
                        }
                    } else {
                        Toast.makeText(this, "No profile found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error loading profile", Toast.LENGTH_SHORT).show();
                });
    }
}
