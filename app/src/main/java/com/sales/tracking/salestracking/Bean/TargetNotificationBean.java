package com.sales.tracking.salestracking.Bean;

import java.util.ArrayList;

public class TargetNotificationBean {

    public static class Call_Target{
        String target_no,target_startdate,target_enddate;

        public String getTarget_no() {
            return target_no;
        }

        public void setTarget_no(String target_no) {
            this.target_no = target_no;
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

    public ArrayList<Call_Target> getCall_target() {
        return call_target;
    }

    public void setCall_target(ArrayList<Call_Target> call_target) {
        this.call_target = call_target;
    }

    public ArrayList<Call_Target> call_target;


    public static class Visit_Target{
        public String getTarget_no() {
            return target_no;
        }

        public void setTarget_no(String target_no) {
            this.target_no = target_no;
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

        String target_no,target_startdate,target_enddate;

    }

    public ArrayList<Visit_Target> getVisit_target() {
        return visit_target;
    }

    public void setVisit_target(ArrayList<Visit_Target> visit_target) {
        this.visit_target = visit_target;
    }

    public ArrayList<Visit_Target> visit_target;

}

