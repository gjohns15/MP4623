package edu.uark.jfrorieemail.scurry;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
import java.util.ArrayList;

/**
 * Created by james on 5/6/2016.
 */
public class AllJobs extends AppCompatActivity {
    ListView listView;
    String[] posterIDS;
    ArrayList<String> userIDs = new ArrayList<String>();
    ArrayList<String> listItems=new ArrayList<String>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.see_messages);

        listView = (ListView) findViewById(R.id.list);

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, listItems);
        Populate populate = new Populate();
        populate.execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int pos = parent.getPositionForView(view);
                Intent intent = new Intent(AllJobs.this, ProfileOther.class);
                intent.putExtra("ID", posterIDS[pos]);
                startActivity(intent);
            //    Toast.makeText(getApplicationContext(),posterIDS[pos],Toast.LENGTH_SHORT).show();
            }
        });


    }

    private class Populate extends AsyncTask<String, String, String> {
        String jobs_url = "http://ec2-54-200-178-12.us-west-2.compute.amazonaws.com/scurry/all_jobs.php";


        @Override
        protected String doInBackground(String... params) {
            SharedPreferences sharedpreferences = getSharedPreferences(LoginScreen.MyPREFERENCES, Context.MODE_PRIVATE);
            String ID = sharedpreferences.getString("idKey", "No ID");
            URL url = null;

            try {
                url = new URL(jobs_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);

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
            String[] str = result.split("!");
            listView.setAdapter(adapter);
            int length = str.length;
            String temp = "";
            posterIDS = new String[length/5];
            for(int i = 1, j =0 ; i<length;){
               // if(i%2 == 0){
                if(i%5==0){
                    posterIDS[j] = str[i];
                    j++;
                    i++;
                }
                else {
                    temp = "";
                    for (int k = 0; k<4; k++, i++) {
                        temp= temp + str[i]+"\n";
                    }
                    listItems.add(temp);

                }
            }

            adapter.notifyDataSetChanged();

        }
    }

}



