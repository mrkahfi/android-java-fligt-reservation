package com.arenatiket.android;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.RelativeLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.arenatiket.android.api.JsonCache;
import com.arenatiket.android.model.Contact;
import com.arenatiket.android.model.Passanger;
import com.arenatiket.android.model.Ticket;
import com.arenatiket.android.utils.Constants;
import com.arenatiket.android.utils.MyApplication;
import com.arenatiket.android.utils.Utils;
import com.arenatiket.android.utils.VolleyToolboxExtension;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import at.grabner.circleprogress.AnimationState;
import at.grabner.circleprogress.AnimationStateChangedListener;
import at.grabner.circleprogress.CircleProgressView;

public class BookingProgressActivity extends AppCompatActivity {

    private JsonObjectRequest request;
    private String url = Constants.baseApiUrl + "android/booking?accesskey=780b4191985eab0fa83b6bf3adbaaf52" +
            "&client=arenatiket";

    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Utils.logd("message : " + error.toString());
            String body = "";
            //get status code here
            String statusCode = String.valueOf(error.networkResponse.statusCode);
            //get response body and parse with appropriate encoding
//            if(error.networkResponse.data!=null) {
            try {
                body = new String(error.networkResponse.data, "UTF-8");
                Utils.logd("message : " + body);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
//            }
        }
    };

    private JSONObject resultObj;
    private double totalRate;
    private int requestCounter = 1;

    private boolean error;

    private int percentage;
    private boolean allSuccess;
    protected Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            resultObj = response;
            totalRate = 0d;
            Utils.longLog("booking response : " + response.toString());
            url = Constants.baseApiUrl + "android/booking?accesskey=780b4191985eab0fa83b6bf3adbaaf52" +
                    "&client=arenatiket";
            if (requestCounter == 1) {
                mCircleView.setValueAnimated(10);
            }
            try {
                if (response.getJSONArray("errors").length() > 0) {
                    error = true;
                    String message = response.getJSONArray("errors").getJSONObject(0).getString("message");
                    showAlert(message);
                } else {
                    error = false;
                }
                if (!response.isNull("results")) {
                    JSONObject results = response.getJSONObject("results");
                    if (Integer.parseInt(response.getString("status")) == 1 || Integer.parseInt(response.getString("status")) == 4) {
                        String trackId = results.getString("track_id");
                        Long trackTime = results.getLong("track_time");
                        Utils.logd("trackId " + trackId + ", trackTime " + trackTime);
                        JSONArray progresses = results.getJSONArray("progress");
//                    url = url.replaceAll("[&?]track_id.*?(?=&|\\?|$)", "");
//                    url = url.replaceAll("[&?]track_time.*?(?=&|\\?|$)", "");
//                    url = url.replaceAll("[&?]progress.*?(?=&|\\?|$)", "");
//                    url = url.replaceAll("[&?]confirm_verified.*?(?=&|\\?|$)", "");
                        allSuccess = true;
                        JSONArray progressesRequest = new JSONArray();
                        for (int i = 0; i < progresses.length(); i++) {
                            final JSONObject progress = progresses.getJSONObject(i);

                            if (!progress.isNull("data")) {
                                if (!progress.getJSONObject("data").isNull("errors")) {
                                    if (progress.getJSONObject("data").getJSONArray("errors").length() > 0) {
                                        error = true;
                                        String message = progress.getJSONObject("data").getJSONArray("errors").getJSONObject(0).getString("message");
                                        showAlert(message);
                                    } else {
                                        error = false;
                                    }
                                }
                            }
                            float ratePerProgress = (progress.getInt("numerator") / progress.getInt("denominator")) * 100f;
//                        Utils.logd("ratePerProgress: " + ratePerProgress);

                            totalRate = totalRate + (int) ratePerProgress;
                            Utils.logd("totalRate: " + totalRate);
                            percentage = (int) (totalRate / ((double) progresses.length()) * 100f);
                            Utils.logd("percentage: " + percentage);

                            mCircleView.setValueAnimated(ratePerProgress);

                            if (progress.getInt("state") == 1) {
                                allSuccess = false;
                                progressesRequest.put(progress);
                            } else if (progress.getInt("state") == 4) {
                                allSuccess = false;
                            }

                        }


                        if (progressesRequest.length() > 0) {
                            Ticket departTicket = MyApplication.selectedDepartItem;
                            String goChoose = departTicket.getValues().get("choose");
                            goChoose = goChoose.replace(":", "%3A");
//                            url = url + "&go_choose=" + goChoose;

                            String backChoose = "";
                            if (MyApplication.selectedReturnItem != null) {
                                Ticket backTicket = MyApplication.selectedReturnItem;
                                backChoose = backTicket.getValues().get("choose");
                                backChoose = backChoose.replace(":", "%3A");
                            }
//                            url = url + "&back_choose=" + backChoose;

                            url = url.replace(" ", "+");
                            url = url.replace("[", "%5B");
                            url = url.replace("]", "%5D");
                            url = url.replace(";", "%3B");
                            url = url.replace(",", "%2C");
                            url = url.replace("@", "%40");

                            url = url + "&track_id=" + trackId + "&track_time=" + trackTime
                                    + "&confirm_verified=true";
//                        try {
//                            url = url + "&from_country=" + fromCountry + "&to_country=" + toCountry + "&go_airline_code="
//                                    + MyApplication.selectedDepartItem.getFlights().getJSONObject(0).getString("airlines")
//                                    + "&back_airline_code=" + (MyApplication.selectedReturnItem != null ?
//                                    MyApplication.selectedReturnItem.getFlights().getJSONObject(0).getString("airlines") : "")
//                                    + "&adult_count=" + adultCount + "&child_count=" + childCount + "&infant_count=" + infantCount
//                                    + "&from_code=" + fromCode + "&to_code=" + toCode + "&date_go=" + goDateText + "&is_return=" + isReturn
//                                    + "&date_back=" + backDateText;
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }

                            url = url + "&progress=" + progressesRequest.toString();

                            Utils.logd("urlnext: " + url);

                            request = new JsonObjectRequest(Request.Method.GET, url, responseListener, errorListener) {
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
                                    return params;
                                }

                                @Override
                                protected VolleyError parseNetworkError(VolleyError volleyError) {
                                    if (volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
                                        VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
                                        volleyError = error;
                                    }

                                    return volleyError;
                                }
                            };

                            request.setRetryPolicy(new DefaultRetryPolicy(
                                    0,
                                    5,
                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (BookingProgressActivity.this != null) {
                                        queue.add(request);
                                    }
                                }
                            }, 100);
                        } else {
                        }

                        if (progresses.length() > 0 && allSuccess) {
//                    if (percentage >= 100) {
//                        if (!error) {
                            JsonCache.saveReservation(BookingProgressActivity.this, results.toString());
                            Intent intent = new Intent(BookingProgressActivity.this, PaymentMenuActivity.class);
                            intent.putExtra("searchParams", bundle);
                            startActivity(intent);
                            BookingProgressActivity.this.finish();
//                        }
                        }
                    } else if (response.getString("status").equals("99")) {
                        JsonCache.saveReservation(BookingProgressActivity.this, results.toString());
                        Intent intent = new Intent(BookingProgressActivity.this, PaymentMenuActivity.class);
                        intent.putExtra("searchParams", bundle);
                        startActivity(intent);
                        BookingProgressActivity.this.finish();
                    }
                } else {
                    allSuccess = false;
                }
            } catch (JSONException e)

            {
                e.printStackTrace();
            }
        }
    };
    private RelativeLayout circleViewLayout;
    private RequestQueue queue;
    private Bundle bundle;

    private void showAlert(String message) {
        circleViewLayout.setVisibility(View.GONE);
        mCircleView.setVisibility(View.GONE);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        alertDialogBuilder.setTitle("Kegagalan");
        alertDialogBuilder
                .setMessage(message)
                .setIcon(getResources().getDrawable(android.R.drawable.ic_dialog_alert))
                .setCancelable(false)
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        BookingProgressActivity.this.finish();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private CircleProgressView mCircleView;
    private int animationDuration;
    private String isReturn;
    private String fromCountry;
    private String toCountry;
    private String adultCount;
    private String childCount;
    private String infantCount;
    private String fromCode;
    private String toCode;
    private String backDateText;
    private String goDateText;
//    private ArrayList<String> goDates;ee

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_progress);
        mCircleView = (CircleProgressView) findViewById(R.id.circleView);
        circleViewLayout = (RelativeLayout) findViewById(R.id.circleViewLayout);

        final Contact contact = (Contact) getIntent().getSerializableExtra("contact_person");
        bundle = getIntent().getBundleExtra("searchParams");
        String goDatesText = bundle.getString("goDatesText");
        fromCode = bundle.getString("fromCode");
        isReturn = bundle.getString("isReturn");
        toCode = bundle.getString("toCode");
        goDateText = bundle.getString("goDate");

        queue = VolleyToolboxExtension.newRequestQueue(this, new HurlStack(null, createSslSocketFactory()));

        JSONArray flights = MyApplication.selectedDepartItem.getFlights();

        String departDateText = null;
        try {
            departDateText = flights.getJSONObject(0).getString("depart_datetime");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String[] splitted1 = departDateText.split(" ");
        goDateText = splitted1[0];

        backDateText = "";
        if (isReturn.equals("1")) {
            backDateText = bundle.getString("backDate");
        }
        fromCountry = bundle.getString("fromCountry");
        toCountry = bundle.getString("toCountry");
        adultCount = bundle.getString("adultCount");
        childCount = bundle.getString("childCount");
        infantCount = bundle.getString("infantCount");

        String contactName = contact.getName();
        String[] splittedContactName = contactName.split(" ");
        final String contactFistName = splittedContactName[0];

        JsonCache.addToContacts(this, JsonCache.getContact(this));

        Ticket departTicket = MyApplication.selectedDepartItem;
        String goChoose = departTicket.getValues().get("choose");
        goChoose = goChoose.replace(":", "%3A");
        url = url + "&go_choose=" + goChoose;
        try {
            url = url + "&go_fare=" + URLEncoder.encode(MyApplication.selectedDepartItem.getFares().toString(), "UTF-8");
            url = url + "&go_route=" + URLEncoder.encode(MyApplication.selectedDepartItem.getFlights().toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String backChoose = "";
        if (MyApplication.selectedReturnItem != null) {
            Ticket backTicket = MyApplication.selectedReturnItem;
            backChoose = backTicket.getValues().get("choose");
            backChoose = backChoose.replace(":", "%3A");
            JSONArray backFlights = backTicket.getFlights();

            departDateText = null;
            try {
                url = url + "&back_fare=" + URLEncoder.encode(MyApplication.selectedReturnItem.getFares().toString(), "UTF-8");
                url = url + "&back_route=" + URLEncoder.encode(MyApplication.selectedReturnItem.getFlights().toString(), "UTF-8");
                departDateText = backFlights.getJSONObject(0).getString("depart_datetime");
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            splitted1 = departDateText.split(" ");
            backDateText = splitted1[0];

        }
        url = url + "&back_choose=" + backChoose;
        Utils.logd("pageIndex " + MyApplication.selectedGoIndex);
        url = url + "&track_id=" + MyApplication.trackIds.get(MyApplication.selectedGoIndex);
        url = url + "&track_time=" + MyApplication.trackTimes.get(MyApplication.selectedGoIndex);

        final String finalBackChoose = backChoose;
        String contactLastName = "";
        if (splittedContactName.length > 1) {
            for (int i = 1; i < splittedContactName.length; i++) {
                String name = splittedContactName[i];
                if (i == 1) {
                    contactLastName = name;
                } else {
                    contactLastName = contactLastName + " " + name;
                }
                i++;
            }
        } else {
            contactLastName = contactFistName;
        }

        url = url + "&contact_email=" + contact.getEmail();

        url = url + "&contact_first_name=" + contactFistName;
        url = url + "&contact_last_name=" + contactLastName;
        url = url + "&contact_phone=" + contact.getPhone();
        url = url + "&contact_title_id=" + contact.getTitle();

        for (int i = 0; i < MyApplication.passangers.size(); i++) {
            Passanger passanger = MyApplication.passangers.get(i);
            String[] splittedName = passanger.getName().split(" ");
            final String fistName = splittedName[0];
            String lastName = "";
            if (splittedName.length > 1) {
                for (int j = 1; j < splittedName.length; j++) {
                    String name = splittedName[j];
                    if (j == 1) {
                        lastName = name;
                    } else {
                        lastName = lastName + " " + name;
                    }
                }
            } else {
                lastName = fistName;
            }

            String dob = passanger.getBirthDate();
            Utils.logd("dob : " + dob);
            if (dob.contains("-")) {
                String[] splittedDob = passanger.getBirthDate().split("-");
                dob = splittedDob[2] + "-" + (splittedDob[1].length() >= 1 ? "0" + splittedDob[1].length() : splittedDob[1].length())
                        + "-" + (splittedDob[0].length() >= 1 ? "0" + splittedDob[0].length() : splittedDob[0].length());
            }

            url = url + "&gender_ids[" + i + "]=" + passanger.getGenderId();
            url = url + "&first_names[" + i + "]=" + fistName;
            url = url + "&last_names[" + i + "]=" + lastName;
            url = url + "&date_of_births[" + i + "]=" + dob;
            url = url + "&id_card_num[" + i + "]=" + passanger.getIdCardNum();
            url = url + "&ifant_assoc_traveller_ctr[" + i + "]=" + passanger.getIfantAssocTravellerCtr();
            url = url + "&traveller_title_ids[" + i + "]=" + passanger.getTitle();
            url = url + "&nationality_ids[" + i + "]=" + passanger.getNationality();

            if (MyApplication.isInternationalFlight) {
                String passportDate = passanger.getPassportDate();
                String[] splittedPassportDate = passanger.getPassportDate().split("-");
                if (splittedPassportDate.length > 2) {
                    passportDate = splittedPassportDate[2] + "-"
                            + (splittedPassportDate[1].length() >= 1 ? "0" + splittedPassportDate[1].length() : splittedPassportDate[1].length())
                            + "-" + (splittedPassportDate[0].length() >= 1 ? "0" + splittedPassportDate[0].length() : splittedPassportDate[0].length());
                }
                url = url + "&passport_number[" + i + "]=" + passanger.getPassportId();
                url = url + "&passport_issuing_country[" + i + "]=" + passanger.getPassportIssuingCountry();
                url = url + "&passport_expiry_date[" + i + "]=" + passportDate;
            }
        }

        url = url + "&merchant_id=" + "2";
        url = url + "&code=" + "";
        url = url + "&password=" + "";
        url = url + "&discount_merchant_id=" + "";
        url = url + "&merchant_member_id=" + "";
        url = url + "&policy=" + "1";

        try {
            url = url + "&from_country=" + fromCountry + "&to_country=" + toCountry + "&go_airline_code="
                    + MyApplication.selectedDepartItem.getFlights().getJSONObject(0).getString("airlines")
                    + "&back_airline_code=" + (MyApplication.selectedReturnItem != null ?
                    MyApplication.selectedReturnItem.getFlights().getJSONObject(0).getString("airlines") : "")
                    + "&adult_count=" + adultCount + "&child_count=" + childCount + "&infant_count=" + infantCount
                    + "&from_code=" + fromCode + "&to_code=" + toCode + "&go_date=" + goDateText + "&is_return=" + isReturn
                    + "&back_date=" + backDateText;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        url = url.replace(" ", "+");
        url = url.replace("[", "%5B");
        url = url.replace("]", "%5D");
        url = url.replace(";", "%3B");
        url = url.replace(",", "%2C");
        url = url.replace("@", "%40");
        Utils.logd("url: " + url);

        final String finalGoChoose = goChoose;
        request = new JsonObjectRequest(Request.Method.GET, url, responseListener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                String creds = String.format("%s:%s", "trial", "fauzan");
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
                return params;
            }

        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                5,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(request);

        mCircleView.setShowTextWhileSpinning(true); // Show/hide text in spinning mode
        mCircleView.setText("Loading...");
        mCircleView.setOnAnimationStateChangedListener(
                new AnimationStateChangedListener() {
                    @Override
                    public void onAnimationStateChanged(AnimationState _animationState) {
                        switch (_animationState) {
                            case IDLE:
                            case ANIMATING:

                        }
                    }
                }
        );

        mCircleView.setValueAnimated(1);
    }


    private static SSLSocketFactory createSslSocketFactory() {
        TrustManager[] byPassTrustManagers = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }
        }};

        SSLContext sslContext = null;
        SSLSocketFactory sslSocketFactory = null;
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, byPassTrustManagers, new SecureRandom());
            sslSocketFactory = sslContext.getSocketFactory();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
        }

        return sslSocketFactory;
    }

}
