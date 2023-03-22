package com.arenatiket.android.customlayout;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arenatiket.android.R;

/**
 * Created by kahfi on 25/05/16.
 */
public class ChildView extends LinearLayout {

    private final TextView birthDateTextView;
    private TextView nameTextView;

    public ChildView(Context context) {
        super(context);

        View.inflate(context, R.layout.item_passanger, this);
        nameTextView = (TextView) findViewById(R.id.nameTextView);
        birthDateTextView = (TextView) findViewById(R.id.birthDateTextView);
    }

    public void setText(String name, String birthDate) {
        nameTextView.setText(name);
        birthDateTextView.setText(birthDate);
    }
}