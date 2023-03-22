package com.arenatiket.android.service;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import java.util.concurrent.TimeUnit;

/**
 * Created by kahfi on 24/05/16.
 */
public class BookingTimerService extends Service {

    public static final String BOOKING = "BOOKING_COUNTING";
    private LocalBroadcastManager broadcaster;
    private MyCount counter;

    @Override
    public void onCreate() {
        broadcaster = LocalBroadcastManager.getInstance(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public class MyCount extends CountDownTimer {

        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            Intent intent = new Intent(BOOKING);
            intent.putExtra("finish", "1");
            intent.putExtra("time", "waktu habis");
            broadcaster.sendBroadcast(intent);
            counter.cancel();
        }

        @Override
        public void onTick(long millisUntilFinished) {

            Intent intent = new Intent(BOOKING);
            intent.putExtra("time", "" + String.format("%d:%d",
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            broadcaster.sendBroadcast(intent);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (counter != null) {
            counter.cancel();
        }
        counter = new MyCount(600000, 1000);
        counter.start();

        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        if (counter!=null) {
            counter.cancel();
        }
        super.onDestroy();
    }

    public MyCount getCounter() {
        return counter;
    }
}
