package edu.umkc.student.tuhvu.wheredidipark;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
        OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener {

    protected static final String TAG = "MainActivity";

    private GoogleMap mMap;

    private Marker mMarker;

    private SharedPreferences mySharedPreferences;

    private PendingIntent mPendingIntent;
    private AlarmManager mAlarmManager;

    LocationService mLocationService;
    boolean mBound = false;

    Location mCurrentLocation;
    String mLastUpdateTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mySharedPreferences = this.getSharedPreferences(TAG, MODE_PRIVATE);

        // Initiate Google Maps API
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void ParkButtonHandler(View view) {
        mCurrentLocation = mLocationService.getLocation();
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateLocationView(mCurrentLocation, mLastUpdateTime);
        placeMarKer(mCurrentLocation);
    }

    public void ShowParkingButtonHandler(View view) {
//        try {
//            setCoordinatesFromSharedPreferences();
//            placeMarKer(mCurrentLocation);
//        }
//        catch (Resources.NotFoundException eNotFound) {
//            Toast.makeText(this, eNotFound.getMessage(), Toast.LENGTH_LONG).show();
//        }

        DialogFragment newFragment = new SavedLocationDialog();
        newFragment.show(getSupportFragmentManager(), "colors");
    }

    public void SaveParkingButtonHandler(View view) {
        saveCoordinatesToSharedPreferences(mCurrentLocation, mLastUpdateTime);
        view.setVisibility(View.INVISIBLE);
    }

    public void updateLocationView(Location location, String lastUpdateTime) {
        TextView mLastUpdateTimeTextView;
        TextView mLatitudeTextView;
        TextView mLongitudeTextView;

        mLatitudeTextView = (TextView) findViewById(R.id.latitude_text);
        mLongitudeTextView = (TextView) findViewById(R.id.longitude_text);
        mLastUpdateTimeTextView = (TextView) findViewById(R.id.last_update_time_text);

        mLatitudeTextView.setText(String.valueOf(location.getLatitude()));
        mLongitudeTextView.setText(String.valueOf(location.getLongitude()));
        mLastUpdateTimeTextView.setText(lastUpdateTime);
    }

    public void ClearMapButtonHandler(View view) {
        mMap.clear();
    }

    private void placeMarKer(Location location) {
        // Update map
        LatLng mLocationCoor = new LatLng(location.getLatitude(), location.getLongitude());
        if (mMarker != null) {
            mMarker.remove();
        }

        mMarker = mMap.addMarker(new MarkerOptions().position(mLocationCoor).title(getString(R.string.marker_title)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mLocationCoor));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLocationCoor, 15));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        String selectedTitle = marker.getTitle();
        String currentTitle = mMarker.getTitle();
        if (selectedTitle.equals(currentTitle)) {
            View parking_button = findViewById(R.id.save_location_button);
            parking_button.setVisibility(View.VISIBLE);
            return true;
        }

        return false;
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // We've bound to LocationService, cast the Ibinder and get LocationService instance
            LocationService.LocalBinder binder = (LocationService.LocalBinder) service;
            mLocationService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        //startAlarm();
        /*Toast.makeText(this, "Location Service started", Toast.LENGTH_SHORT).show();
        this.startService(new Intent(this, LocationService.class));*/
        Intent intent = new Intent(this, LocationService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(this, "Location Service stopped", Toast.LENGTH_SHORT).show();
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void setCoordinatesFromSharedPreferences() {
        if (mySharedPreferences.contains("IsLocationExist")) {
            mCurrentLocation = new Location(STORAGE_SERVICE);
            mCurrentLocation.setLatitude(Double.parseDouble(mySharedPreferences.getString("Latitude", "")));
            mCurrentLocation.setLongitude(Double.parseDouble(mySharedPreferences.getString("Longitude", "")));
            mLastUpdateTime = mySharedPreferences.getString("LastUpdateTime", "");
        } else {
            throw new Resources.NotFoundException("Saved location does not exist. Please locate one!");
        }
    }

    private void saveCoordinatesToSharedPreferences(Location location, String LastUpdateTime) {
        SharedPreferences.Editor edit = mySharedPreferences.edit();
        edit.clear();
        edit.putBoolean("IsLocationExist", true);
        edit.putString("Latitude", String.valueOf(location.getLatitude()));
        edit.putString("Longitude", String.valueOf(location.getLongitude()));
        edit.putString("LastUpdateTime", String.valueOf(LastUpdateTime));
        edit.commit();

        Toast.makeText(this, "location coordinates are saved", Toast.LENGTH_SHORT).show();
    }

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
        mMap.setOnMarkerClickListener((GoogleMap.OnMarkerClickListener) this);
    }
}
