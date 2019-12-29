package com.example.insta;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText txtEmail, txtUser, txtPsd;
    private Button btnSign, btnLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtEmail = findViewById(R.id.txtEmail);
        txtUser = findViewById(R.id.txtUser);
        txtPsd = findViewById(R.id.txtPsd);
        // Setting Key Event for signup
        txtPsd.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
                    onClick(btnSign);
                }
                return false;
            }
        });
        btnSign = findViewById(R.id.btnSign);
        btnLog = findViewById(R.id.btnLog);

        btnSign.setOnClickListener(MainActivity.this);
        btnLog.setOnClickListener(MainActivity.this);
        // token session logout (Logout the current user for next user to Sign up)
        if (ParseUser.getCurrentUser() != null){
           //ParseUser.getCurrentUser().logOut();
             transitionToSocialMediaActivity();
        }
    }
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnSign:
                if (txtEmail.getText().toString().equals("") || txtUser.getText().toString().equals("") || txtPsd.getText().toString().equals("")){
                    FancyToast.makeText(MainActivity.this,"All three field is required!",FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();
                }else{
                ParseUser parseUser = new ParseUser();
                parseUser.setEmail(txtEmail.getText().toString());
                parseUser.setUsername(txtUser.getText().toString());
                parseUser.setPassword(txtPsd.getText().toString());
                // Setting Progress Dialog
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Please wait for a moment");
                progressDialog.show();
                parseUser.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null){
                            FancyToast.makeText(MainActivity.this,txtUser.getText().toString() + " you signed up successfully",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show();
                            // Calling method so that user can switch to social media activity
                            transitionToSocialMediaActivity();
                        }else {
                            FancyToast.makeText(MainActivity.this,e.getMessage(),FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();
                        }
                        txtEmail.getText().clear();
                        txtUser.getText().clear();
                        txtPsd.getText().clear();
                        progressDialog.dismiss();
                    }
                });
                }

                break;
            case R.id.btnLog:
                // Switch to the Login Activity
                Intent intent = new Intent(MainActivity.this, LogInActivity.class);
                startActivity(intent);
            break;
        }
    }
    private void transitionToSocialMediaActivity(){
        Intent intent = new Intent(MainActivity.this, SocialMediaActivity.class);
        startActivity(intent);
    }
}

