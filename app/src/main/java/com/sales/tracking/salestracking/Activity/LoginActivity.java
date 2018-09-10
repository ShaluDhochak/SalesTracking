package com.sales.tracking.salestracking.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sales.tracking.salestracking.Adapter.ViewVisitTaskSpAdapter;
import com.sales.tracking.salestracking.Bean.VisitTaskSpBean;
import com.sales.tracking.salestracking.Fragment.ViewVisitTaskSpFragment;
import com.sales.tracking.salestracking.R;
import com.sales.tracking.salestracking.Utility.ApiLink;
import com.sales.tracking.salestracking.Utility.Connectivity;
import com.sales.tracking.salestracking.Utility.GSONRequest;
import com.sales.tracking.salestracking.Utility.NetworkUtilities;
import com.sales.tracking.salestracking.Utility.SessionManagement;
import com.sales.tracking.salestracking.Utility.StringUtils;
import com.sales.tracking.salestracking.Utility.Utilities;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.userid_et)
    EditText userid_et;

    @BindView(R.id.password_et)
    EditText password_et;

    @BindView(R.id.login_btn)
    TextView login_btn;

    @BindView(R.id.rememberMe_cb)
    CheckBox rememberMe_cb;

    ProgressDialog progressDialog;
    SessionManagement session;
    RequestQueue requestQueue;

    String rememberMe_token = "0";

    String user_id, user_password,userId, userName, userEmail, userMobile, userComId, userdesId, userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initialiseUI();
    }

    private void initialiseUI() {
        session = new SessionManagement(getApplicationContext());

        requestQueue = Volley.newRequestQueue(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading....");
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @OnClick(R.id.login_btn)
    public void login() {

        user_id = userid_et.getText().toString();
        user_password = password_et.getText().toString();

        if(userid_et.getText().toString().trim().length() > 0 && password_et.getText().toString().trim().length() > 0) {
            if(StringUtils.isEmailValid(userid_et.getText().toString())) {
                if (NetworkUtilities.isInternet(this)) {
                    loginFunction();
                }else
                    StringUtils.internetError(this);
            }else{
                Toast.makeText(this,"Enter valid Email",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Email Id or Password cannot be empty.", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.rememberMe_cb)
    public void rememberMe(){
        if (rememberMe_token.equals("0")){
            rememberMe_token= "1";
            rememberMe_cb.setChecked(true);
        }else if (rememberMe_token.equals("1")){
            rememberMe_token= "0";
            rememberMe_cb.setChecked(false);
        }
    }

    public void sharedPreferenceData(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString("user_id", userId);
        edit.putString("user_name", userName);
        edit.putString("user_pass", password_et.getText().toString());
        edit.putString("user_email", userEmail);
        edit.putString("user_mobile", userMobile);
        edit.putString("user_com_id", userComId);
        edit.putString("user_des_id", userdesId);
        edit.putString("user_type", userType);
        edit.commit();
    }

    private void loginFunction() {
        try {
            String URL = ApiLink.ROOT_URL + ApiLink.LOGIN;
            final String requestBody = "user_email=" + userid_et.getText().toString() + "&user_pass=" +
                    password_et.getText().toString() + "&check_pwd=";

            //  final String requestBody = "user_email=azmanager@gmail.com&user_pass=123&check_pwd=";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject userJson = jsonObject.getJSONObject("0");
                            Log.i("User Id", userJson.getString("user_id"));
                            Log.i("User Name", userJson.getString("user_name"));
                            Log.i("User Email", userJson.getString("user_email"));

                            userId = userJson.getString("user_id");
                            userName = userJson.getString("user_name");
                            userEmail = userJson.getString("user_email");
                            userMobile = userJson.getString("user_mobile");
                            userComId = userJson.getString("user_comid");
                            userdesId = userJson.getString("user_desiid");
                            userType = userJson.getString("user_type");
                            sharedPreferenceData();

                            if (rememberMe_token.equals("1")) {
                                session.createLoginSession(user_id, user_password);
                            }
                            Intent loginIntent = new Intent(LoginActivity.this, NavigationDrawerActivity.class);
                            startActivity(loginIntent);

                        /*
                        JSONArray array = jsonObject.getJSONArray("0");

                        for (int i=0;i<array.length();i++){
                            JSONObject jsonObject1 = array.getJSONObject(i);
                        }
                        */
                    } catch (Exception e) {
                        Toast.makeText(LoginActivity.this, "Something went Wrong!!, Please login again with correct credentials..", Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            })
            {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/x-www-form-urlencoded");
                    return params;
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    try {

                        if (response != null) {
                            byte[] responseData = response.data;
                            responseString = new String(responseData, "UTF-8");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                .setMessage("Are you sure want to Exit?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        moveTaskToBack(true);
                    }
                }).setNegativeButton("no", null).show();
    }

}
