package com.example.ukmall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity implements View.OnClickListener{

    EditText usernameET,passwordET;
    Button ButtonLogin;
    TextView signupTV;

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
            Toast.makeText(Login.this, "Next Step", Toast.LENGTH_SHORT).show();

        }
    }

