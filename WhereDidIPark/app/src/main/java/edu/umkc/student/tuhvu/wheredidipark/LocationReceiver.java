package edu.umkc.student.tuhvu.wheredidipark;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by tuvu on 11/8/2015.
 */
public class LocationReceiver extends BroadcastReceiver {
    private static final String TAG = "LocationReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Location Service Alarm initialized", Toast.LENGTH_SHORT).show();
        context.startService(new Intent(context, LocationService.class));
    }
}
