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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.sales.tracking.salestracking.Adapter.LeadSpAdapter;
import com.sales.tracking.salestracking.Adapter.TodaysTaskSalesPersonAdapter;
import com.sales.tracking.salestracking.Adapter.ViewCollectionAdapter;
import com.sales.tracking.salestracking.Bean.CollectionListBean;
import com.sales.tracking.salestracking.Bean.CustomerFeedbackBean;
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
import butterknife.OnItemSelected;

import static android.widget.Toast.makeText;

public class ViewTotalCollectionFragment extends Fragment {

    View view;

    SharedPreferences sharedPref;
    String userIdPref, userTypePref, userComidPref;

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

    @BindView(R.id.addTotalCollectionBox_rl)
    RelativeLayout addTotalCollectionBox_rl;

    @BindView(R.id.nameViewLead_tv)
    TextView nameViewLead_tv;

    @BindView(R.id.totalCollectionViewCollection_tv)
    TextView totalCollectionViewCollection_tv;

    @BindView(R.id.dateViewCollection_tv)
    TextView dateViewCollection_tv;

    @BindView(R.id.viewCollectionDetails_cv)
    CardView viewCollectionDetails_cv;

    @BindView(R.id.salesViewCollectionManagerHeader_rl)
    RelativeLayout salesViewCollectionManagerHeader_rl;

    @BindView(R.id.viewTotalManagerCollection_rv)
    RecyclerView viewTotalManagerCollection_rv;

    @BindView(R.id.salesViewCollectionHeader_rl)
    RelativeLayout salesViewCollectionHeader_rl;

    @BindView(R.id.titleViewCollection_tv)
    TextView titleViewCollection_tv;

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

    //Manager
    @BindView(R.id.billNoManagerLead_tv)
    TextView billNoManagerLead_tv;

    @BindView(R.id.clientNameManagerLead_tv)
    TextView clientNameManagerLead_tv;

    @BindView(R.id.remarkManagerLead_tv)
    TextView remarkManagerLead_tv;

    @BindView(R.id.collectionModeManagerLead_tv)
    TextView collectionModeManagerLead_tv;

    @BindView(R.id.billNumberAddTotalCollection_et)
    EditText billNumberAddTotalCollection_et;

    @BindView(R.id.collectionMode_sp)
    Spinner collectionMode_sp;

    @BindView(R.id.remarksAddTotalCollection_et)
    EditText remarksAddTotalCollection_et;

    @BindView(R.id.chequeNumber_et)
    EditText chequeNumber_et;

    @BindView(R.id.chequeDate_et)
    EditText chequeDate_et;

    @BindView(R.id.clientName_sp)
    Spinner clientName_sp;

    @BindView(R.id.transactionId_et)
    EditText transactionId_et;

    @BindView(R.id.chequeNumberAddTotalCollection_rl)
    RelativeLayout chequeNumberAddTotalCollection_rl;

    @BindView(R.id.chequeDateAddTotalCollection_rl)
    RelativeLayout chequeDateAddTotalCollection_rl;

    @BindView(R.id.separatorBelowChequeNumberAddCustomerFeedback)
    View separatorBelowChequeNumberAddCustomerFeedback;

    @BindView(R.id.separatorBelowChequeDateAddCustomerFeedback)
    View separatorBelowChequeDateAddCustomerFeedback;

    @BindView(R.id.transactionIdAddTotalCollection_rl)
    RelativeLayout transactionIdAddTotalCollection_rl;

    @BindView(R.id.separatorBelowTransactionIdAddCustomerFeedback)
    View separatorBelowTransactionIdAddCustomerFeedback;

    String collection_iid, collection_uiid, selectedMode = "", user_comidPref, selectedClient, selectedClientId = "";

    ArrayList<CollectionListBean.Collections> spMeetingTodayList = new ArrayList<>();
    ArrayList<String> collectionModeArrayList = new ArrayList<>();

