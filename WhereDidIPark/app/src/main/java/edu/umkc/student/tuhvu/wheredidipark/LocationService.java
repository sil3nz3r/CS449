package edu.umkc.student.tuhvu.wheredidipark;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.util.Date;

/**
 * Handle LocationService data with Google Api
 */
public class LocationService extends Service implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    public LocationService() {
        super();
    }

    public LocationService(Context context) {
    }

    private static final String TAG = "LocationService";

    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public LocationService getService() {
            return LocationService.this;
        }
    }

    /**
     * LocationService interval values
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    public static final int TWO_MINUTES = 1000 * 60 * 2;
    public static final float DESIRED_ACCURACY_DIAMETER = 500.0f;

    /**
     * Keys for storing activity state in the Bundle.
     */
    protected final static String REQUESTING_LOCATION_UPDATES_KEY = "requesting-location-updates-key";
    protected final static String LOCATION_KEY = "location-key";
    protected final static String LAST_UPDATED_TIME_STRING_KEY = "last-updated-time-string-key";

    /**
     * Location objects
     */
    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;
    protected Location mCurrentLocation;

    /**
     * The last time that the service was updated
     */
    protected String mLastUpdateTime;

    /**
     * Permanent storage object
     */
    private SharedPreferences mySharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        connectGooglePlayService();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /**
     * On service Start Command
     *
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        connectGooglePlayService();
        return START_NOT_STICKY;
    }

    /**
     * Requests location updates from the FusedLocationApi.
     */
    protected void connectGooglePlayService() {
        Log.i(TAG, "connectGooglePlayService");

        if (mGoogleApiClient == null) {
            buildGoogleApiClient();
        }

        assert GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS : "Google Play Services is not available";
        if (!mGoogleApiClient.isConnected() || !mGoogleApiClient.isConnecting()) {
            mGoogleApiClient.connect();
        }
    }

    protected void disconnectGooglePlayService() {
        Log.i(TAG, "disconnectGooglePlayService");
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * Builds a GoogleApiClient object. Uses the {@code #addApi} method to request the
     * LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "buildGoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onDestroy() {
        disconnectGooglePlayService();
        super.onDestroy();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Connected to GoogleApiClient");

        // Initialize LocationRequest object
        createLocationRequest();

        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        Location mTempLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        assert mTempLocation != null : R.string.no_location_detected;
        updateLocation(mTempLocation);
    }

    private void updateLocation(Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
    }

    /**
     * Sets up the location request. Android has two location request settings:
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     * <p/>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     * <p/>
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    protected void createLocationRequest() {
        Log.i(TAG, "createLocationRequest");
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(TAG, "GoogleApiClient: connection has been suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "GoogleApiClient: connection failed");
        disconnectGooglePlayService();
        stopSelf();
    }

    @Override
    public void onLocationChanged(Location location) {
        assert mGoogleApiClient != null && mGoogleApiClient.isConnected() : "Google API Client is in bad state";

        if (location != null) {
            Log.i(TAG, "position:" + location.getLatitude() + ", " + location.getLongitude() + "accuracy: " + location.getAccuracy());

//            // We have our desired accuracy of so lets quit this service,
//            // onDestroy will be called and stop our location updates
//            if (location.getAccuracy() < DESIRED_ACCURACY_DIAMETER) {
//                disconnectGooglePlayService();
//                // Update location
//            }
        }
    }

    /*Method for clients*/
    public Location getLocation() {
        if (mCurrentLocation != null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }
        return mCurrentLocation;
    }

    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
        }
        // If the new location is more than two minutes older, it must be worse
        else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether two providers are the same
     */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

//    private void updateCoordinatesFromSharedPreferences() {
//        if (mySharedPreferences.contains("IsExist")) {
//            if (mCurrentLocation == null) {
//                mCurrentLocation = new Location(STORAGE_SERVICE);
//                mCurrentLocation.setLatitude(Double.parseDouble(mySharedPreferences.getString("Latitude", "")));
//                mCurrentLocation.setLongitude(Double.parseDouble(mySharedPreferences.getString("Longitude", "")));
//                mLastUpdateTime = mySharedPreferences.getString("LastUpdateTime", "");
//            } else {
//                Toast.makeText(this, "mCurrentLocation has already been set!", Toast.LENGTH_LONG).show();
//            }
//        }
//    }
//
//    private void saveCoordinatesToSharedPreferences() {
//        SharedPreferences.Editor edit = mySharedPreferences.edit();
//        edit.clear();
//        edit.putBoolean("IsExist", true);
//        edit.putString("Latitude", String.valueOf(mCurrentLocation.getLatitude()));
//        edit.putString("Longitude", String.valueOf(mCurrentLocation.getLongitude()));
//        edit.putString("LastUpdateTime", String.valueOf(mLastUpdateTime));
//        edit.commit();
//        Toast.makeText(this, "location coordinates are saved", Toast.LENGTH_SHORT).show();
//    }
}
