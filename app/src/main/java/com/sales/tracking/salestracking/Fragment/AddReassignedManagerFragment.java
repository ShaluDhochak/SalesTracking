package com.sales.tracking.salestracking.Fragment;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.sales.tracking.salestracking.Bean.RequestSpBean;
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


public class AddReassignedManagerFragment extends Fragment {

    @BindView(R.id.submitAssignRequestp_btn)
    Button submitAssignRequestp_btn;

    @BindView(R.id.assignTaskToAssignRequest_sp)
    Spinner assignTaskToAssignRequest_sp;

   /* @BindView(R.id.visitTaskAssignRequest_sp)
    Spinner visitTaskAssignRequest_sp;
    */

    @BindView(R.id.salesCallTaskAssignRequest_sp)
    Spinner salesCallTaskAssignRequest_sp;

    @BindView(R.id.taskTypeAssignRequest_sp)
    Spinner taskTypeAssignRequest_sp;

    View view;

    SharedPreferences sharedPref;
    String userIdPref, userTypePref, userCompIdPref;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    private int SELECT_FILE =1,REQUEST_CODE = 0 ;

    JSONParser jsonParser = new JSONParser();

    String selectedTasktype, selectedtaskTypeId,selectSalesCall, selectSalesCallId, selectAssignTask, selectAssignTaskId, selectedService_id;

    ArrayList<String> taskType;
    ArrayList<String> salescall;
    ArrayList<String> assignToUser;

    Map<String, String> salesCallMap;
    Map<String, String> assignToUserMap = new HashMap<>();

    ProgressDialog pDialog;

    public AddReassignedManagerFragment() {
        salesCallMap = new HashMap<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_reassigned_manager, container, false);
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
        userCompIdPref = sharedPref.getString("user_com_id", "");

