package edu.umkc.student.tuhvu.wheredidipark;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

/**
 * Created by TV020594 on 11/30/2015.
 */
public class SaveLocationDialog extends DialogFragment {
    private SharedPreferences mLocationSharedPreferences;

//    static SaveLocationDialog newInstance(Location locationData) {
//        SaveLocationDialog dialog = new SaveLocationDialog();
//
//        Bundle args = new Bundle();
//    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mLocationSharedPreferences = getActivity().getSharedPreferences(getString(R.string.location_shared_preferences_name), Context.MODE_PRIVATE);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Saved Location")
                .setItems(new String[]{"Location 1", "Location 2", "Location 3"},
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int index) {
                                saveLocationToSharedPreferences(((MainActivity) getActivity()).getCurrentLocation(), index);
                            }
                        });
        return builder.create();
    }

    private void saveLocationToSharedPreferences(Location location, int index) {
        SharedPreferences.Editor edit = mLocationSharedPreferences.edit();
        edit.clear();
        edit.putBoolean(String.valueOf(index) + "IsLocationExist", true);
        edit.putLong(String.valueOf(index) + "Latitude", Double.doubleToLongBits(location.getLatitude()));
        edit.putLong(String.valueOf(index) + "Longitude", Double.doubleToLongBits(location.getLongitude()));
        edit.putLong(String.valueOf(index) + "LastUpdateTime", location.getTime());
        edit.commit();

        Toast.makeText(getActivity(), "location coordinates are saved.", Toast.LENGTH_SHORT).show();
    }
}
