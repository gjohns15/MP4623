package edu.uark.jfrorieemail.scurry;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
 * Created by grantjohns on 4/20/16.
 */
public class Message extends AppCompatActivity
{
    EditText MESSAGE_ET;
    String message;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_message);
        MESSAGE_ET = (EditText)findViewById(R.id.message);


    }

    public void onMessageClick(View view){
        message = MESSAGE_ET.getText().toString();
        sendMessage sendMessage = new sendMessage();
        sendMessage.execute();
    }


    private class sendMessage extends AsyncTask<String, String, String> {
        String profile_url = "http://ec2-54-200-178-12.us-west-2.compute.amazonaws.com/scurry/messages.php";

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

                String data = URLEncoder.encode("ID", "UTF-8") + "=" + URLEncoder.encode(ID, "UTF-8")  + "&" +
                        URLEncoder.encode("message", "UTF-8") + "=" + URLEncoder.encode(message, "UTF-8");

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
            if(result.length() > 0){
            result = result.trim();
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();}

        }
    }


}
