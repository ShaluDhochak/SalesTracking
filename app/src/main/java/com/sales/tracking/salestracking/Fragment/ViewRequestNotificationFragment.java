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

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.sales.tracking.salestracking.Adapter.ViewReassignedRequestAdapter;
import com.sales.tracking.salestracking.Adapter.ViewReassignedRequestNotificationCountAdapter;
import com.sales.tracking.salestracking.Adapter.ViewRequestNotificationDetailsAdapter;
import com.sales.tracking.salestracking.Bean.ExpencesSpBean;
import com.sales.tracking.salestracking.Bean.ReassignRequestNotificationBean;
import com.sales.tracking.salestracking.Bean.RequestCountBean;
import com.sales.tracking.salestracking.Bean.VieqRequestReassignedBean;
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


public class ViewRequestNotificationFragment extends Fragment {

    @BindView(R.id.statusViewRequestNotification_tv)
    TextView statusViewRequestNotification_tv;

    @BindView(R.id.commentViewRequestNotification_tv)
    TextView commentViewRequestNotification_tv;

    @BindView(R.id.taskViewRequestNotification_tv)
    TextView taskViewRequestNotification_tv;

    @BindView(R.id.typeViewRequestNotification_tv)
    TextView typeViewRequestNotification_tv;

    @BindView(R.id.dateViewRequestNotification_tv)
    TextView dateViewRequestNotification_tv;

    @BindView(R.id.requestedByViewRequestNotification_tv)
    TextView requestedByViewRequestNotification_tv;

    @BindView(R.id.minusViewRequestNotification_iv)
    ImageView minusViewRequestNotification_iv;

    @BindView(R.id.viewRequestNotificationDetails_cv)
    CardView viewRequestNotificationDetails_cv;

    @BindView(R.id.viewRequestNotificationRecycler_rv)
    RecyclerView viewRequestNotificationRecycler_rv;


    @BindView(R.id.titleViewRequestNotificationCount_tv)
    TextView titleViewRequestNotificationCount_tv;

    @BindView(R.id.viewRequestNotification_rv)
    RecyclerView viewRequestNotification_rv;

    @BindView(R.id.viewRequestNotificationHeader_rl)
    RelativeLayout viewRequestNotificationHeader_rl;

    @BindView(R.id.viewRequestNotificationRecycler_rl)
    RelativeLayout viewRequestNotificationRecycler_rl;

    @BindView(R.id.viewRequestNotificationViewAll_cv)
            CardView viewRequestNotificationViewAll_cv;

    @BindView(R.id.viewAllRequestNotification_tv)
            TextView viewAllRequestNotification_tv;


   View view;
    SharedPreferences sharedPref;
    String userIdPref, userTypePref, expenses_id, deleteExpenses_id;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    JSONParser jsonParser = new JSONParser();
    ProgressDialog pDialog;

    ViewRequestNotificationDetailsAdapter viewRequestNotificationDetailsAdapter;
    ViewReassignedRequestNotificationCountAdapter viewReassignedRequestNotificationCountAdapter;

