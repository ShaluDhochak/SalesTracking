package com.sales.tracking.salestracking.Bean;

import java.util.ArrayList;

public class ViewExpensesReportBean {


    public static class Sp_Expenses{
        String expense_compid,expense_uid,expense_cat,expense_amt,expense_mode,expense_date,expense_details,expense_status;
        String user_name;

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

        public String getExpense_date() {
            return expense_date;
        }

        public void setExpense_date(String expense_date) {
            this.expense_date = expense_date;
        }

        public String getExpense_details() {
            return expense_details;
        }

        public void setExpense_details(String expense_details) {
            this.expense_details = expense_details;
        }

        public String getExpense_status() {
            return expense_status;
        }

        public void setExpense_status(String expense_status) {
            this.expense_status = expense_status;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getExpcat_name() {
            return expcat_name;
        }

        public void setExpcat_name(String expcat_name) {
            this.expcat_name = expcat_name;
        }

        String expcat_name;

    }

    public ArrayList<Sp_Expenses> getSp_expenses() {
        return sp_expenses;
    }

    public void setSp_expenses(ArrayList<Sp_Expenses> sp_expenses) {
        this.sp_expenses = sp_expenses;
    }

    public ArrayList<Sp_Expenses> sp_expenses;

}

