package com.sales.tracking.salestracking.Bean;

import java.util.ArrayList;

public class VisitDoneDashboardManagerBean {

  public static class SP_All_Visits{
      String visit_code,meeting_dt,meeting_time,user_name,designation,purpose_name,visit_address,visit_comments,visit_status;
      String lead_company;
      String lead_name;

      public String getVisit_code() {
          return visit_code;
      }

      public void setVisit_code(String visit_code) {
          this.visit_code = visit_code;
      }

      public String getMeeting_dt() {
          return meeting_dt;
      }

      public void setMeeting_dt(String meeting_dt) {
          this.meeting_dt = meeting_dt;
      }

      public String getMeeting_time() {
          return meeting_time;
      }

      public void setMeeting_time(String meeting_time) {
          this.meeting_time = meeting_time;
      }

      public String getUser_name() {
          return user_name;
      }

      public void setUser_name(String user_name) {
          this.user_name = user_name;
      }

      public String getDesignation() {
          return designation;
      }

      public void setDesignation(String designation) {
          this.designation = designation;
      }

      public String getPurpose_name() {
          return purpose_name;
      }

      public void setPurpose_name(String purpose_name) {
          this.purpose_name = purpose_name;
      }

      public String getVisit_address() {
          return visit_address;
      }

      public void setVisit_address(String visit_address) {
          this.visit_address = visit_address;
      }

      public String getVisit_comments() {
          return visit_comments;
      }

      public void setVisit_comments(String visit_comments) {
          this.visit_comments = visit_comments;
      }

      public String getVisit_status() {
          return visit_status;
      }

      public void setVisit_status(String visit_status) {
          this.visit_status = visit_status;
      }

      public String getLead_company() {
          return lead_company;
      }

      public void setLead_company(String lead_company) {
          this.lead_company = lead_company;
      }

      public String getLead_name() {
          return lead_name;
      }

      public void setLead_name(String lead_name) {
          this.lead_name = lead_name;
      }

      public String getLead_contact() {
          return lead_contact;
      }

      public void setLead_contact(String lead_contact) {
          this.lead_contact = lead_contact;
      }

      String lead_contact;
  }

    public ArrayList<SP_All_Visits> getSp_all_visits() {
        return sp_all_visits;
    }

    public void setSp_all_visits(ArrayList<SP_All_Visits> sp_all_visits) {
        this.sp_all_visits = sp_all_visits;
    }

    public ArrayList<SP_All_Visits> sp_all_visits;

}

