package edu.uark.jfrorieemail.scurry;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

/**
 * Created by james on 3/13/2016.
 */
public class ProfilePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_own);
    }


    //not called from XML yet
    //assuming that the class Posting Form is for user to post about the driver
    public void userReview(View view){
        setContentView(R.layout.user_post_review);
        Intent intent = new Intent(ProfilePage.this, PostingForm.class);
        startActivity(intent);
    }

    //Changes screen to Profile settings
    public void profSettings(View view){
        SharedPreferences sharedpreferences = getSharedPreferences(LoginScreen.MyPREFERENCES, Context.MODE_PRIVATE);

        Toast.makeText(this, sharedpreferences.getString("idKey", "No ID"),
                Toast.LENGTH_LONG).show();
    /*    setContentView(R.layout.profile_settings);
        Intent intent = new Intent(ProfilePage.this, ProfileSettings.class);
        startActivity(intent);*/
    }

    //Needs to be implemented in the XML too
    //Changes to DM
    public void DM(View view){
        setContentView(R.layout.mult_messages);
        Intent intent = new Intent(ProfilePage.this, Messages.class);
        startActivity(intent);
    }
}
