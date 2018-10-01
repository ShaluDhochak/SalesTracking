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
import com.sales.tracking.salestracking.Adapter.ManagerClientAdapter;
import com.sales.tracking.salestracking.Adapter.ManagerClientManagerHeadAdapter;
import com.sales.tracking.salestracking.Adapter.ManagerUsersManagerHeadAdapter;
import com.sales.tracking.salestracking.Bean.ManagerBean;
import com.sales.tracking.salestracking.Bean.ViewUserManagerHeadBean;
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


public class ViewUserManagerHeadFragment extends Fragment {

    @BindView(R.id.updatedonUserMgrHead_tv)
    TextView updatedonUserMgrHead_tv;

    @BindView(R.id.createdonUserMgrHead_tv)
    TextView createdonUserMgrHead_tv;

    @BindView(R.id.dojUserMgrHead_tv)
    TextView dojUserMgrHead_tv;

    @BindView(R.id.statusUserMgrHead_tv)
    TextView statusUserMgrHead_tv;

    @BindView(R.id.reportingToUserMgrHead_tv)
    TextView reportingToUserMgrHead_tv;

    @BindView(R.id.userTypeUserMgrHead_tv)
    TextView userTypeUserMgrHead_tv;

    @BindView(R.id.designationUserMgrHead_tv)
    TextView designationUserMgrHead_tv;

    @BindView(R.id.mobileUserMgrHeadView_tv)
    TextView mobileUserMgrHeadView_tv;

    @BindView(R.id.emailUserMgrHead_tv)
    TextView emailUserMgrHead_tv;

    @BindView(R.id.nameUserMgrHead_tv)
    TextView nameUserMgrHead_tv;

    @BindView(R.id.viewUserMgrHeadDetails_cv)
    CardView viewUserMgrHeadDetails_cv;

    @BindView(R.id.userMgrHead_rv)
    RecyclerView userMgrHead_rv;

    @BindView(R.id.userMgrHeader_rl)
    RelativeLayout userMgrHeader_rl;

    @BindView(R.id.minusUserMgrHeadDetail_iv)
    ImageView minusUserMgrHeadDetail_iv;


    View view;

    SharedPreferences sharedPref;
    String userIdPref, userTypePref, user_comidPref;

    ManagerUsersManagerHeadAdapter managerUsersManagerHeadAdapter;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    JSONParser jsonParser = new JSONParser();
    ProgressDialog pDialog;

    int selectedLead;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_view_user_manager_head, container, false);
        ButterKnife.bind(this, view);
        initialiseUI();
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

        viewUserMgrHeadDetails_cv.setVisibility(View.GONE);
        userMgrHeader_rl.setVisibility(View.VISIBLE);

        if (userTypePref.equals("Manager Head")){
            getManagerHeadUserViewRecyclerView();
        }

    }

    private void getManagerHeadUserViewRecyclerView(){
        try {
            if (Connectivity.isConnected(getActivity())) {

                String Url = ApiLink.ROOT_URL + ApiLink.MANAGER_SALES_PERSON;
                Map<String, String> map = new HashMap<>();
                map.put("select_users", "");
                map.put("managerhead_id", userIdPref);

                GSONRequest<ViewUserManagerHeadBean> dashboardGsonRequest = new GSONRequest<ViewUserManagerHeadBean>(
                        Request.Method.POST,
                        Url,
                        ViewUserManagerHeadBean.class, map,
                        new com.android.volley.Response.Listener<ViewUserManagerHeadBean>() {
                            @Override
                            public void onResponse(ViewUserManagerHeadBean response) {
                                try {
                                    if (response.getUsers().size() > 0) {

                                        managerUsersManagerHeadAdapter = new ManagerUsersManagerHeadAdapter(getActivity(), response.getUsers(), ViewUserManagerHeadFragment.this);
                                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                        userMgrHead_rv.setLayoutManager(mLayoutManager);
                                        userMgrHead_rv.setItemAnimator(new DefaultItemAnimator());
                                        userMgrHead_rv.setAdapter(managerUsersManagerHeadAdapter);
                                        userMgrHead_rv.setNestedScrollingEnabled(false);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
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
        }catch(Exception e){

        }
    }

    public void getUserData(ViewUserManagerHeadBean.Users bean){
        viewUserMgrHeadDetails_cv.setVisibility(View.VISIBLE);
        userMgrHeader_rl.setVisibility(View.GONE);

        updatedonUserMgrHead_tv.setText(bean.getUpdate_dt());
        createdonUserMgrHead_tv.setText(bean.getCreated_dt());
        dojUserMgrHead_tv.setText(bean.getUser_doj());
        statusUserMgrHead_tv.setText(bean.getUser_status());
        reportingToUserMgrHead_tv.setText(bean.getUser_reporting_to());
        userTypeUserMgrHead_tv.setText(bean.getUser_type());
        designationUserMgrHead_tv.setText(bean.getDesi_name());
        mobileUserMgrHeadView_tv.setText(bean.getUser_mobile());
        emailUserMgrHead_tv.setText(bean.getUser_email());
        nameUserMgrHead_tv.setText(bean.getUser_name());

    }

    @OnClick(R.id.minusUserMgrHeadDetail_iv)
    public void minusUserMgrHeadDetail(){
        viewUserMgrHeadDetails_cv.setVisibility(View.GONE);
        userMgrHeader_rl.setVisibility(View.VISIBLE);

        if (userTypePref.equals("Manager Head")){
            getManagerHeadUserViewRecyclerView();
        }
    }

}
