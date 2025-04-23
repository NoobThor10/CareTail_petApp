package com.example.caretail;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Clinic extends AppCompatActivity {

    private RecyclerView clinicRecyclerView;
    private ClinicAdapter clinicAdapter;
    private List<clinicModel> clinicList;
    private EditText searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic);

        clinicRecyclerView = findViewById(R.id.clinic_recycler);
        searchBar = findViewById(R.id.search_bar);

        ImageButton  backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(view -> {
            // Navigate back to the Dashboard Activity
            Intent intent = new Intent(Clinic.this, Dashboard.class);
            startActivity(intent);
            finish();  // Optional: Close the current activity
        });

        clinicRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        clinicList = new ArrayList<>();
        clinicAdapter = new ClinicAdapter(this, clinicList);
        clinicRecyclerView.setAdapter(clinicAdapter);

        // Hardcoded data
        clinicList.add(new clinicModel("Happy Pets Clinic", "New York, NY", "9:00 AM", "https://example.com/image1.jpg"));
        clinicList.add(new clinicModel("Paws & Claws", "Los Angeles, CA", "10:00 AM", "https://example.com/image2.jpg"));
        clinicList.add(new clinicModel("PetCare Center", "Chicago, IL", "8:30 AM", "https://example.com/image3.jpg"));
        clinicList.add(new clinicModel("Furry Friends Hospital", "Houston, TX", "11:00 AM", "https://example.com/image4.jpg"));

        clinicAdapter.notifyDataSetChanged();

        // Optional: Save to Firebase
        saveClinicsToDatabase(clinicList);


    }

    private void saveClinicsToDatabase(List<clinicModel> clinics) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Clinics");

        for (clinicModel clinic : clinics) {
            String id = reference.push().getKey();
            reference.child(id).setValue(clinic);
        }

        //Toast.makeText(this, "Clinics saved to Firebase", Toast.LENGTH_SHORT).show();
    }
}
