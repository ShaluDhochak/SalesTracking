package com.sales.tracking.salestracking.Activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.sales.tracking.salestracking.Adapter.VisitManagerDashboardReportAdapter;
import com.sales.tracking.salestracking.Adapter.VisitManagerHeadReportAdapter;
import com.sales.tracking.salestracking.Adapter.VisitManagerReportAdapter;
import com.sales.tracking.salestracking.Bean.AllVisitReportMgrHeadBean;
import com.sales.tracking.salestracking.Bean.DashboardManagerBean;
import com.sales.tracking.salestracking.Bean.TaskMeetingBean;
import com.sales.tracking.salestracking.Bean.VisitDoneDashboardManagerBean;
import com.sales.tracking.salestracking.Bean.VisitDoneReportManagerBean;
import com.sales.tracking.salestracking.Bean.VisitReportManagerBean;
import com.sales.tracking.salestracking.Fragment.AllVisitReportFragment;
import com.sales.tracking.salestracking.R;
import com.sales.tracking.salestracking.Utility.ApiLink;
import com.sales.tracking.salestracking.Utility.Connectivity;
import com.sales.tracking.salestracking.Utility.GSONRequest;
import com.sales.tracking.salestracking.Utility.JSONParser;
import com.sales.tracking.salestracking.Utility.Utilities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

public class VisitDashboardCountActivity extends AppCompatActivity {

    @BindView(R.id.meetingtimeVisitManagerReport_tv)
    TextView meetingtimeVisitManagerReport_tv;

    @BindView(R.id.commentsVisitManagerReportDetail_tv)
    TextView commentsVisitManagerReportDetail_tv;

    @BindView(R.id.purposeVisitManagerReportDetail_tv)
    TextView purposeVisitManagerReportDetail_tv;

    @BindView(R.id.locationVisitManagerReportDetail_tv)
    TextView locationVisitManagerReportDetail_tv;

    @BindView(R.id.empnameVisitManagerReportDetail_tv)
    TextView empnameVisitManagerReportDetail_tv;

    @BindView(R.id.dateVisitManagerReport_tv)
    TextView dateVisitManagerReport_tv;

    @BindView(R.id.clientNameVisitManagerReportDetail_tv)
    TextView clientNameVisitManagerReportDetail_tv;

    @BindView(R.id.minusVisitManagerReportDetail_iv)
    ImageView minusVisitManagerReportDetail_iv;

    //Layout
    @BindView(R.id.viewVisitManagerReportHeader_rl)
    RelativeLayout viewVisitManagerReportHeader_rl;

    @BindView(R.id.viewVisitManagerReport_rv)
    RecyclerView viewVisitManagerReport_rv;

    @BindView(R.id.viewVisitManagerReport_cv)
    CardView viewVisitManagerReport_cv;

    //Search Opertaion
    @BindView(R.id.employeeAdvanceSearchReport_sp)
    Spinner employeeAdvanceSearchReport_sp;

    @BindView(R.id.fromdateAdvanceSearchReportDetail_tv)
    TextView fromdateAdvanceSearchReportDetail_tv;

    @BindView(R.id.todateAdvanceSearchReport_tv)
    TextView todateAdvanceSearchReport_tv;

    @BindView(R.id.submitAdvanceSearchReport_btn)
    Button submitAdvanceSearchReport_btn;

    View view;

    DatePickerDialog datePickerDialog;
    ProgressDialog pDialog;

    SharedPreferences sharedPref;
    String userIdPref, userTypePref, user_comidPref;
    String selectAssignTo, selectAssignToId;

    ArrayList<String> assignToUser;

    Map<String, String> assignToUserMap = new HashMap<>();

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    JSONParser jsonParser = new JSONParser();

    VisitManagerReportAdapter visitManagerReportAdapter;
    VisitManagerHeadReportAdapter visitManagerHeadReportAdapter;

    DashboardManagerBean.Dashboard_Count bean;
    String emp_id, process_id;
    int position;

    @BindView(R.id.drawerIcon_iv)
    ImageView drawerIcon_iv;

