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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.sales.tracking.salestracking.Bean.RequestSpBean;
import com.sales.tracking.salestracking.Bean.SalesCallTaskSpBean;
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

public class RequestAddFragment extends Fragment {

    String selectedTaskType ="", selectedTaskTypeId = "", selectedServiceTask= "", selectServiceTaskId = "", selectedService_id = "";

    ArrayList<String> serviceTaskAddRequestSp;

    Map<String, String> serviceTaskMap = new HashMap<>();
    ProgressDialog pDialog;

    SharedPreferences sharedPref;
    String userIdPref, userTypePref,user_comidPref;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    JSONParser jsonParser = new JSONParser();

    View view;

    @BindView(R.id.taskTypeAddRequestSp_sp)
    Spinner taskTypeAddRequestSp_sp;

    @BindView(R.id.saleCallTaskRequestSp_sp)
    Spinner saleCallTaskRequestSp_sp;

    @BindView(R.id.commentAddRequestSp_et)
    EditText commentAddRequestSp_et;

    @BindView(R.id.submitAddRequestSp_btn)
    Button submitAddRequestSp_btn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_request_add, container, false);
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
    }

    private void selectServiceTask(String taskType){
        try{
            if (Connectivity.isConnected(getActivity())) {
                String Url = ApiLink.ROOT_URL + ApiLink.REQUEST_VIEW_SP;
                Map<String, String> map = new HashMap<>();
                try {
                    if (selectedTaskTypeId.equals("Sales Call")) {
                        map.put("request_uid", userIdPref);
                        map.put("sales_tasks_dropdown", "");
                    } else if (selectedTaskTypeId.equals("Visit")) {
                        map.put("visit_tasks_dropdown", "");
                        map.put("request_uid", userIdPref);
                    }
                }catch (Exception e){
                }

                final GSONRequest<RequestSpBean> locationSpinnerGsonRequest = new GSONRequest<RequestSpBean>(
                        Request.Method.POST,
                        Url,
                        RequestSpBean.class,map,
                        new com.android.volley.Response.Listener<RequestSpBean>() {
                            @Override
                            public void onResponse(RequestSpBean response) {
                                serviceTaskAddRequestSp.clear();

                                try {
                                    if (selectedTaskTypeId.equals("Sales Call")) {
                                        serviceTaskAddRequestSp.add("Sale Call Task");

                                        for (int i = 0; i < response.getSales_tasks_dropdown().size(); i++) {
                                            serviceTaskAddRequestSp.add(response.getSales_tasks_dropdown().get(i).getLead_company() + " - " + response.getSales_tasks_dropdown().get(i).getService_person() + " - " + response.getSales_tasks_dropdown().get(i).getService_contactno());
                                            serviceTaskMap.put(response.getSales_tasks_dropdown().get(i).getService_id(), response.getSales_tasks_dropdown().get(i).getLead_company() + " - " + response.getSales_tasks_dropdown().get(i).getService_person() + " - " + response.getSales_tasks_dropdown().get(i).getService_contactno());
                                        }
                                    } else if (selectedTaskTypeId.equals("Visit")) {
                                        serviceTaskAddRequestSp.add("Visit Task");

                                        for (int i = 0; i < response.getVisit_tasks_dropdown().size(); i++) {
                                            serviceTaskAddRequestSp.add(response.getVisit_tasks_dropdown().get(i).getLead_company() + " - " + response.getVisit_tasks_dropdown().get(i).getPurpose_name() + " - " + response.getVisit_tasks_dropdown().get(i).getVisit_address());
                                            serviceTaskMap.put(response.getVisit_tasks_dropdown().get(i).getVisit_id(), response.getVisit_tasks_dropdown().get(i).getLead_company() + " - " + response.getVisit_tasks_dropdown().get(i).getPurpose_name() + " - " + response.getVisit_tasks_dropdown().get(i).getVisit_address());
                                        }
                                    }
                                }catch(Exception e){
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
            serviceTaskAddRequestSp = new ArrayList<String>();
            serviceTaskAddRequestSp.clear();
            if (selectedTaskTypeId.equals("Sales Call")) {

                serviceTaskAddRequestSp.add("Sale Call Task");
            }else if (selectedTaskTypeId.equals("Visit")) {

                serviceTaskAddRequestSp.add("Visit Task");
            }

            ArrayAdapter<String> quotationLocationDataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, serviceTaskAddRequestSp);
            quotationLocationDataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
            saleCallTaskRequestSp_sp.setAdapter(quotationLocationDataAdapter);

        }catch (Exception e){
        }
    }

    @OnItemSelected(R.id.saleCallTaskRequestSp_sp)
    public void saleCallAddSelected(Spinner spinner, int position) {
        selectedServiceTask = spinner.getSelectedItem().toString();
        for (Map.Entry<String, String> e : serviceTaskMap.entrySet()) {
            Object key = e.getKey();
            Object value = e.getValue();
            if (value.equals(selectedServiceTask)) {
                selectServiceTaskId = (String) key;
                selectedService_id= (String) key;
            }
        }
    }

    private void selectTaskType() {

        List<String> selectSpinner = new ArrayList<String>();
        selectSpinner.add("Task type");
        selectSpinner.add("Sales Call");
        selectSpinner.add("Visit");

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, selectSpinner);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        taskTypeAddRequestSp_sp.setAdapter(statusAdapter);
    }

    @OnItemSelected(R.id.taskTypeAddRequestSp_sp)
    public void taskTypeSelected(Spinner spinner, int position) {
        selectedTaskType = spinner.getSelectedItem().toString();

        if (selectedTaskType.equals("Task type")) {
            selectedTaskTypeId = "Task type";
        }else if (selectedTaskType.equals("Sales Call")){
            selectedTaskTypeId = "Sales Call";
        }else if (selectedTaskType.equals("Visit")){
            selectedTaskTypeId = "Visit";
        }
        selectServiceTask(selectedTaskTypeId);
    }

    @OnClick(R.id.submitAddRequestSp_btn)
    public void addRequestTask(){

        if (!selectedTaskType.equals("Task type")){
            if (commentAddRequestSp_et.getText().toString().length()>0){
                new AddRequestTaskSp().execute();
            }else {
                Toast.makeText(getActivity(), "Please Enter Comment", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getActivity(), "Please Select Task Type", Toast.LENGTH_SHORT).show();
        }
    }

    public class AddRequestTaskSp extends AsyncTask<String, JSONObject, JSONObject> {
        String request_compid, request_uid, request_comments, request_type, service_task;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            request_compid = user_comidPref;
            request_uid = userIdPref;
            request_comments = commentAddRequestSp_et.getText().toString();
            request_type = selectedTaskTypeId;
            service_task = selectedService_id;

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Adding Request...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("request_compid", user_comidPref));
            params.add(new BasicNameValuePair("request_uid", request_uid));
            params.add(new BasicNameValuePair("request_comments", request_comments));
            params.add(new BasicNameValuePair("request_type", request_type));
            params.add(new BasicNameValuePair("service_task", service_task));
            params.add(new BasicNameValuePair("add", "add"));


            String url_add_task = ApiLink.ROOT_URL + ApiLink.REQUEST_VIEW_SP;
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
        taskTypeAddRequestSp_sp.setSelection(0);
        commentAddRequestSp_et.setText("");
        saleCallTaskRequestSp_sp.setSelection(0);
    }


}
