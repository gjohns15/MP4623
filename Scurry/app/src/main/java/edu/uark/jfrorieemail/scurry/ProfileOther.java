package edu.uark.jfrorieemail.scurry;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

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
 * Created by grantjohns on 4/14/16.
 */
public class ProfileOther extends AppCompatActivity
{
    String IDE, nameE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_other);
        Populate populate = new Populate();
        populate.execute();
    }

    //Populate no loged in users profile page with information from the database
    private class Populate extends AsyncTask<String, String, String> {
        String profile_other_url = "http://ec2-54-200-178-12.us-west-2.compute.amazonaws.com/scurry/profile_own.php";

        @Override
        protected String doInBackground(String... params) {
          //  SharedPreferences sharedpreferences = getSharedPreferences(LoginScreen.MyPREFERENCES, Context.MODE_PRIVATE);
          //  String ID = sharedpreferences.getString("idKey", "No ID");
            String ID = getIntent().getStringExtra("ID");
            URL url = null;

            try {
                url = new URL(profile_other_url);
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
            TextView name = (TextView)findViewById(R.id.name);
            if(!(String.valueOf(str[0]) == null)) {
                name.setText(String.valueOf(str[0]));
                nameE = String.valueOf(str[0]);
            }
            TextView phone = (TextView)findViewById(R.id.phone_number);
            if(!(String.valueOf(str[0]) == null)) {
                phone.setText(String.valueOf(str[1]));
            }

        }

    }


    //Not implemented
    public void reviewDriver(View view)
    {
        //Intent intent = new Intent(ProfileOther.this, PostReview.class);
        //send user information to review page
        //intent.putExtra
     //  startActivity(intent);
    }

    //Send message to profile page owner
    public void onSendMessage(View view)
    {
        IDE = getIntent().getStringExtra("ID");
        Intent intent = new Intent(ProfileOther.this, Message.class);
        intent.putExtra("ID", IDE);
        intent.putExtra("name", nameE);
        startActivity(intent);
    }
}
