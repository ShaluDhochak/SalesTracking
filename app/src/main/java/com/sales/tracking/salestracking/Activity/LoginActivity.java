package com.sales.tracking.salestracking.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
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
import com.sales.tracking.salestracking.Bean.LoginBean;
import com.sales.tracking.salestracking.R;
import com.sales.tracking.salestracking.Utility.ApiLink;
import com.sales.tracking.salestracking.Utility.Connectivity;
import com.sales.tracking.salestracking.Utility.GSONRequest;
import com.sales.tracking.salestracking.Utility.NetworkUtilities;
import com.sales.tracking.salestracking.Utility.SessionManagement;
import com.sales.tracking.salestracking.Utility.StringUtils;
import com.sales.tracking.salestracking.Utility.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
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

    ProgressDialog progressDialog;
    SessionManagement session;
    LoginBean loginBean;
    RequestQueue requestQueue;

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
        loginFunction();
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


/*

                        JSONArray array = jsonObject.getJSONArray("0");

                        for (int i=0;i<array.length();i++){
                            JSONObject jsonObject1 = array.getJSONObject(i);
                        }
*/

                    } catch (Exception e) {

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            }) {
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
}
