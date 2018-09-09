package com.sales.tracking.salestracking.Fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.JsonParser;
import com.sales.tracking.salestracking.Bean.PurposeBean;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

import static android.view.View.GONE;
import static android.widget.Toast.makeText;


public class AddMeetingTaskFragment extends Fragment {

    View view;

    @BindView(R.id.tskTypeAddMeetingTask_sp)
    Spinner tskTypeAddMeetingTask_sp;

    @BindView(R.id.assignToAddViewMeetingTask_sp)
    Spinner assignToAddViewMeetingTask_sp;

    @BindView(R.id.clientAddMeetingTask_sp)
    Spinner clientAddMeetingTask_sp;

    @BindView(R.id.dateAddMeetingTask_tv)
    TextView dateAddMeetingTask_tv;

    @BindView(R.id.timeAddMeetingTask_tv)
    TextView timeAddMeetingTask_tv;

    @BindView(R.id.purposeAddMeetingTask_sp)
    Spinner purposeAddMeetingTask_sp;

    @BindView(R.id.addressAddMeetingTask_et)
    EditText addressAddMeetingTask_et;

    @BindView(R.id.contactPersonNameAddMeetingTask_et)
    EditText contactPersonNameAddMeetingTask_et;

    @BindView(R.id.phoneNoAddMeetingTask_et)
    EditText phoneNoAddMeetingTask_et;

    @BindView(R.id.submitAddMeetingTask_btn)
    Button submitAddMeetingTask_btn;

    @BindView(R.id.submitAddSalesCallTask_btn)
    Button submitAddSalesCallTask_btn;

    //Layout for visibility
    @BindView(R.id.phoneAddMeetingTask_rl)
    RelativeLayout phoneAddMeetingTask_rl;

    @BindView(R.id.separatorBelowContactPersonNameAddMeetingTask)
    View separatorBelowContactPersonNameAddMeetingTask;

    @BindView(R.id.contactPersonNameAddMeetingTask_rl)
    RelativeLayout contactPersonNameAddMeetingTask_rl;

    @BindView(R.id.addressAddMeetingTask_rl)
    RelativeLayout addressAddMeetingTask_rl;

    @BindView(R.id.separatorBelowAddressAddMeetingTask)
     View separatorBelowAddressAddMeetingTask;

    @BindView(R.id.separatorBelowPurposeAddMeetingTask)
    View separatorBelowPurposeAddMeetingTask;

    @BindView(R.id.purposeAddMeetingTask_rl)
    RelativeLayout purposeAddMeetingTask_rl;

    @BindView(R.id.separatorBelowTimeAddViewMeetingTask)
    View separatorBelowTimeAddViewMeetingTask;

    @BindView(R.id.timeAddViewMeetingTask_rl)
    RelativeLayout timeAddViewMeetingTask_rl;

    @BindView(R.id.separatorBelowDateAddMeetingTask)
    View separatorBelowDateAddMeetingTask;

    @BindView(R.id.dateAddViewMeetingTask_rl)
    RelativeLayout dateAddViewMeetingTask_rl;

    String selectTaskType, selectTaskTypeId, selectPurpose, selectPurposeId, selectclientName, selectClientNameId, selectAssignTo, selectAssignToId;
    String currentuserName, currentClientUserName, currentPurposeName;

    ArrayList<String> assignToUser;
    ArrayList<String> clientNameCompany;
    ArrayList<String> purposesNameCompany;

    Map<String, String> purposeNameMap = new HashMap<>();
    Map<String, String> clientNameMap = new HashMap<>();
    Map<String, String> assignToUserMap = new HashMap<>();

    DatePickerDialog datePickerDialog;
    ProgressDialog pDialog;

