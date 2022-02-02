package com.example.barberapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogIn extends AppCompatActivity {
    EditText lEmail,lPassword;
    Button lSignInBtn,lRegisterBtn;
    FirebaseAuth rAuth;
    ProgressBar rProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViews();
    lSignInBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String email = lEmail.getText().toString().trim();
            String password = lPassword.getText().toString().trim();

            if(TextUtils.isEmpty(email)){
                lEmail.setError("Email is Required");
                return;
            }
            if(TextUtils.isEmpty(password)){
                lPassword.setError("Password is Required");
                return;
            }
            if(password.length() < 6){
                lPassword.setError("Password Must be at least 6 characters ");
                return;
            }

            rProgressBar.setVisibility(View.VISIBLE);

            rAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(LogIn.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));

                    }else{
                        Toast.makeText(LogIn.this, "Incorrect Email Or Password", Toast.LENGTH_SHORT).show();
                        rProgressBar.setVisibility(View.GONE);

                    }
                }
            });
        }
    });

    }

    private void findViews() {
        lEmail = findViewById(R.id.logInEmail);
        lPassword = findViewById(R.id.logInPassword);
        lSignInBtn = findViewById(R.id.signInBtn);
        lRegisterBtn = findViewById(R.id.logInRegistrationBtn);

        rAuth = FirebaseAuth.getInstance();
        rProgressBar = findViewById(R.id.logInProgressBar);
        rProgressBar.setVisibility(View.INVISIBLE);
    }
}
