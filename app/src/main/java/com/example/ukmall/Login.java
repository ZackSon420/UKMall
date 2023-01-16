package com.example.ukmall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ukmall.viewmodel.CartViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity implements View.OnClickListener{

    EditText usernameET,passwordET;
    Button ButtonLogin;
    TextView signupTV;
    FirebaseAuth mAuth;
    private SessionManager sessionManager;
    CartViewModel cartViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        usernameET = (EditText) findViewById(R.id.et_usernamelg);
        passwordET = (EditText) findViewById(R.id.et_passwordlg);

        ButtonLogin = (Button)  findViewById(R.id.bt_loginlg);
        ButtonLogin.setOnClickListener(this);

        signupTV = (TextView) findViewById(R.id.tv_signuplg);
        signupTV.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        //Declare Session
       sessionManager = new SessionManager(this);





    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.bt_loginlg:
                loginUser();
                break;

            case R.id.tv_signuplg:
                startActivity(new Intent(this,Register.class));
                break;



        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        //sessionManager.checkLogin();
        //Check Session whether User is Logged in.
        if (sessionManager.isLoggedIn()) {
            Intent intent = new Intent(Login.this, Homepage.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }


        private void loginUser(){
            String username = usernameET.getText().toString().trim();
            String password = passwordET.getText().toString().trim();

            if (username.isEmpty()) {
                usernameET.setError("Username is required!");
                usernameET.requestFocus();
                return;
            }

            if (password.isEmpty()) {
                passwordET.setError("Password is required");
                passwordET.requestFocus();
                return;
            }
            if(password.length()<6){
                passwordET.setError("Minimum password length is 6 character");
                passwordET.requestFocus();
                return;
            }

            mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        //Create Session For Each User
                        sessionManager.createSession(username,password);
                        Intent intent = new Intent(getApplicationContext(), Homepage.class);
                        startActivity(intent);
                        finish();


                    } else {
                        Toast.makeText(Login.this, "Invalid Username or Password", Toast.LENGTH_LONG).show();

                    }
                }
            });

        }

    }

