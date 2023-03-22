package com.arenatiket.android.model;

import java.io.Serializable;

/**
 * Created by kahfi on 23/05/16.
 */
public class Passanger implements Serializable {
    private String type;
    private String title;
    private String name;
    private String birthDate;
    private String idCardNum;
    private String passportId;
    private String passportDate;
    private String ifantAssocTravellerCtr;
    private String passportIssuingCountry;
    private String nationality;
    private String titleText;
    private String genderId;

    public Passanger(String title, String titleText, String name, String birthDate, String phone) {
        this.titleText = titleText;
        this.title = title;
        this.name = name;
        this.birthDate = birthDate;
        this.nationality = phone;
        this.idCardNum = "";
        this.ifantAssocTravellerCtr = "";
    }

    public Passanger(String type) {
        this.type = type;
        this.title = "";
        this.name = "";
        this.birthDate = "";
        this.nationality = "";
        this.idCardNum = "";
        this.ifantAssocTravellerCtr = "";
    }

    public Passanger(String type, String name) {
        this.type = type;
        this.title = "";
        this.name = name;
        this.birthDate = "";
        this.nationality = "";
        this.idCardNum = "";
        this.ifantAssocTravellerCtr = "";
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        switch (title) {
            case "1":
                this.genderId = "1";
            case "2":
                this.genderId = "2";
            case "5":
                this.genderId = "2";
            case "3":
                this.genderId = "2";
            case "6":
                this.genderId = "1";
        }
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getPassportId() {
        return passportId;
    }

    public void setPassportId(String passportId) {
        this.passportId = passportId;
    }

    public String getPassportDate() {
        return passportDate == null ? "" : passportDate;
    }

    public void setPassportDate(String passportDate) {
        this.passportDate = passportDate;
    }

    public String getPassportIssuingCountry() {
        return passportIssuingCountry;
    }

    public void setPassportIssuingCountry(String passportIssuingCountry) {
        this.passportIssuingCountry = passportIssuingCountry;
    }

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    public String getGenderId() {
        switch (title) {
            case "1":
                return "1";
            case "2":
                return "2";
            case "5":
                return "2";
            case "3":
                return "2";
            case "6":
                return "1";
        }
        return genderId;
    }

    public String getIdCardNum() {
        return idCardNum;
    }

    public void setIdCardNum(String idCardNum) {
        this.idCardNum = idCardNum;
    }

    public void setGenderId(String genderId) {
        this.genderId = genderId;
    }

    public String getIfantAssocTravellerCtr() {
        return ifantAssocTravellerCtr;
    }

    public void setIfantAssocTravellerCtr(String ifantAssocTravellerCtr) {
        this.ifantAssocTravellerCtr = ifantAssocTravellerCtr;
    }
}
