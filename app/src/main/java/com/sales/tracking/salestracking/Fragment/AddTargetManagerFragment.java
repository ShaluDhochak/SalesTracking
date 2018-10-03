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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.sales.tracking.salestracking.Activity.NavigationDrawerActivity;
import com.sales.tracking.salestracking.Bean.ExpencesSpBean;
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

import static android.widget.Toast.makeText;

public class AddTargetManagerFragment extends Fragment {

    View view;

    SharedPreferences sharedPref;
    String userIdPref, userTypePref, userCompIdPref;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    JSONParser jsonParser = new JSONParser();
    DatePickerDialog datePickerDialog;

    String selectTargetType, selectTargetTypeId,selectAssignTo, selectAssignToId, leadType_id;

    ArrayList<String> targetTypeManager;
    ArrayList<String> assignToUser;

    Map<String, String> targetTypeManagerMap = new HashMap<>();
    Map<String, String> assignToUserMap = new HashMap<>();

    ProgressDialog pDialog;

    @BindView(R.id.targetTypeAddTargetDetail_sp)
    Spinner targetTypeAddTargetDetail_sp;

    @BindView(R.id.callCountAddTargetDetail_et)
    EditText callCountAddTargetDetail_et;

    @BindView(R.id.targetAssignedToAddTargetDetail_sp)
    Spinner targetAssignedToAddTargetDetail_sp;

    @BindView(R.id.startDateAddTargetDetail_tv)
    TextView startDateAddTargetDetail_tv;

    @BindView(R.id.endDateAddTargetDetail_tv)
    TextView endDateAddTargetDetail_tv;

    @BindView(R.id.submitAddTargetDetail_btn)
    Button submitAddTargetDetail_btn;

    @BindView(R.id.callCountAddTargetDetailHeading_tv)
    TextView callCountAddTargetDetailHeading_tv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_target_manager, container, false);
        ButterKnife.bind(this, view);
        return view;
   }

    @Override
    public void onResume() {
        super.onResume();
        initialiseUI();
    }

    private void initialiseUI(){

        callCountAddTargetDetailHeading_tv.setText("No. of Call/Visit");
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        userIdPref = sharedPref.getString("user_id", "");
        userTypePref = sharedPref.getString("user_type", "");
        userCompIdPref = sharedPref.getString("user_com_id", "");

        if (userTypePref.equals("Sales Manager")) {
            selectTargetType();
            selectAssignTo();
        }

    }

    private void selectTargetType() {

        List<String> taskTypeSpinner = new ArrayList<String>();
        taskTypeSpinner.add("Target Type");
        taskTypeSpinner.add("Meeting");
        taskTypeSpinner.add("Sales Call");

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_textview, taskTypeSpinner);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        targetTypeAddTargetDetail_sp.setAdapter(statusAdapter);
    }

    @OnItemSelected(R.id.targetTypeAddTargetDetail_sp)
    public void targetSelected(Spinner spinner, int position) {
        selectTargetType = spinner.getSelectedItem().toString();

        if (selectTargetType.equals("Target Type")) {
            selectTargetTypeId = "";
        }else if (selectTargetType.equals("Meeting")){
            selectTargetTypeId = "Visit";
        }else if (selectTargetType.equals("Sales Call")){
            selectTargetTypeId = "Call";
        }

    }

    private void selectAssignTo(){
        try{
            if (Connectivity.isConnected(getActivity())) {
                String Url = ApiLink.ROOT_URL + ApiLink.Dashboard_SalesPerson;
                Map<String, String> map = new HashMap<>();
                map.put("users_undermanager","" );
                map.put("user_comid", userCompIdPref);
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
            targetAssignedToAddTargetDetail_sp.setAdapter(quotationLocationDataAdapter);

        }catch (Exception e){
        }
    }

    @OnItemSelected(R.id.targetAssignedToAddTargetDetail_sp)
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


    @OnClick(R.id.submitAddTargetDetail_btn)
    public void addManager(){
        if (!selectTargetType.equals("Target Type")) {
            if (!selectAssignTo.equals("Assign To")) {
                if (!(callCountAddTargetDetail_et.getText().toString().equals("")) && !(callCountAddTargetDetail_et.getText().equals("0"))) {
                    if (!(startDateAddTargetDetail_tv.getText().toString().equals(""))){
                        if (!(endDateAddTargetDetail_tv.getText().toString().equals(""))) {
                            new addTargetManager().execute();
                        }else{
                            Toast.makeText(getActivity(), "Please Select End Date", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getActivity(), "Please Select Start Date", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getActivity(), "Please Enter no. of Call/Visit", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getActivity(), "Please Select Assigned To", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(getActivity(), "Please Select Target Type", Toast.LENGTH_SHORT).show();
        }
    }

    public class addTargetManager extends AsyncTask<String, JSONObject, JSONObject> {
        String target_empid, target_type, target_no, target_createdby, target_compid, target_startdate, target_enddate;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            target_empid = selectAssignToId;
            target_type = selectTargetTypeId;
            target_no = callCountAddTargetDetail_et.getText().toString();
            target_createdby = userIdPref;
            target_compid = userCompIdPref;
            target_startdate =startDateAddTargetDetail_tv.getText().toString();
            target_enddate = endDateAddTargetDetail_tv.getText().toString();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Assigning Target...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("target_empid", target_empid));
            params.add(new BasicNameValuePair("target_type", target_type));
            params.add(new BasicNameValuePair("target_no", target_no));
            params.add(new BasicNameValuePair("target_createdby", target_createdby));
            params.add(new BasicNameValuePair("target_compid", target_compid));
            params.add(new BasicNameValuePair("target_startdate", target_startdate));
            params.add(new BasicNameValuePair("target_enddate", target_enddate));
            params.add(new BasicNameValuePair("add", "add"));


            String url_add_task = ApiLink.ROOT_URL + ApiLink.TARGET_MANAGER;
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
                    ((NavigationDrawerActivity)getActivity()).viewTargetManagerFragment();
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
        targetTypeAddTargetDetail_sp.setSelection(0);
        targetAssignedToAddTargetDetail_sp.setSelection(0);
        callCountAddTargetDetail_et.setText("");
        startDateAddTargetDetail_tv.setText("");
        endDateAddTargetDetail_tv.setText("");
    }

    @OnClick(R.id.startDateAddTargetDetail_tv)
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
                        startDateAddTargetDetail_tv.setText(year + "-" + (monthOfYear + 1)+ "-" + dayOfMonth);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    @OnClick(R.id.endDateAddTargetDetail_tv)
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
                        endDateAddTargetDetail_tv.setText(year + "-" + (monthOfYear + 1)+ "-" + dayOfMonth);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

}
