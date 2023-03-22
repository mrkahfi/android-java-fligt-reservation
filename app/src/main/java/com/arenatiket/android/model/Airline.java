package com.arenatiket.android.model;

import java.util.ArrayList;

/**
 * Created by kahfi on 04/05/16.
 */
public class Airline {
    private String code;
    private String name;
    private int logoResourceId;
    private ArrayList<Traveller> travellers = new ArrayList<>();

    public Airline(String code) {
        this.code = code;
    }

    public Airline(String code, String name, int logoResourceId) {
        this.code = code;
        this.name = name;
        this.logoResourceId = logoResourceId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLogoResourceId() {
        return logoResourceId;
    }

    public void setLogoResourceId(int logoResourceId) {
        this.logoResourceId = logoResourceId;
    }
}
