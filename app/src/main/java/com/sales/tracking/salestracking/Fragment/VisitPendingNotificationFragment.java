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
import com.sales.tracking.salestracking.Adapter.ViewVisitTaskSpAdapter;
import com.sales.tracking.salestracking.Adapter.VisitPendingAdapter;
import com.sales.tracking.salestracking.Bean.VisitTaskSpBean;
import com.sales.tracking.salestracking.R;
import com.sales.tracking.salestracking.Utility.ApiLink;
import com.sales.tracking.salestracking.Utility.Connectivity;
import com.sales.tracking.salestracking.Utility.GSONRequest;
import com.sales.tracking.salestracking.Utility.JSONParser;
import com.sales.tracking.salestracking.Utility.Utilities;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class VisitPendingNotificationFragment extends Fragment {

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

    @BindView(R.id.followUpDateTimeViewSaleCallPending_rl)
    RelativeLayout followUpDateTimeViewSaleCallPending_rl;

    @BindView(R.id.separatorBelowStatusViewSaleCallPending)
    View separatorBelowStatusViewSaleCallPending;

    Integer count = 0;

    VisitPendingAdapter visitPendingAdapter;
    ArrayList<VisitTaskSpBean.Single_sp_all_Meetings> spAttendanceList = new ArrayList<>();


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

        titleViewSaleCallPending_tv.setText("Visit Pending");

        viewSaleCallPendingDetails_cv.setVisibility(View.GONE);
        salesCallPendingHeader_rl.setVisibility(View.VISIBLE);


        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        userIdPref = sharedPref.getString("user_id", "");
        userTypePref = sharedPref.getString("user_type", "");
        user_comidPref = sharedPref.getString("user_com_id", "");
        if (userTypePref.equals("Sales Executive")) {
            getViewPendingCount();
            getviewVisitTaskSpRecyclerView();
        }

        //  getProfileDetails();
    }

    private void getviewVisitTaskSpRecyclerView(){
        if (Connectivity.isConnected(getActivity())) {

            String Url = ApiLink.ROOT_URL + ApiLink.Dashboard_SalesPerson;
            Map<String, String> map = new HashMap<>();
            map.put("select", "");
            map.put("all", "");
            map.put("visit_uid", userIdPref);

            GSONRequest<VisitTaskSpBean> dashboardGsonRequest = new GSONRequest<VisitTaskSpBean>(
                    Request.Method.POST,
                    Url,
                    VisitTaskSpBean.class, map,
                    new com.android.volley.Response.Listener<VisitTaskSpBean>() {
                        @Override
                        public void onResponse(VisitTaskSpBean response) {
                            try{
                                if (response.getSingle_sp_all_meetings().size()>0){
                                    for (int i = 0; i<response.single_sp_all_meetings.size(); i++) {
                                        if (response.getSingle_sp_all_meetings().get(i).getVisit_status().equals("Pending")){
                                            spAttendanceList.clear();
                                            spAttendanceList.addAll(response.getSingle_sp_all_meetings());

                                            visitPendingAdapter = new VisitPendingAdapter(getActivity(), response.getSingle_sp_all_meetings(), VisitPendingNotificationFragment.this);
                                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                            viewSaleCallPending_rv.setLayoutManager(mLayoutManager);
                                            viewSaleCallPending_rv.setItemAnimator(new DefaultItemAnimator());
                                            viewSaleCallPending_rv.setAdapter(visitPendingAdapter);
                                            viewSaleCallPending_rv.setNestedScrollingEnabled(false);
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

    private void getViewPendingCount(){
        if (Connectivity.isConnected(getActivity())) {

            String Url = ApiLink.ROOT_URL + ApiLink.Dashboard_SalesPerson;
            Map<String, String> map = new HashMap<>();
            map.put("count_sp_meetings", "");
            map.put("visit_uid", userIdPref);

            GSONRequest<VisitTaskSpBean> dashboardGsonRequest = new GSONRequest<VisitTaskSpBean>(
                    Request.Method.POST,
                    Url,
                    VisitTaskSpBean.class, map,
                    new com.android.volley.Response.Listener<VisitTaskSpBean>() {
                        @Override
                        public void onResponse(VisitTaskSpBean response) {
                            try{
                                if (response.getMeeting_count().size()>0){
                                    countCallPending_tv.setText(response.getMeeting_count().get(0).getTot_meetings());
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

    @OnClick(R.id.minusViewSaleCallPendingDetail_iv)
    public void hideSDetails(){
        viewSaleCallPendingDetails_cv.setVisibility(View.GONE);
        salesCallPendingHeader_rl.setVisibility(View.VISIBLE);

        if (userTypePref.equals("Sales Executive")) {
            getviewVisitTaskSpRecyclerView();
        }
    }

    public void getViewVisitSpTask(VisitTaskSpBean.Single_sp_all_Meetings bean){

        viewSaleCallPendingDetails_cv.setVisibility(View.VISIBLE);
        salesCallPendingHeader_rl.setVisibility(View.GONE);

        String indate = bean.getVisit_datetime();
        String[] indate1 = indate.split( " ");

        String followdate = bean.getVisit_followup_date();
        String[] followdate1 = followdate.split( " ");

        clientNameViewSaleCallPendingDetail_tv.setText(bean.getLead_company());
        dateViewSaleCallPending_tv.setText(indate1[0]);
        timeViewSaleCallPendingTask_tv.setText(convertIn12Hours(indate1[1]));

        assignByViewSaleCallPending_tv.setText(bean.getUser_name());
        descriptionViewSaleCallPending_tv.setText(bean.getVisit_comments());
        purposeViewSaleCallPending_tv.setText(bean.getPurpose_name());
        statusViewSaleCallPending_tv.setText(bean.getVisit_status());
        addressViewSaleCallPending_tv.setText(bean.getVisit_address());

        if (!bean.getVisit_photo().equals("")){
            Picasso.get().load(ApiLink.IMAGE_BASE_URL + bean.getVisit_photo()).into(photoViewSaleCallPending_iv);
        }else{
            photoViewSaleCallPending_iv.setImageDrawable(getResources().getDrawable(R.drawable.default_img));
        }

        if (bean.getVisit_status().toString().equals("Followup")){
            followUpDateTimeViewSaleCallPending_rl.setVisibility(View.VISIBLE);
            separatorBelowStatusViewSaleCallPending.setVisibility(View.VISIBLE);
        }else{
            followUpDateTimeViewSaleCallPending_rl.setVisibility(View.GONE);
            separatorBelowStatusViewSaleCallPending.setVisibility(View.GONE);
        }
        followUpDateTimeViewSaleCallPending_tv.setText(followdate1[0] + " / " + convertIn12Hours(followdate1[1]));

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
