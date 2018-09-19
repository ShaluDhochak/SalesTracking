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
import com.sales.tracking.salestracking.Adapter.CallDoneReportAdapter;
import com.sales.tracking.salestracking.Adapter.CallPendingReportAdapter;
import com.sales.tracking.salestracking.Bean.CallDoneReportBean;
import com.sales.tracking.salestracking.Bean.CallPendingReportBean;
import com.sales.tracking.salestracking.Bean.TaskMeetingBean;
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


public class CallPendingReportFragment extends Fragment {

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

    CallPendingReportAdapter callPendingReportAdapter;

    @BindView(R.id.statusViewSaleCallReport_tv)
    TextView statusViewSaleCallReport_tv;

    @BindView(R.id.commentsViewSaleCallReport_tv)
    TextView commentsViewSaleCallReport_tv;

    @BindView(R.id.assignToByViewSaleCallReport_tv)
    TextView assignToByViewSaleCallReport_tv;

    @BindView(R.id.phoneSaleCallReport_tv)
    TextView phoneSaleCallReport_tv;

    @BindView(R.id.contactNameSaleCallReport_tv)
    TextView contactNameSaleCallReport_tv;

    @BindView(R.id.cnameValueSaleCallReport_tv)
    TextView cnameValueSaleCallReport_tv;

    @BindView(R.id.dateViewSaleCallReport_tv)
    TextView dateViewSaleCallReport_tv;

    @BindView(R.id.minusVisitSaleCallReportDetail_iv)
    ImageView minusVisitSaleCallReportDetail_iv;

    @BindView(R.id.viewSaleCallReport_cv)
    CardView viewSaleCallReport_cv;

    @BindView(R.id.saleCallReportHeader_rl)
    RelativeLayout saleCallReportHeader_rl;

    @BindView(R.id.viewSaleCallReport_rv)
    RecyclerView viewSaleCallReport_rv;

    //Search Opertaion
    @BindView(R.id.employeeAdvanceSearchReport_sp)
    Spinner employeeAdvanceSearchReport_sp;

    @BindView(R.id.fromdateAdvanceSearchReportDetail_tv)
    TextView fromdateAdvanceSearchReportDetail_tv;

    @BindView(R.id.todateAdvanceSearchReport_tv)
    TextView todateAdvanceSearchReport_tv;

    @BindView(R.id.submitAdvanceSearchReport_btn)
    Button submitAdvanceSearchReport_btn;

    @BindView(R.id.titleViewSaleCallReport_tv)
    TextView titleViewSaleCallReport_tv;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_all_call_report, container, false);
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
        user_comidPref = sharedPref.getString("user_com_id", "");

        titleViewSaleCallReport_tv.setText("View Call Pending Report");

        saleCallReportHeader_rl.setVisibility(View.VISIBLE);
        viewSaleCallReport_cv.setVisibility(View.GONE);
        if (userTypePref.equals("Sales Manager")) {
            //getTargetViewRecyclerView();

            getDefaultVisitDoneReportManagerRecyclerView();
            selectAssignTo();
        }

    }

    @OnClick(R.id.minusVisitSaleCallReportDetail_iv)
    public void hideDetail(){
        saleCallReportHeader_rl.setVisibility(View.VISIBLE);
        viewSaleCallReport_cv.setVisibility(View.GONE);


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
                    getAllSalesCallReportManagerRecyclerView();
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

    private void getAllSalesCallReportManagerRecyclerView(){
        if (Connectivity.isConnected(getActivity())) {

            String fromd = fromdateAdvanceSearchReportDetail_tv.getText().toString();
            String tod = todateAdvanceSearchReport_tv.getText().toString();

            String Url = ApiLink.ROOT_URL + ApiLink.CALL_PENDING_REPORT;
            Map<String, String> map = new HashMap<>();
            map.put("logged_manager_id", userIdPref);
            map.put("add_pending", "");
            map.put("emp_id", selectAssignToId);
            map.put("startdate", fromd);
            map.put("enddate", tod);

            GSONRequest<CallPendingReportBean> dashboardGsonRequest = new GSONRequest<CallPendingReportBean>(
                    Request.Method.POST,
                    Url,
                    CallPendingReportBean.class, map,
                    new com.android.volley.Response.Listener<CallPendingReportBean>() {
                        @Override
                        public void onResponse(CallPendingReportBean response) {
                            try{
                                if (response.getSp_pending_calls().size()>0){
                                    saleCallReportHeader_rl.setVisibility(View.VISIBLE);


                                    callPendingReportAdapter = new CallPendingReportAdapter(getActivity(),response.getSp_pending_calls(), CallPendingReportFragment.this);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                    viewSaleCallReport_rv.setLayoutManager(mLayoutManager);
                                    viewSaleCallReport_rv.setItemAnimator(new DefaultItemAnimator());
                                    viewSaleCallReport_rv.setAdapter(callPendingReportAdapter);
                                    viewSaleCallReport_rv.setNestedScrollingEnabled(false);


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


    private void getDefaultVisitDoneReportManagerRecyclerView(){
        try {
            if (Connectivity.isConnected(getActivity())) {

                String Url = ApiLink.ROOT_URL + ApiLink.CALL_PENDING_REPORT;
                Map<String, String> map = new HashMap<>();
                map.put("select", "");
                map.put("m_pending", "");
                map.put("service_assignedby", userIdPref);

                GSONRequest<CallPendingReportBean> dashboardGsonRequest = new GSONRequest<CallPendingReportBean>(
                        Request.Method.POST,
                        Url,
                        CallPendingReportBean.class, map,
                        new com.android.volley.Response.Listener<CallPendingReportBean>() {
                            @Override
                            public void onResponse(CallPendingReportBean response) {
                                try {
                                    if (response.getSp_pending_calls().size() > 0) {
                                        saleCallReportHeader_rl.setVisibility(View.VISIBLE);

                                        callPendingReportAdapter = new CallPendingReportAdapter(getActivity(), response.getSp_pending_calls(), CallPendingReportFragment.this);
                                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                        viewSaleCallReport_rv.setLayoutManager(mLayoutManager);
                                        viewSaleCallReport_rv.setItemAnimator(new DefaultItemAnimator());
                                        viewSaleCallReport_rv.setAdapter(callPendingReportAdapter);
                                        viewSaleCallReport_rv.setNestedScrollingEnabled(false);

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


    public void getAllCallPendingData(CallPendingReportBean.Sp_Pending_Calls bean){
        saleCallReportHeader_rl.setVisibility(View.GONE);
        viewSaleCallReport_cv.setVisibility(View.VISIBLE);

        statusViewSaleCallReport_tv.setText(bean.getService_status());
        commentsViewSaleCallReport_tv.setText(bean.getService_comments());
        assignToByViewSaleCallReport_tv.setText(bean.getUser_name());
        phoneSaleCallReport_tv.setText(bean.getService_contactno());
        contactNameSaleCallReport_tv.setText(bean.getService_person());
        cnameValueSaleCallReport_tv.setText(bean.getLead_company());

        String date = bean.getService_createddt();
        String[] date1 = date.split(" ");
        dateViewSaleCallReport_tv.setText(date1[0]);

    }

}

