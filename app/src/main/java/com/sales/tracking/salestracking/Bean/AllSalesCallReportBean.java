package com.sales.tracking.salestracking.Bean;

import java.util.ArrayList;

public class AllSalesCallReportBean {

    public static class Sp_All_Calls{
        String service_id,service_person,service_contactno,service_status,service_createddt,service_comments,user_name;
        String lead_company;

        public String getService_id() {
            return service_id;
        }

        public void setService_id(String service_id) {
            this.service_id = service_id;
        }

        public String getService_person() {
            return service_person;
        }

        public void setService_person(String service_person) {
            this.service_person = service_person;
        }

        public String getService_contactno() {
            return service_contactno;
        }

        public void setService_contactno(String service_contactno) {
            this.service_contactno = service_contactno;
        }

        public String getService_status() {
            return service_status;
        }

        public void setService_status(String service_status) {
            this.service_status = service_status;
        }

        public String getService_createddt() {
            return service_createddt;
        }

        public void setService_createddt(String service_createddt) {
            this.service_createddt = service_createddt;
        }

        public String getService_comments() {
            return service_comments;
        }

        public void setService_comments(String service_comments) {
            this.service_comments = service_comments;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getLead_company() {
            return lead_company;
        }

        public void setLead_company(String lead_company) {
            this.lead_company = lead_company;
        }

        public String getLead_email() {
            return lead_email;
        }

        public void setLead_email(String lead_email) {
            this.lead_email = lead_email;
        }

        String lead_email;

    }

    public ArrayList<Sp_All_Calls> getSp_all_calls() {
        return sp_all_calls;
    }

    public void setSp_all_calls(ArrayList<Sp_All_Calls> sp_all_calls) {
        this.sp_all_calls = sp_all_calls;
    }

    public ArrayList<Sp_All_Calls> sp_all_calls;

}


