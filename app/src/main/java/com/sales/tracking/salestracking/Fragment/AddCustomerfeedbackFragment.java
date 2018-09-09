package com.sales.tracking.salestracking.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.sales.tracking.salestracking.Bean.CustomerFeedbackBean;
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
import butterknife.OnItemSelected;

import static android.widget.Toast.makeText;

public class AddCustomerfeedbackFragment extends Fragment {

    View view;

    @BindView(R.id.commentsAddCfSp_et)
    EditText commentsAddCfSp_et;

    @BindView(R.id.clientNameAddCfSp_sp)
    Spinner clientNameAddCfSp_sp;

    @BindView(R.id.submitAddCustomerFeedbackSp_btn)
    Button submitAddCustomerFeedbackSp_btn;

    SharedPreferences sharedPref;
    String userIdPref, userTypePref, user_comidPref;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    JSONParser jsonParser = new JSONParser();

    String selectedClient, selectedClientId;

    ArrayList<String> clientCustomerFeedbackSp;

    Map<String, String> clientCustomerFeedbackMap = new HashMap<>();
    ProgressDialog pDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_customerfeedback, container, false);
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

        selectClientName();

    }

    @OnClick(R.id.submitAddCustomerFeedbackSp_btn)
    public void addCustomerFeedback(){
        if (!selectedClient.equals("Client Name")) {
            if (!(commentsAddCfSp_et.getText().toString().equals(""))) {
                        new addCustomerFeedbackSp().execute();
            }else{
                Toast.makeText(getActivity(), "Please Enter Comments", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(getActivity(), "Please select Category", Toast.LENGTH_SHORT).show();
        }
    }

    public class addCustomerFeedbackSp extends AsyncTask<String, JSONObject, JSONObject> {
        String fb_leadid, fb_lead_uid,fb_client_comments;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            fb_leadid  =selectedClientId;
            fb_client_comments =commentsAddCfSp_et.getText().toString();
            fb_lead_uid = userIdPref;

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Adding Customer Feedback...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("fb_leadid", fb_leadid));
            params.add(new BasicNameValuePair("fb_client_comments", fb_client_comments));
            params.add(new BasicNameValuePair("fb_lead_uid", fb_lead_uid));
            params.add(new BasicNameValuePair("add", "add"));

            String url_add_task = ApiLink.ROOT_URL + ApiLink.CUSTOMER_FEEDBACK;
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
        clientNameAddCfSp_sp.setSelection(0);
        commentsAddCfSp_et.setText("");
    }



    private void selectClientName() {

        try{
            if (Connectivity.isConnected(getActivity())) {
                String Url = ApiLink.ROOT_URL + ApiLink.CUSTOMER_FEEDBACK;
                Map<String, String> map = new HashMap<>();
                map.put("comp_id",user_comidPref);
                map.put("lead_dropdown", "");

                final GSONRequest<CustomerFeedbackBean> locationSpinnerGsonRequest = new GSONRequest<CustomerFeedbackBean>(
                        Request.Method.POST,
                        Url,
                        CustomerFeedbackBean.class,map,
                        new com.android.volley.Response.Listener<CustomerFeedbackBean>() {
                            @Override
                            public void onResponse(CustomerFeedbackBean response) {
                                clientCustomerFeedbackSp.clear();
                                clientCustomerFeedbackSp.add("Client Name");
                                for(int i=0;i<response.getLead_dropdown().size();i++)
                                {
                                    clientCustomerFeedbackSp.add(response.getLead_dropdown().get(i).getLead_company());
                                    clientCustomerFeedbackMap.put(response.getLead_dropdown().get(i).getLead_id(),response.getLead_dropdown().get(i).getLead_company() );
                                }
                            }
                        },
                        new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Utilities.serverError(getActivity());
                            }
                        });
                locationSpinnerGsonRequest.setShouldCache(false);
                Utilities.getRequestQueue(getActivity()).add(locationSpinnerGsonRequest);
            }
            clientCustomerFeedbackSp = new ArrayList<String>();
            clientCustomerFeedbackSp.clear();
            clientCustomerFeedbackSp.add("Client Name");
            ArrayAdapter<String> clientCfAdapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, clientCustomerFeedbackSp);
            clientCfAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
            clientNameAddCfSp_sp.setAdapter(clientCfAdapter);

        }catch (Exception e){
        }
    }

    @OnItemSelected(R.id.clientNameAddCfSp_sp)
    public void categorySelected(Spinner spinner, int position) {
        selectedClient = spinner.getSelectedItem().toString();

        for (Map.Entry<String, String> e : clientCustomerFeedbackMap.entrySet()) {
            Object key = e.getKey();
            Object value = e.getValue();
            if (value.equals(selectedClient)) {
                selectedClientId = (String) key;
            }
        }
    }


}
