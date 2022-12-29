package com.example.ukmall;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity implements View.OnClickListener {

    EditText nameET,emailET,passwordET,password2ET;
    TextView loginTV;
    Button ButtonSignup;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        nameET = (EditText) findViewById(R.id.et_usernamesg);
        emailET = (EditText) findViewById(R.id.et_email);
        passwordET = (EditText) findViewById(R.id.et_passwordsg);
        password2ET = (EditText) findViewById(R.id.et_passwordsg2);
        ButtonSignup = (Button) findViewById(R.id.bt_signupsg);
        ButtonSignup.setOnClickListener(this);
        loginTV= (TextView) findViewById(R.id.tv_loginsg);
        loginTV.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

    }
        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.bt_signupsg:
                    registerUser();
                    break;

                case R.id.tv_loginsg:
                    startActivity(new Intent(this,Login.class));
                    break;



            }

        }

    private void registerUser() {
        String name = nameET.getText().toString().trim();
        String email = emailET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();
        String password2 = password2ET.getText().toString().trim();

        if (name.isEmpty()) {
            nameET.setError("Name is required!");
            nameET.requestFocus();
            return;
        }
        else if (email.isEmpty()) {
            emailET.setError("Email is required!");
            emailET.requestFocus();
            return;
        }

        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailET.setError("Please provide valid email!");
            emailET.requestFocus();
            return;

        }

        else if (password.isEmpty()) {
            passwordET.setError("Password is required!");
            passwordET.requestFocus();
            return;
        }
        else if (password.length() < 6) {
            passwordET.setError("Min. password length should be 6 characters!");
            passwordET.requestFocus();
            return;
        }

        else if (password2.isEmpty()) {
            password2ET.setError("Please re-enter your password!");
            password2ET.requestFocus();
            return;
        }
        else if (!password2.equals(password)) {
            password2ET.setError("Your password does not matched!");
            password2ET.requestFocus();
            return;
        }
        //else
        {
//            Send data to realtime database in firebase

//            Create user in authentication database
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
//                Check berjaya ke tak
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){
//                        Create new User Object
                        Users user = new user(email,name);

//                        Masukkan object user dalam Realtime Database
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(Register.this, "User registered successfully. Try login again, Thankyouu!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(Register.this, "User realtime registration failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(Register.this, "User registration failed", Toast.LENGTH_SHORT).show();
                    }

                }
            });
            finish();
        }

        }
    }
