package com.sales.tracking.salestracking.Bean;

import java.util.ArrayList;

public class ExpencesSpBean {

    public static class Expcal_Dropdown{
        String expcat_Id;

        public String getExpcat_Id() {
            return expcat_Id;
        }

        public void setExpcat_Id(String expcat_Id) {
            this.expcat_Id = expcat_Id;
        }

        public String getExpcat_name() {
            return expcat_name;
        }

        public void setExpcat_name(String expcat_name) {
            this.expcat_name = expcat_name;
        }

        String expcat_name;

    }

    public ArrayList<Expcal_Dropdown> getExpcat_dropdown() {
        return expcat_dropdown;
    }

    public void setExpcat_dropdown(ArrayList<Expcal_Dropdown> expcat_dropdown) {
        this.expcat_dropdown = expcat_dropdown;
    }

    public ArrayList<Expcal_Dropdown> expcat_dropdown;

    public static class Salesperson_Expenses{
        String expense_id,expense_compid,expense_uid,expense_cat,expcat_name,expense_amt,expense_mode,expense_images;
        String expense_details;
        String expense_date;

        public String getExpense_id() {
            return expense_id;
        }

        public void setExpense_id(String expense_id) {
            this.expense_id = expense_id;
        }

        public String getExpense_compid() {
            return expense_compid;
        }

        public void setExpense_compid(String expense_compid) {
            this.expense_compid = expense_compid;
        }

        public String getExpense_uid() {
            return expense_uid;
        }

        public void setExpense_uid(String expense_uid) {
            this.expense_uid = expense_uid;
        }

        public String getExpense_cat() {
            return expense_cat;
        }

        public void setExpense_cat(String expense_cat) {
            this.expense_cat = expense_cat;
        }

        public String getExpcat_name() {
            return expcat_name;
        }

        public void setExpcat_name(String expcat_name) {
            this.expcat_name = expcat_name;
        }

        public String getExpense_amt() {
            return expense_amt;
        }

        public void setExpense_amt(String expense_amt) {
            this.expense_amt = expense_amt;
        }

        public String getExpense_mode() {
            return expense_mode;
        }

        public void setExpense_mode(String expense_mode) {
            this.expense_mode = expense_mode;
        }

        public String getExpense_images() {
            return expense_images;
        }

        public void setExpense_images(String expense_images) {
            this.expense_images = expense_images;
        }

        public String getExpense_details() {
            return expense_details;
        }

        public void setExpense_details(String expense_details) {
            this.expense_details = expense_details;
        }

        public String getExpense_date() {
            return expense_date;
        }

        public void setExpense_date(String expense_date) {
            this.expense_date = expense_date;
        }

        public String getExpense_status() {
            return expense_status;
        }

        public void setExpense_status(String expense_status) {
            this.expense_status = expense_status;
        }

        String expense_status;

    }

    public ArrayList<Salesperson_Expenses> getSalesperson_expenses() {
        return salesperson_expenses;
    }

    public void setSalesperson_expenses(ArrayList<Salesperson_Expenses> salesperson_expenses) {
        this.salesperson_expenses = salesperson_expenses;
    }

    public ArrayList<Salesperson_Expenses> salesperson_expenses;

}

