package com.arenatiket.android.service;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.arenatiket.android.utils.Utils;

import java.util.concurrent.TimeUnit;

public class PaymentTimerService extends Service {
    public static final String PAYMENT_COUNTING = "PAYMENT_COUNTING";
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
            Intent intent = new Intent(PAYMENT_COUNTING);
            intent.putExtra("finish", "1");
            broadcaster.sendBroadcast(intent);
        }

        @Override
        public void onTick(long millisUntilFinished) {

            Intent intent = new Intent(PAYMENT_COUNTING);
            String textToShow = "" + String.format("%d:%d:%d", TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
//            if (TimeUnit.MILLISECONDS.toHours(millisUntilFinished) <= 3l) {
//                textToShow = "3 jam dari sekarang";
//            }
//            if (TimeUnit.MILLISECONDS.toHours(millisUntilFinished)  < 0) {
//                textToShow = "Expired";
//            }
            intent.putExtra("time", textToShow);
            broadcaster.sendBroadcast(intent);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (counter != null) {
            counter.cancel();
        }

        long diff = intent.getExtras().getLong("diff_millis");
        Utils.logd("starting service diff " + diff);
        counter = new MyCount(diff, 1000);
        counter.start();


        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        if (counter != null) {
            counter.cancel();
        }
        super.onDestroy();
    }
}
