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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.sales.tracking.salestracking.Activity.NavigationDrawerActivity;
import com.sales.tracking.salestracking.Adapter.SalesPersonAdapter;
import com.sales.tracking.salestracking.Bean.ManagerUserBean;
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
import static com.sales.tracking.salestracking.Utility.StringUtils.isEmailValid;


public class AddSalesPersonManagerFragment extends Fragment {

    @BindView(R.id.nameAddSalesPersonDetails_et)
    EditText nameAddSalesPersonDetails_et;

    @BindView(R.id.emailAddSalesPersonDetails_et)
    EditText emailAddSalesPersonDetails_et;

    @BindView(R.id.mobileAddSalesPersonDetails_et)
    EditText mobileAddSalesPersonDetails_et;

    @BindView(R.id.statusAddSalesPersonDetail_sp)
    Spinner statusAddSalesPersonDetail_sp;

    @BindView(R.id.dojAddSalesPersonDetail_tv)
    TextView dojAddSalesPersonDetail_tv;

    @BindView(R.id.passwordAddAddSalesPersonDetails_et)
    EditText passwordAddAddSalesPersonDetails_et;

    @BindView(R.id.addSalespersonMessage_tv)
    TextView addSalespersonMessage_tv;

    @BindView(R.id.addSalesPersonMessage_cv)
    CardView addSalesPersonMessage_cv;

    @BindView(R.id.addSalesPersonDetails_cv)
    CardView addSalesPersonDetails_cv;

    @BindView(R.id.submitAddSalesPersonManager_btn)
    Button submitAddSalesPersonManager_btn;

    String package_users_count = "", users_count = "" ;

    DatePickerDialog datePickerDialog;
    ProgressDialog pDialog;

