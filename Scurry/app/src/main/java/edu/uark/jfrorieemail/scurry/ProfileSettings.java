package edu.uark.jfrorieemail.scurry;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by james on 3/13/2016.
 */
public class ProfileSettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_settings);
    }

    public void editProfile(){
        //Will allow the user to edit profile settings
        //i.e. Name, Phone number, etc
        //Called from ImageView in xml page profile_own
    }
}
