package com.sales.tracking.salestracking.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
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
import com.sales.tracking.salestracking.Adapter.AttendanceAddapter;
import com.sales.tracking.salestracking.Adapter.RequestSpAdapter;
import com.sales.tracking.salestracking.Adapter.SpAttendanceAdapter;
import com.sales.tracking.salestracking.Bean.AttendanceManagerBean;
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


public class RequestViewFragment extends Fragment {

    View view;

    @BindView(R.id.requestTask_rv)
    RecyclerView requestTask_rv;

    @BindView(R.id.viewRequestTaskDetails_cv)
    CardView viewRequestTaskDetails_cv;

    @BindView(R.id.requestTaskHeader_rl)
    RelativeLayout requestTaskHeader_rl;

    @BindView(R.id.statusRequestTask_tv)
    TextView statusRequestTask_tv;

    @BindView(R.id.commentRequestTask_tv)
    TextView commentRequestTask_tv;

    @BindView(R.id.taskViewRequestTask_tv)
    TextView taskViewRequestTask_tv;

    @BindView(R.id.typeRequestTaskDetail_tv)
    TextView typeRequestTaskDetail_tv;

    @BindView(R.id.dateViewRequestTask_tv)
    TextView dateViewRequestTask_tv;

    @BindView(R.id.deleteRequestTaskDetail_iv)
    ImageView deleteRequestTaskDetail_iv;

    @BindView(R.id.minusRequestTaskDetail_iv)
    ImageView minusRequestTaskDetail_iv;

    SharedPreferences sharedPref;
    String userIdPref, userTypePref, type_id, req_id, user_comidPref;
    ProgressDialog pDialog;


    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    JSONParser jsonParser = new JSONParser();

    RequestSpAdapter requestSpAdapter;
    ArrayList<RequestSpBean.Salesperson_requests> requestList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_request_view, container, false);
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

        if (userTypePref.equals("Sales Manager")) {
          //  getAttendanceRecyclerView();
        }else if (userTypePref.equals("Sales Executive")){
            getSPRequestViewRecyclerView();
        }

        viewRequestTaskDetails_cv.setVisibility(View.GONE);
    }


    @OnClick(R.id.minusRequestTaskDetail_iv)
    public void hideDetails(){
        viewRequestTaskDetails_cv.setVisibility(View.GONE);
        requestTaskHeader_rl.setVisibility(View.VISIBLE);
        if (userTypePref.equals("Sales Manager")) {
          //  getAttendanceRecyclerView();
        }else if (userTypePref.equals("Sales Executive")){
            getSPRequestViewRecyclerView();
        }
    }

     public void deleteRequestDetails(RequestSpBean.Salesperson_requests bean){

        req_id = bean.getRequest_id();
        new DeleteRequestTaskSp().execute();
    }

    public void getRequestData(RequestSpBean.Salesperson_requests bean){
        viewRequestTaskDetails_cv.setVisibility(View.VISIBLE);
        requestTaskHeader_rl.setVisibility(View.GONE);

        statusRequestTask_tv.setText(bean.getRequest_status());
        commentRequestTask_tv.setText(bean.getRequest_comments());
        typeRequestTaskDetail_tv.setText(bean.getRequest_type());
        dateViewRequestTask_tv.setText(bean.getRequest_dt());

        type_id = bean.getRequest_type_id().toString();

        getSPRequestTask();
    }

    private void getSPRequestViewRecyclerView(){
        if (Connectivity.isConnected(getActivity())) {

            String Url = ApiLink.ROOT_URL + ApiLink.REQUEST_VIEW_SP;
            Map<String, String> map = new HashMap<>();
            map.put("select", "");
            map.put("request_uid", userIdPref);

            GSONRequest<RequestSpBean> dashboardGsonRequest = new GSONRequest<RequestSpBean>(
                    Request.Method.POST,
                    Url,
                    RequestSpBean.class, map,
                    new com.android.volley.Response.Listener<RequestSpBean>() {
                        @Override
                        public void onResponse(RequestSpBean response) {
                            try{
                                if (response.getSalesperson_requests().size()>0){
                                    requestList.clear();
                                    requestList.addAll(response.getSalesperson_requests());

                                    requestSpAdapter = new RequestSpAdapter(getActivity(),response.getSalesperson_requests(), RequestViewFragment.this);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                    requestTask_rv.setLayoutManager(mLayoutManager);
                                    requestTask_rv.setItemAnimator(new DefaultItemAnimator());
                                    requestTask_rv.setAdapter(requestSpAdapter);
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


    private void getSPRequestTask(){
        if (Connectivity.isConnected(getActivity())) {

            String Url = ApiLink.ROOT_URL + ApiLink.REQUEST_VIEW_SP;
            Map<String, String> map = new HashMap<>();
            map.put("visit_tasks_dropdown", "");
            map.put("request_uid", userIdPref);

            GSONRequest<RequestSpBean> dashboardGsonRequest = new GSONRequest<RequestSpBean>(
                    Request.Method.POST,
                    Url,
                    RequestSpBean.class, map,
                    new com.android.volley.Response.Listener<RequestSpBean>() {
                        @Override
                        public void onResponse(RequestSpBean response) {
                            try{
                                if (response.getVisit_tasks_dropdown().size()>0){
                                     for (int i = 0; i<=response.getVisit_tasks_dropdown().size();i++) {
                                         if (response.getVisit_tasks_dropdown().get(i).getVisit_id().equals(type_id)) {
                                             taskViewRequestTask_tv.setText(response.getVisit_tasks_dropdown().get(i).getLead_company() + " - "
                                             +response.getVisit_tasks_dropdown().get(i).getPurpose_name() +" - "
                                             + response.getVisit_tasks_dropdown().get(i).getVisit_address());
                                         }
                                     }
                                }
                            }catch(Exception e){
                                e.printStackTrace();
                            //    Toast.makeText(getActivity(), "Api response Problem", Toast.LENGTH_SHORT).show();
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

    public class DeleteRequestTaskSp extends AsyncTask<String, JSONObject, JSONObject> {
        String request_id, request_uid;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            request_id = req_id;
            request_uid = userIdPref;

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Deleting Request...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("request_id", request_id));
            params.add(new BasicNameValuePair("request_uid", request_uid));
            params.add(new BasicNameValuePair("delete", "delete"));


            String url_add_task = ApiLink.ROOT_URL + ApiLink.REQUEST_VIEW_SP;
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
                    if (userTypePref.equals("Sales Manager")) {
                        //  getAttendanceRecyclerView();
                    }else if (userTypePref.equals("Sales Executive")){
                        getSPRequestViewRecyclerView();
                    }
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
