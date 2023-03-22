package com.arenatiket.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.arenatiket.android.adapter.MyItemRecyclerViewAdapter;
import com.arenatiket.android.adapter.ViewPagerAdapter;
import com.arenatiket.android.customlayout.SlidingTabLayout;
import com.arenatiket.android.fragment.SearchResultFragment;
import com.arenatiket.android.model.Ticket;
import com.arenatiket.android.utils.MyApplication;
import com.arenatiket.android.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SearchResultActivity extends AppCompatActivity implements SearchResultFragment.OnListFragmentInteractionListener {

    public static final int SEARCH_OPTION = 4;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    ArrayList<CharSequence> Titles = new ArrayList<>();
    ArrayList<CharSequence> subTitles = new ArrayList<>();
    ArrayList<String> goDates = new ArrayList<>();
    int Numboftabs = 1;
    private Toolbar toolbar;
    private JsonObjectRequest request;
    private double minPrice = 0;
    private double maxPrice = 0;
    private double minFilterPrice = 0;
    private double maxFilterPrice = 0;
    private int sortMode;
    private String toolbarSubTitle ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        TextView toolbarSubTitleTextView = (TextView) toolbar.findViewById(R.id.toolbarSubTitleTextView);

        toolbarSubTitleTextView.setSelected(true);
        toolbarSubTitleTextView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        pager = (ViewPager) findViewById(R.id.pager);
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);

        Bundle bundle = getIntent().getBundleExtra("searchParams");
        String goDatesText = bundle.getString("goDatesText");
        String goDateText = bundle.getString("goDate");
        String fromCity = bundle.getString("fromCity");
        String toCity = bundle.getString("toCity");
        String adultCount = bundle.getString("adultCount");
        String childCount = bundle.getString("childCount");
        String infantCount = bundle.getString("infantCount");

        toolbarSubTitle = fromCity + " ke " + toCity + " - " + adultCount + " dewasa"
                + (!childCount.equals("0") ? childCount + " anak, " : "")
                + (!infantCount.equals("0") ? infantCount + " bayi, " : "");
        toolbarSubTitleTextView.setText(toolbarSubTitle);

        int defaultIndex = 0;
        try {
            JSONArray goDateJsonArray = new JSONArray(goDatesText);
            for (int i = 0; i < goDateJsonArray.length(); i++) {
                String dateText = goDateJsonArray.getString(i);
                goDates.add(dateText);
                if (goDateText.equals(dateText)) {
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
        MyApplication.defaultIndex = defaultIndex;

        Utils.logd("default index " + defaultIndex);

        adapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles, subTitles, goDates, Titles.size(),
                ViewPagerAdapter.SEARCH_RESULT, MyItemRecyclerViewAdapter.MODE_GO, bundle);

        pager.setAdapter(adapter);
        pager.setCurrentItem(defaultIndex);
        pager.setOffscreenPageLimit(goDates.size());

        tabs.setDistributeEvenly(true);
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.colorPrimary);
            }
        });
        tabs.setCustomTabView(R.layout.tab_layout, R.id.tabDateTextView, R.id.tabPriceTextView);
        tabs.setViewPager(pager);
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                intent.putExtra("toolbar_subtitle", toolbarSubTitle);
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
