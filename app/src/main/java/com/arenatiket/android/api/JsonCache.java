package com.arenatiket.android.api;

import android.content.Context;
import android.content.SharedPreferences;

import com.arenatiket.android.BookingProgressActivity;
import com.arenatiket.android.model.AirPort;
import com.arenatiket.android.model.Nationality;
import com.arenatiket.android.model.Ticket;
import com.arenatiket.android.model.Traveller;
import com.arenatiket.android.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by kahfi on 09/04/16.
 */
public class JsonCache {
    public static ArrayList<Nationality> getNationalities(Context context) {
        ArrayList<Nationality> nationalities = new ArrayList<Nationality>();
        SharedPreferences preferences = context.getSharedPreferences("json", Context.MODE_PRIVATE);
        String airportJson = preferences.getString("nationalities", "[]");
        JSONArray jsonArray = null;
//        Utils.logd("jsonArray " + airportJson);
        try {
            jsonArray = new JSONArray(airportJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject portJson = jsonArray.getJSONObject(i);
                String id = portJson.getString("id");
                String code = portJson.getString("code");
                String name = portJson.getString("name");
                Nationality nationality = new Nationality(id, code, name);
                nationalities.add(nationality);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return nationalities;
    }

    public static ArrayList<String> getNationalityNames(Context context) {
        ArrayList<String> nationalities = new ArrayList<String>();
        SharedPreferences preferences = context.getSharedPreferences("json", Context.MODE_PRIVATE);
        String airportJson = preferences.getString("nationalities", "[]");
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(airportJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject portJson = jsonArray.getJSONObject(i);
                nationalities.add(portJson.getString("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return nationalities;
    }

    public static ArrayList<String> getNationalityIds(Context context) {
        ArrayList<String> nationalities = new ArrayList<String>();
        SharedPreferences preferences = context.getSharedPreferences("json", Context.MODE_PRIVATE);
        String airportJson = preferences.getString("nationalities", "[]");
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(airportJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject portJson = jsonArray.getJSONObject(i);
                nationalities.add(portJson.getString("id"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return nationalities;
    }

    public static ArrayList<AirPort> getAirports(Context context) {
        ArrayList<AirPort> ports = new ArrayList<AirPort>();
        SharedPreferences preferences = context.getSharedPreferences("json", Context.MODE_PRIVATE);
        String airportJson = preferences.getString("airports", "[]");
        JSONArray jsonArray = null;
//        Utils.logd("jsonArray " + airportJson);
        try {
            jsonArray = new JSONArray(airportJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject portJson = jsonArray.getJSONObject(i);
                int id = portJson.getInt("id");
                String code = portJson.getString("code");
                String city = portJson.getString("city");
                int countryId = portJson.getInt("country_id");
                String country = portJson.getString("country");
                String countryCode = portJson.getString("country_code");
                String timezone = portJson.getString("timezone");
                if (code.equals("CGK") || code.equals("JOG")) {
                    Utils.logd("airport code " + i + " " + code);
                }
                AirPort port = new AirPort(id, code, city, countryId, country, countryCode, timezone);
                ports.add(port);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ports;
    }

    public static ArrayList<Traveller> getTravellers(Context context) {
        ArrayList<Traveller> travellers = new ArrayList<Traveller>();
        SharedPreferences preferences = context.getSharedPreferences("json", Context.MODE_PRIVATE);
        String airportJson = preferences.getString("travellers", "{}");
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(airportJson);
            for (Iterator<String> iter = jsonObject.keys(); iter.hasNext(); ) {
                String key = iter.next();
                JSONObject airline = jsonObject.getJSONObject(key);
                JSONObject adultAttr = airline.getJSONObject("adult");
                JSONObject childAttr = airline.getJSONObject("child");
                JSONObject infantAttr = airline.getJSONObject("infant");
                Traveller traveller = new Traveller(key, adultAttr, childAttr, infantAttr);
                travellers.add(traveller);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return travellers;
    }

    public static ArrayList<Traveller> getIntTravellers(Context context) {
        ArrayList<Traveller> travellers = new ArrayList<Traveller>();
        SharedPreferences preferences = context.getSharedPreferences("json", Context.MODE_PRIVATE);
        String airportJson = preferences.getString("int_travellers", "{}");
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(airportJson);
            for (Iterator<String> iter = jsonObject.keys(); iter.hasNext(); ) {
                String key = iter.next();
                JSONObject airline = jsonObject.getJSONObject(key);
                JSONObject adultAttr = airline.getJSONObject("adult");
                JSONObject childAttr = new JSONObject();
                if (!airline.isNull("child")) {
                    childAttr = airline.getJSONObject("child");
                }
                JSONObject infantAttr = new JSONObject();
                if (!airline.isNull("infant")) {
                    infantAttr = airline.getJSONObject("infant");
                }
                Traveller traveller = new Traveller(key, adultAttr, childAttr, infantAttr);
                travellers.add(traveller);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return travellers;
    }

    public static void setTravellers(Context context, String json) {
        SharedPreferences preferences = context.getSharedPreferences("json", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("travellers", json);
        editor.commit();
    }

    public static void setIntTravellers(Context context, String json) {
        SharedPreferences preferences = context.getSharedPreferences("json", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("int_travellers", json);
        editor.commit();
    }

    public static void saveNationalities(Context context, String json) {
        SharedPreferences preferences = context.getSharedPreferences("json", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("nationalities", json);
        editor.commit();
    }


    public static void saveAirports(Context context, String json) {
        SharedPreferences preferences = context.getSharedPreferences("json", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("airports", json);
        editor.commit();
    }

    public static void saveContact(Context context, String json) {
        SharedPreferences preferences = context.getSharedPreferences("json", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("contact", json);
        editor.commit();
    }

    public static JSONObject getContact(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("json", Context.MODE_PRIVATE);
        String text = preferences.getString("contact", "{}");
        try {
            JSONObject ticket = new JSONObject(text);
            return ticket;
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }

    public static void saveReservation(Context context, String json) {
        SharedPreferences preferences = context.getSharedPreferences("json", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("reservation", json);
        editor.commit();
    }

    public static JSONObject getReservation(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("json", Context.MODE_PRIVATE);
        String text = preferences.getString("reservation", "{}");
        try {
            JSONObject reservation = new JSONObject(text);
            return reservation;
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }


    public static void saveSelectedGoTicket(Context context, String json) {
        SharedPreferences preferences = context.getSharedPreferences("json", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("go_ticket", json);
        editor.commit();
    }

    public static JSONObject getSelectedGoTicket(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("json", Context.MODE_PRIVATE);
        String text = preferences.getString("go_ticket", "{}");
        try {
            JSONObject ticket = new JSONObject(text);
            return ticket;
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }

    public static void saveSelectedBackTicket(Context context, String json) {
        SharedPreferences preferences = context.getSharedPreferences("json", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("back_ticket", json);
        editor.commit();
    }

    public static JSONObject getSelectedBackTicket(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("json", Context.MODE_PRIVATE);
        String text = preferences.getString("back_ticket", "{}");
        try {
            JSONObject ticket = new JSONObject(text);
            return ticket;
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }

    public static void saveBanks(Context context, String json) {
        SharedPreferences preferences = context.getSharedPreferences("json", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("banks", json);
        editor.commit();
    }

    public static JSONArray getBanks(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("json", Context.MODE_PRIVATE);
        String text = preferences.getString("banks", "[]");
        try {
            JSONArray reservation = new JSONArray(text);
            return reservation;
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    public static void saveNicepayResponse(Context context, String json) {
        SharedPreferences preferences = context.getSharedPreferences("json", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("nicepay_result", json);
        editor.commit();
    }

    public static JSONObject getNicepayResponse(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("json", Context.MODE_PRIVATE);
        String text = preferences.getString("nicepay_result", "{}");
        try {
            JSONObject reservation = new JSONObject(text);
            return reservation;
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }

    private static float hargaMinBack;
    private static int minTake;
    private static int maxTake;
    static int inc = 0;

    public static List<Ticket> collectData(List<Ticket> tickets, JSONObject progress, int i, HashMap<String, String> flash, String direction) throws JSONException {
        if (!progress.isNull("data")) {
            if (!progress.getJSONObject("data").isNull("status") &&
                    progress.getJSONObject("data").getInt("status") == 1 &&
                    !progress.getJSONObject("data").isNull("results") &&
                    !progress.getJSONObject("data").getJSONObject("results").isNull(direction) &&
                    progress.getJSONObject("data").getJSONObject("results").getJSONArray(direction).length() > 0) {
                JSONArray routes = progress.getJSONObject("data").getJSONObject("results").getJSONArray(direction);

                for (int j = 0; j < routes.length(); j++) {
                    JSONObject route = routes.getJSONObject(j);
                    String flight_num_str = "";
                    String id = route.getString("id");
                    JSONArray flights = route.getJSONArray("flights");
                    JSONObject fares = route.getJSONObject("fares");

                    String totalFlightDuration = "00:00";
                    Date totalFlightTime = new Date();
                    ArrayList<Map<String, String>> opt = new ArrayList<>();

                    String pattern = "HH:mm";
                    SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
                    try {
                        for (int k = 0; k < flights.length(); k++) {
                            JSONObject flight = flights.getJSONObject(k);
                            String flightDuration = flight.getString("flight_duration");
                            Date flightTime = dateFormat.parse(flightDuration);
                            long sum = totalFlightTime.getTime() + flightTime.getTime();
                            totalFlightTime = new Date(sum);
                            if (k == 0) {
                                flight_num_str = flight.getString("flight_num");
                                Utils.logd("flight_num " + flight_num_str);
                            } else {
                                flight_num_str += "|" + flight.getString("flight_num");
                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    totalFlightDuration = dateFormat.format(totalFlightTime);


                    String transit = "";
                    String travelDuration = "";
                    if (flights.length() == 1) {
                        transit = "Langsung";


                        String[] wPer = flights.getJSONObject(0).getString("flight_duration").split(":");
                        travelDuration = wPer[0] == "00" ? wPer[1] + "menit" : wPer[0] + "j " + wPer[1] + "m";
                    } else {
                        transit = (flights.length() - 1) + " Transit";

                        String departTime = flights.getJSONObject(0).getString("depart_datetime");
                        String arriveTime = flights.getJSONObject(flights.length() - 1).getString("arrive_datetime");

                        String departTimezone = flights.getJSONObject(0).getString("depart_timezone");
                        String arriveTimezone = flights.getJSONObject(flights.length() - 1).getString("arrive_timezone");

                        travelDuration = flights.getJSONObject(0).getString("flight_duration");

                    }

                    int unix_depart_time = (Integer.parseInt(flights.getJSONObject(0).getString("depart_time").substring(0, 2)) * 3600)
                            + (Integer.parseInt(flights.getJSONObject(0).getString("depart_time").substring(3, 5)) * 60);

                    if (!route.getJSONObject("fares").isNull("pro") &&
                            !route.getJSONObject("fares").getJSONObject("pro").isNull("adult")) {
                        JSONObject prices_ad = route.getJSONObject("fares").getJSONObject("pro").getJSONObject("adult");
                        float total_ad = priceTotal(prices_ad, false);
                        JSONObject prices_ch = !route.getJSONObject("fares").getJSONObject("pro").isNull("child") ?
                                route.getJSONObject("fares").getJSONObject("pro").getJSONObject("child") : null;
                        JSONObject prices_in = !route.getJSONObject("fares").getJSONObject("pro").isNull("infant") ?
                                route.getJSONObject("fares").getJSONObject("pro").getJSONObject("infant") : null;
                        float total_ch = prices_ch != null ? priceTotal(prices_ch, false) : 0;
                        float total_in = prices_ch != null ? priceTotal(prices_in, false) : 0;

                        String letter = "undefined";

                        ArrayList<String> letProArr = new ArrayList<>();
                        for (int l = 0; l < flights.length(); l++) {
                            JSONObject flight = flights.getJSONObject(l);
                            if (!flight.isNull("extended_attribute")) {
                                if (!flight.getJSONObject("extended_attribute").isNull("pro")) {
                                    letProArr.add(flight.getJSONObject("extended_attribute").getJSONObject("pro").getString("class_letter"));
                                }
                            }
                        }

                        Collections.sort(letProArr);
                        if (letProArr.size() > 0) {
                            letter = letProArr.get(0);
                        }

                        String choose = flights.getJSONObject(0).getString("airlines") + ":PRO:" + priceValue(prices_ad) + ";"
                                + priceValue(prices_ch) + ';' + priceValue(prices_in) + ":"
                                + flight_num_str + ":" + flights.getJSONObject(0).getString("depart_city")
                                + ":" + flights.getJSONObject(0).getString("depart_datetime") + ':'
                                + flights.getJSONObject(0).getString("depart_port") + ":" + flights.getJSONObject(flights.length() - 1).getString("arrive_city")
                                + ":" + flights.getJSONObject(flights.length() - 1).getString("arrive_datetime") + ":"
                                + flights.getJSONObject(flights.length() - 1).getString("arrive_port") + ":promo:"
                                + (flights.length() - 1) + ":" + transit + ":" + travelDuration + ':' + letter;
//                        Utils.logd("pro choose: " + choose);

                        Map<String, String> values = new HashMap<>();
                        values.put("choose", choose);
                        values.put("total", total_ad + "");
                        values.put("harga_diskon", Math.round(total_ad + Float.parseFloat(prices_ad.getString("discount"))) + "");
                        values.put("harga_diskon_ch", Math.round(total_ch + (prices_ch != null ? Float.parseFloat(prices_ch.getString("discount")) : 0)) + "");
                        values.put("harga_diskon_in", Math.round(total_in + (prices_in != null ? Float.parseFloat(prices_in.getString("discount")) : 0)) + "");
                        values.put("kelas", "promo");
                        values.put("harga_short", Math.round(total_ad + Float.parseFloat(prices_ad.getString("discount"))) + "");
                        values.put("harga_tax", route.getJSONObject("fares").getJSONObject("pro").getJSONObject("adult").getString("tax"));
                        values.put("letter", letter);
                        values.put("travel_duration", totalFlightDuration);
                        opt.add(values);

                        Ticket ticket = new Ticket(id, fares, flights, values);
                        tickets.add(ticket);

                        for (int k = tickets.size() - 1; k > 0; k--) {
                            Ticket prevItem = tickets
                                    .get(k - 1);
                            float prevPrice = Float.parseFloat(prevItem.getValues().get("harga_diskon"));
                            float currentPrice = Float.parseFloat(ticket
                                    .getValues().get("harga_diskon"));
                            if (prevPrice > currentPrice) {
                                tickets.set(k - 1, ticket);
                                tickets.set(k, prevItem);
                            }
                        }

                    } else {
//                        Map<String, String> values = new HashMap<>();
//                        values.put("choose", flights.getJSONObject(0).getString("airlines") + ":PRO:" + "10000"
//                                + ":" + flight_num_str);
//                        values.put("harga", "");
//                        values.put("kelas", "promo");
//                        values.put("letter", null);
//                        opt.add(values);
                    }

                    if (!route.getJSONObject("fares").isNull("eco") &&
                            !route.getJSONObject("fares").getJSONObject("eco").isNull("adult")) {
                        JSONObject prices_ad = route.getJSONObject("fares").getJSONObject("eco").getJSONObject("adult");
                        float total_ad = priceTotal(prices_ad, false);
                        JSONObject prices_ch = !route.getJSONObject("fares").getJSONObject("eco").isNull("child") ?
                                route.getJSONObject("fares").getJSONObject("eco").getJSONObject("child") : null;
                        JSONObject prices_in = !route.getJSONObject("fares").getJSONObject("eco").isNull("infant") ?
                                route.getJSONObject("fares").getJSONObject("eco").getJSONObject("infant") : null;
                        float total_ch = prices_ch != null ? priceTotal(prices_ch, false) : 0;
                        float total_in = prices_ch != null ? priceTotal(prices_in, false) : 0;

                        String letter = "undefined";
                        ArrayList<String> letEcoArr = new ArrayList<>();
                        for (int l = 0; l < flights.length(); l++) {
                            JSONObject flight = flights.getJSONObject(l);
                            if (!flight.isNull("extended_attribute")) {
                                if (!flight.getJSONObject("extended_attribute").isNull("eco")) {
                                    letEcoArr.add(flight.getJSONObject("extended_attribute").getJSONObject("eco").getString("class_letter"));
                                }
                            }
                        }
                        Collections.sort(letEcoArr);
                        if (letEcoArr.size() > 0) {
                            letter = letEcoArr.get(0);
                        }
                        String choose = flights.getJSONObject(0).getString("airlines") + ":ECO:" + priceValue(prices_ad) + ";"
                                + priceValue(prices_ch) + ';' + priceValue(prices_in) + ":"
                                + flight_num_str + ":" + flights.getJSONObject(0).getString("depart_city")
                                + ":" + flights.getJSONObject(0).getString("depart_datetime") + ':'
                                + flights.getJSONObject(0).getString("depart_port") + ":" + flights.getJSONObject(flights.length() - 1).getString("arrive_city")
                                + ":" + flights.getJSONObject(flights.length() - 1).getString("arrive_datetime") + ":"
                                + flights.getJSONObject(flights.length() - 1).getString("arrive_port") + ":ekonomi:"
                                + (flights.length() - 1) + ":" + transit + ":" + travelDuration + ':' + letter;
//                        Utils.logd("eco choose: " + choose);

                        Map<String, String> values = new HashMap<>();
                        values.put("choose", choose);
                        values.put("total", total_ad + "");
                        values.put("harga_diskon", Math.round(total_ad + Float.parseFloat(prices_ad.getString("discount"))) + "");
                        values.put("harga_diskon_ch", Math.round(total_ch + (prices_ch != null ? Float.parseFloat(prices_ch.getString("discount")) : 0)) + "");
                        values.put("harga_diskon_in", Math.round(total_in + (prices_in != null ? Float.parseFloat(prices_in.getString("discount")) : 0)) + "");
                        values.put("kelas", "ekonomi");
                        values.put("harga_short", Math.round(total_ad + Float.parseFloat(prices_ad.getString("discount"))) + "");
                        values.put("harga_tax", route.getJSONObject("fares").getJSONObject("eco").getJSONObject("adult").getString("tax"));
                        values.put("letter", letter);
                        values.put("travel_duration", totalFlightDuration);
                        opt.add(values);


                        Ticket ticket = new Ticket(id, fares, flights, values);
                        tickets.add(ticket);

                        for (int k = tickets.size() - 1; k > 0; k--) {
                            Ticket prevItem = tickets
                                    .get(k - 1);
                            float prevPrice = Float.parseFloat(prevItem.getValues().get("harga_diskon"));
                            float currentPrice = Float.parseFloat(ticket
                                    .getValues().get("harga_diskon"));
                            if (prevPrice > currentPrice) {
                                tickets.set(k - 1, ticket);
                                tickets.set(k, prevItem);
                            }
                        }


                    } else {
//                        Map<String, String> values = new HashMap<>();
//                        values.put("choose", flights.getJSONObject(0).getString("airlines") + ":ECO:" + ""
//                                + ":" + flight_num_str);
//                        values.put("harga", "");
//                        values.put("kelas", "ekonomi");
//                        values.put("letter", null);
//                        opt.add(values);

                    }


                    if (!route.getJSONObject("fares").isNull("bus") && !route.getJSONObject("fares").getJSONObject("bus").isNull("adult")) {
                        JSONObject prices_ad = route.getJSONObject("fares").getJSONObject("bus").getJSONObject("adult");
                        float total_ad = priceTotal(prices_ad, false);
                        JSONObject prices_ch = !route.getJSONObject("fares").getJSONObject("bus").isNull("child") ?
                                route.getJSONObject("fares").getJSONObject("bus").getJSONObject("child") : null;
                        JSONObject prices_in = !route.getJSONObject("fares").getJSONObject("bus").isNull("infant") ?
                                route.getJSONObject("fares").getJSONObject("bus").getJSONObject("infant") : null;
                        float total_ch = prices_ch != null ? priceTotal(prices_ch, false) : 0;
                        float total_in = prices_ch != null ? priceTotal(prices_in, false) : 0;


                        String letter = "undefined";
                        ArrayList<String> letBusArr = new ArrayList<>();
                        for (int l = 0; l < flights.length(); l++) {
                            JSONObject flight = flights.getJSONObject(l);
                            if (!flight.isNull("extended_attribute")) {
                                if (!flight.getJSONObject("extended_attribute").isNull("bus")) {
                                    letBusArr.add(flight.getJSONObject("extended_attribute").getJSONObject("bus").getString("class_letter"));
                                }
                            }
                        }

                        Collections.sort(letBusArr);
                        if (letBusArr.size() > 0) {
                            letter = letBusArr.get(0);
                        }
                        String choose = flights.getJSONObject(0).getString("airlines") + ":BUS:" + priceValue(prices_ad) + ";"
                                + priceValue(prices_ch) + ';' + priceValue(prices_in) + ":"
                                + flight_num_str + ":" + flights.getJSONObject(0).getString("depart_city")
                                + ":" + flights.getJSONObject(0).getString("depart_datetime") + ':'
                                + flights.getJSONObject(0).getString("depart_port") + ":" + flights.getJSONObject(flights.length() - 1).getString("arrive_city")
                                + ":" + flights.getJSONObject(flights.length() - 1).getString("arrive_datetime") + ":"
                                + flights.getJSONObject(flights.length() - 1).getString("arrive_port") + ":bisnis:"
                                + (flights.length() - 1) + ":" + transit + ":" + travelDuration + ':' + letter;
//                        Utils.logd("bus choose: " + choose);

                        Map<String, String> values = new HashMap<>();
                        values.put("choose", choose);
                        values.put("total", total_ad + "");
                        values.put("harga_diskon", Math.round(total_ad + Float.parseFloat(prices_ad.getString("discount"))) + "");
                        values.put("harga_diskon_ch", Math.round(total_ch + (prices_ch != null ? Float.parseFloat(prices_ch.getString("discount")) : 0)) + "");
                        values.put("harga_diskon_in", Math.round(total_in + (prices_in != null ? Float.parseFloat(prices_in.getString("discount")) : 0)) + "");
                        values.put("kelas", "bisnis");
                        values.put("harga_short", Math.round(total_ad + Float.parseFloat(prices_ad.getString("discount"))) + "");
                        values.put("harga_tax", route.getJSONObject("fares").getJSONObject("bus").getJSONObject("adult").getString("tax"));
                        values.put("letter", letter);
                        values.put("travel_duration", totalFlightDuration);
                        opt.add(values);

                        Ticket ticket = new Ticket(id, fares, flights, values);
                        tickets.add(ticket);

                        for (int k = tickets.size() - 1; k > 0; k--) {
                            Ticket prevItem = tickets
                                    .get(k - 1);
                            float prevPrice = Float.parseFloat(prevItem.getValues().get("harga_diskon"));
                            float currentPrice = Float.parseFloat(ticket
                                    .getValues().get("harga_diskon"));
                            if (prevPrice > currentPrice) {
                                tickets.set(k - 1, ticket);
                                tickets.set(k, prevItem);
                            }
                        }

                    } else {
//                        Map<String, String> values = new HashMap<>();
//                        values.put("choose", flights.getJSONObject(0).getString("airlines") + ":BUS:" + ""
//                                + ":" + flight_num_str);
//                        values.put("harga", "");
//                        values.put("kelas", "bisnis");
//                        values.put("letter", null);
//                        opt.add(values);
                    }

                }
            }
        }
        return tickets;
    }


    private static float priceTotal(JSONObject prices, boolean isWithDiscount) {
        if (prices != null) {
//            Utils.logd("prices not null");
            try {
                int total = Integer.parseInt(prices.getString("basic")) + Integer.parseInt(prices.getString("tax"))
                        + Integer.parseInt(prices.getString("fuel")) + Integer.parseInt(prices.getString("adm"))
                        + Integer.parseInt(prices.getString("iwjr"));
                if (isWithDiscount)
                    return total + Integer.parseInt(prices.getString("discount"));
                else
                    return total;
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        } else
            return 0;
    }

    private static String priceValue(JSONObject prices) {
        if (prices != null) {
//            Utils.logd("prices not null");
            try {
                return prices.getString("basic") + "," + prices.getString("tax") + ","
                        + prices.getString("fuel") + "," + prices.getString("adm")
                        + "," + prices.getString("iwjr") + "," + prices.getString("discount");
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        } else
            return "";
    }


    private static String fomatMoney(String delimiter, String arg2, String arg3) {
        return "";
    }

    public static void addToContacts(Context context, JSONObject json) {
        SharedPreferences preferences = context.getSharedPreferences("json", Context.MODE_PRIVATE);
        String text = preferences.getString("contacts", "[]");
        JSONArray contacts = new JSONArray();
        try {
            contacts = new JSONArray(text);
            for (int i = 0; i < contacts.length(); i++) {
                JSONObject contact = contacts.getJSONObject(i);
                if (contact.getString("name").equals(json.getString("name"))) {
                    contacts.remove(i);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SharedPreferences.Editor editor = preferences.edit();
        contacts.put(json);
        editor.putString("contacts", contacts.toString());
        editor.commit();
    }

    public static JSONArray getContacts(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("json", Context.MODE_PRIVATE);
        String text = preferences.getString("contacts", "[]");
        try {
            JSONArray ticket = new JSONArray(text);
            return ticket;
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }
}
