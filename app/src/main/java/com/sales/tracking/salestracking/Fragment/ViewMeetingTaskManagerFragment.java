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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.sales.tracking.salestracking.Adapter.AttendanceAddapter;
import com.sales.tracking.salestracking.Adapter.VisitTaskMeetingAdapter;
import com.sales.tracking.salestracking.Bean.AttendanceManagerBean;
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


public class ViewMeetingTaskManagerFragment extends Fragment {

    View view;

    @BindView(R.id.viewMeetingTaskManager_rv)
    RecyclerView viewMeetingTaskManager_rv;

    @BindView(R.id.salesViewMeetingTaskHeader_rl)
    RelativeLayout salesViewMeetingTaskHeader_rl;

    @BindView(R.id.viewMeetingTaskeDetails_cv)
    CardView viewMeetingTaskeDetails_cv;

    @BindView(R.id.timeValueMeetingTaskDetail_tv)
    TextView timeValueMeetingTaskDetail_tv;
    @BindView(R.id.dateViewMeetingTask_tv)
    TextView dateViewMeetingTask_tv;

    @BindView(R.id.assignToViewMeetingTask_tv)
    TextView assignToViewMeetingTask_tv;
    @BindView(R.id.clientNameMeetingTask_tv)
    TextView clientNameMeetingTask_tv;
    @BindView(R.id.purposeViewMeetingTask_tv)
    TextView purposeViewMeetingTask_tv;

    @BindView(R.id.descriptionViewMeetingTask_tv)
    TextView descriptionViewMeetingTask_tv;
    @BindView(R.id.statusViewMeetingTask_tv)
    TextView statusViewMeetingTask_tv;

    @BindView(R.id.minusVisitTaskMeetingDetail_iv)
    ImageView minusVisitTaskMeetingDetail_iv;

    //Edit Detail
    @BindView(R.id.clientNameEditMeetingTask_sp)
    Spinner clientNameEditMeetingTask_sp;
    @BindView(R.id.assignToEditViewMeetingTask_sp)
    Spinner assignToEditViewMeetingTask_sp;
    @BindView(R.id.purposeEditViewMeetingTask_sp)
    Spinner purposeEditViewMeetingTask_sp;
    @BindView(R.id.addressEditViewMeetingTask_et)
    EditText addressEditViewMeetingTask_et;
    @BindView(R.id.descriptionEditViewMeetingTask_et)
    EditText descriptionEditViewMeetingTask_et;
    @BindView(R.id.statusEditViewMeetingTask_et)
    EditText statusEditViewMeetingTask_et;
    @BindView(R.id.dateEditViewMeetingTask_tv)
    TextView dateEditViewMeetingTask_tv;
    @BindView(R.id.timeEditViewValueMeetingTaskDetail_et)
    TextView timeEditViewValueMeetingTaskDetail_et;

    SharedPreferences sharedPref;
    String userIdPref, userTypePref;

    VisitTaskMeetingAdapter visitTaskMeetingAdapter;
    ArrayList<TaskMeetingBean.All_Meetings_Mgr> spAttendanceList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_meeting_task_manager, container, false);
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
            getVisitTaskMeetingRecyclerView();
        }

        viewMeetingTaskeDetails_cv.setVisibility(View.GONE);
    }

    private void getVisitTaskMeetingRecyclerView(){
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

                                    visitTaskMeetingAdapter = new VisitTaskMeetingAdapter(getActivity(),response.getAll_meetings_mgr(), ViewMeetingTaskManagerFragment.this);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                    viewMeetingTaskManager_rv.setLayoutManager(mLayoutManager);
                                    viewMeetingTaskManager_rv.setItemAnimator(new DefaultItemAnimator());
                                    viewMeetingTaskManager_rv.setAdapter(visitTaskMeetingAdapter);

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

    @OnClick(R.id.minusVisitTaskMeetingDetail_iv)
    public void hideDetails(){
        viewMeetingTaskeDetails_cv.setVisibility(View.GONE);
        salesViewMeetingTaskHeader_rl.setVisibility(View.VISIBLE);
        if (userTypePref.equals("Sales Manager")) {
            getVisitTaskMeetingRecyclerView();
        }
    }

    public void getViewMeetingTask(TaskMeetingBean.All_Meetings_Mgr bean){
        viewMeetingTaskeDetails_cv.setVisibility(View.VISIBLE);
        salesViewMeetingTaskHeader_rl.setVisibility(View.GONE);

        String indate = bean.getVisit_datetime();
        String[] indate1 = indate.split( " ");


        dateViewMeetingTask_tv.setText(indate1[0]);
        timeValueMeetingTaskDetail_tv.setText(indate1[1]);

        assignToViewMeetingTask_tv.setText(bean.getUser_name());
        clientNameMeetingTask_tv.setText(bean.getLead_company());
        purposeViewMeetingTask_tv.setText(bean.getPurpose_name());
        descriptionViewMeetingTask_tv.setText(bean.getVisit_comments());
        statusViewMeetingTask_tv.setText(bean.getVisit_status());
        //    outLocationAttendance_tv.setText(bean.getAtten_out_longitude());
    }

}
