

package com.arenatiket.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.arenatiket.android.adapter.ViewPagerAdapter;
import com.arenatiket.android.customlayout.SlidingTabLayout;
import com.arenatiket.android.fragment.SearchResultFragment;
import com.arenatiket.android.fragment.Tab1Fragment;
import com.arenatiket.android.model.Ticket;
import com.arenatiket.android.utils.Filter;
import com.arenatiket.android.utils.MyApplication;
import com.arenatiket.android.utils.Utils;

import java.util.ArrayList;

public class SearchOptionActivity extends AppCompatActivity implements SearchResultFragment.OnListFragmentInteractionListener,
        Tab1Fragment.OnRadioGroupItemCheckedListener {

    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    ArrayList<CharSequence> Titles = new ArrayList<>();
    int Numboftabs = 2;
    private Toolbar toolbar;
    private int sortMode;
    private int minPrice;
    private int maxPrice;
    private String toolbarSubtitle ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_options);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        TextView toolbarSubTitleTextView = (TextView) toolbar.findViewById(R.id.toolbarSubTitleTextView);

        toolbarSubTitleTextView.setSelected(true);
        toolbarSubTitleTextView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbarSubtitle = getIntent().getStringExtra("toolbar_subtitle");
        toolbarSubTitleTextView.setText(toolbarSubtitle);

        pager = (ViewPager) findViewById(R.id.pager);
        MyApplication.nextActivity = "search_option";

        Intent intent = getIntent();
        minPrice = intent.getIntExtra("minPrice", 0);
        maxPrice = intent.getIntExtra("maxPrice", 0);

        Bundle bundle = new Bundle();
        bundle.putInt("minPrice", minPrice);
        bundle.putInt("maxPrice", maxPrice);

        Titles.add("URUTKAN");
        Titles.add("FILTER");

        adapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles, null, new ArrayList<String>(),
                Numboftabs, ViewPagerAdapter.SEARCH_OPTION, -1, bundle);
        pager.setAdapter(adapter);

        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);

        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.colorPrimary);
            }
        });

        tabs.setViewPager(pager);
    }



    @Override
    public void onListFragmentInteraction(Ticket item) {

    }

    @Override
    public void onTitleChangeListener(int position, String title) {
    }

    @Override
    public void onMinPriceChangeListener(int harga_diskon) {

    }

    @Override
    public void onMaxPriceChangeListener(int harga_diskon) {

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
                onBackPressed();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
        }
        return false;

    }

    @Override
    public void onRadioGroupItemChecked(int index) {
        Utils.logd("onRadioGroupItemChecked " + index);
        sortMode = index;
        Filter.sortMode = index;
    }

    @Override
    public void onBackPressed() {
        Utils.logd("onBackPressed on SearchOptionActivity");
        Intent resultIntent = new Intent();
        resultIntent.putExtra("sortMode", sortMode);
        resultIntent.putExtra("minPrice", minPrice);
        resultIntent.putExtra("maxPrice", maxPrice);
        setResult(Activity.RESULT_OK, resultIntent);
        super.onBackPressed();
    }
}
