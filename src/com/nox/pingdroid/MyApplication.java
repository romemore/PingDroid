/*
 * Created by IntelliJ IDEA.
 * User: rme
 * Date: 06/02/13
 * Time: 17:53
 */
package com.nox.pingdroid;

import android.app.Application;
import com.nox.pingdroid.async.Queue;

public class MyApplication extends Application {
    public static String TAG = "AndSyncNet";

    @Override
    public void onCreate() {
        super.onCreate();
        Queue.get(getApplicationContext());
    }
}