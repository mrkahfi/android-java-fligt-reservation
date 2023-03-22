package com.arenatiket.android.utils;

import android.telephony.TelephonyManager;

import com.arenatiket.android.R;
import com.arenatiket.android.model.Airline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kahfi on 14/04/16.
 */
public class Constants {

    public static final int DEFAULT_MAX_RETRIES = 3;
    public static final String MERCHANT_CODE = "2749";
//    public static final String MERCHANT_CODE = "349";
    public static int MY_SOCKET_TIMEOUT_MS = 5000;

    public static int DEFAULT_FROM_AIRPORT_POSITION = 107;
    public static int DEFAULT_TO_AIRPORT_POSITION = 203;
    public static final String URL_CHARGING_DOKU_DAN_CC = "http://crm.doku.com/doku-library-staging/example-payment-mobile/merchant-example.php";
    public static final String URL_CHARGING_MANDIRI_CLICKPAY = "http://crm.doku.com/doku-library-staging/example-payment-mobile/merchant-mandiri-example.php";
    public static String URL_REQUEST_VACODE = "http://demomerchant.doku.com/va_generate_staging.php";

    public static Map<String, String> baggages = new DefaultHashMap<String, String>("0") {{
        put("AK", "0");
        put("D7", "0");
        put("FD", "0");
        put("I5", "0");
        put("ID", "20");
        put("IL", "20");
        put("IW", "10");
        put("JT", "20");
        put("KD", "15");
        put("MZ", "20");
        put("PQ", "0");
        put("OD", "15");
        put("QG", "20");
        put("QZ", "15");
        put("RI", "15");
        put("SL", "15");
        put("SJ", "20");
        put("SY", "15/20");
        put("XJ", "0");
        put("XN", "20");
        put("Z2", "0");
        put("GA", "20");
        put("MV", "15");
        put("XT", "15");
        put("IN", "20");
        put("JQ", "0");
        put("3K", "0");
        put("GK", "0");
        put("BL", "0");
    }};

    public static Map<String, String> meals = new DefaultHashMap<String, String>("0") {{
        put("AK", "0");
        put("D7", "0");
        put("FD", "0");
        put("I5", "0");
        put("ID", "1");
        put("IL", "0");
        put("IW", "0");
        put("JT", "0");
        put("KD", "0");
        put("MZ", "0");
        put("PQ", "0");
        put("OD", "0");
        put("QG", "0");
        put("QZ", "0");
        put("RI", "0");
        put("SL", "0");
        put("SJ", "1");
        put("SY", "0");
        put("XJ", "0");
        put("XN", "1");
        put("Z2", "0");
        put("GA", "1");
        put("JQ", "0");
        put("3K", "0");
        put("GK", "0");
        put("BL", "0");
    }};
    public static Map<String, Integer> hmin = new HashMap<String, Integer>() {{
        put("GAR", 0);
        put("LIO", 0);
        put("BAT", 0);
        put("SRI", 0);
        put("CIT", 0);
        put("AIR", 0);
        put("MER", 0);
        put("TIG", 0);
        put("IAT", 0);
        put("EXP", 0);
        put("TRI", 0);
        put("KAL", 0);
        put("AVI", 0);
        put("JET", 0);
    }};

    public static ArrayList<Integer> hminTransit = new ArrayList<Integer>() {{
        add(0);
        add(0);
        add(0);
    }};

    public static Map<String, Integer> hminKelas = new HashMap<String, Integer>() {{
        put("promo", 0);
        put("ekonomi", 0);
        put("bisnis", 0);
    }};
    public static Map<String, ArrayList<HashMap<String, Integer>>> toSort = new HashMap<String, ArrayList<HashMap<String, Integer>>>() {{
        put("go", new ArrayList());
        put("back", new ArrayList());
    }};

    public static Map<String, Airline> airlines = new HashMap<String, Airline>() {{
        put("LIO", new Airline("LIO", "Lion Air", R.drawable.lion));
        put("SRI", new Airline("SRI", "Sriwijaya Air", R.drawable.sriwijaya));
        put("GAR", new Airline("GAR", "Garuda Indonesia", R.drawable.garuda));
        put("AIR", new Airline("AIR", "Air Asia", R.drawable.airasia));
        put("CIT", new Airline("CIT", "Citilink", R.drawable.citilink));
        put("EXP", new Airline("EXP", "Express Air", R.drawable.xpress));
        put("TRI", new Airline("TRI", "Trigana", R.drawable.trigana));
        put("KAL", new Airline("KAL", "Kalstar", R.drawable.kalstar));
        put("AVI", new Airline("AVI", "Aviastar", R.drawable.avia));
        put("BAT", new Airline("BAT", "Batik Air", R.drawable.batik));
        put("MER", new Airline("MER", "Merpati", R.drawable.merpati));
        put("JET", new Airline("JET", "Jetstar Airways", R.drawable.jetstar));
    }};

    public static Map<String, String> classes = new HashMap<String, String>() {{
        put("promo", "Promo");
        put("ekonomi", "Ekonomi");
        put("bisnis", "Bisnis");
    }};

    public static Map<String, String> transits = new HashMap<String, String>() {{
        put("1", "1 kali");
        put("2", "2 kali");
    }};
//    public static String baseApiUrl = "http://arenatiket.dev.arenatiket.com/";
    public static String baseApiUrl = "https://arenatiket.com/";
}
