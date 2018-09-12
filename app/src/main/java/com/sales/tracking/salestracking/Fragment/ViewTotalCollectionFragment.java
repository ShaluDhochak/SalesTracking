package com.sales.tracking.salestracking.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.sales.tracking.salestracking.Adapter.LeadSpAdapter;
import com.sales.tracking.salestracking.Adapter.TodaysTaskSalesPersonAdapter;
import com.sales.tracking.salestracking.Adapter.ViewCollectionAdapter;
import com.sales.tracking.salestracking.Bean.CollectionListBean;
import com.sales.tracking.salestracking.Bean.DashboardSalesPersonBean;
import com.sales.tracking.salestracking.Bean.LeadSpBean;
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

import static android.widget.Toast.makeText;

public class ViewTotalCollectionFragment extends Fragment {

    View view;

    SharedPreferences sharedPref;
    String userIdPref, userTypePref;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    JSONParser jsonParser = new JSONParser();
    ProgressDialog pDialog;

    ViewCollectionAdapter viewCollectionAdapter;

    @BindView(R.id.viewTotalCollection_rv)
    RecyclerView viewTotalCollection_rv;

    @BindView(R.id.addAmountAddTotalCollection_et)
    EditText addAmountAddTotalCollection_et;

    @BindView(R.id.submitAddTotalCollection_btn)
    Button submitAddTotalCollection_btn;

    String collection_iid, collection_uiid;

    ArrayList<CollectionListBean.Collections> spMeetingTodayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_total_collection, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getTodaysTaskRecyclerView();
        initialiseUI();
    }

    private void initialiseUI()
    {

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        userIdPref = sharedPref.getString("user_id", "");
        userTypePref = sharedPref.getString("user_type", "");

        getTodaysTaskRecyclerView();
    }

    @OnClick(R.id.submitAddTotalCollection_btn)
    public void submitAddTotalCollection(){
        if (addAmountAddTotalCollection_et.getText().toString().length()>0) {
            new addTotalCollectionSp().execute();
        }else {
            Toast.makeText(getActivity(), "Please Enter Collection Amount", Toast.LENGTH_SHORT).show();
        }
    }

    public class addTotalCollectionSp extends AsyncTask<String, JSONObject, JSONObject> {
        String collection_amount, collection_uid;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            collection_amount  =addAmountAddTotalCollection_et.getText().toString();
            collection_uid =userIdPref;

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Adding Collection...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("collection_amount", collection_amount));
            params.add(new BasicNameValuePair("collection_uid", collection_uid));
            params.add(new BasicNameValuePair("add", "add"));

            String url_add_task = ApiLink.ROOT_URL + ApiLink.COLLECTION_SP;
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
                    clearAll();

                    getTodaysTaskRecyclerView();
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
        addAmountAddTotalCollection_et.setText("");
    }

    private void getTodaysTaskRecyclerView(){
        if (Connectivity.isConnected(getActivity())) {

            String Url = ApiLink.ROOT_URL + ApiLink.COLLECTION_SP;
            Map<String, String> map = new HashMap<>();
            map.put("collection_uid", userIdPref);
            map.put("select", "");

            GSONRequest<CollectionListBean> dashboardGsonRequest = new GSONRequest<CollectionListBean>(
                    Request.Method.POST,
                    Url,
                    CollectionListBean.class, map,
                    new com.android.volley.Response.Listener<CollectionListBean>() {
                        @Override
                        public void onResponse(CollectionListBean response) {
                            try{
                                if (response.getCollections().size()>0){
                                    spMeetingTodayList.clear();
                                    spMeetingTodayList.addAll(response.getCollections());

                                    viewCollectionAdapter = new ViewCollectionAdapter(getActivity(),response.getCollections(), ViewTotalCollectionFragment.this);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                    viewTotalCollection_rv.setLayoutManager(mLayoutManager);
                                    viewTotalCollection_rv.setItemAnimator(new DefaultItemAnimator());
                                    viewTotalCollection_rv.setAdapter(viewCollectionAdapter);

                                }
                            }catch(Exception e){
                                Toast.makeText(getActivity(), "Something went wrong..", Toast.LENGTH_SHORT).show();
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

    public void getDeleteTotalCollection(CollectionListBean.Collections bean) {

        collection_iid = bean.getCollection_id().toString();
        new deleteTotalCollectionSp().execute();
        getTodaysTaskRecyclerView();
    }

    public class deleteTotalCollectionSp extends AsyncTask<String, JSONObject, JSONObject> {
        String collection_id, collection_uid;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            collection_id  =collection_iid;
            collection_uid =userIdPref;

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Deleting Collection...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("collection_id", collection_id));
            params.add(new BasicNameValuePair("collection_uid", collection_uid));
            params.add(new BasicNameValuePair("delete", "delete"));

            String url_add_task = ApiLink.ROOT_URL + ApiLink.COLLECTION_SP;
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
                    clearAll();
                    getTodaysTaskRecyclerView();
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
