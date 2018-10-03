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
import com.sales.tracking.salestracking.Adapter.RequestSpAdapter;
import com.sales.tracking.salestracking.Bean.LeadSpBean;
import com.sales.tracking.salestracking.Bean.ManagerBean;
import com.sales.tracking.salestracking.Bean.RequestSpBean;
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

public class ViewLeadSpFragment extends Fragment {

    View view;

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

    @BindView(R.id.editLeadSpDetails_cv)
            CardView editLeadSpDetails_cv;

    SharedPreferences sharedPref;
    String userIdPref, userTypePref, user_comidPref, lead_iid;

    LeadSpAdapter leadSpAdapter;
    ManagerClientAdapter managerClientAdapter;
    ArrayList<LeadSpBean.Leads> leadList = new ArrayList<>();

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    JSONParser jsonParser = new JSONParser();
    ProgressDialog pDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

        createdByLeadViewTask_rl.setVisibility(View.GONE);
        separatorBelowCreatedByLeadView.setVisibility(View.GONE);
        viewLeadTaskDetails_cv.setVisibility(View.GONE);
        editLeadSpDetails_cv.setVisibility(View.GONE);

       if (userTypePref.equals("Sales Executive")){
            getSPLeadViewRecyclerView();
       }

    }

    @OnClick(R.id.minusLeadTypeDetail_iv)
    public void hideLeadDetails(){
        viewLeadTaskDetails_cv.setVisibility(View.GONE);
        leadTaskHeader_rl.setVisibility(View.VISIBLE);
        createdByLeadViewTask_rl.setVisibility(View.GONE);
        separatorBelowCreatedByLeadView.setVisibility(View.GONE);

        if (userTypePref.equals("Sales Executive")){
            getSPLeadViewRecyclerView();
        }
    }

    public void getLeadData(LeadSpBean.Leads bean){
        viewLeadTaskDetails_cv.setVisibility(View.VISIBLE);
        leadTaskHeader_rl.setVisibility(View.GONE);
        createdByLeadViewTask_rl.setVisibility(View.GONE);
        separatorBelowCreatedByLeadView.setVisibility(View.GONE);

        clientCompanyNameLeadTask_tv.setText(bean.getLead_company());
        leadTypeViewLead_tv.setText(bean.getLeadtype_name());
        contactPersonLeadView_tv.setText(bean.getLead_name());
        emailLeadViewTask_tv.setText(bean.getLead_email());
        mobileLeadViewTask_tv.setText(bean.getLead_contact());
        websiteLeadViewTask_tv.setText(bean.getLead_website());
        addressLeadViewTask_tv.setText(bean.getLead_address());
        statusLeadViewTask_tv.setText(bean.getLead_status());
    }

    public void deleteLeadData(LeadSpBean.Leads bean){
        viewLeadTaskDetails_cv.setVisibility(View.GONE);
        leadTaskHeader_rl.setVisibility(View.VISIBLE);

        lead_iid = bean.getLead_id().toString();
        new deleteLeadSp().execute();
    }

    private void getSPLeadViewRecyclerView(){
        if (Connectivity.isConnected(getActivity())) {

            String Url = ApiLink.ROOT_URL + ApiLink.LEAD_VIEW_SALESPERSON;
            Map<String, String> map = new HashMap<>();
            map.put("select", "");
            map.put("lead_uid", userIdPref);
            map.put("lead_comid", user_comidPref);

            GSONRequest<LeadSpBean> dashboardGsonRequest = new GSONRequest<LeadSpBean>(
                    Request.Method.POST,
                    Url,
                    LeadSpBean.class, map,
                    new com.android.volley.Response.Listener<LeadSpBean>() {
                        @Override
                        public void onResponse(LeadSpBean response) {
                            try{
                                if (response.getLeads().size()>0){
                                    leadList.clear();
                                    leadList.addAll(response.getLeads());

                                    leadSpAdapter = new LeadSpAdapter(getActivity(),response.getLeads(), ViewLeadSpFragment.this);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                    leadTask_rv.setLayoutManager(mLayoutManager);
                                    leadTask_rv.setItemAnimator(new DefaultItemAnimator());
                                    leadTask_rv.setAdapter(leadSpAdapter);
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

    public class deleteLeadSp extends AsyncTask<String, JSONObject, JSONObject> {
        String lead_uid, lead_id,lead_comid;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            lead_uid = userIdPref;
            lead_id = lead_iid;
            lead_comid = user_comidPref;

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Deleting Lead...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("lead_uid", lead_uid));
            params.add(new BasicNameValuePair("lead_id", lead_id));
            params.add(new BasicNameValuePair("lead_comid", lead_comid));
            params.add(new BasicNameValuePair("delete", "delete"));

            String url_add_task = ApiLink.ROOT_URL + ApiLink.LEAD_VIEW_SALESPERSON;
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
                    getSPLeadViewRecyclerView();
                }
                else {
                    makeText(getActivity(), "Not Deleted", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (Exception e) {
            }
        }
    }

}
