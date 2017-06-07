package com.example.shagil.ninjalike.Activity;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shagil.ninjalike.Helper.DatabaseHelper;
import com.example.shagil.ninjalike.R;

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
                try {
                    dbHelper.createUser(userName, password);
                    dbHelper.createScoreTable(userName);
                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }catch(SQLiteConstraintException e){
                    //if username already exist
                    Toast.makeText(getApplicationContext(),"Use another username",Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
}
