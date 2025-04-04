package com.example.caretail;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Training extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_training);
        ImageButton back=findViewById(R.id.btnback);
        ImageButton handshake=findViewById(R.id.btn_handshake);
        ImageButton leash=findViewById(R.id.btn_leash_training);
        ImageButton obidience=findViewById(R.id.btn_obedience);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Training.this, Dashboard.class);
                startActivity(intent);
                finish();
            }
        });
        handshake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Training.this, Handshake.class);
                startActivity(intent);
                finish();
            }
        });
        leash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Training.this, LeashTraining.class);
                startActivity(intent);
                finish();
            }
        });
        obidience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Training.this, Obidience.class);
                startActivity(intent);
                finish();
            }
        });

    }
}