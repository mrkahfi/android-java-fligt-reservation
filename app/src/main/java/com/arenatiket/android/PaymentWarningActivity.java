package com.arenatiket.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class PaymentWarningActivity extends AppCompatActivity {

    private LinearLayout prosesButton;
    private int bankId;
    private String bank;
    private String channel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_warning);

        bankId = getIntent().getIntExtra("bank_id", 1);
        bank = getIntent().getStringExtra("bank");
        channel = getIntent().getStringExtra("channel_id");


        prosesButton = (LinearLayout) findViewById(R.id.prosesButton);
        prosesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentWarningActivity.this, MBankingActivity.class);
                intent.putExtra("bank_id", bankId);
                intent.putExtra("bank", bank);
                intent.putExtra("channel_id", channel);
                startActivity(intent);
            }
        });
    }
}
