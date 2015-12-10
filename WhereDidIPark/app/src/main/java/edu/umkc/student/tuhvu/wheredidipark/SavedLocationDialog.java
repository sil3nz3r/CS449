package edu.umkc.student.tuhvu.wheredidipark;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by TV020594 on 11/30/2015.
 */
public class SavedLocationDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Saved Location")
                .setItems(new String[]{"Location 1", "Location 2", "Location 3"},
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int index) {

                            }
                        });
        return builder.create();
    }
}
