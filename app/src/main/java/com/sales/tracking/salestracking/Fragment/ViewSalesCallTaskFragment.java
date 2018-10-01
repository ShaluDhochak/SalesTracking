package com.sales.tracking.salestracking.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.sales.tracking.salestracking.Adapter.ViewSaleCallTaskAdater;
import com.sales.tracking.salestracking.Adapter.ViewSaleCallTaskManagerAdater;
import com.sales.tracking.salestracking.Adapter.ViewSaleCallTaskManagerHeadAdater;
import com.sales.tracking.salestracking.Adapter.VisitTaskMeetingAdapter;
import com.sales.tracking.salestracking.Bean.SalesCallTaskManagerBean;
import com.sales.tracking.salestracking.Bean.SalesCallTaskSpBean;
import com.sales.tracking.salestracking.Bean.TaskMeetingBean;
import com.sales.tracking.salestracking.R;
import com.sales.tracking.salestracking.Utility.ApiLink;
import com.sales.tracking.salestracking.Utility.Connectivity;
import com.sales.tracking.salestracking.Utility.GSONRequest;
import com.sales.tracking.salestracking.Utility.JSONParser;
import com.sales.tracking.salestracking.Utility.Utilities;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

import static android.widget.Toast.makeText;

public class ViewSalesCallTaskFragment extends Fragment {

    View view;

    SharedPreferences sharedPref;
    String userIdPref, userTypePref, user_comidPref;

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

    //EditText
    @BindView(R.id.phoneNoEditViewSalesCallManager_et)
    EditText phoneNoEditViewSalesCallManager_et;

    @BindView(R.id.contactPersonEditViewSalesCallManager_et)
    EditText contactPersonEditViewSalesCallManager_et;

    @BindView(R.id.assignToEditViewSalesCallManager_sp)
    Spinner assignToEditViewSalesCallManager_sp;

    @BindView(R.id.clientNameEditSalesCallManager_sp)
    Spinner clientNameEditSalesCallManager_sp;

    @BindView(R.id.dateEditViewSalesCallManager_tv)
    TextView dateEditViewSalesCallManager_tv;

    @BindView(R.id.timeEditViewValueSalesCallManagerDetail_et)
    TextView timeEditViewValueSalesCallManagerDetail_et;

    @BindView(R.id.editOkButtonSalesCallManagerDetail_tv)
    Button editOkButtonSalesCallManagerDetail_tv;

    @BindView(R.id.minusEditViewSalesCallManagerDetail_iv)
    ImageView minusEditViewSalesCallManagerDetail_iv;

    @BindView(R.id.editViewSalesCallManagerDetails_cv)
            CardView editViewSalesCallManagerDetails_cv;


    ViewSaleCallTaskAdater viewSaleCallTaskAdater;
    ViewSaleCallTaskManagerAdater viewSaleCallTaskManagerAdater;
    ViewSaleCallTaskManagerHeadAdater viewSaleCallTaskManagerHeadAdater;

    ArrayList<SalesCallTaskSpBean.Sp_All_Service_Calls> spAttendanceList = new ArrayList<>();

    Map<String, String> clientNameMap = new HashMap<>();
    Map<String, String> assignToUserMap = new HashMap<>();
    ArrayList<String> assignToUser;
    ArrayList<String> clientNameCompany;

    String selectclientName, selectClientNameId, selectAssignTo, selectAssignToId, serviceId;

    ProgressDialog pDialog;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    String client_id, assignedTo_id;
    int editClient, editAssignedTo;


    JSONParser jsonParser = new JSONParser();

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
        user_comidPref = sharedPref.getString("user_com_id", "");

        if (userTypePref.equals("Sales Manager")) {
            getSaleCallVisitManagerRecyclerView();
            assignToByViewSaleCallTaskHeading_tv.setText("Assigned To");
        }else if (userTypePref.equals("Sales Executive")){
            assignToByViewSaleCallTaskHeading_tv.setText("Assigned By");
            getSaleCallVisitSpRecyclerView();
        }else if (userTypePref.equals("Manager Head")){
            getSaleCallVisitManagerHeadRecyclerView();
            assignToByViewSaleCallTaskHeading_tv.setText("Assigned To");
        }

