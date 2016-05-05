package edu.uark.jfrorieemail.scurry;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
 * Created by grantjohns
 */
public class Posting  extends AppCompatActivity
{
    TextView Title, Descr, Addr;

    String ID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.posting);
        ID = getIntent().getStringExtra("poster");
        Populate populate = new Populate();
        populate.execute();
        }

    private class Populate extends AsyncTask<String, String, String> {
        String profile_url = "http://ec2-54-200-178-12.us-west-2.compute.amazonaws.com/scurry/post.php";

        @Override
        protected String doInBackground(String... params) {
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


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            result = result.trim();
            String[] str = result.split(",");
            Title = (TextView) findViewById(R.id.titleText);
            ;
            if (!(String.valueOf(str[0]) == null)) {
                Title.setText(String.valueOf(str[1]));
            }
            Descr = (TextView) findViewById(R.id.descrText);
            if (!(String.valueOf(str[0]) == null)) {
                Descr.setText(String.valueOf(str[2]));
            }
            Addr = (TextView) findViewById(R.id.startText);
            if (!(String.valueOf(str[0]) == null)) {
                Descr.setText(String.valueOf(str[3]));
            }

        }
    }

        //view the profile of the user who posted this job
        public void onView() {
            Intent intent = new Intent(Posting.this, ProfileOther.class);
            intent.putExtra("ID", ID);
            startActivity(intent);
        }

        //accept this job, go back to main screen
        public void onAccept() {
        }
}
