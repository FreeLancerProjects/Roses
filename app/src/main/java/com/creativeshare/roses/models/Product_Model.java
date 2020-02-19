package com.creativeshare.roses.models;

import java.io.Serializable;
import java.util.List;

public class Product_Model  implements Serializable {
    private int current_page;
    private List<Product_Model.Data> data;

    public int getCurrent_page() {
        return current_page;
    }

    public List<Product_Model.Data> getData() {
        return data;
    }

    public class Data implements Serializable {
        private int id;
        private String slug;
        private String ar_title;
        private String en_title;
        private String ar_des;
        private String en_des;
        private String price;
        private String image;
        private int market_id;
        private int cat_id;
        private String created_at;
        private String updated_at;
        private String cat_en_name;
        private String cat_ar_name;
        private String name_of_market;
        private String logo_of_market;
        private String banner_of_market;
        private String phone_of_market;
        private String address_of_market;
        private List<Offer_Model.Data.Product_images> product_images;

        public int getId() {
            return id;
        }

        public String getSlug() {
            return slug;
        }

        public String getAr_title() {
            return ar_title;
        }

        public String getEn_title() {
            return en_title;
        }

        public String getAr_des() {
            return ar_des;
        }

        public String getEn_des() {
            return en_des;
        }

        public String getPrice() {
            return price;
        }

        public String getImage() {
            return image;
        }

        public int getMarket_id() {
            return market_id;
        }

        public int getCat_id() {
            return cat_id;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public String getCat_en_name() {
            return cat_en_name;
        }

        public String getCat_ar_name() {
            return cat_ar_name;
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

        public String getPhone_of_market() {
            return phone_of_market;
        }

        public String getAddress_of_market() {
            return address_of_market;
        }

        public List<Offer_Model.Data.Product_images> getProduct_images() {
            return product_images;
        }
private Market market;

        public Market getMarket() {
            return market;
        }

        public class Product_images implements Serializable {
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

    public class Market implements Serializable {
                private String name;

        public String getName() {
            return name;
        }
    }
}