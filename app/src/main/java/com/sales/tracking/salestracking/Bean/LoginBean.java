package com.sales.tracking.salestracking.Bean;

import java.util.ArrayList;

public class LoginBean {

    public ArrayList<LoginBean.user> getUser() {
        return user;
    }

    public void setUser(ArrayList<LoginBean.user> user) {
        this.user = user;
    }

    public ArrayList<user> user;

    public static class user{
        public String user_id;
        public String user_name;
        public String user_email;
        public String user_mobile;
        public String user_pass;
        public String user_comid;
        public String user_desiid;

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

        public String getUser_email() {
            return user_email;
        }

        public void setUser_email(String user_email) {
            this.user_email = user_email;
        }

        public String getUser_mobile() {
            return user_mobile;
        }

        public void setUser_mobile(String user_mobile) {
            this.user_mobile = user_mobile;
        }

        public String getUser_pass() {
            return user_pass;
        }

        public void setUser_pass(String user_pass) {
            this.user_pass = user_pass;
        }

        public String getUser_comid() {
            return user_comid;
        }

        public void setUser_comid(String user_comid) {
            this.user_comid = user_comid;
        }

        public String getUser_desiid() {
            return user_desiid;
        }

        public void setUser_desiid(String user_desiid) {
            this.user_desiid = user_desiid;
        }

        public String getUser_type() {
            return user_type;
        }

        public void setUser_type(String user_type) {
            this.user_type = user_type;
        }

        public String user_type;
    }




}


/*
[
    {
        "user_id": "17",
        "user_name": "Nikhil",
        "user_email": "azmanager@gmail.com",
        "user_mobile": "9877866666",
        "user_pass": "8dbebb270ddb56506296ef5501794c67fd81fe80",
        "user_comid": "1",
        "user_desiid": "2",
        "user_type": "Sales Manager"
    }
]



 */