package com.arenatiket.android;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;
import com.arenatiket.android.adapter.AutoCompleteAdapter;
import com.arenatiket.android.api.JsonCache;
import com.arenatiket.android.model.AirPort;
import com.arenatiket.android.utils.Constants;
import com.arenatiket.android.utils.MyApplication;
import com.arenatiket.android.utils.SelectionDecorator;
import com.arenatiket.android.utils.Utils;
import com.arenatiket.android.utils.VolleyToolboxExtension;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.json.JSONArray;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {

    private LinearLayout searchButton;
    private ImageView passangerButton;
    private RelativeLayout passangerSelectorLayout;
    private RelativeLayout passangerFieldLayout;
    private ImageView adultIncreaseButton;
    private ImageView adultDecreaseButton;
    private ImageView childIncreaseButton;
    private ImageView childDecreaseButton;
    private ImageView infantIncreaseButton;
    private ImageView infantDecreaseButton;
    private TextView adultCountTextView;
    private TextView childCountTextView;
    private TextView infantCountTextView;
    private TextView adultPassangerTextView;
    private TextView childPassangerTextView;
    private TextView infantPassangerTextView;
    private RelativeLayout fromLayout;
    private RelativeLayout toLayout;
    private ScrollView scrollView;
    private AutoCompleteTextView fromAutoCompleteTextView;
    private AutoCompleteTextView toAutoCompleteTextView;
    private TextView fromTextView;
    private TextView toTextView;

    private ArrayList<AirPort> ports = new ArrayList<>();
    private MaterialCalendarView calendar;
    private RelativeLayout goDateButtonLayout;
    private RelativeLayout backDateButtonLayout;
    private int currentScrollPosition;
    private LinearLayout calendarLayout;
    private TextView fromLabelTextView;
    private TextView toLabelTextView;
    private TextView penumpangTextView;
    private int defaultColor;
    private TextView tanggalBerangkatTextView;
    private TextView tanggalKembaliTextView;
    private TextView goDateTextView;
    private TextView backDateTextView;
    private TextView goMonthTextView;
    private TextView backMonthTextView;
    private Date goDate;
    private Date backDate;
    private SelectionDecorator rangeDecorator;
    private SelectionDecorator goSelectedDecorator;
    private SelectionDecorator backSelectedDecorator;
    private ImageView deleteBackDateButton;
    private ImageView revertButton;
    private AirPort fromAirPort;
    private AirPort toAirPort;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
//        setupNavDrawer();
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        passangerButton = (ImageView) findViewById(R.id.passangerButton);
        passangerFieldLayout = (RelativeLayout) findViewById(R.id.passangerFieldLayout);
        searchButton = (LinearLayout) findViewById(R.id.nextButton);
        fromLayout = (RelativeLayout) findViewById(R.id.fromLayout);
        toLayout = (RelativeLayout) findViewById(R.id.toLayout);
        fromTextView = (TextView) findViewById(R.id.fromTextView);
        toTextView = (TextView) findViewById(R.id.toTextView);
        fromLabelTextView = (TextView) findViewById(R.id.fromLabelTextView);
        toLabelTextView = (TextView) findViewById(R.id.toLabelTextView);
        fromAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.fromAutoCompleteTextView);
        toAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.toAutoCompleteTextView);
        revertButton = (ImageView) findViewById(R.id.revertButton);

        defaultColor = fromLabelTextView.getCurrentTextColor();

        tanggalBerangkatTextView = (TextView) findViewById(R.id.tanggalBerangkatTextView);
        tanggalKembaliTextView = (TextView) findViewById(R.id.tanggalKembaliTextView);
        goDateTextView = (TextView) findViewById(R.id.goDateTextView);
        backDateTextView = (TextView) findViewById(R.id.backDateTextView);
        goMonthTextView = (TextView) findViewById(R.id.goMonthTextView);
        backMonthTextView = (TextView) findViewById(R.id.backMonthTextView);
        goDateButtonLayout = (RelativeLayout) findViewById(R.id.goDateButtonLayout);
        backDateButtonLayout = (RelativeLayout) findViewById(R.id.backDateButtonLayout);
        deleteBackDateButton = (ImageView) findViewById(R.id.deleteBackDateButton);

        penumpangTextView = (TextView) findViewById(R.id.penumpangTextView);
        passangerSelectorLayout = (RelativeLayout) findViewById(R.id.passangerSelectorLayout);
        adultIncreaseButton = (ImageView) findViewById(R.id.adultIncreaseButton);
        adultDecreaseButton = (ImageView) findViewById(R.id.adultDecreaseButton);
        childIncreaseButton = (ImageView) findViewById(R.id.childIncreaseButton);
        childDecreaseButton = (ImageView) findViewById(R.id.childDecreaseButton);
        infantIncreaseButton = (ImageView) findViewById(R.id.infantIncreaseButton);
        infantDecreaseButton = (ImageView) findViewById(R.id.infantDecreaseButton);
        adultCountTextView = (TextView) findViewById(R.id.adultCountTextView);
        childCountTextView = (TextView) findViewById(R.id.childCountTextView);
        infantCountTextView = (TextView) findViewById(R.id.infantCountTextView);
        adultPassangerTextView = (TextView) findViewById(R.id.adultPassangerTextView);
        childPassangerTextView = (TextView) findViewById(R.id.childPassangerTextView);
        infantPassangerTextView = (TextView) findViewById(R.id.infantPassangerTextView);
        calendar = (MaterialCalendarView) findViewById(R.id.calendarView);
        calendarLayout = (LinearLayout) findViewById(R.id.calendarLayout);

        initDate();
        initDataset();
        initEventListener();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_confirm_payment:
                if (!JsonCache.getReservation(this).isNull("reservation_id")) {
                    startActivity(new Intent(SearchActivity.this, PaymentMenuActivity.class));
                } else {
                    Snackbar.make(scrollView, "Anda belum melakukan pemesanan", Snackbar.LENGTH_SHORT).show();
                }
        }
        return true;
    }

    protected void initDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_MONTH, -1);
        calendar.setMinimumDate(cal);

        cal.add(Calendar.DAY_OF_MONTH, 3); //Adds a day
        goDate = cal.getTime();
        calendar.setDateSelected(goDate, true);

        goDateTextView.setText(cal.get(Calendar.DAY_OF_MONTH) + "");
        goMonthTextView.setText(Utils.getMonthShortName(cal.get(Calendar.MONTH)) + " " + cal.get(Calendar.YEAR));
    }

    private void initDataset() {

        ports = JsonCache.getAirports(this);
        AutoCompleteAdapter adapter = new AutoCompleteAdapter(this, R.layout.autocomplete_list_item, ports);
        fromAutoCompleteTextView.setAdapter(adapter);
        fromAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                fromAirPort = ports.get(i);
                fromTextView.setText(ports.get(i).getCity() + " (" + ports.get(i).getCode() + ")");
                fromAutoCompleteTextView.setVisibility(View.GONE);
            }
        });

        toAutoCompleteTextView.setAdapter(adapter);
        toAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                toAirPort = ports.get(i);
                toTextView.setText(ports.get(i).getCity() + " (" + ports.get(i).getCode() + ")");
                toAutoCompleteTextView.setVisibility(View.GONE);
            }
        });

        fromAirPort = ports.get(Constants.DEFAULT_FROM_AIRPORT_POSITION);
        fromTextView.setText(ports.get(Constants.DEFAULT_FROM_AIRPORT_POSITION).getCity()
                + " (" + ports.get(Constants.DEFAULT_FROM_AIRPORT_POSITION).getCode() + ")");
        fromAutoCompleteTextView.setVisibility(View.GONE);
        toAirPort = ports.get(Constants.DEFAULT_TO_AIRPORT_POSITION);
        toTextView.setText(ports.get(Constants.DEFAULT_TO_AIRPORT_POSITION).getCity() + " ("
                + ports.get(Constants.DEFAULT_TO_AIRPORT_POSITION).getCode() + ")");
        toAutoCompleteTextView.setVisibility(View.GONE);

    }


    private void initEventListener() {
        fromLayout.setOnClickListener((View.OnClickListener) this);
        toLayout.setOnClickListener((View.OnClickListener) this);
        revertButton.setOnClickListener((View.OnClickListener) this);

        searchButton.setOnClickListener((View.OnClickListener) this);
        passangerFieldLayout.setOnClickListener((View.OnClickListener) this);
        adultIncreaseButton.setOnClickListener((View.OnClickListener) this);
        adultDecreaseButton.setOnClickListener((View.OnClickListener) this);
        childIncreaseButton.setOnClickListener((View.OnClickListener) this);
        childDecreaseButton.setOnClickListener((View.OnClickListener) this);
        infantIncreaseButton.setOnClickListener((View.OnClickListener) this);
        infantDecreaseButton.setOnClickListener((View.OnClickListener) this);
        goDateButtonLayout.setOnClickListener((View.OnClickListener) this);
        backDateButtonLayout.setOnClickListener((View.OnClickListener) this);
        deleteBackDateButton.setOnClickListener((View.OnClickListener) this);

        fromAutoCompleteTextView.setOnFocusChangeListener((View.OnFocusChangeListener) this);
        toAutoCompleteTextView.setOnFocusChangeListener((View.OnFocusChangeListener) this);

        calendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(MaterialCalendarView widget, CalendarDay date, boolean selected) {
                calendar.removeDecorators();
                if (tanggalBerangkatTextView.getCurrentTextColor() == getResources().getColor(R.color.colorPrimary)) {
                    goDateTextView.setText(date.getDay() + "");
                    goMonthTextView.setText(Utils.getMonthShortName(date.getMonth()) + " " + date.getYear());
                    goDate = date.getDate();
                    if (backDate != null) {
                        Collection<CalendarDay> rangeDays = Utils.getDates(goDate, backDate);
                        Collection<CalendarDay> goSelectedDay = new ArrayList<CalendarDay>();
                        goSelectedDay.add(date);

                        Collection<CalendarDay> backSelectedDay = new ArrayList<CalendarDay>();
                        backSelectedDay.add(CalendarDay.from(backDate));
                        rangeDecorator = new SelectionDecorator(SearchActivity.this, SelectionDecorator.RANGE_DECORATOR,
                                rangeDays);
                        goSelectedDecorator = new SelectionDecorator(SearchActivity.this, SelectionDecorator.CAPSULE_LEFT_BLUE_DECORATOR,
                                goSelectedDay);
                        backSelectedDecorator = new SelectionDecorator(SearchActivity.this, SelectionDecorator.CAPSULE_RIGHT_BLUE_DECORATOR,
                                backSelectedDay);
                        calendar.addDecorator(rangeDecorator);
                        calendar.addDecorator(goSelectedDecorator);
                        calendar.addDecorator(backSelectedDecorator);
                    }
                } else if (tanggalKembaliTextView.getCurrentTextColor() == getResources().getColor(R.color.colorPrimary)) {
                    calendar.setDateSelected(backDate, false);
                    backDateTextView.setText(date.getDay() + "");
                    backMonthTextView.setText(Utils.getMonthShortName(date.getMonth()) + " " + date.getYear());
                    backDateTextView.setTextColor(getResources().getColor(R.color.black));
                    backMonthTextView.setTextColor(getResources().getColor(R.color.black));
                    backDate = date.getDate();

                    Collection<CalendarDay> rangeDays = Utils.getDates(goDate, backDate);
                    Collection<CalendarDay> goSelectedDay = new ArrayList<CalendarDay>();
                    goSelectedDay.add(CalendarDay.from(goDate));

                    Collection<CalendarDay> backSelectedDay = new ArrayList<CalendarDay>();
                    backSelectedDay.add(date);
                    rangeDecorator = new SelectionDecorator(SearchActivity.this, SelectionDecorator.RANGE_DECORATOR,
                            rangeDays);
                    goSelectedDecorator = new SelectionDecorator(SearchActivity.this, SelectionDecorator.CAPSULE_LEFT_BLUE_DECORATOR,
                            goSelectedDay);
                    backSelectedDecorator = new SelectionDecorator(SearchActivity.this, SelectionDecorator.CAPSULE_RIGHT_BLUE_DECORATOR,
                            backSelectedDay);
                    calendar.addDecorator(rangeDecorator);
                    calendar.addDecorator(goSelectedDecorator);
                    calendar.addDecorator(backSelectedDecorator);

                    deleteBackDateButton.setVisibility(View.VISIBLE);
                }

                if (backDate != null) {
                    if (goDate.compareTo(backDate) >= 0) {
                        calendar.clearSelection();
                        resetBackDate();
                    }
                }
                hideCalendar();
            }
        });

    }

    protected void resetBackDate() {
        backDate = null;

        calendar.removeDecorators();

        backDateTextView.setText("+");
        backMonthTextView.setText("PILIH TANGGAL");

        backDateTextView.setTextColor(defaultColor);
        backMonthTextView.setTextColor(defaultColor);

        deleteBackDateButton.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        if (fromAutoCompleteTextView.hasFocus() || toAutoCompleteTextView.hasFocus()) {
            clearFieldFocus();
        }

        setDefaultColor();
        if (calendarLayout.getVisibility() == View.VISIBLE) {
            hideCalendar();
        } else if (passangerSelectorLayout.getVisibility() == View.VISIBLE) {
            hidePassangerSelector();
        } else {
            super.onBackPressed();
        }
    }

    public void showCalendar() {
        hidePassangerSelector();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(goDateButtonLayout.getWindowToken(), 0);

        Calendar cal = Calendar.getInstance();
        if (tanggalKembaliTextView.getCurrentTextColor() != defaultColor) {
            cal.setTime(goDate);
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        calendar.setMinimumDate(cal);

        calendarLayout.animate()
                .translationY(0)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        calendarLayout.setVisibility(View.VISIBLE);
                        goDateButtonLayout.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                scrollView.smoothScrollTo(0, goDateButtonLayout.getBottom());
                            }
                        }, 100);
                    }
                });
    }

    public void showPassangerSelector() {
        hideCalendar();
        passangerSelectorLayout.animate()
                .translationY(0)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        passangerSelectorLayout.setVisibility(View.VISIBLE);
                        passangerFieldLayout.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                scrollView.smoothScrollTo(0, passangerFieldLayout.getBottom());
                            }
                        }, 100);
                    }
                });
    }

    public void hideCalendar() {
        calendarLayout.animate()
                .translationY(calendarLayout.getHeight())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        calendarLayout.setVisibility(View.GONE);
                        goDateButtonLayout.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                scrollView.smoothScrollTo(0, currentScrollPosition);
                            }
                        }, 100);

                    }
                });
    }

    public void hidePassangerSelector() {
        passangerButton.setImageDrawable(getResources().getDrawable(R.drawable.arrow_down));
        passangerSelectorLayout.animate()
                .translationY(calendarLayout.getHeight())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        passangerSelectorLayout.setVisibility(View.GONE);
                        passangerFieldLayout.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                scrollView.smoothScrollTo(0, currentScrollPosition);
                            }
                        }, 100);

                    }
                });
    }

    @Override
    public void onClick(View view) {

        clearFieldFocus();
        hideCalendar();

        if (view.getId() == fromLayout.getId()) {
            setDefaultColor();
            hideCalendar();
            fromTextView.setVisibility(View.GONE);
            fromTextView.setVisibility(View.GONE);
            fromAutoCompleteTextView.setVisibility(View.VISIBLE);
            fromAutoCompleteTextView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    fromAutoCompleteTextView.requestFocus();
                    scrollView.smoothScrollTo(0, fromLayout.getBottom());
                }
            }, 100);
        } else if (view.getId() == toLayout.getId()) {
            setDefaultColor();
            hideCalendar();
            toTextView.setVisibility(View.GONE);
            toAutoCompleteTextView.setVisibility(View.VISIBLE);
            toAutoCompleteTextView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    toAutoCompleteTextView.requestFocus();
                    scrollView.smoothScrollTo(0, toLayout.getBottom());
                }
            }, 100);
        } else if (view.getId() == searchButton.getId()) {
            MyApplication.backItems = new ArrayList<>();

            setDefaultColor();
            String fromCode = fromAirPort.getCode();
            String toCode = toAirPort.getCode();
            String fromCity = fromAirPort.getCity();
            String fromCountry = fromAirPort.getCountry_code();
            String toCity = toAirPort.getCity();
            String toCountry = toAirPort.getCountry_code();

            String goDateText = DateFormat.format("yyyy-MM-dd", goDate).toString();

            JSONArray goDateJsonArray = new JSONArray();
            Calendar countedCal = Calendar.getInstance();
            countedCal.setTime(goDate);
            countedCal.add(Calendar.DAY_OF_MONTH, -1);

            Calendar currentCal = Calendar.getInstance();
            currentCal.add(Calendar.DAY_OF_MONTH, 1);
            int diff0 = Utils.getDateDifference(currentCal.getTime(), countedCal.getTime());
            Utils.logd("diff0 " + diff0);
            if (diff0 < 0) {
                countedCal.setTime(currentCal.getTime());
            }

            int diff = Utils.getDateDifference(countedCal.getTime(), goDate);
            int diff2 = 1;
            Utils.logd("diff " + diff);
            if (backDate != null) {
                diff2 = Utils.getDateDifference(goDate, backDate);
                Utils.logd("goDate " + DateFormat.format("yyyy-MM-dd", goDate).toString());
                Utils.logd("backDate " + DateFormat.format("yyyy-MM-dd", backDate).toString());
                if (diff2 > 1) {
                    diff2 = 1;
                }
            }

            Utils.logd("diff2 " + diff2);

            for (int i = 0; i <= diff + diff2; i++) {
                Date date = countedCal.getTime();
                String dateText = DateFormat.format("yyyy-MM-dd", date).toString();
                goDateJsonArray.put(dateText);
                countedCal.add(Calendar.DAY_OF_MONTH, 1);
            }
            String goDatesText = goDateJsonArray.toString();
            Utils.logd(goDatesText);

            Utils.logd("fromAirPOrt " + fromAirPort.getCountry_code() + ", toAirport " + toAirPort.getCountry_code());

            if (fromAirPort.getCountry_code().equals(toAirPort.getCountry_code())) {
                MyApplication.isInternationalFlight = false;
            } else {
                MyApplication.isInternationalFlight = true;
            }


            String backDateText = "";
            String backDatesText = "[]";
            JSONArray backDateJsonArray = new JSONArray();
            if (backDate != null) {
                backDateText = DateFormat.format("yyyy-MM-dd", backDate).toString();

                countedCal.setTime(goDate);
                diff = backDate.compareTo(goDate);


                for (int i = 0; i <= diff + 1; i++) {
                    Date date = countedCal.getTime();
                    String dateText = DateFormat.format("yyyy-MM-dd", date).toString();
                    backDateJsonArray.put(dateText);
                    countedCal.add(Calendar.DAY_OF_MONTH, 1);
                }
                backDatesText = backDateJsonArray.toString();
                Utils.logd(backDatesText);

                backDateText = DateFormat.format("yyyy-MM-dd", backDate).toString();
            }


            MyApplication.goQueue = VolleyToolboxExtension.newRequestQueue(this, new HurlStack(null, createSslSocketFactory()));
            MyApplication.selectedDepartItem = null;
            MyApplication.selectedReturnItem = null;

            MyApplication.nextActivity = "";
            Intent intent = new Intent(this, SearchResultActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("fromCode", fromCode);
            bundle.putString("toCode", toCode);
            bundle.putString("fromCity", fromCity);
            bundle.putString("fromCountry", fromCountry);
            bundle.putString("toCity", toCity);
            bundle.putString("toCountry", toCountry);
            bundle.putString("goDate", goDateText);
            bundle.putString("backDate", backDateText);
            bundle.putString("goDatesText", goDatesText);
            bundle.putString("backDatesText", backDatesText);
            bundle.putString("isReturn", backDate == null ? "0" : "1");
            bundle.putString("adultCount", adultCountTextView.getText().toString() != null ? adultCountTextView.getText().toString() : "0");
            bundle.putString("childCount", childCountTextView.getText().toString() != null ? childCountTextView.getText().toString() : "0");
            bundle.putString("infantCount", infantCountTextView.getText().toString() != null ? infantCountTextView.getText().toString() : "0");
            intent.putExtra("searchParams", bundle);
            startActivity(intent);
        } else if (view.getId() == passangerFieldLayout.getId()) {
            if (passangerSelectorLayout.getVisibility() == View.VISIBLE) {
                setDefaultColor();
                hidePassangerSelector();
            } else if (passangerSelectorLayout.getVisibility() == View.GONE) {
                penumpangTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
                passangerButton.setImageDrawable(getResources().getDrawable(R.drawable.arrow_up));
                currentScrollPosition = passangerFieldLayout.getBottom();
                showPassangerSelector();
            }
        } else if (view.getId() == adultIncreaseButton.getId()) {
            if (Integer.parseInt(adultCountTextView.getText().toString()) < 6) {
                adultCountTextView.setText((Integer.parseInt(adultCountTextView.getText().toString()) + 1) + "");
            }
        } else if (view.getId() == adultDecreaseButton.getId()) {
            if (Integer.parseInt(adultCountTextView.getText().toString()) > 1) {
                adultCountTextView.setText((Integer.parseInt(adultCountTextView.getText().toString()) - 1) + "");
            }
        } else if (view.getId() == childIncreaseButton.getId()) {
            if (Integer.parseInt(childCountTextView.getText().toString()) < 4) {
                childCountTextView.setText((Integer.parseInt(childCountTextView.getText().toString()) + 1) + "");
            }
        } else if (view.getId() == childDecreaseButton.getId()) {
            if (Integer.parseInt(childCountTextView.getText().toString()) > 0) {
                childCountTextView.setText((Integer.parseInt(childCountTextView.getText().toString()) - 1) + "");
            }
        } else if (view.getId() == infantIncreaseButton.getId()) {
            if (Integer.parseInt(infantCountTextView.getText().toString()) < 4) {
                infantCountTextView.setText((Integer.parseInt(infantCountTextView.getText().toString()) + 1) + "");
            }
        } else if (view.getId() == infantDecreaseButton.getId()) {
            if (Integer.parseInt(infantCountTextView.getText().toString()) > 0) {
                infantCountTextView.setText((Integer.parseInt(infantCountTextView.getText().toString()) - 1) + "");
            }
        } else if (view.getId() == goDateButtonLayout.getId()) {
            setDefaultColor();
//            if (calendarLayout.getVisibility() == View.GONE) {
            tanggalBerangkatTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
            currentScrollPosition = scrollView.getVerticalScrollbarPosition();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showCalendar();
                }
            }, 100);
