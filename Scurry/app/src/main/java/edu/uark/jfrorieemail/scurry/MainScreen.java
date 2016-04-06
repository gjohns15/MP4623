package edu.uark.jfrorieemail.scurry;

import android.content.Intent;
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

    public void onPost(View view){
        setContentView(R.layout.posting_form);
        Intent intent = new Intent(MainScreen.this, PostingForm.class);
        startActivity(intent);
    }

    public void onLocalSearch(View view){
        setContentView(R.layout.search);
        Intent intent = new Intent(MainScreen.this, SearchResults.class);
        startActivity(intent);
    }

    public void onProfile(View view){
        setContentView(R.layout.profile_page);
        Intent intent = new Intent(MainScreen.this, ProfilePage.class);
        startActivity(intent);
    }

    public void onLogout(View view){
        //Save all the information
        //Go back to the login page
        //or Destroy()
    }
}
