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
import com.sales.tracking.salestracking.Bean.SalesCallTaskSpBean;
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
import butterknife.OnClick;

public class ViewSalesCallTaskFragment extends Fragment {

    View view;

    SharedPreferences sharedPref;
    String userIdPref, userTypePref;

    @BindView(R.id.viewSaleCallTask_rv)
    RecyclerView viewSaleCallTask_rv;

    @BindView(R.id.salesCallTaskHeader_rl)
    RelativeLayout salesCallTaskHeader_rl;

    @BindView(R.id.viewSaleCallTaskDetails_cv)
    CardView viewSaleCallTaskDetails_cv;



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

    ViewSaleCallTaskAdater viewSaleCallTaskAdater;
    ArrayList<SalesCallTaskSpBean.Sp_All_Service_Calls> spAttendanceList = new ArrayList<>();


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
                                    // for (int i = 0; i<=response.getSp_att_und_mgr().size();i++){
                                    spAttendanceList.clear();
                                    spAttendanceList.addAll(response.getSp_all_service_calls());

                                    viewSaleCallTaskAdater = new ViewSaleCallTaskAdater(getActivity(),response.getSp_all_service_calls(), ViewSalesCallTaskFragment.this);
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

    public void getViewSaleCallTask(SalesCallTaskSpBean.Sp_All_Service_Calls bean){
        viewSaleCallTaskDetails_cv.setVisibility(View.VISIBLE);
        salesCallTaskHeader_rl.setVisibility(View.GONE);


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

    @OnClick(R.id.minusVisitSaleCallTaskDetail_iv)
    public void minusVisitSaleCallTaskDetail(){
        viewSaleCallTaskDetails_cv.setVisibility(View.GONE);
        salesCallTaskHeader_rl.setVisibility(View.VISIBLE);
        getSaleCallVisitSpRecyclerView();
    }
}
