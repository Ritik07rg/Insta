package com.example.insta;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText fieldOne, fieldTwo, fieldThree;
    private Button btnClick, btnAlldata, btnActivity;
    private TextView txtClick;
    private  String getData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnClick = findViewById(R.id.btnClick);
        fieldOne = findViewById(R.id.fieldOne);
        fieldTwo = findViewById(R.id.fieldTwo);
        fieldThree = findViewById(R.id.fieldThree);
        txtClick = findViewById(R.id.txtClick);
        btnAlldata = findViewById(R.id.btnAlldata);
        btnActivity = findViewById(R.id.btnActivity);

        txtClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("KickBoxer");
                parseQuery.getInBackground("lWoZqTLIyR", new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        if (object != null && e == null){
                            txtClick.setText(object.get("name").toString());
                        }
                    }
                });
            }
        });

        // Retrieve all data from the server
        btnAlldata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData = "";
                ParseQuery<ParseObject> queryAll = ParseQuery.getQuery("KickBoxer");
                // findInBackground is used to get all the data
                queryAll.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e == null){
                            if(objects.size() > 0){
                                for (ParseObject Rdata: objects){
                                     getData = getData + Rdata.get("name") +" KickPower:" + Rdata.get("kick_power")+ " KickSpeed"+ Rdata.get("kick_speed") + "\n";
                                }
                                Toast.makeText(MainActivity.this, getData, Toast.LENGTH_LONG).show();

                            }
                        }
                    }
                });
            }
        });

        btnActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignupandLogin.class);
                startActivity(intent);
            }
        });

    }
    // Adding object into the parse server
     public void btnMethod(View view){
        final ParseObject kickBoxer = new ParseObject("KickBoxer");
        kickBoxer.put("name", fieldOne.getText().toString());
        kickBoxer.put("kick_power", Integer.parseInt(fieldTwo.getText().toString()));
        kickBoxer.put("kick_speed", Integer.parseInt(fieldThree.getText().toString()));
        kickBoxer.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null){
                    Toast.makeText(MainActivity.this, kickBoxer.get("name")+ " is added to the server",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(MainActivity.this, e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
     }

}

