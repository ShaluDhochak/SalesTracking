package com.sales.tracking.salestracking.Fragment;

import android.app.DatePickerDialog;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.sales.tracking.salestracking.Adapter.ViewCollectionAdapter;
import com.sales.tracking.salestracking.Adapter.ViewCollectionReportAdapter;
import com.sales.tracking.salestracking.Adapter.ViewExpensesMgrHeadReportAdapter;
import com.sales.tracking.salestracking.Adapter.ViewExpensesReportAdapter;
import com.sales.tracking.salestracking.Bean.CollectionListBean;
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


public class ViewCollectionReportFragment extends Fragment {


    View view;

    //Manager
    @BindView(R.id.billNoManagerLead_tv)
    TextView billNoManagerLead_tv;

    @BindView(R.id.clientNameManagerLead_tv)
    TextView clientNameManagerLead_tv;

    @BindView(R.id.remarkManagerLead_tv)
    TextView remarkManagerLead_tv;

    @BindView(R.id.collectionModeManagerLead_tv)
    TextView collectionModeManagerLead_tv;

    //manager Account
    @BindView(R.id.viewCollectionManagerDetails_cv)
    CardView viewCollectionManagerDetails_cv;

    @BindView(R.id.nameViewManagerLead_tv)
    TextView nameViewManagerLead_tv;

    @BindView(R.id.totalCollectionManagerViewCollection_tv)
    TextView totalCollectionManagerViewCollection_tv;

    @BindView(R.id.dateViewManagerCollection_tv)
    TextView dateViewManagerCollection_tv;

    @BindView(R.id.minusManagerViewCollection_iv)
    ImageView minusManagerViewCollection_iv;

    @BindView(R.id.viewTotalReportManagerCollection_rv)
    RecyclerView viewTotalReportManagerCollection_rv;

    //Manager head
    @BindView(R.id.salesViewCollectionReportManagerHeader_rl)
    RelativeLayout salesViewCollectionReportManagerHeader_rl;


    SharedPreferences sharedPref;
    String userIdPref, userTypePref, userComidPref;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    JSONParser jsonParser = new JSONParser();
    ProgressDialog pDialog;

    ViewCollectionReportAdapter viewCollectionAdapter;

    //Search Opertaion
    @BindView(R.id.employeeAdvanceSearchReport_sp)
    Spinner employeeAdvanceSearchReport_sp;

    @BindView(R.id.fromdateAdvanceSearchReportDetail_tv)
    TextView fromdateAdvanceSearchReportDetail_tv;

    @BindView(R.id.todateAdvanceSearchReport_tv)
    TextView todateAdvanceSearchReport_tv;

    @BindView(R.id.submitAdvanceSearchReport_btn)
    Button submitAdvanceSearchReport_btn;

    //Title
    @BindView(R.id.titleViewCollectionReport_tv)
    TextView titleViewCollectionReport_tv;


    DatePickerDialog datePickerDialog;

    String selectAssignTo, selectAssignToId;

    ArrayList<String> assignToUser;

    Map<String, String> assignToUserMap = new HashMap<>();

    ViewExpensesReportAdapter viewExpensesReportAdapter;
    ViewExpensesMgrHeadReportAdapter viewExpensesMgrHeadReportAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_view_collection_report, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // getTodaysTaskRecyclerView();
        initialiseUI();
    }


    private void initialiseUI()
    {

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        userIdPref = sharedPref.getString("user_id", "");
        userTypePref = sharedPref.getString("user_type", "");
        userComidPref = sharedPref.getString("user_com_id", "");

        titleViewCollectionReport_tv.setText("Collection Report");

        viewCollectionManagerDetails_cv.setVisibility(View.GONE);
        salesViewCollectionReportManagerHeader_rl.setVisibility(View.VISIBLE);

        if (userTypePref.equals("Sales Manager")) {

            getTodaysTaskRecyclerView();
          //  selectAssignTo();

        }else if (userTypePref.equals("Sales Executive")){
            getTodaysTaskRecyclerView();

        }

    }

    public void showCollectionDetails(CollectionListBean.Collections collections){

    }

    private void getTodaysTaskRecyclerView(){
        if (Connectivity.isConnected(getActivity())) {

            String Url = ApiLink.ROOT_URL + ApiLink.COLLECTION_SP;
            Map<String, String> map = new HashMap<>();
          /*  if (userTypePref.equals("Sales Manager")) {
                map.put("reporting_to", userIdPref);
                map.put("select", "");
            }else if (userTypePref.equals("Sales Executive")){
                map.put("collection_uid", userIdPref);
                map.put("select", "");
            }
            */
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

                                    viewCollectionAdapter = new ViewCollectionReportAdapter(getActivity(),response.getCollections(), ViewCollectionReportFragment.this);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                    viewTotalReportManagerCollection_rv.setLayoutManager(mLayoutManager);
                                    viewTotalReportManagerCollection_rv.setItemAnimator(new DefaultItemAnimator());
                                    viewTotalReportManagerCollection_rv.setAdapter(viewCollectionAdapter);
                                    viewTotalReportManagerCollection_rv.setNestedScrollingEnabled(false);

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
