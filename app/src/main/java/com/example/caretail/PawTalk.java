package com.example.caretail;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.View;
import android.widget.ImageButton;

public class PawTalk extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_paw_talk);

        // Apply insets for system bars (status bar, navigation bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize the back button and set the click listener
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            // Create an Intent to navigate back to the Dashboard
            Intent intent = new Intent(PawTalk.this, Dashboard.class);
            startActivity(intent);
            finish(); // Finish this activity so the user can't return to it by pressing back
        });

        // Load the fragment when activity is created
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.mainContent, new chats())  // Assuming `chats` is a Fragment
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        // Handle the back press like the ImageButton click
        Intent intent = new Intent(PawTalk.this, Dashboard.class);
        startActivity(intent);
        finish(); // Finish the current activity
    }
}
