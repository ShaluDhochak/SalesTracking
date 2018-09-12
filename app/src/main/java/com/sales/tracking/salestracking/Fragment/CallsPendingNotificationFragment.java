package com.sales.tracking.salestracking.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import com.sales.tracking.salestracking.Adapter.CallPendingAdapter;
import com.sales.tracking.salestracking.Adapter.ViewSaleCallTaskAdater;
import com.sales.tracking.salestracking.Adapter.VisitPendingAdapter;
import com.sales.tracking.salestracking.Bean.SalesCallTaskSpBean;
import com.sales.tracking.salestracking.Bean.VisitTaskSpBean;
import com.sales.tracking.salestracking.R;
import com.sales.tracking.salestracking.Utility.ApiLink;
import com.sales.tracking.salestracking.Utility.Connectivity;
import com.sales.tracking.salestracking.Utility.GSONRequest;
import com.sales.tracking.salestracking.Utility.JSONParser;
import com.sales.tracking.salestracking.Utility.Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class CallsPendingNotificationFragment extends Fragment {

    View view;
    SharedPreferences sharedPref;
    String userIdPref, userTypePref, user_comidPref;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    JSONParser jsonParser = new JSONParser();
    ProgressDialog pDialog;

    @BindView(R.id.viewSaleCallPendingDetails_cv)
    CardView viewSaleCallPendingDetails_cv;

    @BindView(R.id.salesCallPendingHeader_rl)
    RelativeLayout salesCallPendingHeader_rl;

    @BindView(R.id.saleCallPending_rl)
    RelativeLayout saleCallPending_rl;

    @BindView(R.id.viewSaleCallPending_rv)
    RecyclerView viewSaleCallPending_rv;

    @BindView(R.id.countCallPending_tv)
    TextView countCallPending_tv;

    @BindView(R.id.titleViewSaleCallPending_tv)
    TextView titleViewSaleCallPending_tv;



    @BindView(R.id.followUpDateTimeViewSaleCallPending_tv)
    TextView followUpDateTimeViewSaleCallPending_tv;

    @BindView(R.id.statusViewSaleCallPending_tv)
    TextView statusViewSaleCallPending_tv;

    @BindView(R.id.descriptionViewSaleCallPending_tv)
    TextView descriptionViewSaleCallPending_tv;

    @BindView(R.id.addressViewSaleCallPending_tv)
    TextView addressViewSaleCallPending_tv;

    @BindView(R.id.purposeViewSaleCallPending_tv)
    TextView purposeViewSaleCallPending_tv;

    @BindView(R.id.assignByViewSaleCallPending_tv)
    TextView assignByViewSaleCallPending_tv;

    @BindView(R.id.assignByViewSaleCallPendingHeading_tv)
    TextView assignByViewSaleCallPendingHeading_tv;

    @BindView(R.id.timeViewSaleCallPendingTask_tv)
    TextView timeViewSaleCallPendingTask_tv;

    @BindView(R.id.clientNameViewSaleCallPendingDetail_tv)
    TextView clientNameViewSaleCallPendingDetail_tv;

    @BindView(R.id.dateViewSaleCallPending_tv)
    TextView dateViewSaleCallPending_tv;

    @BindView(R.id.photoViewSaleCallPending_iv)
    ImageView photoViewSaleCallPending_iv;

    @BindView(R.id.minusViewSaleCallPendingDetail_iv)
    ImageView minusViewSaleCallPendingDetail_iv;

    //sALE cALL
    @BindView(R.id.dateViewSaleCallTask_tv)
    TextView dateViewSaleCallTask_tv;

    @BindView(R.id.cnameValueSaleCallTaskDetail_tv)
    TextView cnameValueSaleCallTaskDetail_tv;

    @BindView(R.id.contactNameSaleCallTask_tv)
    TextView contactNameSaleCallTask_tv;

    @BindView(R.id.phoneSaleCallTask_tv)
    TextView phoneSaleCallTask_tv;

    @BindView(R.id.assignToByViewSaleCallTaskHeading_tv)
    TextView assignToByViewSaleCallTaskHeading_tv;

    @BindView(R.id.assignToByViewSaleCallTask_tv)
    TextView assignToByViewSaleCallTask_tv;

    @BindView(R.id.commentsViewSaleCallTask_tv)
    TextView commentsViewSaleCallTask_tv;

    @BindView(R.id.statusViewSaleCallTask_tv)
    TextView statusViewSaleCallTask_tv;

    @BindView(R.id.minusVisitSaleCallTaskDetail_iv)
    ImageView minusVisitSaleCallTaskDetail_iv;

    Integer count = 0;

    CallPendingAdapter callPendingAdapter;
    ArrayList<SalesCallTaskSpBean.Sp_All_Service_Calls> spAttendanceList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_calls_pending_notification, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initialiseUI();
    }

    private void initialiseUI(){
        titleViewSaleCallPending_tv.setText("Calls Pending");

        viewSaleCallPendingDetails_cv.setVisibility(View.GONE);
        salesCallPendingHeader_rl.setVisibility(View.VISIBLE);


        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        userIdPref = sharedPref.getString("user_id", "");
        userTypePref = sharedPref.getString("user_type", "");
        user_comidPref = sharedPref.getString("user_com_id", "");

        if (userTypePref.equals("Sales Manager")) {
            //  getVisitTaskMeetingRecyclerView();
            assignByViewSaleCallPendingHeading_tv.setText("Assigned To");
        }else if (userTypePref.equals("Sales Executive")){
            assignByViewSaleCallPendingHeading_tv.setText("Assigned By");
            getSaleCallPendingCount();
            getviewVisitTaskSpRecyclerView();
        }

    }

    private void getviewVisitTaskSpRecyclerView(){
        if (Connectivity.isConnected(getActivity())) {

            String Url = ApiLink.ROOT_URL + ApiLink.TASK_SERVICECALL;
            Map<String, String> map = new HashMap<>();
            map.put("select", "");
            map.put("all", "");
            map.put("service_uid", userIdPref);

            GSONRequest<SalesCallTaskSpBean> dashboardGsonRequest = new GSONRequest<SalesCallTaskSpBean>(
                    Request.Method.POST,
                    Url,
                    SalesCallTaskSpBean.class, map,
                    new com.android.volley.Response.Listener<SalesCallTaskSpBean>() {
                        @Override
                        public void onResponse(SalesCallTaskSpBean response) {
                            try{
                                if (response.getSp_all_service_calls().size()>0){
                                    for (int i = 0; i<response.getSp_all_service_calls().size(); i++) {
                                        if (response.getSp_all_service_calls().get(i).getService_status().equals("Pending")) {
                                            // for (int i = 0; i<=response.getSp_att_und_mgr().size();i++){
                                            spAttendanceList.clear();
                                            spAttendanceList.addAll(response.getSp_all_service_calls());

                                            callPendingAdapter = new CallPendingAdapter(getActivity(), response.getSp_all_service_calls(), CallsPendingNotificationFragment.this);
                                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                            viewSaleCallPending_rv.setLayoutManager(mLayoutManager);
                                            viewSaleCallPending_rv.setItemAnimator(new DefaultItemAnimator());
                                            viewSaleCallPending_rv.setAdapter(callPendingAdapter);
                                        }
                                    }
                                }
                            }catch(Exception e){
                                e.printStackTrace();
                                Toast.makeText(getActivity(), "Api response Problem", Toast.LENGTH_SHORT).show();
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

    private void getSaleCallPendingCount(){
        if (Connectivity.isConnected(getActivity())) {

            String Url = ApiLink.ROOT_URL + ApiLink.Dashboard_SalesPerson;
            Map<String, String> map = new HashMap<>();
            map.put("count_sp_calls", "");
            map.put("service_uid", userIdPref);

            GSONRequest<SalesCallTaskSpBean> dashboardGsonRequest = new GSONRequest<SalesCallTaskSpBean>(
                    Request.Method.POST,
                    Url,
                    SalesCallTaskSpBean.class, map,
                    new com.android.volley.Response.Listener<SalesCallTaskSpBean>() {
                        @Override
                        public void onResponse(SalesCallTaskSpBean response) {
                            try{
                                if (response.getCalls_count().size()>0){
                                    countCallPending_tv.setText(response.getCalls_count().get(0).getTot_calls());
                                }
                            }catch(Exception e){
                                e.printStackTrace();
                                Toast.makeText(getActivity(), "Api response Problem", Toast.LENGTH_SHORT).show();
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


    @OnClick(R.id.minusVisitSaleCallTaskDetail_iv)
    public void hideSDetails(){
        viewSaleCallPendingDetails_cv.setVisibility(View.GONE);
        salesCallPendingHeader_rl.setVisibility(View.VISIBLE);
        saleCallPending_rl.setVisibility(View.GONE);


        if (userTypePref.equals("Sales Executive")) {
            getviewVisitTaskSpRecyclerView();
        }
    }

    public void getViewVisitSpTask(SalesCallTaskSpBean.Sp_All_Service_Calls bean){

        saleCallPending_rl.setVisibility(View.VISIBLE);
        viewSaleCallPendingDetails_cv.setVisibility(View.GONE);
        salesCallPendingHeader_rl.setVisibility(View.GONE);


        String date = bean.getService_createddt();
        String[] date1 = date.split(" ");
        dateViewSaleCallTask_tv.setText(date1[0]);
        cnameValueSaleCallTaskDetail_tv.setText(bean.getLead_company());
        //   dateViewSaleCallTask_tv.setText(bean.getService_createddt());
        contactNameSaleCallTask_tv.setText(bean.getService_person());
        phoneSaleCallTask_tv.setText(bean.getService_contactno());
        assignToByViewSaleCallTaskHeading_tv.setText("Assign By");
        assignToByViewSaleCallTask_tv.setText(bean.getUser_name());
        commentsViewSaleCallTask_tv.setText(bean.getService_comments());
        statusViewSaleCallTask_tv.setText(bean.getService_status());


    }

    private String convertIn12Hours(String time){

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




}
