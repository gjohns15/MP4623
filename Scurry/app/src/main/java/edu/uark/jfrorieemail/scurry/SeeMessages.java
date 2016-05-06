package edu.uark.jfrorieemail.scurry;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
import java.util.ArrayList;

/**
 * Created by james on 5/6/2016.
 */
public class SeeMessages extends AppCompatActivity {
    ListView listView;
    String[] senderIDs;
    ArrayList<String> userIDs = new ArrayList<String>();
    ArrayList<String> listItems=new ArrayList<String>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.see_messages);

        listView = (ListView) findViewById(R.id.list);

      adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, listItems);
        Populate populate = new Populate();
        populate.execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // Toast.makeText(getApplicationContext(), parent.getPositionForView(view), Toast.LENGTH_SHORT).show();
                int pos = parent.getPositionForView(view);
                Intent intent = new Intent(SeeMessages.this, ProfileOther.class);
                intent.putExtra("ID", senderIDs[pos]);
                startActivity(intent);
               // Toast.makeText(getApplicationContext(),pos + "",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private class Populate extends AsyncTask<String, String, String> {
        String messages_url = "http://ec2-54-200-178-12.us-west-2.compute.amazonaws.com/scurry/see_messages.php";

        @Override
        protected String doInBackground(String... params) {
            SharedPreferences sharedpreferences = getSharedPreferences(LoginScreen.MyPREFERENCES, Context.MODE_PRIVATE);
            String ID = sharedpreferences.getString("idKey", "No ID");
            URL url = null;

            try {
                url = new URL(messages_url);
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
            listView.setAdapter(adapter);
            int length = str.length;
            senderIDs = new String[(length-1)/2];
            for(int i = 1, j =0 ; i<length; i++){
                if(i%2 == 0){
                    senderIDs[j] = str[i];
                    j++;
                }
                else {
                    listItems.add(str[i]);
                }
            }

            adapter.notifyDataSetChanged();


        }
    }

}
