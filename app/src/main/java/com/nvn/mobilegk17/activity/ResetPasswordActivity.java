package com.nvn.mobilegk17.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.nvn.mobilegk17.R;
import com.nvn.mobilegk17.database.DBLogin;

import java.util.concurrent.TimeUnit;

public class ResetPasswordActivity extends AppCompatActivity {
    com.apachat.loadingbutton.core.customViews.CircularProgressButton button;
    EditText email_phone;
    private FirebaseAuth mAuth;
    DBLogin db;
    private static final String TAG = VerifyEmailActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        button = findViewById(R.id.cirContButton);
        email_phone = findViewById(R.id.editTextEmailPhone);
        mAuth = FirebaseAuth.getInstance();

        System.out.println("NVN   "+mAuth);
        db = new DBLogin(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = email_phone.getText().toString();
                onClickVerify(phone);
            }
        });
    }

    private void onClickVerify(String phone) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phone)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(ResetPasswordActivity.this, "Verify Failed", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verifycationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(verifycationId, forceResendingToken);
                                goToVerifyResetPasswordActivity(phone, verifycationId);
                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // Update UI
                            gotoMainActivity(user.getPhoneNumber());
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(ResetPasswordActivity.this, "The verification code entered was invalid", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }


                });
    }

    private void gotoMainActivity(String phoneNumber) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void goToVerifyResetPasswordActivity(String phone, String verifycationId) {
        Intent intent = new Intent(this, VerifyResetPasswordActivity.class);
        intent.putExtra("phone_number", phone);
        intent.putExtra("verifycationId", verifycationId);
        startActivity(intent);
    }

    public void backLogin(View view) {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }
}