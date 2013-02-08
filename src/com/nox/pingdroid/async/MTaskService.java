package com.nox.pingdroid.async;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import com.nox.pingdroid.MyApplication;
import com.nox.pingdroid.async.event.MTaskResult;
import de.greenrobot.event.EventBus;

import java.util.concurrent.atomic.AtomicBoolean;

public class MTaskService extends Service implements MTask.MCallback {
    private static final String TAG = MyApplication.TAG + "/" + MTaskService.class.getSimpleName();

    private AtomicBoolean running = new AtomicBoolean(false);
    private Queue queue;

    @Override
    public void onCreate() {
        super.onCreate();
        queue = Queue.get(getApplicationContext());
        Log.i(TAG, "Service starting!");
        executeNext();
    }

    @Override
    public void onSuccess(String result) {
        Log.i(TAG, "onSuccess : "+ result);
        running.set(false);
        queue.remove();
        EventBus.getDefault().post(new MTaskResult(result.getBytes()));
        executeNext();
    }

    @Override
    public void onFailure(Exception ex) {
        Log.e(TAG, "onFailure : " + ex.getMessage(), ex);
        EventBus.getDefault().post(new MTaskResult(ex));
    }

    private void executeNext() {
      if (running.get()) return; // Only one task at a time.

      MTask task = queue.peek();
      if (task != null) {
        running.set(true);
        task.execute(this);
      } else {
        Log.i(TAG, "Service stopping!");
        stopSelf(); // No more tasks are present. Stop.
      }
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

}
