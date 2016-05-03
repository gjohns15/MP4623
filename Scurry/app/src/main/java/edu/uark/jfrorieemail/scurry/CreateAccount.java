package edu.uark.jfrorieemail.scurry;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    EditText ET_EMAIL, ET_FIRST, ET_LAST,  ET_PHONE, ET_PASS1, ET_PASS2;
    String first_name, last_name, email, phone_number, password1, password2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);
        ET_EMAIL = (EditText)findViewById(R.id.email);
        ET_FIRST = (EditText)findViewById(R.id.fname);
        ET_LAST = (EditText)findViewById(R.id.lname);
        ET_PHONE = (EditText)findViewById(R.id.phone_number);
        ET_PASS1 = (EditText)findViewById(R.id.password);
        ET_PASS2 = (EditText)findViewById(R.id.password2);

    }

    public  void onCancel(View view){
        finish();

    }
    public void onCreateAccount(View view){
        first_name = ET_FIRST.getText().toString();
        last_name = ET_LAST.getText().toString();
        email = ET_EMAIL.getText().toString();
        phone_number = ET_PHONE.getText().toString();
        password1 = ET_PASS1.getText().toString();
        password2 = ET_PASS2.getText().toString();



        //Test for empty input fields/ password matching/ valid email
            if(first_name.trim().equals("") || first_name == null){
                Toast.makeText(getApplicationContext(), "Please enter a first name.", Toast.LENGTH_SHORT).show();
            }
            else if(last_name.trim().equals("")|| last_name == null){
                Toast.makeText(getApplicationContext(), "Please enter a last name.", Toast.LENGTH_SHORT).show();
            }
            else if(email.trim().equals("") || email == null){
                Toast.makeText(getApplicationContext(), "Please enter an email.", Toast.LENGTH_SHORT).show();
            }
            else if(isEmailValid(email) == false){
                Toast.makeText(getApplicationContext(), "Email not valid", Toast.LENGTH_SHORT).show();
            }
            else if (isPhoneValid(phone_number) == false){
                Toast.makeText(getApplicationContext(), "Phone number is not valid.", Toast.LENGTH_SHORT).show();
            }
            else if(password1.trim().equals("") || password1 == null){
                Toast.makeText(getApplicationContext(), "Please enter a password.", Toast.LENGTH_SHORT).show();
            }
            else if(password2.trim().equals("")|| password2 == null){;
                Toast.makeText(getApplicationContext(), "Please enter a password.", Toast.LENGTH_SHORT).show();
            }
             else if(!(password1.trim().equals(password2))){
                Toast.makeText(getApplicationContext(), "Passwords do not match.", Toast.LENGTH_SHORT).show();
            }
            //Create Account
            else{
                CheckEmail checkEmail = new CheckEmail(this);
                checkEmail.execute(email);

            }

    }
    private boolean isEmailValid(String email){
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private boolean isPhoneValid(String phone_number){
        return PhoneNumberUtils.isGlobalPhoneNumber(phone_number);
    }

    private class CheckEmail extends  AsyncTask<String, String, String>{
        AlertDialog alertDialog;
        Context ctx;

        CheckEmail(Context ctx){
            this.ctx = ctx;
        }

        @Override
        protected void onPreExecute() {
            alertDialog = new AlertDialog.Builder(ctx).create();
            alertDialog.setTitle("Email Information");
        }

        @Override
        protected String doInBackground(String... params) {
            String email_url = "http://ec2-54-200-178-12.us-west-2.compute.amazonaws.com/scurry/check_email.php";
            String email = params[0];

            try {
                URL url = new URL(email_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));

                String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String response = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    response += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return response;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            result= result.trim();
            if(result.equals("This email is already associated with an account. Please enter another email or log in.")){
                alertDialog.setMessage(result);
                alertDialog.show();
            }
            else {
                CreateNewAccount createNewAccount = new CreateNewAccount(CreateAccount.this);
                createNewAccount.execute(first_name, last_name, email, password1);
                finish();
            }
        }
    }


    private class CreateNewAccount extends AsyncTask<String, String, String> {
        Context ctx;
        CreateNewAccount(Context ctx){
            this.ctx = ctx;
        }
        @Override
        protected String doInBackground(String... params) {
            String reg_url = "http://ec2-54-200-178-12.us-west-2.compute.amazonaws.com/scurry/register.php";
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
                httpURLConnection.setDoInput(true);
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
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String response = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    response += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return response;



                /*InputStream IS = httpURLConnection.getInputStream();
                IS.close();
                httpURLConnection.disconnect();
                httpURLConnection.disconnect();
                return "Registration Success";*/


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            result= result.trim();
            if(result.equals("Data insertion Success")){
                result = "Registration Successful";
                Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
            }
            else {
                result = "Error " + result;
                Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
            }

        }

    }
}
