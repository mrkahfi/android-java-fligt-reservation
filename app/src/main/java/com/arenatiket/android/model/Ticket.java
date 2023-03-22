package com.arenatiket.android.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by kahfi on 03/05/16.
 */

public class Ticket {
    private Map<String, String> values;
    public String id;
    public String content;
    public String details;

    public JSONObject fares;
    public JSONArray flights;

    private String choose;

    private int total;
    private int priceCh;
    private int priceIn;
    private int hargaDiskon;
    private String kelas;
    private int hargaShort;
    private int hargaTax;
    private String letter;

    public Ticket(String id, String content, String details) {
        this.id = id;
        this.content = content;
        this.details = details;
    }

    public Ticket(String id, JSONObject fares, JSONArray flights, Map<String, String> values) {
        this.id = id;
        this.fares = fares;
        this.flights = flights;
        this.values = values;
    }

    public Ticket(String id, String choose, JSONArray flights) {
        this.id = id;
        this.fares = fares;
        this.flights = flights;
    }

    public Ticket(String id, String choose, int total, int hargaDiskon, String kelas, int hargaShort, int hargaTax, String letter) {
        this.id = id;
        this.total = total;
        this.hargaDiskon = hargaDiskon;
        this.kelas = kelas;
        this.hargaShort = hargaShort;
        this.hargaTax = hargaTax;
        this.letter = letter;
        this.choose = choose;
        this.id = id;
    }



    @Override
    public String toString() {
        return id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public JSONObject getFares() {
        return fares;
    }

    public void setFares(JSONObject fares) {
        this.fares = fares;
    }

    public JSONArray getFlights() {
        return flights;
    }

    public void setFlights(JSONArray flights) {
        this.flights = flights;
    }

    public String getChoose() {
        return choose;
    }

    public void setChoose(String choose) {
        this.choose = choose;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPriceCh() {
        return priceCh;
    }

    public void setPriceCh(int priceCh) {
        this.priceCh = priceCh;
    }

    public int getPriceIn() {
        return priceIn;
    }

    public void setPriceIn(int priceIn) {
        this.priceIn = priceIn;
    }

    public int getHargaDiskon() {
        return hargaDiskon;
    }

    public void setHargaDiskon(int hargaDiskon) {
        this.hargaDiskon = hargaDiskon;
    }

    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }

    public int getHargaShort() {
        return hargaShort;
    }

    public void setHargaShort(int hargaShort) {
        this.hargaShort = hargaShort;
    }

    public int getHargaTax() {
        return hargaTax;
    }

    public void setHargaTax(int hargaTax) {
        this.hargaTax = hargaTax;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public Map<String, String> getValues() {
        return values;
    }

    public void setValues(Map<String, String> values) {
        this.values = values;
    }
}
