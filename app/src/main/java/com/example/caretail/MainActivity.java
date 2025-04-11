package com.example.caretail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;


public class MainActivity extends AppCompatActivity {






    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       ImageView logo=findViewById(R.id.imageView);

        // Fade-in animation
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(2000); // 2 seconds
        logo.startAnimation(fadeIn);

        // Move to LoginActivity after 3 seconds
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(MainActivity.this,Login.class);
            startActivity(intent);
            finish(); // Close the splash screen
        }, 3000); // 3 seconds delay*
    }
}