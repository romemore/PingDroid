package com.nox.pingdroid;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.example.pingdroid.R;
import com.nox.pingdroid.async.MRequest;
import com.nox.pingdroid.async.MTask;
import com.nox.pingdroid.async.Queue;
import com.nox.pingdroid.async.event.MTaskResult;
import de.greenrobot.event.EventBus;

import java.util.Arrays;
import java.util.List;

public class MyActivity extends Activity {
    private static final String TAG = MyApplication.TAG+"/"+MyActivity.class.getSimpleName();

    Button button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<MRequest> requests = Arrays.asList(new MRequest[]{
                        new MRequest("http://fr.wikipedia.org/wiki/Test"),
                        new MRequest("http://mire.ipadsl.net/")
                });
                Queue.get(MyActivity.this).add(new MTask(requests));
            }
        });
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
