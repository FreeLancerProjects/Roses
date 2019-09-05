package com.creativeshare.roses.models;

public class Send_Data {
    private static int market_id;
private static  int cat_id;
private static int type;
    public static int getMarket_id() {
        return market_id;
    }

    public static void setMarket_id(int market_id) {
        Send_Data.market_id = market_id;
    }

    public static int getCat_id() {
        return cat_id;
    }

    public static void setCat_id(int cat_id) {
        Send_Data.cat_id = cat_id;
    }

    public static int getType() {
        return type;
    }

    public static void setType(int type) {
        Send_Data.type = type;
    }
}
