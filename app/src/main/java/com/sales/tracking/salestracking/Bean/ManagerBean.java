package com.sales.tracking.salestracking.Bean;

import java.util.ArrayList;

public class ManagerBean {

    public static class clients{
        String lead_id;
        String lead_company;
        String lead_name;
        String lead_comid;
        String lead_email;
        String lead_contact;
        String lead_address;
        String lead_website;
        String lead_status;
        String user_name;

        String outstanding_bal;

        public String getOutstanding_bal() {
            return outstanding_bal;
        }

        public void setOutstanding_bal(String outstanding_bal) {
            this.outstanding_bal = outstanding_bal;
        }

        public String getLead_assignedto() {
            return lead_assignedto;
        }

        public void setLead_assignedto(String lead_assignedto) {
            this.lead_assignedto = lead_assignedto;
        }

        String lead_assignedto;


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

        public String getLead_name() {
            return lead_name;
        }

        public void setLead_name(String lead_name) {
            this.lead_name = lead_name;
        }

        public String getLead_comid() {
            return lead_comid;
        }

        public void setLead_comid(String lead_comid) {
            this.lead_comid = lead_comid;
        }

        public String getLead_email() {
            return lead_email;
        }

        public void setLead_email(String lead_email) {
            this.lead_email = lead_email;
        }

        public String getLead_contact() {
            return lead_contact;
        }

        public void setLead_contact(String lead_contact) {
            this.lead_contact = lead_contact;
        }

        public String getLead_address() {
            return lead_address;
        }

        public void setLead_address(String lead_address) {
            this.lead_address = lead_address;
        }

        public String getLead_website() {
            return lead_website;
        }

        public void setLead_website(String lead_website) {
            this.lead_website = lead_website;
        }

        public String getLead_status() {
            return lead_status;
        }

        public void setLead_status(String lead_status) {
            this.lead_status = lead_status;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getLeadtype_name() {
            return leadtype_name;
        }

        public void setLeadtype_name(String leadtype_name) {
            this.leadtype_name = leadtype_name;
        }

        String leadtype_name;

    }

    public ArrayList<clients> getClients() {
        return Clients;
    }

    public void setClients(ArrayList<clients> clients) {
        Clients = clients;
    }

    public ArrayList<clients> Clients;

}

