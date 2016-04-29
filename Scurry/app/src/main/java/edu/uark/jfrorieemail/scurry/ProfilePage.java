package edu.uark.jfrorieemail.scurry;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
public class ProfilePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.profile_own);
        setContentView(R.layout.profile_own);
        Populate populate = new Populate();
        populate.execute();
    }

    private class Populate extends AsyncTask<String, String, String> {
        String profile_url = "http://[2602:306:cc42:9780:d5b5:b259:ff89:f9f]:8080/scurry/profile_own.php";

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
            TextView name = (TextView)findViewById(R.id.name);
            name.setText(String.valueOf(str[0]));
            TextView phone = (TextView)findViewById(R.id.phone_number);
            phone.setText(String.valueOf(str[1]));

        }

    }

    //not called from XML yet
    //assuming that the class Posting Form is for user to post about the driver
    public void userReview(View view){
        setContentView(R.layout.user_post_review);
        Intent intent = new Intent(ProfilePage.this, PostingForm.class);
    startActivity(intent);
}

    //Changes screen to Profile settings
    public void profSettings(View view){
        SharedPreferences sharedpreferences = getSharedPreferences(LoginScreen.MyPREFERENCES, Context.MODE_PRIVATE);

        Toast.makeText(this, sharedpreferences.getString("idKey", "No ID"),
                Toast.LENGTH_LONG).show();
     //   Intent intent = new Intent(ProfilePage.this, ProfileSettings.class);
     //   startActivity(intent);
    }

    //Needs to be implemented in the XML too
    //Changes to DM
    public void DM(View view){
        setContentView(R.layout.send_message);
        Intent intent = new Intent(ProfilePage.this, Messages.class);
        startActivity(intent);
    }
}
