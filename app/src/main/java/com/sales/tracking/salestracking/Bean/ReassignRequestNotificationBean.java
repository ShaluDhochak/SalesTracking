package com.sales.tracking.salestracking.Bean;

import java.util.ArrayList;

public class ReassignRequestNotificationBean {

    public static class Select_Pending_Requests{
        String request_type;
        String request_createddt;

        public String getRequest_type() {
            return request_type;
        }

        public void setRequest_type(String request_type) {
            this.request_type = request_type;
        }

        public String getRequest_createddt() {
            return request_createddt;
        }

        public void setRequest_createddt(String request_createddt) {
            this.request_createddt = request_createddt;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        String user_name;
    }

    public ArrayList<Select_Pending_Requests> getSelect_pending_requests() {
        return select_pending_requests;
    }

    public void setSelect_pending_requests(ArrayList<Select_Pending_Requests> select_pending_requests) {
        this.select_pending_requests = select_pending_requests;
    }

    public ArrayList<Select_Pending_Requests> select_pending_requests;

}


