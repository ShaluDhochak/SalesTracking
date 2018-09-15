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
import com.sales.tracking.salestracking.Adapter.SalesPersonAdapter;
import com.sales.tracking.salestracking.Adapter.ViewTotalExpensesAdapter;
import com.sales.tracking.salestracking.Bean.ExpencesSpBean;
import com.sales.tracking.salestracking.Bean.ManagerUserBean;
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


    View view;
    SharedPreferences sharedPref;
    String userIdPref, userTypePref, expenses_id, deleteExpenses_id;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    JSONParser jsonParser = new JSONParser();
    ProgressDialog pDialog;

    SalesPersonAdapter salesPersonAdapter;

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
        viewSalesPersonDetails_cv.setVisibility(View.GONE);
    }

    public void getSalesPersonRecyclerView(){

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


}
