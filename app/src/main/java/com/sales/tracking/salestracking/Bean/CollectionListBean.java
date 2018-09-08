package com.sales.tracking.salestracking.Bean;

import java.util.ArrayList;

public class CollectionListBean {

    public static class Collections{
        String collection_id;

        public String getCollection_id() {
            return collection_id;
        }

        public void setCollection_id(String collection_id) {
            this.collection_id = collection_id;
        }

        public String getCollection_amount() {
            return collection_amount;
        }

        public void setCollection_amount(String collection_amount) {
            this.collection_amount = collection_amount;
        }

        public String getCollection_date() {
            return collection_date;
        }

        public void setCollection_date(String collection_date) {
            this.collection_date = collection_date;
        }

        String collection_amount;
        String collection_date;

    }

    public ArrayList<Collections> getCollections() {
        return collections;
    }

    public void setCollections(ArrayList<Collections> collections) {
        this.collections = collections;
    }

    public ArrayList<Collections> collections;
}

