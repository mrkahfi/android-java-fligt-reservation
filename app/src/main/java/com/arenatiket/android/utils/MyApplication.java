package com.arenatiket.android.utils;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.arenatiket.android.model.Contact;
import com.arenatiket.android.model.Passanger;
import com.arenatiket.android.model.Ticket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kahfi on 11/05/16.
 */
public class MyApplication extends Application {
    public static HashMap<Integer, JsonObjectRequest> goRequestsQueue = new HashMap<>();
    public static HashMap<Integer, JsonObjectRequest> backRequestsQueue = new HashMap<>();
    public static RequestQueue goQueue;
//    public static RequestQueue backQueue;
    public static int defaultIndex;
    public static int selectedGoIndex;

    public static List<Ticket> backItems = new ArrayList<Ticket>();
    public static int defaultBackIndex;

    public static Ticket selectedDepartItem;
    public static Ticket selectedReturnItem;
    public static boolean isPassengerValid;

    public static ArrayList<Passanger> passangers = new ArrayList<>();
    public static HashMap<Integer, String> trackIds = new HashMap<>();
    public static HashMap<Integer, Long> trackTimes = new HashMap<>();

    public static boolean isInternationalFlight;

    public static String nextActivity = "";
}
