package com.sales.tracking.salestracking.Fragment;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.sales.tracking.salestracking.Adapter.AttendanceAddapter;
import com.sales.tracking.salestracking.Adapter.AttendanceReportManagerAddapter;
import com.sales.tracking.salestracking.Bean.AttendanceManagerBean;
import com.sales.tracking.salestracking.Bean.ManagerReportBean;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

import static android.widget.Toast.makeText;


public class AttendanceReportFragment extends Fragment {

    View view;

    DatePickerDialog datePickerDialog;
    ProgressDialog pDialog;

    SharedPreferences sharedPref;
    String userIdPref, userTypePref, user_comidPref;
    String selectAssignTo, selectAssignToId;

    ArrayList<String> assignToUser;

    Map<String, String> assignToUserMap = new HashMap<>();

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    JSONParser jsonParser = new JSONParser();

    //Details

    @BindView(R.id.outtimeAttendanceReportDetail_tv)
    TextView outtimeAttendanceReportDetail_tv;

    @BindView(R.id.intimeAttendanceReportDetail_tv)
    TextView intimeAttendanceReportDetail_tv;

    @BindView(R.id.dateAttendanceReport_tv)
    TextView dateAttendanceReport_tv;

    @BindView(R.id.salesPersonAttendanceReportDetail_tv)
    TextView salesPersonAttendanceReportDetail_tv;

    @BindView(R.id.minusViewAttendanceReportDetail_iv)
    ImageView minusViewAttendanceReportDetail_iv ;

    //Layout

    @BindView(R.id.viewAttendanceReport_rv)
    RecyclerView viewAttendanceReport_rv;

    @BindView(R.id.viewAttendanceReportHeader_rl)
    RelativeLayout viewAttendanceReportHeader_rl;

    @BindView(R.id.viewAttendanceReport_cv)
    CardView viewAttendanceReport_cv;

    //Search Opertaion
    @BindView(R.id.employeeAdvanceSearchReport_sp)
    Spinner employeeAdvanceSearchReport_sp;

    @BindView(R.id.fromdateAdvanceSearchReportDetail_tv)
    TextView fromdateAdvanceSearchReportDetail_tv;

    @BindView(R.id.todateAdvanceSearchReport_tv)
    TextView todateAdvanceSearchReport_tv;

    @BindView(R.id.submitAdvanceSearchReport_btn)
    Button submitAdvanceSearchReport_btn;


