package edu.uark.jfrorieemail.scurry;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

public class LoginScreen extends AppCompatActivity {
    public static final String MyPREFERENCES = "MyPrefs" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

    }

    public void onLogin(View view){

        EditText email_et = (EditText) findViewById(R.id.email);
        EditText password_et = (EditText) findViewById(R.id.password);

        String email = email_et.getText().toString();
        String password = password_et.getText().toString();


        if(email.trim().equals("") || email == null){
            Toast.makeText(getApplicationContext(), "Please enter an email.", Toast.LENGTH_SHORT).show();
        }
        else if(isEmailValid(email) == false){
            Toast.makeText(getApplicationContext(), "Email not valid.", Toast.LENGTH_SHORT).show();
        }
        else if (password.trim().equals("") || password == null){
            Toast.makeText(getApplicationContext(), "Please enter a password.", Toast.LENGTH_SHORT).show();
        }
        else{
            //LOGIN
            LoginAccount loginAccount = new LoginAccount(this);
          loginAccount.execute(email, password);




         /*   if(true){
                setContentView(R.layout.main_page);

                //Starting the intent for the next class
                Intent intent = new Intent(LoginScreen.this, MainScreen.class);
                startActivity(intent);
            }*/
        }
    }
    private class LoginAccount extends AsyncTask<String, String, String> {
        public static final String Name = "nameKey";
        public static final String ID = "idKey";
        SharedPreferences sharedpreferences;

        AlertDialog alertDialog;
        Context ctx;

        LoginAccount(Context ctx) {
            this.ctx = ctx;
        }

        String login_url = "http://ec2-54-200-178-12.us-west-2.compute.amazonaws.com/scurry/login.php";

        @Override
        protected void onPreExecute() {
            alertDialog = new AlertDialog.Builder(ctx).create();
            alertDialog.setTitle("Login Information");
            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        }

        @Override
        protected String doInBackground(String... params) {
            String email = params[0];
            String password = params[1];

            URL url = null;
            try {
                url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                        URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

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

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return  null;
        }
        @Override
        protected void onPostExecute(String result) {
            result= result.trim();
            String[] str = result.split("-");
            String message = str[0];

            if(message.equals("Login Success")){
                String name = str[1];
                String id = str[2];
                message = message+" "+name+" "+id;
               SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(Name, name);
                editor.putString(ID, id);
                editor.commit();
              //  alertDialog.setMessage(message);
              //  alertDialog.show();

                Intent intent = new Intent(LoginScreen.this, MainScreen.class);
                startActivity(intent);
            }
            else {
                alertDialog.setMessage(message);
                alertDialog.show();
            }
        }
    }

    private boolean isEmailValid(String email){
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void onSignUp(View view){
        startActivity(new Intent(this, CreateAccount.class));

    }
}
