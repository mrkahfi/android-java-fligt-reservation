package com.arenatiket.android;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.JsonObjectRequest;
import com.arenatiket.android.adapter.MyItemRecyclerViewAdapter;
import com.arenatiket.android.adapter.ViewPagerAdapter;
import com.arenatiket.android.customlayout.SlidingTabLayout;
import com.arenatiket.android.fragment.SearchResultFragment;
import com.arenatiket.android.model.Airline;
import com.arenatiket.android.model.Ticket;
import com.arenatiket.android.utils.Constants;
import com.arenatiket.android.utils.MyApplication;
import com.arenatiket.android.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class BackSearchResultActivity extends AppCompatActivity implements SearchResultFragment.OnListFragmentInteractionListener {

    public static final int SEARCH_OPTION = 4;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    ArrayList<CharSequence> Titles = new ArrayList<>();
    ArrayList<CharSequence> subTitles = new ArrayList<>();
    ArrayList<String> backDates = new ArrayList<>();
    int Numboftabs = 1;
    private Toolbar toolbar;
    private JsonObjectRequest request;
    private int minPrice = 0;
    private int maxPrice = 0;
    private double minFilterPrice = 0;
    private double maxFilterPrice = 0;
    private int sortMode;
    private ImageView airlineLogoImageView;
    private TextView arrivalPortTextView;
    private TextView departTimeTextView;
    private TextView departPortTextView;
    private TextView arriveTimeTextView;
    private TextView hargaTotalTextView;
    private TextView hargaDiskon1TextView;
    private TextView hargaDiskon2TextView;
    private TextView keberangkatanTextView;
    private TextView departChangeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_search_result);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        TextView toolbarTitleTextView = (TextView) toolbar.findViewById(R.id.toolbarTitleTextView);
        TextView toolbarSubTitleTextView = (TextView) toolbar.findViewById(R.id.toolbarSubTitleTextView);
        toolbarTitleTextView.setText("Pilih Kepulangan");

        toolbarSubTitleTextView.setSelected(true);
        toolbarSubTitleTextView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        pager = (ViewPager) findViewById(R.id.pager);
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        keberangkatanTextView = (TextView) findViewById(R.id.keberangkatanTextView);
        departChangeTextView = (TextView) findViewById(R.id.departChangeTextView);
        airlineLogoImageView = (ImageView) findViewById(R.id.airlineLogoImageView);
        departPortTextView = (TextView) findViewById(R.id.departPortTextView);
        arrivalPortTextView = (TextView) findViewById(R.id.arrivalPortTextView);
        departTimeTextView = (TextView) findViewById(R.id.departTimeTextView);
        arriveTimeTextView = (TextView) findViewById(R.id.arriveTimeTextView);
        hargaTotalTextView = (TextView) findViewById(R.id.hargaTotalTextView);
        hargaTotalTextView.setPaintFlags(hargaTotalTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        hargaDiskon1TextView = (TextView) findViewById(R.id.hargaDiskon1TextView);
        hargaDiskon2TextView = (TextView) findViewById(R.id.hargaDiskon2TextView);

        Bundle bundle = getIntent().getBundleExtra("searchParams");
        String backDatesText = bundle.getString("backDatesText");
        String backDateText = bundle.getString("backDate");
        String goDateText = getIntent().getStringExtra("selectedGoDate");
        String fromCity = bundle.getString("fromCity");
        String toCity = bundle.getString("toCity");
        String adultCount = bundle.getString("adultCount");
        String childCount = bundle.getString("childCount");
        String infantCount = bundle.getString("infantCount");

        String[] splitted = goDateText.split("-");

        toolbarSubTitleTextView.setText(toCity + " ke " + fromCity + " - " + adultCount + " dewasa"
                + (!childCount.equals("0") ? ", " + childCount + " anak, " : "")
                + (!infantCount.equals("0") ? ", " + infantCount + " bayi, " : ""));

        departChangeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        int defaultIndex = 0;
        try {
            JSONArray backDateJsonArray = new JSONArray(backDatesText);
            for (int i = 0; i < backDateJsonArray.length(); i++) {
                String dateText = backDateJsonArray.getString(i);
                backDates.add(dateText);
                if (backDateText.equals(dateText)) {
                    defaultIndex = i;
                }
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date date = format.parse(dateText);
                String formattedDate = DateFormat.format("dd MMM", date).toString();
                Titles.add(formattedDate.toUpperCase());
                subTitles.add("...");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        MyApplication.defaultBackIndex = defaultIndex;

        Utils.logd("default index " + defaultIndex);

        adapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles, subTitles, backDates, Titles.size(),
                ViewPagerAdapter.SEARCH_RESULT, MyItemRecyclerViewAdapter.MODE_BACK, bundle);

        pager.setAdapter(adapter);
        pager.setCurrentItem(defaultIndex);
        pager.setOffscreenPageLimit(backDates.size());

        tabs.setDistributeEvenly(true);
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.colorPrimary);
            }
        });
        tabs.setCustomTabView(R.layout.tab_layout, R.id.tabDateTextView, R.id.tabPriceTextView);
        tabs.setViewPager(pager);

        if (MyApplication.selectedDepartItem != null) {
            try {
                Ticket ticket = MyApplication.selectedDepartItem;

                String id = ticket.getId();
                JSONArray flights = ticket.getFlights();
                JSONObject fares = ticket.getFares();
                Map<String, String> values = ticket.getValues();


                String departPort = flights.getJSONObject(0).getString("depart_port");
                String arrivePort = flights.getJSONObject(flights.length() - 1).getString("arrive_port");
                String departCity = flights.getJSONObject(0).getString("depart_city");
                String arriveCity = flights.getJSONObject(flights.length() - 1).getString("arrive_city");
                String departTime = flights.getJSONObject(0).getString("depart_time");
                String arriveTime = flights.getJSONObject(flights.length() - 1).getString("arrive_time");
                goDateText = flights.getJSONObject(0).getString("depart_datetime");
                String[] splitted1 = goDateText.split(" ");
                splitted = splitted1[0].split("-");
                String monthName = Utils.getMonthShortName(Integer.valueOf(splitted[1]) - 1);
                keberangkatanTextView.setText("Keberangkatan - " + splitted[2] + " " + monthName + " " + splitted[0]);

                departPortTextView.setText(departPort);
                arrivalPortTextView.setText(arrivePort);
                hargaTotalTextView.setText(Utils.getMoneyFormat(Math.round(Float.parseFloat(values.get("total"))), true));
                String hargaDiskon = values.get("harga_diskon");
                String hargaDiskon1 = Utils.getMoneyFormat(Math.round(Float.parseFloat(hargaDiskon.substring(0, hargaDiskon.length() - 3))), false);
                String hargaDiskon2 = hargaDiskon.substring(hargaDiskon.length() - 3);
                hargaDiskon1TextView.setText(hargaDiskon1);
                hargaDiskon2TextView.setText("." + hargaDiskon2);
                Airline airline = Constants.airlines.get(flights.getJSONObject(0).getString("airlines"));
                airlineLogoImageView.setImageDrawable(getResources().getDrawable(airline.getLogoResourceId()));
                departTimeTextView.setText(departTime);
                arriveTimeTextView.setText(arriveTime);

                String numDigit = flights.getJSONObject(0).getString("flight_num").substring(0, 2);
                if (numDigit.contains("ID")) {
                    airlineLogoImageView.setImageDrawable(getResources().getDrawable(R.drawable.batik));
                } else if (numDigit.contains("IW")) {
                    airlineLogoImageView.setImageDrawable(getResources().getDrawable(R.drawable.win));
                } else if (numDigit.contains("IN")) {
                    airlineLogoImageView.setImageDrawable(getResources().getDrawable(R.drawable.namair));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onListFragmentInteraction(Ticket item) {
    }

    @Override
    public void onTitleChangeListener(int position, String title) {
//        Toast.makeText(this, title, Toast.LENGTH_SHORT).show();
//        Utils.logd("price activity: " + title);
        subTitles.set(position > 0 ? position : 0, title);
        adapter.notifyDataSetChanged();
        tabs.setViewPager(pager);
    }

    @Override
    public void onMinPriceChangeListener(int harga_diskon) {
        this.minPrice = harga_diskon;
    }

    @Override
    public void onMaxPriceChangeListener(int harga_diskon) {
        this.maxPrice = harga_diskon;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_result_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_options:
                Intent intent = new Intent(this, SearchOptionActivity.class);
                intent.putExtra("minPrice", minPrice);
                intent.putExtra("maxPrice", maxPrice);
                startActivityForResult(intent, SEARCH_OPTION);
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
        }
        return false;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Utils.logd("onActivityResult on ResultActivity");

        if (requestCode == SEARCH_OPTION && resultCode == RESULT_OK) {
            minFilterPrice = data.getIntExtra("minPrice", 0);
            maxFilterPrice = data.getIntExtra("maxPrice", 0);

            sortMode = data.getIntExtra("sortMode", 0);
        }
    }

}
