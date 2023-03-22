package com.arenatiket.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
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

public class NicepayActivity extends AppCompatActivity {

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
    private RelativeLayout atmButton, mBankingButton, internetBankingButton;
    private LinearLayout atmLayout, mBankingLayout, internetBankingLayout;
    private ImageView atmArrowImageView, mobileBankingArrowImageView, internetBankingArrowImageView;
    private TextView atmVaccTextView, mBankingVaccTextView, internetBankingVaccTextView;
    private boolean expired = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nicepay);

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

        atmButton = (RelativeLayout) findViewById(R.id.atmButton);
        mBankingButton = (RelativeLayout) findViewById(R.id.mobileBankingButton);
        internetBankingButton = (RelativeLayout) findViewById(R.id.internetBankingButton);

        atmLayout = (LinearLayout) findViewById(R.id.atmLayout);
        mBankingLayout = (LinearLayout) findViewById(R.id.mBankingLayout);
        internetBankingLayout = (LinearLayout) findViewById(R.id.internetBankingLayout);

        atmArrowImageView = (ImageView) findViewById(R.id.atmArrowImageView);
        mobileBankingArrowImageView = (ImageView) findViewById(R.id.mobileBankingArrowImageView);
        internetBankingArrowImageView = (ImageView) findViewById(R.id.internetBankingArrowImageView);

        atmVaccTextView = (TextView) findViewById(R.id.atmVaccTextView);
        mBankingVaccTextView = (TextView) findViewById(R.id.mBankingVaccTextView);
        internetBankingVaccTextView = (TextView) findViewById(R.id.internetBankingVaccTextView);

        atmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBankingLayout.setVisibility(View.GONE);
                internetBankingLayout.setVisibility(View.GONE);
                atmLayout.setVisibility(View.VISIBLE);

                mobileBankingArrowImageView.setImageResource(R.drawable.arrow_down);
                internetBankingArrowImageView.setImageResource(R.drawable.arrow_down);
                atmArrowImageView.setImageResource(R.drawable.arrow_up);
            }
        });
        mBankingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBankingLayout.setVisibility(View.VISIBLE);
                internetBankingLayout.setVisibility(View.GONE);
                atmLayout.setVisibility(View.GONE);

                mobileBankingArrowImageView.setImageResource(R.drawable.arrow_up);
                internetBankingArrowImageView.setImageResource(R.drawable.arrow_down);
                atmArrowImageView.setImageResource(R.drawable.arrow_down);
            }
        });
        internetBankingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBankingLayout.setVisibility(View.GONE);
                internetBankingLayout.setVisibility(View.VISIBLE);
                atmLayout.setVisibility(View.GONE);

                mobileBankingArrowImageView.setImageResource(R.drawable.arrow_down);
                internetBankingArrowImageView.setImageResource(R.drawable.arrow_up);
                atmArrowImageView.setImageResource(R.drawable.arrow_down);
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptGoToHome();
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
            datetimePayByTextView.setText(splitted[2] + " " + monthName + " " + splitted[0] + ", " + splitted1[1] + " WIB");
            reservationId = reservation.getJSONObject("form_doku").getString("BOOKINGCODE");
            reservationIdTextView.setText(reservationId);

            JSONArray banks = JsonCache.getBanks(this);

            JSONObject nicepay = JsonCache.getNicepayResponse(this);

            mBankingVaccTextView.setText(nicepay.getString("vacc"));
            atmVaccTextView.setText(nicepay.getString("vacc"));
            internetBankingVaccTextView.setText(nicepay.getString("vacc"));


            switch (bankId) {

                case 1:
                    accountNumberTextView.setText(nicepay.getString("vacc"));
                    accountNameTextView.setText("Virtual Account");
                    bankNameAndCodeTextView.setText("BNI");
                    break;
                case 2:
                    accountNumberTextView.setText(nicepay.getString("vacc"));
                    accountNameTextView.setText("Virtual Account");
                    bankNameAndCodeTextView.setText("BANK MANDIRI");
                    break;
                case 3:
                    accountNumberTextView.setText(nicepay.getString("vacc"));
                    accountNameTextView.setText("Virtual Account");
                    bankNameAndCodeTextView.setText("BCA");
                    break;
                case 4:
                    accountNumberTextView.setText(nicepay.getString("vacc"));
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

        timerTextView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (expired) {
                    timerTextView.setTextColor(Color.RED);
                    timerTextView.setText("Expired");
                }
            }
        }, 1000);

        countingReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                String time = intent.getStringExtra("time");
                Utils.logd("time== " + time);
                expired = false;
                if (time != null && channel != null) {
                    if (channel.equals("04") || channel.equals("15")) {
                        String[] splitted = time.split(":");
                        if (Integer.parseInt(splitted[0]) <= 2) {
                            time = "3 jam dari sekarang";
                        }
                    }
//                    if (time.equals("Expired")) {
//                        timerTextView.setTextColor(getResources().getColor(R.color.colorPrimaryRed));
//                    }
                    timerTextView.setTextColor(getResources().getColor(R.color.colorPrimaryYellow));
                    timerTextView.setText(time);
                }
            }
        };

    }

    private void promptGoToHome() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Konfirmasi");
        builder.setMessage("Apakah Anda yakin akan kembali ke halaman booking?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(NicepayActivity.this, SearchActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
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
