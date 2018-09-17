package com.sales.tracking.salestracking.Bean;

import java.util.ArrayList;

public class TaskMeetingBean {

    public ArrayList<All_Meetings_Mgr> getAll_meetings_mgr() {
        return all_meetings_mgr;
    }

    public void setAll_meetings_mgr(ArrayList<All_Meetings_Mgr> all_meetings_mgr) {
        this.all_meetings_mgr = all_meetings_mgr;
    }

    public ArrayList<All_Meetings_Mgr> all_meetings_mgr;

    public static class All_Meetings_Mgr{
        String visit_id,visit_uid,visit_latitude,visit_address,visit_longitude,visit_datetime,visit_status,visit_comments,visit_leadid;
        String visit_photo;

        public String getVisit_id() {
            return visit_id;
        }

        public void setVisit_id(String visit_id) {
            this.visit_id = visit_id;
        }

        public String getVisit_uid() {
            return visit_uid;
        }

        public void setVisit_uid(String visit_uid) {
            this.visit_uid = visit_uid;
        }

        public String getVisit_latitude() {
            return visit_latitude;
        }

        public void setVisit_latitude(String visit_latitude) {
            this.visit_latitude = visit_latitude;
        }

        public String getVisit_address() {
            return visit_address;
        }

        public void setVisit_address(String visit_address) {
            this.visit_address = visit_address;
        }

        public String getVisit_longitude() {
            return visit_longitude;
        }

        public void setVisit_longitude(String visit_longitude) {
            this.visit_longitude = visit_longitude;
        }

        public String getVisit_datetime() {
            return visit_datetime;
        }

        public void setVisit_datetime(String visit_datetime) {
            this.visit_datetime = visit_datetime;
        }

        public String getVisit_status() {
            return visit_status;
        }

        public void setVisit_status(String visit_status) {
            this.visit_status = visit_status;
        }

        public String getVisit_comments() {
            return visit_comments;
        }

        public void setVisit_comments(String visit_comments) {
            this.visit_comments = visit_comments;
        }

        public String getVisit_leadid() {
            return visit_leadid;
        }

        public void setVisit_leadid(String visit_leadid) {
            this.visit_leadid = visit_leadid;
        }

        public String getVisit_photo() {
            return visit_photo;
        }

        public void setVisit_photo(String visit_photo) {
            this.visit_photo = visit_photo;
        }

        public String getVisit_purposeid() {
            return visit_purposeid;
        }

        public void setVisit_purposeid(String visit_purposeid) {
            this.visit_purposeid = visit_purposeid;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getPurpose_name() {
            return purpose_name;
        }

        public void setPurpose_name(String purpose_name) {
            this.purpose_name = purpose_name;
        }

        public String getLead_company() {
            return lead_company;
        }

        public void setLead_company(String lead_company) {
            this.lead_company = lead_company;
        }

        String visit_purposeid;
        String user_name;
        String purpose_name;
        String lead_company;

    }

    public ArrayList<Users_DD> getUsers_dd() {
        return users_dd;
    }

    public void setUsers_dd(ArrayList<Users_DD> users_dd) {
        this.users_dd = users_dd;
    }

    public ArrayList<Users_DD> users_dd;

    public static class Users_DD{
        String user_id;

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

        String user_name;
    }


    public ArrayList<Clients_DD> getClients_dd() {
        return clients_dd;
    }

    public void setClients_dd(ArrayList<Clients_DD> clients_dd) {
        this.clients_dd = clients_dd;
    }

    public ArrayList<Clients_DD> clients_dd;

    public static class Clients_DD{
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

    public static class Users_dd1{
        String user_id;

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

        String user_name;
    }

    public ArrayList<Users_dd1> getUsers_dd1() {
        return users_dd1;
    }

    public void setUsers_dd1(ArrayList<Users_dd1> users_dd1) {
        this.users_dd1 = users_dd1;
    }

    public ArrayList<Users_dd1> users_dd1;

}
