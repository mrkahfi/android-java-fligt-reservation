package com.arenatiket.android;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.arenatiket.android.api.JsonCache;
import com.arenatiket.android.service.PaymentTimerService;
import com.arenatiket.android.utils.Constants;
import com.arenatiket.android.utils.Utils;
import com.doku.sdkocov2.DirectSDK;
import com.doku.sdkocov2.interfaces.iPaymentCallback;
import com.doku.sdkocov2.model.PaymentItems;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PaymentMenuActivity2 extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private LinearLayout bcaMBankingButton;
    private LinearLayout mandiriKlikpayButton;
    private TextView countDownTextView;
    private BroadcastReceiver countingReceiver;
    private LinearLayout mandiriMBankingButton;
    private LinearLayout briMBankingButton;
    private LinearLayout bniMBankingButton;
    private LinearLayout dokuKlikPayButton;
    private LinearLayout briKlikPayButton;
    private LinearLayout atmBersamaButton;
    private LinearLayout alfamartButton;
    private LinearLayout ccButton;
    private TextView hargaDiskon1TextView;
    private TextView hargaDiskon2TextView;
    private String hargaDiskon;
    private JSONObject formDoku;
    private String invoiceNumber;
    private DirectSDK directSDK;
    private String channel = "01";
    private JSONObject respongetTokenSDK = new JSONObject();
    private String jsonRespon;
    private RequestQueue requestQueue;

    int REQUEST_CODE_MANDIRI = 3;
    int REQUEST_CODE_VIRTUALACCOUNT = 4;
    TelephonyManager telephonyManager;
    String responseToken, Challenge1, Challenge2, Challenge3, debitCard;
    int PayChanChoosed = 0;
    private static final int REQUEST_PHONE = 1;
    private static String[] PERMISSION_PHONE = {Manifest.permission.READ_PHONE_STATE};
    String tokenPayment = null;
    String customerID = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_menu);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        TextView titleTextView = (TextView) toolbar.findViewById(R.id.toolbarTitleTextView);
        titleTextView.setText("Pembayaran");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        bcaMBankingButton = (LinearLayout) findViewById(R.id.bcaMBankingButton);
        mandiriMBankingButton = (LinearLayout) findViewById(R.id.mandiriMBankingButton);
        briMBankingButton = (LinearLayout) findViewById(R.id.briMBankingButton);
        bniMBankingButton = (LinearLayout) findViewById(R.id.bniMBankingButton);
        mandiriKlikpayButton = (LinearLayout) findViewById(R.id.mandiriKlikpayButton);
        dokuKlikPayButton = (LinearLayout) findViewById(R.id.dokuKlikPayButton);
        ccButton = (LinearLayout) findViewById(R.id.ccButton);
        briKlikPayButton = (LinearLayout) findViewById(R.id.briKlikPayButton);
        atmBersamaButton = (LinearLayout) findViewById(R.id.atmBersamaButton);
        alfamartButton = (LinearLayout) findViewById(R.id.alfamartButton);
        countDownTextView = (TextView) findViewById(R.id.countDownTextView);

        hargaDiskon1TextView = (TextView) findViewById(R.id.hargaDiskon1TextView);
        hargaDiskon2TextView = (TextView) findViewById(R.id.hargaDiskon2TextView);

        initEventListener();

        JSONObject reservation = JsonCache.getReservation(this);
        Calendar countedCal = Calendar.getInstance();
        String dateString = "2016-01-01 00:00";
        try {
            dateString = reservation.getString("datetime_pay_by");
            Utils.logd("datetime pay by " + dateString);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            countedCal.setTime(sdf.parse(dateString));


            hargaDiskon = reservation.getJSONObject("form_doku").getInt("PURCHASEAMOUNT") + "";
            String hargaDiskon1 = Utils.getMoneyFormat(Integer.parseInt(hargaDiskon.substring(0,
                    hargaDiskon.length() - 3)), false);
            String hargaDiskon2 = hargaDiskon.substring(hargaDiskon.length() - 3);
            hargaDiskon1TextView.setText(hargaDiskon1);
            hargaDiskon2TextView.setText("." + hargaDiskon2);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Utils.longLog(reservation.toString());

        Calendar currentCal = Calendar.getInstance();

        long diff = countedCal.getTimeInMillis() - TimeUnit.MINUTES.toMillis(20) - currentCal
                .getTimeInMillis();

        Intent timerIntent = new Intent(this, PaymentTimerService.class);
        timerIntent.putExtra("diff_millis", diff);
        startService(timerIntent);

        Utils.logd("starting timer diff " + diff);


        countingReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                String time = intent.getStringExtra("time");
                if (time != null) {
                    countDownTextView.setText(time);
                }
            }
        };
    }

    private void initEventListener() {
        bcaMBankingButton.setOnClickListener((View.OnClickListener) this);
        mandiriMBankingButton.setOnClickListener((View.OnClickListener) this);
        briMBankingButton.setOnClickListener((View.OnClickListener) this);
        bniMBankingButton.setOnClickListener((View.OnClickListener) this);
        mandiriKlikpayButton.setOnClickListener((View.OnClickListener) this);
        dokuKlikPayButton.setOnClickListener((View.OnClickListener) this);
        briKlikPayButton.setOnClickListener((View.OnClickListener) this);
        ccButton.setOnClickListener((View.OnClickListener) this);
        atmBersamaButton.setOnClickListener((View.OnClickListener) this);
        alfamartButton.setOnClickListener((View.OnClickListener) this);
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

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, PaymentWarningActivity.class);
        intent.putExtra("channel_id", "00");
        if (v.getId() == bcaMBankingButton.getId()) {
            intent.putExtra("bank", "bca");
            intent.putExtra("bank_id", 3);
            intent.putExtra("channel_id", "00");
            startActivity(intent);
        } else if (v.getId() == mandiriMBankingButton.getId()) {
            intent.putExtra("bank", "mandiri");
            intent.putExtra("bank_id", 2);
            intent.putExtra("channel_id", "00");
            startActivity(intent);
        } else if (v.getId() == briMBankingButton.getId()) {
            intent.putExtra("bank", "bri");
            intent.putExtra("bank_id", 4);
            intent.putExtra("channel_id", "00");
            startActivity(intent);
        } else if (v.getId() == bniMBankingButton.getId()) {
            intent.putExtra("bank", "bni");
            intent.putExtra("bank_id", 1);
            intent.putExtra("channel_id", "00");
            startActivity(intent);
        } else if (v.getId() == mandiriKlikpayButton.getId()) {
//            intent = new Intent(this, DokuPaymentActivity.class);
//            intent.putExtra("method", "mandiri");
//            intent.putExtra("channel_id", "02");
//            startActivity(intent);

            Intent i = new Intent(this, MandiriClickPay.class);
            startActivityForResult(i, REQUEST_CODE_MANDIRI);
        } else if (v.getId() == ccButton.getId()) {
//            intent = new Intent(this, DokuPaymentActivity.class);
//            intent.putExtra("method", "cc");
//            intent.putExtra("channel_id", "1");
            if (ActivityCompat.checkSelfPermission(this, String.valueOf(PERMISSION_PHONE))
                    != PackageManager.PERMISSION_GRANTED) {
                getPermissionFirst(1);
            } else {
                try {
                    connectSDK(1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else if (v.getId() == dokuKlikPayButton.getId()) {
//            intent = new Intent(this, DokuPaymentActivity.class);
//            intent.putExtra("method", "doku");
//            intent.putExtra("channel_id", "4");
//            startActivity(intent);
            if (ActivityCompat.checkSelfPermission(this, String.valueOf(PERMISSION_PHONE))
                    != PackageManager.PERMISSION_GRANTED) {
                getPermissionFirst(2);
            } else {
                try {
                    connectSDK(2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            startActivity(intent);
        } else if (v.getId() == briKlikPayButton.getId()) {
            intent = new Intent(this, DokuPaymentActivity.class);
            intent.putExtra("method", "bri");
            intent.putExtra("channel_id", "06");
            startActivity(intent);
        } else if (v.getId() == atmBersamaButton.getId()) {
            intent = new Intent(this, MBankingActivity.class);
            intent.putExtra("method", "atm_bersama");
            intent.putExtra("channel_id", "05");
            startActivity(intent);
        } else if (v.getId() == alfamartButton.getId()) {
            intent = new Intent(this, MBankingActivity.class);
            intent.putExtra("method", "alfamart");
            intent.putExtra("channel_id", "14");
            startActivity(intent);
        }
    }


    private void connectSDK(int menuPaymentChannel) throws JSONException {

        requestQueue = Volley.newRequestQueue(this);

        JSONObject reservation = JsonCache.getReservation(this);

        formDoku = reservation.getJSONObject("form_doku");
        //set payment parameter
        invoiceNumber = formDoku.getString("AD-BRQFO-189108");

        directSDK = new DirectSDK();

        PaymentItems cardDetails = null;
        cardDetails = InputCard();
        directSDK.setCart_details(cardDetails);

        directSDK.setPaymentChannel(menuPaymentChannel);
        directSDK.getResponse(new iPaymentCallback() {

            @Override
            public void onSuccess(final String text) {
                Log.d("RESPONSE", text);
                try {
                    respongetTokenSDK = new JSONObject(text);

                    if (respongetTokenSDK.getString("res_response_code").equalsIgnoreCase("0000")) {
                        jsonRespon = text;
                        requestPayment();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(final String text) {
                Log.d("onError", text);
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onException(Exception eSDK) {
                eSDK.printStackTrace();
            }
        }, getApplicationContext());
    }

    private void requestPayment() {
        String url = Constants.URL_CHARGING_DOKU_DAN_CC;
        JsonObjectRequest requestPayment = new JsonObjectRequest(Request.Method.POST, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject json) {

                if (json != null) {
                    try {

                        if (json.getString("res_response_code").equalsIgnoreCase("0000") && json != null) {
                            Intent intent = new Intent(getApplicationContext(), ConfirmationSuccessActivity.class);
                            intent.putExtra("data", json.toString());
                            startActivity(intent);
                            finish();
                            Toast.makeText(getApplicationContext(), " PAYMENT SUKSES", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(getApplicationContext(), ConfirmationSuccessActivity.class);
                            intent.putExtra("data", json.toString());
                            startActivity(intent);
                            finish();
                            Toast.makeText(getApplicationContext(), "PAYMENT ERROR", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("data", jsonRespon);
                return params;
            }
        };

        requestQueue.add(requestPayment);
    }

    private void mandiriPayment() {
        JSONObject jGroup = new JSONObject();// /main Object
        try {

            invoiceNumber = String.valueOf(formDoku.getString("TRANSIDMERCHANT"));

            jGroup.put("req_transaction_id", invoiceNumber);
            jGroup.put("req_payment_channel", "02");
            jGroup.put("req_card_number", debitCard);
            jGroup.put("req_device_id", telephonyManager.getDeviceId());
            jGroup.put("req_challenge_code_1", Challenge1);
            jGroup.put("req_challenge_code_2", Challenge2);
            jGroup.put("req_challenge_code_3", Challenge3);
            jGroup.put("req_response_token", responseToken);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = Constants.URL_CHARGING_MANDIRI_CLICKPAY;
        JsonObjectRequest requestPayment = new JsonObjectRequest(Request.Method.POST, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject json) {

                if (json != null) {
                    try {
                        if (json.getString("res_response_code").equalsIgnoreCase("0000") && json != null) {
                            Intent intent = new Intent(getApplicationContext(), ConfirmationSuccessActivity.class);
                            intent.putExtra("data", json.toString());
                            startActivity(intent);
                            finish();
                            Toast.makeText(getApplicationContext(), " PAYMENT SUKSES", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(getApplicationContext(), ConfirmationSuccessActivity.class);
                            intent.putExtra("data", json.toString());
                            startActivity(intent);
                            finish();
                            Toast.makeText(getApplicationContext(), "PAYMENT ERROR", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(requestPayment);
    }

    private PaymentItems InputCard() throws JSONException {

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

        PaymentItems paymentItems = new PaymentItems();

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
        return paymentItems;
    }


    private class RequestPayment extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PaymentMenuActivity2.this);
            pDialog.setMessage("Mohon Tunggu ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected JSONObject doInBackground(String... args) {
            JSONObject defResp = null;

            return defResp;
        }

        @Override
        protected void onPostExecute(JSONObject json) {

            pDialog.dismiss();

        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void getPermissionFirst(int paymentChanel) {
        PayChanChoosed = paymentChanel;
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_PHONE_STATE)) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    REQUEST_PHONE);

        } else {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE},
                    REQUEST_PHONE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PHONE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (PayChanChoosed != 0) {
                    if (PayChanChoosed == 1 || PayChanChoosed == 2) {

                        try {
                            if (tokenPayment != null) {
                                connectSecondPay(PayChanChoosed);
                            } else if (customerID != null) {
                                connectFirstPay(PayChanChoosed);
                            } else {
                                connectSDK(PayChanChoosed);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else if (PayChanChoosed == 3) {
                        mandiriPayment();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "PAYCHAN ERROR", Toast.LENGTH_SHORT);
                }
            } else {
                Toast.makeText(getApplicationContext(), "Permission Failed", Toast.LENGTH_SHORT);
            }
        }
    }


    private void connectSecondPay(int menuPaymentChannel) throws JSONException {


        //set payment parameter
        invoiceNumber = String.valueOf(formDoku.getString("TRANSIDMERCHANT"));
        customerID = String.valueOf(formDoku.getString("TRANSIDMERCHANT"));

        directSDK = new DirectSDK();

        PaymentItems paymentItems = new PaymentItems();

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

        paymentItems = new PaymentItems();

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
        // paymentItems.isProduction(false);
        paymentItems.setCustomerID(customerID);

        paymentItems.setTokenPayment(tokenPayment);
        directSDK.setCart_details(paymentItems);

        //set layout parameter
//        LayoutItems layoutItems = null;
//        layoutItems = setLayout();
//        directSDK.setLayout(layoutItems);

        directSDK.setPaymentChannel(menuPaymentChannel);
        directSDK.getResponse(new iPaymentCallback() {

            @Override
            public void onSuccess(final String text) {

                try {
                    respongetTokenSDK = new JSONObject(text);

                    if (respongetTokenSDK.getString("res_response_code").equalsIgnoreCase("0000")) {
                        jsonRespon = text;
                        new RequestPayment().execute();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(final String text) {
                Log.d("onError", text);
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onException(Exception eSDK) {
                eSDK.printStackTrace();
            }
        }, getApplicationContext());

        tokenPayment = null;
        customerID = null;
    }

    private void connectFirstPay(int menuPaymentChannel) throws JSONException {

        //set payment parameter
        invoiceNumber = String.valueOf(formDoku.getString("TRANSIDMERCHANT"));
        customerID = String.valueOf(formDoku.getString("TRANSIDMERCHANT"));

        directSDK = new DirectSDK();

        PaymentItems paymentItems = new PaymentItems();

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

        paymentItems = new PaymentItems();

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
        // paymentItems.isProduction(false);
        paymentItems.setCustomerID(customerID);
        directSDK.setCart_details(paymentItems);


        //set layout parameter
//        LayoutItems layoutItems = null;
//        layoutItems = setLayout();
//        directSDK.setLayout(layoutItems);

        directSDK.setPaymentChannel(menuPaymentChannel);
        directSDK.getResponse(new iPaymentCallback() {

            @Override
            public void onSuccess(final String text) {

                try {
                    respongetTokenSDK = new JSONObject(text);

                    if (respongetTokenSDK.getString("res_response_code").equalsIgnoreCase("0000")) {
                        jsonRespon = text;
                        new RequestPayment().execute();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(final String text) {
                Log.d("onError", text);
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onException(Exception eSDK) {
                eSDK.printStackTrace();
            }
        }, getApplicationContext());


        tokenPayment = null;
        customerID = null;
    }
}
