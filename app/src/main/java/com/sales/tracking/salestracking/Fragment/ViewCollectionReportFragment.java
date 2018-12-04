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
import com.sales.tracking.salestracking.Adapter.ViewCollectionAdSearchReportAdapter;
import com.sales.tracking.salestracking.Adapter.ViewCollectionAdapter;
import com.sales.tracking.salestracking.Adapter.ViewCollectionReportAdapter;
import com.sales.tracking.salestracking.Adapter.ViewCollectionSpReportAdapter;
import com.sales.tracking.salestracking.Adapter.ViewExpensesMgrHeadReportAdapter;
import com.sales.tracking.salestracking.Adapter.ViewExpensesReportAdapter;
import com.sales.tracking.salestracking.Bean.CollectionListBean;
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


public class ViewCollectionReportFragment extends Fragment {


    View view;

    //Manager
    @BindView(R.id.billNoManagerLead_tv)
    TextView billNoManagerLead_tv;

    @BindView(R.id.clientNameManagerLead_tv)
    TextView clientNameManagerLead_tv;

    @BindView(R.id.remarkManagerLead_tv)
    TextView remarkManagerLead_tv;

    @BindView(R.id.collectionModeManagerLead_tv)
    TextView collectionModeManagerLead_tv;

    //manager Account
    @BindView(R.id.viewCollectionManagerDetails_cv)
    CardView viewCollectionManagerDetails_cv;

    @BindView(R.id.nameViewManagerLead_tv)
    TextView nameViewManagerLead_tv;

    @BindView(R.id.totalCollectionManagerViewCollection_tv)
    TextView totalCollectionManagerViewCollection_tv;

    @BindView(R.id.dateViewManagerCollection_tv)
    TextView dateViewManagerCollection_tv;

    @BindView(R.id.minusManagerViewCollection_iv)
    ImageView minusManagerViewCollection_iv;

    @BindView(R.id.viewTotalReportManagerCollection_rv)
    RecyclerView viewTotalReportManagerCollection_rv;

    //Manager head
    @BindView(R.id.salesViewCollectionReportManagerHeader_rl)
    RelativeLayout salesViewCollectionReportManagerHeader_rl;


    SharedPreferences sharedPref;
    String userIdPref, userTypePref, userComidPref;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    JSONParser jsonParser = new JSONParser();
    ProgressDialog pDialog;

    ViewCollectionReportAdapter viewCollectionAdapter;

    //Search Opertaion
    @BindView(R.id.employeeAdvanceSearchReport_sp)
    Spinner employeeAdvanceSearchReport_sp;

    @BindView(R.id.fromdateAdvanceSearchReportDetail_tv)
    TextView fromdateAdvanceSearchReportDetail_tv;

    @BindView(R.id.todateAdvanceSearchReport_tv)
    TextView todateAdvanceSearchReport_tv;

    @BindView(R.id.submitAdvanceSearchReport_btn)
    Button submitAdvanceSearchReport_btn;

    //Title
    @BindView(R.id.titleViewCollectionReport_tv)
    TextView titleViewCollectionReport_tv;


    DatePickerDialog datePickerDialog;

    String selectAssignTo, selectAssignToId;

    ArrayList<String> assignToUser;

    Map<String, String> assignToUserMap = new HashMap<>();

    ViewExpensesReportAdapter viewExpensesReportAdapter;
    ViewCollectionAdSearchReportAdapter viewCollectionAdSearchReportAdapter;

