package edu.uark.jfrorieemail.scurry;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by james on 3/13/2016.
 */
public class ProfileSettings extends AppCompatActivity {

    TextView fname, lname, pnum;
    EditText FN, LN, PN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_settings);
        fname = (TextView) findViewById(R.id.fnameView);
        lname = (TextView) findViewById(R.id.lnameView);
        pnum = (TextView) findViewById(R.id.numView);

        FN = (EditText) findViewById(R.id.fnameText);
        LN = (EditText) findViewById(R.id.lnameText);
        PN = (EditText) findViewById(R.id.numText);

        //instantiate field values
        FN.setText();
        LN.setText();
        PN.setText();
    }

    public void onSave()
    {
        //push changes to database

        //return to profile page
        Intent intent = new Intent(ProfileSettings.this, ProfilePage.class);
        //shared preferences needed?
        startActivity(intent);
    }
}
