package com.nox.pingdroid.async;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.nox.pingdroid.MyApplication;
import com.google.gson.Gson;
import com.squareup.tape.FileObjectQueue;
import com.squareup.tape.TaskQueue;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

public class Queue extends TaskQueue<MTask> {
    private static final String TAG = MyApplication.TAG+"/"+Queue.class.getSimpleName();

    /**
     * Singleton handling
     */
   private static Queue instance;
   private WeakReference<Context> context;

   private Queue(FileObjectQueue<MTask> delegate, Context context) {
       super(delegate);
       this.context = new WeakReference<Context>(context);
       instance = this;
       if (size() > 0) {
         startService();
       }
   }

   public static Queue get(Context context) {
       if(instance != null)
           return instance;
       else
           return create(context);
   }

   private static Queue create(Context context) {
       FileObjectQueue.Converter<MTask> converter = new GsonConverter<MTask>(new Gson(), MTask.class);
       File queueFile = new File(context.getFilesDir(), "queue");
       FileObjectQueue<MTask> delegate;
       try {
           delegate = new FileObjectQueue<MTask>(queueFile, converter);
       } catch (IOException e) {
           throw new RuntimeException("Unable to create file queue.", e);
       }
       return new Queue(delegate, context);
   }

    /**
     *
     */

      private void startService() {
//TODO handle null context !!!
        context.get().startService(new Intent(context.get(), MTaskService.class));
      }

      @Override public void add(MTask entry) {
        super.add(entry);
        Log.d(TAG, "added to queue " + entry + " ("+size()+")");
        startService();
      }

      @Override public void remove() {
        super.remove();
          Log.d(TAG, "removed from queue ("+size()+")");
      }

    /**
     * execute with listener
     */
//    public void execute(final MRequest req, final MListener listener) {
//        addRequestListenerToListOfRequestListeners(cachedSpiceRequest,
//            requestListener);
//        this.requestQueue.add(cachedSpiceRequest);
//    }




}
