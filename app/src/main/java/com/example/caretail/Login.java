package com.example.caretail;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.FirebaseException;

import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import java.util.concurrent.TimeUnit;
import androidx.annotation.NonNull;




public class Login extends AppCompatActivity {

    EditText etPhone;
    Button btnSendOtp;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();

        etPhone = findViewById(R.id.etPhone);
        btnSendOtp = findViewById(R.id.btnSendOtp);

        btnSendOtp.setOnClickListener(view -> {
            String phone = etPhone.getText().toString().trim();

            if (phone.isEmpty() || phone.length() != 10) {
                etPhone.setError("Enter a valid 10-digit phone number");
                return;
            }

            String fullPhone = "+91" + phone;

            PhoneAuthOptions options =
                    PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                            .setPhoneNumber(fullPhone)
                            .setTimeout(60L, TimeUnit.SECONDS)
                            .setActivity(this)
                            .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                                    // Auto retrieval or instant verification
                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {
                                    Toast.makeText(Login.this, "Verification failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCodeSent(@NonNull String verificationId,
                                                       @NonNull PhoneAuthProvider.ForceResendingToken token) {
                                    Intent intent = new Intent(Login.this, OtpVerification.class);
                                    intent.putExtra("phone", fullPhone);
                                    intent.putExtra("verificationId", verificationId);
                                    startActivity(intent);
                                }
                            })
                            .build();
            PhoneAuthProvider.verifyPhoneNumber(options);
        });
    }
}
