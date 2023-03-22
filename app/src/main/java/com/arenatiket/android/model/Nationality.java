package com.arenatiket.android.model;

/**
 * Created by kahfi on 27/08/16.
 */
public class Nationality {
    private final String id;
    private final String code;
    private final String name;

    public Nationality(String id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
