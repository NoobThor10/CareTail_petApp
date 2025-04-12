package com.example.caretail;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.caretail.R;

public class Login extends AppCompatActivity {

    private EditText input;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        input = findViewById(R.id.input);
        login = findViewById(R.id.login);

        login.setOnClickListener(v -> {
            String phone = input.getText().toString().trim();
            if (phone.length() != 10) {
                input.setError("Enter valid 10-digit number");
                return;
            }

            phone = "+91" + phone;

            Intent intent = new Intent(Login.this, Dashboard.class);
            intent.putExtra("phone", phone);
            startActivity(intent);
        });
    }
}
