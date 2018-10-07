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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.sales.tracking.salestracking.Adapter.ManagerClientAdapter;
import com.sales.tracking.salestracking.Adapter.ViewReassignedRequestNotificationCountAdapter;
import com.sales.tracking.salestracking.Adapter.ViewRequestNotificationDetailsAdapter;
import com.sales.tracking.salestracking.Adapter.ViewRequestNotificationDetailsManagerAdapter;
import com.sales.tracking.salestracking.Adapter.ViewRequestNotificationDetailsManagerHeadAdapter;
import com.sales.tracking.salestracking.Bean.ClientNotificationManagerBean;
import com.sales.tracking.salestracking.Bean.ExpencesSpBean;
import com.sales.tracking.salestracking.Bean.LeadSpBean;
import com.sales.tracking.salestracking.Bean.ManagerBean;
import com.sales.tracking.salestracking.Bean.ReassignRequestNotificationBean;
import com.sales.tracking.salestracking.Bean.RequestCountBean;
import com.sales.tracking.salestracking.Bean.VieqRequestReassignedBean;
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


public class ViewClientNotificationManagerFragment extends Fragment {

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
    TextView submitEditLeadSp_btn;

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

    @BindView(R.id.titleViewLeadCountTask_tv)
    TextView titleViewLeadCountTask_tv;

    SharedPreferences sharedPref;
    String userIdPref, userTypePref, user_comidPref, lead_iid;

    ViewRequestNotificationDetailsManagerAdapter viewRequestNotificationDetailsManagerAdapter;
    ViewRequestNotificationDetailsManagerHeadAdapter viewRequestNotificationDetailsManagerHeadAdapter;
    ArrayList<ManagerBean.clients> clientList = new ArrayList<>();

    String selectedLeadType, selectedLeadTypeId, leadType_id, defaultLeadtype;

    ArrayList<String> leadTypeAddLeadSp;

    Map<String, String> leadTypeMap = new HashMap<>();

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    JSONParser jsonParser = new JSONParser();
    ProgressDialog pDialog;

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

    private void initialiseUI(){
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        userIdPref = sharedPref.getString("user_id", "");
        userTypePref = sharedPref.getString("user_type", "");
        user_comidPref = sharedPref.getString("user_com_id", "");

        editLeadSpDetails_cv.setVisibility(View.GONE);
        viewLeadTaskDetails_cv.setVisibility(View.GONE);

        titleViewLeadCountTask_tv.setVisibility(View.VISIBLE);

        if (userTypePref.equals("Sales Manager")) {
            titleViewLeadTask_tv.setText("View Client");
            getManagerClientViewRecyclerView();
            getcount();
        }else if (userTypePref.equals("Manager Head")) {
            titleViewLeadTask_tv.setText("View Client");
            getManagerHeadClientViewRecyclerView();
            getMgrHeadcount();
        }
    }

    @OnClick(R.id.minusLeadTypeDetail_iv)
    public void hideLeadDetails(){
        viewLeadTaskDetails_cv.setVisibility(View.GONE);
        editLeadSpDetails_cv.setVisibility(View.GONE);
        leadTaskHeader_rl.setVisibility(View.VISIBLE);
        if (userTypePref.equals("Sales Manager")) {
            titleViewLeadTask_tv.setText("View Client");
            getManagerClientViewRecyclerView();
            getcount();
        }else if (userTypePref.equals("Manager Head")) {
            titleViewLeadTask_tv.setText("View Client");
            getManagerHeadClientViewRecyclerView();
            getMgrHeadcount();
        }
    }

    public void deleteClientNotifiData(ManagerBean.clients bean){
        viewLeadTaskDetails_cv.setVisibility(View.GONE);
        leadTaskHeader_rl.setVisibility(View.VISIBLE);
        editLeadSpDetails_cv.setVisibility(View.GONE);

        lead_iid = bean.getLead_id().toString();
        new deleteManagerClientSp().execute();
    }

    public void getClientNotifiData(ManagerBean.clients bean){
        viewLeadTaskDetails_cv.setVisibility(View.VISIBLE);
        leadTaskHeader_rl.setVisibility(View.GONE);
        editLeadSpDetails_cv.setVisibility(View.GONE);

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
    }

    public void getEditClientNotifiData(ManagerBean.clients bean){
        viewLeadTaskDetails_cv.setVisibility(View.GONE);
        leadTaskHeader_rl.setVisibility(View.GONE);
        editLeadSpDetails_cv.setVisibility(View.VISIBLE);

        titleViewLeadTask_tv.setText("Edit Client");

        lead_iid  =bean.getLead_id().toString();

        clientCompanyNameEditLeadSp_et.setText(bean.getLead_company());
        // lTypeEditLeadSp_sp.setText(bean.getLeadtype_name());
        contactPersonEditLeadSp_et.setText(bean.getLead_name());
        emailEditLeadSp_et.setText(bean.getLead_email());
        mobileEditLeadSp_et.setText(bean.getLead_contact());
        websiteEditLeadSp_et.setText(bean.getLead_website());
        addressEditLeadSp_et.setText(bean.getLead_address());
        leadType_id = bean.getLeadtype_name().toString();
        selectleadType();

    }

