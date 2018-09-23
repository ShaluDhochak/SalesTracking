package com.sales.tracking.salestracking.Bean;

import java.util.ArrayList;

public class RequestCountBean {

    public static class Request_Count{
        public String getTot_requests() {
            return tot_requests;
        }

        public void setTot_requests(String tot_requests) {
            this.tot_requests = tot_requests;
        }

        String tot_requests;
    }

    public ArrayList<Request_Count> getRequest_count() {
        return request_count;
    }

    public void setRequest_count(ArrayList<Request_Count> request_count) {
        this.request_count = request_count;
    }

    public ArrayList<Request_Count> request_count;

}


