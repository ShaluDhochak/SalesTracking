package com.sales.tracking.salestracking.Fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sales.tracking.salestracking.Adapter.AttendanceAddapter;
import com.sales.tracking.salestracking.Adapter.VisitTaskMeetingAdapter;
import com.sales.tracking.salestracking.Adapter.VisitTaskMeetingManagerHeadAdapter;
import com.sales.tracking.salestracking.Bean.AttendanceManagerBean;
import com.sales.tracking.salestracking.Bean.PurposeBean;
import com.sales.tracking.salestracking.Bean.TaskMeetingBean;
import com.sales.tracking.salestracking.R;
import com.sales.tracking.salestracking.Utility.ApiLink;
import com.sales.tracking.salestracking.Utility.Connectivity;
import com.sales.tracking.salestracking.Utility.GSONRequest;
import com.sales.tracking.salestracking.Utility.JSONParser;
import com.sales.tracking.salestracking.Utility.Utilities;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

import static android.widget.Toast.makeText;


public class ViewMeetingTaskManagerFragment extends Fragment {

    View view;

    @BindView(R.id.viewMeetingTaskManager_rv)
    RecyclerView viewMeetingTaskManager_rv;

    @BindView(R.id.salesViewMeetingTaskHeader_rl)
    RelativeLayout salesViewMeetingTaskHeader_rl;

    //View details
    @BindView(R.id.viewMeetingTaskeDetails_cv)    //cardView for View
            CardView viewMeetingTaskeDetails_cv;

    @BindView(R.id.timeValueMeetingTaskDetail_tv)
    TextView timeValueMeetingTaskDetail_tv;

    @BindView(R.id.dateViewMeetingTask_tv)
    TextView dateViewMeetingTask_tv;

    @BindView(R.id.assignToViewMeetingTask_tv)
    TextView assignToViewMeetingTask_tv;

    @BindView(R.id.clientNameMeetingTask_tv)
    TextView clientNameMeetingTask_tv;

    @BindView(R.id.purposeViewMeetingTask_tv)
    TextView purposeViewMeetingTask_tv;

    @BindView(R.id.descriptionViewMeetingTask_tv)
    TextView descriptionViewMeetingTask_tv;

    @BindView(R.id.statusViewMeetingTask_tv)
    TextView statusViewMeetingTask_tv;

    @BindView(R.id.addressViewMeetingTask_tv)
    TextView addressViewMeetingTask_tv;

    @BindView(R.id.minusVisitTaskMeetingDetail_iv)
    ImageView minusVisitTaskMeetingDetail_iv;

    //Edit Detail
    @BindView(R.id.editViewMeetingTaskeDetails_cv)  //cardview for edit text
    CardView editViewMeetingTaskeDetails_cv;

    @BindView(R.id.editOkButtonVisitTaskMeetingDetail_tv)   //submit button
    Button editOkButtonVisitTaskMeetingDetail_tv;

    @BindView(R.id.clientNameEditMeetingTask_sp)
    Spinner clientNameEditMeetingTask_sp;

    @BindView(R.id.assignToEditViewMeetingTask_sp)
    Spinner assignToEditViewMeetingTask_sp;

    @BindView(R.id.purposeEditViewMeetingTask_sp)
    Spinner purposeEditViewMeetingTask_sp;

    @BindView(R.id.addressEditViewMeetingTask_et)
    EditText addressEditViewMeetingTask_et;

    @BindView(R.id.descriptionEditViewMeetingTask_et)
    EditText descriptionEditViewMeetingTask_et;

    @BindView(R.id.statusEditViewMeetingTask_sp)
    Spinner statusEditViewMeetingTask_sp;

    @BindView(R.id.dateEditViewMeetingTask_tv)
    TextView dateEditViewMeetingTask_tv;

    @BindView(R.id.descriptionEditViewMeetingTask_rl)
    RelativeLayout descriptionEditViewMeetingTask_rl;

