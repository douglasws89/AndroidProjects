package com.example.cmps121.hw3;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements TextWatcher {


    private LocationData locationData = LocationData.getLocationData();


    EditText nickName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestLocationUpdate();

        Button chatButton = (Button) findViewById(R.id.button);
        chatButton.setEnabled(false);

        nickName = (EditText) this.findViewById(R.id.editText);
        nickName.addTextChangedListener(this);

        EditText nickname_edit = (EditText) findViewById(R.id.editText);
        nickname_edit.setEnabled(false);

        Toast error = Toast.makeText(getApplicationContext(), "Acquiring location...", Toast.LENGTH_SHORT);
        error.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestLocationUpdate();
    }

    @Override
    protected void onPause() {
        super.onPause();
        removeLocationUpdate();
    }

    public void goToMessages(View v){

        // Let's get the new greeting
        EditText et = (EditText) this.findViewById(R.id.editText);
        String s = et.getText().toString();

        String lat = Double.toString(locationData.getLocation().getLatitude());
        String lng = Double.toString(locationData.getLocation().getLongitude());

        // Shared Preferences
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("texto", s);
        editor.putString("lat",lat);
        editor.putString("lng",lng);

        String z = settings.getString("user_id", null);

        if (z == null){
            Random rand = new Random();
            int  n = rand.nextInt(1000);
            editor.putString("user_id",Integer.toString(n));
        }

        editor.commit();

        // Change view
        changeSetting(findViewById(R.id.button));
    }

    // Switches to another activity
    public void changeSetting(View v){
        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);
    }

    // --------------------------- Location Stuff -------------------------------

    private void requestLocationUpdate(){
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED) {

                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                //Log.i(LOG_TAG, "requesting location update");
            }
        }
    }

    /*
	Remove location update. This must be called in onPause if the user has allowed location sharing
	 */
    private void removeLocationUpdate() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED) {

                locationManager.removeUpdates(locationListener);
               // Log.i(LOG_TAG, "removing location update");
            }
        }
    }

    /**
     * Listenes to the location, and gets the most precise recent location.
     */
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            // We display the accuracy.
            displayAccuracy(location);

            Location lastLocation = locationData.getLocation();

            // Do something with the location you receive.
            double newAccuracy = location.getAccuracy();

            long newTime = location.getTime();
            // Is this better than what we had?  We allow a bit of degradation in time.
            boolean isBetter = ((lastLocation == null) ||
                    newAccuracy < lastLocation.getAccuracy() + (newTime - lastLocation.getTime()));
            if (isBetter) {
                // We replace the old estimate by this one.
                locationData.setLocation(location);
            }

            EditText nickname_edit = (EditText) findViewById(R.id.editText);
            nickname_edit.setEnabled(true);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        @Override
        public void onProviderEnabled(String provider) {
            TextView accView = (TextView) findViewById(R.id.textView2);
            String accuracy = "Welcome! Acquiring accuracy...";
            accView.setText(accuracy);
            Toast error = Toast.makeText(getApplicationContext(), "GPS Enabled!", Toast.LENGTH_SHORT);
            error.show();
        }

        @Override
        public void onProviderDisabled(String provider) {
            EditText nickname_edit = (EditText) findViewById(R.id.editText);
            nickname_edit.setEnabled(false);
            Button chatButton = (Button) findViewById(R.id.button);
            chatButton.setEnabled(false);
            Toast error = Toast.makeText(getApplicationContext(), "Please enable your GPS!", Toast.LENGTH_LONG);
            error.show();
        }
    };

    /**
     * Displays the accuracy to the user.
     * @param location
     */
    private void displayAccuracy(Location location) {
        // Displays the accuracy.
        TextView accView = (TextView) findViewById(R.id.textView2);
        EditText nickname_edit = (EditText) findViewById(R.id.editText);
        if (location == null) {
            Toast error = Toast.makeText(getApplicationContext(), "Waiting for GPS accuracy...", Toast.LENGTH_SHORT);
            error.show();
            nickname_edit.setEnabled(false);
        } else {
            String acc = "Weolcome! Your accuracy is: " + String.format("%5.1f m", location.getAccuracy());
            accView.setText(acc);
            nickname_edit.setEnabled(true);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        float accuracy = locationData.getLocation().getAccuracy();
        float minAccuracy = 50;
        if ((accuracy > minAccuracy)){
            Toast error = Toast.makeText(getApplicationContext(), "Waiting for GPS accuracy...", Toast.LENGTH_SHORT);
            error.show();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

        Button chatButton = (Button) findViewById(R.id.button);
        String nicknameTest = nickName.getText().toString();
        float accuracy = locationData.getLocation().getAccuracy();

        float minAccuracy = 50;

        if ((!nicknameTest.matches("")) && (accuracy < minAccuracy) && (accuracy > 0)){
            chatButton.setEnabled(true);
        }else{
            chatButton.setEnabled(false);
        }
    }
}
