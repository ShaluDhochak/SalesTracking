package com.sales.tracking.salestracking.Bean;

import java.util.ArrayList;

public class CollectionListBean {



    public static class Collections{

       String user_name;
        String collection_id;
        String collection_date;
        String collection_amount;
        String lead_name;

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getCollection_id() {
            return collection_id;
        }

        public void setCollection_id(String collection_id) {
            this.collection_id = collection_id;
        }

        public String getCollection_date() {
            return collection_date;
        }

        public void setCollection_date(String collection_date) {
            this.collection_date = collection_date;
        }

        public String getCollection_amount() {
            return collection_amount;
        }

        public void setCollection_amount(String collection_amount) {
            this.collection_amount = collection_amount;
        }

        public String getLead_name() {
            return lead_name;
        }

        public void setLead_name(String lead_name) {
            this.lead_name = lead_name;
        }

        public String getCollection_client_id() {
            return collection_client_id;
        }

        public void setCollection_client_id(String collection_client_id) {
            this.collection_client_id = collection_client_id;
        }

        public String getCollection_bill_no() {
            return collection_bill_no;
        }

        public void setCollection_bill_no(String collection_bill_no) {
            this.collection_bill_no = collection_bill_no;
        }

        public String getCollection_mode() {
            return collection_mode;
        }

        public void setCollection_mode(String collection_mode) {
            this.collection_mode = collection_mode;
        }

        public String getCollection_remark() {
            return collection_remark;
        }

        public void setCollection_remark(String collection_remark) {
            this.collection_remark = collection_remark;
        }

        public String getLead_company() {
            return lead_company;
        }

        public void setLead_company(String lead_company) {
            this.lead_company = lead_company;
        }

        public String getCheque_number() {
            return cheque_number;
        }

        public void setCheque_number(String cheque_number) {
            this.cheque_number = cheque_number;
        }

        public String getTransaction_id() {
            return transaction_id;
        }

        public void setTransaction_id(String transaction_id) {
            this.transaction_id = transaction_id;
        }

        public String getCheque_date() {
            return cheque_date;
        }

        public void setCheque_date(String cheque_date) {
            this.cheque_date = cheque_date;
        }

        String collection_client_id;
        String collection_bill_no;
       String collection_mode,collection_remark,lead_company,cheque_number,transaction_id,cheque_date;

    }

    public ArrayList<Collections> getCollections() {
        return collections;
    }

    public void setCollections(ArrayList<Collections> collections) {
        this.collections = collections;
    }

    public ArrayList<Collections> collections;
}

