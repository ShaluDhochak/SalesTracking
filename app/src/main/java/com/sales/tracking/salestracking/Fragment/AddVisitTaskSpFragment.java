package com.sales.tracking.salestracking.Fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
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

import java.text.SimpleDateFormat;
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


public class AddVisitTaskSpFragment extends Fragment {

    @BindView(R.id.purposeAddVisitTaskSp_sp)
    Spinner purposeAddVisitTaskSp_sp;

    @BindView(R.id.clientAddVisitTaskSp_sp)
    Spinner clientAddVisitTaskSp_sp;

    @BindView(R.id.timeAddVisitTaskSp_tv)
    TextView timeAddVisitTaskSp_tv;

    @BindView(R.id.dateAddVisitTaskSp_tv)
    TextView dateAddVisitTaskSp_tv;

    @BindView(R.id.addressAddVisitTaskSp_et)
    EditText addressAddVisitTaskSp_et;

    @BindView(R.id.submitAddVisitTaskSp_btn)
    Button submitAddVisitTaskSp_btn;

    String  selectPurpose, selectPurposeId, selectclientName, selectClientNameId;

    ProgressDialog pDialog;

    ArrayList<String> clientNameCompany;
    ArrayList<String> purposesNameCompany;

    Map<String, String> purposeNameMap = new HashMap<>();
    Map<String, String> clientNameMap = new HashMap<>();

    View view;
    SharedPreferences sharedPref;
    String userIdPref, userTypePref,user_comidPref;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    JSONParser jsonParser = new JSONParser();
    TimePickerDialog timePickerDialog;
    DatePickerDialog datePickerDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_visit_task_sp, container, false);
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


        selectClientName();
        selectPurpose();
    }

    @OnClick(R.id.dateAddVisitTaskSp_tv)
    public void addDateVisitTask(){
        final Calendar calenderObj = Calendar.getInstance();
        int mYear = calenderObj.get(Calendar.YEAR);
        int mMonth = calenderObj.get(Calendar.MONTH);
        int mDay = calenderObj.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        dateAddVisitTaskSp_tv.setText(year + "-" + (monthOfYear + 1)+ "-" + dayOfMonth);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    @OnClick(R.id.timeAddVisitTaskSp_tv)
    public void addVisitTaskTime(){
        final String time;
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss a");
        time = simpleDateFormat.format(mcurrentTime.getTime());
        timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                timeAddVisitTaskSp_tv.setText(selectedHour%12 + ":" + selectedMinute  + ((selectedHour>=12) ? " PM" : " AM"));
            }
        }, hour, minute, true);//Yes 24 hour time
        timePickerDialog.show();
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
            ArrayAdapter<String> quotationLocationDataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_textview, clientNameCompany);
            quotationLocationDataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
            clientAddVisitTaskSp_sp.setAdapter(quotationLocationDataAdapter);

        }catch (Exception e){

        }
    }

    @OnItemSelected(R.id.clientAddVisitTaskSp_sp)
    public void clientAddVisitTaskSelected(Spinner spinner, int position) {
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
            ArrayAdapter<String> quotationLocationDataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_textview, purposesNameCompany);
            quotationLocationDataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
            purposeAddVisitTaskSp_sp.setAdapter(quotationLocationDataAdapter);

        }catch (Exception e){
        }
    }

    @OnItemSelected(R.id.purposeAddVisitTaskSp_sp)
    public void purposeAddVisitTaskSelected(Spinner spinner, int position) {
        selectPurpose = spinner.getSelectedItem().toString();
        for (Map.Entry<String, String> e : purposeNameMap.entrySet()) {
            Object key = e.getKey();
            Object value = e.getValue();
            if (value.equals(selectPurpose)) {
                selectPurposeId = (String) key;
            }
        }
    }

    @OnClick(R.id.submitAddVisitTaskSp_btn)
    public void submitAddVisitTaskSp_btn(){
        if (!selectclientName.equals("Client Name")){
            if (!selectPurpose.equals("Purposes")){
                if (addressAddVisitTaskSp_et.getText().toString().length()>0){
                    if (dateAddVisitTaskSp_tv.getText().toString().length()>0){
                        if (timeAddVisitTaskSp_tv.getText().toString().length()>0){
                            new CreateNewVisitTask().execute();
                        }else{
                            Toast.makeText(getActivity(), "Please Select Time", Toast.LENGTH_SHORT).show();
                        }
                    }else
                    {
                        Toast.makeText(getActivity(), "Please Select Date", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getActivity(), "Please Select Address", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getActivity(), "Please Select Purpose", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(getActivity(), "Please Select Client Name", Toast.LENGTH_SHORT).show();
        }

    }
    public class CreateNewVisitTask extends AsyncTask<String, JSONObject, JSONObject> {
        String visit_leadid, visit_purposeid, visit_assignedby, visit_time,visit_date;
        String visit_uid, visit_address;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            visit_address = addressAddVisitTaskSp_et.getText().toString();
            visit_date = dateAddVisitTaskSp_tv.getText().toString();
            visit_time = timeAddVisitTaskSp_tv.getText().toString();
        //    visit_uid = selectAssignToId;
            visit_assignedby = userIdPref;
            visit_leadid = selectClientNameId;
            visit_purposeid= selectPurposeId;

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Adding Task...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected JSONObject doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("visit_address", visit_address));
            params.add(new BasicNameValuePair("visit_date", visit_date));
            params.add(new BasicNameValuePair("visit_time", visit_time));
            params.add(new BasicNameValuePair("visit_uid", visit_assignedby));
            params.add(new BasicNameValuePair("visit_assignedby", "17"));
            params.add(new BasicNameValuePair("visit_leadid", visit_leadid));
            params.add(new BasicNameValuePair("visit_purposeid", visit_purposeid));
            params.add(new BasicNameValuePair("sp_add", "sp_add"));

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
            }catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(JSONObject response) {
            try {
                pDialog.dismiss();
                if (!(response == null)) {
                    makeText(getActivity(),"Meeting Task Created Successfully", Toast.LENGTH_SHORT).show();
                     clearAll();
                }
                else {
                    makeText(getActivity(), "Already Inserted ", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (Exception e) {
            }
        }
    }

    private void clearAll(){
        addressAddVisitTaskSp_et.setText("");
        dateAddVisitTaskSp_tv.setText("");
        timeAddVisitTaskSp_tv.setText("");
        clientAddVisitTaskSp_sp.setSelection(0);
        purposeAddVisitTaskSp_sp.setSelection(0);
    }



}
