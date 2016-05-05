package edu.uark.jfrorieemail.scurry;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by grantjohns
 */
public class Posting  extends AppCompatActivity
{
    String title, descr, addr;
    TextView Title, Descr, Addr;

    String ID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.posting);
        Title = (TextView) findViewById(R.id.titleText);
        Descr = (TextView) findViewById(R.id.descrText);
        Addr = (TextView) findViewById(R.id.startText);
        ID = getIntent().getStringExtra("poster");

        //query database to assign values to String fields

        //assign TextViews to have value of desired Strings
        Title.setText(title);
        Addr.setText(addr);
        Descr.setText(descr);
    }

    //view the profile of the user who posted this job
    public void onView()
    {
        Intent intent = new Intent(Posting.this, ProfileOther.class);
        intent.putExtra("ID", ID);
        startActivity(intent);
    }

    //accept this job, go back to main screen
    public void onAccept()
    {}

}
