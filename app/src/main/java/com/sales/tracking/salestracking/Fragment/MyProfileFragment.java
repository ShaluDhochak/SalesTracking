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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.sales.tracking.salestracking.Bean.CustomerFeedbackBean;
import com.sales.tracking.salestracking.Bean.ProfileBean;
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

public class MyProfileFragment extends Fragment {
    View view;

    SharedPreferences sharedPref;
    String userIdPref, userTypePref, user_comidPref;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    JSONParser jsonParser = new JSONParser();
    ProgressDialog pDialog;

    @BindView(R.id.submitUpdateProfile_btn)
    Button submitUpdateProfile_btn;

    @BindView(R.id.nameUpdateProfile_et)
    EditText nameUpdateProfile_et;

    @BindView(R.id.emailUpdateProfile_et)
    EditText emailUpdateProfile_et;

    @BindView(R.id.contactNoUpdateProfile_et)
    EditText contactNoUpdateProfile_et;

    @BindView(R.id.submitChangePasswordProfile_btn)
    Button submitChangePasswordProfile_btn;

    @BindView(R.id.confirmChangePasswordProfile_et)
    EditText confirmChangePasswordProfile_et;

    @BindView(R.id.newChangePasswordProfile_et)
    EditText newChangePasswordProfile_et;

    @BindView(R.id.oldChangePasswordProfile_et)
    EditText oldChangePasswordProfile_et;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_profile, container, false);
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

        getProfileDetails();
    }

    private void getProfileDetails(){
        try{
            if (Connectivity.isConnected(getActivity())) {
                String Url = ApiLink.ROOT_URL + ApiLink.PROFILE_DETAILS;
                Map<String, String> map = new HashMap<>();
                map.put("user_id", userIdPref);
                map.put("select", "");

                final GSONRequest<ProfileBean> locationSpinnerGsonRequest = new GSONRequest<ProfileBean>(
                        Request.Method.POST,
                        Url,
                        ProfileBean.class, map,
                        new com.android.volley.Response.Listener<ProfileBean>() {
                            @Override
                            public void onResponse(ProfileBean response) {
                                if (response.getUser_profile().size() > 0) {
                                    contactNoUpdateProfile_et.setText(response.getUser_profile().get(0).getUser_mobile());
                                    nameUpdateProfile_et.setText(response.getUser_profile().get(0).getUser_name());
                                    emailUpdateProfile_et.setText(response.getUser_profile().get(0).getUser_email());
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
        }catch (Exception e){
        }
    }

    @OnClick(R.id.submitChangePasswordProfile_btn)
    public void changePasswordProfile(){
        if (oldChangePasswordProfile_et.getText().toString().length()>0){
            if (newChangePasswordProfile_et.getText().toString().length()>0){
                if (confirmChangePasswordProfile_et.getText().toString().length()>0){
                    if (confirmChangePasswordProfile_et.getText().toString().equals(newChangePasswordProfile_et.getText().toString())){
                        new changePasswordMethod().execute();
                    }else{
                        Toast.makeText(getActivity(), "Confirm Password must Be Same as that of New Password", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getActivity(), "Please Enter Confirm Password", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getActivity(), "Please Enter New Password", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getActivity(), "Please Enter Old Password", Toast.LENGTH_SHORT).show();
        }

    }

    @OnClick(R.id.submitUpdateProfile_btn)
    public void updateProfile(){
        String oldChange = oldChangePasswordProfile_et.getText().toString();
        if (newChangePasswordProfile_et.getText().toString().equals(oldChange)){
            new updateProfileMethod().execute();
        }else{
            Toast.makeText(getActivity(), "New Password and Old Password are same..", Toast.LENGTH_SHORT).show();
        }

    }

    public class updateProfileMethod extends AsyncTask<String, JSONObject, JSONObject> {
        String user_name, user_email,user_mobile;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            user_name  =nameUpdateProfile_et.getText().toString();
            user_email =emailUpdateProfile_et.getText().toString();
            user_mobile = contactNoUpdateProfile_et.getText().toString();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("UPdating Profile...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("filter[user_name]", user_name));
            params.add(new BasicNameValuePair("filter[user_email]", user_email));
            params.add(new BasicNameValuePair("filter[user_mobile]", user_mobile));
            params.add(new BasicNameValuePair("profile_submit", ""));
            params.add(new BasicNameValuePair("user_id", userIdPref));

            String url_add_task = ApiLink.ROOT_URL + ApiLink.PROFILE_DETAILS;
            JSONObject json = jsonParser.makeHttpRequest(url_add_task, "POST", params);

            try {
                int success = json.getInt(TAG_SUCCESS);
                String message = json.getString(TAG_MESSAGE);
                if (success == 1 && message.equals("Updated Successfully")) {
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
                    makeText(getActivity(),"Updated Successfully", Toast.LENGTH_SHORT).show();

                }
                else {
                    makeText(getActivity(), "Not Updated", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (Exception e) {
            }
        }
    }

    public class changePasswordMethod extends AsyncTask<String, JSONObject, JSONObject> {
        String user_id, new_pwd,change_pwd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            user_id  =userIdPref;
            new_pwd =newChangePasswordProfile_et.getText().toString();
            change_pwd = oldChangePasswordProfile_et.getText().toString();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Change Password...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user_id", user_id));
            params.add(new BasicNameValuePair("new_pwd", new_pwd));
            params.add(new BasicNameValuePair("change_pwd", change_pwd));

            String url_add_task = ApiLink.ROOT_URL + ApiLink.CHANGE_PASSWORD;
            JSONObject json = jsonParser.makeHttpRequest(url_add_task, "POST", params);

            try {
                int success = json.getInt(TAG_SUCCESS);
                String message = json.getString(TAG_MESSAGE);
                if (success == 1 && message.equals("Password changed successfully")) {
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
                    makeText(getActivity(),"Password changed successfully", Toast.LENGTH_SHORT).show();
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
        newChangePasswordProfile_et.setText("");
        oldChangePasswordProfile_et.setText("");
        confirmChangePasswordProfile_et.setText("");
    }

}
