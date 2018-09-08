package com.sales.tracking.salestracking.Fragment;

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
import com.sales.tracking.salestracking.Adapter.LeadSpAdapter;
import com.sales.tracking.salestracking.Adapter.RequestSpAdapter;
import com.sales.tracking.salestracking.Bean.LeadSpBean;
import com.sales.tracking.salestracking.Bean.RequestSpBean;
import com.sales.tracking.salestracking.R;
import com.sales.tracking.salestracking.Utility.ApiLink;
import com.sales.tracking.salestracking.Utility.Connectivity;
import com.sales.tracking.salestracking.Utility.GSONRequest;
import com.sales.tracking.salestracking.Utility.Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    SharedPreferences sharedPref;
    String userIdPref, userTypePref, lead_id;

    LeadSpAdapter leadSpAdapter;
    ArrayList<LeadSpBean.Leads> leadList = new ArrayList<>();

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
        if (userTypePref.equals("Sales Manager")) {
            //  getAttendanceRecyclerView();
        }else if (userTypePref.equals("Sales Executive")){
            getSPLeadViewRecyclerView();
        }

        viewLeadTaskDetails_cv.setVisibility(View.GONE);
    }

    @OnClick(R.id.minusLeadTypeDetail_iv)
    public void hideLeadDetails(){
        viewLeadTaskDetails_cv.setVisibility(View.GONE);
        leadTaskHeader_rl.setVisibility(View.VISIBLE);
        if (userTypePref.equals("Sales Manager")) {
            //  getAttendanceRecyclerView();
        }else if (userTypePref.equals("Sales Executive")){
            getSPLeadViewRecyclerView();
        }
    }

    public void getLeadData(LeadSpBean.Leads bean){
        viewLeadTaskDetails_cv.setVisibility(View.VISIBLE);
        leadTaskHeader_rl.setVisibility(View.GONE);

        clientCompanyNameLeadTask_tv.setText(bean.getLead_company());
        leadTypeViewLead_tv.setText(bean.getLeadtype_name());
        contactPersonLeadView_tv.setText(bean.getLead_name());
        emailLeadViewTask_tv.setText(bean.getLead_email());
        mobileLeadViewTask_tv.setText(bean.getLead_contact());
        websiteLeadViewTask_tv.setText(bean.getLead_website());
        addressLeadViewTask_tv.setText(bean.getLead_address());
        statusLeadViewTask_tv.setText(bean.getLead_status());
    }

    private void getSPLeadViewRecyclerView(){
        if (Connectivity.isConnected(getActivity())) {

            String Url = ApiLink.ROOT_URL + ApiLink.LEAD_VIEW_SALESPERSON;
            Map<String, String> map = new HashMap<>();
            map.put("select", "");
            map.put("lead_uid", userIdPref);
            map.put("lead_comid", "1");

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