    ArrayList<String> clientArrayList = new ArrayList<>();
    Map<String, String> clientMap = new HashMap<>();

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
        // getTodaysTaskRecyclerView();
        initialiseUI();
    }

    private void initialiseUI() {

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        userIdPref = sharedPref.getString("user_id", "");
        userTypePref = sharedPref.getString("user_type", "");
        userComidPref = sharedPref.getString("user_com_id", "");


        if (userTypePref.equals("Sales Manager")) {
            titleViewCollection_tv.setText("View Collection");
            getTodaysTaskManagerRecyclerView();
            addTotalCollectionBox_rl.setVisibility(View.GONE);


            salesViewCollectionHeader_rl.setVisibility(View.GONE);
            viewCollectionDetails_cv.setVisibility(View.GONE);
            salesViewCollectionManagerHeader_rl.setVisibility(View.VISIBLE);
            viewCollectionManagerDetails_cv.setVisibility(View.GONE);
        } else if (userTypePref.equals("Sales Executive")) {
            getTodaysTaskRecyclerView();
            addTotalCollectionBox_rl.setVisibility(View.VISIBLE);      //need to change this form
            salesViewCollectionHeader_rl.setVisibility(View.GONE);
            viewCollectionDetails_cv.setVisibility(View.GONE);
            salesViewCollectionManagerHeader_rl.setVisibility(View.VISIBLE);
            viewCollectionManagerDetails_cv.setVisibility(View.GONE);

            collectionModeArrayList.clear();
            collectionModeArrayList.add("Select Mode");
            collectionModeArrayList.add("Cash");
            collectionModeArrayList.add("Cheque");
            collectionModeArrayList.add("Paytm");

            ArrayAdapter<String> clientCfAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_textview, collectionModeArrayList);
            clientCfAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
            collectionMode_sp.setAdapter(clientCfAdapter);

            user_comidPref = sharedPref.getString("user_com_id", "");
            selectClientName();
        }

    }

    @OnItemSelected(R.id.collectionMode_sp)
    public void categorySelected(Spinner spinner, int position) {
        selectedMode = spinner.getSelectedItem().toString();

        if (selectedMode.equals("Cheque")) {
            chequeDateAddTotalCollection_rl.setVisibility(View.VISIBLE);
            chequeNumberAddTotalCollection_rl.setVisibility(View.VISIBLE);
            separatorBelowChequeDateAddCustomerFeedback.setVisibility(View.VISIBLE);
            separatorBelowChequeNumberAddCustomerFeedback.setVisibility(View.VISIBLE);

            transactionIdAddTotalCollection_rl.setVisibility(View.GONE);
            separatorBelowTransactionIdAddCustomerFeedback.setVisibility(View.GONE);
        } else if (selectedMode.equals("Cash") || selectedMode.equals("Select Mode")) {
            chequeDateAddTotalCollection_rl.setVisibility(View.GONE);
            chequeNumberAddTotalCollection_rl.setVisibility(View.GONE);
            separatorBelowChequeDateAddCustomerFeedback.setVisibility(View.GONE);
            separatorBelowChequeNumberAddCustomerFeedback.setVisibility(View.GONE);
            transactionIdAddTotalCollection_rl.setVisibility(View.GONE);
            separatorBelowTransactionIdAddCustomerFeedback.setVisibility(View.GONE);
        } else if (selectedMode.equals("Paytm")) {
            chequeDateAddTotalCollection_rl.setVisibility(View.GONE);
            chequeNumberAddTotalCollection_rl.setVisibility(View.GONE);
            separatorBelowChequeDateAddCustomerFeedback.setVisibility(View.GONE);
            separatorBelowChequeNumberAddCustomerFeedback.setVisibility(View.GONE);
            transactionIdAddTotalCollection_rl.setVisibility(View.VISIBLE);
            separatorBelowTransactionIdAddCustomerFeedback.setVisibility(View.VISIBLE);
        }
    }

    private void selectClientName() {

        try {
            if (Connectivity.isConnected(getActivity())) {
                String Url = ApiLink.ROOT_URL + ApiLink.CUSTOMER_FEEDBACK;
                Map<String, String> map = new HashMap<>();
                map.put("comp_id", user_comidPref);
                map.put("lead_dropdown", "");

                final GSONRequest<CustomerFeedbackBean> locationSpinnerGsonRequest = new GSONRequest<CustomerFeedbackBean>(
                        Request.Method.POST,
                        Url,
                        CustomerFeedbackBean.class, map,
                        new com.android.volley.Response.Listener<CustomerFeedbackBean>() {
                            @Override
                            public void onResponse(CustomerFeedbackBean response) {
                                clientArrayList.clear();
                                clientMap.clear();
                                clientArrayList.add("Client Name");
                                for (int i = 0; i < response.getLead_dropdown().size(); i++) {
                                    clientArrayList.add(response.getLead_dropdown().get(i).getLead_company());
                                    clientMap.put(response.getLead_dropdown().get(i).getLead_id(), response.getLead_dropdown().get(i).getLead_company());
                                }
                            }
                        },
                        new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Utilities.serverError(getActivity());
                            }
                        });
                clientArrayList = new ArrayList<String>();
                clientArrayList.clear();
                clientArrayList.add("Client Name");
                locationSpinnerGsonRequest.setShouldCache(false);
                Utilities.getRequestQueue(getActivity()).add(locationSpinnerGsonRequest);
            }

            ArrayAdapter<String> clientCfAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_textview, clientArrayList);
            clientCfAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
            clientName_sp.setAdapter(clientCfAdapter);

        } catch (Exception e) {
        }
    }

    @OnItemSelected(R.id.clientName_sp)
    public void clientSelected(Spinner spinner, int position) {
        selectedClient = spinner.getSelectedItem().toString();

        for (Map.Entry<String, String> e : clientMap.entrySet()) {
            Object key = e.getKey();
            Object value = e.getValue();
            if (value.equals(selectedClient)) {
                selectedClientId = (String) key;
            }
        }
    }


    @OnClick(R.id.submitAddTotalCollection_btn)
    public void submitAddTotalCollection() {
        if (addAmountAddTotalCollection_et.getText().toString().length() > 0) {
            new addTotalCollectionSp().execute();
        } else {
            Toast.makeText(getActivity(), "Please Enter Collection Amount", Toast.LENGTH_SHORT).show();
        }
    }

    public class addTotalCollectionSp extends AsyncTask<String, JSONObject, JSONObject> {
        String collection_amount, collection_uid, collection_client_id, collection_bill_no, collection_mode,
                collection_remark, cheque_number, cheque_date, transaction_id;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            collection_amount = addAmountAddTotalCollection_et.getText().toString();
            collection_uid = userIdPref;
            collection_client_id = selectedClientId;
            collection_bill_no = billNumberAddTotalCollection_et.getText().toString();
            collection_mode = selectedMode;
            collection_remark = remarksAddTotalCollection_et.getText().toString();
            cheque_number = chequeNumber_et.getText().toString();
            cheque_date = chequeDate_et.getText().toString();
            transaction_id = transactionId_et.getText().toString();

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
            params.add(new BasicNameValuePair("collection_client_id", collection_client_id));
            params.add(new BasicNameValuePair("collection_bill_no", collection_bill_no));
            params.add(new BasicNameValuePair("collection_mode", collection_mode));
            params.add(new BasicNameValuePair("collection_remark", collection_remark));
            params.add(new BasicNameValuePair("cheque_number", cheque_number));
            params.add(new BasicNameValuePair("cheque_date", cheque_date));
            params.add(new BasicNameValuePair("transaction_id", transaction_id));


            String url_add_task = ApiLink.ROOT_URL + ApiLink.COLLECTION_SP;
            JSONObject json = jsonParser.makeHttpRequest(url_add_task, "POST", params);

            try {
                int success = json.getInt(TAG_SUCCESS);
                String message = json.getString(TAG_MESSAGE);
                if (success == 1 && message.equals("Added Successfully")) {
                    return json;
                } else {
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
                    makeText(getActivity(), "Added Successfully", Toast.LENGTH_SHORT).show();
                    clearAll();
                    if (userTypePref.equals("Sales Manager")) {
                        titleViewCollection_tv.setText("View Collection");
                        getTodaysTaskManagerRecyclerView();
                    } else if (userTypePref.equals("Sales Executive")) {
                        getTodaysTaskRecyclerView();
                    }
                } else {
                    makeText(getActivity(), "Not Updated", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (Exception e) {
            }
        }
    }

    private void clearAll() {
        addAmountAddTotalCollection_et.setText("");
    }

    private void getTodaysTaskRecyclerView() {
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
                            try {
                                if (response.getCollections().size() > 0) {
                                    spMeetingTodayList.clear();
                                    spMeetingTodayList.addAll(response.getCollections());

                                 /*   viewCollectionAdapter = new ViewCollectionAdapter(getActivity(),response.getCollections(), ViewTotalCollectionFragment.this);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                    viewTotalCollection_rv.setLayoutManager(mLayoutManager);
                                    viewTotalCollection_rv.setItemAnimator(new DefaultItemAnimator());
                                    viewTotalCollection_rv.setAdapter(viewCollectionAdapter);
                                    viewTotalCollection_rv.setNestedScrollingEnabled(false);
*/
                                    viewCollectionAdapter = new ViewCollectionAdapter(getActivity(), response.getCollections(), ViewTotalCollectionFragment.this);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                    viewTotalManagerCollection_rv.setLayoutManager(mLayoutManager);
                                    viewTotalManagerCollection_rv.setItemAnimator(new DefaultItemAnimator());
                                    viewTotalManagerCollection_rv.setAdapter(viewCollectionAdapter);
                                    viewTotalManagerCollection_rv.setNestedScrollingEnabled(false);

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

    private void getTodaysTaskManagerRecyclerView() {
        if (Connectivity.isConnected(getActivity())) {

            String Url = ApiLink.ROOT_URL + ApiLink.COLLECTION_SP;
            Map<String, String> map = new HashMap<>();
            map.put("reporting_to", userIdPref);
            map.put("select", "");

           /* if (userTypePref.equals("Sales Manager")) {
                map.put("reporting_to", userIdPref);
                map.put("select", "");
            }else if (userTypePref.equals("Sales Executive")){
                map.put("collection_uid", userIdPref);
                map.put("select", "");
            }
            */

            GSONRequest<CollectionListBean> dashboardGsonRequest = new GSONRequest<CollectionListBean>(
                    Request.Method.POST,
                    Url,
                    CollectionListBean.class, map,
                    new com.android.volley.Response.Listener<CollectionListBean>() {
                        @Override
                        public void onResponse(CollectionListBean response) {
                            try {
                                if (response.getCollections().size() > 0) {
                                    spMeetingTodayList.clear();
                                    spMeetingTodayList.addAll(response.getCollections());

                                    viewCollectionAdapter = new ViewCollectionAdapter(getActivity(), response.getCollections(), ViewTotalCollectionFragment.this);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                    viewTotalManagerCollection_rv.setLayoutManager(mLayoutManager);
                                    viewTotalManagerCollection_rv.setItemAnimator(new DefaultItemAnimator());
                                    viewTotalManagerCollection_rv.setAdapter(viewCollectionAdapter);
                                    viewTotalManagerCollection_rv.setNestedScrollingEnabled(false);

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


    public void getDeleteTotalCollection(CollectionListBean.Collections bean) {

        collection_iid = bean.getCollection_id().toString();
        new deleteTotalCollectionSp().execute();
        if (userTypePref.equals("Sales Manager")) {
            titleViewCollection_tv.setText("View Collection");
            getTodaysTaskManagerRecyclerView();
        } else if (userTypePref.equals("Sales Executive")) {
            getTodaysTaskRecyclerView();
        }
    }

    public class deleteTotalCollectionSp extends AsyncTask<String, JSONObject, JSONObject> {
        String collection_id, collection_uid;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            collection_id = collection_iid;
            collection_uid = userIdPref;

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
                } else {
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
                    makeText(getActivity(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                    clearAll();
                    if (userTypePref.equals("Sales Manager")) {
                        titleViewCollection_tv.setText("View Collection");
                        getTodaysTaskManagerRecyclerView();
                    } else if (userTypePref.equals("Sales Executive")) {
                        getTodaysTaskRecyclerView();
                    }
                } else {
                    makeText(getActivity(), "Not Updated", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (Exception e) {
            }
        }
    }

    @OnClick(R.id.minusViewCollection_iv)
    public void hideManagerDetails() {
        if (userTypePref.equals("Sales Manager")) {
            titleViewCollection_tv.setText("View Collection");
            getTodaysTaskManagerRecyclerView();
            addTotalCollectionBox_rl.setVisibility(View.GONE);
            salesViewCollectionHeader_rl.setVisibility(View.GONE);
            viewCollectionDetails_cv.setVisibility(View.GONE);
            salesViewCollectionManagerHeader_rl.setVisibility(View.VISIBLE);
            viewCollectionManagerDetails_cv.setVisibility(View.GONE);
        } else if (userTypePref.equals("Sales Executive")) {
            getTodaysTaskRecyclerView();

            addTotalCollectionBox_rl.setVisibility(View.VISIBLE);
            salesViewCollectionHeader_rl.setVisibility(View.VISIBLE);
            viewCollectionDetails_cv.setVisibility(View.GONE);
            salesViewCollectionManagerHeader_rl.setVisibility(View.GONE);
            viewCollectionManagerDetails_cv.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.minusManagerViewCollection_iv)
    public void hideManager1Details() {
        if (userTypePref.equals("Sales Manager")) {
            titleViewCollection_tv.setText("View Collection");
            getTodaysTaskManagerRecyclerView();
            addTotalCollectionBox_rl.setVisibility(View.GONE);
            salesViewCollectionHeader_rl.setVisibility(View.GONE);
            viewCollectionDetails_cv.setVisibility(View.GONE);
            salesViewCollectionManagerHeader_rl.setVisibility(View.VISIBLE);
            viewCollectionManagerDetails_cv.setVisibility(View.GONE);
        } else if (userTypePref.equals("Sales Executive")) {
            getTodaysTaskRecyclerView();

            addTotalCollectionBox_rl.setVisibility(View.VISIBLE);
            salesViewCollectionHeader_rl.setVisibility(View.GONE);
            viewCollectionDetails_cv.setVisibility(View.GONE);
            salesViewCollectionManagerHeader_rl.setVisibility(View.VISIBLE);
            viewCollectionManagerDetails_cv.setVisibility(View.GONE);
        }
    }

    public void showCollectionDetails(CollectionListBean.Collections bean) {

        if (userTypePref.equals("Sales Manager")) {
            titleViewCollection_tv.setText("View Collection");
            addTotalCollectionBox_rl.setVisibility(View.GONE);
            salesViewCollectionHeader_rl.setVisibility(View.GONE);
            viewCollectionDetails_cv.setVisibility(View.GONE);
            salesViewCollectionManagerHeader_rl.setVisibility(View.GONE);
            viewCollectionManagerDetails_cv.setVisibility(View.VISIBLE);

            nameViewManagerLead_tv.setText(bean.getUser_name());
            totalCollectionManagerViewCollection_tv.setText(bean.getCollection_amount());
            dateViewManagerCollection_tv.setText(bean.getCollection_date());

            billNoManagerLead_tv.setText(bean.getCollection_bill_no());
            clientNameManagerLead_tv.setText(bean.getLead_company());
            remarkManagerLead_tv.setText(bean.getCollection_remark());
            collectionModeManagerLead_tv.setText(bean.getCollection_mode());


        } else if (userTypePref.equals("Sales Executive")) {
            addTotalCollectionBox_rl.setVisibility(View.VISIBLE);
            salesViewCollectionHeader_rl.setVisibility(View.GONE);
            viewCollectionDetails_cv.setVisibility(View.GONE);
            salesViewCollectionManagerHeader_rl.setVisibility(View.VISIBLE);
            viewCollectionManagerDetails_cv.setVisibility(View.VISIBLE);

            nameViewLead_tv.setText(bean.getUser_name());
            totalCollectionViewCollection_tv.setText(bean.getCollection_amount());
            dateViewCollection_tv.setText(bean.getCollection_date());

            nameViewManagerLead_tv.setText(bean.getUser_name());
            totalCollectionManagerViewCollection_tv.setText(bean.getCollection_amount());
            dateViewManagerCollection_tv.setText(bean.getCollection_date());


            billNoManagerLead_tv.setText(bean.getCollection_bill_no());
            clientNameManagerLead_tv.setText(bean.getLead_company());
            remarkManagerLead_tv.setText(bean.getCollection_remark());
            collectionModeManagerLead_tv.setText(bean.getCollection_mode());
        }
    }


}
