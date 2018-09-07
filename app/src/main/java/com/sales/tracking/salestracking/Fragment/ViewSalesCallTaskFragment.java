package com.sales.tracking.salestracking.Fragment;

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
import com.sales.tracking.salestracking.Adapter.ViewSaleCallTaskAdater;
import com.sales.tracking.salestracking.Adapter.VisitTaskMeetingAdapter;
import com.sales.tracking.salestracking.Bean.TaskMeetingBean;
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

public class ViewSalesCallTaskFragment extends Fragment {

    View view;

    SharedPreferences sharedPref;
    String userIdPref, userTypePref;

    @BindView(R.id.viewSaleCallTask_rv)
    RecyclerView viewSaleCallTask_rv;

    @BindView(R.id.viewSaleCallTaskDetails_cv)
    CardView viewSaleCallTaskDetails_cv;

    @BindView(R.id.dateViewSaleCallTask_tv)
    TextView dateViewSaleCallTask_tv;

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

    ViewSaleCallTaskAdater viewSaleCallTaskAdater;
    ArrayList<TaskMeetingBean.All_Meetings_Mgr> spAttendanceList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.fragment_view_sales_call_task, container, false);
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
          //  getVisitTaskMeetingRecyclerView();
            assignToByViewSaleCallTaskHeading_tv.setText("Assigned To");
        }else if (userTypePref.equals("Sales Executive")){
            assignToByViewSaleCallTaskHeading_tv.setText("Assigned By");

            getSaleCallVisitSpRecyclerView();
        }

        viewSaleCallTaskDetails_cv.setVisibility(View.GONE);
    }


    private void getSaleCallVisitSpRecyclerView(){
        if (Connectivity.isConnected(getActivity())) {

            String Url = ApiLink.ROOT_URL + ApiLink.Dashboard_SalesPerson;
            Map<String, String> map = new HashMap<>();
            map.put("select", "");
            map.put("m_all", "");
            map.put("visit_assignedby", userIdPref);

            GSONRequest<TaskMeetingBean> dashboardGsonRequest = new GSONRequest<TaskMeetingBean>(
                    Request.Method.POST,
                    Url,
                    TaskMeetingBean.class, map,
                    new com.android.volley.Response.Listener<TaskMeetingBean>() {
                        @Override
                        public void onResponse(TaskMeetingBean response) {
                            try{
                                if (response.getAll_meetings_mgr().size()>0){
                                    // for (int i = 0; i<=response.getSp_att_und_mgr().size();i++){
                                    spAttendanceList.clear();
                                    spAttendanceList.addAll(response.getAll_meetings_mgr());

                                    viewSaleCallTaskAdater = new ViewSaleCallTaskAdater(getActivity(),response.getAll_meetings_mgr(), ViewSalesCallTaskFragment.this);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                    viewSaleCallTask_rv.setLayoutManager(mLayoutManager);
                                    viewSaleCallTask_rv.setItemAnimator(new DefaultItemAnimator());
                                    viewSaleCallTask_rv.setAdapter(viewSaleCallTaskAdater);

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
