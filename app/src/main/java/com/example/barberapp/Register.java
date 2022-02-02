package com.example.barberapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {
EditText rFullName,rEmail,rPassword,rPhoneNumber;
Button rRegisterBtn;
TextView rLogInBtn;
FirebaseAuth rAuth;
ProgressBar rProgressBar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findViews();

        rLogInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LogIn.class));

            }
        });
        if(rAuth.getCurrentUser() != null){

            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            finish();
        }

        rRegisterBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                String email = rEmail.getText().toString().trim();
                String password = rPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    rEmail.setError("Email is Required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    rPassword.setError("Password is Required");
                    return;
                }
                if(password.length() < 6){
                    rPassword.setError("Password Must be at least 6 characters ");
                    return;
                }

                rProgressBar.setVisibility(View.VISIBLE);

                // Register User to the Fire Base

                rAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Register.this, "User Created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),HomeActivity .class));
                        }else {
                            Toast.makeText(Register.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            rProgressBar.setVisibility(View.GONE);
                        }
                    }
                });


            }
        });
    }

    private void findViews() {
        rFullName = findViewById(R.id.register_Full_Name);
        rEmail = findViewById(R.id.register_Email);
        rPassword = findViewById(R.id.register_Password);
        rPhoneNumber = findViewById(R.id.register_phoneNumber);
        rRegisterBtn = findViewById(R.id.register_Btn);
        rLogInBtn  = findViewById(R.id.register_createText);

        rAuth = FirebaseAuth.getInstance();
        rProgressBar = findViewById(R.id.progressBar);
        rProgressBar.setVisibility(View.INVISIBLE);

    }
}