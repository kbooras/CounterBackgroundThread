package com.example.kirstiebooras.countertask;

import android.content.Context;
import android.util.Log;

/**
 * when paused, do nothing
 * when running, update the count every second and notify the Main Activity of the new count
 * Created by kirstiebooras on 2/23/15.
 */
public class CounterThread extends Thread {

    private static final String TAG = "CounterThread";
    private final Object mPauseLock = new Object();
    private CounterApplication mApp;
    private boolean mRunning;
    private boolean mPaused;
    private int mSeconds;

    public CounterThread(Context context) {
        mApp = (CounterApplication) context.getApplicationContext();
        mRunning = true;
        mPaused = false;
        mSeconds = 0;
        Log.v(TAG, "Create thread");
    }

    /**
     * In a loop, sleep for 1 second. If in the running state, call notify(seconds) on the
     * Application. If interrupted, toggle the running state.
     */
    @Override
    public void run() {
        Log.v(TAG, "start thread");
        while(mRunning) {
            try {
                // Wait 1 second for the next update
                Thread.sleep(1000);
                mSeconds++;
                Log.v(TAG, "notify application");
                mApp.notify(mSeconds);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (mPauseLock) {
                while (mPaused) {
                    try {
                        mPauseLock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Called from Application.pauseThread()
     */
    public void onPause() {
        synchronized (mPauseLock) {
            Log.v(TAG, "pause thread");
            mPaused = true;
        }
    }

    /**
     * Called from Application.resumeThread()
     */
    public void onResume() {
        synchronized (mPauseLock) {
            Log.v(TAG, "resume thread");
            mPaused = false;
            mPauseLock.notifyAll();
        }
    }
}
