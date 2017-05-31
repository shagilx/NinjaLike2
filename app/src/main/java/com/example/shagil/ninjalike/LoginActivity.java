package com.example.shagil.ninjalike;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shagil.ninjalike.Helper.DatabaseHelper;

import java.io.ByteArrayOutputStream;

public class LoginActivity extends AppCompatActivity {
    EditText userNameText,passwordText;
    Button loginButton,signUpButton;
    public static String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userNameText=(EditText)findViewById(R.id.userLoginText);
        passwordText=(EditText)findViewById(R.id.passwordLoginText);
        loginButton=(Button)findViewById(R.id.Loginbutton);
        signUpButton=(Button)findViewById(R.id.gotosignupbutton);



        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,SignUpActivity.class);
                    startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=userNameText.getText().toString().toLowerCase().trim();
                String password=passwordText.getText().toString();
                DatabaseHelper dbHelper=new DatabaseHelper(getApplicationContext());
                boolean valid=dbHelper.checkCredentials(username,password);
                if (valid){
                    Intent intent=new Intent(LoginActivity.this,ChooseSkillsActivity.class);
                    userName=username;
                    //intent.putExtra("username",username);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"Invalid Credentials",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
