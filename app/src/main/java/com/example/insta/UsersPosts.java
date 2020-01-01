package com.example.insta;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

public class UsersPosts extends AppCompatActivity {
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_posts);
        linearLayout = findViewById(R.id.linearLayout);
        // Getting value of Intent from UserTab
        Intent receivedIntentObject = getIntent();
        final String receivedUserName = receivedIntentObject.getStringExtra("username");

        setTitle(receivedUserName + "'s Posts");

        ParseQuery<ParseObject> parseQuery = new ParseQuery("Photo");
        parseQuery.whereEqualTo("username",receivedUserName);
        parseQuery.orderByDescending("picture");

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(objects.size() > 0 && e == null){
                    for (final ParseObject post : objects){
                        final TextView postDes = new TextView(UsersPosts.this);
                        postDes.setText(post.get("image_des") +"");
                        // Getting parseFile or data (Image) in backGround
                        final ParseFile postPicture = (ParseFile) post.get("picture");
                        postPicture.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if (data != null && e == null){
                                    // Decoding byteArray into bitmap object
                                    // Setting image into linear layout
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                                    ImageView postImageView = new ImageView(UsersPosts.this);
                                    LinearLayout.LayoutParams img_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                                    img_params.setMargins(5,5,5,5);
                                    postImageView.setLayoutParams(img_params);
                                    postImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                    postImageView.setImageBitmap(bitmap);

                                    // Setting description into linear layout
                                    LinearLayout.LayoutParams img_des = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                                    img_des.setMargins(5,5,5,5);
                                    postDes.setLayoutParams(img_des);
                                    postDes.setGravity(Gravity.CENTER);
                                    postDes.setBackgroundColor(Color.BLUE);
                                    postDes.setTextColor(Color.BLACK);
                                    postDes.setTextSize(30f);

                                    linearLayout.addView(postImageView);
                                    linearLayout.addView(postDes);
                                }
                            }
                        });
                    }
                }else {
                    FancyToast.makeText(UsersPosts.this,receivedUserName+ " doesn't have any post!", Toast.LENGTH_SHORT,FancyToast.INFO,true).show();
                    finish();
                }
                progressDialog.dismiss();
            }

        });

    }
}
