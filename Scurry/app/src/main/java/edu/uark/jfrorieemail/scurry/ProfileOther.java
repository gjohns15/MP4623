package edu.uark.jfrorieemail.scurry;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by grantjohns on 4/14/16.
 */
public class ProfileOther extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_other);
    }

    public void reviewDriver()
    {
        Intent intent = new Intent(this, PostReview.class);
        //send user information to review page
        //intent.putExtra
        startActivity(intent);
    }

    public void sendMessage()
    {
        Intent intent = new Intent(this, Message.class);
        //send user information to message page
        //intent.putExtra
        //startActivity(intent);
    }
}
