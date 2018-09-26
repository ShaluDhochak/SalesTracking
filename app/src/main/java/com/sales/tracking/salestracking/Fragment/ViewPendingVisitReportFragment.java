package com.sales.tracking.salestracking.Fragment;

import android.app.DatePickerDialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.sales.tracking.salestracking.Adapter.VisitManagerReportAdapter;
import com.sales.tracking.salestracking.Adapter.VisitPendingReportAdapter;
import com.sales.tracking.salestracking.Bean.TaskMeetingBean;
import com.sales.tracking.salestracking.Bean.VisitPendingReportBean;
import com.sales.tracking.salestracking.Bean.VisitReportManagerBean;
import com.sales.tracking.salestracking.R;
import com.sales.tracking.salestracking.Utility.ApiLink;
import com.sales.tracking.salestracking.Utility.Connectivity;
import com.sales.tracking.salestracking.Utility.GSONRequest;
import com.sales.tracking.salestracking.Utility.JSONParser;
import com.sales.tracking.salestracking.Utility.Utilities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;


public class ViewPendingVisitReportFragment extends Fragment {

    @BindView(R.id.meetingtimeVisitManagerReport_tv)
    TextView meetingtimeVisitManagerReport_tv;

    @BindView(R.id.commentsVisitManagerReportDetail_tv)
    TextView commentsVisitManagerReportDetail_tv;

    @BindView(R.id.purposeVisitManagerReportDetail_tv)
    TextView purposeVisitManagerReportDetail_tv;

    @BindView(R.id.locationVisitManagerReportDetail_tv)
    TextView locationVisitManagerReportDetail_tv;

    @BindView(R.id.empnameVisitManagerReportDetail_tv)
    TextView empnameVisitManagerReportDetail_tv;

    @BindView(R.id.dateVisitManagerReport_tv)
    TextView dateVisitManagerReport_tv;

    @BindView(R.id.clientNameVisitManagerReportDetail_tv)
    TextView clientNameVisitManagerReportDetail_tv;

    @BindView(R.id.minusVisitManagerReportDetail_iv)
    ImageView minusVisitManagerReportDetail_iv;

    //Layout
    @BindView(R.id.viewVisitManagerReportHeader_rl)
    RelativeLayout viewVisitManagerReportHeader_rl;

    @BindView(R.id.viewVisitManagerReport_rv)
    RecyclerView viewVisitManagerReport_rv;

    @BindView(R.id.viewVisitManagerReport_cv)
    CardView viewVisitManagerReport_cv;

    //Search Opertaion
    @BindView(R.id.employeeAdvanceSearchReport_sp)
    Spinner employeeAdvanceSearchReport_sp;

    @BindView(R.id.fromdateAdvanceSearchReportDetail_tv)
    TextView fromdateAdvanceSearchReportDetail_tv;

    @BindView(R.id.todateAdvanceSearchReport_tv)
    TextView todateAdvanceSearchReport_tv;

    @BindView(R.id.submitAdvanceSearchReport_btn)
    Button submitAdvanceSearchReport_btn;

    View view;

    DatePickerDialog datePickerDialog;
    ProgressDialog pDialog;

    SharedPreferences sharedPref;
    String userIdPref, userTypePref, user_comidPref;
    String selectAssignTo, selectAssignToId;

    ArrayList<String> assignToUser;

    Map<String, String> assignToUserMap = new HashMap<>();

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    JSONParser jsonParser = new JSONParser();

