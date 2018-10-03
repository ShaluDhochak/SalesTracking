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
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.sales.tracking.salestracking.Adapter.SalesPersonAdapter;
import com.sales.tracking.salestracking.Adapter.ViewTotalExpensesAdapter;
import com.sales.tracking.salestracking.Bean.ExpencesSpBean;
import com.sales.tracking.salestracking.Bean.LeadSpBean;
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

import static android.widget.Toast.makeText;


public class ViewSalesPersonDetailsFragment extends Fragment {

    @BindView(R.id.viewSalesPerson_rv)
    RecyclerView viewSalesPerson_rv;

    @BindView(R.id.viewSalesPersonHeader_rl)
    RelativeLayout viewSalesPersonHeader_rl;

    @BindView(R.id.viewSalesPersonDetails_cv)
    CardView viewSalesPersonDetails_cv;

    @BindView(R.id.updatedOnSalesPersonDetail_tv)
    TextView updatedOnSalesPersonDetail_tv;

    @BindView(R.id.createdOnSalesPersonDetail_tv)
    TextView createdOnSalesPersonDetail_tv;

    @BindView(R.id.dojSalesPersonDetail_tv)
    TextView dojSalesPersonDetail_tv;

    @BindView(R.id.statusSalesPersonDetail_tv)
    TextView statusSalesPersonDetail_tv;
    @BindView(R.id.mobileSalesPersonDetail_tv)
    TextView mobileSalesPersonDetail_tv;

    @BindView(R.id.emailSalesPersonDetails_tv)
    TextView emailSalesPersonDetails_tv;
    @BindView(R.id.nameSalesPersonDetails_tv)
    TextView nameSalesPersonDetails_tv;

    @BindView(R.id.minusSalesPersonDetail_iv)
    ImageView minusSalesPersonDetail_iv;

    //Edit

    @BindView(R.id.okEditSalesPersonDetail_tv)
    Button okEditSalesPersonDetail_tv;

    @BindView(R.id.mobileEditSalesPersonDetail_et)
    EditText mobileEditSalesPersonDetail_et ;

    @BindView(R.id.emailEditSalesPersonDetails_et)
    EditText emailEditSalesPersonDetails_et;

    @BindView(R.id.minusEditSalesPersonDetail_iv)
    ImageView minusEditSalesPersonDetail_iv;

    @BindView(R.id.nameEditSalesPersonDetails_et)
    EditText nameEditSalesPersonDetails_et;

    @BindView(R.id.statusEditSalesPersonDetail_sp)
    Spinner statusEditSalesPersonDetail_sp;

    @BindView(R.id.dojEditSalesPersonDetail_tv)
    TextView dojEditSalesPersonDetail_tv;

    @BindView(R.id.editSalesPersonDetails_cv)
    CardView editSalesPersonDetails_cv;

    View view;
    SharedPreferences sharedPref;
    String userIdPref, userTypePref, client_id, deleteExpenses_id;

    String selectStatus, selectStatusId;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    JSONParser jsonParser = new JSONParser();
    ProgressDialog pDialog;
    DatePickerDialog datePickerDialog;

    SalesPersonAdapter salesPersonAdapter;

    int selectedStatus;
    String statusType_id;

