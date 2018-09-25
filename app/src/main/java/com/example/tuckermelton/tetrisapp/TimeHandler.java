package com.example.tuckermelton.tetrisapp;

import android.os.Handler;
import android.os.HandlerThread;
import java.util.Calendar;

/**
 * Created by tuckermelton on 2/18/18.
 */

//This class allows us to run a method every rateLimitMillis
public class TimeHandler {
    public interface IUpdate{
        void updateSeconds(final int secondsValue);
    }
    protected Runnable rateLimitRequest;
    protected int rateLimitMillis = 1000;
    protected IUpdate iUpdate;
    protected Handler handler;
    protected HandlerThread handlerThread;

    public TimeHandler(final IUpdate iUpdate) {
        handlerThread = new HandlerThread("TimeHandler");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
        this.iUpdate = iUpdate;
        rateLimitRequest = new Runnable() {
            @Override
            public void run() {
                int seconds = Calendar.getInstance().get(Calendar.SECOND);
                // Handlers usually communicate with message queues, but this is simpler
                iUpdate.updateSeconds(seconds);
                handler.postDelayed(this, rateLimitMillis);
            }
        };
        handler.postDelayed(rateLimitRequest, rateLimitMillis);
    }
}
