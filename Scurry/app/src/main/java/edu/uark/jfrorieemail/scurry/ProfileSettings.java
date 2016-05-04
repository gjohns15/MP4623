package edu.uark.jfrorieemail.scurry;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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
 * Created by james on 3/13/2016.
 */
public class ProfileSettings extends AppCompatActivity {

    TextView fname, lname, pnum, email;
    EditText FN, LN, PN, EM, PS;
    String first_name, last_name, Nemail, phone_number, password, cPass;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_settings);
        email = (TextView) findViewById(R.id.emailCurrent);
        fname = (TextView) findViewById(R.id.fnameCurrent);
        lname = (TextView) findViewById(R.id.lnameCurrent);
        pnum = (TextView) findViewById(R.id.numCurrent);

        EM = (EditText) findViewById(R.id.emailText);
        FN = (EditText) findViewById(R.id.fnameText);
        LN = (EditText) findViewById(R.id.lnameText);
        PN = (EditText) findViewById(R.id.numText);
        PS = (EditText) findViewById(R.id.passText);

        Populate populate = new Populate();
        populate.execute();
    }
    public void onSave(View view){
        first_name = FN.getText().toString();
        last_name = LN.getText().toString();
        Nemail = EM.getText().toString();
        phone_number = PN.getText().toString();
        password = PS.getText().toString();



        //Check if there are any blank or null values then check if email already exists then save changes
            if(!(Nemail.trim().equals("") || Nemail == null)){
                if(isEmailValid(Nemail) == false){
                    Toast.makeText(getApplicationContext(), "Email not valid", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Nemail = "no email";
            }

            if (!(phone_number.trim().equals("") || phone_number == null)){

                if (isPhoneValid(phone_number) == false){
                    Toast.makeText(getApplicationContext(), "Phone number is not valid.", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                phone_number = pnum.getText().toString();
            }

            if(first_name.trim().equals("") || first_name == null){
                first_name = fname.getText().toString();
            }

            if(last_name.trim().equals("")|| last_name == null){
                last_name = lname.getText().toString();
            }

            if(password.trim().equals("") || password == null){
                password = cPass;
            }

            CheckEmail checkEmail = new CheckEmail();
            checkEmail.execute(Nemail);

    }
    private boolean isEmailValid(String email){
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private boolean isPhoneValid(String phone_number){
        if(phone_number != null) {
            return PhoneNumberUtils.isGlobalPhoneNumber(phone_number);
        }
        else {
            return false;
        }
    }

    private class CheckEmail extends  AsyncTask<String, String, String>{

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
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();

            }
            else {
                SaveChanges saveChanges = new SaveChanges();
                saveChanges.execute(first_name, last_name, Nemail, phone_number, password);

            }
        }
    }


    private class SaveChanges extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String reg_url = "http://ec2-54-200-178-12.us-west-2.compute.amazonaws.com/scurry/change_settings.php";
            String first_name = params[0];
            String last_name = params[1];
            String email = params[2];
            String phone = params[3];
            String password = params[4];
            SharedPreferences sharedpreferences = getSharedPreferences(LoginScreen.MyPREFERENCES, Context.MODE_PRIVATE);
            String ID = sharedpreferences.getString("idKey", "No ID");
            if(ID == "No ID"){
                return null;
            }

            URL url = null;
            try {
                url = new URL(reg_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream OS = httpURLConnection.getOutputStream();

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("ID", "UTF-8") + "=" + URLEncoder.encode(ID, "UTF-8") + "&" +
                        URLEncoder.encode("first_name", "UTF-8") + "=" + URLEncoder.encode(first_name, "UTF-8") + "&" +
                        URLEncoder.encode("last_name", "UTF-8") + "=" + URLEncoder.encode(last_name, "UTF-8") + "&" +
                        URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                        URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8") + "&" +
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
                result = "Changes Saved";
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            }
            else {
                result = "Error " + result;
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            }

        }

    }
    //Populate page with current information
    private class Populate extends AsyncTask<String, String, String> {
        String profile_url = "http://ec2-54-200-178-12.us-west-2.compute.amazonaws.com/scurry/profile_settings.php";

        @Override
        protected String doInBackground(String... params) {
            SharedPreferences sharedpreferences = getSharedPreferences(LoginScreen.MyPREFERENCES, Context.MODE_PRIVATE);
            String ID = sharedpreferences.getString("idKey", "No ID");
            URL url = null;

            try {
                url = new URL(profile_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String data = URLEncoder.encode("ID", "UTF-8") + "=" + URLEncoder.encode(ID, "UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
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


            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            result= result.trim();
            String[] str = result.split(",");
            if(!(String.valueOf(str[0]) == null)) {
                fname.setText(String.valueOf(str[0]));
            }
            if(!(String.valueOf(str[1]) == null)) {
                lname.setText(String.valueOf(str[1]));
            }
            if(!(String.valueOf(str[2]) == null)) {
                pnum.setText(String.valueOf(str[2]));
            }
            if(!(String.valueOf(str[3]) == null)) {
                email.setText(String.valueOf(str[3]));
            }
            if(!(String.valueOf(str[4]) == null)){
                cPass = str[4];
            }

        }

    }
}
