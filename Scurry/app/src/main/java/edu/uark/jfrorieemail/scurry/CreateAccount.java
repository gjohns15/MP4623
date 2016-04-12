package edu.uark.jfrorieemail.scurry;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by grantjohns on 3/13/16.
 */
public class CreateAccount extends AppCompatActivity {
    EditText ET_EMAIL, ET_FIRST, ET_LAST,  ET_PASS1, ET_PASS2;
    String first_name, last_name, email, password1, password2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);
        ET_EMAIL = (EditText)findViewById(R.id.email);
        ET_FIRST = (EditText)findViewById(R.id.fname);
        ET_LAST = (EditText)findViewById(R.id.lname);
        ET_PASS1 = (EditText)findViewById(R.id.password);
        ET_PASS2 = (EditText)findViewById(R.id.password2);

    }

    public void onCreateAccount(View view){
        first_name = ET_FIRST.getText().toString();
        last_name = ET_LAST.getText().toString();
        email = ET_EMAIL.getText().toString();
        password1 = ET_PASS1.getText().toString();
        password2 = ET_PASS2.getText().toString();
        Boolean empty = true;

        //Test for empty input fields/ password matching/ valid email
            if(first_name.trim().equals("") || first_name == null){
                empty = true;
                Toast.makeText(getApplicationContext(), "Please enter a first name.", Toast.LENGTH_SHORT).show();
            }
            else if(last_name.trim().equals("")|| last_name == null){
                empty = true;
                Toast.makeText(getApplicationContext(), "Please enter a last name.", Toast.LENGTH_SHORT).show();
            }
            else if(email.trim().equals("") || email == null){
                empty = true;
                Toast.makeText(getApplicationContext(), "Please enter an email.", Toast.LENGTH_SHORT).show();
            }
            else if(isEmailValid(email) == false){
                empty = true;
                Toast.makeText(getApplicationContext(), "Email not valid", Toast.LENGTH_SHORT).show();
            }
            else if(password1.trim().equals("") || password1 == null){
                empty = true;
                Toast.makeText(getApplicationContext(), "Please enter a password.", Toast.LENGTH_SHORT).show();
            }
            else if(password2.trim().equals("")|| password2 == null){
                empty = true;
                Toast.makeText(getApplicationContext(), "Please enter a password.", Toast.LENGTH_SHORT).show();
            }
             else if(!(password1.trim().equals(password2))){
                empty = true;
                Toast.makeText(getApplicationContext(), "Passwords do not match.", Toast.LENGTH_SHORT).show();
            }
            //Create Account
            else{
                CreateNewAccount createNewAccount = new CreateNewAccount(this);
                createNewAccount.execute(first_name, last_name, email, password1);
                finish();

            }

    }
    private boolean isEmailValid(String email){
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private class CreateNewAccount extends AsyncTask<String, String, String> {
        Context ctx;
        CreateNewAccount(Context ctx){
            this.ctx = ctx;
        }
        @Override
        protected String doInBackground(String... params) {
            String reg_url = "http://10.0.2.2/scurry/register.php";
            String first_name = params[0];
            String last_name = params[1];
            String email = params[2];
            String password = params[3];

            URL url = null;
            try {
                url = new URL(reg_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("first_name", "UTF-8") + "=" + URLEncoder.encode(first_name, "UTF-8") + "&" +
                        URLEncoder.encode("last_name", "UTF-8") + "=" + URLEncoder.encode(last_name, "UTF-8") + "&" +
                        URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                        URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream();
                IS.close();
                httpURLConnection.disconnect();
                httpURLConnection.disconnect();
                return "Registration Success...";


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String result) {

                Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
        }

    }
}
