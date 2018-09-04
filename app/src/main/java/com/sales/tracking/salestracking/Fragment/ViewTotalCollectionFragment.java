package com.sales.tracking.salestracking.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.sales.tracking.salestracking.Adapter.TodaysTaskSalesPersonAdapter;
import com.sales.tracking.salestracking.Adapter.ViewCollectionAdapter;
import com.sales.tracking.salestracking.Bean.DashboardSalesPersonBean;
import com.sales.tracking.salestracking.R;
import com.sales.tracking.salestracking.Utility.ApiLink;
import com.sales.tracking.salestracking.Utility.Connectivity;
import com.sales.tracking.salestracking.Utility.GSONRequest;
import com.sales.tracking.salestracking.Utility.Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;


public class ViewTotalCollectionFragment extends Fragment {

    View view;
    ViewCollectionAdapter viewCollectionAdapter;

    @BindView(R.id.viewCollectionManager_rv)
    RecyclerView viewCollectionManager_rv;

    ArrayList<DashboardSalesPersonBean.sp_meetings_today> spMeetingTodayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_total_collection, container, false);
        initialiseUI();
        // Inflate the layout for this fragment
        return view;
    }

    private void initialiseUI(){

        getTodaysTaskRecyclerView();
    }

    private void getTodaysTaskRecyclerView(){
        if (Connectivity.isConnected(getActivity())) {

            String Url = ApiLink.ROOT_URL + ApiLink.Dashboard_SalesPerson;
            Map<String, String> map = new HashMap<>();
            map.put("visit_uid", "20");
            map.put("sel_user_dash", "");
            map.put("today", "");

            GSONRequest<DashboardSalesPersonBean> dashboardGsonRequest = new GSONRequest<DashboardSalesPersonBean>(
                    Request.Method.POST,
                    Url,
                    DashboardSalesPersonBean.class, map,
                    new com.android.volley.Response.Listener<DashboardSalesPersonBean>() {
                        @Override
                        public void onResponse(DashboardSalesPersonBean response) {
                            try{
                                if (response.getSp_meetings_today().size()>0){
                                    spMeetingTodayList.clear();
                                    spMeetingTodayList.addAll(response.getSp_meetings_today());

                                    viewCollectionAdapter = new ViewCollectionAdapter(getActivity(),response.getSp_meetings_today());
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                    viewCollectionManager_rv.setLayoutManager(mLayoutManager);
                                    viewCollectionManager_rv.setItemAnimator(new DefaultItemAnimator());
                                    viewCollectionManager_rv.setAdapter(viewCollectionAdapter);

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

}
