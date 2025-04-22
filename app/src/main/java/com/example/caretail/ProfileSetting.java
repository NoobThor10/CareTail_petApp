package com.example.caretail;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileSetting extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseFirestore firestore;

    TextView petName, age, ownerName, weight, breed, condition, medication, allergies, vaccination;
    Button editProfileButton;
    ImageButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setting);

        // Firebase
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // View Bindings
        petName = findViewById(R.id.displayPetName);
        age = findViewById(R.id.displayAge); // (Not used in Firestore but declared in XML)
        ownerName = findViewById(R.id.displayOwnerName);
        weight = findViewById(R.id.displayWeight);
        breed = findViewById(R.id.displayBreed);
        condition = findViewById(R.id.displayMedicalCondition);
        medication = findViewById(R.id.displayMedication);
        allergies = findViewById(R.id.displayAllergies);
        vaccination = findViewById(R.id.displayVaccination);

        editProfileButton = findViewById(R.id.editProfileButton);
        backBtn = findViewById(R.id.back_pfp);

        // Get Current User
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = user.getUid();

        // Fetch Profile from Firestore
        firestore.collection("PetProfiles").document(uid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        petName.setText(documentSnapshot.getString("petName"));
                        ownerName.setText(documentSnapshot.getString("ownerName"));
                        weight.setText(documentSnapshot.getString("weight"));
                        breed.setText(documentSnapshot.getString("breed"));
                        condition.setText(documentSnapshot.getString("condition"));
                        medication.setText(documentSnapshot.getString("medication"));
                        allergies.setText(documentSnapshot.getString("allergy"));
                        vaccination.setText(documentSnapshot.getString("vaccination"));

                        // Optional - If age is available in future
                        if (documentSnapshot.contains("age")) {
                            age.setText(documentSnapshot.getString("age"));
                        }
                    } else {
                        Toast.makeText(this, "Profile not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });

        // Edit Button → Go to EditProfileActivity
        editProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileSetting.this, Edit_profile.class);
            startActivity(intent);
        });

        // Back Button
        backBtn.setOnClickListener(v -> onBackPressed());
    }
}
