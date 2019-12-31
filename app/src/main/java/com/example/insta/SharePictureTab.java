package com.example.insta;


import android.Manifest;
import android.app.Activity;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class SharePictureTab extends Fragment implements View.OnClickListener{
    private ImageView imgShare;
    private EditText imgDes;
    private Button btnShare;


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
                String[] filePathColoum = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(selectedImage,filePathColoum,null,null,null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColoum[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                Bitmap receivedImageBitmap = BitmapFactory.decodeFile(picturePath);
                imgShare.setImageBitmap(receivedImageBitmap);
            }catch (Exception e ){
                e.getStackTrace();
            }
        }
    }
}
