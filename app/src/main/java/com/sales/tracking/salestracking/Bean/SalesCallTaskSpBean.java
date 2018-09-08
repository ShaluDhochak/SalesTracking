package com.sales.tracking.salestracking.Bean;

import java.util.ArrayList;

public class SalesCallTaskSpBean {


    public static class Sp_All_Service_Calls{
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

        String service_id,service_person,service_contactno,service_status,service_createddt,service_comments,user_name,lead_company,lead_email;

    }

    public ArrayList<Sp_All_Service_Calls> getSp_all_service_calls() {
        return sp_all_service_calls;
    }

    public void setSp_all_service_calls(ArrayList<Sp_All_Service_Calls> sp_all_service_calls) {
        this.sp_all_service_calls = sp_all_service_calls;
    }

    public ArrayList<Sp_All_Service_Calls> sp_all_service_calls;


    public static class Sp_Servicecalls_DD{
        String service_id;
        String service_person;
        String service_contactno;

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

        public String getLead_company() {
            return lead_company;
        }

        public void setLead_company(String lead_company) {
            this.lead_company = lead_company;
        }

        String lead_company;
    }

    public ArrayList<Sp_Servicecalls_DD> getSp_servicecalls_dd() {
        return sp_servicecalls_dd;
    }

    public void setSp_servicecalls_dd(ArrayList<Sp_Servicecalls_DD> sp_servicecalls_dd) {
        this.sp_servicecalls_dd = sp_servicecalls_dd;
    }

    public ArrayList<Sp_Servicecalls_DD> sp_servicecalls_dd;


}

