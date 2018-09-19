package com.sales.tracking.salestracking.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
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

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.sales.tracking.salestracking.Adapter.ViewReassignedRequestAdapter;
import com.sales.tracking.salestracking.Adapter.ViewTotalExpensesAdapter;
import com.sales.tracking.salestracking.Adapter.ViewTotalExpensesManagerAdapter;
import com.sales.tracking.salestracking.Bean.ExpencesSpBean;
import com.sales.tracking.salestracking.Bean.VieqRequestReassignedBean;
import com.sales.tracking.salestracking.R;
import com.sales.tracking.salestracking.Utility.ApiLink;
import com.sales.tracking.salestracking.Utility.Connectivity;
import com.sales.tracking.salestracking.Utility.GSONRequest;
import com.sales.tracking.salestracking.Utility.JSONParser;
import com.sales.tracking.salestracking.Utility.Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ViewReassignManagerFragment extends Fragment {

    @BindView(R.id.statusViewReasignedRequest_tv)
    TextView statusViewReasignedRequest_tv;

    @BindView(R.id.commentViewReasignedRequest_tv)
    TextView commentViewReasignedRequest_tv;

    @BindView(R.id.taskViewReasignedRequest_tv)
    TextView taskViewReasignedRequest_tv;

    @BindView(R.id.typeViewReasignedRequest_tv)
    TextView typeViewReasignedRequest_tv;

    @BindView(R.id.dateViewReasignedRequest_tv)
    TextView dateViewReasignedRequest_tv;

    @BindView(R.id.requestedByViewReasignedRequest_tv)
    TextView requestedByViewReasignedRequest_tv;

    @BindView(R.id.minusViewReasignedRequest_iv)
    ImageView minusViewReasignedRequest_iv;

    @BindView(R.id.viewReasignedRequestDetails_cv)
    CardView viewReasignedRequestDetails_cv;

    @BindView(R.id.viewReasignedRequest_rv)
    RecyclerView viewReasignedRequest_rv;

    @BindView(R.id.viewReasignedRequestHeader_rl)
    RelativeLayout viewReasignedRequestHeader_rl;


    View view;
    SharedPreferences sharedPref;
    String userIdPref, userTypePref, expenses_id, deleteExpenses_id;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    JSONParser jsonParser = new JSONParser();
    ProgressDialog pDialog;

    ViewReassignedRequestAdapter viewReassignedRequestAdapter;

    ArrayList<ExpencesSpBean.Salesperson_Expenses> spExpensesList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_reassign_manager, container, false);
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

        //   getExpensesRecyclerView();
        if (userTypePref.equals("Sales Manager")) {
            getViewRequest();
        }
        viewReasignedRequestDetails_cv.setVisibility(View.GONE);
    }


    public void getViewRequestData(VieqRequestReassignedBean.Mgr_sp_requests bean) {

        viewReasignedRequestDetails_cv.setVisibility(View.VISIBLE);
        viewReasignedRequestHeader_rl.setVisibility(View.GONE);

        statusViewReasignedRequest_tv.setText(bean.getRequest_status());
        commentViewReasignedRequest_tv.setText(bean.getRequest_comments());
        taskViewReasignedRequest_tv.setText(bean.getLead_company()+ " - "+ bean.getPurpose_name() + " - "+ bean.getVisit_address());
        typeViewReasignedRequest_tv.setText(bean.getRequest_type());
        dateViewReasignedRequest_tv.setText(bean.getRequest_dt());
        requestedByViewReasignedRequest_tv.setText(bean.getUser_name());

    }

    private void getViewRequest() {
        if (Connectivity.isConnected(getActivity())) {

            String Url = ApiLink.ROOT_URL + ApiLink.VIEW_REASSIGNED_REQUEST;
            Map<String, String> map = new HashMap<>();
            map.put("reporting_to", userIdPref);
            map.put("select_m", "");

            GSONRequest<VieqRequestReassignedBean> dashboardGsonRequest = new GSONRequest<VieqRequestReassignedBean>(
                    Request.Method.POST,
                    Url,
                    VieqRequestReassignedBean.class, map,
                    new com.android.volley.Response.Listener<VieqRequestReassignedBean>() {
                        @Override
                        public void onResponse(VieqRequestReassignedBean response) {
                            try {
                                if (response.getMgr_sp_requests().size() > 0) {

                                    viewReassignedRequestAdapter = new ViewReassignedRequestAdapter(getActivity(), response.getMgr_sp_requests(), ViewReassignManagerFragment.this);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                    viewReasignedRequest_rv.setLayoutManager(mLayoutManager);
                                    viewReasignedRequest_rv.setItemAnimator(new DefaultItemAnimator());
                                    viewReasignedRequest_rv.setAdapter(viewReassignedRequestAdapter);

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

    @OnClick(R.id.minusViewReasignedRequest_iv)
    public void hideExpensesDetails() {
        if (userTypePref.equals("Sales Manager")) {
            getViewRequest();
        }
        viewReasignedRequestDetails_cv.setVisibility(View.GONE);
        viewReasignedRequestHeader_rl.setVisibility(View.VISIBLE);

    }



    }
