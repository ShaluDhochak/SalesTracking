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
import com.sales.tracking.salestracking.Adapter.AssignToClientAdapter;
import com.sales.tracking.salestracking.Adapter.LeadSpAdapter;
import com.sales.tracking.salestracking.Adapter.ManagerClientAdapter;
import com.sales.tracking.salestracking.Adapter.ManagerClientManagerHeadAdapter;
import com.sales.tracking.salestracking.Bean.LeadSpBean;
import com.sales.tracking.salestracking.Bean.ManagerBean;
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

public class ViewClientManagerFragment extends Fragment {
    View view;

    @BindView(R.id.titleViewLeadTask_tv)
    TextView titleViewLeadTask_tv;

    @BindView(R.id.leadTask_rv)
    RecyclerView leadTask_rv;

    @BindView(R.id.leadTaskHeader_rl)
    RelativeLayout leadTaskHeader_rl;

    @BindView(R.id.viewLeadTaskDetails_cv)
    CardView viewLeadTaskDetails_cv;

    @BindView(R.id.minusLeadTypeDetail_iv)
    ImageView minusLeadTypeDetail_iv;

    @BindView(R.id.clientCompanyNameLeadTask_tv)
    TextView clientCompanyNameLeadTask_tv;

    @BindView(R.id.leadTypeViewLead_tv)
    TextView leadTypeViewLead_tv;

    @BindView(R.id.contactPersonLeadView_tv)
    TextView contactPersonLeadView_tv;

    @BindView(R.id.emailLeadViewTask_tv)
    TextView emailLeadViewTask_tv;

    @BindView(R.id.mobileLeadViewTask_tv)
    TextView mobileLeadViewTask_tv;

    @BindView(R.id.websiteLeadViewTask_tv)
    TextView websiteLeadViewTask_tv;

    @BindView(R.id.addressLeadViewTask_tv)
    TextView addressLeadViewTask_tv;

    @BindView(R.id.statusLeadViewTask_tv)
    TextView statusLeadViewTask_tv;

    @BindView(R.id.createdByLeadViewTask_rl)
    RelativeLayout createdByLeadViewTask_rl;

    @BindView(R.id.separatorBelowCreatedByLeadView)
    View separatorBelowCreatedByLeadView;

    @BindView(R.id.createdByLeadViewTask_tv)
    TextView createdByLeadViewTask_tv;

    //Edit
    @BindView(R.id.submitEditLeadSp_btn)
    Button submitEditLeadSp_btn;

    @BindView(R.id.addressEditLeadSp_et)
    EditText addressEditLeadSp_et;

    @BindView(R.id.websiteEditLeadSp_et)
    EditText websiteEditLeadSp_et;

    @BindView(R.id.mobileEditLeadSp_et)
    EditText mobileEditLeadSp_et;

    @BindView(R.id.emailEditLeadSp_et)
    EditText emailEditLeadSp_et;

    @BindView(R.id.contactPersonEditLeadSp_et)
    EditText contactPersonEditLeadSp_et;

    @BindView(R.id.clientCompanyNameEditLeadSp_et)
    EditText clientCompanyNameEditLeadSp_et;

    @BindView(R.id.lTypeEditLeadSp_sp)
    Spinner lTypeEditLeadSp_sp;

    @BindView(R.id.editLeadSpDetails_cv)
    CardView editLeadSpDetails_cv;

    @BindView(R.id.minusEditLeadTypeDetail_iv)
    ImageView minusEditLeadTypeDetail_iv;

    @BindView(R.id.outstandingBalLeadViewTask_rl)
    RelativeLayout outstandingBalLeadViewTask_rl;

    @BindView(R.id.separatorBelowStatusLeadView)
    View separatorBelowStatusLeadView;

    @BindView(R.id.outstandingBalLeadViewTask_tv)
    TextView outstandingBalLeadViewTask_tv;


    //Edit outstand
    @BindView(R.id.outstandBalEditLeadSp_et)
    EditText outstandBalEditLeadSp_et;

    @BindView(R.id.assignToEditLeadSp_sp)
    Spinner assignToEditLeadSp_sp;


    SharedPreferences sharedPref;
    String userIdPref, userTypePref, user_comidPref, lead_iid;

    ManagerClientAdapter managerClientAdapter;
    ArrayList<ManagerBean.clients> clientList = new ArrayList<>();

    ManagerClientManagerHeadAdapter managerClientManagerHeadAdapter;

