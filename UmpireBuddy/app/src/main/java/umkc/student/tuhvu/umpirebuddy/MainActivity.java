package umkc.student.tuhvu.umpirebuddy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends Activity implements View.OnClickListener {


    private View mStrikeButton;
    private Button mBallButton;
    private int mStrikeCount;
    private int mBallCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStrikeButton = findViewById(R.id.Strike_button);
        mBallButton = (Button)findViewById(R.id.Ball_button);

        mStrikeButton.setOnClickListener(this);
        mBallButton.setOnClickListener(this);
    }

    private void updateCount(int countId) {

        TextView mStrikeCount_tv = (TextView)findViewById(R.id.Strike_count);
        TextView mBallCount_tv = (TextView)findViewById(R.id.Ball_count);

        switch (countId) {
            case R.id.Strike_count: // case of strike
                // set the strike count
                mStrikeCount_tv.setText(Integer.toString(mStrikeCount));

                // set the ball count to 0 if the strike count is also 0
                if (mStrikeCount == 0) {
                    mBallCount_tv.setText(Integer.toString(0));
                }
                break;
            case R.id.Ball_count: // case of ball
                // set the ball count
                mBallCount_tv.setText(Integer.toString(mBallCount));

                // set the strike count to 0 if the ball count is also 0
                if (mBallCount == 0) {
                    mStrikeCount_tv.setText(Integer.toString(0));
                }
                break;
            default: // exception case is currently resetting the count
                mStrikeCount_tv.setText(Integer.toString(0));
                mBallCount_tv.setText(Integer.toString(0));
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Strike_button:
                mStrikeCount++;
                if (mStrikeCount == 3) {
                    final AlertDialog dlgAlert = new AlertDialog.Builder(this)
                            .setMessage(R.string.Strike_out_text)
                            .setPositiveButton(android.R.string.ok, null)
                            .setCancelable(false)
                            .create();

                    dlgAlert.setOnShowListener(new DialogInterface.OnShowListener() {

                        @Override
                        public void onShow(DialogInterface dialog) {
                            Button btn = dlgAlert.getButton(AlertDialog.BUTTON_POSITIVE);
                            btn.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View view) {
                                    dlgAlert.dismiss();
                                }
                            });
                        }
                    });

                    mStrikeCount = 0;


//                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
//                    dlgAlert.setMessage("Out!");
//                    dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                        }
//                    });
                    dlgAlert.show();
                }
                updateCount(R.id.Strike_count);
                break;
            case R.id.Ball_button:
                mBallCount++;
                if (mBallCount == 4)
                {
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
                    dlgAlert.setMessage("Walk!");
                    dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
                    dlgAlert.show();
                    mBallCount = 0;
                }
                updateCount(R.id.Ball_count);
                break;
            default:
        }
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
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        switch (item.getItemId()) {
            case R.id.reset:
                mStrikeCount = 0;
                mBallCount = 0;
                updateCount(-1);
                return true;
            case R.id.about:
                return true;
            default:
        }

        return super.onOptionsItemSelected(item);
    }
}
