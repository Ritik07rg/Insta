package com.example.insta;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText txtUserLog, txtPsdLog;
    private Button btnLogIn, btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        setTitle("Log In");
        txtUserLog = findViewById(R.id.txtUserLog);
        txtPsdLog = findViewById(R.id.txtPsdLog);
        btnLogIn = findViewById(R.id.btnLogIn);
        btnSignUp = findViewById(R.id.btnSignUp);

        btnLogIn.setOnClickListener(LogInActivity.this);
        btnSignUp.setOnClickListener(LogInActivity.this);

        if (ParseUser.getCurrentUser() != null){
             //ParseUser.getCurrentUser().logOut();
            transitionToSocialMediaActivity();
        }
    }
    @Override
    public void onClick(View view) {
     switch (view.getId()){
         case R.id.btnLogIn:
             ParseUser.logInInBackground(txtUserLog.getText().toString(), txtPsdLog.getText().toString(),new LogInCallback() {
                 @Override
                 public void done(ParseUser user, ParseException e) {
                     if (user != null && e == null){
                         FancyToast.makeText(LogInActivity.this  , "You logged in successfully",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show();
                         transitionToSocialMediaActivity();
                     }else {
                         FancyToast.makeText(LogInActivity.this  , e.getMessage(),FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();
                     }
                     txtUserLog.getText().clear();
                     txtPsdLog.getText().clear();
                 }
             });
             break;
         case R.id.btnSignUp:
             break;
     }
    }
    private void  transitionToSocialMediaActivity(){
        Intent intent = new Intent(LogInActivity.this, SocialMediaActivity.class);
        startActivity(intent);
    }
}
