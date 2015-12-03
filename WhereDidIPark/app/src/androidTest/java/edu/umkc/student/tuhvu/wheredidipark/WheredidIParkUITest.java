package edu.umkc.student.tuhvu.wheredidipark;

import android.content.Context;
import android.content.SharedPreferences;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

/**
 * Created by TV020594 on 12/1/2015.
 */
public class WheredidIParkUITest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity activity;
    private static final String KEY_SP_PACKAGE = "PrivateStorageUtilsTest";

    public WheredidIParkUITest() {
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
        Button saveLocationButton;
        saveLocationButton = (Button)activity.findViewById(R.id.save_location_button);
    }

    private void clearSharedPrefs() {
        SharedPreferences sharedPreferences = getInstrumentation().getTargetContext().getSharedPreferences(KEY_SP_PACKAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
}
