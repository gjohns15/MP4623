package edu.uark.jfrorieemail.scurry;

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
 * Created by james on 3/13/2016.
 */
public class PostingForm extends AppCompatActivity {
    EditText ET_TITLE, ET_DESCRIPTION, ET_ADDRESS,ET_STREET, ET_CITY, ET_STATE, ET_ZIP;
    String title, description, address, street, city, state, zip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_posting);
        ET_TITLE = (EditText)findViewById(R.id.title);
        ET_DESCRIPTION = (EditText)findViewById(R.id.description);
        ET_STREET = (EditText)findViewById(R.id.street);
        ET_CITY = (EditText)findViewById(R.id.city);
        ET_STATE = (EditText)findViewById(R.id.state);
        ET_ZIP = (EditText)findViewById(R.id.zip );
    }

    public  void onCancel(View view){
       finish();
    }

    public void onPostJob(View view){
        title = ET_TITLE.getText().toString();
        description = ET_DESCRIPTION.getText().toString();
        street = ET_STREET.getText().toString();
        city = ET_CITY.getText().toString();
        state = ET_STATE.getText().toString();
        zip = ET_ZIP.getText().toString();

        if(title.trim().equals("") || title == null){
            Toast.makeText(getApplicationContext(), "Please enter a title.", Toast.LENGTH_SHORT).show();
        }
        else if(description.trim().equals("")|| description == null){
            Toast.makeText(getApplicationContext(), "Please enter a description.", Toast.LENGTH_SHORT).show();
        }
        else if(street.trim().equals("") || street == null){
            Toast.makeText(getApplicationContext(), "Please enter an street.", Toast.LENGTH_SHORT).show();
        }
        else if(city.trim().equals("") || city == null){
            Toast.makeText(getApplicationContext(), "Please enter an city.", Toast.LENGTH_SHORT).show();
        }
        else if(state.trim().equals("") || state == null){
            Toast.makeText(getApplicationContext(), "Please enter an state.", Toast.LENGTH_SHORT).show();
        }
        else if(zip.trim().equals("") || zip == null){
            Toast.makeText(getApplicationContext(), "Please enter an zip.", Toast.LENGTH_SHORT).show();
        }
        else {
            address = street+ "," + city + "," + state + "," + zip;
            PostJob postJob = new PostJob();
            postJob.execute();
        }

    }

    private class PostJob extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {
            String  post_url = "http://ec2-54-200-178-12.us-west-2.compute.amazonaws.com/scurry/post.php";
            SharedPreferences sharedpreferences = getSharedPreferences(LoginScreen.MyPREFERENCES, Context.MODE_PRIVATE);
            String ID = sharedpreferences.getString("idKey", "No ID");

            try {
                URL url = new URL(post_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream OS = httpURLConnection.getOutputStream();

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("ID", "UTF-8") + "=" + URLEncoder.encode(ID, "UTF-8") + "&" +
                        URLEncoder.encode("title", "UTF-8") + "=" + URLEncoder.encode(title, "UTF-8") + "&" +
                        URLEncoder.encode("description", "UTF-8") + "=" + URLEncoder.encode(description, "UTF-8") + "&" +
                        URLEncoder.encode("address", "UTF-8") + "=" + URLEncoder.encode(address, "UTF-8");

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
            if(result == null) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
            else{
                result = result.trim();
                if (result.equals("Data insertion Success")) {
                    result = "Job Posted Successfully";
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    result = "Error " + result;
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                }

            }

        }
    }
}
