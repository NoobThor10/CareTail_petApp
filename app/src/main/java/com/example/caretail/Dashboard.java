package com.example.caretail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ImageButton healthRecords=findViewById(R.id.healthRecords);
        ImageButton shopping= findViewById(R.id.shop);
        ImageButton chat_button=findViewById(R.id.chat);
        ImageButton diet=findViewById(R.id.dietPlan);
        ImageButton training=findViewById(R.id.training);
        ImageButton profile=findViewById(R.id.btn_profile);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Dashboard.this,ProfileSetting.class);
                startActivity(intent);
                finish();
            }
        });

        chat_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Dashboard.this,PawTalk.class);
                startActivity(intent);
                finish();
            }
        });

        shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Dashboard.this,Shop.class);
                startActivity(intent);
                finish();
            }
        });

        healthRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Dashboard.this,HealthRecords.class);
                startActivity(intent);
                finish();
            }
        });
        diet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Dashboard.this, DietPlan.class);
                startActivity(intent);
                finish();

            }
        });
        training.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Dashboard.this, Training.class);
                startActivity(intent);
                finish();
            }
        });


    }
}