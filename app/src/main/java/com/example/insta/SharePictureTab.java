package com.example.insta;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;


/**
 * A simple {@link Fragment} subclass.
 */
public class SharePictureTab extends Fragment implements View.OnClickListener{
    private ImageView imgShare;
    private EditText imgDes;
    private Button btnShare;
    Bitmap receivedImageBitmap;


    public SharePictureTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_share_picture_tab, container, false);
        imgShare = view.findViewById(R.id.imgShare);
        imgDes = view.findViewById(R.id.imgDes);
        btnShare = view.findViewById(R.id.btnShare);

        imgShare.setOnClickListener(SharePictureTab.this);
        btnShare.setOnClickListener(SharePictureTab.this);
        return view;
    }

    @Override
    public void onClick(View view) {
       switch (view.getId()){
           case R.id.imgShare:
               // 1st
               if (android.os.Build.VERSION.SDK_INT >= 23 &&
                       ActivityCompat.checkSelfPermission(getContext(),
                       Manifest.permission.READ_EXTERNAL_STORAGE)
                       != PackageManager.PERMISSION_GRANTED) {
                   // Permission is not granted
                   // Asking for permission
                   requestPermissions (new String[]
                          {Manifest.permission.READ_EXTERNAL_STORAGE},
                   1000);
               }else{
                   // 3rd
                   getChosenImage();
               }
               break;
           case R.id.btnShare:
               // 5th
               // Saving an image to the server
               if (receivedImageBitmap != null){

                   if (imgDes.getText().toString().equals("")){
                       FancyToast.makeText(getContext(),"Please specify a description", FancyToast.LENGTH_SHORT,FancyToast.INFO,true).show();
                   }else{
                       ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                       receivedImageBitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                       byte[] bytes = byteArrayOutputStream.toByteArray();
                       ParseFile parseFile = new ParseFile("img.png",bytes);
                       ParseObject parseObject = new ParseObject("Photo");
                       parseObject.put("picture",parseFile);
                       parseObject.put("image_des",imgDes.getText().toString());
                       parseObject.put("username", ParseUser.getCurrentUser().getUsername());
                       final ProgressDialog progressDialog = new ProgressDialog(getContext());
                       progressDialog.setMessage("Uploading Image!");
                       progressDialog.show();
                       parseObject.saveInBackground(new SaveCallback() {
                           @Override
                           public void done(ParseException e) {
                               if (e == null ){
                                   FancyToast.makeText(getContext(),"Image uploaded successfully!", FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
                               }else {
                                   FancyToast.makeText(getContext(),e.getMessage(), FancyToast.LENGTH_SHORT,FancyToast.ERROR,true).show();
                               }
                               progressDialog.dismiss();
                           }
                       });
                   }
               }else{
                   FancyToast.makeText(getContext(),"Please select an image!", FancyToast.LENGTH_SHORT,FancyToast.INFO,true).show();
               }

               break;
       }
    }

    private void getChosenImage() {
        // Path of Image
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,2000);
    }

    // Knowing the result
    // If user granted Permission then this method will run
    // 2nd

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                getChosenImage();
            }
        }
    }

    // 4th
    // Knowing the activity result
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2000){
            if (resultCode == Activity.RESULT_OK)
            try {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(selectedImage,filePathColumn,null,null,null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                receivedImageBitmap = BitmapFactory.decodeFile(picturePath);
                imgShare.setImageBitmap(receivedImageBitmap);
            }catch (Exception e ){
                e.printStackTrace();
            }
        }
    }
}