    VisitPendingReportAdapter visitPendingReportAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_all_visit_report, container, false);
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

        viewVisitManagerReportHeader_rl.setVisibility(View.VISIBLE);
        viewVisitManagerReport_cv.setVisibility(View.GONE);
        if (userTypePref.equals("Sales Manager")) {
            getDefaultVisitReportManagerRecyclerView();
            selectAssignTo();
        }

    }

    @OnClick(R.id.minusVisitManagerReportDetail_iv)
    public void hideDetail(){
        viewVisitManagerReportHeader_rl.setVisibility(View.VISIBLE);
        viewVisitManagerReport_cv.setVisibility(View.GONE);
    }

    private void selectAssignTo(){
        try{
            if (Connectivity.isConnected(getActivity())) {
                String Url = ApiLink.ROOT_URL + ApiLink.Dashboard_SalesPerson;
                Map<String, String> map = new HashMap<>();
                map.put("users_undermanager","" );
                map.put("user_comid", user_comidPref);
                map.put("user_reporting_to", userIdPref);

                final GSONRequest<TaskMeetingBean> targetAssignToGsonRequest = new GSONRequest<TaskMeetingBean>(
                        Request.Method.POST,
                        Url,
                        TaskMeetingBean.class,map,
                        new com.android.volley.Response.Listener<TaskMeetingBean>() {
                            @Override
                            public void onResponse(TaskMeetingBean response) {
                                assignToUser.clear();
                                assignToUser.add("Assign To");
                                for(int i=0;i<response.getUsers_dd1().size();i++)
                                {
                                    assignToUser.add(response.getUsers_dd1().get(i).getUser_name());
                                    assignToUserMap.put(response.getUsers_dd1().get(i).getUser_id(), response.getUsers_dd1().get(i).getUser_name());
                                }
                            }
                        },
                        new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Utilities.serverError(getActivity());
                            }
                        });
                targetAssignToGsonRequest.setShouldCache(false);
                Utilities.getRequestQueue(getActivity()).add(targetAssignToGsonRequest);
            }
            assignToUser = new ArrayList<String>();
            assignToUser.clear();
            assignToUser.add("Assign To");
            ArrayAdapter<String> quotationLocationDataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_textview, assignToUser);
            quotationLocationDataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
            employeeAdvanceSearchReport_sp.setAdapter(quotationLocationDataAdapter);

        }catch (Exception e){
        }
    }

    @OnItemSelected(R.id.employeeAdvanceSearchReport_sp)
    public void assignToAddSelected(Spinner spinner, int position) {
        selectAssignTo = spinner.getSelectedItem().toString();
        for (Map.Entry<String, String> e : assignToUserMap.entrySet()) {
            Object key = e.getKey();
            Object value = e.getValue();
            if (value.equals(selectAssignTo)) {
                selectAssignToId = (String) key;
            }
        }
    }

    @OnClick(R.id.fromdateAdvanceSearchReportDetail_tv)
    public void startDate(){
        final Calendar calenderObj = Calendar.getInstance();
        int mYear = calenderObj.get(Calendar.YEAR);
        int mMonth = calenderObj.get(Calendar.MONTH);
        int mDay = calenderObj.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        fromdateAdvanceSearchReportDetail_tv.setText(year + "-" + (monthOfYear + 1)+ "-" + dayOfMonth);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    @OnClick(R.id.todateAdvanceSearchReport_tv)
    public void endDate(){
        final Calendar calenderObj = Calendar.getInstance();
        int mYear = calenderObj.get(Calendar.YEAR);
        int mMonth = calenderObj.get(Calendar.MONTH);
        int mDay = calenderObj.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        todateAdvanceSearchReport_tv.setText(year + "-" + (monthOfYear + 1)+ "-" + dayOfMonth);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    @OnClick(R.id.submitAdvanceSearchReport_btn)
    public void submitAdvanceSearch(){
        if (!selectAssignTo.equals("Assign To")) {
            if (!(fromdateAdvanceSearchReportDetail_tv.getText().toString().equals(""))){
                if (!(todateAdvanceSearchReport_tv.getText().toString().equals(""))) {
                    getAllVisitReportManagerRecyclerView();
                }else{
                    Toast.makeText(getActivity(), "Please Select End Date", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getActivity(), "Please Select Start Date", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getActivity(), "Please Select Assigned To", Toast.LENGTH_SHORT).show();
        }

    }

    private void getAllVisitReportManagerRecyclerView(){
        if (Connectivity.isConnected(getActivity())) {

            String fromd = fromdateAdvanceSearchReportDetail_tv.getText().toString();
            String tod = todateAdvanceSearchReport_tv.getText().toString();

            String Url = ApiLink.ROOT_URL + ApiLink.VISIT_PENDING_REPORT;
            Map<String, String> map = new HashMap<>();
            map.put("logged_manager_id", userIdPref);
            map.put("add_pending", "");
            map.put("emp_id", selectAssignToId);
            map.put("startdate", fromd);
            map.put("enddate", tod);

            GSONRequest<VisitPendingReportBean> dashboardGsonRequest = new GSONRequest<VisitPendingReportBean>(
                    Request.Method.POST,
                    Url,
                    VisitPendingReportBean.class, map,
                    new com.android.volley.Response.Listener<VisitPendingReportBean>() {
                        @Override
                        public void onResponse(VisitPendingReportBean response) {
                            try{
                                if (response.getSp_pending_visits().size()>0){
                                    viewVisitManagerReportHeader_rl.setVisibility(View.VISIBLE);

                                    visitPendingReportAdapter = new VisitPendingReportAdapter(getActivity(),response.getSp_pending_visits(), ViewPendingVisitReportFragment.this);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                    viewVisitManagerReport_rv.setLayoutManager(mLayoutManager);
                                    viewVisitManagerReport_rv.setItemAnimator(new DefaultItemAnimator());
                                    viewVisitManagerReport_rv.setAdapter(visitPendingReportAdapter);
                                    viewVisitManagerReport_rv.setNestedScrollingEnabled(false);
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

    private void getDefaultVisitReportManagerRecyclerView(){
        try {
            if (Connectivity.isConnected(getActivity())) {

                String Url = ApiLink.ROOT_URL + ApiLink.VISIT_PENDING_REPORT;
                Map<String, String> map = new HashMap<>();
                map.put("select_pending", "");
                map.put("all", "");
                map.put("reporting_to", userIdPref);

                GSONRequest<VisitPendingReportBean> dashboardGsonRequest = new GSONRequest<VisitPendingReportBean>(
                        Request.Method.POST,
                        Url,
                        VisitPendingReportBean.class, map,
                        new com.android.volley.Response.Listener<VisitPendingReportBean>() {
                            @Override
                            public void onResponse(VisitPendingReportBean response) {
                                try {
                                    if (response.getSp_pending_visits().size() > 0) {
                                        viewVisitManagerReportHeader_rl.setVisibility(View.VISIBLE);

                                        visitPendingReportAdapter = new VisitPendingReportAdapter(getActivity(), response.getSp_pending_visits(), ViewPendingVisitReportFragment.this);
                                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                        viewVisitManagerReport_rv.setLayoutManager(mLayoutManager);
                                        viewVisitManagerReport_rv.setItemAnimator(new DefaultItemAnimator());
                                        viewVisitManagerReport_rv.setAdapter(visitPendingReportAdapter);
                                        viewVisitManagerReport_rv.setNestedScrollingEnabled(false);

                                    }
                                } catch (Exception e) {
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
        }catch(Exception e){
            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    public void getAllVisitData(VisitPendingReportBean.Sp_Pending_Visits bean){
        viewVisitManagerReportHeader_rl.setVisibility(View.GONE);
        viewVisitManagerReport_cv.setVisibility(View.VISIBLE);

        meetingtimeVisitManagerReport_tv.setText(convertIn12Hours(bean.getMeeting_time()));
        commentsVisitManagerReportDetail_tv.setText(bean.getVisit_comments());
        purposeVisitManagerReportDetail_tv.setText(bean.getPurpose_name());
        locationVisitManagerReportDetail_tv.setText(bean.getVisit_address());
        empnameVisitManagerReportDetail_tv.setText(bean.getUser_name());
        dateVisitManagerReport_tv.setText(bean.getMeeting_dt());
        clientNameVisitManagerReportDetail_tv.setText(bean.getLead_company());

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
