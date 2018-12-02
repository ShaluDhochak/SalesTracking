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
import com.sales.tracking.salestracking.Adapter.ViewExpensesMgrHeadReportAdapter;
import com.sales.tracking.salestracking.Adapter.ViewExpensesReportAdapter;
import com.sales.tracking.salestracking.Bean.AllExpensesMgrheadBean;
import com.sales.tracking.salestracking.Bean.CallDoneReportBean;
import com.sales.tracking.salestracking.Bean.TaskMeetingBean;
import com.sales.tracking.salestracking.Bean.ViewExpensesReportBean;
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


public class ViewExpensesReportFragment extends Fragment {

   @BindView(R.id.statusExpensesReport_tv)
    TextView statusExpensesReport_tv;

   @BindView(R.id.detailsExpensesReport_tv)
   TextView detailsExpensesReport_tv;

   @BindView(R.id.modeExpensesReport_tv)
   TextView modeExpensesReport_tv;

   @BindView(R.id.expenseByExpensesReport_tv)
   TextView expenseByExpensesReport_tv;

   @BindView(R.id.amountExpensesReport_tv)
   TextView amountExpensesReport_tv;

   @BindView(R.id.categoryExpensesReport_tv)
   TextView categoryExpensesReport_tv;

   @BindView(R.id.dateExpensesReport_tv)
   TextView dateExpensesReport_tv;

   @BindView(R.id.minusViewExpensesReport_iv)
    ImageView minusViewExpensesReport_iv;

   @BindView(R.id.viewExpensesReportDetails_cv)
    CardView viewExpensesReportDetails_cv;

   @BindView(R.id.viewExpensesReport_rl)
    RelativeLayout viewExpensesReport_rl;

   @BindView(R.id.viewExpensesReport_rv)
    RecyclerView viewExpensesReport_rv;

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

