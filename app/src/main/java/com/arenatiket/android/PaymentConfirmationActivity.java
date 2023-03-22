package com.arenatiket.android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.arenatiket.android.api.JsonCache;
import com.arenatiket.android.utils.Constants;
import com.arenatiket.android.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PaymentConfirmationActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener, View.OnTouchListener {

    private Toolbar toolbar;
    private TextView methodTextView;
    private Spinner methodSpinner;
    private TextView dateTextView;
    private Spinner dateSpinner;
    private TextView bankNameTextView;
    private EditText bankNameEditText;
    private TextView bankAccountTextView;
    private Spinner bankAccountSpinner;
    private TextView amountTextView;
    private EditText amountEditText;
    private int defaultColor;
    private int primaryColor;
    private TextView bookingCodeTextView;
    private EditText bookingCodeEditText;
    private LinearLayout formLayout;
    private LinearLayout confirmButton;
    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            confirmButton.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            Utils.logd("message : " + error.toString());
        }
    };
    private String url = Constants.baseApiUrl + "android/payment?accesskey=780b4191985eab0fa83b6bf3adbaaf52";
    private ProgressBar progressBar;
    private int bankId;
    private String reservationId;
    private String bookingDateString;
    private int price;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_confirmation);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        TextView titleTextView = (TextView) toolbar.findViewById(R.id.toolbarTitleTextView);
        titleTextView.setText("Konfirmasi");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        bankId = getIntent().getIntExtra("bank_id", 1);
        reservationId = getIntent().getStringExtra("reservation_id");
        price = getIntent().getIntExtra("price", 0);

        primaryColor = getResources().getColor(R.color.colorPrimary);

        formLayout = (LinearLayout) findViewById(R.id.formLayout);
        methodTextView = (TextView) findViewById(R.id.methodTextView);
        defaultColor = methodTextView.getCurrentTextColor();
        methodSpinner = (Spinner) findViewById(R.id.methodSpinner);
        dateTextView = (TextView) findViewById(R.id.dateTextView);
        dateSpinner = (Spinner) findViewById(R.id.dateSpinner);
        bankNameTextView = (TextView) findViewById(R.id.bankNameTextView);
        bankNameEditText = (EditText) findViewById(R.id.bankNameEditText);
        bankAccountTextView = (TextView) findViewById(R.id.bankAccountTextView);
        bankAccountSpinner = (Spinner) findViewById(R.id.bankAccountSpinner);
        amountTextView = (TextView) findViewById(R.id.amountTextView);
        amountEditText = (EditText) findViewById(R.id.amountEditText);
        bookingCodeTextView = (TextView) findViewById(R.id.bookingCodeTextView);
        bookingCodeEditText = (EditText) findViewById(R.id.bookingCodeEditText);
        confirmButton = (LinearLayout) findViewById(R.id.confirmButton);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        initEventListener();

        resetColors();
        methodTextView.setTextColor(primaryColor);
        bankNameEditText.postDelayed(new Runnable() {
            @Override
            public void run() {
                bankNameEditText.clearFocus();
            }
        }, 10);
        methodSpinner.requestFocus();

        initData();
    }

    private void initData() {

        JSONObject reservation = JsonCache.getReservation(this);
        try {
            JSONObject results = reservation.getJSONArray("progress").getJSONObject(0).getJSONObject("data")
                    .getJSONObject("results");
            bookingDateString = results.getString("datetime_of_booking");

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat indonesianFormat = new SimpleDateFormat("dd MMM yyyy");
            Date date = format.parse(bookingDateString);
            Calendar bookingDay = Calendar.getInstance();
            bookingDay.setTime(date);

            Calendar today = Calendar.getInstance();
            today.getTime();
            int diff = Utils.getDateDifference(date, today.getTime());
            ArrayList<String> dateTexts = new ArrayList<>();
            ArrayList<String> dateValues = new ArrayList<>();


            String text = format.format(date);
            dateValues.add(text);

            String formattedText = indonesianFormat.format(date);
            dateTexts.add(formattedText);

            for (int i = 1; i < diff; i++) {
                bookingDay.add(Calendar.DAY_OF_MONTH, 1);
                date = bookingDay.getTime();

                text = format.format(date);
                dateValues.add(text);

                formattedText = indonesianFormat.format(date);
                dateTexts.add(formattedText);

            }

            Utils.logd("size " + dateTexts.size() + "");

            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, dateTexts);
            dateSpinner.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        bankAccountSpinner.setSelection(bankId - 1);
        amountEditText.setText(price+ "");
    }

    private void resetColors() {
        methodTextView.setTextColor(defaultColor);
        dateTextView.setTextColor(defaultColor);
        bankNameTextView.setTextColor(defaultColor);
        bankAccountTextView.setTextColor(defaultColor);
        amountTextView.setTextColor(defaultColor);
        bookingCodeEditText.setTextColor(defaultColor);

    }

    private void initEventListener() {
        methodSpinner.setOnFocusChangeListener((View.OnFocusChangeListener) this);
        dateSpinner.setOnFocusChangeListener((View.OnFocusChangeListener) this);
        bankNameEditText.setOnFocusChangeListener((View.OnFocusChangeListener) this);
        bankAccountSpinner.setOnFocusChangeListener((View.OnFocusChangeListener) this);
        amountEditText.setOnFocusChangeListener((View.OnFocusChangeListener) this);
        bookingCodeEditText.setOnFocusChangeListener((View.OnFocusChangeListener) this);

        methodSpinner.setOnTouchListener((View.OnTouchListener) this);
        dateSpinner.setOnTouchListener((View.OnTouchListener) this);
        bankNameEditText.setOnClickListener((View.OnClickListener) this);
        bankAccountSpinner.setOnTouchListener((View.OnTouchListener) this);
        amountEditText.setOnClickListener((View.OnClickListener) this);
        bookingCodeEditText.setOnClickListener((View.OnClickListener) this);
        confirmButton.setOnClickListener((View.OnClickListener) this);

        bankAccountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bankId = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == confirmButton.getId()) {
            confirm();
        }
    }

    private void confirm() {
        url = url + "&bank_id=" + bankId + "&reservation_id=" + reservationId + "&payment_doku=";
        confirmButton.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        Utils.logd("url " + url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                confirmButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                Utils.longLog("url response : " + response.toString());
                try {
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
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                Constants.MY_SOCKET_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(request);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        resetColors();

        if (hasFocus) {
            if (v.getId() == methodSpinner.getId()) {
                methodTextView.setTextColor(primaryColor);
            } else if (v.getId() == dateSpinner.getId()) {
                dateTextView.setTextColor(primaryColor);
            } else if (v.getId() == bankNameEditText.getId()) {
                bankNameTextView.setTextColor(primaryColor);
            } else if (v.getId() == bankAccountSpinner.getId()) {
                bankAccountTextView.setTextColor(primaryColor);
            } else if (v.getId() == amountEditText.getId()) {
                amountTextView.setTextColor(primaryColor);
            } else if (v.getId() == bookingCodeEditText.getId()) {
                bookingCodeTextView.setTextColor(primaryColor);
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        v.requestFocus();
        resetColors();
        if (v.getId() == methodSpinner.getId()) {
            methodTextView.setTextColor(primaryColor);
        } else if (v.getId() == dateSpinner.getId()) {
            dateTextView.setTextColor(primaryColor);
        } else if (v.getId() == bankNameEditText.getId()) {
            bankNameTextView.setTextColor(primaryColor);
        } else if (v.getId() == bankAccountSpinner.getId()) {
            bankAccountTextView.setTextColor(primaryColor);
        } else if (v.getId() == amountEditText.getId()) {
            amountTextView.setTextColor(primaryColor);
        } else if (v.getId() == bookingCodeEditText.getId()) {
            bookingCodeTextView.setTextColor(primaryColor);
        }
        return false;
    }

}
