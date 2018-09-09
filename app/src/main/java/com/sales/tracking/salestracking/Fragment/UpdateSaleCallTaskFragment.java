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
import com.sales.tracking.salestracking.Bean.SalesCallTaskSpBean;
import com.sales.tracking.salestracking.Bean.UpdateViewTaskSpBean;
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


public class UpdateSaleCallTaskFragment extends Fragment {

    @BindView(R.id.vtaskUpdateSaleTaskSp_sp)
    Spinner vtaskUpdateSaleTaskSp_sp;

    @BindView(R.id.statusUpdateSaleTaskSp_sp)
    Spinner statusUpdateSaleTaskSp_sp;

    @BindView(R.id.commentUpdateSaleTaskSp_et)
    EditText commentUpdateSaleTaskSp_et;

    @BindView(R.id.submitUpdateSaleTaskSp_btn)
    Button submitUpdateSaleTaskSp_btn;

    String selectedSaleTask, selectedSaleTaskId, selectedService_id, selectTaskStatus, selectedTaskStatusId;

    ArrayList<String> saleTaskUpdateVisitSp;

    Map<String, String> saleTaskMap = new HashMap<>();
    ProgressDialog pDialog;

    SharedPreferences sharedPref;
    String userIdPref, userTypePref;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    JSONParser jsonParser = new JSONParser();
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_update_sale_call_task, container, false);
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

        selectSaletask();
        selectSaleStatus();
    }

    private void selectSaletask(){
        try{
            if (Connectivity.isConnected(getActivity())) {
                String Url = ApiLink.ROOT_URL + ApiLink.TASK_SERVICECALL;
                Map<String, String> map = new HashMap<>();
                map.put("sp_servicecalls","" );
                map.put("service_uid", userIdPref);

                final GSONRequest<SalesCallTaskSpBean> locationSpinnerGsonRequest = new GSONRequest<SalesCallTaskSpBean>(
                        Request.Method.POST,
                        Url,
                        SalesCallTaskSpBean.class,map,
                        new com.android.volley.Response.Listener<SalesCallTaskSpBean>() {
                            @Override
                            public void onResponse(SalesCallTaskSpBean response) {
                                saleTaskUpdateVisitSp.clear();
                                saleTaskUpdateVisitSp.add("Sale Call Task");
                                for(int i=0;i<response.getSp_servicecalls_dd().size();i++)
                                {
                                    saleTaskUpdateVisitSp.add(response.getSp_servicecalls_dd().get(i).getLead_company() + " - "+ response.getSp_servicecalls_dd().get(i).getService_person() + " - "+ response.getSp_servicecalls_dd().get(i).getService_contactno());
                                    saleTaskMap.put(response.getSp_servicecalls_dd().get(i).getService_id(), response.getSp_servicecalls_dd().get(i).getLead_company() + " - "+ response.getSp_servicecalls_dd().get(i).getService_person() + " - "+ response.getSp_servicecalls_dd().get(i).getService_contactno());
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
            saleTaskUpdateVisitSp = new ArrayList<String>();
            saleTaskUpdateVisitSp.clear();
            saleTaskUpdateVisitSp.add("Sale Call Task");
            ArrayAdapter<String> quotationLocationDataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, saleTaskUpdateVisitSp);
            quotationLocationDataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
            vtaskUpdateSaleTaskSp_sp.setAdapter(quotationLocationDataAdapter);

        }catch (Exception e){
        }
    }

    @OnItemSelected(R.id.vtaskUpdateSaleTaskSp_sp)
    public void visitTaskAddSelected(Spinner spinner, int position) {
        selectedSaleTask = spinner.getSelectedItem().toString();
        for (Map.Entry<String, String> e : saleTaskMap.entrySet()) {
            Object key = e.getKey();
            Object value = e.getValue();
            if (value.equals(selectedSaleTask)) {
                selectedSaleTaskId = (String) key;
                selectedService_id= (String) key;
            }
        }
    }

    private void selectSaleStatus() {

        List<String> statusSpinner = new ArrayList<String>();
        statusSpinner.add("Status");
        statusSpinner.add("Pending");
        statusSpinner.add("Done");
        statusSpinner.add("Cancel");

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, statusSpinner);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        statusUpdateSaleTaskSp_sp.setAdapter(statusAdapter);
    }

    @OnItemSelected(R.id.statusUpdateSaleTaskSp_sp)
    public void taskStatusSelected(Spinner spinner, int position) {
        selectTaskStatus = spinner.getSelectedItem().toString();

        if (selectTaskStatus.equals("Status")) {
            selectedTaskStatusId = "";
        }else if (selectTaskStatus.equals("Pending")){
            selectedTaskStatusId = "Pending";
        }else if (selectTaskStatus.equals("Done")){
            selectedTaskStatusId = "Done";
        }else if (selectTaskStatus.equals("Cancel")){
            selectedTaskStatusId = "Cancel";
        }

    }

    @OnClick(R.id.submitUpdateSaleTaskSp_btn)
    public void updateVisitTask(){

        if (selectedSaleTask.equals("Sale Call Task")){
            if (selectTaskStatus.equals("Status")){
               if (commentUpdateSaleTaskSp_et.getText().toString().length()>0){
                   new UpdateSaleCallTaskSp().execute();
               }else{
                   Toast.makeText(getActivity(), "Please Enter Comment", Toast.LENGTH_SHORT).show();
               }
            }else{
                Toast.makeText(getActivity(), "Please Select Status", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getActivity(), "Please Select Sale Call Task", Toast.LENGTH_SHORT).show();
        }

    }

    public class UpdateSaleCallTaskSp extends AsyncTask<String, JSONObject, JSONObject> {
        String service_id,  service_status, service_comment;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            service_comment = commentUpdateSaleTaskSp_et.getText().toString();

            service_status = selectedTaskStatusId;

            service_id= selectedService_id;

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Updating Call Task...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("filter[service_comments]", service_comment));
            params.add(new BasicNameValuePair("service_id", service_id));
            params.add(new BasicNameValuePair("filter[service_status]", service_status));
            params.add(new BasicNameValuePair("update", "update"));


            String url_add_task = ApiLink.ROOT_URL + ApiLink.SALE_CALL_TASK_UPDATE;
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
        statusUpdateSaleTaskSp_sp.setSelection(0);
        commentUpdateSaleTaskSp_et.setText("");
        vtaskUpdateSaleTaskSp_sp.setSelection(0);

    }

}
