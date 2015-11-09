package edu.umkc.student.tuhvu.wheredidipark;

import android.location.Location;
import android.widget.TextView;

/**
 * Created by tuvu on 11/8/2015.
 */
public class UpdateUI {
    public void updateTextView(TextView targetTextView, String content) {

    }

    public void updateLocationView(Location location, String lastUpdateTime) {
        MainActivity mActivity = new MainActivity();
        TextView mLastUpdateTimeTextView;
        TextView mLatitudeTextView;
        TextView mLongitudeTextView;

        mLatitudeTextView = (TextView) mActivity.findViewById(R.id.latitude_text);
        mLongitudeTextView = (TextView) mActivity.findViewById(R.id.longitude_text);
        mLastUpdateTimeTextView = (TextView) mActivity.findViewById(R.id.last_update_time_text);

        mLatitudeTextView.setText(String.valueOf(location.getLatitude()));
        mLongitudeTextView.setText(String.valueOf(location.getLongitude()));
        mLastUpdateTimeTextView.setText(lastUpdateTime);
    }
}
