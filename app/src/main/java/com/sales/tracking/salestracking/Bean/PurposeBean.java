package com.sales.tracking.salestracking.Bean;

import java.util.ArrayList;

public class PurposeBean {
    public ArrayList<Purpose_dd> getPurpose_dd() {
        return purpose_dd;
    }

    public void setPurpose_dd(ArrayList<Purpose_dd> purpose_dd) {
        this.purpose_dd = purpose_dd;
    }

    public ArrayList<Purpose_dd> purpose_dd;

    public static class Purpose_dd{
        String purpose_id;

        public String getPurpose_id() {
            return purpose_id;
        }

        public void setPurpose_id(String purpose_id) {
            this.purpose_id = purpose_id;
        }

        public String getPurpose_name() {
            return purpose_name;
        }

        public void setPurpose_name(String purpose_name) {
            this.purpose_name = purpose_name;
        }

        String purpose_name;
    }
}

/*
key purposes= " "
api = task_meeting.php

"": [
        {
            "": "1",
            "": "Informal Meeting"
        }
    ]
 */
