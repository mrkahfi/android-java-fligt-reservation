package com.arenatiket.android.model;

/**
 * Created by kahfi on 01/04/16.
 */
public class AirPort {
    private int id;
    private String name;
    private String code;
    private String city;
    private int country_id;
    private String country;
    private String country_code;
    private String timezone;

    public AirPort(int id, String code, String city, int country_id, String country, String country_code, String timezone) {
        this.id = id;
        this.code = code;
        this.city = city;
        this.country_id = country_id;
        this.country = country;
        this.country_code = country_code;
        this.timezone = timezone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCity() {
        return city != null ? city : "";
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getCountry_id() {
        return country_id;
    }

    public void setCountry_id(int country_id) {
        this.country_id = country_id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
}
