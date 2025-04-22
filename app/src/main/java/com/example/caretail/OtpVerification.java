package com.example.caretail;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;



import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.concurrent.TimeUnit;

import java.util.HashMap;


public class OtpVerification extends AppCompatActivity {

    EditText etPhone, etOtp;
    Button btnVerify;
    String verificationId, phone;

    FirebaseAuth auth;
    DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        etPhone = findViewById(R.id.etPhone);
        etOtp = findViewById(R.id.etOtp);
        btnVerify = findViewById(R.id.btnVerify);

        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference("Users");

        // Get values from intent
        verificationId = getIntent().getStringExtra("verificationId");
        phone = getIntent().getStringExtra("phone");

        // Show phone number in EditText
        etPhone.setText(phone);

        btnVerify.setOnClickListener(view -> {
            String otp = etOtp.getText().toString().trim();

            if (otp.isEmpty() || otp.length() != 6) {
                etOtp.setError("Enter valid 6-digit OTP");
                return;
            }

            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);

            auth.signInWithCredential(credential)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(OtpVerification.this, "Login Successful", Toast.LENGTH_SHORT).show();

                            // Store user data in Firebase Realtime DB
                            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                            HashMap<String, Object> map = new HashMap<>();
                            map.put("phone", phone);
                            map.put("uid", uid);

                            userRef.child(uid).setValue(map)
                                    .addOnSuccessListener(aVoid -> {
                                        startActivity(new Intent(OtpVerification.this, Dashboard.class));
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(this, "Failed to save user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            Toast.makeText(OtpVerification.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}