    String selectedLeadType, selectedLeadTypeId, leadType_id, defaultLeadtype, selectAssignTo, selectAssignToId, assignType_id;

    ArrayList<String> leadTypeAddLeadSp;
    ArrayList<String> assignToUser = new ArrayList<>();


    AssignToClientAdapter myAdapter;

    ArrayList<TaskMeetingBean.Users_DD> listVOs = new ArrayList<>();


    Map<String, String> leadTypeMap = new HashMap<>();
    Map<String, String> assignToUserMap = new HashMap<>();

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    JSONParser jsonParser = new JSONParser();
    ProgressDialog pDialog;

    int selectedLead, selectAssign;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_lead_sp, container, false);
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

        editLeadSpDetails_cv.setVisibility(View.GONE);
        viewLeadTaskDetails_cv.setVisibility(View.GONE);

        titleViewLeadTask_tv.setText("View Client");

        if (userTypePref.equals("Sales Manager")) {
            getManagerClientViewRecyclerView();
        } else if (userTypePref.equals("Manager Head")) {
            getManagerHeadClientViewRecyclerView();
        }

    }

    @OnClick(R.id.minusLeadTypeDetail_iv)
    public void hideLeadDetails() {
        viewLeadTaskDetails_cv.setVisibility(View.GONE);
        editLeadSpDetails_cv.setVisibility(View.GONE);
        leadTaskHeader_rl.setVisibility(View.VISIBLE);
        if (userTypePref.equals("Sales Manager")) {
            getManagerClientViewRecyclerView();
        } else if (userTypePref.equals("Manager Head")) {
            getManagerHeadClientViewRecyclerView();
        }
    }

    public void deleteClientData(ManagerBean.clients bean) {
        viewLeadTaskDetails_cv.setVisibility(View.GONE);
        leadTaskHeader_rl.setVisibility(View.VISIBLE);
        editLeadSpDetails_cv.setVisibility(View.GONE);

        lead_iid = bean.getLead_id().toString();
        new deleteManagerClientSp().execute();
    }

    public void getClientData(ManagerBean.clients bean) {
        viewLeadTaskDetails_cv.setVisibility(View.VISIBLE);
        leadTaskHeader_rl.setVisibility(View.GONE);
        editLeadSpDetails_cv.setVisibility(View.GONE);

        outstandingBalLeadViewTask_rl.setVisibility(View.VISIBLE);
        separatorBelowStatusLeadView.setVisibility(View.VISIBLE);

        titleViewLeadTask_tv.setText("View Client");

        clientCompanyNameLeadTask_tv.setText(bean.getLead_company());
        leadTypeViewLead_tv.setText(bean.getLeadtype_name());
        contactPersonLeadView_tv.setText(bean.getLead_name());
        emailLeadViewTask_tv.setText(bean.getLead_email());
        mobileLeadViewTask_tv.setText(bean.getLead_contact());
        websiteLeadViewTask_tv.setText(bean.getLead_website());
        addressLeadViewTask_tv.setText(bean.getLead_address());
        createdByLeadViewTask_tv.setText(bean.getUser_name());
        statusLeadViewTask_tv.setText(bean.getLead_status());

        outstandingBalLeadViewTask_tv.setText(bean.getOutstanding_bal());
    }

    public void getEditClientClientData(ManagerBean.clients bean) {
        viewLeadTaskDetails_cv.setVisibility(View.GONE);
        leadTaskHeader_rl.setVisibility(View.GONE);
        editLeadSpDetails_cv.setVisibility(View.VISIBLE);

        titleViewLeadTask_tv.setText("Edit Client");

        lead_iid = bean.getLead_id().toString();

        clientCompanyNameEditLeadSp_et.setText(bean.getLead_company());
        // lTypeEditLeadSp_sp.setText(bean.getLeadtype_name());
        contactPersonEditLeadSp_et.setText(bean.getLead_name());
        emailEditLeadSp_et.setText(bean.getLead_email());
        mobileEditLeadSp_et.setText(bean.getLead_contact());
        websiteEditLeadSp_et.setText(bean.getLead_website());
        addressEditLeadSp_et.setText(bean.getLead_address());
        outstandBalEditLeadSp_et.setText(bean.getOutstanding_bal());
        leadType_id = bean.getLeadtype_name().toString();

        assignType_id = bean.getLead_assignedto().toString();
        selectleadType();
        selectAssignTo();

    }

    @OnClick(R.id.minusEditLeadTypeDetail_iv)
    public void hideEdit() {
        viewLeadTaskDetails_cv.setVisibility(View.GONE);
        editLeadSpDetails_cv.setVisibility(View.GONE);
        leadTaskHeader_rl.setVisibility(View.VISIBLE);
        if (userTypePref.equals("Sales Manager")) {
            getManagerClientViewRecyclerView();
        }
    }

    private void selectleadType() {
        try {
            if (Connectivity.isConnected(getActivity())) {
                String Url = ApiLink.ROOT_URL + ApiLink.LEAD_VIEW_SALESPERSON;
                Map<String, String> map = new HashMap<>();
                map.put("leadtype_dropdown", "");
                map.put("lead_comid", user_comidPref);

                final GSONRequest<LeadSpBean> leadTypeGsonRequest = new GSONRequest<LeadSpBean>(
                        Request.Method.POST,
                        Url,
                        LeadSpBean.class, map,
                        new com.android.volley.Response.Listener<LeadSpBean>() {
                            @Override
                            public void onResponse(LeadSpBean response) {
                                leadTypeAddLeadSp.clear();
                                leadTypeAddLeadSp.add("Lead Type");

                                for (int i = 0; i < response.getLeadtype_dropdown().size(); i++) {
                                    leadTypeAddLeadSp.add(response.getLeadtype_dropdown().get(i).getLeadtype_name());
                                    leadTypeMap.put(response.getLeadtype_dropdown().get(i).getLeadtype_id(), response.getLeadtype_dropdown().get(i).getLeadtype_name());
                                    String myString = leadType_id; //the value you want the position for
                                    if (myString.equals(response.getLeadtype_dropdown().get(i).getLeadtype_name())) {
                                        selectedLead = i + 1;
                                        lTypeEditLeadSp_sp.setSelection(selectedLead);
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
                leadTypeGsonRequest.setShouldCache(false);
                Utilities.getRequestQueue(getActivity()).add(leadTypeGsonRequest);
            }
            leadTypeAddLeadSp = new ArrayList<String>();
            leadTypeAddLeadSp.clear();
            leadTypeAddLeadSp.add("Lead Type");

            ArrayAdapter<String> leadTypeDataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_textview, leadTypeAddLeadSp);
            leadTypeDataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
            lTypeEditLeadSp_sp.setAdapter(leadTypeDataAdapter);

        } catch (Exception e) {
        }
    }

    @OnItemSelected(R.id.lTypeEditLeadSp_sp)
    public void visitTaskAddSelected(Spinner spinner, int position) {
        selectedLeadType = spinner.getSelectedItem().toString();

        for (Map.Entry<String, String> e : leadTypeMap.entrySet()) {
            Object key = e.getKey();
            Object value = e.getValue();
            if (value.equals(selectedLeadType)) {
                selectedLeadTypeId = (String) key;
                leadType_id = (String) key;
            }
        }
    }

    private void selectAssignTo() {
        try {
            if (Connectivity.isConnected(getActivity())) {
                String Url = ApiLink.ROOT_URL + ApiLink.Dashboard_SalesPerson;
                Map<String, String> map = new HashMap<>();
                map.put("users", "");
                map.put("user_comid", user_comidPref);

                final GSONRequest<TaskMeetingBean> locationSpinnerGsonRequest = new GSONRequest<TaskMeetingBean>(
                        Request.Method.POST,
                        Url,
                        TaskMeetingBean.class, map,
                        new com.android.volley.Response.Listener<TaskMeetingBean>() {
                            @Override
                            public void onResponse(TaskMeetingBean response) {
                                assignToUser.clear();
                                assignToUser.add("Assign To");
                                listVOs.clear();
                                for (int i = 0; i < response.getUsers_dd().size(); i++) {

                                    // for (int i = 0; i < response.getUsers_dd().length; i++) {
                                    TaskMeetingBean.Users_DD stateVO = new TaskMeetingBean.Users_DD();
                                    stateVO.setUser_name(response.getUsers_dd().get(i).getUser_name());
                                    stateVO.setUser_id(response.getUsers_dd().get(i).getUser_id());
                                    stateVO.setSelected(false);
                                    listVOs.add(stateVO);
                                    // }


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
            myAdapter = new AssignToClientAdapter(getActivity(), 0, listVOs);
            assignToEditLeadSp_sp.setAdapter(myAdapter);


         /*   assignToUser = new ArrayList<String>();
            assignToUser.clear();
            assignToUser.add("Assign To");
            ArrayAdapter<String> quotationLocationDataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_textview, assignToUser);
            quotationLocationDataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
            assignToEditLeadSp_sp.setAdapter(quotationLocationDataAdapter);

            */
        } catch (Exception e) {
        }
    }

    @OnItemSelected(R.id.assignToEditLeadSp_sp)
    public void assignToAddSelected(Spinner spinner, int position) {
        selectAssignTo = spinner.getSelectedItem().toString();
        for (Map.Entry<String, String> e : assignToUserMap.entrySet()) {
            Object key = e.getKey();
            Object value = e.getValue();
            if (value.equals(selectAssignTo)) {
                selectAssignToId = (String) key;
                //   assignType_id= (String) key;
            }
        }
    }

    private boolean getIsSelectedAssignedTo(){
        for (TaskMeetingBean.Users_DD users_dd : myAdapter.getListState()){
            if (users_dd.isSelected()){
                return true;
            }
        }
        return false;
    }


    @OnClick(R.id.submitEditLeadSp_btn)
    public void submitEditLead() {



        if (!selectedLeadType.equals("Lead Type")) {
            if (clientCompanyNameEditLeadSp_et.getText().toString().length() > 0) {
                if (contactPersonEditLeadSp_et.getText().toString().length() > 0) {
                    if (emailEditLeadSp_et.getText().toString().length() > 0) {
                        if (isEmailValid(emailEditLeadSp_et.getText().toString().trim())) {
                            if (mobileEditLeadSp_et.getText().toString().length() > 0 && mobileEditLeadSp_et.getText().toString().length() == 10) {
                                if (websiteEditLeadSp_et.getText().toString().length() > 0) {
                                    if (addressEditLeadSp_et.getText().toString().length() > 0) {
                                        if (outstandBalEditLeadSp_et.getText().toString().length() > 0) {
                                            if (getIsSelectedAssignedTo()) {
                                                new editManagerClientSp().execute();
                                            }else{
                                                Toast.makeText(getActivity(), "Please Select Assign To", Toast.LENGTH_SHORT).show();

                                            }
                                        } else {
                                            Toast.makeText(getActivity(), "Please Enter Outstanding Bal", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(getActivity(), "Please Enter Address", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Please Enter Website ", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Please Enter 10 digit Mobile No", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "InValid Email Address.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Please Enter Email Id", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Please Enter Contact Person", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Please enter Client Company Name", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "Please select Lead Type", Toast.LENGTH_SHORT).show();
        }
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public class deleteManagerClientSp extends AsyncTask<String, JSONObject, JSONObject> {
        String lead_id;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            lead_id = lead_iid;

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Deleting Lead...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("lead_id", lead_id));
            params.add(new BasicNameValuePair("delete", "delete"));

            String url_add_task = ApiLink.ROOT_URL + ApiLink.MANAGER_CLIENT;
            JSONObject json = jsonParser.makeHttpRequest(url_add_task, "POST", params);

            try {
                int success = json.getInt(TAG_SUCCESS);
                String message = json.getString(TAG_MESSAGE);
                if (success == 1 && message.equals("Lead Deleted Successfully")) {
                    return json;
                } else {
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
                    makeText(getActivity(), "Lead Deleted Successfully", Toast.LENGTH_SHORT).show();
                    getManagerClientViewRecyclerView();
                } else {
                    makeText(getActivity(), "Not Deleted", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (Exception e) {
            }
        }
    }

    public class editManagerClientSp extends AsyncTask<String, JSONObject, JSONObject> {
        String lead_address, lead_uid, lead_leadtypeid, lead_company, lead_name, lead_email, lead_contact, lead_website, lead_assignto;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            lead_address = addressEditLeadSp_et.getText().toString();
            lead_uid = userIdPref;
            lead_leadtypeid = selectedLeadTypeId;
            lead_company = clientCompanyNameEditLeadSp_et.getText().toString();
            lead_name = contactPersonEditLeadSp_et.getText().toString();
            lead_email = emailEditLeadSp_et.getText().toString();
            lead_contact = mobileEditLeadSp_et.getText().toString();
            lead_website = websiteEditLeadSp_et.getText().toString();
            lead_assignto = selectAssignToId;

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Editing Client...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            ArrayList<TaskMeetingBean.Users_DD> assignToArrayList = myAdapter.getListState();
            lead_assignto = "";
            for (TaskMeetingBean.Users_DD users_dd : assignToArrayList){
                if (users_dd.isSelected()) {
                    if (lead_assignto.equals("")) {
                        lead_assignto = users_dd.getUser_id();
                    } else {
                        lead_assignto = lead_assignto + "," + users_dd.getUser_id();
                    }
                }
            }

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("filter[lead_address]", lead_address));
         //   params.add(new BasicNameValuePair("filter[lead_uid]", userIdPref));
            params.add(new BasicNameValuePair("filter[lead_leadtypeid]", lead_leadtypeid));
            params.add(new BasicNameValuePair("edit", ""));
            params.add(new BasicNameValuePair("filter[lead_company]", lead_company));
            params.add(new BasicNameValuePair("filter[lead_name]", lead_name));
            params.add(new BasicNameValuePair("filter[lead_email]", lead_email));
            params.add(new BasicNameValuePair("filter[lead_contact]", lead_contact));
            params.add(new BasicNameValuePair("filter[lead_website]", lead_website));
            // params.add(new BasicNameValuePair("filter[lead_comid]", user_comidPref));
        //    params.add(new BasicNameValuePair("filter[lead_status]", "Done"));
            params.add(new BasicNameValuePair("lead_id", lead_iid));
            params.add(new BasicNameValuePair("assignedto", lead_assignto));


            String url_add_task = ApiLink.ROOT_URL + ApiLink.MANAGER_CLIENT;

            //manager_client.php
            JSONObject json = jsonParser.makeHttpRequest(url_add_task, "POST", params);

            try {
                int success = json.getInt(TAG_SUCCESS);
                String message = json.getString(TAG_MESSAGE);
                if (success == 1 && message.equals("Lead Updated Successfully")) {
                    return json;
                } else {
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
                    makeText(getActivity(), "Lead Updated Successfully", Toast.LENGTH_SHORT).show();
                    viewLeadTaskDetails_cv.setVisibility(View.GONE);
                    editLeadSpDetails_cv.setVisibility(View.GONE);
                    leadTaskHeader_rl.setVisibility(View.VISIBLE);
                    getManagerClientViewRecyclerView();
                } else {
                    makeText(getActivity(), "Not Updated", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (Exception e) {
            }
        }
    }

    private void getManagerClientViewRecyclerView() {
        if (Connectivity.isConnected(getActivity())) {

            String Url = ApiLink.ROOT_URL + ApiLink.MANAGER_CLIENT;
            Map<String, String> map = new HashMap<>();
            map.put("select", "");
            map.put("manager_id", userIdPref);

            GSONRequest<ManagerBean> dashboardGsonRequest = new GSONRequest<ManagerBean>(
                    Request.Method.POST,
                    Url,
                    ManagerBean.class, map,
                    new com.android.volley.Response.Listener<ManagerBean>() {
                        @Override
                        public void onResponse(ManagerBean response) {
                            try {
                                if (response.getClients().size() > 0) {
                                    clientList.clear();
                                    clientList.addAll(response.getClients());

                                    managerClientAdapter = new ManagerClientAdapter(getActivity(), response.getClients(), ViewClientManagerFragment.this);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                    leadTask_rv.setLayoutManager(mLayoutManager);
                                    leadTask_rv.setItemAnimator(new DefaultItemAnimator());
                                    leadTask_rv.setAdapter(managerClientAdapter);
                                    leadTask_rv.setNestedScrollingEnabled(false);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
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

    private void getManagerHeadClientViewRecyclerView() {
        try {
            if (Connectivity.isConnected(getActivity())) {

                String Url = ApiLink.ROOT_URL + ApiLink.MANAGER_CLIENT;
                Map<String, String> map = new HashMap<>();
                map.put("select", "");
                map.put("managerhead_id", userIdPref);

                GSONRequest<ManagerBean> dashboardGsonRequest = new GSONRequest<ManagerBean>(
                        Request.Method.POST,
                        Url,
                        ManagerBean.class, map,
                        new com.android.volley.Response.Listener<ManagerBean>() {
                            @Override
                            public void onResponse(ManagerBean response) {
                                try {
                                    if (response.getClients().size() > 0) {

                                        managerClientManagerHeadAdapter = new ManagerClientManagerHeadAdapter(getActivity(), response.getClients(), ViewClientManagerFragment.this);
                                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                        leadTask_rv.setLayoutManager(mLayoutManager);
                                        leadTask_rv.setItemAnimator(new DefaultItemAnimator());
                                        leadTask_rv.setAdapter(managerClientManagerHeadAdapter);
                                        leadTask_rv.setNestedScrollingEnabled(false);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
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
        } catch (Exception e) {

        }
    }


}
