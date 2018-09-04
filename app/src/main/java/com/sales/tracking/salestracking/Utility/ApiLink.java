package com.sales.tracking.salestracking.Utility;

public interface ApiLink {

    String ROOT_URL = "http://arizonamediaz.co.in/sales_tracking/api/";
    String LOGIN = "login.php";

    //Dashboard
    String USER_LIST = "user.php";

    //Dashboard Sales Exceutive
    String Dashboard_SalesPerson ="task_meeting.php";

    String Attendance_Manager = "sp_attendance.php";

    /*
            )
  ( 1 to 3 is for counts  and 4 th to view all records  of meetings of   sales executive today on dashboard ).

1.   API : http://localhost/sales_tracking/api/task_meeting.php

      parameters : count_a_meetings =  count_a_meetings , visit_uid = 18 ( eg. 18          for rccc login as sales exec)

       KEY : meeting_count

2.    API : http://localhost/sales_tracking/api/task_meeting.php

       parameters  :count_b_calls =  count_b_calls , service_uid = 18  ( eg. 18 for             rccc login as sales exec)

       KEY : calls_count

3.    API : http://localhost/sales_tracking/api/task_meeting.php

       parameters  : count_c_leads = count_c_leads , lead_uid = 18  ( eg. 18 for               rccc login as sales exec)

       KEY : leads_count

4.    API : http://localhost/sales_tracking/api/task_meeting.php

       parameters  : visit_uid = 18 , sel_user_dash=sel_user_dash, today=today  (           eg. 18 for   rccc login as sales exec)

       KEY : sp_meetings_today

    */
}
