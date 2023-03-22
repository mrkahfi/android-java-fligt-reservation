package com.arenatiket.android.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.arenatiket.android.R;
import com.arenatiket.android.utils.Filter;
import com.arenatiket.android.utils.Utils;

/**
 * Created by kahfi on 21-01-2015.
 */
public class Tab1Fragment extends Fragment {

    private String orderId;
    private String status;
    private RadioButton lowestPriceRadioButton;
    private RadioButton departDatePriceRadioButton;
    private RadioButton arrivalDateRadioButton;
    private RadioButton durationRadioButton;
    private RadioGroup pointsRadioGroup;
    private OnRadioGroupItemCheckedListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab1, container, false);
        pointsRadioGroup = (RadioGroup) v.findViewById(R.id.points_radio_group);
        lowestPriceRadioButton = (RadioButton) v.findViewById(R.id.lowestPriceRadioButton);
        departDatePriceRadioButton = (RadioButton) v.findViewById(R.id.departDateRadioButton);
        arrivalDateRadioButton = (RadioButton) v.findViewById(R.id.arrivalDateRadioButton);
        durationRadioButton = (RadioButton) v.findViewById(R.id.durationRadioButton);

        switch (Filter.sortMode) {
            case SearchResultFragment.LOWEST_PRICE:
                pointsRadioGroup.check(R.id.lowestPriceRadioButton);
                break;
            case SearchResultFragment.DEPART_TIME:
                pointsRadioGroup.check(R.id.departDateRadioButton);
                break;
            case SearchResultFragment.ARRIVE_TIME:
                pointsRadioGroup.check(R.id.arrivalDateRadioButton);
                break;
            case SearchResultFragment.DURATION:
                pointsRadioGroup.check(R.id.durationRadioButton);
                break;
            default:
                pointsRadioGroup.check(R.id.lowestPriceRadioButton);
                break;
        }

        pointsRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                View radioButton = radioGroup.findViewById(i);
                int idx = radioGroup.indexOfChild(radioButton);
                Utils.logd("onCheckedChanged " + idx);
                mListener.onRadioGroupItemChecked(idx);
            }
        });
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public interface OnRadioGroupItemCheckedListener {
        public void onRadioGroupItemChecked(int index);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnRadioGroupItemCheckedListener) {
            mListener = (OnRadioGroupItemCheckedListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnRadioGroupItemCheckedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}