    SharedPreferences sharedPref;
    String userIdPref, userTypePref, user_comidPref;
    String selectStatus, selectStatusId;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    JSONParser jsonParser = new JSONParser();

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_sales_person_manager, container, false);
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

        addSalesPersonMessage_cv.setVisibility(GONE);
        addSalesPersonDetails_cv.setVisibility(View.GONE);

        getAddSalesPersonPackageCount();
        getAddSalesPersonUserCount();

    }

    @OnClick(R.id.dojAddSalesPersonDetail_tv)
    public void selectDOjDate(){
        final Calendar calenderObj = Calendar.getInstance();
        int mYear = calenderObj.get(Calendar.YEAR);
        int mMonth = calenderObj.get(Calendar.MONTH);
        int mDay = calenderObj.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        dojAddSalesPersonDetail_tv.setText(year + "-" + (monthOfYear + 1)+ "-" + dayOfMonth);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    @OnClick(R.id.submitAddSalesPersonManager_btn)
    public void submitAddMeetingTaskBtn() {
        if (nameAddSalesPersonDetails_et.getText().toString().length() > 0) {
            if (emailAddSalesPersonDetails_et.getText().toString().length() > 0) {
                if (isEmailValid(emailAddSalesPersonDetails_et.getText().toString().trim())) {
                    if (mobileAddSalesPersonDetails_et.getText().toString().length() > 0 && mobileAddSalesPersonDetails_et.getText().toString().length() == 10) {
                        if (!(selectStatus.equals("Status"))) {
                            if (dojAddSalesPersonDetail_tv.getText().toString().length() > 0) {
                                if (passwordAddAddSalesPersonDetails_et.getText().toString().length() > 0) {

                                    new addSalesPersonManager().execute();

                                } else {
                                    Toast.makeText(getActivity(), "Please Enter Password", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Please Enter DOJ ", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Please Select Status", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Please Enter 10 Digits Mobile No", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Please Enter valid Email Id", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Please enter EmailId", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "Please enter Name", Toast.LENGTH_SHORT).show();
        }
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void selectStatus() {

        List<String> statusSpinner = new ArrayList<String>();
        statusSpinner.add("Status");
        statusSpinner.add("Active");
        statusSpinner.add("Inactive");

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_textview, statusSpinner);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        statusAddSalesPersonDetail_sp.setAdapter(statusAdapter);
    }

    @OnItemSelected(R.id.statusAddSalesPersonDetail_sp)
    public void statusSelected(Spinner spinner, int position){
        selectStatus = spinner.getSelectedItem().toString();

        if (selectStatus.equals("Status")) {
           selectStatusId = "";
        } else {
           selectStatusId= selectStatus;
        }
    }

    public class addSalesPersonManager extends AsyncTask<String, JSONObject, JSONObject> {
        String user_name, user_email, user_mobile, user_comid, user_pass, user_createdby, user_createdon, user_type, user_status;
        String user_reporting_to, user_doj, user_desiid;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            user_name = nameAddSalesPersonDetails_et.getText().toString();
            user_email = emailAddSalesPersonDetails_et.getText().toString();
            user_mobile = mobileAddSalesPersonDetails_et.getText().toString();
            user_comid = user_comidPref;
            user_pass = passwordAddAddSalesPersonDetails_et.getText().toString();
            user_createdby = userIdPref;
            user_createdon = dojAddSalesPersonDetail_tv.getText().toString();
            user_type = "Sales Executive";
            user_status = selectStatusId;
            user_reporting_to = userIdPref;
            user_doj = dojAddSalesPersonDetail_tv.getText().toString();
            user_desiid = "3";


            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Adding Sales Person...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user_name", user_name));
            params.add(new BasicNameValuePair("user_email", user_email));
            params.add(new BasicNameValuePair("user_mobile", user_mobile));
            params.add(new BasicNameValuePair("add_bymanager", ""));
            params.add(new BasicNameValuePair("user_comid", user_comid));
            params.add(new BasicNameValuePair("user_pass", user_pass));
            params.add(new BasicNameValuePair("user_createdby", user_createdby));
            params.add(new BasicNameValuePair("user_createdon", user_createdon));
            params.add(new BasicNameValuePair("user_type", user_type));
            params.add(new BasicNameValuePair("user_status", user_status));
            params.add(new BasicNameValuePair("user_reporting_to", user_reporting_to));
            params.add(new BasicNameValuePair("user_doj", user_doj));
            params.add(new BasicNameValuePair("user_desiid", "3"));

            String url_add_task = ApiLink.ROOT_URL + ApiLink.USER_LIST;

            //manager_client.php
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
                    ((NavigationDrawerActivity)getActivity()).viewSalesPersonManagerFragment();
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
        nameAddSalesPersonDetails_et.setText("");
        emailAddSalesPersonDetails_et.setText("");
        mobileAddSalesPersonDetails_et.setText("");
        passwordAddAddSalesPersonDetails_et.setText("");
        statusAddSalesPersonDetail_sp.setSelection(0);
        dojAddSalesPersonDetail_tv.setText("");

    }

    public void getAddSalesPersonPackageCount(){

        if (Connectivity.isConnected(getActivity())) {

            String Url = ApiLink.ROOT_URL + ApiLink.USER_LIST;
            Map<String, String> map = new HashMap<>();
            map.put("user_comid", user_comidPref);
            map.put("package_users_count", "");

            GSONRequest<ManagerUserBean> dashboardGsonRequest = new GSONRequest<ManagerUserBean>(
                    Request.Method.POST,
                    Url,
                    ManagerUserBean.class, map,
                    new com.android.volley.Response.Listener<ManagerUserBean>() {
                        @Override
                        public void onResponse(ManagerUserBean response) {
                            try {
                                if (response.getPackage_users_count().size() > 0) {
                                    package_users_count = response.getPackage_users_count().get(0).getPack_noofmemb();
                                    getDefaultValues();
                                }
                            } catch (Exception e) {
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

    public void getAddSalesPersonUserCount(){

        if (Connectivity.isConnected(getActivity())) {

            String Url = ApiLink.ROOT_URL + ApiLink.USER_LIST;
            Map<String, String> map = new HashMap<>();
            map.put("user_comid", user_comidPref);
            map.put("users_count", "");

            GSONRequest<ManagerUserBean> dashboardGsonRequest = new GSONRequest<ManagerUserBean>(
                    Request.Method.POST,
                    Url,
                    ManagerUserBean.class, map,
                    new com.android.volley.Response.Listener<ManagerUserBean>() {
                        @Override
                        public void onResponse(ManagerUserBean response) {
                            try {
                                if (response.getUsers_count().size() > 0) {
                                    users_count = response.getUsers_count().get(0).getTot_users().toString();
                                    getDefaultValues();
                                }
                            } catch (Exception e) {
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

    private void getDefaultValues(){
        try {
            if (Integer.parseInt(package_users_count) > Integer.parseInt(users_count)) {
                //    package_users_count.compareTo(users_count) > 0) {

                //if (package_users_count.compareTo(users_count) > 0) {
                selectStatus();
                addSalesPersonDetails_cv.setVisibility(View.VISIBLE);
                addSalesPersonMessage_cv.setVisibility(View.GONE);
            } else {
                selectStatus();
                addSalesPersonMessage_cv.setVisibility(View.VISIBLE);
                addSalesPersonDetails_cv.setVisibility(View.GONE);
            }
        }catch(Exception e) {
        //    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

}
