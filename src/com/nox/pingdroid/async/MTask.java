/*
 * Created by IntelliJ IDEA.
 * User: rme
 * Date: 06/02/13
 * Time: 17:37
 */
package com.nox.pingdroid.async;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.nox.pingdroid.MyApplication;
import com.squareup.tape.Task;

import java.util.Arrays;
import java.util.List;

//import com.github.kevinsawicki.http.HttpRequest;
//import static com.github.kevinsawicki.http.HttpRequest.post;

/** Uploads the specified file to imgur.com. */
public class MTask implements Task<MTask.MCallback> {
  private static final long serialVersionUID = 126142781146165256L;

  private static final String TAG = MyApplication.TAG+"/"+MTask.class.getSimpleName();

//  private static final String IMGUR_API_KEY = "74e20e836f0307a90683c4643a2b656e";
//  private static final String IMGUR_UPLOAD_URL = "http://api.imgur.com/2/upload";
//  private static final Pattern IMGUR_URL_REGEX = Pattern.compile("<imgur_page>(.+?)</imgur_page>");
  private static final Handler MAIN_THREAD = new Handler(Looper.getMainLooper());

  public interface MCallback {
    void onSuccess(String url);
    void onFailure(Exception ex);
  }

  private final List<MRequest> requests;

  public MTask(MRequest request) {
      this( Arrays.asList(new MRequest[]{request}) );
  }

  public MTask(List<MRequest> requests) {
    this.requests = requests;
  }

  @Override public void execute(final MCallback callback) {
      if(requests.size()==0)return;
    // Image uploading is slow. Execute HTTP POST on a background thread.
    new Thread(new Runnable() {
      @Override public void run() {
          StringBuffer requestsAsStringBuf = new StringBuffer();
          for(MRequest r:requests) {
              requestsAsStringBuf.append(',');
              requestsAsStringBuf.append(r.getUrl());
          }
          final String requestsAsString = requestsAsStringBuf.toString().substring(1);

          Log.d(TAG, "start task ("+requests.size()+','+requestsAsString+")");
          try {
              Thread.sleep(Math.round(Math.random()*10000));
          } catch (InterruptedException e) {
              e.printStackTrace();
          }
          Log.d(TAG, "finish task ("+requests.size()+','+requestsAsString+")");
          MAIN_THREAD.post(new Runnable() {
            @Override public void run() {
              callback.onSuccess(requestsAsString+','+requests.size());
            }
          });
      }
    }).start();
  }
}

/*
        try {
          HttpRequest request = post(IMGUR_UPLOAD_URL)
              .part("key", IMGUR_API_KEY)
              .part("image", file);

          if (request.ok()) {
            Matcher m = IMGUR_URL_REGEX.matcher(request.body());
            m.find();
            final String url = m.group(1);
            Log.i(TAG, "Upload success! " + url);

            // Get back to the main thread before invoking a callback.
            MAIN_THREAD.post(new Runnable() {
              @Override public void run() {
                callback.onSuccess(url);
              }
            });
          } else {
            Log.i(TAG, "Upload failed :(  Will retry.");

            // Get back to the main thread before invoking a callback.
            MAIN_THREAD.post(new Runnable() {
              @Override public void run() {
                callback.onFailure();
              }
            });
          }
        } catch (RuntimeException e) {
          e.printStackTrace();
          throw e;
        }
*/