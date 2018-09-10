package com.sales.tracking.salestracking.Bean;

import java.util.ArrayList;

public class ProfileBean {

    public static class User_Profile{
        String user_name,user_email,user_mobile;

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
    }

    public ArrayList<User_Profile> getUser_profile() {
        return user_profile;
    }

    public void setUser_profile(ArrayList<User_Profile> user_profile) {
        this.user_profile = user_profile;
    }

    public ArrayList<User_Profile> user_profile;
}
