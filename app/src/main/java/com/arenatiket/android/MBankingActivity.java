package com.arenatiket.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arenatiket.android.api.JsonCache;
import com.arenatiket.android.service.PaymentTimerService;
import com.arenatiket.android.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class MBankingActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private LinearLayout confirmButton;
    private TextView datetimePayByTextView;
    private TextView reservationIdTextView;
    private TextView bankNameAndCodeTextView;
    private TextView accountNumberTextView;
    private TextView timerTextView;
    private int bankId;
    private TextView transferAmountTextView;
    private BroadcastReceiver countingReceiver;
    private String reservationId;
    private int price;
    private TextView accountNameTextView;
    private String channel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mbanking);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        TextView titleTextView = (TextView) toolbar.findViewById(R.id.toolbarTitleTextView);
        titleTextView.setText("Pembayaran");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        bankId = getIntent().getIntExtra("bank_id", 0);
        channel = getIntent().getStringExtra("channel_id");

        confirmButton = (LinearLayout) findViewById(R.id.confirmButton);
        reservationIdTextView = (TextView) findViewById(R.id.reservationIdTextView);
        bankNameAndCodeTextView = (TextView) findViewById(R.id.bankNameAndCodeTextView);
        accountNumberTextView = (TextView) findViewById(R.id.accountNumberTextView);
        accountNameTextView = (TextView) findViewById(R.id.accountNameTextView);
        transferAmountTextView = (TextView) findViewById(R.id.transferAmountTextView);
        timerTextView = (TextView) findViewById(R.id.timerTextView);
        datetimePayByTextView = (TextView) findViewById(R.id.datetimePaybyTextView);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MBankingActivity.this, PaymentConfirmationActivity.class);
                intent.putExtra("bank_id", bankId);
                intent.putExtra("reservation_id", reservationId);
                intent.putExtra("price", price);
                startActivity(intent);
            }
        });

        JSONObject reservation = JsonCache.getReservation(this);
        Calendar countedCal = Calendar.getInstance();
        String dateString = "2016-01-01 00:00";
        try {
            dateString = reservation.getString("datetime_pay_by");

            String[] splitted1 = dateString.split(" ");
            String[] splitted = splitted1[0].split("-");
            String monthName = Utils.getMonthName(Integer.valueOf(splitted[1]) - 1).toUpperCase();
            datetimePayByTextView.setText(splitted[2] + " " + monthName + " " + splitted[0] + ", " + splitted1[1] +  " WIB");
            reservationId = reservation.getJSONObject("form_doku").getString("BOOKINGCODE");
            reservationIdTextView.setText(reservationId);

            JSONArray banks = JsonCache.getBanks(this);

            switch (bankId) {
                case 1:
                    accountNumberTextView.setText(banks.getJSONObject(bankId - 1).getString("account"));
                    accountNameTextView.setText(banks.getJSONObject(bankId - 1).getString("owner"));
                    bankNameAndCodeTextView.setText("BNI");
                    break;
                case 2:
                    accountNumberTextView.setText(banks.getJSONObject(bankId - 1).getString("account"));
                    accountNameTextView.setText("Virtual Account");
                    bankNameAndCodeTextView.setText("BANK MANDIRI");
                    break;
                case 3:
                    accountNumberTextView.setText(banks.getJSONObject(0).getString("account"));
                    accountNameTextView.setText(banks.getJSONObject(0).getString("owner"));
                    bankNameAndCodeTextView.setText("BCA");
                    break;
                case 4:
                    accountNumberTextView.setText(banks.getJSONObject(bankId - 1).getString("account"));
                    accountNameTextView.setText("Virtual Account");
                    bankNameAndCodeTextView.setText("BRI");
                    break;
                default:
                    accountNumberTextView.setText(banks.getJSONObject(bankId - 1).getString("account"));
                    accountNameTextView.setText("Virtual Account");
                    bankNameAndCodeTextView.setText("BANK PERMATA");
            }
            price = Integer.parseInt(reservation.getJSONObject("form_doku").getString("PURCHASEAMOUNT"));
            transferAmountTextView.setText(Utils.getMoneyFormat(price, true));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        countingReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                String time = intent.getStringExtra("time");
                if (time != null && channel != null) {
                    if (channel.equals("04") || channel.equals("15")) {
                        String[] splitted = time.split(":");
                        if (Integer.parseInt(splitted[0]) <=2) {
                            time = "3 jam dari sekarang";
                        }
                    }
                    timerTextView.setText(time);
                }
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((countingReceiver),
                new IntentFilter(PaymentTimerService.PAYMENT_COUNTING)
        );
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(countingReceiver);
        super.onStop();
    }
}
