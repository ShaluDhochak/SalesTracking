package com.sales.tracking.salestracking.Utility;

public interface ApiLink {

    String ROOT_URL = "http://arizonamediaz.co.in/sales_tracking/api/";
    String LOGIN = "login.php";

    //Dashboard
    String USER_LIST = "user.php";

    //Dashboard Sales Executive
    String Dashboard_SalesPerson = "task_meeting.php";
    String DASHBOARD_MANAGER = "manager_dashboard.php";
    String DASHBOARD_MANAGERHEAD = "managerhead_dashboard.php";

    String Attendance_Manager = "sp_attendance.php";
    String ATTENDANCE_MANAGER_HEAD = "mgr_head_sp_attendance.php";

    String TASK_SERVICECALL = "task_servicecall.php";

    String VISIT_TASK_SPINNER = "sp_visitupdate.php";

    String SALE_CALL_TASK_UPDATE = "sp_servicecallupdate.php";
    //adding SALES CALL

    String REQUEST_VIEW_SP = "request_salesperson.php";

    String LEAD_VIEW_SALESPERSON = "sp_lead.php";

    String COLLECTION_SP = "collection.php";
    String REP_COLLECTION = "rep_collection.php";

    String Expenses_SP = "expences.php";

    String CUSTOMER_FEEDBACK = "cust_fb.php";

    String CHANGE_PASSWORD = "user_changepwd.php";

    String PROFILE_DETAILS = "profile.php";

    String MANAGER_CLIENT = "manager_client.php";

    String MANAGER_SALES_PERSON = "user.php";

    String TARGET_MANAGER = "target.php";

    String VISIT_PENDING_REPORT = "rep_manager_visits.php";
    String VISIT_MANAGER_HEAD_REPORT = "rep_manager_head_visits.php";

    String CALL_PENDING_REPORT = "rep_manager_calls.php";
    String CALL_MANAGER_HEAD_NOTIFICATION = "managerheadheader.php";

    String EXPENSES_REPORT="rep_manager_expenses.php";
    String EXPENSES_MGR_HEAD_REPORT = "rep_manager_head_expenses.php";

    String VIEW_REASSIGNED_REQUEST = "request_salesmgr.php";

    String VISIT_REQUEST_NOTIFICATION = "managerheader.php";

    String IMAGE_BASE_URL = "http://arizonamediaz.co.in/sales_tracking/ci/";

    String TRACK_SALES_PERSON = "track_sp.php";
    String TRACK_MANAGER_HEAD = "managerhead_track_sp.php";

    String ATTENDENCE = "sp_attendance.php";



}
