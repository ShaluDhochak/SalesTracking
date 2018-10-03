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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.sales.tracking.salestracking.Adapter.TargetViewAdapter;
import com.sales.tracking.salestracking.Adapter.ViewTotalExpensesAdapter;
import com.sales.tracking.salestracking.Adapter.ViewTotalExpensesManagerAdapter;
import com.sales.tracking.salestracking.Bean.ExpencesSpBean;
import com.sales.tracking.salestracking.Bean.ManagerBean;
import com.sales.tracking.salestracking.Bean.TargetManagerBean;
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


public class ViewTargetManagerFragment extends Fragment {

    DatePickerDialog datePickerDialog;
    ProgressDialog pDialog;

    SharedPreferences sharedPref;
    String userIdPref, userTypePref, user_comidPref;
    String selectStatus, selectStatusId;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    JSONParser jsonParser = new JSONParser();

    TargetViewAdapter targetViewAdapter;

    ArrayList<TargetManagerBean.Target> targetList = new ArrayList<>();


    @BindView(R.id.targetDurationDetail_tv)
    TextView targetDurationDetail_tv;

    @BindView(R.id.targetTargetDetail_tv)
    TextView targetTargetDetail_tv;

    @BindView(R.id.typeTargetDetail_tv)
    TextView typeTargetDetail_tv;

    @BindView(R.id.dateTargetTask_tv)
    TextView dateTargetTask_tv;

    @BindView(R.id.salesPersonTargetDetail_tv)
    TextView salesPersonTargetDetail_tv;

    @BindView(R.id.minusViewTargetDetail_iv)
    ImageView minusViewTargetDetail_iv;

    @BindView(R.id.viewTargetDetails_cv)
    CardView viewTargetDetails_cv;


    @BindView(R.id.viewTarget_rv)
    RecyclerView viewTarget_rv;

    @BindView(R.id.viewTargetHeader_rl)
    RelativeLayout viewTargetHeader_rl;

   String target_idd;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_target_manager, container, false);
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

        viewTargetDetails_cv.setVisibility(View.GONE);
        viewTargetHeader_rl.setVisibility(View.VISIBLE);
        if (userTypePref.equals("Sales Manager")) {
             getTargetViewRecyclerView();
        }

    }
    private void getTargetViewRecyclerView() {
        if (Connectivity.isConnected(getActivity())) {

            String Url = ApiLink.ROOT_URL + ApiLink.TARGET_MANAGER;
            Map<String, String> map = new HashMap<>();
            map.put("select", "");
            map.put("target_createdby",userIdPref);
            map.put("target_compid", user_comidPref);

            GSONRequest<TargetManagerBean> dashboardGsonRequest = new GSONRequest<TargetManagerBean>(
                    Request.Method.POST,
                    Url,
                    TargetManagerBean.class, map,
                    new com.android.volley.Response.Listener<TargetManagerBean>() {
                        @Override
                        public void onResponse(TargetManagerBean response) {
                            try {
                                if (response.getTarget().size() > 0) {
                                    targetList.clear();
                                    targetList.addAll(response.getTarget());

                                    targetViewAdapter = new TargetViewAdapter(getActivity(), response.getTarget(), ViewTargetManagerFragment.this);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                    viewTarget_rv.setLayoutManager(mLayoutManager);
                                    viewTarget_rv.setItemAnimator(new DefaultItemAnimator());
                                    viewTarget_rv.setAdapter(targetViewAdapter);
                                    viewTarget_rv.setNestedScrollingEnabled(false);

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
    }

    @OnClick(R.id.minusViewTargetDetail_iv)
    public void hideTargetManager(){
        viewTargetHeader_rl.setVisibility(View.VISIBLE);
        viewTargetDetails_cv.setVisibility(View.GONE);
        if (userTypePref.equals("Sales Manager")) {
            getTargetViewRecyclerView();
        }
    }

    public void deleteTargetData(TargetManagerBean.Target bean){
        viewTargetDetails_cv.setVisibility(View.GONE);
        viewTargetHeader_rl.setVisibility(View.VISIBLE);

        target_idd = bean.getTarget_id().toString();
        new deleteManagerTarget().execute();
    }

    public void getTargetData(TargetManagerBean.Target bean){
        viewTargetDetails_cv.setVisibility(View.VISIBLE);
        viewTargetHeader_rl.setVisibility(View.GONE);

        targetDurationDetail_tv.setText(bean.getTarget_startdate() + " to "+ bean.getTarget_enddate());
        targetTargetDetail_tv.setText(bean.getTarget_no());
        typeTargetDetail_tv.setText(bean.getTarget_type());

        String date = bean.getTarget_createdon();
        String[] date1 = date.split(" ");

        dateTargetTask_tv.setText(date1[0]);

        salesPersonTargetDetail_tv.setText(bean.getUser_name());

    }

    public class deleteManagerTarget extends AsyncTask<String, JSONObject, JSONObject> {
        String  target_id;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            target_id = target_idd;

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Deleting Target...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("target_id", target_id));
            params.add(new BasicNameValuePair("delete", "delete"));

            String url_add_task = ApiLink.ROOT_URL + ApiLink.TARGET_MANAGER;
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
                    getTargetViewRecyclerView();
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
