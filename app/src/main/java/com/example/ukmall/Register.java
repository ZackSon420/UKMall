package com.example.ukmall;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity implements View.OnClickListener {

    EditText nameET,emailET,passwordET,password2ET;
    TextView loginTV;
    Button ButtonSignup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        nameET = (EditText) findViewById(R.id.et_name);
        emailET = (EditText) findViewById(R.id.et_email);
        passwordET = (EditText) findViewById(R.id.et_password);
        password2ET = (EditText) findViewById(R.id.et_password2);
        ButtonSignup = (Button) findViewById(R.id.bt_signup);
        ButtonSignup.setOnClickListener(this);
        loginTV= (TextView) findViewById(R.id.tv_login);
        loginTV.setOnClickListener(this);

    }
        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.bt_signup:
                    registerUser();
                    break;

                case R.id.tv_login:
                    //startActivity(new Intent(this,MainActivity.class));
                    //break;
                    Toast.makeText(Register.this, "Soon!", Toast.LENGTH_SHORT).show();


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
        if (email.isEmpty()) {
            emailET.setError("Email is required!");
            emailET.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailET.setError("Please provide valid email!");
            emailET.requestFocus();
            return;

        }

        if (password.isEmpty()) {
            passwordET.setError("Password is required!");
            passwordET.requestFocus();
            return;
        }
        if (password.length() < 6) {
            passwordET.setError("Min. password length should be 6 characters!");
            passwordET.requestFocus();
            return;
        }

        if (password2.isEmpty()) {
            password2ET.setError("Please re-enter your password!");
            password2ET.requestFocus();
            return;
        }
        if (!password2.equals(password)) {
            password2ET.setError("Your password does not matched!");
            password2ET.requestFocus();
            return;
        }

        {
            Toast.makeText(Register.this, "Next Step", Toast.LENGTH_SHORT).show();
        }

        }
    }