    ArrayList<ExpencesSpBean.Salesperson_Expenses> spExpensesList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_view_request_notification, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initialiseUI();
    }

    private void initialiseUI() {

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        userIdPref = sharedPref.getString("user_id", "");
        userTypePref = sharedPref.getString("user_type", "");

        viewRequestNotificationViewAll_cv.setVisibility(View.VISIBLE);
        if (userTypePref.equals("Sales Manager")) {
            getViewcOUNTRequest();
            getViewRequestCountDetails();
            viewRequestNotificationRecycler_rl.setVisibility(View.VISIBLE);// for request visit date request by
        }
        viewRequestNotificationDetails_cv.setVisibility(View.GONE);
       // viewRequestNotificationViewAll_cv.setVisibility(View.GONE);
        viewRequestNotificationHeader_rl.setVisibility(View.GONE);

       if (!titleViewRequestNotificationCount_tv.getText().toString().equals("0")){
           viewRequestNotificationViewAll_cv.setVisibility(View.VISIBLE);
       }
    }




    public void getViewRequestNotificationData(VieqRequestReassignedBean.Mgr_sp_requests bean) {
        viewRequestNotificationDetails_cv.setVisibility(View.VISIBLE);
        viewRequestNotificationHeader_rl.setVisibility(View.GONE);

        statusViewRequestNotification_tv.setText(bean.getRequest_status());
        commentViewRequestNotification_tv.setText(bean.getRequest_comments());
        taskViewRequestNotification_tv.setText(bean.getLead_company()+ " - "+ bean.getPurpose_name() + " - "+ bean.getVisit_address());
        typeViewRequestNotification_tv.setText(bean.getRequest_type());
        dateViewRequestNotification_tv.setText(bean.getRequest_dt());
        requestedByViewRequestNotification_tv.setText(bean.getUser_name());

    }

    private void getViewRequest() {
        if (Connectivity.isConnected(getActivity())) {

            String Url = ApiLink.ROOT_URL + ApiLink.VIEW_REASSIGNED_REQUEST;
            Map<String, String> map = new HashMap<>();
            map.put("reporting_to", userIdPref);
            map.put("select_m", "");

            GSONRequest<VieqRequestReassignedBean> dashboardGsonRequest = new GSONRequest<VieqRequestReassignedBean>(
                    Request.Method.POST,
                    Url,
                    VieqRequestReassignedBean.class, map,
                    new com.android.volley.Response.Listener<VieqRequestReassignedBean>() {
                        @Override
                        public void onResponse(VieqRequestReassignedBean response) {
                            try {
                                if (response.getMgr_sp_requests().size() > 0) {

                                    viewRequestNotificationDetailsAdapter = new ViewRequestNotificationDetailsAdapter(getActivity(), response.getMgr_sp_requests(), ViewRequestNotificationFragment.this);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                    viewRequestNotification_rv.setLayoutManager(mLayoutManager);
                                    viewRequestNotification_rv.setItemAnimator(new DefaultItemAnimator());
                                    viewRequestNotification_rv.setAdapter(viewRequestNotificationDetailsAdapter);

                                }
                            } catch (Exception e) {
                                // Toast.makeText(getActivity(), "Something went wrong..", Toast.LENGTH_SHORT).show();
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

    private void getViewRequestCountDetails() {
        if (Connectivity.isConnected(getActivity())) {

            String Url = ApiLink.ROOT_URL + ApiLink.VISIT_REQUEST_NOTIFICATION;
            Map<String, String> map = new HashMap<>();
            map.put("user_id", userIdPref);
            map.put("sel_requests", "");

            GSONRequest<ReassignRequestNotificationBean> dashboardGsonRequest = new GSONRequest<ReassignRequestNotificationBean>(
                    Request.Method.POST,
                    Url,
                    ReassignRequestNotificationBean.class, map,
                    new com.android.volley.Response.Listener<ReassignRequestNotificationBean>() {
                        @Override
                        public void onResponse(ReassignRequestNotificationBean response) {
                            try {
                                if (response.getSelect_pending_requests().size() > 0) {

                                    viewReassignedRequestNotificationCountAdapter = new ViewReassignedRequestNotificationCountAdapter(getActivity(), response.getSelect_pending_requests());
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                    viewRequestNotificationRecycler_rv.setLayoutManager(mLayoutManager);
                                    viewRequestNotificationRecycler_rv.setItemAnimator(new DefaultItemAnimator());
                                    viewRequestNotificationRecycler_rv.setAdapter(viewReassignedRequestNotificationCountAdapter);

                                }
                            } catch (Exception e) {
                                // Toast.makeText(getActivity(), "Something went wrong..", Toast.LENGTH_SHORT).show();
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

    private void getViewcOUNTRequest() {
        if (Connectivity.isConnected(getActivity())) {

            String Url = ApiLink.ROOT_URL + ApiLink.VISIT_REQUEST_NOTIFICATION;
            Map<String, String> map = new HashMap<>();
            map.put("user_id", userIdPref);
            map.put("count_requests", "");

            GSONRequest<RequestCountBean> dashboardGsonRequest = new GSONRequest<RequestCountBean>(
                    Request.Method.POST,
                    Url,
                    RequestCountBean.class, map,
                    new com.android.volley.Response.Listener<RequestCountBean>() {
                        @Override
                        public void onResponse(RequestCountBean response) {
                            try {
                                if (response.getRequest_count().size() > 0) {
                                        viewRequestNotificationViewAll_cv.setVisibility(View.VISIBLE);
                                    titleViewRequestNotificationCount_tv.setText(response.getRequest_count().get(0).getTot_requests().toString());
                                }
                            } catch (Exception e) {
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


    @OnClick(R.id.minusViewRequestNotification_iv)
    public void hideExpensesDetails() {
        if (userTypePref.equals("Sales Manager")) {
            getViewRequest();
        }
        viewRequestNotificationDetails_cv.setVisibility(View.GONE);

        viewRequestNotificationHeader_rl.setVisibility(View.VISIBLE);
        viewRequestNotificationViewAll_cv.setVisibility(View.GONE);
    }

    @OnClick(R.id.viewAllRequestNotification_tv)
    public void viewRequest(){
        if (userTypePref.equals("Sales Manager")) {
            getViewRequest();

            viewRequestNotificationViewAll_cv.setVisibility(View.GONE);
            viewRequestNotificationHeader_rl.setVisibility(View.VISIBLE);
            viewRequestNotificationDetails_cv.setVisibility(View.GONE);
        }
    }

}
