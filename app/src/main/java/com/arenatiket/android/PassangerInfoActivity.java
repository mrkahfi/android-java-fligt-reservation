package com.arenatiket.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arenatiket.android.adapter.ViewPagerAdapter;
import com.arenatiket.android.customlayout.SlidingTabLayout;
import com.arenatiket.android.fragment.PassangerInfoFragment;
import com.arenatiket.android.model.Contact;
import com.arenatiket.android.model.Passanger;
import com.arenatiket.android.service.BookingTimerService;
import com.arenatiket.android.utils.MyApplication;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class PassangerInfoActivity extends AppCompatActivity implements PassangerInfoFragment.OnNextClickedListener {

    private TextView countDownTextView;
    private Toolbar toolbar;
    private EditText nameEditText;
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
    private ViewPagerAdapter adapter;
    private ViewPager pager;
    private SlidingTabLayout tabs;

    ArrayList<CharSequence> titles = new ArrayList<>();
    ArrayList<CharSequence> subTitles = new ArrayList<>();
    int numboftabs = 1;
    private Bundle bundle;
    private Contact contact;
    private BroadcastReceiver countingReceiver;
//    private ArrayList<Passanger> passangers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passanger_info);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        TextView titleTextView = (TextView) toolbar.findViewById(R.id.toolbarTitleTextView);
        titleTextView.setText("Informasi Penumpang");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);
        countDownTextView = (TextView) findViewById(R.id.countDownTextView);

        bundle = getIntent().getBundleExtra("searchParams");
        contact = (Contact) getIntent().getSerializableExtra("contact_person");

        int adultCount = Integer.parseInt(bundle.getString("adultCount"));
        int childCount = Integer.parseInt(bundle.getString("childCount"));
        int infantCount = Integer.parseInt(bundle.getString("infantCount"));

        MyApplication.isPassengerValid = false;

        MyApplication.passangers = new ArrayList<Passanger>();

        for (int i = 1; i <= adultCount; i++) {
            titles.add("DEWASA " + i);
            if (i == 1) {
                subTitles.add(contact.getName());
                MyApplication.passangers.add(new Passanger("adult", contact.getName()));
            } else {
                subTitles.add("");
                MyApplication.passangers.add(new Passanger("adult"));
            }
        }

        for (int i = 1; i <= childCount; i++) {
            titles.add("ANAK " + i);
            subTitles.add("");
            MyApplication.passangers.add(new Passanger("child"));
        }

        for (int i = 1; i <= infantCount; i++) {
            titles.add("BAYI " + i);
            subTitles.add("");
            MyApplication.passangers.add(new Passanger("infant"));
        }

        numboftabs = titles.size();

        adapter = new ViewPagerAdapter(getSupportFragmentManager(), titles, subTitles, MyApplication.passangers,
                titles.size(), ViewPagerAdapter.PASSANGER_INFO, -1);

        pager.setAdapter(adapter);

        tabs.setDistributeEvenly(true);

        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.colorPrimary);
            }
        });
        tabs.setCustomTabView(R.layout.tab_layout, R.id.tabDateTextView, R.id.tabPriceTextView);

        tabs.setViewPager(pager);
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
    public void onNextClickedListener() {
        if (pager.getCurrentItem() < numboftabs - 1) {
            pager.setCurrentItem(pager.getCurrentItem() + 1);
        } else {
            Intent intent = new Intent(this, BookingSummaryActivity.class);
            intent.putExtra("searchParams", bundle);
            intent.putExtra("contact_person", contact);
            startActivity(intent);
        }
    }

    @Override
    public void onTitleChangeListener(String tabTitle, int position) {
        subTitles.set(position, tabTitle);
        adapter.notifyDataSetChanged();
        tabs.setViewPager(pager);
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