//            }
        } else if (view.getId() == backDateButtonLayout.getId()) {
            setDefaultColor();
//            if (calendarLayout.getVisibility() == View.GONE) {
            calendar.clearSelection();
            calendar.removeDecorators();
            tanggalKembaliTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
            currentScrollPosition = scrollView.getVerticalScrollbarPosition();
            Collection<CalendarDay> calendarDays = new ArrayList<>();
            calendarDays.add(CalendarDay.from(goDate));
            goSelectedDecorator = new SelectionDecorator(this, SelectionDecorator.CIRCLE_BLUE_DECORATOR, calendarDays);
            if (backDate != null) {
                goSelectedDecorator = new SelectionDecorator(this, SelectionDecorator.CAPSULE_LEFT_BLUE_DECORATOR, calendarDays);
                calendar.setDateSelected(backDate, true);
                Collection<CalendarDay> rangeDays = Utils.getDates(goDate, backDate);
                rangeDecorator = new SelectionDecorator(SearchActivity.this, SelectionDecorator.RANGE_DECORATOR,
                        rangeDays);
                calendar.addDecorator(rangeDecorator);
                calendar.addDecorator(backSelectedDecorator);
            }
            calendar.addDecorator(goSelectedDecorator);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showCalendar();
                }
            }, 100);