    ViewExpensesReportAdapter viewExpensesReportAdapter;
    ViewExpensesMgrHeadReportAdapter viewExpensesMgrHeadReportAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_expenses_report, container, false);
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

        viewExpensesReport_rl.setVisibility(View.VISIBLE);
        viewExpensesReportDetails_cv.setVisibility(View.GONE);
        if (userTypePref.equals("Sales Manager")) {
            getDefaultExpensesReportManagerRecyclerView();
            selectAssignTo();
        }else if (userTypePref.equals("Manager Head")){
            getDefaultExpensesReportManagerHeadRecyclerView();
            selectAssignToMgrHead();
        }

    }

    @OnClick(R.id.minusViewExpensesReport_iv)
    public void hideDetail(){
        viewExpensesReport_rl.setVisibility(View.VISIBLE);
        viewExpensesReportDetails_cv.setVisibility(View.GONE);
        if (userTypePref.equals("Sales Manager")) {
            getDefaultExpensesReportManagerRecyclerView();
            selectAssignTo();
        }else if (userTypePref.equals("Manager Head")){
            getDefaultExpensesReportManagerHeadRecyclerView();
            selectAssignToMgrHead();
        }
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

    private void selectAssignToMgrHead(){
        try{
            if (Connectivity.isConnected(getActivity())) {
                String Url = ApiLink.ROOT_URL + ApiLink.CALL_MANAGER_HEAD_NOTIFICATION;
                Map<String, String> map = new HashMap<>();
                map.put("get_users","" );
                map.put("managerhead_id", userIdPref);


                final GSONRequest<TaskMeetingBean> targetAssignToGsonRequest = new GSONRequest<TaskMeetingBean>(
                        Request.Method.POST,
                        Url,
                        TaskMeetingBean.class,map,
                        new com.android.volley.Response.Listener<TaskMeetingBean>() {
                            @Override
                            public void onResponse(TaskMeetingBean response) {
                                assignToUser.clear();
                                assignToUser.add("Employee Name");
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
            assignToUser.add("Employee Name");
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
        if (userTypePref.equals("Sales Manager")) {

            if (!selectAssignTo.equals("Assign To")) {
                if (!(fromdateAdvanceSearchReportDetail_tv.getText().toString().equals(""))) {
                    if (!(todateAdvanceSearchReport_tv.getText().toString().equals(""))) {
                        getAllExpensesReportManagerRecyclerView();

                    } else {
                        Toast.makeText(getActivity(), "Please Select End Date", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Please Select Start Date", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Please Select Assigned To", Toast.LENGTH_SHORT).show();
            }
        }else if (userTypePref.equals("Manager Head")) {
            if (!selectAssignTo.equals("Employee Name")) {
                if (!(fromdateAdvanceSearchReportDetail_tv.getText().toString().equals(""))) {
                    if (!(todateAdvanceSearchReport_tv.getText().toString().equals(""))) {
                        getAllExpensesReportManagerHeadRecyclerView();

                    } else {
                        Toast.makeText(getActivity(), "Please Select End Date", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Please Select Start Date", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Please Select Employee Name", Toast.LENGTH_SHORT).show();
            }
        }

    }


    private void getAllExpensesReportManagerRecyclerView(){
        try {
            if (Connectivity.isConnected(getActivity())) {

                String fromd = fromdateAdvanceSearchReportDetail_tv.getText().toString();
                String tod = todateAdvanceSearchReport_tv.getText().toString();

                String Url = ApiLink.ROOT_URL + ApiLink.EXPENSES_REPORT;
                Map<String, String> map = new HashMap<>();
                map.put("logged_manager_id", userIdPref);
                map.put("add", "");
                map.put("emp_id", selectAssignToId);
                map.put("startdate", fromd);
                map.put("enddate", tod);

                GSONRequest<ViewExpensesReportBean> dashboardGsonRequest = new GSONRequest<ViewExpensesReportBean>(
                        Request.Method.POST,
                        Url,
                        ViewExpensesReportBean.class, map,
                        new com.android.volley.Response.Listener<ViewExpensesReportBean>() {
                            @Override
                            public void onResponse(ViewExpensesReportBean response) {
                                try {
                                    if (response.getSp_expenses().size() > 0) {
                                        viewExpensesReport_rl.setVisibility(View.VISIBLE);

                                        viewExpensesReportAdapter = new ViewExpensesReportAdapter(getActivity(), response.getSp_expenses(), ViewExpensesReportFragment.this);
                                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                        viewExpensesReport_rv.setLayoutManager(mLayoutManager);
                                        viewExpensesReport_rv.setItemAnimator(new DefaultItemAnimator());
                                        viewExpensesReport_rv.setAdapter(viewExpensesReportAdapter);
                                        viewExpensesReport_rv.setNestedScrollingEnabled(false);

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    //   Toast.makeText(getActivity(), "Api response Problem", Toast.LENGTH_SHORT).show();
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
        }catch (Exception e){

        }
    }

    private void getAllExpensesReportManagerHeadRecyclerView(){
        try {
            if (Connectivity.isConnected(getActivity())) {

                String fromd = fromdateAdvanceSearchReportDetail_tv.getText().toString();
                String tod = todateAdvanceSearchReport_tv.getText().toString();

                String Url = ApiLink.ROOT_URL + ApiLink.EXPENSES_MGR_HEAD_REPORT;
                Map<String, String> map = new HashMap<>();
                map.put("logged_head_manager_id", userIdPref);
                map.put("add", "");
                map.put("emp_id", selectAssignToId);
                map.put("startdate", fromd);
                map.put("enddate", tod);

                GSONRequest<AllExpensesMgrheadBean> dashboardGsonRequest = new GSONRequest<AllExpensesMgrheadBean>(
                        Request.Method.POST,
                        Url,
                        AllExpensesMgrheadBean.class, map,
                        new com.android.volley.Response.Listener<AllExpensesMgrheadBean>() {
                            @Override
                            public void onResponse(AllExpensesMgrheadBean response) {
                                try {
                                    if (response.getExpenses_report().size() > 0) {
                                        viewExpensesReport_rl.setVisibility(View.VISIBLE);

                                        viewExpensesMgrHeadReportAdapter = new ViewExpensesMgrHeadReportAdapter(getActivity(), response.getExpenses_report(), ViewExpensesReportFragment.this);
                                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                        viewExpensesReport_rv.setLayoutManager(mLayoutManager);
                                        viewExpensesReport_rv.setItemAnimator(new DefaultItemAnimator());
                                        viewExpensesReport_rv.setAdapter(viewExpensesMgrHeadReportAdapter);
                                        viewExpensesReport_rv.setNestedScrollingEnabled(false);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    // Toast.makeText(getActivity(), "Api response Problem", Toast.LENGTH_SHORT).show();
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
        }catch (Exception e){

        }
    }

    private void getDefaultExpensesReportManagerHeadRecyclerView(){
        try {
            if (Connectivity.isConnected(getActivity())) {

                String Url = ApiLink.ROOT_URL + ApiLink.EXPENSES_MGR_HEAD_REPORT;
                Map<String, String> map = new HashMap<>();
                map.put("select", "");
                map.put("all", "");
                map.put("user_reporting_to", userIdPref);

                GSONRequest<AllExpensesMgrheadBean> dashboardGsonRequest = new GSONRequest<AllExpensesMgrheadBean>(
                        Request.Method.POST,
                        Url,
                        AllExpensesMgrheadBean.class, map,
                        new com.android.volley.Response.Listener<AllExpensesMgrheadBean>() {
                            @Override
                            public void onResponse(AllExpensesMgrheadBean response) {
                                try {
                                    if (response.getExpenses_report().size() > 0) {
                                        viewExpensesReport_rl.setVisibility(View.VISIBLE);

                                        viewExpensesMgrHeadReportAdapter = new ViewExpensesMgrHeadReportAdapter(getActivity(), response.getExpenses_report(), ViewExpensesReportFragment.this);
                                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                        viewExpensesReport_rv.setLayoutManager(mLayoutManager);
                                        viewExpensesReport_rv.setItemAnimator(new DefaultItemAnimator());
                                        viewExpensesReport_rv.setAdapter(viewExpensesMgrHeadReportAdapter);
                                        viewExpensesReport_rv.setNestedScrollingEnabled(false);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                   // Toast.makeText(getActivity(), "Api response Problem", Toast.LENGTH_SHORT).show();
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
           // Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    private void getDefaultExpensesReportManagerRecyclerView(){
        try {
            if (Connectivity.isConnected(getActivity())) {

                String Url = ApiLink.ROOT_URL + ApiLink.EXPENSES_REPORT;
                Map<String, String> map = new HashMap<>();
                map.put("select", "");
                map.put("all", "");
                map.put("reporting_to", userIdPref);

                GSONRequest<ViewExpensesReportBean> dashboardGsonRequest = new GSONRequest<ViewExpensesReportBean>(
                        Request.Method.POST,
                        Url,
                        ViewExpensesReportBean.class, map,
                        new com.android.volley.Response.Listener<ViewExpensesReportBean>() {
                            @Override
                            public void onResponse(ViewExpensesReportBean response) {
                                try {
                                    if (response.getSp_expenses().size() > 0) {
                                        viewExpensesReport_rl.setVisibility(View.VISIBLE);

                                        viewExpensesReportAdapter = new ViewExpensesReportAdapter(getActivity(), response.getSp_expenses(), ViewExpensesReportFragment.this);
                                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                        viewExpensesReport_rv.setLayoutManager(mLayoutManager);
                                        viewExpensesReport_rv.setItemAnimator(new DefaultItemAnimator());
                                        viewExpensesReport_rv.setAdapter(viewExpensesReportAdapter);
                                        viewExpensesReport_rv.setNestedScrollingEnabled(false);
                                    }
                                } catch (Exception e) {
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
        }catch(Exception e){
          //  Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    public void getExpensesReportData(ViewExpensesReportBean.Sp_Expenses bean){
        viewExpensesReport_rl.setVisibility(View.GONE);
        viewExpensesReportDetails_cv.setVisibility(View.VISIBLE);

        statusExpensesReport_tv.setText(bean.getExpense_status());
        detailsExpensesReport_tv.setText(bean.getExpense_details());
        modeExpensesReport_tv.setText(bean.getExpense_mode());
        expenseByExpensesReport_tv.setText(bean.getUser_name());
        amountExpensesReport_tv.setText(bean.getExpense_amt());
        categoryExpensesReport_tv.setText(bean.getExpcat_name());

        dateExpensesReport_tv.setText(bean.getExpense_date());
    }

    public void getExpensesMgrHeadReportData(AllExpensesMgrheadBean.Expenses_Report bean){
        viewExpensesReport_rl.setVisibility(View.GONE);
        viewExpensesReportDetails_cv.setVisibility(View.VISIBLE);

        statusExpensesReport_tv.setText(bean.getExpense_status());
        detailsExpensesReport_tv.setText(bean.getExpense_details());
        modeExpensesReport_tv.setText(bean.getExpense_mode());
        expenseByExpensesReport_tv.setText(bean.getUser_name());
        amountExpensesReport_tv.setText(bean.getExpense_amt());
        categoryExpensesReport_tv.setText(bean.getExpcat_name());

        dateExpensesReport_tv.setText(bean.getExpense_date());

    }

}
