package com.arenatiket.android;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.Window;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.arenatiket.android.api.JsonCache;
import com.arenatiket.android.utils.Constants;
import com.arenatiket.android.utils.Utils;
import com.doku.sdkocov2.DirectSDK;
import com.doku.sdkocov2.interfaces.iPaymentCallback;
import com.doku.sdkocov2.model.LayoutItems;
import com.doku.sdkocov2.model.PaymentItems;

import org.json.JSONException;
import org.json.JSONObject;

public class DokuPaymentActivity2 extends AppCompatActivity {

    private Toolbar toolbar;

    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Utils.logd("message : " + error.toString());
        }
    };
    private String channel = "";
    private DirectSDK directSDK;
    private PaymentItems paymentItems;
    private JSONObject formDoku;
    private JSONObject respongetTokenSDK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_PROGRESS);


        directSDK = new DirectSDK();

        setContentView(R.layout.activity_doku_payment);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        TextView titleTextView = (TextView) toolbar.findViewById(R.id.toolbarTitleTextView);
        titleTextView.setText("Pembayaran");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.READ_PHONE_STATE)
//                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
//                Manifest.permission.READ_PHONE_STATE)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.READ_PHONE_STATE},
//                    1);
//        } else {
//            loadData();
//        }

    }

    private void loadData() {

        channel = getIntent().getStringExtra("channel_id");

        final JSONObject reservation = JsonCache.getReservation(this);
        try {
            JSONObject formDoku = reservation.getJSONObject("form_doku");

            String formWord = "";
            int amount = 0;
            int purchaseAmount = 0;


            switch (channel) {
                case "01":
                    amount = formDoku.getInt("AMOUNT") + formDoku.getInt("doku_pay_cc");
                    purchaseAmount = amount + formDoku.getInt("doku_pay_cc");
                    formWord = formDoku.getString("words_for_doku_cc");
                    break;
                case "04":
                    amount = formDoku.getInt("AMOUNT") + formDoku.getInt("doku_pay_wallet");
                    purchaseAmount = amount + formDoku.getInt("doku_pay_wallet");
                    formWord = formDoku.getString("words_for_doku_wallet");
                    break;
                default:
                    amount = formDoku.getInt("AMOUNT") + formDoku.getInt("doku_pay");
                    purchaseAmount = amount + formDoku.getInt("doku_pay");
                    formWord = formDoku.getString("words_for_doku");
                    break;
            }

            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String unencryptedWord = purchaseAmount + ".00" + Constants.MERCHANT_CODE +
                    getString(R.string.doku_shared_key) + formDoku.getString("TRANSIDMERCHANT") + formDoku.getString("CURRENCY") +
                    telephonyManager.getDeviceId();
            String word = Utils.sha1(unencryptedWord);

            Utils.logd(unencryptedWord);
            Utils.logd(formWord + " ....... " + word);
            Utils.logd("TransIdMerchant " + formDoku.getString("TRANSIDMERCHANT"));
            Utils.logd("BookingCode " + formDoku.getString("BOOKINGCODE"));

            paymentItems = new PaymentItems();
            String dataAmount = purchaseAmount + ".00";
            paymentItems.setDataAmount(dataAmount + "");
            paymentItems.setDataBasket(formDoku.getString("BASKET"));
            paymentItems.setDataCurrency(formDoku.getString("CURRENCY"));
            paymentItems.setDataWords(word);
            paymentItems.setDataMerchantChain(formDoku.getString("CHAINMERCHANT"));
            paymentItems.setDataSessionID(formDoku.getString("SESSIONID"));
            paymentItems.setDataTransactionID(formDoku.getString("TRANSIDMERCHANT"));
            paymentItems.setDataMerchantCode(Constants.MERCHANT_CODE);
            paymentItems.setDataImei(telephonyManager.getDeviceId());
            paymentItems.setMobilePhone(JsonCache.getContact(this).getString("phone"));
            paymentItems.isProduction(false); //set ‘true’ for production and ‘false’ for development
            paymentItems.setPublicKey(getString(R.string.doku_public_key)); //PublicKey can be obtained from the DOKU Back Office
            Utils.logd("channel " + Integer.valueOf(channel));
            directSDK.setCart_details(paymentItems);
            directSDK.setPaymentChannel(Integer.valueOf(channel));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LayoutItems layoutItems = new LayoutItems();
        layoutItems.setToolbarColor(getString(R.string.colorPrimary));
        layoutItems.setToolbarTextColor("#FFFFFF");
        layoutItems.setFontColor(getString(R.string.colorPrimaryDark));
        layoutItems.setBackgroundColor("#eaeaea");
        layoutItems.setLabelTextColor("#9a9a9a");
        layoutItems.setButtonBackground(getResources().getDrawable(R.drawable.bg_btn_primary));
        layoutItems.setButtonTextColor("#FFFFFF");
        directSDK.setLayout(layoutItems);

        directSDK.getResponse(new iPaymentCallback() {
            @Override
            public void onSuccess(final String text) {
                try {
                    respongetTokenSDK = new JSONObject(text);
                    if (respongetTokenSDK.getString("res_response_code").equalsIgnoreCase("0000")) {
                        Utils.logd("respon doku: " + text);
                        try {
                            JSONObject json = new JSONObject(text);
                            AlertDialog.Builder dialog = new AlertDialog.Builder(DokuPaymentActivity2.this);
                            dialog.setMessage(json.getString("res_response_msg"));
                            dialog.setTitle("Error");
                            dialog.setIcon(getResources().getDrawable(android.R.drawable.ic_dialog_alert));
                            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(final String text) {
                Utils.logd("Error: " + text);

                try {
                    JSONObject json = new JSONObject(text);
                    AlertDialog.Builder dialog = new AlertDialog.Builder(DokuPaymentActivity2.this);
                    dialog.setMessage(json.getString("res_response_msg"));
                    dialog.setTitle("Error");
                    dialog.setIcon(getResources().getDrawable(android.R.drawable.ic_dialog_alert));
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onException(Exception eSDK) {
                eSDK.printStackTrace();
            }
        }, getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadData();
                } else {
                    finish();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
