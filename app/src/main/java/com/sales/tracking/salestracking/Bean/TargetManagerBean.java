package com.sales.tracking.salestracking.Bean;

import java.util.ArrayList;

public class TargetManagerBean {

    public static class Target{
        String target_id,target_compid,target_empid,user_name,target_type,target_no,target_createdby,target_createdon;
        String target_updatedby,target_updatedon,target_startdate,target_enddate;

        public String getTarget_id() {
            return target_id;
        }

        public void setTarget_id(String target_id) {
            this.target_id = target_id;
        }

        public String getTarget_compid() {
            return target_compid;
        }

        public void setTarget_compid(String target_compid) {
            this.target_compid = target_compid;
        }

        public String getTarget_empid() {
            return target_empid;
        }

        public void setTarget_empid(String target_empid) {
            this.target_empid = target_empid;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getTarget_type() {
            return target_type;
        }

        public void setTarget_type(String target_type) {
            this.target_type = target_type;
        }

        public String getTarget_no() {
            return target_no;
        }

        public void setTarget_no(String target_no) {
            this.target_no = target_no;
        }

        public String getTarget_createdby() {
            return target_createdby;
        }

        public void setTarget_createdby(String target_createdby) {
            this.target_createdby = target_createdby;
        }

        public String getTarget_createdon() {
            return target_createdon;
        }

        public void setTarget_createdon(String target_createdon) {
            this.target_createdon = target_createdon;
        }

        public String getTarget_updatedby() {
            return target_updatedby;
        }

        public void setTarget_updatedby(String target_updatedby) {
            this.target_updatedby = target_updatedby;
        }

        public String getTarget_updatedon() {
            return target_updatedon;
        }

        public void setTarget_updatedon(String target_updatedon) {
            this.target_updatedon = target_updatedon;
        }

        public String getTarget_startdate() {
            return target_startdate;
        }

        public void setTarget_startdate(String target_startdate) {
            this.target_startdate = target_startdate;
        }

        public String getTarget_enddate() {
            return target_enddate;
        }

        public void setTarget_enddate(String target_enddate) {
            this.target_enddate = target_enddate;
        }
    }

    public ArrayList<Target> target;

    public ArrayList<Target> getTarget() {
        return target;
    }

    public void setTarget(ArrayList<Target> target) {
        this.target = target;
    }

}

