package com.example.insta;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileTab extends Fragment {

    private EditText edtProfile, edtBio, edtProfession, edtHobby, edtSport;
    private Button btnUpdateInfo;

    public ProfileTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_tab, container, false);

        edtProfile = view.findViewById(R.id.edtProfile);
        edtBio = view.findViewById(R.id.edtBio);
        edtProfession = view.findViewById(R.id.edtProfession);
        edtHobby = view.findViewById(R.id.edtHobby);
        edtSport = view.findViewById(R.id.edtSport);

        btnUpdateInfo = view.findViewById(R.id.btnUpdateInfo);
        // getting reference of current parseUser
        final ParseUser parseUser = ParseUser.getCurrentUser();
        // We are checking whether the info is null or not
        if (parseUser.get("profileName")== null){
            edtProfile.setText("");
        }else{
           edtProfile.setText(parseUser.get("profileName").toString());
        }
        if (parseUser.get("Bio")== null){
            edtBio.setText("");
        }else{
            edtBio.setText(parseUser.get("Bio").toString());
        }
        if (parseUser.get("Profession")== null){
            edtProfession.setText("");
        }else{
            edtProfession.setText(parseUser.get("Profession").toString());
        }
        if (parseUser.get("Hobby")== null){
            edtHobby.setText("");
        }else{
            edtHobby.setText(parseUser.get("Hobby").toString());
        }
        if (parseUser.get("Sport") == null){
            edtSport.setText("");
        }else {
            edtSport.setText(parseUser.get("Sport").toString());
        }


        btnUpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Getting the value from the users
                parseUser.put("profileName", edtProfile.getText().toString());
                parseUser.put("Bio", edtBio.getText().toString());
                parseUser.put("Profession", edtProfession.getText().toString());
                parseUser.put("Hobby", edtHobby.getText().toString());
                parseUser.put("Sport", edtSport.getText().toString());
                // Saving data in background
                parseUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null){
                            FancyToast.makeText(getContext(),"Info Updated", Toast.LENGTH_SHORT, FancyToast.INFO,true).show();
                        }else {
                            FancyToast.makeText(getContext(),e.getMessage(), Toast.LENGTH_SHORT, FancyToast.ERROR,true).show();
                        }
                    }
                });
            }
        });
        return view;
    }

}
