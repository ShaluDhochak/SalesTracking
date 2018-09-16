package com.sales.tracking.salestracking.Bean;

import java.util.ArrayList;

public class ManagerUserBean {

    public static class Manager_Users{
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

        String user_name;
        String user_email;
        String user_mobile;
        String user_pass;
        String created_dt;
        String update_dt;
        String user_doj;
        String user_status;
        String user_type;
        String user_reporting_to;
        String desi_name;

    }

    public ArrayList<Manager_Users> getManager_users() {
        return manager_users;
    }

    public void setManager_users(ArrayList<Manager_Users> manager_users) {
        this.manager_users = manager_users;
    }

    public ArrayList<Manager_Users> manager_users;

    public static class Users_Count{
        public String getTot_users() {
            return tot_users;
        }

        public void setTot_users(String tot_users) {
            this.tot_users = tot_users;
        }

        String tot_users;
    }

    public ArrayList<Users_Count> getUsers_count() {
        return users_count;
    }

    public void setUsers_count(ArrayList<Users_Count> users_count) {
        this.users_count = users_count;
    }

    public ArrayList<Users_Count> users_count;


    public static class Package_Users_Count {
        public String getPack_noofmemb() {
            return pack_noofmemb;
        }

        public void setPack_noofmemb(String pack_noofmemb) {
            this.pack_noofmemb = pack_noofmemb;
        }

        String pack_noofmemb;

    }

    public ArrayList<Package_Users_Count> getPackage_users_count() {
        return package_users_count;
    }

    public void setPackage_users_count(ArrayList<Package_Users_Count> package_users_count) {
        this.package_users_count = package_users_count;
    }

    public ArrayList<Package_Users_Count> package_users_count;

}

