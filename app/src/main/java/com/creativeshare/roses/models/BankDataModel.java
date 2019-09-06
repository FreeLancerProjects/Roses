package com.creativeshare.roses.models;

import java.io.Serializable;
import java.util.List;

public class BankDataModel implements Serializable {

    private List<BankModel> data;

    public List<BankModel> getData() {
        return data;
    }

    public class BankModel implements Serializable
    {
        private int id;
                private String account_name;
                private String account_number;
                private String IBN;
                private String bank_name;
                private String created_at;
                private String updated_at;

        public int getId() {
            return id;
        }

        public String getAccount_name() {
            return account_name;
        }

        public String getAccount_number() {
            return account_number;
        }

        public String getIBN() {
            return IBN;
        }

        public String getBank_name() {
            return bank_name;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }
    }
}
