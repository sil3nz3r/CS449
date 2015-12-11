package edu.umkc.student.tuhvu.wheredidipark;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.widget.Button;

/**
 * Created by TV020594 on 11/28/2015.
 */
public class SaveLocationDialogTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity activity;
    private static final String KEY_SP_PACKAGE = "SaveLocationDialogTest";

    public SaveLocationDialogTest() {
        super(MainActivity.class);
    }

    public void setUp() throws Exception {
        setActivityInitialTouchMode(false);
        activity = (MainActivity)getActivity();
        clearSharedPrefs();
    }

    public void tearDown() throws Exception {
        clearSharedPrefs();
    }

    public void testDisplaySavedLocationList() throws Exception {
        // Define the save location button
        final Button saveLocationButton = (Button)activity.findViewById(R.id.save_location_button);

        // Click the save location button
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                saveLocationButton.performClick();
            }
        });
        getInstrumentation().waitForIdleSync();

        // Get the DialogFragment saved location dialog
        final Fragment savedLocationDialog = activity.getSupportFragmentManager().findFragmentByTag(String.valueOf(R.string.saved_location_dialog_tag));

        // Verify whether the saved location dialog shows up or not
        assertTrue(savedLocationDialog instanceof DialogFragment);
        assertTrue(((DialogFragment) savedLocationDialog).getShowsDialog());
    }

    private void clearSharedPrefs() {
        SharedPreferences sharedPreferences = getInstrumentation().getTargetContext().getSharedPreferences(KEY_SP_PACKAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
}
