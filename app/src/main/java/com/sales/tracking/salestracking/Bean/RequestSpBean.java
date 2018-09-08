package com.sales.tracking.salestracking.Bean;

import java.util.ArrayList;

public class RequestSpBean {

    public static class Salesperson_requests{
        String user_name;
        String request_status;

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getRequest_status() {
            return request_status;
        }

        public void setRequest_status(String request_status) {
            this.request_status = request_status;
        }

        public String getRequest_id() {
            return request_id;
        }

        public void setRequest_id(String request_id) {
            this.request_id = request_id;
        }

        public String getRequest_comments() {
            return request_comments;
        }

        public void setRequest_comments(String request_comments) {
            this.request_comments = request_comments;
        }

        public String getRequest_type() {
            return request_type;
        }

        public void setRequest_type(String request_type) {
            this.request_type = request_type;
        }

        public String getRequest_type_id() {
            return request_type_id;
        }

        public void setRequest_type_id(String request_type_id) {
            this.request_type_id = request_type_id;
        }

        public String getRequest_dt() {
            return request_dt;
        }

        public void setRequest_dt(String request_dt) {
            this.request_dt = request_dt;
        }

        String request_id;
        String request_comments;
        String request_type;
        String request_type_id;
        String request_dt;

    }

    public ArrayList<Salesperson_requests> getSalesperson_requests() {
        return salesperson_requests;
    }

    public void setSalesperson_requests(ArrayList<Salesperson_requests> salesperson_requests) {
        this.salesperson_requests = salesperson_requests;
    }

    public ArrayList<Salesperson_requests> salesperson_requests;


    public static class Visit_Tasks_Dropdown{
        String visit_id;
        String lead_company;
        String purpose_name;

        public String getVisit_id() {
            return visit_id;
        }

        public void setVisit_id(String visit_id) {
            this.visit_id = visit_id;
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

        public String getVisit_address() {
            return visit_address;
        }

        public void setVisit_address(String visit_address) {
            this.visit_address = visit_address;
        }

        String visit_address;
    }

    public ArrayList<Visit_Tasks_Dropdown> getVisit_tasks_dropdown() {
        return visit_tasks_dropdown;
    }

    public void setVisit_tasks_dropdown(ArrayList<Visit_Tasks_Dropdown> visit_tasks_dropdown) {
        this.visit_tasks_dropdown = visit_tasks_dropdown;
    }

    public ArrayList<Visit_Tasks_Dropdown> visit_tasks_dropdown;


    public static class Sales_Tasks_Dropdown{
        public String getService_id() {
            return service_id;
        }

        public void setService_id(String service_id) {
            this.service_id = service_id;
        }

        public String getLead_company() {
            return lead_company;
        }

        public void setLead_company(String lead_company) {
            this.lead_company = lead_company;
        }

        public String getService_person() {
            return service_person;
        }

        public void setService_person(String service_person) {
            this.service_person = service_person;
        }

        public String getService_contactno() {
            return service_contactno;
        }

        public void setService_contactno(String service_contactno) {
            this.service_contactno = service_contactno;
        }

        String service_id,lead_company,service_person,service_contactno;
    }

    public ArrayList<Sales_Tasks_Dropdown> getSales_tasks_dropdown() {
        return sales_tasks_dropdown;
    }

    public void setSales_tasks_dropdown(ArrayList<Sales_Tasks_Dropdown> sales_tasks_dropdown) {
        this.sales_tasks_dropdown = sales_tasks_dropdown;
    }

    public ArrayList<Sales_Tasks_Dropdown> sales_tasks_dropdown;

}