    AttendanceReportManagerAddapter attendanceReportManagerAddapter;
    ArrayList<ManagerReportBean.Sp_Att_Advsearch> attendanceList = new ArrayList<>();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_attendance_report, container, false);
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

        viewAttendanceReportHeader_rl.setVisibility(View.GONE);
        viewAttendanceReport_cv.setVisibility(View.GONE);
        if (userTypePref.equals("Sales Manager")) {
            //getTargetViewRecyclerView();
            selectAssignTo();
        }

    }

    @OnClick(R.id.minusViewAttendanceReportDetail_iv)
    public void hideDetail(){
        viewAttendanceReportHeader_rl.setVisibility(View.VISIBLE);
        viewAttendanceReport_cv.setVisibility(View.GONE);


    }

    private void selectAssignTo(){
        try{
            if (Connectivity.isConnected(getActivity())) {
                String Url = ApiLink.ROOT_URL + ApiLink.Dashboard_SalesPerson;
                Map<String, String> map = new HashMap<>();
                map.put("users_undermanager","" );
                map.put("user_comid", user_comidPref);
                map.put("user_reporting_to", userIdPref);


                final GSONRequest<TaskMeetingBean> targetAssignToGsonRequest = new GSONRequest<TaskMeetingBean>(
                        Request.Method.POST,
                        Url,
                        TaskMeetingBean.class,map,
                        new com.android.volley.Response.Listener<TaskMeetingBean>() {
                            @Override
                            public void onResponse(TaskMeetingBean response) {
                                assignToUser.clear();
                                assignToUser.add("Assign To");
                                for(int i=0;i<response.getUsers_dd1().size();i++)
                                {
                                    assignToUser.add(response.getUsers_dd1().get(i).getUser_name());
                                    assignToUserMap.put(response.getUsers_dd1().get(i).getUser_id(), response.getUsers_dd1().get(i).getUser_name());
                                }
                            }
                        },
                        new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Utilities.serverError(getActivity());
                            }
                        });
                targetAssignToGsonRequest.setShouldCache(false);
                Utilities.getRequestQueue(getActivity()).add(targetAssignToGsonRequest);
            }
            assignToUser = new ArrayList<String>();
            assignToUser.clear();
            assignToUser.add("Assign To");
            ArrayAdapter<String> quotationLocationDataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_textview, assignToUser);
            quotationLocationDataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
            employeeAdvanceSearchReport_sp.setAdapter(quotationLocationDataAdapter);

        }catch (Exception e){
        }
    }

    @OnItemSelected(R.id.employeeAdvanceSearchReport_sp)
    public void assignToAddSelected(Spinner spinner, int position) {
        selectAssignTo = spinner.getSelectedItem().toString();
        for (Map.Entry<String, String> e : assignToUserMap.entrySet()) {
            Object key = e.getKey();
            Object value = e.getValue();
            if (value.equals(selectAssignTo)) {
                selectAssignToId = (String) key;
            }
        }
    }

    @OnClick(R.id.fromdateAdvanceSearchReportDetail_tv)
    public void startDate(){
        final Calendar calenderObj = Calendar.getInstance();
        int mYear = calenderObj.get(Calendar.YEAR);
        int mMonth = calenderObj.get(Calendar.MONTH);
        int mDay = calenderObj.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        fromdateAdvanceSearchReportDetail_tv.setText(year + "-" + (monthOfYear + 1)+ "-" + dayOfMonth);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    @OnClick(R.id.todateAdvanceSearchReport_tv)
    public void endDate(){
        final Calendar calenderObj = Calendar.getInstance();
        int mYear = calenderObj.get(Calendar.YEAR);
        int mMonth = calenderObj.get(Calendar.MONTH);
        int mDay = calenderObj.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        todateAdvanceSearchReport_tv.setText(year + "-" + (monthOfYear + 1)+ "-" + dayOfMonth);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    @OnClick(R.id.submitAdvanceSearchReport_btn)
    public void submitAdvanceSearch(){
            if (!selectAssignTo.equals("Assign To")) {
                     if (!(fromdateAdvanceSearchReportDetail_tv.getText().toString().equals(""))){
                        if (!(todateAdvanceSearchReport_tv.getText().toString().equals(""))) {
                            getAttendanceReportManagerRecyclerView();
                        }else{
                            Toast.makeText(getActivity(), "Please Select End Date", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getActivity(), "Please Select Start Date", Toast.LENGTH_SHORT).show();
                    }
            }else{
                Toast.makeText(getActivity(), "Please Select Assigned To", Toast.LENGTH_SHORT).show();
            }

    }

    private void getAttendanceReportManagerRecyclerView(){
        if (Connectivity.isConnected(getActivity())) {

            String fromd = fromdateAdvanceSearchReportDetail_tv.getText().toString();
            String tod = todateAdvanceSearchReport_tv.getText().toString();

            String Url = ApiLink.ROOT_URL + ApiLink.Attendance_Manager;
            Map<String, String> map = new HashMap<>();
            map.put("logged_manager_id", userIdPref);
            map.put("add", "");
            map.put("emp_id", selectAssignToId);
            map.put("startdate", fromd);
            map.put("enddate", tod);

            GSONRequest<ManagerReportBean> dashboardGsonRequest = new GSONRequest<ManagerReportBean>(
                    Request.Method.POST,
                    Url,
                    ManagerReportBean.class, map,
                    new com.android.volley.Response.Listener<ManagerReportBean>() {
                        @Override
                        public void onResponse(ManagerReportBean response) {
                            try{
                                if (response.getSp_att_advsearch().size()>0){
                                    viewAttendanceReportHeader_rl.setVisibility(View.VISIBLE);
                                    // for (int i = 0; i<=response.getSp_att_und_mgr().size();i++){
                                    attendanceList.clear();
                                    attendanceList.addAll(response.getSp_att_advsearch());

                                    attendanceReportManagerAddapter = new AttendanceReportManagerAddapter(getActivity(),response.getSp_att_advsearch(), AttendanceReportFragment.this);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                    attendanceDetail_rv.setLayoutManager(mLayoutManager);
                                    attendanceDetail_rv.setItemAnimator(new DefaultItemAnimator());
                                    attendanceDetail_rv.setAdapter(attendanceReportManagerAddapter);

                                }
                            }catch(Exception e){
                                e.printStackTrace();
                                Toast.makeText(getActivity(), "Api response Problem", Toast.LENGTH_SHORT).show();
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


    /*
      params.add(new BasicNameValuePair("logged_manager_id", logged_manager_id));
            params.add(new BasicNameValuePair("emp_id", emp_id));
            params.add(new BasicNameValuePair("startdate", startdate));
            params.add(new BasicNameValuePair("enddate", enddate));
            params.add(new BasicNameValuePair("add", "add"));




     */


    private void clearAll(){
        targetTypeAddTargetDetail_sp.setSelection(0);
        targetAssignedToAddTargetDetail_sp.setSelection(0);
        callCountAddTargetDetail_et.setText("");
        startDateAddTargetDetail_tv.setText("");
        endDateAddTargetDetail_tv.setText("");
    }
}
