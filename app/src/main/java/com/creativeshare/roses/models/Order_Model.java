package com.creativeshare.roses.models;

import java.io.Serializable;
import java.util.List;

public class Order_Model implements Serializable {

        private int current_page;
        private List<Data> data;

    public int getCurrent_page() {
        return current_page;
    }

    public List<Data> getData() {
        return data;
    }

    public class Data implements Serializable
        {
            private int id;
            private int type;
            private int user_id;
            private int market_id;
                private String title;
                private Long next_date;
                private double longitude;
                private double latitude;
                private String address;
                private int status;
                private int order_tracker;
            private int offer_id;
                private int total_cost;
                private int rating;
              private int is_deleted;
                private String created_at;
                private String updated_at;
               private String user_name;
                private String user_image;
                private String market_name;
                private String market_image;
                private String market_phone_code;
               private String market_phone;
               private String market_address;
               private List<OrderDetails> orderDetails;
            private List<Services> services;

            public int getId() {
                return id;
            }

            public int getType() {
                return type;
            }

            public int getUser_id() {
                return user_id;
            }

            public int getMarket_id() {
                return market_id;
            }

            public String getTitle() {
                return title;
            }

            public Long getNext_date() {
                return next_date;
            }

            public double getLongitude() {
                return longitude;
            }

            public double getLatitude() {
                return latitude;
            }

            public String getAddress() {
                return address;
            }

            public int getStatus() {
                return status;
            }

            public int getOrder_tracker() {
                return order_tracker;
            }

            public int getOffer_id() {
                return offer_id;
            }

            public int getTotal_cost() {
                return total_cost;
            }

            public int getRating() {
                return rating;
            }

            public int getIs_deleted() {
                return is_deleted;
            }

            public String getCreated_at() {
                return created_at;
            }

            public String getUpdated_at() {
                return updated_at;
            }

            public String getUser_name() {
                return user_name;
            }

            public String getUser_image() {
                return user_image;
            }

            public String getMarket_name() {
                return market_name;
            }

            public String getMarket_image() {
                return market_image;
            }

            public String getMarket_phone_code() {
                return market_phone_code;
            }

            public String getMarket_phone() {
                return market_phone;
            }

            public String getMarket_address() {
                return market_address;
            }

            public List<OrderDetails> getOrderDetails() {
                return orderDetails;
            }

            public List<Services> getServices() {
                return services;
            }

            public class OrderDetails implements Serializable
            {
                private int id;
                    private int product_id;
                   private int order_id;
                   private int offer_id;
                   private int amount;
                    private double total_price;
                    private String des;
                    private String created_at;
                    private String updated_at;
                    private String product_ar_title;
                    private String product_en_title;
                    private String product_default_image;
                    private String product_ar_des;
                   private String product_en_des;
                   private String product_price;
                   private List<product_images> product_images;

                public int getId() {
                    return id;
                }

                public int getProduct_id() {
                    return product_id;
                }

                public int getOrder_id() {
                    return order_id;
                }

                public int getOffer_id() {
                    return offer_id;
                }

                public int getAmount() {
                    return amount;
                }

                public double getTotal_price() {
                    return total_price;
                }

                public String getDes() {
                    return des;
                }

                public String getCreated_at() {
                    return created_at;
                }

                public String getUpdated_at() {
                    return updated_at;
                }

                public String getProduct_ar_title() {
                    return product_ar_title;
                }

                public String getProduct_en_title() {
                    return product_en_title;
                }

                public String getProduct_default_image() {
                    return product_default_image;
                }

                public String getProduct_ar_des() {
                    return product_ar_des;
                }

                public String getProduct_en_des() {
                    return product_en_des;
                }

                public String getProduct_price() {
                    return product_price;
                }

                public List<OrderDetails.product_images> getProduct_images() {
                    return product_images;
                }

                public String getCat_en_name() {
                    return cat_en_name;
                }

                public String getCat_ar_name() {
                    return cat_ar_name;
                }

                public class  product_images implements Serializable
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
                private String cat_en_name;
                    private String cat_ar_name;


            }
           public class Services implements Serializable{

                private int id;
                   private int order_id;
                   private int service_id;
                    private String created_at;
                    private String updated_at;
                    private String Service_ar_title;
                   private String Service_en_title;

               public int getId() {
                   return id;
               }

               public int getOrder_id() {
                   return order_id;
               }

               public int getService_id() {
                   return service_id;
               }

               public String getCreated_at() {
                   return created_at;
               }

               public String getUpdated_at() {
                   return updated_at;
               }

               public String getService_ar_title() {
                   return Service_ar_title;
               }

               public String getService_en_title() {
                   return Service_en_title;
               }
           }
        }
}
