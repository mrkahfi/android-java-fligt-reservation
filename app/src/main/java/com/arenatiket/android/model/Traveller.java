package com.arenatiket.android.model;

import org.json.JSONObject;

/**
 * Created by kahfi on 12/9/16.
 */
public class Traveller {
    private String airlines;
    private JSONObject adultAttrJson;
    private JSONObject childAttrJson;
    private JSONObject infantAttrJson;

    public Traveller(String airlines, JSONObject adultAttrJson, JSONObject childAttrJson, JSONObject infantAttrJson) {
        this.airlines = airlines;
        this.adultAttrJson = adultAttrJson;
        this.childAttrJson = childAttrJson;
        this.infantAttrJson = infantAttrJson;
    }

    public Traveller() {

    }

    public String getAirlines() {
        return airlines;
    }

    public void setAirlines(String airlines) {
        this.airlines = airlines;
    }

    public JSONObject getAdultAttrJson() {
        return adultAttrJson;
    }

    public void setAdultAttrJson(JSONObject adultAttrJson) {
        this.adultAttrJson = adultAttrJson;
    }

    public JSONObject getChildAttrJson() {
        return childAttrJson;
    }

    public void setChildAttrJson(JSONObject childAttrJson) {
        this.childAttrJson = childAttrJson;
    }

    public JSONObject getInfantAttrJson() {
        return infantAttrJson;
    }

    public void setInfantAttrJson(JSONObject infantAttrJson) {
        this.infantAttrJson = infantAttrJson;
    }
}
