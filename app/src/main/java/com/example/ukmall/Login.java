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

                        Intent intent = new Intent(getApplicationContext(), Homepage.class);
//                        intent.putExtra("name", name);
                        startActivity(intent);

                        //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
//                        DatabaseReference userRef = reference.child(username);

//                        userRef.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                String name = String.valueOf(snapshot.child("name").getValue());
//
//
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });


                    } else {
                        Toast.makeText(Login.this, "Invalid Username or Password", Toast.LENGTH_LONG).show();

                    }
                }
            });

           // Toast.makeText(Login.this, "Next Step", Toast.LENGTH_SHORT).show();

        }

//        public void readUsername(String email){
//
//            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
//            reference.child(email).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DataSnapshot> task) {
//
//                    if(task.isSuccessful()){
//
//                        if(task.getResult().exists()){
//                            DataSnapshot snapshot = task.getResult();
//                            String name = String.valueOf(snapshot.child("name").getValue());
//                        }
//
//                    }else{
//                        Toast.makeText(Login.this, "Failed", Toast.LENGTH_SHORT).show();
//                    }
//
//                }
//            });
//        }
    }

