package com.example.caretail;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class OtpVerification extends AppCompatActivity {

    private EditText otpInput, phoneDisplay;
    private Button verifyButton;
    private String phone;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        otpInput = findViewById(R.id.otp_input);
        phoneDisplay = findViewById(R.id.phone_display);
        verifyButton = findViewById(R.id.verify_button);

        db = FirebaseFirestore.getInstance();

        phone = getIntent().getStringExtra("phone");
        phoneDisplay.setText(phone);

        verifyButton.setOnClickListener(v -> {
            String enteredOtp = otpInput.getText().toString().trim();

            if (enteredOtp.isEmpty() || enteredOtp.length() < 6) {
                otpInput.setError("Enter a valid OTP");
                return;
            }

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("PredefinedOtps")
                    .whereEqualTo("phone", phone)
                    .whereEqualTo("otp", enteredOtp)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // OTP is correct - save user login
                            String uid = phone + "_" + enteredOtp;  // or some unique ID
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("phone", phone);
                            userData.put("otp", enteredOtp);
                            userData.put("timestamp", FieldValue.serverTimestamp());

                            db.collection("LoggedInUsers").document(uid)
                                    .set(userData)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(this, Dashboard.class));
                                        finish();
                                    })
                                    .addOnFailureListener(e ->
                                            Toast.makeText(this, "Failed to save user: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                                    );

                        } else {
                            Toast.makeText(this, "Incorrect OTP", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Error checking OTP: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
        });

    }

    private void saveUser(String phone) {
        String uid = "user_" + System.currentTimeMillis();

        Map<String, Object> userData = new HashMap<>();
        userData.put("uid", uid);
        userData.put("phone", phone);
        userData.put("timestamp", FieldValue.serverTimestamp());

        db.collection("Users")
                .document(uid)
                .set(userData)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, Dashboard.class));
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to save user", Toast.LENGTH_SHORT).show()
                );
    }
}
