package edu.uark.jfrorieemail.scurry;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by grantjohns on 3/13/16.
 */
public class MainScreen extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
    }

    //changes to post
    public void onPost(View view){
        Intent intent = new Intent(MainScreen.this, PostingForm.class);
        startActivity(intent);
    }

    //changes to search
    public void onLocalSearch(View view){
        Intent intent = new Intent(MainScreen.this, MapsActivity.class);
        startActivity(intent);

    }

    //changes to Profile page
    public void onProfile(View view){
        Intent intent = new Intent(MainScreen.this, ProfilePage.class);
        startActivity(intent);
    }

    public void onAllJobs(View view){
        Intent intent = new Intent(MainScreen.this, AllJobs.class);
        startActivity(intent);
    }

    public void onLogout(View view){
       SharedPreferences sharedpreferences = getSharedPreferences(LoginScreen.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();
        Intent intent = new Intent(MainScreen.this, LoginScreen.class);
        startActivity(intent);
    }
}