    SharedPreferences sharedPref;
    String userIdPref, userTypePref, user_comidPref;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    JSONParser jsonParser = new JSONParser();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_meeting_task, container, false);
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

        selectTaskType();
        selectAssignTo();
        selectClientName();
        selectPurpose();

    }

    @OnClick(R.id.dateAddMeetingTask_tv)
    public void selectDate(){
        final Calendar calenderObj = Calendar.getInstance();
        int mYear = calenderObj.get(Calendar.YEAR);
        int mMonth = calenderObj.get(Calendar.MONTH);
        int mDay = calenderObj.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        dateAddMeetingTask_tv.setText(year + "-" + (monthOfYear + 1)+ "-" + dayOfMonth);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    @OnClick(R.id.timeAddMeetingTask_tv)
    public void selectTime(){

    }

    @OnClick(R.id.submitAddMeetingTask_btn)
    public void submitAddMeetingTaskBtn(){
        new CreateNewMeetingTask().execute();

        //{"success":1,"message":"Meeting Task Created Successfully"}
    }

    @OnClick(R.id.submitAddSalesCallTask_btn)
    public void setSubmitAddSalesCallTask_btn(){
       new CreateNewSalesCallTask().execute();
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
            ArrayAdapter<String> quotationLocationDataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, assignToUser);
            quotationLocationDataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
            assignToAddViewMeetingTask_sp.setAdapter(quotationLocationDataAdapter);

        }catch (Exception e){
        }
    }

    @OnItemSelected(R.id.assignToAddViewMeetingTask_sp)
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
            ArrayAdapter<String> quotationLocationDataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, clientNameCompany);
            quotationLocationDataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
            clientAddMeetingTask_sp.setAdapter(quotationLocationDataAdapter);

        }catch (Exception e){

        }
    }

    @OnItemSelected(R.id.clientAddMeetingTask_sp)
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

    private void selectPurpose(){
        try{
            if (Connectivity.isConnected(getActivity())) {
                String Url = ApiLink.ROOT_URL + ApiLink.Dashboard_SalesPerson;
                Map<String, String> map = new HashMap<>();
                map.put("purposes", "");

                final GSONRequest<PurposeBean> clientSpinnerGsonRequest = new GSONRequest<PurposeBean>(
                        Request.Method.POST,
                        Url,
                        PurposeBean.class,map,
                        new com.android.volley.Response.Listener<PurposeBean>() {
                            @Override
                            public void onResponse(PurposeBean response) {
                                purposesNameCompany.clear();
                                purposesNameCompany.add("Purpose");
                                for(int i=0;i<response.getPurpose_dd().size();i++)
                                {
                                    purposesNameCompany.add(response.getPurpose_dd().get(i).getPurpose_name());
                                    purposeNameMap.put(response.getPurpose_dd().get(i).getPurpose_id(), response.getPurpose_dd().get(i).getPurpose_name());
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
            purposesNameCompany = new ArrayList<String>();
            purposesNameCompany.clear();
            purposesNameCompany.add("Purposes");
            ArrayAdapter<String> quotationLocationDataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, purposesNameCompany);
            quotationLocationDataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
            purposeAddMeetingTask_sp.setAdapter(quotationLocationDataAdapter);

        }catch (Exception e){
        }
    }

    @OnItemSelected(R.id.purposeAddMeetingTask_sp)
    public void purposeSelected(Spinner spinner, int position) {
        selectPurpose = spinner.getSelectedItem().toString();
        for (Map.Entry<String, String> e : purposeNameMap.entrySet()) {
            Object key = e.getKey();
            Object value = e.getValue();
            if (value.equals(selectPurpose)) {
                selectPurposeId = (String) key;
            }
        }
    }

    private void selectTaskType() {

            List<String> taskTypeSpinner = new ArrayList<String>();
            taskTypeSpinner.add("Task Type");
            taskTypeSpinner.add("Meeting");
            taskTypeSpinner.add("Sales Call");

            ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, taskTypeSpinner);
            statusAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
            tskTypeAddMeetingTask_sp.setAdapter(statusAdapter);
    }

    @OnItemSelected(R.id.tskTypeAddMeetingTask_sp)
    public void taskTypeSelected(Spinner spinner, int position){
            selectTaskType = spinner.getSelectedItem().toString();

            if (selectTaskType.equals("Meeting")) {
                submitAddMeetingTask_btn.setVisibility(View.VISIBLE);
                submitAddSalesCallTask_btn.setVisibility(GONE);
                purposeAddMeetingTask_rl.setVisibility(View.VISIBLE);

                contactPersonNameAddMeetingTask_rl.setVisibility(GONE);
                phoneAddMeetingTask_rl.setVisibility(GONE);
                addressAddMeetingTask_rl.setVisibility(View.VISIBLE);
                dateAddViewMeetingTask_rl.setVisibility(View.VISIBLE);
                timeAddViewMeetingTask_rl.setVisibility(View.VISIBLE);

                separatorBelowContactPersonNameAddMeetingTask.setVisibility(GONE);
                separatorBelowAddressAddMeetingTask.setVisibility(View.VISIBLE);
                separatorBelowPurposeAddMeetingTask.setVisibility(View.VISIBLE);
                separatorBelowTimeAddViewMeetingTask.setVisibility(View.VISIBLE);
                separatorBelowDateAddMeetingTask.setVisibility(View.VISIBLE);

            } else if (selectTaskType.equals("Sales Call")) {
                submitAddSalesCallTask_btn.setVisibility(View.VISIBLE);
                submitAddMeetingTask_btn.setVisibility(GONE);
                purposeAddMeetingTask_rl.setVisibility(GONE);

                contactPersonNameAddMeetingTask_rl.setVisibility(View.VISIBLE);
                phoneAddMeetingTask_rl.setVisibility(View.VISIBLE);
                addressAddMeetingTask_rl.setVisibility(GONE);
                dateAddViewMeetingTask_rl.setVisibility(GONE);
                timeAddViewMeetingTask_rl.setVisibility(GONE);

                separatorBelowContactPersonNameAddMeetingTask.setVisibility(View.VISIBLE);
                separatorBelowAddressAddMeetingTask.setVisibility(GONE);
                separatorBelowPurposeAddMeetingTask.setVisibility(GONE);
                separatorBelowTimeAddViewMeetingTask.setVisibility(GONE);
                separatorBelowDateAddMeetingTask.setVisibility(GONE);

            }


    }

    public class CreateNewMeetingTask extends AsyncTask<String, JSONObject, JSONObject> {
        String visit_leadid, visit_purposeid, visit_assignedby, visit_time,visit_date;
        String visit_uid, visit_address;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            visit_address = addressAddMeetingTask_et.getText().toString();

            visit_date = dateAddMeetingTask_tv.getText().toString();
            //visit_time = timeAddMeetingTask_tv.getText().toString();
            visit_time = "20:00:00";
            visit_uid = selectAssignToId;
            visit_assignedby = userIdPref;
            visit_leadid = selectClientNameId;
            visit_purposeid= selectPurposeId;

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Adding Task...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
            pDialog.dismiss();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("visit_address", visit_address));
            params.add(new BasicNameValuePair("visit_date", visit_date));
            params.add(new BasicNameValuePair("visit_time", visit_time));
            params.add(new BasicNameValuePair("visit_uid", visit_uid));
            params.add(new BasicNameValuePair("visit_assignedby", visit_assignedby));
            params.add(new BasicNameValuePair("visit_leadid", visit_leadid));
            params.add(new BasicNameValuePair("visit_purposeid", visit_purposeid));
            params.add(new BasicNameValuePair("add", "add"));

            String url_add_task = ApiLink.ROOT_URL + ApiLink.Dashboard_SalesPerson;
            JSONObject json = jsonParser.makeHttpRequest(url_add_task, "POST", params);

            try {
                int success = json.getInt(TAG_SUCCESS);
                String message = json.getString(TAG_MESSAGE);
                if (success == 1 && message.equals("Meeting Task Created Successfully")) {
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
                    makeText(getActivity(),"Meeting Task Created Successfully", Toast.LENGTH_SHORT).show();
                   // clearAll();
                }
                else {
                    makeText(getActivity(), "Already Inserted ", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (Exception e) {
            }
        }
    }



    public class CreateNewSalesCallTask extends AsyncTask<String, JSONObject, JSONObject> {
        String service_uid, service_person, service_contactno, service_leadid,service_assignedby;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            service_assignedby = userIdPref;
            service_leadid = selectClientNameId;
            service_contactno = phoneNoAddMeetingTask_et.getText().toString();
            service_uid = selectAssignToId;
            service_person= contactPersonNameAddMeetingTask_et.getText().toString();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Adding Task...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
            pDialog.dismiss();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("service_uid", service_uid));
            params.add(new BasicNameValuePair("service_person", service_person));
            params.add(new BasicNameValuePair("service_contactno", service_contactno));
            params.add(new BasicNameValuePair("service_leadid", service_leadid));
            params.add(new BasicNameValuePair("service_assignedby", service_assignedby));
            params.add(new BasicNameValuePair("add", "add"));

            String url_add_task = ApiLink.ROOT_URL + ApiLink.TASK_SERVICECALL;
            JSONObject json = jsonParser.makeHttpRequest(url_add_task, "POST", params);

            try {
                int success = json.getInt(TAG_SUCCESS);
                String message = json.getString(TAG_MESSAGE);
                if (success == 1 && message.equals("Inserted Successfully")) {
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
                    makeText(getActivity(),"Inserted Successfully", Toast.LENGTH_SHORT).show();
                    // clearAll();
                }
                else {
                    makeText(getActivity(), "Already Inserted ", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (Exception e) {
            }
        }
    }

     private void clearAll() {
      /*  leadFirstName_EditText.setText("");
        leademail_EditText.setText("");
        leadContactNo_EditText.setText("");
        leadAddress_EditText.setText("");
        locationSpinner.setSelection(0);
        leadSourceSpinner.setSelection(0);
        assignToSpinner.setSelection(0);
        leadComment_EditText.setText("");
        */
    }



}
