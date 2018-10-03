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
import com.sales.tracking.salestracking.Adapter.CustomerFeedbackAdapter;
import com.sales.tracking.salestracking.Adapter.LeadSpAdapter;
import com.sales.tracking.salestracking.Bean.CustomerFeedbackBean;
import com.sales.tracking.salestracking.Bean.LeadSpBean;
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


public class ViewCustomerFeedbackFragment extends Fragment {

    View view;

    ProgressDialog pDialog;

    SharedPreferences sharedPref;
    String userIdPref, userTypePref, fb_id;


    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    JSONParser jsonParser = new JSONParser();

    CustomerFeedbackAdapter customerAdapter;
    ArrayList<CustomerFeedbackBean.Customer_Feedback> leadList = new ArrayList<>();

    @BindView(R.id.customerFeedback_rv)
    RecyclerView customerFeedback_rv;

    @BindView(R.id.viewCustomerFeedbackDetails_cv)
    CardView viewCustomerFeedbackDetails_cv;

    @BindView(R.id.customerFeedbackHeader_rl)
    RelativeLayout customerFeedbackHeader_rl;

    @BindView(R.id.clientCustomerFeedback_tv)
    TextView clientCustomerFeedback_tv;

    @BindView(R.id.minusCustomerFeedbackDetail_iv)
    ImageView minusCustomerFeedbackDetail_iv;

    @BindView(R.id.commentCustomerFeedback_tv)
    TextView commentCustomerFeedback_tv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       view = inflater.inflate(R.layout.fragment_view_customer_feedback, container, false);

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
        if (userTypePref.equals("Sales Manager")) {
        }else if (userTypePref.equals("Sales Executive")){
            getSPCustomerFeedbackRecyclerView();
        }

        viewCustomerFeedbackDetails_cv.setVisibility(View.GONE);
    }

    @OnClick(R.id.minusCustomerFeedbackDetail_iv)
    public void hideLeadDetails(){
        viewCustomerFeedbackDetails_cv.setVisibility(View.GONE);
        customerFeedbackHeader_rl.setVisibility(View.VISIBLE);
        if (userTypePref.equals("Sales Manager")) {
            //  getAttendanceRecyclerView();
        }else if (userTypePref.equals("Sales Executive")){
            getSPCustomerFeedbackRecyclerView();
        }
    }

    public void getCustomerFeedbackData(CustomerFeedbackBean.Customer_Feedback bean){
        viewCustomerFeedbackDetails_cv.setVisibility(View.VISIBLE);
        customerFeedbackHeader_rl.setVisibility(View.GONE);

        clientCustomerFeedback_tv.setText(bean.getLead_company());
        commentCustomerFeedback_tv.setText(bean.getFb_client_comments());

    }

    private void getSPCustomerFeedbackRecyclerView(){
        if (Connectivity.isConnected(getActivity())) {

            String Url = ApiLink.ROOT_URL + ApiLink.CUSTOMER_FEEDBACK;
            Map<String, String> map = new HashMap<>();
            map.put("select", "");
            map.put("fb_lead_uid", userIdPref);

            GSONRequest<CustomerFeedbackBean> dashboardGsonRequest = new GSONRequest<CustomerFeedbackBean>(
                    Request.Method.POST,
                    Url,
                    CustomerFeedbackBean.class, map,
                    new com.android.volley.Response.Listener<CustomerFeedbackBean>() {
                        @Override
                        public void onResponse(CustomerFeedbackBean response) {
                            try{
                                if (response.getCustomer_feedback().size()>0){
                                    leadList.clear();
                                    leadList.addAll(response.getCustomer_feedback());

                                    customerAdapter = new CustomerFeedbackAdapter(getActivity(),response.getCustomer_feedback(), ViewCustomerFeedbackFragment.this);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                    customerFeedback_rv.setLayoutManager(mLayoutManager);
                                    customerFeedback_rv.setItemAnimator(new DefaultItemAnimator());
                                    customerFeedback_rv.setAdapter(customerAdapter);
                                    customerFeedback_rv.setNestedScrollingEnabled(false);
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

    public void deleteCustomerFeedbackData(CustomerFeedbackBean.Customer_Feedback bean){
        viewCustomerFeedbackDetails_cv.setVisibility(View.GONE);
        customerFeedbackHeader_rl.setVisibility(View.VISIBLE);

        fb_id = bean.getFb_id();
        new deleteCustomerFeedbackSp().execute();
    }

    public class deleteCustomerFeedbackSp extends AsyncTask<String, JSONObject, JSONObject> {
        String fb_idd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

           fb_idd = fb_id;

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Deleting Customer Feedback...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("fb_id", fb_id));
            params.add(new BasicNameValuePair("delete", "delete"));

            String url_add_task = ApiLink.ROOT_URL + ApiLink.CUSTOMER_FEEDBACK;
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
                    getSPCustomerFeedbackRecyclerView();
                }
                else {
                    makeText(getActivity(), "Not Updated", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (Exception e) {
            }
        }
    }



}
