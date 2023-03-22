package com.arenatiket.android.utils;

import com.arenatiket.android.model.Airline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kahfi on 14/05/16.
 */
public class Filter {

    public static int maxPrice;
    public static int minPrice;
    public static HashMap<String, String> airlines = new HashMap<>();
    public static HashMap<String, String> classes = new HashMap<>();
    public static HashMap<String, String> transits = new HashMap<>();
    public static boolean directFlight;

    public static int sortMode;

    public static ArrayList<String> mSelectedFlightItems = new ArrayList() {{
        for (Map.Entry<String, Airline> entry : Constants.airlines.entrySet()) {
            Airline value = entry.getValue();
            add(value.getName());
        }
    }};

    public static ArrayList<String> mSelectedFlightItemCodes = new ArrayList<String>() {{
        for (Map.Entry<String, Airline> entry : Constants.airlines.entrySet()) {
            Airline value = entry.getValue();
            add(value.getCode());
        }
    }};

    public static ArrayList<String> mSelectedClassItems = new ArrayList<String>() {{
        for (Map.Entry<String, String> entry : Constants.classes.entrySet()) {
            String value = entry.getValue();
            add(value);
        }
    }};

    public static ArrayList<String> mSelectedClassItemCodes = new ArrayList<String>() {{
        for (Map.Entry<String, String> entry : Constants.classes.entrySet()) {
            String value = entry.getKey();
            add(value);
        }
    }};

    public static ArrayList<String> mSelectedTransitItems = new ArrayList<String>() {{
        for (Map.Entry<String, String> entry : Constants.transits.entrySet()) {
            String value = entry.getValue();
            add(value);
        }
    }};

    public static ArrayList<String> mSelectedTransitItemCodes = new ArrayList<String>() {{
        for (Map.Entry<String, String> entry : Constants.transits.entrySet()) {
            String value = entry.getKey();
            add(value);
        }
    }};

    public static int minIndex;
    public static int maxIndex;
}
