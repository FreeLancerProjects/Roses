package com.creativeshare.roses.models;

import java.io.Serializable;
import java.util.List;

public class UserModel implements Serializable {


  private int id;
          private String name;
          private String phone_code;
    private String  phone;
    private String  logo;
    private String  banner;
    private String  user_type;
    private String  twitter;
    private String  facebook;
    private String  google;
    private String  instagram;
    private String  address;
    private double latitude;
          private double longitude;
          private int is_company;
          private int is_login;
          private int software_type;
          private int block;
         private String email_verified_at;
         private String created_at;
    private String  updated_at;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone_code() {
        return phone_code;
    }

    public String getPhone() {
        return phone;
    }

    public String getLogo() {
        return logo;
    }

    public String getBanner() {
        return banner;
    }

    public String getUser_type() {
        return user_type;
    }

    public String getTwitter() {
        return twitter;
    }

    public String getFacebook() {
        return facebook;
    }

    public String getGoogle() {
        return google;
    }

    public String getInstagram() {
        return instagram;
    }

    public String getAddress() {
        return address;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getIs_company() {
        return is_company;
    }

    public int getIs_login() {
        return is_login;
    }

    public int getSoftware_type() {
        return software_type;
    }

    public int getBlock() {
        return block;
    }

    public String getEmail_verified_at() {
        return email_verified_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }
}
