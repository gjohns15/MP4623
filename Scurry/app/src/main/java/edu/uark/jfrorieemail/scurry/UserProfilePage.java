package edu.uark.jfrorieemail.scurry;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by grantjohns on 4/14/16.
 */
public class UserProfilePage extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_page);
    }

    public void onHome()
    {
        Intent intent = new Intent(this, MainScreen.class);
        startActivity(intent);
    }

    public void onReview()
    {
        Intent intent = new Intent(this, PostReview.class);
        //send user information to review page
        //intent.putExtra
        startActivity(intent);
    }

    public void onContact()
    {
        Intent intent = new Intent(this, FirstDirectMessage.class);
        //send user information to message page
        //intent.putExtra
        startActivity(intent);
    }
}