        if (userTypePref.equals("Sales Manager")) {
            selectTaskType();
            selectAssignTo();
            //  selectMode();
        }

    }

    private void selectTaskType() {

        List<String> selectSpinner = new ArrayList<String>();
        selectSpinner.add("Task type");
        selectSpinner.add("Sales Call");
        selectSpinner.add("Visit");

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_textview, selectSpinner);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        taskTypeAssignRequest_sp.setAdapter(statusAdapter);
    }

    @OnItemSelected(R.id.taskTypeAssignRequest_sp)
    public void taskTypeSelected(Spinner spinner, int position) {
        selectedTasktype = spinner.getSelectedItem().toString();

        if (selectedTasktype.equals("Task type")) {
            selectedtaskTypeId = "Task type";
        }else if (selectedTasktype.equals("Sales Call")){
            selectedtaskTypeId = "call";
        }else if (selectedTasktype.equals("Visit")){
            selectedtaskTypeId = "visit";
        }
        selectServiceTask(selectedtaskTypeId);
    }

    private void selectServiceTask(String taskType){
        try{
            if (Connectivity.isConnected(getActivity())) {
                String Url = ApiLink.ROOT_URL + ApiLink.VIEW_REASSIGNED_REQUEST;
                Map<String, String> map = new HashMap<>();
                try {
                    if (selectedtaskTypeId.equals("call")) {
                        map.put("service_assignedby", userIdPref);
                        map.put("sales_tasks_dropdown", "");
                    } else if (selectedtaskTypeId.equals("visit")) {
                        map.put("visit_tasks_dropdown", "");
                        map.put("service_assignedby", userIdPref);
                    }
                }catch (Exception e){
                }
              /*  String Url = ApiLink.ROOT_URL + ApiLink.REQUEST_VIEW_SP;
                Map<String, String> map = new HashMap<>();
                try {
                    if (selectedtaskTypeId.equals("Sales Call")) {
                        map.put("request_uid", "18");
                        map.put("sales_tasks_dropdown", "");
                    } else if (selectedtaskTypeId.equals("Visit")) {
                        map.put("visit_tasks_dropdown", "");
                        map.put("request_uid", "18");
                    }
                }catch (Exception e){
                }*/

                final GSONRequest<RequestSpBean> spinnerGsonRequest = new GSONRequest<RequestSpBean>(
                        Request.Method.POST,
                        Url,
                        RequestSpBean.class,map,
                        new com.android.volley.Response.Listener<RequestSpBean>() {
                            @Override
                            public void onResponse(RequestSpBean response) {
                                salescall.clear();

                                try {
                                    if (selectedtaskTypeId.equals("call")) {
                                        salescall.add("Sale Call Task");

                                        for (int i = 0; i < response.getSales_tasks_dropdown().size(); i++) {
                                            salescall.add(response.getSales_tasks_dropdown().get(i).getLead_company() + " - " + response.getSales_tasks_dropdown().get(i).getService_person() + " - " + response.getSales_tasks_dropdown().get(i).getService_contactno());
                                            salesCallMap.put(response.getSales_tasks_dropdown().get(i).getService_id(), response.getSales_tasks_dropdown().get(i).getLead_company() + " - " + response.getSales_tasks_dropdown().get(i).getService_person() + " - " + response.getSales_tasks_dropdown().get(i).getService_contactno());
                                        }
                                    } else if (selectedtaskTypeId.equals("visit")) {
                                        salescall.add("Visit Task");

                                        for (int i = 0; i < response.getVisit_tasks_dropdown().size(); i++) {
                                            salescall.add(response.getVisit_tasks_dropdown().get(i).getLead_company() + " - " + response.getVisit_tasks_dropdown().get(i).getPurpose_name() + " - " + response.getVisit_tasks_dropdown().get(i).getVisit_address());
                                            salesCallMap.put(response.getVisit_tasks_dropdown().get(i).getVisit_id(), response.getVisit_tasks_dropdown().get(i).getLead_company() + " - " + response.getVisit_tasks_dropdown().get(i).getPurpose_name() + " - " + response.getVisit_tasks_dropdown().get(i).getVisit_address());
                                        }
                                    }
                                }catch(Exception e){
                                }
                            }
                        },
                        new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Utilities.serverError(getActivity());
                            }
                        });
                spinnerGsonRequest.setShouldCache(false);
                Utilities.getRequestQueue(getActivity()).add(spinnerGsonRequest);
            }


            salescall = new ArrayList<String>();
            salescall.clear();
            if (selectedtaskTypeId.equals("call")) {
                salescall.add("Sale Call Task");
            }else if (selectedtaskTypeId.equals("visit")) {
                salescall.add("Visit Task");
            }

            ArrayAdapter<String> quotationLocationDataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_textview, salescall);
            quotationLocationDataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
            salesCallTaskAssignRequest_sp.setAdapter(quotationLocationDataAdapter);

        }catch (Exception e){
        }
    }

    @OnItemSelected(R.id.salesCallTaskAssignRequest_sp)
    public void saleCallAddSelected(Spinner spinner, int position) {
        selectSalesCall = spinner.getSelectedItem().toString();
        for (Map.Entry<String, String> e : salesCallMap.entrySet()) {
            Object key = e.getKey();
            Object value = e.getValue();
            if (value.equals(selectSalesCall)) {
                selectSalesCallId = (String) key;
                selectedService_id= (String) key;
            }
        }
    }

    private void selectAssignTo(){
        try{
            if (Connectivity.isConnected(getActivity())) {
                String Url = ApiLink.ROOT_URL + ApiLink.Dashboard_SalesPerson;
                Map<String, String> map = new HashMap<>();
                map.put("users_undermanager","" );
                map.put("user_comid", userCompIdPref);
                map.put("user_reporting_to",userIdPref);

                final GSONRequest<TaskMeetingBean> locationSpinnerGsonRequest = new GSONRequest<TaskMeetingBean>(
                        Request.Method.POST,
                        Url,
                        TaskMeetingBean.class,map,
                        new com.android.volley.Response.Listener<TaskMeetingBean>() {
                            @Override
                            public void onResponse(TaskMeetingBean response) {
                                assignToUser.clear();
                                assignToUser.add("Assign Task To");
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
                locationSpinnerGsonRequest.setShouldCache(false);
                Utilities.getRequestQueue(getActivity()).add(locationSpinnerGsonRequest);
            }
            assignToUser = new ArrayList<String>();
            assignToUser.clear();
            assignToUser.add("Assign Task To");
            ArrayAdapter<String> quotationLocationDataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_textview, assignToUser);
            quotationLocationDataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
            assignTaskToAssignRequest_sp.setAdapter(quotationLocationDataAdapter);

        }catch (Exception e){
        }
    }

    @OnItemSelected(R.id.assignTaskToAssignRequest_sp)
    public void assignToAddSelected(Spinner spinner, int position) {
        selectAssignTask = spinner.getSelectedItem().toString();
        for (Map.Entry<String, String> e : assignToUserMap.entrySet()) {
            Object key = e.getKey();
            Object value = e.getValue();
            if (value.equals(selectAssignTask)) {
                selectAssignTaskId = (String) key;
            }
        }
    }

    @OnClick(R.id.submitAssignRequestp_btn)
    public void addRequest(){
        new addReassignedManager().execute();
      /*

        if (!selectedTasktype.equals("Task type")) {
            if (selectedTasktype.equals("Sales Call")){
              if (! selectSalesCall.equals("Sale Call Task") ){
                  if (! selectAssignTask.equals("Assign Task To")){
                        new addReassignedManager().execute();
                  }else{
                      Toast.makeText(getActivity(), "Please Select Assign Task To", Toast.LENGTH_SHORT).show();
                  }
              }else{
                  Toast.makeText(getActivity(), "Please Select Sales Call Task", Toast.LENGTH_SHORT).show();
              }
            }else if (selectedTasktype.equals("Visit")){
                if (! selectSalesCall.equals("Visit Task") ){
                    if (! selectAssignTask.equals("Assign Task To")){
                        new addReassignedManager().execute();
                    }else{
                        Toast.makeText(getActivity(), "Please Select Assign Task To", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getActivity(), "Please Select Visit Task", Toast.LENGTH_SHORT).show();
                }
            }
        }else {
            Toast.makeText(getActivity(), "Please select Task Type", Toast.LENGTH_SHORT).show();
        }
        */
    }

    public class addReassignedManager extends AsyncTask<String, JSONObject, JSONObject> {
        String service_id, visit_id, update, request_uid, request_type;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (selectedTasktype.equals("Sales Call")){
                service_id = selectedService_id;
            }else{
                service_id = "";
            }
            if (selectedTasktype.equals("Visit")){
                visit_id = selectedService_id;
            }else{
                visit_id = "";
            }

            update = "update";
            request_uid = userIdPref;
            request_type = selectedtaskTypeId ;

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Adding Reassigned...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("service_id", service_id));
            params.add(new BasicNameValuePair("visit_id", visit_id));
            params.add(new BasicNameValuePair("update", update));
            params.add(new BasicNameValuePair("request_uid", request_uid));
            params.add(new BasicNameValuePair("request_type", request_type));

            String url_add_task = ApiLink.ROOT_URL + ApiLink.VIEW_REASSIGNED_REQUEST;
            JSONObject json = jsonParser.makeHttpRequest(url_add_task, "POST", params);

            try {
                int success = json.getInt(TAG_SUCCESS);
                String message = json.getString(TAG_MESSAGE);
                if (success == 1 && message.equals("Added Successfully")) {
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
                    makeText(getActivity(),"Added Successfully", Toast.LENGTH_SHORT).show();
                    clearAll();
                }
                else {
                    makeText(getActivity(), "Not Updated", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (Exception e) {
            }
        }
    }

    private void clearAll(){
        taskTypeAssignRequest_sp.setSelection(0);
        assignTaskToAssignRequest_sp.setSelection(0);
        salesCallTaskAssignRequest_sp.setSelection(0);
     //   visitTaskAssignRequest_sp.setSelection(0);
    }


}
