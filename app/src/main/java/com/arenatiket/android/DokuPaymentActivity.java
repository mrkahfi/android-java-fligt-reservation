package com.arenatiket.android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.arenatiket.android.api.JsonCache;
import com.arenatiket.android.utils.Constants;
import com.arenatiket.android.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DokuPaymentActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private WebView webView;
    private String url = "http://arenatiket.com/android/doku_request?accesskey=780b4191985eab0fa83b6bf3adbaaf52";
    private int totalDiscount;
    private int totalFare;
    private int totalAmount;
    private String totalIdMerchant = "0";


    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Utils.logd("message : " + error.toString());
        }
    };
    private String channel = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_PROGRESS);

        setContentView(R.layout.activity_doku_payment);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        TextView titleTextView = (TextView) toolbar.findViewById(R.id.toolbarTitleTextView);
        titleTextView.setText("Pembayaran");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        channel = getIntent().getStringExtra("channel_id");

        JSONObject reservation = JsonCache.getReservation(this);

        Utils.logd(reservation.toString());
        try {
            JSONObject formDoku = reservation.getJSONObject("form_doku");
            totalDiscount = formDoku.getInt("arena_total_discount");
            totalFare = formDoku.getInt("arena_total_fare");
            totalAmount = formDoku.getInt("AMOUNT");
            totalIdMerchant = formDoku.getString("TRANSIDMERCHANT");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        webView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

//        webView.setWebChromeClient(new WebChromeClient() {
//            public void onProgressChanged(WebView view, int progress) {
//                DokuPaymentActivity.this.setProgress(progress * 1000);
//            }
//        });
//
//        webView.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return false;
//            }
//        });

        url = url + "&paymentchannel="+channel+"&total_discount=" + totalDiscount + "&total_fare=" + totalFare + "&totalamount="
                + totalAmount + "&transidmerchant=" + totalIdMerchant;
        webView.loadUrl(url);


        Utils.logd("url " + url);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String respon) {
                Utils.longLog("url response : " + respon);
                try {
                    JSONObject response = new JSONObject(respon);
                    if (response.getInt("status") == 1) {

                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                String creds = String.format("%s:%s", "trial", "fauzan");
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("accesskey", "780b4191985eab0fa83b6bf3adbaaf52");
                params.put("deviceid", "");
                params.put("doku-pairing-code", "");
                params.put("doku-invoice-no", "");
                params.put("amount", "");
                params.put("basket_name", "");
                params.put("quantity", "");
                params.put("subtotal", "");
                params.put("customer_name", "");
                params.put("customer_phone", "");
                params.put("customer_email", "");
                params.put("customer_address", "");

                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                Constants.MY_SOCKET_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(request);

    }
}