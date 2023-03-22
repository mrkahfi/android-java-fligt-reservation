package com.arenatiket.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arenatiket.android.api.JsonCache;
import com.arenatiket.android.model.Airline;
import com.arenatiket.android.model.Contact;
import com.arenatiket.android.model.Passanger;
import com.arenatiket.android.model.Ticket;
import com.arenatiket.android.service.BookingTimerService;
import com.arenatiket.android.service.PaymentTimerService;
import com.arenatiket.android.utils.Constants;
import com.arenatiket.android.utils.MyApplication;
import com.arenatiket.android.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ContactInfoActivity extends AppCompatActivity implements View.OnFocusChangeListener, View.OnClickListener {

    private TextView countDownTextView;
    private Toolbar toolbar;
    private AutoCompleteTextView nameEditText;
    private EditText phoneEditText;
    private EditText emailEditText;
    private TextView nameTextView;
    private TextView phoneTextView;
    private TextView emailTextView;
    private LinearLayout mrLayout;
    private LinearLayout msLayout;
    private LinearLayout mrsLayout;
    private TextView mrTextView;
    private TextView msTextView;
    private TextView mrsTextView;
    private int defaultColor;
    private LinearLayout nextButton;
    private TextView passangerCountTextView;
    private ImageView directionArrowImageView;
    private TextView departPortCodeTextView;
    private TextView arrivePortCodeTextView;
    private TextView hargaDiskon1TextView;
    private TextView hargaDiskon2TextView;
    private String name;
    private String phone;
    private String email;
    private TextInputLayout nameLayout;
    private TextInputLayout phoneLayout;
    private TextInputLayout emailLayout;
    private Bundle bundle;
    private String title;
    private BroadcastReceiver countingReceiver;
    private String titleText = "Mr. ";
    private JSONArray contacts = new JSONArray();
    private ArrayList<String> contactNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        passangerCountTextView = (TextView) findViewById(R.id.passangerCountTextView);
        directionArrowImageView = (ImageView) findViewById(R.id.directionArrowImageView);
        departPortCodeTextView = (TextView) findViewById(R.id.depatPortCodeTextView);
        arrivePortCodeTextView = (TextView) findViewById(R.id.arrivePortCodeTextView);

        hargaDiskon1TextView = (TextView) findViewById(R.id.hargaDiskon1TextView);
        hargaDiskon2TextView = (TextView) findViewById(R.id.hargaDiskon2TextView);

        mrLayout = (LinearLayout) findViewById(R.id.mrLayout);
        msLayout = (LinearLayout) findViewById(R.id.msLayout);
        mrsLayout = (LinearLayout) findViewById(R.id.mrsLayout);

        mrTextView = (TextView) findViewById(R.id.mrTextView);
        msTextView = (TextView) findViewById(R.id.msTextView);
        mrsTextView = (TextView) findViewById(R.id.mrsTextView);
        defaultColor = mrsTextView.getCurrentTextColor();

        nameLayout = (TextInputLayout) findViewById(R.id.nameLayout);
        nameTextView = (TextView) findViewById(R.id.nameTextView);

        phoneLayout = (TextInputLayout) findViewById(R.id.phoneLayout);
        phoneTextView = (TextView) findViewById(R.id.phoneTextView);

        emailLayout = (TextInputLayout) findViewById(R.id.emailLayout);
        emailTextView = (TextView) findViewById(R.id.nationalityTextView);

        nameEditText = (AutoCompleteTextView) findViewById(R.id.nameEditText);
        phoneEditText = (EditText) findViewById(R.id.phoneEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);

        nextButton = (LinearLayout) findViewById(R.id.nextButton);

        countDownTextView = (TextView) findViewById(R.id.countDownTextView);


        Intent timerIntent = new Intent(this, BookingTimerService.class);
        startService(timerIntent);

        contacts = JsonCache.getContacts(this);

        for (int i = 0; i < contacts.length(); i++) {
            try {
                JSONObject contact = contacts.getJSONObject(i);
                contactNames.add(contact.getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, contactNames);
        nameEditText.setAdapter(adapter);

        nameEditText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Utils.logd("itemSelected " + i);
                try {
                    Utils.logd(contacts.getJSONObject(i).getString("email"));
                    emailEditText.setText(contacts.getJSONObject(i).getString("email"));
                    phoneEditText.setText(contacts.getJSONObject(i).getString("phone"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });

        bundle = getIntent().getBundleExtra("searchParams");
        String backDatesText = bundle.getString("backDatesText");
        String backDateText = bundle.getString("backDate");
        String goDateText = getIntent().getStringExtra("selectedGoDate");
        String isReturn = bundle.getString("isReturn");
        String fromCity = bundle.getString("fromCity");
        String toCity = bundle.getString("toCity");
        int adultCount = Integer.parseInt(bundle.getString("adultCount"));
        int childCount = Integer.parseInt(bundle.getString("childCount"));
        int infantCount = Integer.parseInt(bundle.getString("infantCount"));

        if (isReturn.equals("0")) {
            directionArrowImageView.setImageResource(R.drawable.one_path_arrow);
        }

        passangerCountTextView.setText(adultCount + " dewasa"
                + (childCount > 0 ? ", " + childCount + " anak, " : "")
                + (infantCount > 0 ? ", " + infantCount + " bayi, " : ""));


        if (MyApplication.selectedDepartItem != null) {
            try {
                Ticket ticket = MyApplication.selectedDepartItem;

                String id = ticket.getId();
                JSONArray flights = ticket.getFlights();
                Map<String, String> values = ticket.getValues();

                String departPort = flights.getJSONObject(0).getString("depart_port");
                String arrivePort = flights.getJSONObject(flights.length() - 1).getString("arrive_port");

                departPortCodeTextView.setText(departPort);
                arrivePortCodeTextView.setText(arrivePort);

                String hargaDiskon = values.get("harga_diskon");
                Utils.logd("harga_diskon " + hargaDiskon);
                String hargaDiskonCh = values.get("harga_diskon_ch");
                String hargaDiskonIn = values.get("harga_diskon_in");

                int price_ad = Integer.parseInt(hargaDiskon);
                int price_ch = !hargaDiskonCh.equals("0") ? Integer.parseInt(hargaDiskonCh) : price_ad;
                int price_in = !hargaDiskonIn.equals("0") ? Integer.parseInt(hargaDiskonIn) : price_ad;

                int totalPrice = (price_ad * adultCount) + (price_ch * childCount) + (price_in * infantCount);

                hargaDiskon = totalPrice + "";

                String hargaDiskon1 = Utils.getMoneyFormat(Integer.parseInt(hargaDiskon.substring(0, hargaDiskon.length() - 3)), false);
                String hargaDiskon2 = hargaDiskon.substring(hargaDiskon.length() - 3);
                hargaDiskon1TextView.setText(hargaDiskon1);
                hargaDiskon2TextView.setText("." + hargaDiskon2);
                Airline airline = Constants.airlines.get(flights.getJSONObject(0).getString("airlines"));

                if (MyApplication.selectedReturnItem != null) {
                    directionArrowImageView.setImageResource(R.drawable.two_path_arrow);
                    Ticket returnTicket = MyApplication.selectedReturnItem;
                    String returnId = returnTicket.getId();
                    JSONArray returnFlights = returnTicket.getFlights();
                    Map<String, String> returnValues = returnTicket.getValues();

                    String returnHargaDiskon = returnValues.get("harga_diskon");
                    Utils.logd("back harga_diskon " + returnHargaDiskon);
                    String returnHargaDiskonCh = returnValues.get("harga_diskon_ch");
                    String returnHargaDiskonIn = returnValues.get("harga_diskon_in");

                    int back_price_ad = Integer.parseInt(returnHargaDiskon);
                    int back_price_ch = !returnHargaDiskonCh.equals("0") ? Integer.parseInt(returnHargaDiskonCh) : back_price_ad;
                    int back_price_in = !returnHargaDiskonIn.equals("0") ? Integer.parseInt(returnHargaDiskonIn) : back_price_ad;

                    int backTotalPrice = (back_price_ad * adultCount) + (back_price_ch * childCount) + (back_price_in * infantCount);

                    int totalHarga = Integer.parseInt(hargaDiskon) + backTotalPrice;
                    String totalHargaText = totalHarga + "";
                    String totalHarga1 = Utils.getMoneyFormat(Integer.parseInt(totalHargaText.substring(0, totalHargaText.length() - 3)), false);
                    String totalHarga2 = totalHargaText.substring(totalHargaText.length() - 3);
                    hargaDiskon1TextView.setText(totalHarga1);
                    hargaDiskon2TextView.setText("." + totalHarga2);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        title = "1";

        initEventListener();

    }

    private void initEventListener() {
        mrLayout.setOnClickListener((View.OnClickListener) this);
        msLayout.setOnClickListener((View.OnClickListener) this);
        mrsLayout.setOnClickListener((View.OnClickListener) this);
        nextButton.setOnClickListener((View.OnClickListener) this);

        nameEditText.setOnFocusChangeListener((View.OnFocusChangeListener) this);
        phoneEditText.setOnFocusChangeListener((View.OnFocusChangeListener) this);
        emailEditText.setOnFocusChangeListener((View.OnFocusChangeListener) this);

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

    @Override
    public void onFocusChange(View view, boolean hasFocus) {

        nameTextView.setTextColor(getResources().getColor(R.color.gray13));
        phoneTextView.setTextColor(getResources().getColor(R.color.gray13));
        emailTextView.setTextColor(getResources().getColor(R.color.gray13));

        if (hasFocus) {
            if (view.getId() == nameEditText.getId()) {
                nameTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
            } else if (view.getId() == phoneEditText.getId()) {
                phoneTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
            } else if (view.getId() == emailEditText.getId()) {
                emailTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == mrLayout.getId() || view.getId() == msLayout.getId() || view.getId() == mrsLayout.getId()) {
            mrLayout.setBackgroundResource(0);
            msLayout.setBackgroundResource(0);
            mrsLayout.setBackgroundResource(0);

            mrTextView.setTextColor(defaultColor);
            msTextView.setTextColor(defaultColor);
            mrsTextView.setTextColor(defaultColor);
            if (view.getId() == mrLayout.getId()) {
                mrLayout.setBackgroundResource(R.drawable.bg_capsule_active);
                mrTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
                title = "1";
                titleText = "Mr. ";
            } else if (view.getId() == msLayout.getId()) {
                msLayout.setBackgroundResource(R.drawable.bg_capsule_active);
                msTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
                title = "2";
                titleText = "Ms. ";
            } else if (view.getId() == mrsLayout.getId()) {
                mrsLayout.setBackgroundResource(R.drawable.bg_capsule_active);
                mrsTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
                title = "3";
                titleText = "Mrs. ";
            }
        } else if (view.getId() == nextButton.getId()) {
            name = nameEditText.getText().toString();
            phone = phoneEditText.getText().toString();
            email = emailEditText.getText().toString();

            JSONObject contactJsonObject = new JSONObject();
            try {
                contactJsonObject.put("name", name);
                contactJsonObject.put("phone", phone);
                contactJsonObject.put("email", email);
//                if (!userexists(JsonCache.getContacts(this), name)) {
                JsonCache.saveContact(this, contactJsonObject.toString());
//                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            boolean valid = validate(name, phone, email);
            if (valid) {
                Contact contact = new Contact(title, titleText, name, email, phone);
                Intent intent = new Intent(this, PassangerInfoActivity.class);
                intent.putExtra("searchParams", bundle);
                intent.putExtra("contact_person", contact);
                startActivity(intent);
            }
        }
    }

    private boolean userexists(JSONArray jsonArray, String usernameToFind) {
        return jsonArray.toString().contains("\"name\":\"" + usernameToFind + "\"");
    }

    private boolean validate(String name, String phone, String email) {
        boolean valid = true;
        if (name.isEmpty() || name.length() < 3) {
            nameEditText.setError("Minimum 3 karakter");
            valid = false;
        } else {
            nameEditText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Masukan email yang valid");
            valid = false;
        } else {
            emailEditText.setError(null);
        }

        if (phone.isEmpty() || phone.length() < 11 || phone.length() > 14) {
            phoneEditText.setError("Panjang 11 sampai 14 karakter");
            valid = false;
        } else {
            phoneEditText.setError(null);
        }

        return valid;
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((countingReceiver),
                new IntentFilter(BookingTimerService.BOOKING)
        );
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(countingReceiver);
        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_options:
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
        }
        return false;

    }


}
