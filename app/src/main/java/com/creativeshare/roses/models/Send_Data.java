package com.creativeshare.roses.models;

public class Send_Data {
    private static int market_id;
private static  int cat_id;
private static int type;
private static Order_Model.Data data;
    private static int index;
private static double lat,lang;

    public static double getLat() {
        return lat;
    }

    public static void setLat(double lat) {
        Send_Data.lat = lat;
    }

    public static double getLang() {
        return lang;
    }

    public static void setLang(double lang) {
        Send_Data.lang = lang;
    }

    public static int getIndex() {
        return index;
    }

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

    public static Order_Model.Data getData() {
        return data;
    }

    public static void setData(Order_Model.Data data) {
        Send_Data.data = data;
    }

    public static void setindex(int i) {
        Send_Data.index=i;
    }
}
