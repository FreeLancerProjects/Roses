package com.creativeshare.roses.models;

import java.io.Serializable;
import java.util.List;

public class Add_Order_Model implements Serializable {


    private int type;
    private int user_id;
    private int market_id;
    private String title;
    private long next_date;
    private double longitude;
    private double latitude;
    private String address;
    private int offer_id;
    private double total_cost;
    private List<Order_details> order_details;
    private List<Services> services;
    private String name;

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getMarket_id() {
        return market_id;
    }

    public void setMarket_id(int market_id) {
        this.market_id = market_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getNext_date() {
        return next_date;
    }

    public void setNext_date(long next_date) {
        this.next_date = next_date;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getOffer_id() {
        return offer_id;
    }

    public void setOffer_id(int offer_id) {
        this.offer_id = offer_id;
    }

    public double getTotal_cost() {
        return total_cost;
    }

    public void setTotal_cost(double total_cost) {
        this.total_cost = total_cost;
    }

    public List<Order_details> getOrder_details() {
        return order_details;
    }

    public void setOrder_details(List<Order_details> order_details) {
        this.order_details = order_details;
    }

    public List<Services> getServices() {
        return services;
    }

    public void setServices(List<Services> services) {
        this.services = services;
    }

    public void setname(String name_of_market) {
        this.name=name_of_market;
    }

    public static class Order_details implements Serializable {
        private int product_id;
        private int offer_id;
        private int amount;
        private double total_price;
        private String des;
private String image;
private String ar_name;
        private String en_name;

        public String getAr_name() {
            return ar_name;
        }

        public void setAr_name(String ar_name) {
            this.ar_name = ar_name;
        }

        public String getEn_name() {
            return en_name;
        }

        public void setEn_name(String en_name) {
            this.en_name = en_name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getProduct_id() {
            return product_id;
        }

        public void setProduct_id(int product_id) {
            this.product_id = product_id;
        }

        public int getOffer_id() {
            return offer_id;
        }

        public void setOffer_id(int offer_id) {
            this.offer_id = offer_id;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public double getTotal_price() {
            return total_price;
        }

        public void setTotal_price(double total_price) {
            this.total_price = total_price;
        }

        public String getDes() {
            return des;
        }

        public void setDes(String des) {
            this.des = des;
        }
    }

    public static class Services implements Serializable {
        private int service_id;

        public int getService_id() {
            return service_id;
        }

        public void setService_id(int service_id) {
            this.service_id = service_id;
        }
    }


}
