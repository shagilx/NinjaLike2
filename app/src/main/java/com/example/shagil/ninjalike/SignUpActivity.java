package com.example.shagil.ninjalike;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.shagil.ninjalike.Helper.DatabaseHelper;

public class SignUpActivity extends AppCompatActivity {
    EditText userNameEditText,passwordEditText;
    Button signUpButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        userNameEditText=(EditText)findViewById(R.id.userText);
        passwordEditText=(EditText)findViewById(R.id.passwordText);
        signUpButton=(Button)findViewById(R.id.SignUpbutton);


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName=userNameEditText.getText().toString().toLowerCase().trim();
                String password=passwordEditText.getText().toString();
                DatabaseHelper dbHelper=new DatabaseHelper(getApplicationContext());
                dbHelper.createUser(userName,password);
                Intent intent=new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });



    }
}
