package com.sales.tracking.salestracking.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.sales.tracking.salestracking.Activity.NavigationDrawerActivity;
import com.sales.tracking.salestracking.Adapter.DashboardManagerAdapter;
import com.sales.tracking.salestracking.Adapter.TodaysTaskSalesPersonAdapter;
import com.sales.tracking.salestracking.Bean.DashboardManagerBean;
import com.sales.tracking.salestracking.Bean.DashboardSalesPersonBean;
import com.sales.tracking.salestracking.R;
import com.sales.tracking.salestracking.Utility.ApiLink;
import com.sales.tracking.salestracking.Utility.Connectivity;
import com.sales.tracking.salestracking.Utility.GSONRequest;
import com.sales.tracking.salestracking.Utility.Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class DashboardFragment extends Fragment {

    @BindView(R.id.dashboardDetail_rv)
    RecyclerView dashboardDetail_rv;

    @BindView(R.id.taskDetailsSalesPerson_cv)
    CardView taskDetailsSalesPerson_cv;

    @BindView(R.id.todayTaskSalesPerson_rv)    //Recycler view for Today's Task Sales Person
    RecyclerView todayTaskSalesPerson_rv;

    @BindView(R.id.salesHeader_rl)
    RelativeLayout salesHeader_rl;

    @BindView(R.id.countTotalMeeting_tv)
    TextView countTotalMeeting_tv;

    @BindView(R.id.countTotalCalls_tv)
    TextView countTotalCalls_tv;

    @BindView(R.id.countTotalLeads_tv)
    TextView countTotalLeads_tv;

    //sales person details View
    @BindView(R.id.minusDashboardTodaysTask_iv)
    ImageView minusDashboardTodaysTask_iv;

    @BindView(R.id.assignByTaskSalePersonValue_tv)
    TextView assignByTaskSalePersonValue_tv;

    @BindView(R.id.addressTaskSalePersonValue_tv)
    TextView addressTaskSalePersonValue_tv;

    @BindView(R.id.purposeTaskSalePersonValue_tv)
    TextView purposeTaskSalePersonValue_tv ;

    @BindView(R.id.clientNameValueTodaysTaskDetails_tv)
    TextView clientNameValueTodaysTaskDetails_tv;

    @BindView(R.id.timeValueTodaysTaskDetails_tv)
    TextView timeValueTodaysTaskDetails_tv;

    View view;
    TodaysTaskSalesPersonAdapter todaysTaskSalesPersonAdapter;
    DashboardManagerAdapter dashboardManagerAdapter;
    SharedPreferences sharedPref;
    String userNamePref, userEmailPref, userTypePref, userIdPref;

    ArrayList<DashboardSalesPersonBean.sp_meetings_today> spMeetingTodayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ButterKnife.bind(this, view);
        initialiseUI();
        return view;
    }

    private void initialiseUI(){

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        userNamePref = sharedPref.getString("user_name", "");
        userEmailPref = sharedPref.getString("user_email", "");
        userTypePref = sharedPref.getString("user_type", "");
        userIdPref = sharedPref.getString("user_id", "");

        if (userTypePref.equals("Sales Executive")){

            salesHeader_rl.setVisibility(View.VISIBLE);
            dashboardDetail_rv.setVisibility(View.GONE);
            getTodayMeeting();
            getTotalLeads();
            getTotalCalls();
            getTodaysTaskRecyclerView();
            taskDetailsSalesPerson_cv.setVisibility(View.GONE);
        }else if (userTypePref.equals("Sales Manager")){
            salesHeader_rl.setVisibility(View.GONE);
            dashboardDetail_rv.setVisibility(View.VISIBLE);

            getDashboardManagerRecyclerView();
        }else if (userTypePref.equals("Manager Head")){
            salesHeader_rl.setVisibility(View.GONE);
            dashboardDetail_rv.setVisibility(View.VISIBLE);

            getDashboardManagerHeadRecyclerView();
        }

    }

    private void getTotalCalls(){
        if (Connectivity.isConnected(getActivity())) {

            String Url = ApiLink.ROOT_URL + ApiLink.Dashboard_SalesPerson;
            Map<String, String> map = new HashMap<>();
            map.put("count_b_calls", "");
            map.put("service_uid", userIdPref);

            GSONRequest<DashboardSalesPersonBean> dashboardGsonRequest = new GSONRequest<DashboardSalesPersonBean>(
                    Request.Method.POST,
                    Url,
                    DashboardSalesPersonBean.class, map,
                    new com.android.volley.Response.Listener<DashboardSalesPersonBean>() {
                        @Override
                        public void onResponse(DashboardSalesPersonBean response) {
                            try{
                                if (response.getCalls_count().size()>0){
                                    countTotalCalls_tv.setText(response.getCalls_count().get(0).getTot_calls());
                                }
                            }catch(Exception e){
                     //           Toast.makeText(getActivity(), "Something went wrong..", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    });
            dashboardGsonRequest.setShouldCache(false);
            Utilities.getRequestQueue(getActivity()).add(dashboardGsonRequest);
        }
    }

    private void getTotalLeads(){
        if (Connectivity.isConnected(getActivity())) {

            String Url = ApiLink.ROOT_URL + ApiLink.Dashboard_SalesPerson;
            Map<String, String> map = new HashMap<>();
            map.put("count_c_leads", "");
            map.put("lead_uid", userIdPref);

            GSONRequest<DashboardSalesPersonBean> dashboardGsonRequest = new GSONRequest<DashboardSalesPersonBean>(
                    Request.Method.POST,
                    Url,
                    DashboardSalesPersonBean.class, map,
                    new com.android.volley.Response.Listener<DashboardSalesPersonBean>() {
                        @Override
                        public void onResponse(DashboardSalesPersonBean response) {
                            try{
                                if (response.getLeads_count().size()>0){
                                    countTotalLeads_tv.setText(response.getLeads_count().get(0).getTot_leads());
                                }
                            }catch(Exception e){
                             //   Toast.makeText(getActivity(), "Something went wrong..", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    });
            dashboardGsonRequest.setShouldCache(false);
            Utilities.getRequestQueue(getActivity()).add(dashboardGsonRequest);
        }
    }

    private void getTodayMeeting() {
        if (Connectivity.isConnected(getActivity())) {

            String Url = ApiLink.ROOT_URL + ApiLink.Dashboard_SalesPerson;
            Map<String, String> map = new HashMap<>();
            map.put("count_a_meetings", "");
            map.put("visit_uid", userIdPref);

            GSONRequest<DashboardSalesPersonBean> dashboardGsonRequest = new GSONRequest<DashboardSalesPersonBean>(
                    Request.Method.POST,
                    Url,
                    DashboardSalesPersonBean.class, map,
                    new com.android.volley.Response.Listener<DashboardSalesPersonBean>() {
                        @Override
                        public void onResponse(DashboardSalesPersonBean response) {
                            try{
                            if (response.getMeeting_count().size()>0){
                                countTotalMeeting_tv.setText(response.getMeeting_count().get(0).getTot_meetings());
                            }
                            }catch(Exception e){
                            //    Toast.makeText(getActivity(), "Something went wrong..", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    });
            dashboardGsonRequest.setShouldCache(false);
            Utilities.getRequestQueue(getActivity()).add(dashboardGsonRequest);
        }
    }

    private void getTodaysTaskRecyclerView(){
        if (Connectivity.isConnected(getActivity())) {

            String Url = ApiLink.ROOT_URL + ApiLink.Dashboard_SalesPerson;
            Map<String, String> map = new HashMap<>();
            map.put("visit_uid", userIdPref);
            map.put("sel_user_dash", "");
            map.put("today", "");

            GSONRequest<DashboardSalesPersonBean> dashboardGsonRequest = new GSONRequest<DashboardSalesPersonBean>(
                    Request.Method.POST,
                    Url,
                    DashboardSalesPersonBean.class, map,
                    new com.android.volley.Response.Listener<DashboardSalesPersonBean>() {
                        @Override
                        public void onResponse(DashboardSalesPersonBean response) {
                            try{
                                if (response.getSp_meetings_today().size()>0){
                                    spMeetingTodayList.clear();
                                    spMeetingTodayList.addAll(response.getSp_meetings_today());

                                    todaysTaskSalesPersonAdapter = new TodaysTaskSalesPersonAdapter(getActivity(),response.getSp_meetings_today(), DashboardFragment.this);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                    todayTaskSalesPerson_rv.setLayoutManager(mLayoutManager);
                                    todayTaskSalesPerson_rv.setItemAnimator(new DefaultItemAnimator());
                                    todayTaskSalesPerson_rv.setAdapter(todaysTaskSalesPersonAdapter);

                                }
                            }catch(Exception e){

                            }
                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    });
            dashboardGsonRequest.setShouldCache(false);
            Utilities.getRequestQueue(getActivity()).add(dashboardGsonRequest);
        }
    }

    private void getDashboardManagerRecyclerView(){
        if (Connectivity.isConnected(getActivity())) {

            String Url  =ApiLink.ROOT_URL + ApiLink.DASHBOARD_MANAGER;
            //String Url = "http://arizonamediaz.co.in/sales_tracking/api/manager_dashboard.php";
            Map<String, String> map = new HashMap<>();
            map.put("manager_id", userIdPref);

            GSONRequest<DashboardManagerBean> dashboardGsonRequest = new GSONRequest<DashboardManagerBean>(
                    Request.Method.POST,
                    Url,
                    DashboardManagerBean.class, map,
                    new com.android.volley.Response.Listener<DashboardManagerBean>() {
                        @Override
                        public void onResponse(DashboardManagerBean response) {
                            try{
                                if (response.getDashboard_count().size()>0){

                                    dashboardManagerAdapter = new DashboardManagerAdapter(getActivity(),response.getDashboard_count());
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                    dashboardDetail_rv.setLayoutManager(mLayoutManager);
                                    dashboardDetail_rv.setItemAnimator(new DefaultItemAnimator());
                                    dashboardDetail_rv.setAdapter(dashboardManagerAdapter);

                                }
                            }catch(Exception e){
                                //     Toast.makeText(getActivity(), "Something went wrong..", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    });
            dashboardGsonRequest.setShouldCache(false);
            Utilities.getRequestQueue(getActivity()).add(dashboardGsonRequest);
        }
    }

    private void getDashboardManagerHeadRecyclerView(){
        if (Connectivity.isConnected(getActivity())) {

            String Url =ApiLink.ROOT_URL + ApiLink.DASHBOARD_MANAGERHEAD;
            Map<String, String> map = new HashMap<>();
            map.put("managerhead_id", userIdPref);

            GSONRequest<DashboardManagerBean> dashboardGsonRequest = new GSONRequest<DashboardManagerBean>(
                    Request.Method.POST,
                    Url,
                    DashboardManagerBean.class, map,
                    new com.android.volley.Response.Listener<DashboardManagerBean>() {
                        @Override
                        public void onResponse(DashboardManagerBean response) {
                            try{
                                if (response.getDashboard_count().size()>0){

                                    dashboardManagerAdapter = new DashboardManagerAdapter(getActivity(),response.getDashboard_count());
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                    dashboardDetail_rv.setLayoutManager(mLayoutManager);
                                    dashboardDetail_rv.setItemAnimator(new DefaultItemAnimator());
                                    dashboardDetail_rv.setAdapter(dashboardManagerAdapter);

                                }
                            }catch(Exception e){
                                //     Toast.makeText(getActivity(), "Something went wrong..", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    });
            dashboardGsonRequest.setShouldCache(false);
            Utilities.getRequestQueue(getActivity()).add(dashboardGsonRequest);
        }
    }

    public void getTotalTaskBean(DashboardSalesPersonBean.sp_meetings_today bean){

        todayTaskSalesPerson_rv.setVisibility(View.GONE);
        taskDetailsSalesPerson_cv.setVisibility(View.VISIBLE);

        String indate = bean.getVisit_datetime();
        String[] indate1 = indate.split( " ");

        timeValueTodaysTaskDetails_tv.setText(convertIn12Hours(indate1[1]));
        clientNameValueTodaysTaskDetails_tv.setText(bean.getLead_company());
        assignByTaskSalePersonValue_tv.setText(bean.getUser_name());
        addressTaskSalePersonValue_tv.setText(bean.getVisit_address());
        purposeTaskSalePersonValue_tv.setText(bean.getPurpose_name());
    }

    public String convertIn12Hours(String time){

        String timeToDisplay = "";
        String[] timeArray = time.split(":");
        Integer hours = Integer.parseInt(timeArray[0]);

        if(hours > 12){
            timeToDisplay = (24 - hours) + ":" +  timeArray[1] + " PM";
        }else{
            timeToDisplay = timeArray[0] + ":" + timeArray[1] + " AM";
        }

        return timeToDisplay;
    }

    @OnClick(R.id.minusDashboardTodaysTask_iv)
    public void hideDetailsTask(){
        todayTaskSalesPerson_rv.setVisibility(View.VISIBLE);
        taskDetailsSalesPerson_cv.setVisibility(View.GONE);
    }

}
