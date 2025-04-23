package com.example.caretail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileSetting extends AppCompatActivity {

    private TextView nameText, breedText, ageText, ownerText, weighhtText, allergiesText;
    private CircleImageView profileImage;
    private ImageButton backBtn, editProfileBtn;
    private DatabaseReference databaseRef;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setting);

        // Firebase
        nameText = findViewById(R.id.nameText);
        breedText = findViewById(R.id.breedText);
        ageText = findViewById(R.id.ageText);
        ownerText = findViewById(R.id.ownerText);
        weighhtText = findViewById(R.id.weighhtText);
        allergiesText = findViewById(R.id.allergiesText);
        profileImage = findViewById(R.id.profileImage);
        backBtn = findViewById(R.id.backBtn);
        editProfileBtn = findViewById(R.id.editProfileBtn);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            databaseRef = FirebaseDatabase.getInstance().getReference("Pets").child(currentUser.getUid());
            loadProfileData();
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ProfileSetting.this, Dashboard.class);
                startActivity(intent);
                finish();
            }
        });

        editProfileBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileSetting.this, Edit_profile.class);
            startActivity(intent);
        });
    }

    private void loadProfileData() {
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    nameText.setText(snapshot.child("name").getValue(String.class));
                    breedText.setText(snapshot.child("breed").getValue(String.class));
                    ageText.setText(snapshot.child("age").getValue(String.class));
                    ownerText.setText(snapshot.child("owner").getValue(String.class));
                    weighhtText.setText(snapshot.child("weight").getValue(String.class));
                    allergiesText.setText(snapshot.child("allergies").getValue(String.class));

                    String imageUrl = snapshot.child("profileImage").getValue(String.class);
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        Glide.with(ProfileSetting.this).load(Uri.parse(imageUrl)).into(profileImage);
                    }
                } else {
                    Toast.makeText(ProfileSetting.this, "No profile data found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ProfileSetting.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}