//            }
        } else if (view.getId() == deleteBackDateButton.getId()) {
            resetBackDate();
        } else if (view.getId() == revertButton.getId()) {
            RotateAnimation rotate = new RotateAnimation(0, 180,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                    0.5f);

            rotate.setDuration(4000);
            rotate.setRepeatCount(Animation.INFINITE);
            revertButton.setAnimation(rotate);

            AirPort tempAirPort = fromAirPort;
            fromAirPort = toAirPort;
            toAirPort = tempAirPort;

            String tempText = fromTextView.getText().toString();
            fromTextView.setText(toTextView.getText());
            toTextView.setText(tempText);
        }

        if (view.getId() == adultIncreaseButton.getId() || view.getId() == adultDecreaseButton.getId()
                || view.getId() == childIncreaseButton.getId() || view.getId() == childDecreaseButton.getId()
                || view.getId() == infantIncreaseButton.getId() || view.getId() == infantDecreaseButton.getId()) {
            if (Integer.parseInt(adultCountTextView.getText().toString()) == 4) {
                adultIncreaseButton.setImageDrawable(getResources().getDrawable(R.drawable.arrow_up));
            } else {
                adultIncreaseButton.setImageDrawable(getResources().getDrawable(R.drawable.arrow_up_blue));
            }

            if (Integer.parseInt(childCountTextView.getText().toString()) == 4) {
                childIncreaseButton.setImageDrawable(getResources().getDrawable(R.drawable.arrow_up));
            } else {
                childIncreaseButton.setImageDrawable(getResources().getDrawable(R.drawable.arrow_up_blue));
            }

            if (Integer.parseInt(infantCountTextView.getText().toString()) == 4) {
                infantIncreaseButton.setImageDrawable(getResources().getDrawable(R.drawable.arrow_up));
            } else {
                infantIncreaseButton.setImageDrawable(getResources().getDrawable(R.drawable.arrow_up_blue));
            }

//            if (Integer.parseInt(adultCountTextView.getText().toString()) == 1) {
//                adultPassangerTextView.setVisibility(View.GONE);
//                adultDecreaseButton.setImageDrawable(getResources().getDrawable(R.drawable.arrow_down));
//            } else {
            adultPassangerTextView.setVisibility(View.VISIBLE);
            adultPassangerTextView.setText(adultCountTextView.getText() + " dewasa");
            adultDecreaseButton.setImageDrawable(getResources().getDrawable(R.drawable.arrow_down_blue));
//            }

            if (Integer.parseInt(childCountTextView.getText().toString()) == 0) {
                childPassangerTextView.setVisibility(View.GONE);
                childDecreaseButton.setImageDrawable(getResources().getDrawable(R.drawable.arrow_down));
            } else {
                childPassangerTextView.setVisibility(View.VISIBLE);
                childPassangerTextView.setText(", " + childCountTextView.getText() + " anak");
                childDecreaseButton.setImageDrawable(getResources().getDrawable(R.drawable.arrow_down_blue));
            }

            if (Integer.parseInt(infantCountTextView.getText().toString()) == 0) {
                infantPassangerTextView.setVisibility(View.GONE);
                infantDecreaseButton.setImageDrawable(getResources().getDrawable(R.drawable.arrow_down));
            } else {
                infantPassangerTextView.setVisibility(View.VISIBLE);
                infantPassangerTextView.setText(", " + infantCountTextView.getText() + " bayi");
                infantDecreaseButton.setImageDrawable(getResources().getDrawable(R.drawable.arrow_down_blue));
            }
        }


    }

    private void clearFieldFocus() {
        fromAutoCompleteTextView.setVisibility(View.GONE);
        fromTextView.setVisibility(View.VISIBLE);
        toAutoCompleteTextView.setVisibility(View.GONE);
        toTextView.setVisibility(View.VISIBLE);
        fromAutoCompleteTextView.clearFocus();
        toAutoCompleteTextView.clearFocus();

    }

    @Override
    public void onFocusChange(View view, boolean b) {

        setDefaultColor();

        if (view.getId() == fromAutoCompleteTextView.getId()) {
            if (!b) {
                fromAutoCompleteTextView.setVisibility(View.GONE);
                fromTextView.setVisibility(View.VISIBLE);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            } else {
                hideCalendar();
                fromLabelTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                fromAutoCompleteTextView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fromAutoCompleteTextView.requestFocus();
                        scrollView.smoothScrollTo(0, fromLayout.getBottom());
                    }
                }, 100);
            }
        } else if (view.getId() == toAutoCompleteTextView.getId()) {
            if (!b) {
                toAutoCompleteTextView.setVisibility(View.GONE);
                toTextView.setVisibility(View.VISIBLE);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            } else {
                hideCalendar();
                toLabelTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                toAutoCompleteTextView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toAutoCompleteTextView.requestFocus();
                        scrollView.smoothScrollTo(0, toLayout.getBottom());
                    }
                }, 100);
            }
        }
    }

    private void setDefaultColor() {
        fromLabelTextView.setTextColor(defaultColor);
        toLabelTextView.setTextColor(defaultColor);
        tanggalBerangkatTextView.setTextColor(defaultColor);
        tanggalKembaliTextView.setTextColor(defaultColor);
        penumpangTextView.setTextColor(defaultColor);

    }

    public interface OnItemClickListener {
        public void onItemClickListener(AirPort item, int position);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.goRequestsQueue = new HashMap<>();
        hideCalendar();
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
