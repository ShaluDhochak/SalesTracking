package com.sales.tracking.salestracking.Bean;

import java.util.ArrayList;

public class UpdateViewTaskSpBean {

    public ArrayList<Sp_Tasks_DD> getSp_tasks_dd() {
        return sp_tasks_dd;
    }

    public void setSp_tasks_dd(ArrayList<Sp_Tasks_DD> sp_tasks_dd) {
        this.sp_tasks_dd = sp_tasks_dd;
    }

    public ArrayList<Sp_Tasks_DD> sp_tasks_dd;

    public static class Sp_Tasks_DD{
        String visit_id;
        String visit_address;

        public String getVisit_id() {
            return visit_id;
        }

        public void setVisit_id(String visit_id) {
            this.visit_id = visit_id;
        }

        public String getVisit_address() {
            return visit_address;
        }

        public void setVisit_address(String visit_address) {
            this.visit_address = visit_address;
        }

        public String getLead_company() {
            return lead_company;
        }

        public void setLead_company(String lead_company) {
            this.lead_company = lead_company;
        }

        public String getPurpose_name() {
            return purpose_name;
        }

        public void setPurpose_name(String purpose_name) {
            this.purpose_name = purpose_name;
        }

        String lead_company;
        String purpose_name;

    }

}

/*

    "": [
        {
            "": "7",
            "": "adfasdf",
            "": "XYZ",
            "": "Informal Meeting"
        },
 */
