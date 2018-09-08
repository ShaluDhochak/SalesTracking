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
import com.sales.tracking.salestracking.Adapter.AttendanceAddapter;
import com.sales.tracking.salestracking.Adapter.RequestSpAdapter;
import com.sales.tracking.salestracking.Adapter.SpAttendanceAdapter;
import com.sales.tracking.salestracking.Bean.AttendanceManagerBean;
import com.sales.tracking.salestracking.Bean.RequestSpBean;
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


public class RequestViewFragment extends Fragment {

    View view;

    @BindView(R.id.requestTask_rv)
    RecyclerView requestTask_rv;

    @BindView(R.id.viewRequestTaskDetails_cv)
    CardView viewRequestTaskDetails_cv;

    @BindView(R.id.requestTaskHeader_rl)
    RelativeLayout requestTaskHeader_rl;

    @BindView(R.id.statusRequestTask_tv)
    TextView statusRequestTask_tv;

    @BindView(R.id.commentRequestTask_tv)
    TextView commentRequestTask_tv;

    @BindView(R.id.taskViewRequestTask_tv)
    TextView taskViewRequestTask_tv;

    @BindView(R.id.typeRequestTaskDetail_tv)
    TextView typeRequestTaskDetail_tv;

    @BindView(R.id.dateViewRequestTask_tv)
    TextView dateViewRequestTask_tv;


    @BindView(R.id.deleteRequestTaskDetail_iv)
    ImageView deleteRequestTaskDetail_iv;


    @BindView(R.id.minusRequestTaskDetail_iv)
    ImageView minusRequestTaskDetail_iv;

    SharedPreferences sharedPref;
    String userIdPref, userTypePref, type_id;

    RequestSpAdapter requestSpAdapter;
    ArrayList<RequestSpBean.Salesperson_requests> requestList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_request_view, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initialiseUI();
    }

    private void initialiseUI(){
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        userIdPref = sharedPref.getString("user_id", "");
        userTypePref = sharedPref.getString("user_type", "");
        if (userTypePref.equals("Sales Manager")) {
          //  getAttendanceRecyclerView();
        }else if (userTypePref.equals("Sales Executive")){
            getSPRequestViewRecyclerView();
        }

        viewRequestTaskDetails_cv.setVisibility(View.GONE);
    }


    @OnClick(R.id.minusRequestTaskDetail_iv)
    public void hideDetails(){
        viewRequestTaskDetails_cv.setVisibility(View.GONE);
        requestTaskHeader_rl.setVisibility(View.VISIBLE);
        if (userTypePref.equals("Sales Manager")) {
          //  getAttendanceRecyclerView();
        }else if (userTypePref.equals("Sales Executive")){
            getSPRequestViewRecyclerView();
        }
    }

    @OnClick(R.id.deleteRequestTaskDetail_iv)
    public void deleteRequestDetails(){

    }

    public void getRequestData(RequestSpBean.Salesperson_requests bean){
        viewRequestTaskDetails_cv.setVisibility(View.VISIBLE);
        requestTaskHeader_rl.setVisibility(View.GONE);

        statusRequestTask_tv.setText(bean.getRequest_status());
        commentRequestTask_tv.setText(bean.getRequest_comments());
        typeRequestTaskDetail_tv.setText(bean.getRequest_type());
        dateViewRequestTask_tv.setText(bean.getRequest_dt());

        type_id = bean.getRequest_type_id().toString();

        getSPRequestTask();

    }

    private void getSPRequestViewRecyclerView(){
        if (Connectivity.isConnected(getActivity())) {

            String Url = ApiLink.ROOT_URL + ApiLink.REQUEST_VIEW_SP;
            Map<String, String> map = new HashMap<>();
            map.put("select", "");
            map.put("request_uid", userIdPref);

            GSONRequest<RequestSpBean> dashboardGsonRequest = new GSONRequest<RequestSpBean>(
                    Request.Method.POST,
                    Url,
                    RequestSpBean.class, map,
                    new com.android.volley.Response.Listener<RequestSpBean>() {
                        @Override
                        public void onResponse(RequestSpBean response) {
                            try{
                                if (response.getSalesperson_requests().size()>0){
                                    // for (int i = 0; i<=response.getSp_att_und_mgr().size();i++){
                                    requestList.clear();
                                    requestList.addAll(response.getSalesperson_requests());

                                    requestSpAdapter = new RequestSpAdapter(getActivity(),response.getSalesperson_requests(), RequestViewFragment.this);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                    requestTask_rv.setLayoutManager(mLayoutManager);
                                    requestTask_rv.setItemAnimator(new DefaultItemAnimator());
                                    requestTask_rv.setAdapter(requestSpAdapter);
                                }
                            }catch(Exception e){
                                e.printStackTrace();
                              //  Toast.makeText(getActivity(), "Api response Problem", Toast.LENGTH_SHORT).show();
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


    private void getSPRequestTask(){
        if (Connectivity.isConnected(getActivity())) {

            String Url = ApiLink.ROOT_URL + ApiLink.REQUEST_VIEW_SP;
            Map<String, String> map = new HashMap<>();
            map.put("visit_tasks_dropdown", "");
            map.put("request_uid", userIdPref);

            GSONRequest<RequestSpBean> dashboardGsonRequest = new GSONRequest<RequestSpBean>(
                    Request.Method.POST,
                    Url,
                    RequestSpBean.class, map,
                    new com.android.volley.Response.Listener<RequestSpBean>() {
                        @Override
                        public void onResponse(RequestSpBean response) {
                            try{
                                if (response.getVisit_tasks_dropdown().size()>0){
                                     for (int i = 0; i<=response.getVisit_tasks_dropdown().size();i++) {
                                         if (response.getVisit_tasks_dropdown().get(i).getVisit_id().equals(type_id)) {
                                             taskViewRequestTask_tv.setText(response.getVisit_tasks_dropdown().get(i).getLead_company() + " - "
                                             +response.getVisit_tasks_dropdown().get(i).getPurpose_name() +" - "
                                             + response.getVisit_tasks_dropdown().get(i).getVisit_address());
                                         }
                                     }
                                }
                            }catch(Exception e){
                                e.printStackTrace();
                            //    Toast.makeText(getActivity(), "Api response Problem", Toast.LENGTH_SHORT).show();
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

}
