package com.sales.tracking.salestracking.Bean;

import java.util.ArrayList;

public class DashboardSalesPersonBean {

    //Total Meetings
    public static class Meeting_Count{
        public String getTot_meetings() {
            return tot_meetings;
        }

        public void setTot_meetings(String tot_meetings) {
            this.tot_meetings = tot_meetings;
        }

        String tot_meetings;

    }

    public ArrayList<Meeting_Count> getMeeting_count() {
        return meeting_count;
    }

    public void setMeeting_count(ArrayList<Meeting_Count> meeting_count) {
        this.meeting_count = meeting_count;
    }

    public ArrayList<Meeting_Count> meeting_count;


    //Total Calls
    public static class Calls_Count{
        public String getTot_calls() {
            return tot_calls;
        }

        public void setTot_calls(String tot_calls) {
            this.tot_calls = tot_calls;
        }

        String tot_calls;
    }

    public ArrayList<Calls_Count> getCalls_count() {
        return calls_count;
    }

    public void setCalls_count(ArrayList<Calls_Count> calls_count) {
        this.calls_count = calls_count;
    }

    public ArrayList<Calls_Count> calls_count;


    //Total Leads
    public static class Leads_Count{
        public String getTot_leads() {
            return tot_leads;
        }

        public void setTot_leads(String tot_leads) {
            this.tot_leads = tot_leads;
        }

        String tot_leads;
    }

    public ArrayList<Leads_Count> getLeads_count() {
        return leads_count;
    }

    public void setLeads_count(ArrayList<Leads_Count> leads_count) {
        this.leads_count = leads_count;
    }

    public ArrayList<Leads_Count> leads_count ;


    //Sp Meeting Today
    public static class sp_meetings_today{
        String visit_followup_date,visit_id,visit_latitude,visit_address,visit_longitude,visit_datetime,visit_leadid,user_name;
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

        public String getVisit_leadid() {
            return visit_leadid;
        }

        public void setVisit_leadid(String visit_leadid) {
            this.visit_leadid = visit_leadid;
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

    public ArrayList<DashboardSalesPersonBean.sp_meetings_today> getSp_meetings_today() {
        return sp_meetings_today;
    }

    public void setSp_meetings_today(ArrayList<DashboardSalesPersonBean.sp_meetings_today> sp_meetings_today) {
        this.sp_meetings_today = sp_meetings_today;
    }

    public ArrayList<sp_meetings_today> sp_meetings_today;
}
