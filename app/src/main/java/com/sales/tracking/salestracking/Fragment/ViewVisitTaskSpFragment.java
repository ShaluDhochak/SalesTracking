package com.sales.tracking.salestracking.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
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
import com.sales.tracking.salestracking.Adapter.VisitTaskMeetingAdapter;
import com.sales.tracking.salestracking.Bean.TaskMeetingBean;
import com.sales.tracking.salestracking.Bean.VisitTaskSpBean;
import com.sales.tracking.salestracking.R;
import com.sales.tracking.salestracking.Utility.ApiLink;
import com.sales.tracking.salestracking.Utility.Connectivity;
import com.sales.tracking.salestracking.Utility.GSONRequest;
import com.sales.tracking.salestracking.Utility.Utilities;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViewVisitTaskSpFragment extends Fragment {

    View view;

    @BindView(R.id.viewVisitSpTaskManager_rv)
    RecyclerView viewVisitSpTaskManager_rv;

    @BindView(R.id.viewVisitSpTaskHeader_rl)
    RelativeLayout viewVisitSpTaskHeader_rl;

    @BindView(R.id.minusViewVisitSpDetail_iv)
    ImageView minusViewVisitSpDetail_iv;

    //View Detail Layout field and recyclerview

    @BindView(R.id.viewVisitSpTaskDetails_cv)
    CardView viewVisitSpTaskDetails_cv;

    @BindView(R.id.dateViewVisitSpTask_tv)
    TextView dateViewVisitSpTask_tv;

    @BindView(R.id.clientNameViewVisitSpTaskDetail_tv)
    TextView clientNameViewVisitSpTaskDetail_tv;

    @BindView(R.id.timeViewVisitSpTask_tv)
    TextView timeViewVisitSpTask_tv;

    @BindView(R.id.assignByViewVisitSpTask_tv)
    TextView assignByViewVisitSpTask_tv;

    @BindView(R.id.purposeViewVisitSpTask_tv)
    TextView purposeViewVisitSpTask_tv;

    @BindView(R.id.addressViewVisitSpTask_tv)
    TextView addressViewVisitSpTask_tv;

    @BindView(R.id.descriptionViewVisitSpTask_tv)
    TextView descriptionViewVisitSpTask_tv;

    @BindView(R.id.photoViewVisitSpTask_iv)
    ImageView photoViewVisitSpTask_iv;

    @BindView(R.id.statusViewVisitSpTask_tv)
    TextView statusViewVisitSpTask_tv;

    @BindView(R.id.followUpDateTimeViewVisitSpTask_tv)
    TextView followUpDateTimeViewVisitSpTask_tv;

    @BindView(R.id.followUpDateTimeViewVisitSpTask_rl)
    RelativeLayout followUpDateTimeViewVisitSpTask_rl;

    @BindView(R.id.separatorBelowStatusViewVisitSpTask)
    View separatorBelowStatusViewVisitSpTask;

    ViewVisitTaskSpAdapter viewVisitTaskSpAdapter;
    ArrayList<VisitTaskSpBean.Single_sp_all_Meetings> spAttendanceList = new ArrayList<>();

    SharedPreferences sharedPref;
    String userIdPref, userTypePref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_visit_task_sp, container, false);
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
        if (userTypePref.equals("Sales Executive")) {
            getviewVisitTaskSpRecyclerView();
        }
        viewVisitSpTaskDetails_cv.setVisibility(View.GONE);
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
                                    spAttendanceList.clear();
                                    spAttendanceList.addAll(response.getSingle_sp_all_meetings());

                                    viewVisitTaskSpAdapter = new ViewVisitTaskSpAdapter(getActivity(),response.getSingle_sp_all_meetings(), ViewVisitTaskSpFragment.this);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                    viewVisitSpTaskManager_rv.setLayoutManager(mLayoutManager);
                                    viewVisitSpTaskManager_rv.setItemAnimator(new DefaultItemAnimator());
                                    viewVisitSpTaskManager_rv.setAdapter(viewVisitTaskSpAdapter);
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

    @OnClick(R.id.minusViewVisitSpDetail_iv)
    public void hideDetails(){
        viewVisitSpTaskDetails_cv.setVisibility(View.GONE);
        viewVisitSpTaskHeader_rl.setVisibility(View.VISIBLE);

        if (userTypePref.equals("Sales Executive")) {
            getviewVisitTaskSpRecyclerView();
        }
    }

    public void getViewVisitSpTask(VisitTaskSpBean.Single_sp_all_Meetings bean){

        viewVisitSpTaskDetails_cv.setVisibility(View.VISIBLE);
        viewVisitSpTaskHeader_rl.setVisibility(View.GONE);
        followUpDateTimeViewVisitSpTask_rl.setVisibility(View.GONE);
        separatorBelowStatusViewVisitSpTask.setVisibility(View.GONE);

        String indate = bean.getVisit_datetime();
        String[] indate1 = indate.split( " ");

        String followdate = bean.getVisit_followup_date();
        String[] followdate1 = followdate.split( " ");

        clientNameViewVisitSpTaskDetail_tv.setText(bean.getLead_company());
        dateViewVisitSpTask_tv.setText(indate1[0]);
        timeViewVisitSpTask_tv.setText(convertIn12Hours(indate1[1]));

        assignByViewVisitSpTask_tv.setText(bean.getUser_name());
        descriptionViewVisitSpTask_tv.setText(bean.getVisit_comments());
        purposeViewVisitSpTask_tv.setText(bean.getPurpose_name());
        statusViewVisitSpTask_tv.setText(bean.getVisit_status());
        addressViewVisitSpTask_tv.setText(bean.getVisit_address());
        if (bean.getVisit_status().toString().equals("Followup")){
            followUpDateTimeViewVisitSpTask_rl.setVisibility(View.VISIBLE);
            separatorBelowStatusViewVisitSpTask.setVisibility(View.VISIBLE);
        }else{
            followUpDateTimeViewVisitSpTask_rl.setVisibility(View.GONE);
            separatorBelowStatusViewVisitSpTask.setVisibility(View.GONE);
        }

        if (!bean.getVisit_photo().equals("")){
            Picasso.get().load(ApiLink.IMAGE_BASE_URL + bean.getVisit_photo()).into(photoViewVisitSpTask_iv);
        }else{
            photoViewVisitSpTask_iv.setImageDrawable(getResources().getDrawable(R.drawable.default_img));
        }

        followUpDateTimeViewVisitSpTask_tv.setText(followdate1[0] + " / " + convertIn12Hours(followdate1[1]));

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
