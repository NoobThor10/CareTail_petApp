package com.example.caretail;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import de.hdodenhof.circleimageview.CircleImageView;

public class Edit_profile extends AppCompatActivity {

    private EditText nameInput, ageInput, ownerInput, weightInput;
    private Spinner breedSpinner, allergySpinner;
    private CircleImageView editProfileImage;
    private ImageButton backBtn, saveBtn;
    private Button selectImageBtn;

    private static final int PICK_IMAGE = 1;
    private Uri imageUri;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        nameInput = findViewById(R.id.nameInput);
        ageInput = findViewById(R.id.ageInput);
        ownerInput = findViewById(R.id.ownerInput);
        weightInput = findViewById(R.id.weightInput);
        breedSpinner = findViewById(R.id.breedSpinner);
        allergySpinner = findViewById(R.id.allergySpinner);
        editProfileImage = findViewById(R.id.editProfileImage);
        selectImageBtn = findViewById(R.id.selectImageBtn);
        backBtn = findViewById(R.id.backBtn);
        saveBtn = findViewById(R.id.saveBtn);

        databaseReference = FirebaseDatabase.getInstance().getReference("Pets").child("defaultPetId");

        populateSpinners();
        loadExistingData();

        selectImageBtn.setOnClickListener(v -> pickImage());
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Edit_profile.this, Dashboard.class);
                startActivity(intent);
                finish();
            }
        });
        saveBtn.setOnClickListener(v -> saveData());
    }

    private void populateSpinners() {
        List<String> breeds = Arrays.asList("Select Breed", "Labrador", "German Shepherd", "Bulldog");
        List<String> allergies = Arrays.asList("None", "Pollen", "Dust", "Food");

        ArrayAdapter<String> breedAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, breeds);
        breedSpinner.setAdapter(breedAdapter);

        ArrayAdapter<String> allergyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, allergies);
        allergySpinner.setAdapter(allergyAdapter);
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                editProfileImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadExistingData() {
        databaseReference.get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                nameInput.setText(snapshot.child("name").getValue(String.class));
                ageInput.setText(snapshot.child("age").getValue(String.class));
                ownerInput.setText(snapshot.child("owner").getValue(String.class));
                weightInput.setText(snapshot.child("weight").getValue(String.class));
                // Set spinner values if needed
            }
        });
    }

    private void saveData() {
        String name = nameInput.getText().toString();
        String age = ageInput.getText().toString();
        String owner = ownerInput.getText().toString();
        String weight = weightInput.getText().toString();
        String breed = breedSpinner.getSelectedItem().toString();
        String allergy = allergySpinner.getSelectedItem().toString();

        HashMap<String, String> map = new HashMap<>();
        map.put("name", name);
        map.put("age", age);
        map.put("owner", owner);
        map.put("weight", weight);
        map.put("breed", breed);
        map.put("allergy", allergy);

        databaseReference.setValue(map).addOnSuccessListener(unused ->
                Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
        );
    }
}
