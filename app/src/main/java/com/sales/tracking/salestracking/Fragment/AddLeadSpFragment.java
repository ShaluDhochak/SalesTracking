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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.sales.tracking.salestracking.Bean.LeadSpBean;
import com.sales.tracking.salestracking.Bean.SalesCallTaskSpBean;
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

public class AddLeadSpFragment extends Fragment {

    View view;
    SharedPreferences sharedPref;
    String userIdPref, userTypePref, user_comidPref;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    JSONParser jsonParser = new JSONParser();

    String selectedLeadType, selectedLeadTypeId, leadType_id;

    ArrayList<String> leadTypeAddLeadSp;

    Map<String, String> leadTypeMap = new HashMap<>();
    ProgressDialog pDialog;

    @BindView(R.id.lTypeAddLeadSp_sp)
    Spinner lTypeAddLeadSp_sp;

    @BindView(R.id.clientCompanyNameAddLeadSp_et)
    EditText clientCompanyNameAddLeadSp_et;

    @BindView(R.id.contactPersonAddLeadSp_et)
    EditText contactPersonAddLeadSp_et;

    @BindView(R.id.emailAddLeadSp_et)
    EditText emailAddLeadSp_et;

    @BindView(R.id.mobileAddLeadSp_et)
    EditText mobileAddLeadSp_et;

    @BindView(R.id.addressAddLeadSp_et)
    EditText addressAddLeadSp_et;

    @BindView(R.id.websiteAddLeadSp_et)
    EditText websiteAddLeadSp_et;

    @BindView(R.id.submitAddLeadSp_btn)
    Button submitAddLeadSp_btn;

