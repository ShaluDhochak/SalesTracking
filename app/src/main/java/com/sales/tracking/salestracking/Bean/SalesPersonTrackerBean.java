package com.sales.tracking.salestracking.Bean;


import java.util.ArrayList;

public class SalesPersonTrackerBean {

    ArrayList<SalesTracker> sales_person_tracking;

    public ArrayList<SalesTracker> getSales_person_tracking() {
        return sales_person_tracking;
    }

    public void setSales_person_tracking(ArrayList<SalesTracker> sales_person_tracking) {
        this.sales_person_tracking = sales_person_tracking;
    }

    public static class SalesTracker{

        String user_id;
        String user_name;
        String user_mobile;
        String user_type;
        String user_lattitude;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getUser_mobile() {
            return user_mobile;
        }

        public void setUser_mobile(String user_mobile) {
            this.user_mobile = user_mobile;
        }

        public String getUser_type() {
            return user_type;
        }

        public void setUser_type(String user_type) {
            this.user_type = user_type;
        }

        public String getUser_lattitude() {
            return user_lattitude;
        }

        public void setUser_lattitude(String user_lattitude) {
            this.user_lattitude = user_lattitude;
        }

        public String getUser_longitude() {
            return user_longitude;
        }

        public void setUser_longitude(String user_longitude) {
            this.user_longitude = user_longitude;
        }

        String user_longitude;


    }
}
