package com.example.insta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupandLogin extends AppCompatActivity {
    private EditText userSignUp, userSignPsd, userLogIn, userLogPsd;
    private Button btnSignUp, btnLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signupand_login);
        userSignUp = findViewById(R.id.userSignUp);
        userSignPsd = findViewById(R.id.userSignPsd);
        userLogIn  = findViewById(R.id.userLogIn);
        userLogPsd  = findViewById(R.id.userLogPsd);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnLogIn = findViewById(R.id.btnLogIn);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ParseUser newUser = new ParseUser();
                newUser.setUsername(userSignUp.getText().toString());
                newUser.setPassword(userSignPsd.getText().toString());

                newUser.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null){
                            Toast.makeText(SignupandLogin.this, newUser.get("username")+"is signed up successfully!", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(SignupandLogin.this, e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logInInBackground(userLogIn.getText().toString(), userLogPsd.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if(user != null && e == null){
                            Toast.makeText(SignupandLogin.this,user.get("username") + "is Logged in successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignupandLogin.this, Welcome.class);
                            startActivity(intent);
                        }else {
                            Toast.makeText(SignupandLogin.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}
