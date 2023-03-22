package com.arenatiket.android.adapter;

/**
 * Created by kahfi on 30/01/16.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.arenatiket.android.fragment.PassangerInfoFragment;
import com.arenatiket.android.fragment.SearchResultFragment;
import com.arenatiket.android.fragment.Tab1Fragment;
import com.arenatiket.android.fragment.Tab2Fragment;
import com.arenatiket.android.model.Passanger;
import com.arenatiket.android.utils.Utils;

import java.util.ArrayList;


/**
 * Created by kahfi on 21-01-2015.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public static final int SEARCH_RESULT = 55;
    public static final int SEARCH_OPTION = 56;
    public static final int PASSANGER_INFO = 57;
    private final int style;
    private ArrayList<String> dates;
    private ArrayList<Passanger> passangers;
    private ArrayList<CharSequence> mSubtitles;
    private final int mode;
    ArrayList<CharSequence> Titles; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs = 2; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
    private Bundle bundle;


    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm, ArrayList<CharSequence> mTitles, ArrayList<CharSequence> mSubtitles,
                            ArrayList<String> dates, int mNumbOfTabsumb,
                            int style, int mode, Bundle bundle) {
        super(fm);

        this.Titles = mTitles;
        this.mSubtitles = mSubtitles;
        this.NumbOfTabs = mNumbOfTabsumb;
        this.dates = dates;
        this.style = style;
        this.mode = mode;
        this.bundle = bundle;
    }

    public ViewPagerAdapter(FragmentManager fm, ArrayList<CharSequence> mTitles, ArrayList<CharSequence> mSubtitles,
                            ArrayList<Passanger> passangers, int mNumbOfTabsumb,
                            int style, int mode) {
        super(fm);

        this.Titles = mTitles;
        this.mSubtitles = mSubtitles;
        this.NumbOfTabs = mNumbOfTabsumb;
        this.mode = mode;
        this.passangers = passangers;
        this.style = style;
    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {
        Utils.logd("position " + position);
        switch (style) {
            case SEARCH_RESULT:
                Utils.logd("goDate" + position + " : " + dates.get(position));
                SearchResultFragment fragment = SearchResultFragment.newInstance(1, position, dates.get(position), mode, bundle);
                return fragment;
            case SEARCH_OPTION:
                if (position == 0) // if the position is 0 we are returning the First tab
                {
                    Tab1Fragment tab1 = new Tab1Fragment();
                    return tab1;
                } else             // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
                {
                    Tab2Fragment tab2 = Tab2Fragment.newInstance(bundle);
                    return tab2;
                }
            case PASSANGER_INFO:
                PassangerInfoFragment passangerInfoFragment = PassangerInfoFragment.newInstance(passangers.get(position),
                        passangers.get(position).getType(), position);
                return passangerInfoFragment;

            default:
                fragment = SearchResultFragment.newInstance(1, position, dates.get(position), mode, bundle);
                return fragment;

        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles.get(position);
    }

    @Override
    public int getCount() {
        return NumbOfTabs;
    }

    public CharSequence getPageSubTitle(int i) {
        return mSubtitles.get(i);
    }
}