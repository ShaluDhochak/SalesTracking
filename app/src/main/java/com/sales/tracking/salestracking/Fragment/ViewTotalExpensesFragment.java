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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.sales.tracking.salestracking.Adapter.ViewCollectionAdapter;
import com.sales.tracking.salestracking.Adapter.ViewTotalExpensesAdapter;
import com.sales.tracking.salestracking.Bean.CollectionListBean;
import com.sales.tracking.salestracking.Bean.ExpencesSpBean;
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

public class ViewTotalExpensesFragment extends Fragment {

    @BindView(R.id.viewExpensesSp_rv)
    RecyclerView viewExpensesSp_rv;

    @BindView(R.id.viewExpensesSpDetails_cv)
    CardView viewExpensesSpDetails_cv;

    @BindView(R.id.viewExpensesHeader_rl)
    RelativeLayout viewExpensesHeader_rl;

    @BindView(R.id.dateExpensesSpTask_tv)
    TextView dateExpensesSpTask_tv;

    @BindView(R.id.deleteViewExpensesSpDetail_iv)
    ImageView deleteViewExpensesSpDetail_iv;

    @BindView(R.id.categoryExpensesSpDetail_tv)
    TextView categoryExpensesSpDetail_tv;

    @BindView(R.id.amountExpensesSpDetail_tv)
    TextView amountExpensesSpDetail_tv;

    @BindView(R.id.modeExpensesSpDetail_tv)
    TextView modeExpensesSpDetail_tv;

    @BindView(R.id.detailsExpensesSpDetail_tv)
    TextView detailsExpensesSpDetail_tv;

    @BindView(R.id.photoExpensesSpDetail_tv)
    ImageView photoExpensesSpDetail_tv;

    View view;
    SharedPreferences sharedPref;
    String userIdPref, userTypePref;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    JSONParser jsonParser = new JSONParser();
    ProgressDialog pDialog;

    ViewTotalExpensesAdapter viewTotalExpensesAdapter;

    ArrayList<ExpencesSpBean.Salesperson_Expenses> spExpensesList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_total_expenses, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getExpensesRecyclerView();
        initialiseUI();
    }

    private void initialiseUI() {

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        userIdPref = sharedPref.getString("user_id", "");
        userTypePref = sharedPref.getString("user_type", "");

        getExpensesRecyclerView();
        viewExpensesSpDetails_cv.setVisibility(View.GONE);
    }

    private void getExpensesRecyclerView() {
        if (Connectivity.isConnected(getActivity())) {

            String Url = ApiLink.ROOT_URL + ApiLink.Expenses_SP;
            Map<String, String> map = new HashMap<>();
            map.put("expense_uid", userIdPref);
            map.put("select_exp", "");

            GSONRequest<ExpencesSpBean> dashboardGsonRequest = new GSONRequest<ExpencesSpBean>(
                    Request.Method.POST,
                    Url,
                    ExpencesSpBean.class, map,
                    new com.android.volley.Response.Listener<ExpencesSpBean>() {
                        @Override
                        public void onResponse(ExpencesSpBean response) {
                            try {
                                if (response.getSalesperson_expenses().size() > 0) {
                                    spExpensesList.clear();
                                    spExpensesList.addAll(response.getSalesperson_expenses());

                                    viewTotalExpensesAdapter = new ViewTotalExpensesAdapter(getActivity(), response.getSalesperson_expenses(), ViewTotalExpensesFragment.this);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                    viewExpensesSp_rv.setLayoutManager(mLayoutManager);
                                    viewExpensesSp_rv.setItemAnimator(new DefaultItemAnimator());
                                    viewExpensesSp_rv.setAdapter(viewTotalExpensesAdapter);

                                }
                            } catch (Exception e) {
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

    public void getExpensesData(ExpencesSpBean.Salesperson_Expenses bean) {
        dateExpensesSpTask_tv.setText("");
        //  deleteViewExpensesSpDetail_iv.setText("");
        categoryExpensesSpDetail_tv.setText("");
        amountExpensesSpDetail_tv.setText("");
        modeExpensesSpDetail_tv.setText("");
        detailsExpensesSpDetail_tv.setText("");
     //   photoExpensesSpDetail_tv.setText("");

    }

}