    VisitManagerDashboardReportAdapter visitManagerDashboardReportAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_visit_dashboard_count);
        ButterKnife.bind(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        initialiseUI();
    }

    private void initialiseUI(){
        sharedPref = PreferenceManager.getDefaultSharedPreferences(VisitDashboardCountActivity.this);
        userIdPref = sharedPref.getString("user_id", "");
        userTypePref = sharedPref.getString("user_type", "");
        user_comidPref = sharedPref.getString("user_com_id", "");

        bean = VisitDashboardCountActivity.this.getIntent().getParcelableExtra("bean");
        position = VisitDashboardCountActivity.this.getIntent().getIntExtra("position", 0);
        emp_id = bean.getUser_id();

        viewVisitManagerReportHeader_rl.setVisibility(View.VISIBLE);
        viewVisitManagerReport_cv.setVisibility(View.GONE);
        if (userTypePref.equals("Sales Manager")) {
            viewVisitManagerReportHeader_rl.setVisibility(View.VISIBLE);

            getDefaultVisitReportManagerRecyclerView();
            selectAssignTo();
        }else if (userTypePref.equals("Manager Head")){
            getDefaultVisitReportManagerHeadRecyclerView();
            selectAssignToMgrHead();
        }

    }

    @OnClick(R.id.minusVisitManagerReportDetail_iv)
    public void hideDetail(){
        viewVisitManagerReportHeader_rl.setVisibility(View.VISIBLE);
        viewVisitManagerReport_cv.setVisibility(View.GONE);

        if (userTypePref.equals("Sales Manager")) {
            getDefaultVisitReportManagerRecyclerView();
            selectAssignTo();
        }else if (userTypePref.equals("Manager Head")){
            getDefaultVisitReportManagerHeadRecyclerView();
            selectAssignToMgrHead();
        }
    }

    private void selectAssignTo(){
        try{
            if (Connectivity.isConnected(VisitDashboardCountActivity.this)) {
                String Url = ApiLink.ROOT_URL + ApiLink.Dashboard_SalesPerson;
                Map<String, String> map = new HashMap<>();
                map.put("users_undermanager","" );
                map.put("user_comid", user_comidPref);
                map.put("user_reporting_to", userIdPref);


                final GSONRequest<TaskMeetingBean> targetAssignToGsonRequest = new GSONRequest<TaskMeetingBean>(
                        Request.Method.POST,
                        Url,
                        TaskMeetingBean.class,map,
                        new com.android.volley.Response.Listener<TaskMeetingBean>() {
                            @Override
                            public void onResponse(TaskMeetingBean response) {
                                assignToUser.clear();
                                assignToUser.add("Assign To");
                                for(int i=0;i<response.getUsers_dd1().size();i++)
                                {
                                    assignToUser.add(response.getUsers_dd1().get(i).getUser_name());
                                    assignToUserMap.put(response.getUsers_dd1().get(i).getUser_id(), response.getUsers_dd1().get(i).getUser_name());
                                }
                            }
                        },
                        new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Utilities.serverError(VisitDashboardCountActivity.this);
                            }
                        });
                targetAssignToGsonRequest.setShouldCache(false);
                Utilities.getRequestQueue(VisitDashboardCountActivity.this).add(targetAssignToGsonRequest);
            }
            assignToUser = new ArrayList<String>();
            assignToUser.clear();
            assignToUser.add("Assign To");
            ArrayAdapter<String> quotationLocationDataAdapter = new ArrayAdapter<String>(VisitDashboardCountActivity.this, R.layout.spinner_textview, assignToUser);
            quotationLocationDataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
            employeeAdvanceSearchReport_sp.setAdapter(quotationLocationDataAdapter);

        }catch (Exception e){
        }
    }

    private void selectAssignToMgrHead(){
        try{
            if (Connectivity.isConnected(VisitDashboardCountActivity.this)) {
                String Url = ApiLink.ROOT_URL + ApiLink.CALL_MANAGER_HEAD_NOTIFICATION;
                Map<String, String> map = new HashMap<>();
                map.put("get_users","" );
                map.put("managerhead_id", userIdPref);

                final GSONRequest<TaskMeetingBean> targetAssignToGsonRequest = new GSONRequest<TaskMeetingBean>(
                        Request.Method.POST,
                        Url,
                        TaskMeetingBean.class,map,
                        new com.android.volley.Response.Listener<TaskMeetingBean>() {
                            @Override
                            public void onResponse(TaskMeetingBean response) {
                                assignToUser.clear();
                                assignToUser.add("Employee Name");
                                for(int i=0;i<response.getUsers_dd1().size();i++)
                                {
                                    assignToUser.add(response.getUsers_dd1().get(i).getUser_name());
                                    assignToUserMap.put(response.getUsers_dd1().get(i).getUser_id(), response.getUsers_dd1().get(i).getUser_name());
                                }
                            }
                        },
                        new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Utilities.serverError(VisitDashboardCountActivity.this);
                            }
                        });
                targetAssignToGsonRequest.setShouldCache(false);
                Utilities.getRequestQueue(VisitDashboardCountActivity.this).add(targetAssignToGsonRequest);
            }
            assignToUser = new ArrayList<String>();
            assignToUser.clear();
            assignToUser.add("Employee Name");
            ArrayAdapter<String> quotationLocationDataAdapter = new ArrayAdapter<String>(VisitDashboardCountActivity.this, R.layout.spinner_textview, assignToUser);
            quotationLocationDataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
            employeeAdvanceSearchReport_sp.setAdapter(quotationLocationDataAdapter);

        }catch (Exception e){
        }
    }

    @OnItemSelected(R.id.employeeAdvanceSearchReport_sp)
    public void assignToAddSelected(Spinner spinner, int position) {
        selectAssignTo = spinner.getSelectedItem().toString();
        for (Map.Entry<String, String> e : assignToUserMap.entrySet()) {
            Object key = e.getKey();
            Object value = e.getValue();
            if (value.equals(selectAssignTo)) {
                selectAssignToId = (String) key;
            }
        }
    }

    @OnClick(R.id.fromdateAdvanceSearchReportDetail_tv)
    public void startDate(){
        final Calendar calenderObj = Calendar.getInstance();
        int mYear = calenderObj.get(Calendar.YEAR);
        int mMonth = calenderObj.get(Calendar.MONTH);
        int mDay = calenderObj.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(VisitDashboardCountActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        fromdateAdvanceSearchReportDetail_tv.setText(year + "-" + (monthOfYear + 1)+ "-" + dayOfMonth);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    @OnClick(R.id.todateAdvanceSearchReport_tv)
    public void endDate(){
        final Calendar calenderObj = Calendar.getInstance();
        int mYear = calenderObj.get(Calendar.YEAR);
        int mMonth = calenderObj.get(Calendar.MONTH);
        int mDay = calenderObj.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(VisitDashboardCountActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        todateAdvanceSearchReport_tv.setText(year + "-" + (monthOfYear + 1)+ "-" + dayOfMonth);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    @OnClick(R.id.submitAdvanceSearchReport_btn)
    public void submitAdvanceSearch(){

        if (userTypePref.equals("Sales Manager")) {

            if (!selectAssignTo.equals("Assign To")) {
                if (!(fromdateAdvanceSearchReportDetail_tv.getText().toString().equals(""))){
                    if (!(todateAdvanceSearchReport_tv.getText().toString().equals(""))) {
                        getAllVisitReportManagerRecyclerView();
                    }else{
                        Toast.makeText(VisitDashboardCountActivity.this, "Please Select End Date", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(VisitDashboardCountActivity.this, "Please Select Start Date", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(VisitDashboardCountActivity.this, "Please Select Assigned To", Toast.LENGTH_SHORT).show();
            }

        }else if (userTypePref.equals("Manager Head")) {
            if (!selectAssignTo.equals("Employee Name")) {
                if (!(fromdateAdvanceSearchReportDetail_tv.getText().toString().equals(""))) {
                    if (!(todateAdvanceSearchReport_tv.getText().toString().equals(""))) {
                        getAllVisitReportManagerHeadRecyclerView();

                    } else {
                        Toast.makeText(VisitDashboardCountActivity.this, "Please Select End Date", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(VisitDashboardCountActivity.this, "Please Select Start Date", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(VisitDashboardCountActivity.this, "Please Select Employee Name", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @OnClick(R.id.drawerIcon_iv)
    public void drawerIconDashboard(){
        Intent backIntent  =new Intent(VisitDashboardCountActivity.this, NavigationDrawerActivity.class);
        backIntent.putExtra("drawer_Open", "open_track_sales" );
        startActivity(backIntent);

    }
    private void getAllVisitReportManagerRecyclerView(){
        try {
            if (Connectivity.isConnected(VisitDashboardCountActivity.this)) {

                String fromd = fromdateAdvanceSearchReportDetail_tv.getText().toString();
                String tod = todateAdvanceSearchReport_tv.getText().toString();

                String Url = ApiLink.ROOT_URL + ApiLink.VISIT_PENDING_REPORT;
                Map<String, String> map = new HashMap<>();
                map.put("logged_manager_id", userIdPref);
                map.put("add", "");
                map.put("emp_id", selectAssignToId);
                map.put("startdate", fromd);
                map.put("enddate", tod);

                GSONRequest<VisitDoneDashboardManagerBean> dashboardGsonRequest = new GSONRequest<VisitDoneDashboardManagerBean>(
                        Request.Method.POST,
                        Url,
                        VisitDoneDashboardManagerBean.class, map,
                        new com.android.volley.Response.Listener<VisitDoneDashboardManagerBean>() {
                            @Override
                            public void onResponse(VisitDoneDashboardManagerBean response) {
                                try {
                                    if (response.getSp_all_visits().size() > 0) {
                                        viewVisitManagerReportHeader_rl.setVisibility(View.VISIBLE);

                                        visitManagerDashboardReportAdapter = new VisitManagerDashboardReportAdapter(VisitDashboardCountActivity.this, response.getSp_all_visits());
                                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(VisitDashboardCountActivity.this, LinearLayoutManager.VERTICAL, false);
                                        viewVisitManagerReport_rv.setLayoutManager(mLayoutManager);
                                        viewVisitManagerReport_rv.setItemAnimator(new DefaultItemAnimator());
                                        viewVisitManagerReport_rv.setAdapter(visitManagerDashboardReportAdapter);
                                        viewVisitManagerReport_rv.setNestedScrollingEnabled(false);


                                    }
                                } catch (Exception e) {
                                    viewVisitManagerReportHeader_rl.setVisibility(View.GONE);

                                    e.printStackTrace();
                                    Toast.makeText(VisitDashboardCountActivity.this, "Api response Problem", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                viewVisitManagerReportHeader_rl.setVisibility(View.GONE);
                            }
                        });
                dashboardGsonRequest.setShouldCache(false);
                Utilities.getRequestQueue(VisitDashboardCountActivity.this).add(dashboardGsonRequest);
            }
        }catch (Exception e){

        }
    }

    private void getAllVisitReportManagerHeadRecyclerView(){
        try {
            if (Connectivity.isConnected(VisitDashboardCountActivity.this)) {

                String fromd = fromdateAdvanceSearchReportDetail_tv.getText().toString();
                String tod = todateAdvanceSearchReport_tv.getText().toString();

                String Url = ApiLink.ROOT_URL + ApiLink.VISIT_MANAGER_HEAD_REPORT;
                Map<String, String> map = new HashMap<>();
                map.put("logged_head_manager_id", userIdPref);
                map.put("add", "");
                map.put("emp_id", selectAssignToId);
                map.put("startdate", fromd);
                map.put("enddate", tod);

                GSONRequest<AllVisitReportMgrHeadBean> dashboardGsonRequest = new GSONRequest<AllVisitReportMgrHeadBean>(
                        Request.Method.POST,
                        Url,
                        AllVisitReportMgrHeadBean.class, map,
                        new com.android.volley.Response.Listener<AllVisitReportMgrHeadBean>() {
                            @Override
                            public void onResponse(AllVisitReportMgrHeadBean response) {
                                try {
                                    if (response.getVisits_report().size() > 0) {
                                        viewVisitManagerReportHeader_rl.setVisibility(View.VISIBLE);

                                        visitManagerHeadReportAdapter = new VisitManagerHeadReportAdapter(VisitDashboardCountActivity.this, response.getVisits_report());
                                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(VisitDashboardCountActivity.this, LinearLayoutManager.VERTICAL, false);
                                        viewVisitManagerReport_rv.setLayoutManager(mLayoutManager);
                                        viewVisitManagerReport_rv.setItemAnimator(new DefaultItemAnimator());
                                        viewVisitManagerReport_rv.setAdapter(visitManagerHeadReportAdapter);
                                        viewVisitManagerReport_rv.setNestedScrollingEnabled(false);

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    //  Toast.makeText(getActivity(), "Api response Problem", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                viewVisitManagerReportHeader_rl.setVisibility(View.VISIBLE);
                            }
                        });
                dashboardGsonRequest.setShouldCache(false);
                Utilities.getRequestQueue(VisitDashboardCountActivity.this).add(dashboardGsonRequest);
            }
        }catch (Exception e){

        }
    }

    private void getDefaultVisitReportManagerRecyclerView(){
        try {
            if (Connectivity.isConnected(VisitDashboardCountActivity.this)) {

                String Url =  ApiLink.ROOT_URL + ApiLink.VISIT_PENDING_REPORT;
                Map<String, String> map = new HashMap<>();
                map.put("emp_id", emp_id);
                map.put("add_done", "");
                map.put("logged_manager_id", userIdPref);

                GSONRequest<VisitDoneDashboardManagerBean> dashboardGsonRequest = new GSONRequest<VisitDoneDashboardManagerBean>(
                        Request.Method.POST,
                        Url,
                        VisitDoneDashboardManagerBean.class, map,
                        new com.android.volley.Response.Listener<VisitDoneDashboardManagerBean>() {
                            @Override
                            public void onResponse(VisitDoneDashboardManagerBean response) {
                                try {
                                    if (response.getSp_all_visits().size() > 0) {

                                        visitManagerDashboardReportAdapter = new VisitManagerDashboardReportAdapter(VisitDashboardCountActivity.this, response.getSp_all_visits());
                                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(VisitDashboardCountActivity.this, LinearLayoutManager.VERTICAL, false);
                                        viewVisitManagerReport_rv.setLayoutManager(mLayoutManager);
                                        viewVisitManagerReport_rv.setItemAnimator(new DefaultItemAnimator());
                                        viewVisitManagerReport_rv.setAdapter(visitManagerDashboardReportAdapter);
                                        viewVisitManagerReport_rv.setNestedScrollingEnabled(false);

                                    }
                                } catch (Exception e) {

                                    viewVisitManagerReportHeader_rl.setVisibility(View.GONE);
                                    e.printStackTrace();
                                    Toast.makeText(VisitDashboardCountActivity.this, "Api response Problem", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                viewVisitManagerReportHeader_rl.setVisibility(View.GONE);

                            }
                        });
                dashboardGsonRequest.setShouldCache(false);
                Utilities.getRequestQueue(VisitDashboardCountActivity.this).add(dashboardGsonRequest);
            }
        }catch(Exception e){
            Toast.makeText(VisitDashboardCountActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    private void getDefaultVisitReportManagerHeadRecyclerView(){
        try {
            if (Connectivity.isConnected(VisitDashboardCountActivity.this)) {

                String Url = ApiLink.ROOT_URL + ApiLink.VISIT_MANAGER_HEAD_REPORT;
                Map<String, String> map = new HashMap<>();
                map.put("select", "select");
                map.put("all", "all");
                map.put("user_reporting_to", userIdPref);

                GSONRequest<AllVisitReportMgrHeadBean> dashboardGsonRequest = new GSONRequest<AllVisitReportMgrHeadBean>(
                        Request.Method.POST,
                        Url,
                        AllVisitReportMgrHeadBean.class, map,
                        new com.android.volley.Response.Listener<AllVisitReportMgrHeadBean>() {
                            @Override
                            public void onResponse(AllVisitReportMgrHeadBean response) {
                                try {
                                    if (response.getVisits_report().size() > 0) {
                                        viewVisitManagerReportHeader_rl.setVisibility(View.VISIBLE);

                                        visitManagerHeadReportAdapter = new VisitManagerHeadReportAdapter(VisitDashboardCountActivity.this, response.getVisits_report());
                                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(VisitDashboardCountActivity.this, LinearLayoutManager.VERTICAL, false);
                                        viewVisitManagerReport_rv.setLayoutManager(mLayoutManager);
                                        viewVisitManagerReport_rv.setItemAnimator(new DefaultItemAnimator());
                                        viewVisitManagerReport_rv.setAdapter(visitManagerHeadReportAdapter);
                                        viewVisitManagerReport_rv.setNestedScrollingEnabled(false);

                                    }
                                } catch (Exception e) {

                                    viewVisitManagerReportHeader_rl.setVisibility(View.GONE);
                                    e.printStackTrace();
                                    //    Toast.makeText(getActivity(), "Api response Problem", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                viewVisitManagerReportHeader_rl.setVisibility(View.GONE);
                            }
                        });
                dashboardGsonRequest.setShouldCache(false);
                Utilities.getRequestQueue(VisitDashboardCountActivity.this).add(dashboardGsonRequest);
            }
        }catch(Exception e){
            Toast.makeText(VisitDashboardCountActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    public void getAllVisitData(VisitDoneDashboardManagerBean.SP_All_Visits bean){
        viewVisitManagerReportHeader_rl.setVisibility(View.GONE);
        viewVisitManagerReport_cv.setVisibility(View.VISIBLE);

        meetingtimeVisitManagerReport_tv.setText(convertIn12Hours(bean.getMeeting_time()));
        commentsVisitManagerReportDetail_tv.setText(bean.getVisit_comments());
        purposeVisitManagerReportDetail_tv.setText(bean.getPurpose_name());
        locationVisitManagerReportDetail_tv.setText(bean.getVisit_address());
        empnameVisitManagerReportDetail_tv.setText(bean.getUser_name());
        dateVisitManagerReport_tv.setText(bean.getMeeting_dt());
        clientNameVisitManagerReportDetail_tv.setText(bean.getLead_company());

    }

    public void getAllVisitMgrHead(VisitDoneReportManagerBean.sp_Done_Visit bean){
        viewVisitManagerReportHeader_rl.setVisibility(View.GONE);
        viewVisitManagerReport_cv.setVisibility(View.VISIBLE);

        meetingtimeVisitManagerReport_tv.setText(convertIn12Hours(bean.getMeeting_time()));
        commentsVisitManagerReportDetail_tv.setText(bean.getVisit_comments());
        purposeVisitManagerReportDetail_tv.setText(bean.getPurpose_name());
        locationVisitManagerReportDetail_tv.setText(bean.getVisit_address());
        empnameVisitManagerReportDetail_tv.setText(bean.getUser_name());
        dateVisitManagerReport_tv.setText(bean.getMeeting_dt());
        clientNameVisitManagerReportDetail_tv.setText(bean.getLead_company());

    }

    private String convertIn12Hours(String time){

        String timeToDisplay = "";
        String[] timeArray = time.split(":");
        Integer hours = Integer.parseInt(timeArray[0]);

        if(hours > 12){
            Integer diff = (24-hours);
            timeToDisplay = (12 - diff) + ":" +  timeArray[1] + " PM";
        }else{
            timeToDisplay = timeArray[0] + ":" + timeArray[1] + " AM";
        }

        return timeToDisplay;
    }

}

