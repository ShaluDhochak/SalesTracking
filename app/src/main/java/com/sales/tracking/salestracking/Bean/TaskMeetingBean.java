package com.sales.tracking.salestracking.Bean;

import java.util.ArrayList;

public class TaskMeetingBean {

    public ArrayList<All_Meetings_Mgr> all_meetings_mgr;

    public static class All_Meetings_Mgr{
        String visit_id,visit_uid,visit_latitude,visit_address,visit_longitude,visit_datetime,visit_status,visit_comments,visit_leadid;
        String visit_photo,visit_purposeid,user_name,purpose_name,lead_company;

    }

    public ArrayList<Users_DD> users_dd;

    public static class Users_DD{
        String user_id,user_name;
    }


    public ArrayList<Clients_DD> clients_dd;

    public static class Clients_DD{
        String lead_id,lead_company;
    }


}

/*

m_all, visit_assignedby = 17, select
 "all_meetings_mgr": [
        },
 */

/*
users, user_comid =1
 "users_dd": {
        },
 */

/*
clients, lead_comid = 1
    "clients_dd": [
        },
 */