    @BindView(R.id.titleAddLeadSp_tv)
    TextView titleAddLeadSp_tv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_lead_sp, container, false);
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

        if (userTypePref.equals("Sales Executive")) {
            titleAddLeadSp_tv.setText("Add Lead");
            selectleadType();
        }else if (userTypePref.equals("Sales Manager")){
            titleAddLeadSp_tv.setText("Add Client");
            selectleadType();
        }
    }

    private void selectleadType(){
        try{
            if (Connectivity.isConnected(getActivity())) {
                String Url = ApiLink.ROOT_URL + ApiLink.LEAD_VIEW_SALESPERSON;
                Map<String, String> map = new HashMap<>();
                map.put("leadtype_dropdown","" );
                map.put("lead_comid", user_comidPref);

                final GSONRequest<LeadSpBean> leadTypeGsonRequest = new GSONRequest<LeadSpBean>(
                        Request.Method.POST,
                        Url,
                        LeadSpBean.class,map,
                        new com.android.volley.Response.Listener<LeadSpBean>() {
                            @Override
                            public void onResponse(LeadSpBean response) {
                                leadTypeAddLeadSp.clear();
                                leadTypeAddLeadSp.add("Lead Type");
                                for(int i=0;i<response.getLeadtype_dropdown().size();i++)
                                {
                                    leadTypeAddLeadSp.add(response.getLeadtype_dropdown().get(i).getLeadtype_name());
                                    leadTypeMap.put(response.getLeadtype_dropdown().get(i).getLeadtype_id(),response.getLeadtype_dropdown().get(i).getLeadtype_name() );
                                }
                            }
                        },
                        new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Utilities.serverError(getActivity());
                            }
                        });
                leadTypeGsonRequest.setShouldCache(false);
                Utilities.getRequestQueue(getActivity()).add(leadTypeGsonRequest);
            }
            leadTypeAddLeadSp = new ArrayList<String>();
            leadTypeAddLeadSp.clear();
            leadTypeAddLeadSp.add("Lead Type");
            ArrayAdapter<String> leadTypeDataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_textview, leadTypeAddLeadSp);
            leadTypeDataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
            lTypeAddLeadSp_sp.setAdapter(leadTypeDataAdapter);

        }catch (Exception e){
        }
    }

    @OnItemSelected(R.id.lTypeAddLeadSp_sp)
    public void visitTaskAddSelected(Spinner spinner, int position) {
        selectedLeadType = spinner.getSelectedItem().toString();
        for (Map.Entry<String, String> e : leadTypeMap.entrySet()) {
            Object key = e.getKey();
            Object value = e.getValue();
            if (value.equals(selectedLeadType)) {
                selectedLeadTypeId = (String) key;
                leadType_id= (String) key;
            }
        }
    }

    @OnClick(R.id.submitAddLeadSp_btn)
    public void submitAddLead(){

        if (!selectedLeadType.equals("Lead Type")) {
            if (clientCompanyNameAddLeadSp_et.getText().toString().length()>0){
                if (contactPersonAddLeadSp_et.getText().toString().length()>0){
                    if (emailAddLeadSp_et.getText().toString().length()>0){
                        if (isEmailValid(emailAddLeadSp_et.getText().toString().trim())){
                                if (mobileAddLeadSp_et.getText().toString().length() > 0 && mobileAddLeadSp_et.getText().toString().length() == 10) {
                                    if (websiteAddLeadSp_et.getText().toString().length() > 0) {
                                        if (addressAddLeadSp_et.getText().toString().length() > 0) {
                                            if (userTypePref.equals("Sales Executive")) {
                                                new addLeadSp().execute();
                                            } else if (userTypePref.equals("Sales Manager")) {
                                                new addClientSp().execute();
                                            }

                                        } else {
                                            Toast.makeText(getActivity(), "Please Enter Address", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(getActivity(), "Please Enter Website ", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Please Enter 10 digit Mobile No", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(getActivity(), "InValid Email Address.", Toast.LENGTH_SHORT).show();
                            }
                    }else{
                        Toast.makeText(getActivity(), "Please Enter Email Id", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getActivity(), "Please Enter Contact Person", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(getActivity(), "Please enter Client Company Name", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(getActivity(), "Please select Lead Type", Toast.LENGTH_SHORT).show();
        }
    }

    public class addLeadSp extends AsyncTask<String, JSONObject, JSONObject> {
        String lead_address, lead_uid,lead_leadtypeid, lead_company, lead_name, lead_email, lead_contact, lead_website;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            lead_address  =addressAddLeadSp_et.getText().toString();
            lead_uid =userIdPref;
            lead_leadtypeid = leadType_id;
            lead_company = clientCompanyNameAddLeadSp_et.getText().toString();
            lead_name = contactPersonAddLeadSp_et.getText().toString();
            lead_email = emailAddLeadSp_et.getText().toString();
            lead_contact = mobileAddLeadSp_et.getText().toString();
            lead_website = websiteAddLeadSp_et.getText().toString();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Adding Lead...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("lead_address", lead_address));
            params.add(new BasicNameValuePair("lead_uid", userIdPref));
            params.add(new BasicNameValuePair("lead_leadtypeid", lead_leadtypeid));
            params.add(new BasicNameValuePair("add", "add"));
            params.add(new BasicNameValuePair("lead_company", lead_company));
            params.add(new BasicNameValuePair("lead_name", lead_name));
            params.add(new BasicNameValuePair("lead_email", lead_email));
            params.add(new BasicNameValuePair("lead_contact", lead_contact));
            params.add(new BasicNameValuePair("lead_website", lead_website));
            params.add(new BasicNameValuePair("lead_comid", user_comidPref));

            String url_add_task = ApiLink.ROOT_URL + ApiLink.LEAD_VIEW_SALESPERSON;

            //manager_client.php
            JSONObject json = jsonParser.makeHttpRequest(url_add_task, "POST", params);

            try {
                int success = json.getInt(TAG_SUCCESS);
                String message = json.getString(TAG_MESSAGE);
                if (success == 1 && message.equals("Lead Created Successfully")) {
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
                    makeText(getActivity(),"Lead Created Successfully", Toast.LENGTH_SHORT).show();
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

    public class addClientSp extends AsyncTask<String, JSONObject, JSONObject> {
        String lead_address, lead_uid,lead_leadtypeid, lead_company, lead_name, lead_email, lead_contact, lead_website;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            lead_address  =addressAddLeadSp_et.getText().toString();
            lead_uid =userIdPref;
            lead_leadtypeid = leadType_id;
            lead_company = clientCompanyNameAddLeadSp_et.getText().toString();
            lead_name = contactPersonAddLeadSp_et.getText().toString();
            lead_email = emailAddLeadSp_et.getText().toString();
            lead_contact = mobileAddLeadSp_et.getText().toString();
            lead_website = websiteAddLeadSp_et.getText().toString();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Adding Client...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("lead_address", lead_address));
            params.add(new BasicNameValuePair("lead_uid", userIdPref));
            params.add(new BasicNameValuePair("lead_leadtypeid", lead_leadtypeid));
            params.add(new BasicNameValuePair("add", "add"));
            params.add(new BasicNameValuePair("lead_company", lead_company));
            params.add(new BasicNameValuePair("lead_name", lead_name));
            params.add(new BasicNameValuePair("lead_email", lead_email));
            params.add(new BasicNameValuePair("lead_contact", lead_contact));
            params.add(new BasicNameValuePair("lead_website", lead_website));
            params.add(new BasicNameValuePair("lead_comid", user_comidPref));
            params.add(new BasicNameValuePair("lead_status", "Done"));

            String url_add_task = ApiLink.ROOT_URL + ApiLink.MANAGER_CLIENT;

            //manager_client.php
            JSONObject json = jsonParser.makeHttpRequest(url_add_task, "POST", params);

            try {
                int success = json.getInt(TAG_SUCCESS);
                String message = json.getString(TAG_MESSAGE);
                if (success == 1 && message.equals("Lead Created Successfully")) {
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
                    makeText(getActivity(),"Lead Created Successfully", Toast.LENGTH_SHORT).show();
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


    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void clearAll(){
        lTypeAddLeadSp_sp.setSelection(0);
        addressAddLeadSp_et.setText("");
        clientCompanyNameAddLeadSp_et.setText("");
        contactPersonAddLeadSp_et.setText("");
        emailAddLeadSp_et.setText("");
        contactPersonAddLeadSp_et.setText("");
        websiteAddLeadSp_et.setText("");
        mobileAddLeadSp_et.setText("");
    }
}
