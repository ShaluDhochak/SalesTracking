package com.sales.tracking.salestracking.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.sales.tracking.salestracking.Adapter.CallPendingAdapter;
import com.sales.tracking.salestracking.Bean.SalesCallTaskSpBean;
import com.sales.tracking.salestracking.Bean.TargetNotificationBean;
import com.sales.tracking.salestracking.R;
import com.sales.tracking.salestracking.Utility.ApiLink;
import com.sales.tracking.salestracking.Utility.Connectivity;
import com.sales.tracking.salestracking.Utility.GSONRequest;
import com.sales.tracking.salestracking.Utility.JSONParser;
import com.sales.tracking.salestracking.Utility.Utilities;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TargetFragment extends Fragment {

    View view;
    SharedPreferences sharedPref;
    String userIdPref, userTypePref, user_comidPref;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    JSONParser jsonParser = new JSONParser();
    ProgressDialog pDialog;


    @BindView(R.id.visitPendingCountTargetHeading_tv)
    TextView visitPendingCountTargetHeading_tv;

    @BindView(R.id.visitPendingCountTarget_tv)
    TextView visitPendingCountTarget_tv;

    @BindView(R.id.saleCallsPendingCountTargetHeading_tv)
    TextView saleCallsPendingCountTargetHeading_tv;

    @BindView(R.id.saleCallsPendingCountTarget_tv)
    TextView saleCallsPendingCountTarget_tv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_target, container, false);
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
        user_comidPref = sharedPref.getString("user_com_id", "");

        getSaleCallPendingCount();
        getviewVisitTaskSpCount();
    }

    private void getviewVisitTaskSpCount(){
        if (Connectivity.isConnected(getActivity())) {

            String Url = ApiLink.ROOT_URL + ApiLink.Dashboard_SalesPerson;
            Map<String, String> map = new HashMap<>();
            map.put("visit_target", "");
            map.put("visit_uid", userIdPref);

            GSONRequest<TargetNotificationBean> dashboardGsonRequest = new GSONRequest<TargetNotificationBean>(
                    Request.Method.POST,
                    Url,
                    TargetNotificationBean.class, map,
                    new com.android.volley.Response.Listener<TargetNotificationBean>() {
                        @Override
                        public void onResponse(TargetNotificationBean response) {
                            try{
                                if (response.getVisit_target().size()>0){
                                    visitPendingCountTargetHeading_tv.setText(response.getVisit_target().get(0).getTarget_no() + " Visits:");
                                    visitPendingCountTarget_tv.setText("From " + response.getVisit_target().get(0).getTarget_startdate() + " To " + response.getVisit_target().get(0).getTarget_enddate());
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
            map.put("call_target", "");
            map.put("visit_uid", userIdPref);

            GSONRequest<TargetNotificationBean> dashboardGsonRequest = new GSONRequest<TargetNotificationBean>(
                    Request.Method.POST,
                    Url,
                    TargetNotificationBean.class, map,
                    new com.android.volley.Response.Listener<TargetNotificationBean>() {
                        @Override
                        public void onResponse(TargetNotificationBean response) {
                            try{
                                if (response.getCall_target().size()>0){
                                    saleCallsPendingCountTargetHeading_tv.setText(response.getCall_target().get(0).getTarget_no() + " Sales");
                                    saleCallsPendingCountTarget_tv.setText("From " + response.getCall_target().get(0).getTarget_startdate() + " To " + response.getCall_target().get(0).getTarget_enddate());
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



}
