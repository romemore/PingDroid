package com.nox.pingdroid;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.util.Log;
import android.widget.Button;

import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;
import com.nox.pingdroid.async.MRequest;
import com.nox.pingdroid.async.MTask;
import com.nox.pingdroid.async.Queue;
import com.nox.pingdroid.async.event.MTaskResult;

import de.greenrobot.event.EventBus;

@EActivity(R.layout.main)
public class MyActivity extends Activity {
    private static final String TAG = MyApplication.TAG+"/"+MyActivity.class.getSimpleName();

    @ViewById
    Button button;

    @Click
    protected void buttonClicked() {
        List<MRequest> requests = Arrays.asList(new MRequest[]{
                new MRequest("http://fr.wikipedia.org/wiki/Test"),
                new MRequest("http://mire.ipadsl.net/")
        });
        Queue.get(this).add(new MTask(requests));
    }
    

    public void onEventMainThread(MTaskResult result) {
        if(result.isError()) {
            Log.e(TAG, result.getError().getMessage(), result.getError());
        } else {
            Log.i(TAG, new String(result.getResult()));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

}
