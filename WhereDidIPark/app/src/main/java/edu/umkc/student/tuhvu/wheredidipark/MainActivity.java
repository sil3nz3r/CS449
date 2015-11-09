package edu.umkc.student.tuhvu.wheredidipark;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends FragmentActivity implements
        OnMapReadyCallback {

    protected static final String TAG = "MainActivity";

    private GoogleMap mMap;

    private Marker mMarker;

    private SharedPreferences mainActivityPreferences;

    private PendingIntent mPendingIntent;
    private AlarmManager mAlartmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivityPreferences = this.getSharedPreferences(TAG, MODE_PRIVATE);

        // Initiate Google Maps API
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Retrieve a PendingIntent that will perform a broadcast
        Intent alarmIntent = new Intent(this, LocationServiceAlarmReceiver.class);
        mPendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
    }

    public void ParkButtonHandler(View view) {
    }

    public void ClearMapButtonHandler(View view) {
        mMap.clear();
    }

    /*private void placeMarKer() {
        // Update map
        LatLng mLocationCoor = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        if (mMarker != null) {
            mMarker.remove();
        }
        mMarker = mMap.addMarker(new MarkerOptions().position(mLocationCoor).title(getString(R.string.marker_title)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mLocationCoor));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLocationCoor, 15));
    }*/

    @Override
    protected void onStart() {
        super.onStart();
        startAlarm();
    }

    protected void startAlarm() {
        mAlartmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        int interval = 10000;

        mAlartmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, mPendingIntent);
        Toast.makeText(this, "Location Service Alarm set", Toast.LENGTH_SHORT).show();
    }

    public void cancelAlarm() {
        if (mAlartmManager != null) {
            mAlartmManager.cancel(mPendingIntent);
            Toast.makeText(this, "Location Service Alarm canceled", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //saveCoordinatesToSharedPreferences();
    }

    @Override
    protected void onStop() {
        super.onStop();
        cancelAlarm();
        //saveCoordinatesToSharedPreferences();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //saveCoordinatesToSharedPreferences();
    }

    /**
     * Updates fields based on data stored in the bundle.
     *
     * @param savedInstanceState The activity state saved in the Bundle.
     */
    /*private void updateValuesFromBundle(Bundle savedInstanceState) {
        Log.i(TAG, "Updating values from bundle");
        if (savedInstanceState != null) {
            // Update the value of mRequestingLocationUpdates from the Bundle, and make sure that
            // the Start Updates and Stop Updates buttons are correctly enabled or disabled.
            if (savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        REQUESTING_LOCATION_UPDATES_KEY);
            }

            // Update the value of mCurrentLocation from the Bundle and update the UI to show the
            // correct latitude and longitude.
            if (savedInstanceState.keySet().contains(LOCATION_KEY)) {
                // Since LOCATION_KEY was found in the Bundle, we can be sure that mCurrentLocation
                // is not null.
                mCurrentLocation = savedInstanceState.getParcelable(LOCATION_KEY);
            }

            // Update the value of mLastUpdateTime from the Bundle and update the UI.
            if (savedInstanceState.keySet().contains(LAST_UPDATED_TIME_STRING_KEY)) {
                mLastUpdateTime = savedInstanceState.getString(LAST_UPDATED_TIME_STRING_KEY);
            }
            //updateUI();
        }
    }*/

    /*private void updateCoordinatesFromSharedPreferences() {
        if (mainActivityPreferences.contains("IsExist")) {
            if (mCurrentLocation == null) {
                mCurrentLocation = new Location(STORAGE_SERVICE);
                mCurrentLocation.setLatitude(Double.parseDouble(mainActivityPreferences.getString("Latitude", "")));
                mCurrentLocation.setLongitude(Double.parseDouble(mainActivityPreferences.getString("Longitude", "")));
                mLastUpdateTime = mainActivityPreferences.getString("LastUpdateTime", "");
            } else {
                Toast.makeText(this, "mCurrentLocation has already been set!", Toast.LENGTH_LONG).show();
            }
        }
    }*/

    /*private void saveCoordinatesToSharedPreferences() {
        SharedPreferences.Editor edit = mainActivityPreferences.edit();
        edit.clear();
        edit.putBoolean("IsExist", true);
        edit.putString("Latitude", String.valueOf(mCurrentLocation.getLatitude()));
        edit.putString("Longitude", String.valueOf(mCurrentLocation.getLongitude()));
        edit.putString("LastUpdateTime", String.valueOf(mLastUpdateTime));
        edit.commit();
        Toast.makeText(this, "location coordinates are saved", Toast.LENGTH_SHORT).show();
    }

    *//**
     * Stores activity data in the Bundle.
     *//*
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY, mRequestingLocationUpdates);
        savedInstanceState.putParcelable(LOCATION_KEY, mCurrentLocation);
        savedInstanceState.putString(LAST_UPDATED_TIME_STRING_KEY, mLastUpdateTime);
        super.onSaveInstanceState(savedInstanceState);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
}