    ArrayList<ManagerUserBean.Manager_Users> userlist = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_view_sales_person_details, container, false);
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

        getSalesPersonRecyclerView();

     //   selectStatus();

        viewSalesPersonDetails_cv.setVisibility(View.GONE);
        editSalesPersonDetails_cv.setVisibility(View.GONE);
    }

    public void getSalesPersonRecyclerView(){
        try {
            if (Connectivity.isConnected(getActivity())) {

                String Url = ApiLink.ROOT_URL + ApiLink.MANAGER_SALES_PERSON;
                Map<String, String> map = new HashMap<>();
                map.put("user_reporting_to", userIdPref);
                map.put("select_sp", "");

                GSONRequest<ManagerUserBean> dashboardGsonRequest = new GSONRequest<ManagerUserBean>(
                        Request.Method.POST,
                        Url,
                        ManagerUserBean.class, map,
                        new com.android.volley.Response.Listener<ManagerUserBean>() {
                            @Override
                            public void onResponse(ManagerUserBean response) {
                                try {
                                    if (response.getManager_users().size() > 0) {
                                        userlist.clear();
                                        userlist.addAll(response.getManager_users());

                                        salesPersonAdapter = new SalesPersonAdapter(getActivity(), response.getManager_users(), ViewSalesPersonDetailsFragment.this);
                                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                        viewSalesPerson_rv.setLayoutManager(mLayoutManager);
                                        viewSalesPerson_rv.setItemAnimator(new DefaultItemAnimator());
                                        viewSalesPerson_rv.setAdapter(salesPersonAdapter);
                                        viewSalesPerson_rv.setNestedScrollingEnabled(false);
                                    }
                                } catch (Exception e) {
                                    // Toast.makeText(getActivity(), "Something went wrong..", Toast.LENGTH_SHORT).show();
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

    @OnClick(R.id.minusSalesPersonDetail_iv)
    public void hideSalesPDetails(){
        viewSalesPersonHeader_rl.setVisibility(View.VISIBLE);
        viewSalesPersonDetails_cv.setVisibility(View.GONE);
        editSalesPersonDetails_cv.setVisibility(View.GONE);
        if (userTypePref.equals("Sales Manager")){
            getSalesPersonRecyclerView();
        }

    }

    public void getClientData(ManagerUserBean.Manager_Users bean){
        viewSalesPersonHeader_rl.setVisibility(View.GONE);
        viewSalesPersonDetails_cv.setVisibility(View.VISIBLE);
        editSalesPersonDetails_cv.setVisibility(View.GONE);

        updatedOnSalesPersonDetail_tv.setText(bean.getUpdate_dt());
        createdOnSalesPersonDetail_tv.setText(bean.getCreated_dt());
        dojSalesPersonDetail_tv.setText(bean.getUser_doj());
        statusSalesPersonDetail_tv.setText(bean.getUser_status());
        mobileSalesPersonDetail_tv.setText(bean.getUser_mobile());
        emailSalesPersonDetails_tv.setText(bean.getUser_email());
        nameSalesPersonDetails_tv.setText(bean.getUser_name());

    }

    public void getEditSalesPersonData(ManagerUserBean.Manager_Users bean){
        viewSalesPersonHeader_rl.setVisibility(View.GONE);
        viewSalesPersonDetails_cv.setVisibility(View.GONE);
        editSalesPersonDetails_cv.setVisibility(View.VISIBLE);

        dojEditSalesPersonDetail_tv.setText(bean.getUser_doj());
        nameEditSalesPersonDetails_et.setText(bean.getUser_name());
        emailEditSalesPersonDetails_et.setText(bean.getUser_email());
        mobileEditSalesPersonDetail_et.setText(bean.getUser_mobile());

        statusType_id = bean.getUser_status().toString();

        client_id = bean.getUser_id().toString();

        selectStatus();
    }

    @OnClick(R.id.minusEditSalesPersonDetail_iv)
    public void hideSalesPersonDetails(){
        viewSalesPersonHeader_rl.setVisibility(View.VISIBLE);
        viewSalesPersonDetails_cv.setVisibility(View.GONE);
        editSalesPersonDetails_cv.setVisibility(View.GONE);
        if (userTypePref.equals("Sales Manager")){
            getSalesPersonRecyclerView();
        }
    }

    public void deleteClientData(ManagerUserBean.Manager_Users bean){
        viewSalesPersonHeader_rl.setVisibility(View.VISIBLE);
        viewSalesPersonDetails_cv.setVisibility(View.GONE);

        client_id = bean.getUser_id().toString();
        new deleteClient().execute();
    }

    public class deleteClient extends AsyncTask<String, JSONObject, JSONObject> {
        String user_id;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            user_id = client_id;

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Deleting Client...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user_id", user_id));
            params.add(new BasicNameValuePair("delete", "delete"));

            String url_add_task = ApiLink.ROOT_URL + ApiLink.MANAGER_SALES_PERSON;
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
                    getSalesPersonRecyclerView();
                }
                else {
                    makeText(getActivity(), "Not Deleted", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (Exception e) {
            }
        }
    }


    public class editSalesPerson extends AsyncTask<String, JSONObject, JSONObject> {
        String user_id, user_doj, user_status, user_name, user_email, user_mobile;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            user_id = client_id;

            user_doj = dojEditSalesPersonDetail_tv.getText().toString();
            user_status = selectStatusId;
            user_name = nameEditSalesPersonDetails_et.getText().toString();
            user_email = emailEditSalesPersonDetails_et.getText().toString();
            user_mobile = mobileEditSalesPersonDetail_et.getText().toString();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Editing Sales Person...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user_id", user_id));
            params.add(new BasicNameValuePair("edit", "edit"));
            params.add(new BasicNameValuePair("filter[user_name]", user_name));
            params.add(new BasicNameValuePair("filter[user_email]", user_email));
            params.add(new BasicNameValuePair("filter[user_mobile]", user_mobile));
            params.add(new BasicNameValuePair("filter[user_status]", user_status));
            params.add(new BasicNameValuePair("filter[user_doj]", user_doj));

            String url_add_task = ApiLink.ROOT_URL + ApiLink.MANAGER_SALES_PERSON;
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
                    getSalesPersonRecyclerView();
                }
                else {
                    makeText(getActivity(), "Not Updated!", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (Exception e) {
            }
        }
    }

    private void selectStatus() {

        List<String> statusSpinner = new ArrayList<String>();
        statusSpinner.add("Status");
        statusSpinner.add("Active");
        statusSpinner.add("Inactive");

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_textview, statusSpinner);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        statusEditSalesPersonDetail_sp.setAdapter(statusAdapter);
        if (statusType_id.equals("Active")){
            statusEditSalesPersonDetail_sp.setSelection(1);
        }else if (statusType_id.equals("Inactive")){
            statusEditSalesPersonDetail_sp.setSelection(2);
        }

}


    @OnItemSelected(R.id.statusEditSalesPersonDetail_sp)
    public void statusSelected(Spinner spinner, int position){
        selectStatus = spinner.getSelectedItem().toString();

        if (selectStatus.equals("Status")) {
            selectStatusId = "";
        } else {
            selectStatusId= selectStatus;
        }
    }

    @OnClick(R.id.dojEditSalesPersonDetail_tv)
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
                        dojEditSalesPersonDetail_tv.setText(year + "-" + (monthOfYear + 1)+ "-" + dayOfMonth);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    @OnClick(R.id.okEditSalesPersonDetail_tv)
    public void submitEditSalesPersonBtn() {
        if (nameEditSalesPersonDetails_et.getText().toString().length() > 0) {
            if (emailEditSalesPersonDetails_et.getText().toString().length() > 0) {
                if (isEmailValid(emailEditSalesPersonDetails_et.getText().toString().trim())) {
                    if (mobileEditSalesPersonDetail_et.getText().toString().length() > 0 && mobileEditSalesPersonDetail_et.getText().toString().length() == 10) {
                        if (!(selectStatus.equals("Status"))) {
                            if (dojEditSalesPersonDetail_tv.getText().toString().length() > 0) {
                                new editSalesPerson().execute();

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

}
