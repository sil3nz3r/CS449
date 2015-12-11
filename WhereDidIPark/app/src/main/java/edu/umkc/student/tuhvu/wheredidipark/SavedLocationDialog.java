package edu.umkc.student.tuhvu.wheredidipark;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sil3nz3r on 12/01/2015.
 */
public class SavedLocationDialog extends DialogFragment {
    private SharedPreferences mLocationSharedPreferences;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mLocationSharedPreferences = getActivity().getSharedPreferences(getString(R.string.location_shared_preferences_name), Context.MODE_PRIVATE);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle("Saved Location");
//                .setItems(new String[]{"Location 1", "Location 2", "Location 3"},
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int index) {
//                                retrieveLocationFromSharedPreferences(index);
//                            }
//                        });

        // Custom ListView in Java
        ListView list = new ListView(getActivity());
        List<String> elements = new ArrayList<String>();
        for (int i = 0; i < 3; i ++) {
            elements.add("Location" + String.valueOf(i + 1));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_2, android.R.id.text1, elements);

        //ListAdapter adapter1 = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, mCursor,)

        list.setAdapter(adapter);
        //list.setOnItemClickListener();
        builder.setView(list);

        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder//.setView(inflater.inflate(R.layout.dialog, null))
                .setTitle("Saved Location")
                .setView(list)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do something ...
                    }
                });
        return builder.create();
    }

    private void retrieveLocationFromSharedPreferences(int index) {
        Location location = new Location(String.valueOf(R.string.shared_preferences_service));

        if (mLocationSharedPreferences.contains(String.valueOf(index) + "IsLocationExist")) {

            location.setLatitude(Double.longBitsToDouble(mLocationSharedPreferences.getLong(String.valueOf(index) + "Latitude", 0L)));
            location.setLongitude(Double.longBitsToDouble(mLocationSharedPreferences.getLong(String.valueOf(index) + "Longitude", 0L)));
            location.setTime(mLocationSharedPreferences.getLong(String.valueOf(index) + "LastUpdateTime", 0L));
            ((MainActivity)getActivity()).setCurrentLocation(location);
        } else {
            Toast.makeText(getActivity(), "Saved location is empty.", Toast.LENGTH_SHORT).show();
        }
    }
}
