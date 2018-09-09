package com.sales.tracking.salestracking.Bean;

import java.util.ArrayList;

public class CustomerFeedbackBean {

    public static class Customer_Feedback{
        String fb_id;
        String fb_leadid;
        String fb_lead_uid;
        String fb_client_comments;
        String added_dt;
        String lead_company;

        public String getFb_id() {
            return fb_id;
        }

        public void setFb_id(String fb_id) {
            this.fb_id = fb_id;
        }

        public String getFb_leadid() {
            return fb_leadid;
        }

        public void setFb_leadid(String fb_leadid) {
            this.fb_leadid = fb_leadid;
        }

        public String getFb_lead_uid() {
            return fb_lead_uid;
        }

        public void setFb_lead_uid(String fb_lead_uid) {
            this.fb_lead_uid = fb_lead_uid;
        }

        public String getFb_client_comments() {
            return fb_client_comments;
        }

        public void setFb_client_comments(String fb_client_comments) {
            this.fb_client_comments = fb_client_comments;
        }

        public String getAdded_dt() {
            return added_dt;
        }

        public void setAdded_dt(String added_dt) {
            this.added_dt = added_dt;
        }

        public String getLead_company() {
            return lead_company;
        }

        public void setLead_company(String lead_company) {
            this.lead_company = lead_company;
        }

    }

    public ArrayList<Customer_Feedback> getCustomer_feedback() {
        return customer_feedback;
    }

    public void setCustomer_feedback(ArrayList<Customer_Feedback> customer_feedback) {
        this.customer_feedback = customer_feedback;
    }

    public ArrayList<Customer_Feedback> customer_feedback;


    public static class Lead_DropDown{
        String lead_id;

        public String getLead_id() {
            return lead_id;
        }

        public void setLead_id(String lead_id) {
            this.lead_id = lead_id;
        }

        public String getLead_company() {
            return lead_company;
        }

        public void setLead_company(String lead_company) {
            this.lead_company = lead_company;
        }

        String lead_company;

    }

    public ArrayList<Lead_DropDown> getLead_dropdown() {
        return lead_dropdown;
    }

    public void setLead_dropdown(ArrayList<Lead_DropDown> lead_dropdown) {
        this.lead_dropdown = lead_dropdown;
    }

    public ArrayList<Lead_DropDown> lead_dropdown;

}

