package com.creativeshare.roses.models;

import java.io.Serializable;
import java.util.List;

public class Catogries_Model implements Serializable {
private List<Data> data;


    public List<Data> getData() {
        return data;
    }

    public static class Data implements Serializable
    {
       private int id;
           private String ar_title;
            private String en_title;
           private String image;
            private String is_deleted;
            private String created_at;
            private String updated_at;

        public Data(int id, String ar_title, String en_title) {
            this.id = id;
            this.ar_title = ar_title;
            this.en_title = en_title;
        }

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

        public String getIs_deleted() {
            return is_deleted;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }
    }
}

