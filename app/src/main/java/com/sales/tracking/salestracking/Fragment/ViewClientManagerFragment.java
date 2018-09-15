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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.sales.tracking.salestracking.Adapter.LeadSpAdapter;
import com.sales.tracking.salestracking.Adapter.ManagerClientAdapter;
import com.sales.tracking.salestracking.Bean.LeadSpBean;
import com.sales.tracking.salestracking.Bean.ManagerBean;
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

    SharedPreferences sharedPref;
    String userIdPref, userTypePref, user_comidPref, lead_iid;

    ManagerClientAdapter managerClientAdapter;
    ArrayList<ManagerBean.clients> clientList = new ArrayList<>();

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

        viewLeadTaskDetails_cv.setVisibility(View.GONE);

        if (userTypePref.equals("Sales Manager")) {
            titleViewLeadTask_tv.setText("View Client");
            getManagerClientViewRecyclerView();
        }

    }

    @OnClick(R.id.minusLeadTypeDetail_iv)
    public void hideLeadDetails(){
        viewLeadTaskDetails_cv.setVisibility(View.GONE);
        leadTaskHeader_rl.setVisibility(View.VISIBLE);
        if (userTypePref.equals("Sales Manager")) {
            getManagerClientViewRecyclerView();
        }
    }

    public void deleteClientData(ManagerBean.clients bean){
        viewLeadTaskDetails_cv.setVisibility(View.GONE);
        leadTaskHeader_rl.setVisibility(View.VISIBLE);

        lead_iid = bean.getLead_id().toString();
        new deleteManagerClientSp().execute();
    }

    public void getClientData(ManagerBean.clients bean){
        viewLeadTaskDetails_cv.setVisibility(View.VISIBLE);
        leadTaskHeader_rl.setVisibility(View.GONE);

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

                                    managerClientAdapter = new ManagerClientAdapter(getActivity(),response.getClients(), ViewClientManagerFragment.this);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                    leadTask_rv.setLayoutManager(mLayoutManager);
                                    leadTask_rv.setItemAnimator(new DefaultItemAnimator());
                                    leadTask_rv.setAdapter(managerClientAdapter);
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
