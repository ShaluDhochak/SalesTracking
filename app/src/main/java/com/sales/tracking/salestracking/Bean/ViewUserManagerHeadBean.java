package com.sales.tracking.salestracking.Bean;

import java.util.ArrayList;

public class ViewUserManagerHeadBean {

    public static class Users{
        String user_id;
        String user_name;
        String user_email;
        String user_mobile;

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

        public String getCreated_dt() {
            return created_dt;
        }

        public void setCreated_dt(String created_dt) {
            this.created_dt = created_dt;
        }

        public String getUpdate_dt() {
            return update_dt;
        }

        public void setUpdate_dt(String update_dt) {
            this.update_dt = update_dt;
        }

        public String getUser_doj() {
            return user_doj;
        }

        public void setUser_doj(String user_doj) {
            this.user_doj = user_doj;
        }

        public String getUser_status() {
            return user_status;
        }

        public void setUser_status(String user_status) {
            this.user_status = user_status;
        }

        public String getUser_type() {
            return user_type;
        }

        public void setUser_type(String user_type) {
            this.user_type = user_type;
        }

        public String getUser_reporting_to() {
            return user_reporting_to;
        }

        public void setUser_reporting_to(String user_reporting_to) {
            this.user_reporting_to = user_reporting_to;
        }

        public String getDesi_name() {
            return desi_name;
        }

        public void setDesi_name(String desi_name) {
            this.desi_name = desi_name;
        }

        String user_pass;
        String created_dt;
        String update_dt;
        String user_doj;
        String user_status;
        String user_type;
        String user_reporting_to;
        String desi_name;

    }

    public ArrayList<Users> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<Users> users) {
        this.users = users;
    }

    public ArrayList<Users> users;

}