    @OnClick(R.id.minusEditLeadTypeDetail_iv)
    public void hideEdit(){
        viewLeadTaskDetails_cv.setVisibility(View.GONE);
        editLeadSpDetails_cv.setVisibility(View.GONE);
        leadTaskHeader_rl.setVisibility(View.VISIBLE);
        if (userTypePref.equals("Sales Manager")) {
            getManagerClientViewRecyclerView();
        }
    }

    private void selectleadType(){
        try{
            if (Connectivity.isConnected(getActivity())) {
                String Url = ApiLink.ROOT_URL + ApiLink.LEAD_VIEW_SALESPERSON;
                Map<String, String> map = new HashMap<>();
                map.put("leadtype_dropdown","" );
                map.put("lead_comid", user_comidPref);

                final GSONRequest<LeadSpBean> leadTypeGsonRequest = new GSONRequest<LeadSpBean>(
                        Request.Method.POST,
                        Url,
                        LeadSpBean.class,map,
                        new com.android.volley.Response.Listener<LeadSpBean>() {
                            @Override
                            public void onResponse(LeadSpBean response) {
                                leadTypeAddLeadSp.clear();
                                leadTypeAddLeadSp.add("Lead Type");
                                for(int i=0;i<response.getLeadtype_dropdown().size();i++)
                                {
                                    leadTypeAddLeadSp.add(response.getLeadtype_dropdown().get(i).getLeadtype_name());
                                    leadTypeMap.put(response.getLeadtype_dropdown().get(i).getLeadtype_id(), response.getLeadtype_dropdown().get(i).getLeadtype_name());
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

        }catch (Exception e){
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
                leadType_id= (String) key;
            }
        }
    }

    @OnClick(R.id.submitEditLeadSp_btn)
    public void submitAddLead(){

        if (!selectedLeadType.equals("Lead Type")) {
            if (clientCompanyNameEditLeadSp_et.getText().toString().length()>0){
                if (contactPersonEditLeadSp_et.getText().toString().length()>0){
                    if (emailEditLeadSp_et.getText().toString().length()>0){
                        if (isEmailValid(emailEditLeadSp_et.getText().toString().trim())){
                            if (mobileEditLeadSp_et.getText().toString().length() > 0 && mobileEditLeadSp_et.getText().toString().length() == 10) {
                                if (websiteEditLeadSp_et.getText().toString().length() > 0) {
                                    if (addressEditLeadSp_et.getText().toString().length() > 0) {
                                        new editManagerClientSp().execute();
                                    } else {
                                        Toast.makeText(getActivity(), "Please Enter Address", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Please Enter Website ", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Please Enter 10 digit Mobile No", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getActivity(), "InValid Email Address.", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getActivity(), "Please Enter Email Id", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getActivity(), "Please Enter Contact Person", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(getActivity(), "Please enter Client Company Name", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(getActivity(), "Please select Lead Type", Toast.LENGTH_SHORT).show();
        }
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public class deleteManagerClientSp extends AsyncTask<String, JSONObject, JSONObject> {
        String  lead_id;
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
                    makeText(getActivity(),"Lead Deleted Successfully", Toast.LENGTH_SHORT).show();
                    getManagerClientViewRecyclerView();
                }
                else {
                    makeText(getActivity(), "Not Deleted", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (Exception e) {
            }
        }
    }

    public class editManagerClientSp extends AsyncTask<String, JSONObject, JSONObject> {
        String lead_address, lead_uid,lead_leadtypeid, lead_company, lead_name, lead_email, lead_contact, lead_website;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            lead_address  =addressEditLeadSp_et.getText().toString();
            lead_uid =userIdPref;
            lead_leadtypeid = selectedLeadTypeId;
            lead_company = clientCompanyNameEditLeadSp_et.getText().toString();
            lead_name = contactPersonEditLeadSp_et.getText().toString();
            lead_email = emailEditLeadSp_et.getText().toString();
            lead_contact = contactPersonEditLeadSp_et.getText().toString();
            lead_website = websiteEditLeadSp_et.getText().toString();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Editing Client...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("filter[lead_address]", lead_address));
            params.add(new BasicNameValuePair("filter[lead_uid]", userIdPref));
            params.add(new BasicNameValuePair("filter[lead_leadtypeid]", lead_leadtypeid));
            params.add(new BasicNameValuePair("edit", "edit"));
            params.add(new BasicNameValuePair("filter[lead_company]", lead_company));
            params.add(new BasicNameValuePair("filter[lead_name]", lead_name));
            params.add(new BasicNameValuePair("filter[lead_email]", lead_email));
            params.add(new BasicNameValuePair("filter[lead_contact]", lead_contact));
            params.add(new BasicNameValuePair("filter[lead_website]", lead_website));
            params.add(new BasicNameValuePair("filter[lead_comid]", user_comidPref));
            params.add(new BasicNameValuePair("filter[lead_status]", "Done"));
            params.add(new BasicNameValuePair("lead_id", lead_iid));

            String url_add_task = ApiLink.ROOT_URL + ApiLink.MANAGER_CLIENT;

            //manager_client.php
            JSONObject json = jsonParser.makeHttpRequest(url_add_task, "POST", params);

            try {
                int success = json.getInt(TAG_SUCCESS);
                String message = json.getString(TAG_MESSAGE);
                if (success == 1 && message.equals("Lead Created Successfully")) {
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
                    makeText(getActivity(),"Lead Created Successfully", Toast.LENGTH_SHORT).show();
                    //  clearAll();
                }
                else {
                    makeText(getActivity(), "Not Updated", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (Exception e) {
            }
        }
    }

    private void getManagerClientViewRecyclerView(){
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
                            try{
                                if (response.getClients().size()>0){
                                    clientList.clear();
                                    clientList.addAll(response.getClients());

                                    viewRequestNotificationDetailsManagerAdapter = new ViewRequestNotificationDetailsManagerAdapter(getActivity(),response.getClients(), ViewClientNotificationManagerFragment.this);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                    leadTask_rv.setLayoutManager(mLayoutManager);
                                    leadTask_rv.setItemAnimator(new DefaultItemAnimator());
                                    leadTask_rv.setAdapter(viewRequestNotificationDetailsManagerAdapter);
                                    leadTask_rv.setNestedScrollingEnabled(false);
                                }
                            }catch(Exception e){
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

    private void getManagerHeadClientViewRecyclerView(){
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
                            try{
                                if (response.getClients().size()>0){
                                    clientList.clear();
                                    clientList.addAll(response.getClients());

                                    viewRequestNotificationDetailsManagerHeadAdapter = new ViewRequestNotificationDetailsManagerHeadAdapter(getActivity(),response.getClients(), ViewClientNotificationManagerFragment.this);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                    leadTask_rv.setLayoutManager(mLayoutManager);
                                    leadTask_rv.setItemAnimator(new DefaultItemAnimator());
                                    leadTask_rv.setAdapter(viewRequestNotificationDetailsManagerHeadAdapter);
                                    leadTask_rv.setNestedScrollingEnabled(false);
                                }
                            }catch(Exception e){
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

    private void getcount(){
        if (Connectivity.isConnected(getActivity())) {

            String Url = ApiLink.ROOT_URL + ApiLink.VISIT_REQUEST_NOTIFICATION;
            Map<String, String> map = new HashMap<>();
            map.put("count_clients", "");
            map.put("user_id", userIdPref);

            GSONRequest<ClientNotificationManagerBean> dashboardGsonRequest = new GSONRequest<ClientNotificationManagerBean>(
                    Request.Method.POST,
                    Url,
                    ClientNotificationManagerBean.class, map,
                    new com.android.volley.Response.Listener<ClientNotificationManagerBean>() {
                        @Override
                        public void onResponse(ClientNotificationManagerBean response) {
                            try{
                                if (response.getClients_count().size()>0){
                                    titleViewLeadCountTask_tv.setText(response.getClients_count().get(0).getTot_leads().toString());
                                }
                            }catch(Exception e){
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

    private void getMgrHeadcount(){
        if (Connectivity.isConnected(getActivity())) {

            String Url = ApiLink.ROOT_URL + ApiLink.CALL_MANAGER_HEAD_NOTIFICATION;
            Map<String, String> map = new HashMap<>();
            map.put("count_clients", "");
            map.put("user_id", userIdPref);

            GSONRequest<ClientNotificationManagerBean> dashboardGsonRequest = new GSONRequest<ClientNotificationManagerBean>(
                    Request.Method.POST,
                    Url,
                    ClientNotificationManagerBean.class, map,
                    new com.android.volley.Response.Listener<ClientNotificationManagerBean>() {
                        @Override
                        public void onResponse(ClientNotificationManagerBean response) {
                            try{
                                if (response.getClients_count().size()>0){
                                    titleViewLeadCountTask_tv.setText(response.getClients_count().get(0).getTot_leads().toString());
                                }
                            }catch(Exception e){
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


}
