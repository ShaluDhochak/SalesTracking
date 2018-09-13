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
import com.sales.tracking.salestracking.Adapter.ViewCollectionAdapter;
import com.sales.tracking.salestracking.Adapter.ViewTotalExpensesAdapter;
import com.sales.tracking.salestracking.Adapter.ViewTotalExpensesManagerAdapter;
import com.sales.tracking.salestracking.Bean.CollectionListBean;
import com.sales.tracking.salestracking.Bean.ExpencesSpBean;
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

    @BindView(R.id.minusViewExpensesSpDetail_iv)
    ImageView minusViewExpensesSpDetail_iv;

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


    //Manager account
    @BindView(R.id.expenseByExpensesSpDetail_tv)
    TextView expenseByExpensesSpDetail_tv;

    @BindView(R.id.expensesByExpensesSpDetail_rl)
    RelativeLayout expensesByExpensesSpDetail_rl;

    @BindView(R.id.separatorBelowExpensebYExpensesSpDetail)
    View separatorBelowExpensebYExpensesSpDetail;

    View view;
    SharedPreferences sharedPref;
    String userIdPref, userTypePref, expenses_id, deleteExpenses_id;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    JSONParser jsonParser = new JSONParser();
    ProgressDialog pDialog;

    ViewTotalExpensesAdapter viewTotalExpensesAdapter;

    ViewTotalExpensesManagerAdapter viewTotalExpensesManagerAdapter;

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
        initialiseUI();
    }

    private void initialiseUI() {

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        userIdPref = sharedPref.getString("user_id", "");
        userTypePref = sharedPref.getString("user_type", "");

        if (userTypePref.equals("Sales Manager")) {
            getExpensesManagerRecyclerView();
            expensesByExpensesSpDetail_rl.setVisibility(View.VISIBLE);
            separatorBelowExpensebYExpensesSpDetail.setVisibility(View.VISIBLE);
        }else if (userTypePref.equals("Sales Executive")){
            getExpensesRecyclerView();
            expensesByExpensesSpDetail_rl.setVisibility(View.GONE);
            separatorBelowExpensebYExpensesSpDetail.setVisibility(View.GONE);
        }
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

    public void getExpensesData(ExpencesSpBean.Salesperson_Expenses bean) {

        viewExpensesSpDetails_cv.setVisibility(View.VISIBLE);
        viewExpensesHeader_rl.setVisibility(View.GONE);

        String date = bean.getExpense_date();
        String[] date1 = date.split(" ");
        dateExpensesSpTask_tv.setText(date1[0]);
        categoryExpensesSpDetail_tv.setText(bean.getExpcat_name());
        amountExpensesSpDetail_tv.setText(bean.getExpense_amt());
        modeExpensesSpDetail_tv.setText(bean.getExpense_mode());
        detailsExpensesSpDetail_tv.setText(bean.getExpense_details());
     //   photoExpensesSpDetail_tv.setText("");

    }

    @OnClick(R.id.deleteViewExpensesSpDetail_iv)
    public void deleteExpensesRowdetails(){
        viewExpensesSpDetails_cv.setVisibility(View.GONE);
        viewExpensesHeader_rl.setVisibility(View.VISIBLE);


    }

    private void getExpensesManagerRecyclerView() {
        if (Connectivity.isConnected(getActivity())) {

            String Url = ApiLink.ROOT_URL + ApiLink.Expenses_SP;
            Map<String, String> map = new HashMap<>();
            map.put("expense_uid", userIdPref);
            map.put("select_mgr_exp", "");

            GSONRequest<ExpencesSpBean> dashboardGsonRequest = new GSONRequest<ExpencesSpBean>(
                    Request.Method.POST,
                    Url,
                    ExpencesSpBean.class, map,
                    new com.android.volley.Response.Listener<ExpencesSpBean>() {
                        @Override
                        public void onResponse(ExpencesSpBean response) {
                            try {
                                if (response.getManager_expenses().size() > 0) {
                                   // spExpensesList.clear();
                                   // spExpensesList.addAll(response.get());

                                    viewTotalExpensesManagerAdapter = new ViewTotalExpensesManagerAdapter(getActivity(), response.getManager_expenses(), ViewTotalExpensesFragment.this);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                    viewExpensesSp_rv.setLayoutManager(mLayoutManager);
                                    viewExpensesSp_rv.setItemAnimator(new DefaultItemAnimator());
                                    viewExpensesSp_rv.setAdapter(viewTotalExpensesManagerAdapter);

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

    public void getExpensesManagerData(ExpencesSpBean.Manager_Expenses bean) {

        viewExpensesSpDetails_cv.setVisibility(View.VISIBLE);
        viewExpensesHeader_rl.setVisibility(View.GONE);

        String date = bean.getExpense_date();
        String[] date1 = date.split(" ");
        dateExpensesSpTask_tv.setText(date1[0]);
        categoryExpensesSpDetail_tv.setText(bean.getExpcat_name());
        amountExpensesSpDetail_tv.setText(bean.getExpense_amt());
        modeExpensesSpDetail_tv.setText(bean.getExpense_mode());
        detailsExpensesSpDetail_tv.setText(bean.getExpense_details());
        expenseByExpensesSpDetail_tv.setText(bean.getUser_name());
        //   photoExpensesSpDetail_tv.setText("");

    }


    @OnClick(R.id.minusViewExpensesSpDetail_iv)
    public void hideExpensesDetails(){
        viewExpensesSpDetails_cv.setVisibility(View.GONE);
        viewExpensesHeader_rl.setVisibility(View.VISIBLE);

        if (userTypePref.equals("Sales Manager")) {
            getExpensesManagerRecyclerView();
            expensesByExpensesSpDetail_rl.setVisibility(View.VISIBLE);
            separatorBelowExpensebYExpensesSpDetail.setVisibility(View.VISIBLE);
        }else if (userTypePref.equals("Sales Executive")){
            getExpensesRecyclerView();
            expensesByExpensesSpDetail_rl.setVisibility(View.GONE);
            separatorBelowExpensebYExpensesSpDetail.setVisibility(View.GONE);
        }

    }

    public void getExpensesDeleteData(ExpencesSpBean.Salesperson_Expenses bean) {

        expenses_id = bean.getExpense_id();
        new deleteTotalCollectionSp().execute();
    }

    public void getExpensesManagerDeleteData(ExpencesSpBean.Manager_Expenses bean) {

        expenses_id = bean.getExpense_id();
        new deleteTotalCollectionSp().execute();
    }

    public class deleteTotalCollectionSp extends AsyncTask<String, JSONObject, JSONObject> {
        String expense_uid, expense_id;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            expense_uid = userIdPref;
            expense_id = expenses_id;

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Deleting Expenses...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("expense_uid", expense_uid));
            params.add(new BasicNameValuePair("expense_id", expenses_id));
            params.add(new BasicNameValuePair("delete", "delete"));

            String url_add_task = ApiLink.ROOT_URL + ApiLink.Expenses_SP;
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
                        getExpensesManagerRecyclerView();
                    }else if (userTypePref.equals("Sales Executive")){
                        getExpensesRecyclerView();
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
