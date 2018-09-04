package com.sales.tracking.salestracking.Bean;

import java.util.ArrayList;

public class AttendanceManagerBean {

    public ArrayList<Sp_Att_Und_Mgr> getSp_att_und_mgr() {
        return sp_att_und_mgr;
    }

    public void setSp_att_und_mgr(ArrayList<Sp_Att_Und_Mgr> sp_att_und_mgr) {
        this.sp_att_und_mgr = sp_att_und_mgr;
    }

    public ArrayList<Sp_Att_Und_Mgr> sp_att_und_mgr;

    public static class Sp_Att_Und_Mgr{
        String atten_id,atten_uid,atten_in_latitude,atten_in_longitude,atten_in_datetime,atten_out_latitude,atten_out_longitude;
        String atten_out_datetime;

        public String getAtten_id() {
            return atten_id;
        }

        public void setAtten_id(String atten_id) {
            this.atten_id = atten_id;
        }

        public String getAtten_uid() {
            return atten_uid;
        }

        public void setAtten_uid(String atten_uid) {
            this.atten_uid = atten_uid;
        }

        public String getAtten_in_latitude() {
            return atten_in_latitude;
        }

        public void setAtten_in_latitude(String atten_in_latitude) {
            this.atten_in_latitude = atten_in_latitude;
        }

        public String getAtten_in_longitude() {
            return atten_in_longitude;
        }

        public void setAtten_in_longitude(String atten_in_longitude) {
            this.atten_in_longitude = atten_in_longitude;
        }

        public String getAtten_in_datetime() {
            return atten_in_datetime;
        }

        public void setAtten_in_datetime(String atten_in_datetime) {
            this.atten_in_datetime = atten_in_datetime;
        }

        public String getAtten_out_latitude() {
            return atten_out_latitude;
        }

        public void setAtten_out_latitude(String atten_out_latitude) {
            this.atten_out_latitude = atten_out_latitude;
        }

        public String getAtten_out_longitude() {
            return atten_out_longitude;
        }

        public void setAtten_out_longitude(String atten_out_longitude) {
            this.atten_out_longitude = atten_out_longitude;
        }

        public String getAtten_out_datetime() {
            return atten_out_datetime;
        }

        public void setAtten_out_datetime(String atten_out_datetime) {
            this.atten_out_datetime = atten_out_datetime;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        String user_name;
    }

}

/*
 "": [
        {
            "": "1",
            "": "18",
            "": "19.2183307",
            "": "72.9780897",
            "": "2018-07-16 10:00:00",
            "": "19.2183307",
            "": "72.9780897",
            "": "2018-07-16 20:00:00",
            "": "Mayurii"
        },
 */
