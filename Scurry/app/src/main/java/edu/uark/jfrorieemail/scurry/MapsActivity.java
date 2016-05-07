package edu.uark.jfrorieemail.scurry;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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
import java.util.List;
import java.util.Map;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnInfoWindowClickListener{
    private GoogleMap mMap;
    private GoogleApiClient mClient;
    protected Location currentLocation;
    String poster = "name";
    private static final String TAG = MapsActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (mClient == null) {
            mClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        Populate populate = new Populate();
        populate.execute();
    }

    private class Populate extends AsyncTask<String, String, String> {
        String maps_url = "http://ec2-54-200-178-12.us-west-2.compute.amazonaws.com/scurry/maps.php";

        @Override
        protected String doInBackground(String... params) {
            URL url = null;

            try {
               // SharedPreferences sharedpreferences = getSharedPreferences(LoginScreen.MyPREFERENCES, Context.MODE_PRIVATE);
               // String ID = sharedpreferences.getString("idKey", "No ID");
                String ID = "1";
                url = new URL(maps_url);
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
            Geocoder coder = new Geocoder(getApplicationContext());
            result = result.trim();
            String[] str = result.split("!");

            try
            {
                List<Address> posting = null;
                for(int i =0; i< 5; i++) {
                    posting = coder.getFromLocationName(str[2], 1);
                }

                if(!posting.isEmpty()) {
                    Address post = posting.get(0);
                    LatLng jobMarker = new LatLng(post.getLatitude(), post.getLongitude());

                    mMap.addMarker(new MarkerOptions().position(jobMarker).title(str[0]));
                }
            }

            catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMyLocationButtonClickListener(this);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.setOnInfoWindowClickListener(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
        }

        currentLocation = LocationServices.FusedLocationApi.getLastLocation(mClient);
        if (currentLocation != null) {

            Double lat = currentLocation.getLatitude();
            Double lng = currentLocation.getLongitude();

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 16));
            if (!Geocoder.isPresent()) {
                return;
            }


        }
        else {

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(36.0626, -94.1574), 14));
            Log.d(TAG, "current location null");
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        mClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mClient.isConnected()) {
            mClient.disconnect();}
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }


    @Override
    public void onConnected(Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        currentLocation = LocationServices.FusedLocationApi.getLastLocation(mClient);
        if (currentLocation != null) {
            Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_LONG).show();
            Double lat = currentLocation.getLatitude();
            Double lng = currentLocation.getLongitude();
            Toast.makeText(getApplicationContext(), lat.toString(), Toast.LENGTH_LONG).show();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 16));
            if (!Geocoder.isPresent()) {
                return;
            }


        }
        else {
            Toast.makeText(getApplicationContext(), "current location null", Toast.LENGTH_LONG).show();
            Log.d(TAG, "current location null");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onInfoWindowClick(Marker marker)
    {
        Intent intent = new Intent(MapsActivity.this, Posting.class);
        intent.putExtra("title", marker.getTitle());
        intent.putExtra("poster", poster);
        startActivity(intent);

    }
}
