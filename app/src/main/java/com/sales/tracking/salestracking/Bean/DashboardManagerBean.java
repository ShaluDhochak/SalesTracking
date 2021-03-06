package com.sales.tracking.salestracking.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class DashboardManagerBean implements Parcelable {
    public static class Dashboard_Count implements Parcelable {
        String user_name,meetings_today,meetings_week,meetings_till_date,calls_today,calls_week;
        String calls_till_date;
        String leads_today;
        String leads_week;
        String leads_till_date;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        String user_id;

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



        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.user_name);
            dest.writeString(this.meetings_today);
            dest.writeString(this.meetings_week);
            dest.writeString(this.meetings_till_date);
            dest.writeString(this.calls_today);
            dest.writeString(this.calls_week);
            dest.writeString(this.calls_till_date);
            dest.writeString(this.leads_today);
            dest.writeString(this.leads_week);
            dest.writeString(this.leads_till_date);
            dest.writeString(this.user_id);
        }

        public Dashboard_Count() {
        }

        protected Dashboard_Count(Parcel in) {
            this.user_name = in.readString();
            this.meetings_today = in.readString();
            this.meetings_week = in.readString();
            this.meetings_till_date = in.readString();
            this.calls_today = in.readString();
            this.calls_week = in.readString();
            this.calls_till_date = in.readString();
            this.leads_today = in.readString();
            this.leads_week = in.readString();
            this.leads_till_date = in.readString();
            this.user_id = in.readString();
        }

        public static final Parcelable.Creator<Dashboard_Count> CREATOR = new Parcelable.Creator<Dashboard_Count>() {
            @Override
            public Dashboard_Count createFromParcel(Parcel source) {
                return new Dashboard_Count(source);
            }

            @Override
            public Dashboard_Count[] newArray(int size) {
                return new Dashboard_Count[size];
            }
        };
    }

    public ArrayList<Dashboard_Count> getDashboard_count() {
        return dashboard_count;
    }

    public void setDashboard_count(ArrayList<Dashboard_Count> dashboard_count) {
        this.dashboard_count = dashboard_count;
    }

    public ArrayList<Dashboard_Count> dashboard_count;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.dashboard_count);
    }

    public DashboardManagerBean() {
    }

    protected DashboardManagerBean(Parcel in) {
        this.dashboard_count = in.createTypedArrayList(Dashboard_Count.CREATOR);
    }

    public static final Parcelable.Creator<DashboardManagerBean> CREATOR = new Parcelable.Creator<DashboardManagerBean>() {
        @Override
        public DashboardManagerBean createFromParcel(Parcel source) {
            return new DashboardManagerBean(source);
        }

        @Override
        public DashboardManagerBean[] newArray(int size) {
            return new DashboardManagerBean[size];
        }
    };
}
