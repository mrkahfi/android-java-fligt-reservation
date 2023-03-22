package com.arenatiket.android.utils;

import android.content.Context;

import com.arenatiket.android.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by kahfi on 13/04/16.
 */
public class SelectionDecorator implements DayViewDecorator {

    public static final int SELECTED_DECORATOR = 0;
    public static final int RANGE_DECORATOR = 1;
    public static final int CIRCLE_BLUE_DECORATOR = 2;
    public static final int CAPSULE_LEFT_BLUE_DECORATOR = 3;
    public static final int CAPSULE_RIGHT_BLUE_DECORATOR = 4;
    private final int type;
    private final HashSet<CalendarDay> dates;
    private final Context context;

    public SelectionDecorator(Context context, int type, Collection<CalendarDay> dates) {
        this.context = context;
        this.type = type;
        this.dates = new HashSet<>(dates);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        if (type == RANGE_DECORATOR) {
            view.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.rectangle_transparent_blue_tile));
        } else if (type == SELECTED_DECORATOR) {
            view.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.circle_blue_tile));
        } else if (type == CIRCLE_BLUE_DECORATOR) {
            view.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.circle_transparent_blue_tile));
        } else if (type == CAPSULE_LEFT_BLUE_DECORATOR) {
            view.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.capsule_blue_left_tile));
        }else if (type == CAPSULE_RIGHT_BLUE_DECORATOR) {
            view.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.capsule_blue_right_tile));
        }
    }
}