package com.sales.tracking.salestracking.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.sales.tracking.salestracking.Adapter.AttendanceAddapter;
import com.sales.tracking.salestracking.Adapter.ManagerHeadAttendanceAdapter;
import com.sales.tracking.salestracking.Adapter.SpAttendanceAdapter;
import com.sales.tracking.salestracking.Adapter.TodaysTaskSalesPersonAdapter;
import com.sales.tracking.salestracking.Bean.AttendanceManagerBean;
import com.sales.tracking.salestracking.Bean.DashboardSalesPersonBean;
import com.sales.tracking.salestracking.R;
import com.sales.tracking.salestracking.Utility.ApiLink;
import com.sales.tracking.salestracking.Utility.Connectivity;
import com.sales.tracking.salestracking.Utility.GSONRequest;
import com.sales.tracking.salestracking.Utility.Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AttendanceManagerFragment extends Fragment {

    View view;
    @BindView(R.id.attendanceDetail_rv)
    RecyclerView attendanceDetail_rv;

    @BindView(R.id.minusViewAttendance_iv)
    ImageView minusViewAttendance_iv;

    @BindView(R.id.viewAttendanceDetails_cv)
    CardView viewAttendanceDetails_cv;

    @BindView(R.id.dateViewAttendance_tv)
    TextView dateViewAttendance_tv;

    @BindView(R.id.salesPersonValueAttendanceDetail_tv)
    TextView salesPersonValueAttendanceDetail_tv;

    @BindView(R.id.inTimeAttendance_tv)
    TextView inTimeAttendance_tv;

    @BindView(R.id.inLocationAttendance_tv)
    TextView inLocationAttendance_tv;

    @BindView(R.id.outTimeAttendance_tv)
    TextView outTimeAttendance_tv;

    @BindView(R.id.outLocationAttendance_tv)
    TextView outLocationAttendance_tv;

    SharedPreferences sharedPref;
    String userIdPref, userTypePref;

    AttendanceAddapter attendanceAddapter;
    ArrayList<AttendanceManagerBean.Sp_Att_Und_Mgr> attendanceList = new ArrayList<>();
    SpAttendanceAdapter spAttendanceAddapter;
    ArrayList<AttendanceManagerBean.Single_sp_att> spAttendanceList = new ArrayList<>();
    ManagerHeadAttendanceAdapter managerHeadAttendanceAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_attendance_manager, container, false);
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
            getAttendanceRecyclerView();
        } else if (userTypePref.equals("Sales Executive")) {
            getSPAttendanceRecyclerView();
        } else if (userTypePref.equals("Manager Head")) {
            getManagerHeadRecyclerView();
        }

        viewAttendanceDetails_cv.setVisibility(View.GONE);
    }

    @OnClick(R.id.minusViewAttendance_iv)
    public void hideDetails() {
        viewAttendanceDetails_cv.setVisibility(View.GONE);
        attendanceDetail_rv.setVisibility(View.VISIBLE);
        if (userTypePref.equals("Sales Manager")) {
            getAttendanceRecyclerView();
        } else if (userTypePref.equals("Sales Executive")) {
            getSPAttendanceRecyclerView();
        } else if (userTypePref.equals("Manager Head")) {
            getManagerHeadRecyclerView();
        }
    }

    public void getAttendanceData(AttendanceManagerBean.Sp_Att_Und_Mgr bean) {
        viewAttendanceDetails_cv.setVisibility(View.VISIBLE);
        attendanceDetail_rv.setVisibility(View.GONE);

        String indate = bean.getAtten_in_datetime();
        String[] indate1 = indate.split(" ");

        String outDate = bean.getAtten_out_datetime();
        String[] outDate1 = outDate.split(" ");

        Location location = new Location(LocationManager.GPS_PROVIDER);
        if (!bean.getAtten_out_latitude().equals("")) {
            location.setLatitude(Double.parseDouble(bean.getAtten_out_latitude()));
        }
        if (!bean.getAtten_out_longitude().equals("")) {
            location.setLongitude(Double.parseDouble(bean.getAtten_out_longitude()));
        }
        // location.setTime(new Date().getTime()); //Set time as current Date

        dateViewAttendance_tv.setText(indate1[0]);
        salesPersonValueAttendanceDetail_tv.setText(bean.getUser_name());
        inTimeAttendance_tv.setText(convertIn12Hours(indate1[1]));
        inLocationAttendance_tv.setText(getCompleteAddressString(Double.parseDouble(bean.getAtten_in_latitude()), Double.parseDouble(bean.getAtten_in_longitude())));
        outTimeAttendance_tv.setText(convertIn12Hours(outDate1[1]));
        if (!bean.getAtten_out_latitude().equals("") && !bean.getAtten_out_longitude().equals("")) {
            outLocationAttendance_tv.setText(getCompleteAddressString(Double.parseDouble(bean.getAtten_out_latitude()), Double.parseDouble(bean.getAtten_out_longitude())));
        }

        //    outLocationAttendance_tv.setText(bean.getAtten_out_longitude());
    }

    public void getSpAttendanceData(AttendanceManagerBean.Single_sp_att bean) {
        viewAttendanceDetails_cv.setVisibility(View.VISIBLE);
        attendanceDetail_rv.setVisibility(View.GONE);

        String indate = bean.getAtten_in_datetime();
        String[] indate1 = indate.split(" ");

        String outDate = bean.getAtten_out_datetime();
        String[] outDate1 = outDate.split(" ");

        dateViewAttendance_tv.setText(indate1[0]);
        salesPersonValueAttendanceDetail_tv.setText(bean.getUser_name());

        if (!indate1[1].equals("00:00:00")) {
            inTimeAttendance_tv.setText(convertIn12Hours(indate1[1]));
        } else {
            inTimeAttendance_tv.setText("");
        }

        if (!bean.getAtten_in_latitude().equals("") && !bean.getAtten_in_longitude().equals("0")) {
            inLocationAttendance_tv.setText(getCompleteAddressString(Double.parseDouble(bean.getAtten_in_latitude()), Double.parseDouble(bean.getAtten_in_longitude())));
        } else {
            inLocationAttendance_tv.setText("NA");
        }
        //   inLocationAttendance_tv.setText(bean.getAtten_in_latitude());

        if (!outDate1[1].equals("00:00:00")) {
            outTimeAttendance_tv.setText(convertIn12Hours(outDate1[1]));
        } else {
            outTimeAttendance_tv.setText("NA");
        }

        if (!bean.getAtten_out_latitude().equals("") && !bean.getAtten_out_longitude().equals("")) {
            outLocationAttendance_tv.setText(getCompleteAddressString(Double.parseDouble(bean.getAtten_out_latitude()), Double.parseDouble(bean.getAtten_out_longitude())));
        } else {
            outLocationAttendance_tv.setText("NA");
        }

    }

    public void getManagerheadAttendanceData(AttendanceManagerBean.Sp_att_und_mgrhead bean) {
        viewAttendanceDetails_cv.setVisibility(View.VISIBLE);
        attendanceDetail_rv.setVisibility(View.GONE);

        String indate = bean.getAtten_in_datetime();
        String[] indate1 = indate.split(" ");

        String outDate = bean.getAtten_out_datetime();
        String[] outDate1 = outDate.split(" ");

        dateViewAttendance_tv.setText(indate1[0]);
        salesPersonValueAttendanceDetail_tv.setText(bean.getUser_name());
        inTimeAttendance_tv.setText(convertIn12Hours(indate1[1]));

        inLocationAttendance_tv.setText(getCompleteAddressString(Double.parseDouble(bean.getAtten_in_latitude()), Double.parseDouble(bean.getAtten_in_longitude())));
        //   inLocationAttendance_tv.setText(bean.getAtten_in_latitude());
        outTimeAttendance_tv.setText(convertIn12Hours(outDate1[1]));
        outLocationAttendance_tv.setText(getCompleteAddressString(Double.parseDouble(bean.getAtten_out_latitude()), Double.parseDouble(bean.getAtten_out_longitude())));

    }

    private void getAttendanceRecyclerView() {
        if (Connectivity.isConnected(getActivity())) {

            String Url = ApiLink.ROOT_URL + ApiLink.Attendance_Manager;
            Map<String, String> map = new HashMap<>();
            map.put("select", "");
            map.put("all", "");
            map.put("reporting_to", userIdPref);

            GSONRequest<AttendanceManagerBean> dashboardGsonRequest = new GSONRequest<AttendanceManagerBean>(
                    Request.Method.POST,
                    Url,
                    AttendanceManagerBean.class, map,
                    new com.android.volley.Response.Listener<AttendanceManagerBean>() {
                        @Override
                        public void onResponse(AttendanceManagerBean response) {
                            try {
                                if (response.getSp_att_und_mgr().size() > 0) {
                                    // for (int i = 0; i<=response.getSp_att_und_mgr().size();i++){
                                    attendanceList.clear();
                                    attendanceList.addAll(response.getSp_att_und_mgr());

                                    attendanceAddapter = new AttendanceAddapter(getActivity(), response.getSp_att_und_mgr(), AttendanceManagerFragment.this);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                    attendanceDetail_rv.setLayoutManager(mLayoutManager);
                                    attendanceDetail_rv.setItemAnimator(new DefaultItemAnimator());
                                    attendanceDetail_rv.setAdapter(attendanceAddapter);

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                //   Toast.makeText(getActivity(), "Api response Problem", Toast.LENGTH_SHORT).show();
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

    private String convertIn12Hours(String time) {

        String timeToDisplay = "";
        String[] timeArray = time.split(":");
        Integer hours = Integer.parseInt(timeArray[0]);

        if (hours > 12) {
            Integer diff = (24 - hours);
            timeToDisplay = (12 - diff) + ":" + timeArray[1] + " PM";

            //  timeToDisplay = (24 - hours) + ":" +  timeArray[1] + " PM";
        } else {
            timeToDisplay = timeArray[0] + ":" + timeArray[1] + " AM";
        }

        return timeToDisplay;
    }

    private void getSPAttendanceRecyclerView() {
        if (Connectivity.isConnected(getActivity())) {

            String Url = ApiLink.ROOT_URL + ApiLink.Attendance_Manager;
            Map<String, String> map = new HashMap<>();
            map.put("select", "");
            map.put("atten_uid", userIdPref);

            GSONRequest<AttendanceManagerBean> dashboardGsonRequest = new GSONRequest<AttendanceManagerBean>(
                    Request.Method.POST,
                    Url,
                    AttendanceManagerBean.class, map,
                    new com.android.volley.Response.Listener<AttendanceManagerBean>() {
                        @Override
                        public void onResponse(AttendanceManagerBean response) {
                            try {
                                if (response.getSingle_sp_att().size() > 0) {
                                    spAttendanceList.clear();
                                    spAttendanceList.addAll(response.getSingle_sp_att());

                                    spAttendanceAddapter = new SpAttendanceAdapter(getActivity(), response.getSingle_sp_att(), AttendanceManagerFragment.this);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                    attendanceDetail_rv.setLayoutManager(mLayoutManager);
                                    attendanceDetail_rv.setItemAnimator(new DefaultItemAnimator());
                                    attendanceDetail_rv.setAdapter(spAttendanceAddapter);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                //     Toast.makeText(getActivity(), "Api response Problem", Toast.LENGTH_SHORT).show();
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

    private void getManagerHeadRecyclerView() {
        if (Connectivity.isConnected(getActivity())) {

            String Url = ApiLink.ROOT_URL + ApiLink.Attendance_Manager;
            Map<String, String> map = new HashMap<>();
            map.put("select", "");
            map.put("all", "");
            map.put("user_reporting_to", userIdPref);

            GSONRequest<AttendanceManagerBean> dashboardGsonRequest = new GSONRequest<AttendanceManagerBean>(
                    Request.Method.POST,
                    Url,
                    AttendanceManagerBean.class, map,
                    new com.android.volley.Response.Listener<AttendanceManagerBean>() {
                        @Override
                        public void onResponse(AttendanceManagerBean response) {
                            try {
                                if (response.getSp_att_und_mgrhead().size() > 0) {

                                    managerHeadAttendanceAdapter = new ManagerHeadAttendanceAdapter(getActivity(), response.getSp_att_und_mgrhead(), AttendanceManagerFragment.this);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                    attendanceDetail_rv.setLayoutManager(mLayoutManager);
                                    attendanceDetail_rv.setItemAnimator(new DefaultItemAnimator());
                                    attendanceDetail_rv.setAdapter(managerHeadAttendanceAdapter);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                //      Toast.makeText(getActivity(), "Api response Problem", Toast.LENGTH_SHORT).show();
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

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strAdd;
    }

}
