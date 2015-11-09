package edu.umkc.student.tuhvu.wheredidipark;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by tuvu on 11/8/2015.
 */
public class LocationServiceAlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "LocationServiceAlarmReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
        context.startService(new Intent(context, LocationService.class));
    }
}
