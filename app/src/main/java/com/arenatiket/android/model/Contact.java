package com.arenatiket.android.model;

import java.io.Serializable;

/**
 * Created by kahfi on 23/05/16.
 */
public class Contact implements Serializable {

    private String title;
    private String titleText;
    private String name;
    private String email;
    private String phone;

    public Contact(String title, String titleText, String name, String email, String phone) {
        this.title = title;
        this.titleText = titleText;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
