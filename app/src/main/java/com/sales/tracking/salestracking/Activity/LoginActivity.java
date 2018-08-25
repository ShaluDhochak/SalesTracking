package com.sales.tracking.salestracking.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.sales.tracking.salestracking.Bean.LoginBean;
import com.sales.tracking.salestracking.R;
import com.sales.tracking.salestracking.Utility.ApiLink;
import com.sales.tracking.salestracking.Utility.Connectivity;
import com.sales.tracking.salestracking.Utility.GSONRequest;
import com.sales.tracking.salestracking.Utility.NetworkUtilities;
import com.sales.tracking.salestracking.Utility.SessionManagement;
import com.sales.tracking.salestracking.Utility.StringUtils;
import com.sales.tracking.salestracking.Utility.Utilities;

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
    Button login_btn;

    ProgressDialog progressDialog;
    SessionManagement session;
    LoginBean loginBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initialiseUI();
    }

    private void initialiseUI(){
        session = new SessionManagement(getApplicationContext());

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading....");
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @OnClick(R.id.login_btn)
    public void login()  {
        if(userid_et.getText().toString().trim().length() > 0 && password_et.getText().toString().trim().length() > 0)
        {
            if(StringUtils.isEmailValid(userid_et.getText().toString())) {
                if (NetworkUtilities.isInternet(this)) {
                    getLoginFunction();
                } else
                    StringUtils.internetError(this);
            }else{
                Toast.makeText(this,"Enter valid Email",Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "Email Id or Password cannot be empty.", Toast.LENGTH_SHORT).show();
        }
    }

   /* private void getLoginFunction() {
        if (Connectivity.isConnected(LoginActivity.this)) {

            try {
                progressDialog.show();
                String name = userid_et.getText().toString().trim();
                String password = password_et.getText().toString().trim();
              //  session.createLoginSession(name, password);

                final JSONObject jsonObject = new JSONObject();
                jsonObject.put("user_email", name);
                jsonObject.put("user_pass", password);
                jsonObject.put("check_pwd", "");


                String Url = ApiLink.ROOT_URL + ApiLink.LOGIN;

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Url, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (!(response.equals(""))) {try {
                            Toast.makeText(LoginActivity.this, "LoginSuccessfully done", Toast.LENGTH_SHORT).show();
                            Toast.makeText(LoginActivity.this, response.getString("user_name"), Toast.LENGTH_SHORT).show();
                            // saveUserInfo(emailId,password,response, response.getJSONObject("result"), SharedPreferenceManager.getInstance(context));
                        } catch (Exception e) {
                            Toast.makeText(LoginActivity.this, "njf", Toast.LENGTH_SHORT).show();
                        }
                        }else{
                            Toast.makeText(LoginActivity.this, "No Output", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                       // view.loginSuccess();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                       // view.loginFailure("Please check Email and Password");
                    }
                });

                Volley.newRequestQueue(LoginActivity.this).add(jsonObjectRequest);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
            Utilities.internetConnectionError(LoginActivity.this);
    }
    */


    private void getLoginFunction() {
        if (Connectivity.isConnected(LoginActivity.this)) {

                progressDialog.show();
                String name = userid_et.getText().toString().trim();
                String password = password_et.getText().toString().trim();
                //  session.createLoginSession(name, password);


                String Url = ApiLink.ROOT_URL + ApiLink.LOGIN;

                //   String URL = "http://vistacars.in/lms/api/login_user";
                Map<String, String> map = new HashMap<>();
                map.put("user_email", name);
                map.put("user_pass", password);
                map.put("check_pwd", "");

                GSONRequest<LoginBean> loginGsonRequest = new GSONRequest<LoginBean>(
                        Request.Method.POST,
                        Url,
                        LoginBean.class, map,
                        new com.android.volley.Response.Listener<LoginBean>() {
                            @Override
                            public void onResponse(LoginBean res) {
                                if (!(res.equals(""))) {
                                    loginBean = res;
                                    String UserId = loginBean.getUser().get(0).getUser_id().toString();
                                    Toast.makeText(LoginActivity.this, UserId, Toast.LENGTH_SHORT).show();
                                    //     sharedPreferenceData();
                                    startActivity(new Intent(LoginActivity.this, NavigationDrawerActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(LoginActivity.this, "Enter valid credentials", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                Utilities.serverError(LoginActivity.this);
                            }
                        });
                loginGsonRequest.setShouldCache(false);
                Utilities.getRequestQueue(LoginActivity.this).add(loginGsonRequest);

        }
        else
            Utilities.internetConnectionError(LoginActivity.this);

        }

}
