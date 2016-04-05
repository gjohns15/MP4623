package edu.uark.jfrorieemail.scurry;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

    }

    public void onLogin(View view){

        EditText email_et = (EditText) findViewById(R.id.email);
        EditText password_et = (EditText) findViewById(R.id.password);

        String email_val = email_et.getText().toString();
        String password_val = password_et.getText().toString();

        if(isEmailValid(email_val) == false){
            Toast.makeText(getApplicationContext(), "Email not valid", Toast.LENGTH_SHORT).show();
        }
        else{
            //check DB for email
            //pull password for that email
            //compare both
            //if same -> setContentView(R.layout.main_page);
            //else -> Toast "Incorrect email and password combination. Try again"

            //transition purposes
            //if NOT null
            if(password_val.matches("")){
                Toast.makeText(getApplicationContext(), "Please enter a password", Toast.LENGTH_SHORT).show();
            }
            else{
                setContentView(R.layout.main_page);
            }
        }
    }

    private boolean isEmailValid(String email){
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
