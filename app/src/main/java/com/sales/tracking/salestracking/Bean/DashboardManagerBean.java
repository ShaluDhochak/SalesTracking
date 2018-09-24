package com.sales.tracking.salestracking.Bean;

import java.util.ArrayList;

public class DashboardManagerBean {
    public static class Dashboard_Count{
        String user_name,meetings_today,meetings_week,meetings_till_date,calls_today,calls_week;
        String calls_till_date;
        String leads_today;
        String leads_week;

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getMeetings_today() {
            return meetings_today;
        }

        public void setMeetings_today(String meetings_today) {
            this.meetings_today = meetings_today;
        }

        public String getMeetings_week() {
            return meetings_week;
        }

        public void setMeetings_week(String meetings_week) {
            this.meetings_week = meetings_week;
        }

        public String getMeetings_till_date() {
            return meetings_till_date;
        }

        public void setMeetings_till_date(String meetings_till_date) {
            this.meetings_till_date = meetings_till_date;
        }

        public String getCalls_today() {
            return calls_today;
        }

        public void setCalls_today(String calls_today) {
            this.calls_today = calls_today;
        }

        public String getCalls_week() {
            return calls_week;
        }

        public void setCalls_week(String calls_week) {
            this.calls_week = calls_week;
        }

        public String getCalls_till_date() {
            return calls_till_date;
        }

        public void setCalls_till_date(String calls_till_date) {
            this.calls_till_date = calls_till_date;
        }

        public String getLeads_today() {
            return leads_today;
        }

        public void setLeads_today(String leads_today) {
            this.leads_today = leads_today;
        }

        public String getLeads_week() {
            return leads_week;
        }

        public void setLeads_week(String leads_week) {
            this.leads_week = leads_week;
        }

        public String getLeads_till_date() {
            return leads_till_date;
        }

        public void setLeads_till_date(String leads_till_date) {
            this.leads_till_date = leads_till_date;
        }

        String leads_till_date;

    }

    public ArrayList<Dashboard_Count> getDashboard_count() {
        return dashboard_count;
    }

    public void setDashboard_count(ArrayList<Dashboard_Count> dashboard_count) {
        this.dashboard_count = dashboard_count;
    }

    public ArrayList<Dashboard_Count> dashboard_count;

}