        viewSaleCallTaskDetails_cv.setVisibility(View.GONE);
        editViewSalesCallManagerDetails_cv.setVisibility(View.GONE);
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
    }

    public void getViewSaleCallTask(SalesCallTaskSpBean.Sp_All_Service_Calls bean){
        viewSaleCallTaskDetails_cv.setVisibility(View.VISIBLE);
        salesCallTaskHeader_rl.setVisibility(View.GONE);

        String date = bean.getService_createddt();
        String[] date1 = date.split(" ");
       dateViewSaleCallTask_tv.setText(date1[0]);
        cnameValueSaleCallTaskDetail_tv.setText(bean.getLead_company());
        contactNameSaleCallTask_tv.setText(bean.getService_person());
        phoneSaleCallTask_tv.setText(bean.getService_contactno());
        assignToByViewSaleCallTaskHeading_tv.setText("Assign By");
        assignToByViewSaleCallTask_tv.setText(bean.getUser_name());
        commentsViewSaleCallTask_tv.setText(bean.getService_comments());
        statusViewSaleCallTask_tv.setText(bean.getService_status());

    }

    public void getViewSaleCallManagerTask(SalesCallTaskManagerBean.All_Service_Calls_Mgr bean){
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

    private void getSaleCallVisitManagerRecyclerView(){
        if (Connectivity.isConnected(getActivity())) {

            String Url = ApiLink.ROOT_URL + ApiLink.TASK_SERVICECALL;
            Map<String, String> map = new HashMap<>();
            map.put("select", "");
            map.put("m_all", "");
            map.put("service_assignedby", userIdPref);

            GSONRequest<SalesCallTaskManagerBean> dashboardGsonRequest = new GSONRequest<SalesCallTaskManagerBean>(
                    Request.Method.POST,
                    Url,
                    SalesCallTaskManagerBean.class, map,
                    new com.android.volley.Response.Listener<SalesCallTaskManagerBean>() {
                        @Override
                        public void onResponse(SalesCallTaskManagerBean response) {
                            try{
                                if (response.getAll_service_calls_mgr().size()>0){

                                    viewSaleCallTaskManagerAdater = new ViewSaleCallTaskManagerAdater(getActivity(),response.getAll_service_calls_mgr(), ViewSalesCallTaskFragment.this);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                    viewSaleCallTask_rv.setLayoutManager(mLayoutManager);
                                    viewSaleCallTask_rv.setItemAnimator(new DefaultItemAnimator());
                                    viewSaleCallTask_rv.setAdapter(viewSaleCallTaskManagerAdater);
                                    viewSaleCallTask_rv.setNestedScrollingEnabled(false);

                                }
                            }catch(Exception e){
                                e.printStackTrace();
                        //        Toast.makeText(getActivity(), "Api response Problem", Toast.LENGTH_SHORT).show();
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


    private void getSaleCallVisitManagerHeadRecyclerView(){
        try {
            if (Connectivity.isConnected(getActivity())) {

                String Url = ApiLink.ROOT_URL + ApiLink.TASK_SERVICECALL;
                Map<String, String> map = new HashMap<>();
                map.put("select", "");
                map.put("mh_all", "");
                map.put("managerhead_id", userIdPref);

                GSONRequest<SalesCallTaskManagerBean> dashboardGsonRequest = new GSONRequest<SalesCallTaskManagerBean>(
                        Request.Method.POST,
                        Url,
                        SalesCallTaskManagerBean.class, map,
                        new com.android.volley.Response.Listener<SalesCallTaskManagerBean>() {
                            @Override
                            public void onResponse(SalesCallTaskManagerBean response) {
                                try {
                                    if (response.getAll_service_calls_mgr().size() > 0) {

                                        viewSaleCallTaskManagerHeadAdater = new ViewSaleCallTaskManagerHeadAdater(getActivity(), response.getAll_service_calls_mgr(), ViewSalesCallTaskFragment.this);
                                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                        viewSaleCallTask_rv.setLayoutManager(mLayoutManager);
                                        viewSaleCallTask_rv.setItemAnimator(new DefaultItemAnimator());
                                        viewSaleCallTask_rv.setAdapter(viewSaleCallTaskManagerHeadAdater);
                                        viewSaleCallTask_rv.setNestedScrollingEnabled(false);

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    //        Toast.makeText(getActivity(), "Api response Problem", Toast.LENGTH_SHORT).show();
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

    //SalesCallTaskManagerBean

    @OnClick(R.id.minusVisitSaleCallTaskDetail_iv)
    public void minusVisitSaleCallTaskDetail(){
        viewSaleCallTaskDetails_cv.setVisibility(View.GONE);
        salesCallTaskHeader_rl.setVisibility(View.VISIBLE);
        editViewSalesCallManagerDetails_cv.setVisibility(View.GONE);

        if (userTypePref.equals("Sales Manager")) {
            getSaleCallVisitManagerRecyclerView();
            assignToByViewSaleCallTaskHeading_tv.setText("Assigned To");
        }else if (userTypePref.equals("Sales Executive")){
            assignToByViewSaleCallTaskHeading_tv.setText("Assigned By");
            getSaleCallVisitSpRecyclerView();
        }else if (userTypePref.equals("Manager Head")){
            getSaleCallVisitManagerHeadRecyclerView();
            assignToByViewSaleCallTaskHeading_tv.setText("Assigned To");
        }
    }

    public void getDeleteSaleCallManagerTask(SalesCallTaskManagerBean.All_Service_Calls_Mgr bean){
        serviceId = bean.getService_id();
        new deleteSalesPersonManager().execute();
    }

    public void getEditViewSaleCallManagerTask(SalesCallTaskManagerBean.All_Service_Calls_Mgr bean){
        viewSaleCallTaskDetails_cv.setVisibility(View.GONE);
        salesCallTaskHeader_rl.setVisibility(View.GONE);
        editViewSalesCallManagerDetails_cv.setVisibility(View.VISIBLE);

        phoneNoEditViewSalesCallManager_et.setText(bean.getService_contactno());
        contactPersonEditViewSalesCallManager_et.setText(bean.getService_person());

        String date = bean.getService_createddt();
        String[] date1 = date.split(" ");
        dateEditViewSalesCallManager_tv.setText(date1[0]);
        timeEditViewValueSalesCallManagerDetail_et.setText(convertIn12Hours(date1[1]));

        client_id = bean.getLead_company().toString();
        assignedTo_id = bean.getUser_name().toString();

        selectClientName();
        selectAssignTo();
        serviceId = bean.getService_id();

    }

    @OnClick(R.id.minusEditViewSalesCallManagerDetail_iv)
    public void hideEditViewSalesCallManagerDetail(){
        editViewSalesCallManagerDetails_cv.setVisibility(View.GONE);
        viewSaleCallTaskDetails_cv.setVisibility(View.GONE);
        salesCallTaskHeader_rl.setVisibility(View.VISIBLE);

        if (userTypePref.equals("Sales Manager")) {
            getSaleCallVisitManagerRecyclerView();
            assignToByViewSaleCallTaskHeading_tv.setText("Assigned To");
        }else if (userTypePref.equals("Sales Executive")){
            assignToByViewSaleCallTaskHeading_tv.setText("Assigned By");
            getSaleCallVisitSpRecyclerView();
        }else if (userTypePref.equals("Manager Head")){
            getSaleCallVisitManagerHeadRecyclerView();
            assignToByViewSaleCallTaskHeading_tv.setText("Assigned To");
        }
    }

    @OnClick(R.id.editOkButtonSalesCallManagerDetail_tv)
    public void submitEditViewSalesCallManager(){
        editViewSalesCallManagerDetails_cv.setVisibility(View.VISIBLE);
        viewSaleCallTaskDetails_cv.setVisibility(View.GONE);
        salesCallTaskHeader_rl.setVisibility(View.GONE);

        if (!selectAssignTo.equals("Assign To")){
            if (!selectclientName.equals("Client Name")){
                new addEditSalesPersonSp().execute();
            }else{
                Toast.makeText(getActivity(), "Please Select Client Name", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getActivity(), "Please Select Assign to", Toast.LENGTH_SHORT).show();
        }

    }

    public class addEditSalesPersonSp extends AsyncTask<String, JSONObject, JSONObject> {
        String service_uid, service_person, service_contactno, service_leadid, service_id;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            service_uid = userIdPref;
            service_person = contactPersonEditViewSalesCallManager_et.getText().toString();
            service_contactno = phoneNoEditViewSalesCallManager_et.getText().toString();
            service_leadid = selectClientNameId;
            service_id = serviceId;

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Editing Sales Call...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("filter[service_uid]", service_id));
            params.add(new BasicNameValuePair("filter[service_person]", service_person));
            params.add(new BasicNameValuePair("filter[service_contactno]", service_contactno));
            params.add(new BasicNameValuePair("filter[service_leadid]", service_leadid));
            params.add(new BasicNameValuePair("service_id", service_id));
            params.add(new BasicNameValuePair("edit", "edit"));

            String url_add_task = ApiLink.ROOT_URL + ApiLink.TASK_SERVICECALL;
            JSONObject json = jsonParser.makeHttpRequest(url_add_task, "POST", params);

            try {
                int success = json.getInt(TAG_SUCCESS);
                String message = json.getString(TAG_MESSAGE);
                if (success == 1 && message.equals("Updated Successfully")) {
                    return json;
                }
                else {
                    return null;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(JSONObject response) {
            try {
                pDialog.dismiss();
                if (!(response == null)) {
                    makeText(getActivity(),"Updated Successfully", Toast.LENGTH_SHORT).show();

                }
                else {
                    makeText(getActivity(), "Not Updated", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (Exception e) {
            }
        }
    }

    public class deleteSalesPersonManager extends AsyncTask<String, JSONObject, JSONObject> {
        String service_id;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            service_id = serviceId;

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Editing Sales Call...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
             params.add(new BasicNameValuePair("service_id", service_id));
            params.add(new BasicNameValuePair("delete", ""));

            String url_add_task = ApiLink.ROOT_URL + ApiLink.TASK_SERVICECALL;
            JSONObject json = jsonParser.makeHttpRequest(url_add_task, "POST", params);

            try {
                int success = json.getInt(TAG_SUCCESS);
                String message = json.getString(TAG_MESSAGE);
                if (success == 1 && message.equals("Deleted Successfully")) {
                    return json;
                }
                else {
                    return null;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(JSONObject response) {
            try {
                pDialog.dismiss();
                if (!(response == null)) {
                    makeText(getActivity(),"Deleted Successfully", Toast.LENGTH_SHORT).show();

                    if (userTypePref.equals("Sales Manager")) {
                        getSaleCallVisitManagerRecyclerView();
                        assignToByViewSaleCallTaskHeading_tv.setText("Assigned To");
                    }else if (userTypePref.equals("Sales Executive")){
                        assignToByViewSaleCallTaskHeading_tv.setText("Assigned By");
                        getSaleCallVisitSpRecyclerView();
                    }else if (userTypePref.equals("Manager Head")){
                        getSaleCallVisitManagerHeadRecyclerView();
                        assignToByViewSaleCallTaskHeading_tv.setText("Assigned To");
                    }
                }
                else {
                    makeText(getActivity(), "Not Updated", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (Exception e) {
            }
        }
    }

    private void selectAssignTo(){
        try{
            if (Connectivity.isConnected(getActivity())) {
                String Url = ApiLink.ROOT_URL + ApiLink.Dashboard_SalesPerson;
                Map<String, String> map = new HashMap<>();
                map.put("users","" );
                map.put("user_comid", user_comidPref);

                final GSONRequest<TaskMeetingBean> locationSpinnerGsonRequest = new GSONRequest<TaskMeetingBean>(
                        Request.Method.POST,
                        Url,
                        TaskMeetingBean.class,map,
                        new com.android.volley.Response.Listener<TaskMeetingBean>() {
                            @Override
                            public void onResponse(TaskMeetingBean response) {
                                assignToUser.clear();
                                assignToUser.add("Assign To");
                                for(int i=0;i<response.getUsers_dd().size();i++)
                                {
                                    assignToUser.add(response.getUsers_dd().get(i).getUser_name());
                                    assignToUserMap.put(response.getUsers_dd().get(i).getUser_id(), response.getUsers_dd().get(i).getUser_name());
                                    String myString = assignedTo_id; //the value you want the position for
                                    if (myString.equals(response.getUsers_dd().get(i).getUser_name())) {
                                        editAssignedTo = i + 1;
                                        assignToEditViewSalesCallManager_sp.setSelection(editAssignedTo);
                                    }
                                }
                            }
                        },
                        new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Utilities.serverError(getActivity());
                            }
                        });
                locationSpinnerGsonRequest.setShouldCache(false);
                Utilities.getRequestQueue(getActivity()).add(locationSpinnerGsonRequest);
            }
            assignToUser = new ArrayList<String>();
            assignToUser.clear();
            assignToUser.add("Assign To");
            ArrayAdapter<String> quotationLocationDataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_textview, assignToUser);
            quotationLocationDataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
            assignToEditViewSalesCallManager_sp.setAdapter(quotationLocationDataAdapter);

        }catch (Exception e){
        }
    }

    @OnItemSelected(R.id.assignToEditViewSalesCallManager_sp)
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

    private void selectClientName(){
        try {
            if (Connectivity.isConnected(getActivity())) {
                String Url = ApiLink.ROOT_URL + ApiLink.Dashboard_SalesPerson;
                Map<String, String> map = new HashMap<>();
                map.put("clients", "");
                map.put("lead_comid", user_comidPref);

                final GSONRequest<TaskMeetingBean> clientSpinnerGsonRequest = new GSONRequest<TaskMeetingBean>(
                        Request.Method.POST,
                        Url,
                        TaskMeetingBean.class, map,
                        new com.android.volley.Response.Listener<TaskMeetingBean>() {
                            @Override
                            public void onResponse(TaskMeetingBean response) {
                                clientNameCompany.clear();
                                clientNameCompany.add("Client Name");
                                for (int i = 0; i < response.getClients_dd().size(); i++) {
                                    clientNameCompany.add(response.getClients_dd().get(i).getLead_company());
                                    clientNameMap.put(response.getClients_dd().get(i).getLead_id(), response.getClients_dd().get(i).getLead_company());
                                    String myString = client_id; //the value you want the position for
                                    if (myString.equals(response.getClients_dd().get(i).getLead_company())) {
                                        editClient = i + 1;
                                        clientNameEditSalesCallManager_sp.setSelection(editClient);
                                    }
                                }
                            }
                        },
                        new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Utilities.serverError(getActivity());
                            }
                        });
                clientSpinnerGsonRequest.setShouldCache(false);
                Utilities.getRequestQueue(getActivity()).add(clientSpinnerGsonRequest);
            }
            clientNameCompany = new ArrayList<String>();
            clientNameCompany.clear();
            clientNameCompany.add("Client Name");
            ArrayAdapter<String> quotationLocationDataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_textview, clientNameCompany);
            quotationLocationDataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
            clientNameEditSalesCallManager_sp.setAdapter(quotationLocationDataAdapter);

        }catch (Exception e){

        }
    }

    @OnItemSelected(R.id.clientNameEditSalesCallManager_sp)
    public void clientAddSelected(Spinner spinner, int position) {
        selectclientName = spinner.getSelectedItem().toString();
        for (Map.Entry<String, String> e : clientNameMap.entrySet()) {
            Object key = e.getKey();
            Object value = e.getValue();
            if (value.equals(selectclientName)) {
                selectClientNameId = (String) key;
            }
        }
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
