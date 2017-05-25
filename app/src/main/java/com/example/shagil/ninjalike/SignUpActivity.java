package com.example.shagil.ninjalike;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class SignUpActivity extends AppCompatActivity {
    EditText userNameEditText,passwordEditText;
    Button signUpButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        userNameEditText=(EditText)findViewById(R.id.userText);
        passwordEditText=(EditText)findViewById(R.id.passwordText);

        String userName=userNameEditText.getText().toString().toLowerCase().trim();
        String password=passwordEditText.getText().toString();



    }
}
