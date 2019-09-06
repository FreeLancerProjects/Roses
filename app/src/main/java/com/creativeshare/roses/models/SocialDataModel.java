package com.creativeshare.roses.models;

import java.io.Serializable;

public class SocialDataModel implements Serializable {

    private String snapchat;
    private String twitter;
    private String instagram;

    public String getSnapchat() {
        return snapchat;
    }

    public String getTwitter() {
        return twitter;
    }

    public String getInstagram() {
        return instagram;
    }
}
