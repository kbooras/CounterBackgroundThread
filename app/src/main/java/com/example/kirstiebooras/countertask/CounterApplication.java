package com.example.kirstiebooras.countertask;

import android.app.Application;
import android.util.Log;

/**
 * Create Counter Task
 * Handle Main Activity registration
 * Created by kirstiebooras on 2/23/15.
 */
public class CounterApplication extends Application {

    private static final String TAG = "CounterApplication";
    private MainActivity mMainActivity = null;
    private CounterThread mThread;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "create application");

        // Create and start a CounterThread
        mThread = new CounterThread(this);
        mThread.start();
    }

    /**
     * Registers the activity with the application
     * Synchronized with updateActivityFromBgThread to make sure the main thread is not set to null
     * resulting in a NullPointerException.
     */
    public synchronized void setUpdatable(MainActivity activity) {
        this.mMainActivity = activity;
    }

    /**
     * Tell the MainActivity to display the new second count
     * @param seconds: The number of seconds to display
     */
    public void notify(int seconds) {
        Log.v(TAG, "notify");
        updateMainActivity(seconds);
//        Handler mainHandler = new Handler(this.getMainLooper());
//        Runnable myRunnable = new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        };
//        mainHandler.post(myRunnable);
    }

    private synchronized void updateMainActivity(int seconds) {
        if (mMainActivity != null) {
            mMainActivity.updateActivityFromBgThread(seconds);
        }
    }

    /**
     * Pauses the CounterThread. Called from MainActivity.onPause()
     */
    public void pauseThread() {
        Log.v(TAG, "call pause thread");
        mThread.onPause();
    }

    /**
     * Resumes the CounterThread. Called from MainActivity.onResume()
     */
    public void resumeThread() {
        Log.v(TAG, "call resume thread");
        mThread.onResume();
    }


    // one receiving a notify[seconds] cause the MainActivity to display the new count by using updating runnable
    // updating runnable calls updateSeconds on MainActivity
}