    @BindView(R.id.separatorBelowAddressEditViewMeetingTask)
    View separatorBelowAddressEditViewMeetingTask;

    @BindView(R.id.timeEditViewValueMeetingTaskDetail_et)
    TextView timeEditViewValueMeetingTaskDetail_et;

    @BindView(R.id.photoViewMeetingTask_iv)
    ImageView photoViewMeetingTask_iv;

    String selectStatus, selectStatusId, selectPurpose, selectPurposeId, selectclientName, selectClientNameId, selectAssignTo, selectAssignToId;
    String currentuserName, currentVisitIdEdit, currentPurposeName;

    ArrayList<String> assignToUser;
    ArrayList<String> clientNameCompany;
    ArrayList<String> purposesNameCompany;

    Map<String, String> purposeNameMap = new HashMap<>();
    Map<String, String> clientNameMap = new HashMap<>();
    Map<String, String> assignToUserMap = new HashMap<>();


    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;

    SharedPreferences sharedPref;
    String userIdPref, userTypePref, user_comidPref;
    String client_id, purpose_id, assignedTo_id;
    int editClient, purposeEdit, editAssignedTo;

    ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";


    VisitTaskMeetingAdapter visitTaskMeetingAdapter;

    VisitTaskMeetingManagerHeadAdapter visitTaskMeetingManagerHeadAdapter;
    ArrayList<TaskMeetingBean.All_Meetings_Mgr> spAttendanceList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_meeting_task_manager, container, false);
        ButterKnife.bind(this, view);
        initialiseUI();

        if (userTypePref.equals("Sales Manager")) {
            getVisitTaskMeetingRecyclerView();
        }else if (userTypePref.equals("Manager Head")){
            getVisitTaskManagerHeadMeetingRecyclerView();
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initialiseUI();
        if (userTypePref.equals("Sales Manager")) {
            getVisitTaskMeetingRecyclerView();
        }else if (userTypePref.equals("Manager Head")){
            getVisitTaskManagerHeadMeetingRecyclerView();
        }
    }

    private void initialiseUI() {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        userIdPref = sharedPref.getString("user_id", "");
        userTypePref = sharedPref.getString("user_type", "");
        user_comidPref = sharedPref.getString("user_com_id", "");

        if (userTypePref.equals("Sales Manager")) {
            getVisitTaskMeetingRecyclerView();
        }else if (userTypePref.equals("Manager Head")){
            getVisitTaskManagerHeadMeetingRecyclerView();
        }

        viewMeetingTaskeDetails_cv.setVisibility(View.GONE);
        editViewMeetingTaskeDetails_cv.setVisibility(View.GONE);
    }

    private void getVisitTaskMeetingRecyclerView() {
        try {
            if (Connectivity.isConnected(getActivity())) {

                String Url = ApiLink.ROOT_URL + ApiLink.Dashboard_SalesPerson;
                Map<String, String> map = new HashMap<>();
                map.put("select", "");
                map.put("m_all", "");
                map.put("visit_assignedby", userIdPref);

                GSONRequest<TaskMeetingBean> dashboardGsonRequest = new GSONRequest<TaskMeetingBean>(
                        Request.Method.POST,
                        Url,
                        TaskMeetingBean.class, map,
                        new com.android.volley.Response.Listener<TaskMeetingBean>() {
                            @Override
                            public void onResponse(TaskMeetingBean response) {
                                try {
                                    if (response.getAll_meetings_mgr().size() > 0) {
                                        // for (int i = 0; i<=response.getSp_att_und_mgr().size();i++){
                                        spAttendanceList.clear();
                                        spAttendanceList.addAll(response.getAll_meetings_mgr());

                                        visitTaskMeetingAdapter = new VisitTaskMeetingAdapter(getActivity(), response.getAll_meetings_mgr(), ViewMeetingTaskManagerFragment.this);
                                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                        viewMeetingTaskManager_rv.setLayoutManager(mLayoutManager);
                                        viewMeetingTaskManager_rv.setItemAnimator(new DefaultItemAnimator());
                                        viewMeetingTaskManager_rv.setAdapter(visitTaskMeetingAdapter);
                                        viewMeetingTaskManager_rv.setNestedScrollingEnabled(false);


                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                  //  Toast.makeText(getActivity(), "Api response Problem", Toast.LENGTH_SHORT).show();
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
        }catch(Exception e){

        }
    }

    private void getVisitTaskManagerHeadMeetingRecyclerView(){
        try {
            if (Connectivity.isConnected(getActivity())) {

                String Url = ApiLink.ROOT_URL + ApiLink.Dashboard_SalesPerson;
                Map<String, String> map = new HashMap<>();
                map.put("select", "");
                map.put("mh_all", "");
                map.put("managerhead_id", userIdPref);

                GSONRequest<TaskMeetingBean> dashboardGsonRequest = new GSONRequest<TaskMeetingBean>(
                        Request.Method.POST,
                        Url,
                        TaskMeetingBean.class, map,
                        new com.android.volley.Response.Listener<TaskMeetingBean>() {
                            @Override
                            public void onResponse(TaskMeetingBean response) {
                                try {
                                    if (response.getAll_meetings_mgr().size() > 0) {

                                        visitTaskMeetingManagerHeadAdapter = new VisitTaskMeetingManagerHeadAdapter(getActivity(), response.getAll_meetings_mgr(), ViewMeetingTaskManagerFragment.this);
                                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                        viewMeetingTaskManager_rv.setLayoutManager(mLayoutManager);
                                        viewMeetingTaskManager_rv.setItemAnimator(new DefaultItemAnimator());
                                        viewMeetingTaskManager_rv.setAdapter(visitTaskMeetingManagerHeadAdapter);
                                        viewMeetingTaskManager_rv.setNestedScrollingEnabled(false);

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    //  Toast.makeText(getActivity(), "Api response Problem", Toast.LENGTH_SHORT).show();
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
        }catch(Exception e){
        }
    }

    @OnClick(R.id.minusVisitTaskMeetingDetail_iv)
    public void hideDetails() {
        viewMeetingTaskeDetails_cv.setVisibility(View.GONE);
        salesViewMeetingTaskHeader_rl.setVisibility(View.VISIBLE);
        editViewMeetingTaskeDetails_cv.setVisibility(View.GONE);

        if (userTypePref.equals("Sales Manager")) {
            getVisitTaskMeetingRecyclerView();
        }if (userTypePref.equals("Manager Head")){
            getVisitTaskManagerHeadMeetingRecyclerView();
        }
    }

    public void getViewMeetingTask(TaskMeetingBean.All_Meetings_Mgr bean) {
        viewMeetingTaskeDetails_cv.setVisibility(View.VISIBLE);
        salesViewMeetingTaskHeader_rl.setVisibility(View.GONE);
        editViewMeetingTaskeDetails_cv.setVisibility(View.GONE);

        String indate = bean.getVisit_datetime();
        String[] indate1 = indate.split(" ");

        dateViewMeetingTask_tv.setText(indate1[0]);
        timeValueMeetingTaskDetail_tv.setText(convertIn12Hours(indate1[1]));

        assignToViewMeetingTask_tv.setText(bean.getUser_name());
        clientNameMeetingTask_tv.setText(bean.getLead_company());
        purposeViewMeetingTask_tv.setText(bean.getPurpose_name());
        descriptionViewMeetingTask_tv.setText(bean.getVisit_comments());
        addressViewMeetingTask_tv.setText(bean.getVisit_address());
        statusViewMeetingTask_tv.setText(bean.getVisit_status());

        if (!bean.getVisit_photo().equals("")) {
            Picasso.get().load(ApiLink.IMAGE_BASE_URL + bean.getVisit_photo()).into(photoViewMeetingTask_iv);
        } else {
            photoViewMeetingTask_iv.setImageDrawable(getResources().getDrawable(R.drawable.default_img));
        }
    }

    public void getDeleteMeetingTask(TaskMeetingBean.All_Meetings_Mgr bean) {

        currentVisitIdEdit = bean.getVisit_id().toString();
        new CreateDeleteMeetingTask().execute();

       // getVisitTaskMeetingRecyclerView();
         if (userTypePref.equals("Sales Manager")) {
             getVisitTaskMeetingRecyclerView();
         }else if (userTypePref.equals("Manager Head")){
             getVisitTaskManagerHeadMeetingRecyclerView();
         }
        viewMeetingTaskeDetails_cv.setVisibility(View.GONE);
        salesViewMeetingTaskHeader_rl.setVisibility(View.VISIBLE);
        editViewMeetingTaskeDetails_cv.setVisibility(View.GONE);
    }

    @OnClick(R.id.minusEditViewVisitTaskMeetingDetail_iv)
    public void hideEditDetails() {
        viewMeetingTaskeDetails_cv.setVisibility(View.GONE);
        salesViewMeetingTaskHeader_rl.setVisibility(View.VISIBLE);
        editViewMeetingTaskeDetails_cv.setVisibility(View.GONE);

        if (userTypePref.equals("Sales Manager")) {
            getVisitTaskMeetingRecyclerView();
        }else if (userTypePref.equals("Manager Head")){
            getVisitTaskManagerHeadMeetingRecyclerView();
        }
    }

    public void getEditViewMeetingTask(TaskMeetingBean.All_Meetings_Mgr bean) {

        currentuserName = bean.getUser_name().toString();
        currentVisitIdEdit = bean.getVisit_id().toString();

        client_id = bean.getLead_company().toString();
        purpose_id = bean.getPurpose_name().toString();
        assignedTo_id = bean.getUser_name().toString();
        selectStatus();
        selectAssignTo();
        selectClientName();
        selectPurpose();

        viewMeetingTaskeDetails_cv.setVisibility(View.GONE);
        salesViewMeetingTaskHeader_rl.setVisibility(View.GONE);
        editViewMeetingTaskeDetails_cv.setVisibility(View.VISIBLE);
        descriptionEditViewMeetingTask_rl.setVisibility(View.GONE);
        separatorBelowAddressEditViewMeetingTask.setVisibility(View.GONE);

        String indate = bean.getVisit_datetime();
        String[] indate1 = indate.split(" ");

        dateEditViewMeetingTask_tv.setText(indate1[0]);
        timeEditViewValueMeetingTaskDetail_et.setText(convertIn12Hours(indate1[1]));
        addressEditViewMeetingTask_et.setText(bean.getVisit_address());

    }

    @OnClick(R.id.dateEditViewMeetingTask_tv)
    public void selectDate() {
        final Calendar calenderObj = Calendar.getInstance();
        int mYear = calenderObj.get(Calendar.YEAR);
        int mMonth = calenderObj.get(Calendar.MONTH);
        int mDay = calenderObj.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        dateEditViewMeetingTask_tv.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    @OnClick(R.id.timeEditViewValueMeetingTaskDetail_et)
    public void selectTime() {
        final String time;
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss a");
        time = simpleDateFormat.format(mcurrentTime.getTime());
        timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                if (selectedMinute < 10){
                    timeEditViewValueMeetingTaskDetail_et.setText(selectedHour % 12 + ":0" + selectedMinute + ((selectedHour >= 12) ? " PM" : " AM"));
                }else {
                    timeEditViewValueMeetingTaskDetail_et.setText(selectedHour % 12 + ":" + selectedMinute + ((selectedHour >= 12) ? " PM" : " AM"));
                }
            }
        }, hour, minute, true);//Yes 24 hour time
        timePickerDialog.show();
    }

    @OnClick(R.id.editOkButtonVisitTaskMeetingDetail_tv)
    public void okEditBtn() {

        if (!selectclientName.equals("Client Name")) {
            if (!selectAssignTo.equals("Assign To")) {
                if (!selectPurpose.equals("Purpose")) {
                    new CreateEditMeetingTask().execute();
                } else {
                    Toast.makeText(getActivity(), "Please Select Purpose", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Please Select Assign To", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "Please Select Client Name", Toast.LENGTH_SHORT).show();
        }

    }

    private void selectAssignTo() {
        try {
            if (Connectivity.isConnected(getActivity())) {
                String Url = ApiLink.ROOT_URL + ApiLink.Dashboard_SalesPerson;
                Map<String, String> map = new HashMap<>();
                map.put("users", "");
                map.put("user_comid", user_comidPref);

                final GSONRequest<TaskMeetingBean> locationSpinnerGsonRequest = new GSONRequest<TaskMeetingBean>(
                        Request.Method.POST,
                        Url,
                        TaskMeetingBean.class, map,
                        new com.android.volley.Response.Listener<TaskMeetingBean>() {
                            @Override
                            public void onResponse(TaskMeetingBean response) {
                                assignToUser.clear();
                                assignToUser.add("Assign To");
                                for (int i = 0; i < response.getUsers_dd().size(); i++) {
                                    assignToUser.add(response.getUsers_dd().get(i).getUser_name());
                                    assignToUserMap.put(response.getUsers_dd().get(i).getUser_id(), response.getUsers_dd().get(i).getUser_name());
                                    String myString = assignedTo_id; //the value you want the position for
                                    if (myString.equals(response.getUsers_dd().get(i).getUser_name())) {
                                        editAssignedTo = i + 1;
                                        assignToEditViewMeetingTask_sp.setSelection(editAssignedTo);
                                    }
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
            assignToUser = new ArrayList<String>();
            assignToUser.clear();
            assignToUser.add("Assign To");
            ArrayAdapter<String> quotationLocationDataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_textview, assignToUser);
            quotationLocationDataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
            assignToEditViewMeetingTask_sp.setAdapter(quotationLocationDataAdapter);

        } catch (Exception e) {
        }
    }

    @OnItemSelected(R.id.assignToEditViewMeetingTask_sp)
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

    private void selectClientName() {
        try {
            if (Connectivity.isConnected(getActivity())) {
                String Url = ApiLink.ROOT_URL + ApiLink.Dashboard_SalesPerson;
                Map<String, String> map = new HashMap<>();
                map.put("clients", "");
                map.put("lead_comid", user_comidPref);

                final GSONRequest<TaskMeetingBean> clientSpinnerGsonRequest = new GSONRequest<TaskMeetingBean>(
                        Request.Method.POST,
                        Url,
                        TaskMeetingBean.class, map,
                        new com.android.volley.Response.Listener<TaskMeetingBean>() {
                            @Override
                            public void onResponse(TaskMeetingBean response) {
                                clientNameCompany.clear();
                                clientNameCompany.add("Client Name");
                                for (int i = 0; i < response.getClients_dd().size(); i++) {
                                    clientNameCompany.add(response.getClients_dd().get(i).getLead_company());
                                    clientNameMap.put(response.getClients_dd().get(i).getLead_id(), response.getClients_dd().get(i).getLead_company());
                                    String myString = client_id; //the value you want the position for
                                    if (myString.equals(response.getClients_dd().get(i).getLead_company())) {
                                        editClient = i + 1;
                                        clientNameEditMeetingTask_sp.setSelection(editClient);
                                    }
                                }
                            }
                        },
                        new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Utilities.serverError(getActivity());
                            }
                        });
                clientSpinnerGsonRequest.setShouldCache(false);
                Utilities.getRequestQueue(getActivity()).add(clientSpinnerGsonRequest);
            }
            clientNameCompany = new ArrayList<String>();
            clientNameCompany.clear();
            clientNameCompany.add("Client Name");
            ArrayAdapter<String> quotationLocationDataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_textview, clientNameCompany);
            quotationLocationDataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
            clientNameEditMeetingTask_sp.setAdapter(quotationLocationDataAdapter);
        } catch (Exception e) {
        }
    }

    @OnItemSelected(R.id.clientNameEditMeetingTask_sp)
    public void clientAddSelected(Spinner spinner, int position) {
        selectclientName = spinner.getSelectedItem().toString();
        for (Map.Entry<String, String> e : clientNameMap.entrySet()) {
            Object key = e.getKey();
            Object value = e.getValue();
            if (value.equals(selectclientName)) {
                selectClientNameId = (String) key;
            }
        }
    }

    private void selectPurpose() {
        try {
            if (Connectivity.isConnected(getActivity())) {
                String Url = ApiLink.ROOT_URL + ApiLink.Dashboard_SalesPerson;
                Map<String, String> map = new HashMap<>();
                map.put("purposes", "");
                map.put("user_comid",user_comidPref);


                final GSONRequest<PurposeBean> clientSpinnerGsonRequest = new GSONRequest<PurposeBean>(
                        Request.Method.POST,
                        Url,
                        PurposeBean.class, map,
                        new com.android.volley.Response.Listener<PurposeBean>() {
                            @Override
                            public void onResponse(PurposeBean response) {
                                purposesNameCompany.clear();
                                purposesNameCompany.add("Purpose");
                                for (int i = 0; i < response.getPurpose_dd().size(); i++) {
                                    purposesNameCompany.add(response.getPurpose_dd().get(i).getPurpose_name());
                                    purposeNameMap.put(response.getPurpose_dd().get(i).getPurpose_id(), response.getPurpose_dd().get(i).getPurpose_name());
                                    String myString = purpose_id; //the value you want the position for
                                    if (myString.equals(response.getPurpose_dd().get(i).getPurpose_name())) {
                                        purposeEdit = i + 1;
                                        purposeEditViewMeetingTask_sp.setSelection(purposeEdit);
                                    }
                                }
                            }
                        },
                        new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Utilities.serverError(getActivity());
                            }
                        });
                clientSpinnerGsonRequest.setShouldCache(false);
                Utilities.getRequestQueue(getActivity()).add(clientSpinnerGsonRequest);
            }
            purposesNameCompany = new ArrayList<String>();
            purposesNameCompany.clear();
            purposesNameCompany.add("Purpose");
            ArrayAdapter<String> quotationLocationDataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_textview, purposesNameCompany);
            quotationLocationDataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
            purposeEditViewMeetingTask_sp.setAdapter(quotationLocationDataAdapter);

        } catch (Exception e) {
        }
    }

    @OnItemSelected(R.id.purposeEditViewMeetingTask_sp)
    public void purposeSelected(Spinner spinner, int position) {
        selectPurpose = spinner.getSelectedItem().toString();
        for (Map.Entry<String, String> e : purposeNameMap.entrySet()) {
            Object key = e.getKey();
            Object value = e.getValue();
            if (value.equals(selectPurpose)) {
                selectPurposeId = (String) key;
            }
        }
    }

    private void selectStatus() {
        List<String> statusSpinner = new ArrayList<String>();
        statusSpinner.add("Status");
        statusSpinner.add("Pending");
        statusSpinner.add("Done");

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_textview, statusSpinner);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        statusEditViewMeetingTask_sp.setAdapter(statusAdapter);

    }

    @OnItemSelected(R.id.statusEditViewMeetingTask_sp)
    public void statusSelected(Spinner spinner, int position) {
        selectStatus = spinner.getSelectedItem().toString();
        if (selectStatus.equals("Status")) {
            selectStatusId = "";
        } else if (selectStatus.equals("Pending")) {
            selectStatusId = selectStatus;
        } else if (selectStatus.equals("Done")) {
            selectStatusId = selectStatus;
        }
    }

    public class CreateEditMeetingTask extends AsyncTask<String, JSONObject, JSONObject> {
        String visit_leadid, visit_purposeid, visit_assignedby, visit_time, visit_date;
        String visit_uid, visit_address, visit_id;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            visit_address = addressEditViewMeetingTask_et.getText().toString();

            visit_date = dateEditViewMeetingTask_tv.getText().toString();
            //visit_time = timeAddMeetingTask_tv.getText().toString();
            visit_time = timeEditViewValueMeetingTaskDetail_et.getText().toString();
            visit_uid = selectAssignToId;
            visit_assignedby = userIdPref;
            visit_leadid = selectClientNameId;
            visit_purposeid = selectPurposeId;
            visit_id = currentVisitIdEdit;

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Editing Task...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected JSONObject doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("filter[visit_address]", visit_address));
            params.add(new BasicNameValuePair("filter[visit_date]", visit_date));
            params.add(new BasicNameValuePair("filter[visit_time]", visit_time));
            params.add(new BasicNameValuePair("filter[visit_uid]", visit_uid));
            params.add(new BasicNameValuePair("filter[visit_purposeid]", visit_purposeid));
            params.add(new BasicNameValuePair("filter[visit_leadid]", visit_leadid));
            params.add(new BasicNameValuePair("edit", "edit"));
            params.add(new BasicNameValuePair("visit_id", visit_id));

            String url_add_task = ApiLink.ROOT_URL + ApiLink.Dashboard_SalesPerson;
            JSONObject json = jsonParser.makeHttpRequest(url_add_task, "POST", params);

            try {
                int success = json.getInt(TAG_SUCCESS);
                String message = json.getString(TAG_MESSAGE);
                if (success == 1 && message.equals("Meeting Task Updated Successfully")) {
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
                    makeText(getActivity(), "Meeting Task Updated Successfully", Toast.LENGTH_SHORT).show();
                    // clearAll();
                } else {
                    makeText(getActivity(), "Already Inserted!", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (Exception e) {
            }
        }
    }

    public class CreateDeleteMeetingTask extends AsyncTask<String, JSONObject, JSONObject> {
        String visit_id;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            visit_id = currentVisitIdEdit;

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Deleting Task...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected JSONObject doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("delete", "delete"));
            params.add(new BasicNameValuePair("visit_id", visit_id));

            String url_add_task = ApiLink.ROOT_URL + ApiLink.Dashboard_SalesPerson;
            JSONObject json = jsonParser.makeHttpRequest(url_add_task, "POST", params);

            try {
                int success = json.getInt(TAG_SUCCESS);
                String message = json.getString(TAG_MESSAGE);
                if (success == 1 && message.equals("Meeting Task Deleted Successfully")) {
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
                    makeText(getActivity(), "Meeting Task Deleted Successfully", Toast.LENGTH_SHORT).show();
                    if (userTypePref.equals("Sales Manager")) {
                        getVisitTaskMeetingRecyclerView();
                    }else if (userTypePref.equals("Manager Head")){
                        getVisitTaskManagerHeadMeetingRecyclerView();
                    }
                } else {
                    makeText(getActivity(), "Oops! An error occured", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (Exception e) {
            }
        }
    }

    private String convertIn12Hours(String time){

        String timeToDisplay = "";
        String[] timeArray = time.split(":");
        Integer hours = Integer.parseInt(timeArray[0]);

        if(hours > 12){
            timeToDisplay = (hours - 12) + ":" +  timeArray[1] + " PM";
        }else{
            timeToDisplay = timeArray[0] + ":" + timeArray[1] + " AM";
        }

        return timeToDisplay;
    }

}
