package edu.uark.jfrorieemail.scurry;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by grantjohns on 4/14/16.
 */
public class PostReview extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_post_review);
    }

    public void sendReview()
    {
        //create Review object

        //push review to database

        //return user to main screen
        Intent intent = new Intent(this, MainScreen.class);
        startActivity(intent);
    }
}

