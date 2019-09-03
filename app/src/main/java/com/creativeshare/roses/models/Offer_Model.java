package com.creativeshare.roses.models;

import java.io.Serializable;
import java.util.List;

public class Offer_Model implements Serializable {

        private int current_page;
        private List<Data> data;

    public int getCurrent_page() {
        return current_page;
    }

    public List<Data> getData() {
        return data;
    }

    public class  Data implements Serializable
        {
           private int id;
               private String ar_title;
            private String en_title;
            private String image;
                private int type;
                private double value;
               private  int is_active;
               private int from_date;
                private int to_date;
               private int market_id;
                private int product_id;
                private String created_at;
            private String updated_at;
            private String product_en_title;
            private String product_ar_title;
            private String product_price;
            private String product_en_description;
            private String product_ar_description;
            private String name_of_market;
            private String logo_of_market;
            private String banner_of_market;
            private String phone_code_of_market;
            private String phone_of_market;
            private String address_of_market;
            private List<Product_images> product_images;

            public int getId() {
                return id;
            }

            public String getAr_title() {
                return ar_title;
            }

            public String getEn_title() {
                return en_title;
            }

            public String getImage() {
                return image;
            }

            public int getType() {
                return type;
            }

            public double getValue() {
                return value;
            }

            public int getIs_active() {
                return is_active;
            }

            public int getFrom_date() {
                return from_date;
            }

            public int getTo_date() {
                return to_date;
            }

            public int getMarket_id() {
                return market_id;
            }

            public int getProduct_id() {
                return product_id;
            }

            public String getCreated_at() {
                return created_at;
            }

            public String getUpdated_at() {
                return updated_at;
            }

            public String getProduct_en_title() {
                return product_en_title;
            }

            public String getProduct_ar_title() {
                return product_ar_title;
            }

            public String getProduct_price() {
                return product_price;
            }

            public String getProduct_en_description() {
                return product_en_description;
            }

            public String getProduct_ar_description() {
                return product_ar_description;
            }

            public String getName_of_market() {
                return name_of_market;
            }

            public String getLogo_of_market() {
                return logo_of_market;
            }

            public String getBanner_of_market() {
                return banner_of_market;
            }

            public String getPhone_code_of_market() {
                return phone_code_of_market;
            }

            public String getPhone_of_market() {
                return phone_of_market;
            }

            public String getAddress_of_market() {
                return address_of_market;
            }

            public List<Product_images> getProduct_images() {
                return product_images;
            }

            public class Product_images implements Serializable
            {
               private int id;
                   private int product_id;
                    private String image;
                   private String created_at;
                    private String updated_at;

                public int getId() {
                    return id;
                }

                public int getProduct_id() {
                    return product_id;
                }

                public String getImage() {
                    return image;
                }

                public String getCreated_at() {
                    return created_at;
                }

                public String getUpdated_at() {
                    return updated_at;
                }
            }
        }
}
