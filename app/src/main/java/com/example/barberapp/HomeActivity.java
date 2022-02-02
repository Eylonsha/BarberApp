package com.example.barberapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.barberapp.Common.Common;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.example.barberapp.Model.User;

import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import com.example.barberapp.fragments.HomeFragment;
import com.example.barberapp.fragments.ShoppingFragment;

public class HomeActivity extends AppCompatActivity {
    Button logOff;
    //@BindViews(R.id.logOutBtn)
    BottomNavigationView bottomNavigationView;
    BottomSheetDialog bottomSheetDialog;
    CollectionReference userRef;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //logOff = findViewById(R.id.logOutBtn);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        ButterKnife.bind(HomeActivity.this);

        //init
        userRef = FirebaseFirestore.getInstance().collection("User");
        dialog = new SpotsDialog.Builder().setContext(this).build();
        if (getIntent() != null) {

            boolean isLogin = getIntent().getBooleanExtra(Common.IS_LOGIN, false);

            if (isLogin) {
                dialog.show();
// check if user exists
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                DocumentReference currentUser = userRef.document(user.getPhoneNumber().toString());
                currentUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot userSnapShot = task.getResult();
                            if (!userSnapShot.exists()) {
                                showUpdateDialog(user.getPhoneNumber());
                            } else {
                                Common.currentUser = userSnapShot.toObject(User.class);
                                bottomNavigationView.setSelectedItemId(R.id.action_home);
                            }
                            if (dialog.isShowing())
                                dialog.dismiss();
                        }
                    }
                });
            }
        }


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            Fragment fragment = null;

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_home)
                    fragment = new HomeFragment();
                else if (menuItem.getItemId() == R.id.action_shopping)
                    fragment = new ShoppingFragment();

                return loadFragment(fragment);
            }
        });


    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

            return true;
        }
        return false;
    }

    private void showUpdateDialog(String phoneNumber) {
        if (dialog.isShowing())
            dialog.dismiss();

        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.setCancelable(false);
        View sheetView = getLayoutInflater().inflate(R.layout.layout_update_information, null);
        Button btn_update = (Button) sheetView.findViewById(R.id.btn_update);
        final TextInputEditText edt_name = (TextInputEditText) sheetView.findViewById(R.id.edt_name);
        final TextInputEditText edt_address = (TextInputEditText) sheetView.findViewById(R.id.edt_address);

        btn_update.setOnClickListener(v -> {
            User user = new User(edt_name.getText().toString(),
                    edt_address.getText().toString(),
                    phoneNumber);

            userRef.document(phoneNumber).set(user).addOnSuccessListener(unused -> {
                bottomSheetDialog.dismiss();
                if (dialog.isShowing())
                    dialog.dismiss();

                Common.currentUser = user;
                bottomNavigationView.setSelectedItemId(R.id.action_home);
                Toast.makeText(HomeActivity.this, "Thank You", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    bottomSheetDialog.dismiss();
                    Toast.makeText(HomeActivity.this, "Wrong Input", Toast.LENGTH_SHORT).show();
                }
            });

        });
        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}
