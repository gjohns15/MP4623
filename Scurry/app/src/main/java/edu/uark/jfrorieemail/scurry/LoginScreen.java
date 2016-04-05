package edu.uark.jfrorieemail.scurry;


import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LoginScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        ActionBar m_myActionBar=getSupportActionBar();
        m_myActionBar.hide();

    }
}
