package com.example.kirstiebooras.countertask;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Displays a counter with the number of seconds the activity has been active.
 */
public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    private TextView mCount;
    protected CounterApplication mApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.v(TAG, "create activity");

        mCount = (TextView) findViewById(R.id.seconds);
        mApplication = (CounterApplication) getApplication();
    }

    /**
     * Activity registers itself with the application
     */
    @Override
    protected void onStart() {
        super.onStart();
        CounterApplication app = (CounterApplication) getApplication();
        app.setUpdatable(this);
    }

    /**
     * Implements a runnable to update the number of seconds on the UI thread
     * @param seconds: The number of seconds to set the TextView equal to
     */
    public final void updateActivityFromBgThread(final int seconds) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateSeconds(seconds);
            }
        });
    }

    /**
     * Called form the application to update the number of seconds
     * @param seconds: The number of seconds
     */
    public void updateSeconds(int seconds) {
        mCount.setText(String.valueOf(seconds));
    }

    /**
     * Called when the activity comes back. Calls the resumeThread method in the Application
     * to tell the thread to resume counting.
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "resume");
        // Send onResume to the application
        mApplication.resumeThread();
    }

    /**
     * Called when the activity goes away. Calls the pauseThread method in the Application
     * to tell the thread to pause counting.
     */
    @Override
    protected void onPause() {
        super.onPause();
        Log.v(TAG, "pause");
        // Send onPause to the application
        mApplication.pauseThread();
    }

    /**
     * Activity de-registers itself with the application
     */
    @Override
    protected void onStop() {
        CounterApplication app = (CounterApplication) getApplication();
        app.setUpdatable(null);
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }
}