    ViewCollectionSpReportAdapter viewCollectionSpReportAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_view_collection_report, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // getTodaysTaskRecyclerView();
        initialiseUI();
    }


    private void initialiseUI()
    {

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        userIdPref = sharedPref.getString("user_id", "");
        userTypePref = sharedPref.getString("user_type", "");
        userComidPref = sharedPref.getString("user_com_id", "");

        titleViewCollectionReport_tv.setText("Collection Report");

        viewCollectionManagerDetails_cv.setVisibility(View.GONE);
        salesViewCollectionReportManagerHeader_rl.setVisibility(View.VISIBLE);


        if (userTypePref.equals("Sales Manager")) {
            getTodaysTaskManagerRecyclerView();
            selectAssignToMgrHead();
        }else if (userTypePref.equals("Sales Executive")){
            getTodaysTaskRecyclerView();
            selectAssignTo();
        }

    }

    public void showCollectionDetails(CollectionListBean.Collections bean){


         if (userTypePref.equals("Sales Executive")){
             viewCollectionManagerDetails_cv.setVisibility(View.VISIBLE);

             nameViewManagerLead_tv.setText(bean.getUser_name());
             totalCollectionManagerViewCollection_tv.setText(bean.getCollection_amount());
             dateViewManagerCollection_tv.setText(bean.getCollection_date());

             billNoManagerLead_tv.setText(bean.getCollection_bill_no());
             clientNameManagerLead_tv.setText(bean.getLead_company());
             remarkManagerLead_tv.setText(bean.getCollection_remark());
             collectionModeManagerLead_tv.setText(bean.getCollection_mode());
        }
    }

    public void showCollectionManagerDetails(CollectionListBean.Sp_Collection bean){
        if (userTypePref.equals("Sales Manager")) {
             viewCollectionManagerDetails_cv.setVisibility(View.VISIBLE);
            salesViewCollectionReportManagerHeader_rl.setVisibility(View.GONE);

            nameViewManagerLead_tv.setText(bean.getUser_name());
            totalCollectionManagerViewCollection_tv.setText(bean.getCollection_amount());
            dateViewManagerCollection_tv.setText(bean.getCollection_date());

            billNoManagerLead_tv.setText(bean.getCollection_bill_no());
            clientNameManagerLead_tv.setText(bean.getLead_company());
            remarkManagerLead_tv.setText(bean.getCollection_remark());
            collectionModeManagerLead_tv.setText(bean.getCollection_mode());


        }

    }

    public void showCollectionadvManagerDetails(CollectionListBean.Sp_collection_advsearch bean){
        if (userTypePref.equals("Sales Manager")) {
            viewCollectionManagerDetails_cv.setVisibility(View.VISIBLE);
            salesViewCollectionReportManagerHeader_rl.setVisibility(View.GONE);

            nameViewManagerLead_tv.setText(bean.getUser_name());
            totalCollectionManagerViewCollection_tv.setText(bean.getCollection_amount());
            dateViewManagerCollection_tv.setText(bean.getCollection_date());

            billNoManagerLead_tv.setText(bean.getCollection_bill_no());
            clientNameManagerLead_tv.setText(bean.getLead_company());
            remarkManagerLead_tv.setText(bean.getCollection_remark());
            collectionModeManagerLead_tv.setText(bean.getCollection_mode());


        }
    }

    @OnClick(R.id.minusManagerViewCollection_iv)
    public void hideManager1Details(){
        if (userTypePref.equals("Sales Manager")) {
            getTodaysTaskManagerRecyclerView();
            viewCollectionManagerDetails_cv.setVisibility(View.GONE);
            salesViewCollectionReportManagerHeader_rl.setVisibility(View.VISIBLE);
        }
    }



    private void selectAssignTo(){
        try{
            if (Connectivity.isConnected(getActivity())) {
                String Url = ApiLink.ROOT_URL + ApiLink.Dashboard_SalesPerson;
                Map<String, String> map = new HashMap<>();
                map.put("users_undermanager","" );
                map.put("user_comid", userComidPref);
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
        }

    }

    private void getAllExpensesReportManagerRecyclerView(){
        try {
            if (Connectivity.isConnected(getActivity())) {

                String fromd = fromdateAdvanceSearchReportDetail_tv.getText().toString();
                String tod = todateAdvanceSearchReport_tv.getText().toString();

                String Url = ApiLink.ROOT_URL + ApiLink.REP_COLLECTION;
                Map<String, String> map = new HashMap<>();
                map.put("logged_manager_id", userIdPref);
                map.put("add", "");
                map.put("emp_id", selectAssignToId);
                map.put("startdate", fromd);
                map.put("enddate", tod);

                GSONRequest<CollectionListBean> dashboardGsonRequest = new GSONRequest<CollectionListBean>(
                        Request.Method.POST,
                        Url,
                        CollectionListBean.class, map,
                        new com.android.volley.Response.Listener<CollectionListBean>() {
                            @Override
                            public void onResponse(CollectionListBean response) {
                                try{
                                    if (response.getSp_collection_advsearch().size()>0){

                                        viewCollectionAdSearchReportAdapter = new ViewCollectionAdSearchReportAdapter(getActivity(),response.getSp_collection_advsearch(), ViewCollectionReportFragment.this);
                                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                        viewTotalReportManagerCollection_rv.setLayoutManager(mLayoutManager);
                                        viewTotalReportManagerCollection_rv.setItemAnimator(new DefaultItemAnimator());
                                        viewTotalReportManagerCollection_rv.setAdapter(viewCollectionAdSearchReportAdapter);
                                        viewTotalReportManagerCollection_rv.setNestedScrollingEnabled(false);


                                    }else{

                                        viewCollectionAdSearchReportAdapter = new ViewCollectionAdSearchReportAdapter(getActivity(),new ArrayList<CollectionListBean.Sp_collection_advsearch>(), ViewCollectionReportFragment.this);
                                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                        viewTotalReportManagerCollection_rv.setLayoutManager(mLayoutManager);
                                        viewTotalReportManagerCollection_rv.setItemAnimator(new DefaultItemAnimator());
                                        viewTotalReportManagerCollection_rv.setAdapter(viewCollectionAdSearchReportAdapter);
                                        viewTotalReportManagerCollection_rv.setNestedScrollingEnabled(false);

                                    }

                                }catch(Exception e){
                                    Toast.makeText(getActivity(), "Something went wrong..", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                              //  Toast.makeText(getActivity(), "Something went wrong with api", Toast.LENGTH_SHORT).show();
                                viewCollectionAdSearchReportAdapter = new ViewCollectionAdSearchReportAdapter(getActivity(),new ArrayList<CollectionListBean.Sp_collection_advsearch>(), ViewCollectionReportFragment.this);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                viewTotalReportManagerCollection_rv.setLayoutManager(mLayoutManager);
                                viewTotalReportManagerCollection_rv.setItemAnimator(new DefaultItemAnimator());
                                viewTotalReportManagerCollection_rv.setAdapter(viewCollectionAdSearchReportAdapter);
                                viewTotalReportManagerCollection_rv.setNestedScrollingEnabled(false);
                            }
                        });
                dashboardGsonRequest.setShouldCache(false);
                Utilities.getRequestQueue(getActivity()).add(dashboardGsonRequest);
            }
        }catch (Exception e){

        }

    }


    private void getTodaysTaskRecyclerView(){
        if (Connectivity.isConnected(getActivity())) {

            String Url = ApiLink.ROOT_URL + ApiLink.COLLECTION_SP;
            Map<String, String> map = new HashMap<>();
          /*  if (userTypePref.equals("Sales Manager")) {
                map.put("reporting_to", userIdPref);
                map.put("select", "");
            }else if (userTypePref.equals("Sales Executive")){
                map.put("collection_uid", userIdPref);
                map.put("select", "");
            }
            */
            map.put("collection_uid", userIdPref);
            map.put("select", "");

            GSONRequest<CollectionListBean> dashboardGsonRequest = new GSONRequest<CollectionListBean>(
                    Request.Method.POST,
                    Url,
                    CollectionListBean.class, map,
                    new com.android.volley.Response.Listener<CollectionListBean>() {
                        @Override
                        public void onResponse(CollectionListBean response) {
                            try{
                                if (response.getCollections().size()>0){

                                    viewCollectionSpReportAdapter = new ViewCollectionSpReportAdapter(getActivity(),response.getCollections(), ViewCollectionReportFragment.this);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                    viewTotalReportManagerCollection_rv.setLayoutManager(mLayoutManager);
                                    viewTotalReportManagerCollection_rv.setItemAnimator(new DefaultItemAnimator());
                                    viewTotalReportManagerCollection_rv.setAdapter(viewCollectionSpReportAdapter);
                                    viewTotalReportManagerCollection_rv.setNestedScrollingEnabled(false);

                                }
                            }catch(Exception e){
                                Toast.makeText(getActivity(), "Something went wrong..", Toast.LENGTH_SHORT).show();
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


    private void getTodaysTaskManagerRecyclerView(){
        if (Connectivity.isConnected(getActivity())) {

            String Url = ApiLink.ROOT_URL + ApiLink.REP_COLLECTION;
            Map<String, String> map = new HashMap<>();
            map.put("reporting_to", userIdPref);
            map.put("select", "");
            map.put("all", "");

           /* if (userTypePref.equals("Sales Manager")) {
                map.put("reporting_to", userIdPref);
                map.put("select", "");
            }else if (userTypePref.equals("Sales Executive")){
                map.put("collection_uid", userIdPref);
                map.put("select", "");
            }
            */

            GSONRequest<CollectionListBean> dashboardGsonRequest = new GSONRequest<CollectionListBean>(
                    Request.Method.POST,
                    Url,
                    CollectionListBean.class, map,
                    new com.android.volley.Response.Listener<CollectionListBean>() {
                        @Override
                        public void onResponse(CollectionListBean response) {
                            try{
                                if (response.getSp_collection().size()>0){

                                    viewCollectionAdapter = new ViewCollectionReportAdapter(getActivity(),response.getSp_collection(), ViewCollectionReportFragment.this);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                    viewTotalReportManagerCollection_rv.setLayoutManager(mLayoutManager);
                                    viewTotalReportManagerCollection_rv.setItemAnimator(new DefaultItemAnimator());
                                    viewTotalReportManagerCollection_rv.setAdapter(viewCollectionAdapter);
                                    viewTotalReportManagerCollection_rv.setNestedScrollingEnabled(false);


                                }
                            }catch(Exception e){
                                Toast.makeText(getActivity(), "Something went wrong..", Toast.LENGTH_SHORT).show();
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
