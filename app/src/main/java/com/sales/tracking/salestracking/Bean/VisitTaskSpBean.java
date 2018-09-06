package com.sales.tracking.salestracking.Bean;

import java.util.ArrayList;

public class VisitTaskSpBean {

    public static class Single_sp_all_Meetings{
        String visit_followup_date,visit_id,visit_uid,visit_latitude,visit_longitude,visit_address,visit_datetime,visit_status,visit_comments;
        String visit_leadid;
        String visit_photo;
        String visit_purposeid;
        String user_name;
        String purpose_name;

        public String getVisit_followup_date() {
            return visit_followup_date;
        }

        public void setVisit_followup_date(String visit_followup_date) {
            this.visit_followup_date = visit_followup_date;
        }

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

        public String getVisit_longitude() {
            return visit_longitude;
        }

        public void setVisit_longitude(String visit_longitude) {
            this.visit_longitude = visit_longitude;
        }

        public String getVisit_address() {
            return visit_address;
        }

        public void setVisit_address(String visit_address) {
            this.visit_address = visit_address;
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

        String lead_company;
    }

    public ArrayList<Single_sp_all_Meetings> getSingle_sp_all_meetings() {
        return single_sp_all_meetings;
    }

    public void setSingle_sp_all_meetings(ArrayList<Single_sp_all_Meetings> single_sp_all_meetings) {
        this.single_sp_all_meetings = single_sp_all_meetings;
    }

    public ArrayList<Single_sp_all_Meetings> single_sp_all_meetings;


}
