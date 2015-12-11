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

public class MainActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener {

    protected static final String TAG = "MainActivity";

    private GoogleMap mMap;

    private Marker mMarker;

    private SharedPreferences mySharedPreferences;

    LocationService mLocationService;
    boolean mBound = false;

    private Location mCurrentLocation;

    public Location getCurrentLocation() {
        setCurrentLocation(mLocationService.getLocation());
        return mCurrentLocation;
    }

    public void setCurrentLocation(Location location) {
        mCurrentLocation = location;
        placeMarKer(mCurrentLocation);
    }

    View parking_button;

    public String mLastUpdateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mySharedPreferences = this.getSharedPreferences(TAG, MODE_PRIVATE);
        parking_button = findViewById(R.id.save_location_button);

        // Initiate Google Maps API
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void ParkButtonHandler(View view) {
        updateLocationView(getCurrentLocation(), Helper.formatDate(getCurrentLocation().getTime()));
    }

    public void ShowParkingButtonHandler(View view) {
        DialogFragment newFragment = new SavedLocationDialog();
        newFragment.show(getSupportFragmentManager(), String.valueOf(R.string.saved_location_dialog_tag));
    }

    public void SaveParkingButtonHandler(View view) {
        DialogFragment newFragment = new SaveLocationDialog();
        newFragment.show(getSupportFragmentManager(), String.valueOf(R.string.save_location_dialog_tag));
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
        parking_button.setVisibility(View.INVISIBLE);
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
        Toast.makeText(this, "Location Service started", Toast.LENGTH_SHORT).show();
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
