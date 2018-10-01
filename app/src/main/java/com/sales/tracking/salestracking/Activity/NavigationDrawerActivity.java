package com.sales.tracking.salestracking.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.sales.tracking.salestracking.Fragment.AddCustomerfeedbackFragment;
import com.sales.tracking.salestracking.Fragment.AddLeadSpFragment;
import com.sales.tracking.salestracking.Fragment.AddMeetingTaskFragment;
import com.sales.tracking.salestracking.Fragment.AddReassignedManagerFragment;
import com.sales.tracking.salestracking.Fragment.AddSalesPersonManagerFragment;
import com.sales.tracking.salestracking.Fragment.AddTargetManagerFragment;
import com.sales.tracking.salestracking.Fragment.AddTotalExpensesFragment;
import com.sales.tracking.salestracking.Fragment.AddVisitTaskSpFragment;
import com.sales.tracking.salestracking.Fragment.AllCallReportFragment;
import com.sales.tracking.salestracking.Fragment.AllVisitReportFragment;
import com.sales.tracking.salestracking.Fragment.AttendanceManagerFragment;
import com.sales.tracking.salestracking.Fragment.AttendanceReportFragment;
import com.sales.tracking.salestracking.Fragment.CallDoneReportFragment;
import com.sales.tracking.salestracking.Fragment.CallPendingReportFragment;
import com.sales.tracking.salestracking.Fragment.CallsPendingNotificationFragment;
import com.sales.tracking.salestracking.Fragment.DashboardFragment;
import com.sales.tracking.salestracking.Fragment.MyProfileFragment;
import com.sales.tracking.salestracking.Fragment.RequestAddFragment;
import com.sales.tracking.salestracking.Fragment.RequestViewFragment;
import com.sales.tracking.salestracking.Fragment.TargetFragment;
import com.sales.tracking.salestracking.Fragment.TrackSalesPersonActivity;
import com.sales.tracking.salestracking.Fragment.UpdateSaleCallTaskFragment;
import com.sales.tracking.salestracking.Fragment.ViewClientManagerFragment;
import com.sales.tracking.salestracking.Fragment.ViewClientNotificationManagerFragment;
import com.sales.tracking.salestracking.Fragment.ViewCustomerFeedbackFragment;
import com.sales.tracking.salestracking.Fragment.ViewDoneVisitReportFragment;
import com.sales.tracking.salestracking.Fragment.ViewExpensesReportFragment;
import com.sales.tracking.salestracking.Fragment.ViewLeadSpFragment;
import com.sales.tracking.salestracking.Fragment.ViewMeetingTaskManagerFragment;
import com.sales.tracking.salestracking.Fragment.ViewPendingVisitReportFragment;
import com.sales.tracking.salestracking.Fragment.ViewReassignManagerFragment;
import com.sales.tracking.salestracking.Fragment.ViewRequestNotificationFragment;
import com.sales.tracking.salestracking.Fragment.ViewSalesCallTaskFragment;
import com.sales.tracking.salestracking.Fragment.ViewSalesPersonDetailsFragment;
import com.sales.tracking.salestracking.Fragment.ViewTargetManagerFragment;
import com.sales.tracking.salestracking.Fragment.ViewTotalCollectionFragment;
import com.sales.tracking.salestracking.Fragment.ViewTotalExpensesFragment;
import com.sales.tracking.salestracking.Fragment.ViewUserManagerHeadFragment;
import com.sales.tracking.salestracking.Fragment.ViewVisitTaskSpFragment;
import com.sales.tracking.salestracking.Fragment.VisitPendingNotificationFragment;
import com.sales.tracking.salestracking.Fragment.VisitTaskUpdateSpFragment;
import com.sales.tracking.salestracking.R;
import com.sales.tracking.salestracking.Utility.LocationTracker;
import com.sales.tracking.salestracking.Utility.SessionManagement;

import butterknife.ButterKnife;

public class NavigationDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, LocationListener,
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    TextView toolbar_title;
    TextView nav_user, userEmailHeading_tv;
    ImageView editUserProfile_iv;

    //header recyclerview
    RelativeLayout reportHeading_rl, menu_rl;
    SessionManagement session;

    SharedPreferences sharedPref;
    String userNamePref, userEmailPref, userTypePref;

    String drawer_Open = "";

    Handler handler = new Handler();

    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 300000;

    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int REQUEST_CHECK_SETTINGS = 10;

    double latitude, longitude;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;

    private boolean mRequestingLocationUpdates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        ButterKnife.bind(this);
        mRequestingLocationUpdates = false;
        buildGoogleApiClient();
        initialiseUI();
    }

    private void initialiseUI() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(NavigationDrawerActivity.this);
        userNamePref = sharedPref.getString("user_name", "");
        userEmailPref = sharedPref.getString("user_email", "");
        userTypePref = sharedPref.getString("user_type", "");

        session = new SessionManagement(getApplicationContext());

        if (userTypePref.equals("Sales Executive")) {
            locationTracking();
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.getMenu().getItem(2).setActionView(R.layout.menu_layout);
        navigationView.getMenu().getItem(3).setActionView(R.layout.menu_layout);
        navigationView.getMenu().getItem(4).setActionView(R.layout.menu_layout);

        //  navigationView.getMenu().getItem(6).setActionView(R.layout.menu_layout);
        navigationView.getMenu().getItem(7).setActionView(R.layout.menu_layout);
        navigationView.getMenu().getItem(11).setActionView(R.layout.menu_layout);
        navigationView.getMenu().getItem(14).setActionView(R.layout.menu_layout);
        navigationView.getMenu().getItem(17).setActionView(R.layout.menu_layout);
        navigationView.getMenu().getItem(20).setActionView(R.layout.menu_layout);

        navigationView.getMenu().getItem(25).setActionView(R.layout.menu_layout);
        navigationView.getMenu().getItem(28).setActionView(R.layout.menu_layout);
        navigationView.getMenu().getItem(31).setActionView(R.layout.menu_layout);
        navigationView.getMenu().getItem(35).setActionView(R.layout.menu_layout);
        navigationView.getMenu().getItem(38).setActionView(R.layout.menu_layout);
        navigationView.getMenu().getItem(41).setActionView(R.layout.menu_layout);
        navigationView.getMenu().getItem(45).setActionView(R.layout.menu_layout);


        View hView = navigationView.getHeaderView(0);
        nav_user = (TextView) hView.findViewById(R.id.usernameHeading_tv);
        userEmailHeading_tv = (TextView) hView.findViewById(R.id.userEmailHeading_tv);
        editUserProfile_iv = (ImageView) hView.findViewById(R.id.editUserProfile_iv);
        editUserProfile_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myProfileFragment();
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        reportHeading_rl = (RelativeLayout) hView.findViewById(R.id.reportHeading_rl);
        reportHeading_rl.setOnClickListener(this);

        menu_rl = (RelativeLayout) hView.findViewById(R.id.menu_rl);
        //   menu_rl.setOnClickListener(this);

        nav_user.setText(userNamePref);
        userEmailHeading_tv.setText(userEmailPref);

        dashboardFragment();

        setDrawerFromActivity();
        navigationView.setNavigationItemSelectedListener(this);

        if (userTypePref.equals("Sales Executive")) {
            navigationView.getMenu().setGroupVisible(R.id.main_sales_person_option, true);
            navigationView.getMenu().setGroupVisible(R.id.main_option, false);
            navigationView.getMenu().setGroupVisible(R.id.main_head_option, false);
        } else if (userTypePref.equals("Sales Manager")) {
            navigationView.getMenu().setGroupVisible(R.id.main_sales_person_option, false);
            navigationView.getMenu().setGroupVisible(R.id.main_option, true);
            navigationView.getMenu().setGroupVisible(R.id.main_head_option, false);
        }else if (userTypePref.equals("Manager Head")){
            navigationView.getMenu().setGroupVisible(R.id.main_sales_person_option, false);
            navigationView.getMenu().setGroupVisible(R.id.main_option, false);
            navigationView.getMenu().setGroupVisible(R.id.main_head_option, true);
        }

    }

    private void locationTracking() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    100);
        } else {
            startLocationTracking();
        }
    }


    private void startLocationTracking() {
        final LocationTracker locationTrackerThread = new LocationTracker(NavigationDrawerActivity.this, String.valueOf(latitude), String.valueOf(longitude));
        locationTrackerThread.trackSalesPersonAPI();
    }

    private void setDrawerFromActivity() {
        drawer_Open = getIntent().getStringExtra("drawer_Open");
        try {
            if (drawer_Open.equals("open_track_sales")) {
                drawer.openDrawer(GravityCompat.START);
            } else {

            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (userTypePref.equals("Sales Executive")) {
            getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        } else if (userTypePref.equals("Sales Manager")) {
            getMenuInflater().inflate(R.menu.menu_manager_navigation, menu);
        }else if (userTypePref.equals("Manager Head")){
            getMenuInflater().inflate(R.menu.manager_head_menu_navigation, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (userTypePref.equals("Sales Executive")) {
            if (id == R.id.action_clients) {
                // Intent notificationIntent = new Intent(this, NotificationActivity.class);
                // startActivity(notificationIntent);
                callPendingFragment();
                drawer.closeDrawer(GravityCompat.START);
                return true;
            } else if (id == R.id.action_visit) {
                visitPendingFragment();
                drawer.closeDrawer(GravityCompat.START);
                return true;
            } else if (id == R.id.action_target) {
                targetFragment();
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        } else if (userTypePref.equals("Sales Manager")) {
            if (id == R.id.action_request_pending) {
                requestManagerNotificationFragment();
                drawer.closeDrawer(GravityCompat.START);
                return true;
            } else if (id == R.id.action_view_manager_clients) {
                viewClientManagerNotificationFragment();
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        }else if (userTypePref.equals("Manager Head")){
            if (id==R.id.action_view_managerhead_clients){
                requestManagerNotificationFragment();
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            dashboardFragment();
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_track_sales_person) {
            drawer.closeDrawer(GravityCompat.START);
            Intent trackSlaesTrackingIntent = new Intent(NavigationDrawerActivity.this, TrackSalesPersonActivity.class);
            startActivity(trackSlaesTrackingIntent);
        } else if (id == R.id.nav_manager) {
            navigationView.getMenu().setGroupVisible(R.id.manager_option, true);
            navigationView.getMenu().setGroupVisible(R.id.main_option, false);
            navigationView.getMenu().setGroupVisible(R.id.reports_option, false);
            navigationView.getMenu().setGroupVisible(R.id.reassign_request_option, false);

             navigationView.getMenu().setGroupVisible(R.id.managerhead_option ,false);
            navigationView.getMenu().setGroupVisible(R.id.main_head_option, false);
            navigationView.getMenu().setGroupVisible(R.id.managerhead_report_option, false);

            navigationView.getMenu().setGroupVisible(R.id.main_sales_person_option, false);
            navigationView.getMenu().setGroupVisible(R.id.manager_salesperson_option, false);

            setDefaultManageTask();
            setDefaultExpenses();
            setDefaultManageSalesPersonTarget();
            setDefaultManageSalesPerson();
            setDefaultManageClient();

            drawer.openDrawer(GravityCompat.START);
        } else if (id == R.id.nav_reassignment_request) {
            getDefaultReassignManager();
            drawer.openDrawer(GravityCompat.START);
        } else if (id == R.id.nav_reassign_request_header) {
            setDefaultDrawer();
        } else if (id == R.id.nav_view_request) {
            getDefaultReassignManager();
            drawer.closeDrawer(GravityCompat.START);

            viewReassignRequestManager();
        } else if (id == R.id.nav_assign_request) {
            getDefaultReassignManager();
            drawer.closeDrawer(GravityCompat.START);

            assignReassignRequestManager();
        } else if (id == R.id.nav_reports)   //Manager Accpunt Report hEading
        {
            menu_rl.setVisibility(View.VISIBLE);
            reportHeading_rl.setVisibility(View.GONE);

            setReportManagerDefault();

        } else if (id == R.id.nav_profile) {
            myProfileFragment();
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_manager_title) {
            setDefaultDrawer();
        } else if (id == R.id.nav_Reports_title) {
            setDefaultDrawer();

        } else if (id == R.id.nav_attendance) {
            menu_rl.setVisibility(View.VISIBLE);
            reportHeading_rl.setVisibility(View.GONE);
            setReportManagerDefault();


            viewAttendanceReportFragmnet();
            drawer.closeDrawer(GravityCompat.START);

        } else if (id == R.id.nav_all_visits) {
            menu_rl.setVisibility(View.VISIBLE);
            reportHeading_rl.setVisibility(View.GONE);
            setReportManagerDefault();

            viewAllVisitReportFragmnet();
            drawer.closeDrawer(GravityCompat.START);

        } else if (id == R.id.nav_visits_done) {
            menu_rl.setVisibility(View.VISIBLE);
            reportHeading_rl.setVisibility(View.GONE);
            setReportManagerDefault();

            viewDoneVisitReportFragmnet();
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_visits_pending) {
            menu_rl.setVisibility(View.VISIBLE);
            reportHeading_rl.setVisibility(View.GONE);
            setReportManagerDefault();

            viewPendingVisitReportFragmnet();
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_all_calls) {

            menu_rl.setVisibility(View.VISIBLE);
            reportHeading_rl.setVisibility(View.GONE);
            setReportManagerDefault();


            viewAllCallReportFragmnet();
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_calls_done) {

            menu_rl.setVisibility(View.VISIBLE);
            reportHeading_rl.setVisibility(View.GONE);
            setReportManagerDefault();

            viewCallDoneReportFragment();
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_calls_pending) {

            menu_rl.setVisibility(View.VISIBLE);
            reportHeading_rl.setVisibility(View.GONE);
            setReportManagerDefault();

            viewCallPendingReportFragment();
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_calls_expenses) {

            menu_rl.setVisibility(View.VISIBLE);
            reportHeading_rl.setVisibility(View.GONE);
            setReportManagerDefault();

            viewExpensesReportFragment();
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_expenses) {
            setDefaultManageTask();
            setDefaultManageSalesPersonTarget();
            setDefaultManageSalesPerson();
            setDefaultManageClient();
            navigationView.getMenu().getItem(21).setVisible(true);
            navigationView.getMenu().getItem(22).setVisible(true);
        } else if (id == R.id.nav_view_expenses) {                    //manager View Expenses
            viewTotalExpensesFragmnet();
            drawer.closeDrawer(GravityCompat.START);
            setDefaultManageTask();
            setDefaultManageSalesPersonTarget();
            setDefaultManageSalesPerson();
            setDefaultManageClient();
            navigationView.getMenu().getItem(21).setVisible(true);
            navigationView.getMenu().getItem(22).setVisible(true);
        } else if (id == R.id.nav_add_expenses) {                     //manager Add Expenses
            addTotalExpensesFragmnet();
            drawer.closeDrawer(GravityCompat.START);
            setDefaultManageTask();
            setDefaultManageSalesPersonTarget();
            setDefaultManageSalesPerson();
            setDefaultManageClient();
            navigationView.getMenu().getItem(21).setVisible(true);
            navigationView.getMenu().getItem(22).setVisible(true);
        } else if (id == R.id.nav_view_meeting_tasks) {
            viewMeetingTaskFragment();
            drawer.closeDrawer(GravityCompat.START);

            setDefaultManageTask();
            setDefaultExpenses();
            setDefaultManageSalesPersonTarget();
            setDefaultManageSalesPerson();
            setDefaultManageClient();
        } else if (id == R.id.nav_view_salescalls_tasks) {
            viewSaleCallTaskFragment();
            drawer.closeDrawer(GravityCompat.START);
            setDefaultManageTask();
            setDefaultExpenses();
            setDefaultManageSalesPersonTarget();
            setDefaultManageSalesPerson();
            setDefaultManageClient();
        } else if (id == R.id.nav_add_manage_tasks) {
            addMeetingSaleCallTaskFragment();
            drawer.closeDrawer(GravityCompat.START);
            setDefaultManageTask();
            setDefaultExpenses();
            setDefaultManageSalesPersonTarget();
            setDefaultManageSalesPerson();
            setDefaultManageClient();
        } else if (id == R.id.nav_manage_sales_person_target) {
            setDefaultExpenses();
            setDefaultManageTask();
            setDefaultManageSalesPerson();
            setDefaultManageClient();
            navigationView.getMenu().getItem(18).setVisible(true);
            navigationView.getMenu().getItem(19).setVisible(true);
        } else if (id == R.id.nav_view_manage_sales_person_target) {
            viewTargetManagerFragment();
            drawer.closeDrawer(GravityCompat.START);

            setDefaultExpenses();
            setDefaultManageTask();
            setDefaultManageSalesPerson();
            setDefaultManageClient();
            navigationView.getMenu().getItem(18).setVisible(true);
            navigationView.getMenu().getItem(19).setVisible(true);
        } else if (id == R.id.nav_add_manage_sales_person_target) {
            addTargetManagerFragment();
            drawer.closeDrawer(GravityCompat.START);

            setDefaultExpenses();
            setDefaultManageTask();
            setDefaultManageSalesPerson();
            setDefaultManageClient();
            navigationView.getMenu().getItem(18).setVisible(true);
            navigationView.getMenu().getItem(19).setVisible(true);
        } else if (id == R.id.nav_manage_sales_person) {
            setDefaultExpenses();
            setDefaultManageTask();
            setDefaultManageSalesPersonTarget();
            setDefaultManageClient();
            navigationView.getMenu().getItem(15).setVisible(true);
            navigationView.getMenu().getItem(16).setVisible(true);
        } else if (id == R.id.nav_view_manage_sales_person) {
            viewSalesPersonManagerFragment();
            drawer.closeDrawer(GravityCompat.START);

            setDefaultExpenses();
            setDefaultManageTask();
            setDefaultManageSalesPersonTarget();
            setDefaultManageClient();
            navigationView.getMenu().getItem(15).setVisible(true);
            navigationView.getMenu().getItem(16).setVisible(true);
        } else if (id == R.id.nav_add_manage_sales_person) {
            addSalesPersonManagerFragment();
            drawer.closeDrawer(GravityCompat.START);

            setDefaultExpenses();
            setDefaultManageTask();
            setDefaultManageSalesPersonTarget();
            setDefaultManageClient();
            navigationView.getMenu().getItem(15).setVisible(true);
            navigationView.getMenu().getItem(16).setVisible(true);
        } else if (id == R.id.nav_manage_client) {
            setDefaultManageTask();
            setDefaultExpenses();
            setDefaultManageSalesPersonTarget();
            setDefaultManageSalesPerson();
            navigationView.getMenu().getItem(12).setVisible(true);
            navigationView.getMenu().getItem(13).setVisible(true);
        } else if (id == R.id.nav_view_manage_client) {
            viewClientManagerFragment();
            drawer.closeDrawer(GravityCompat.START);

            setDefaultManageTask();
            setDefaultExpenses();
            setDefaultManageSalesPersonTarget();
            setDefaultManageSalesPerson();
            navigationView.getMenu().getItem(12).setVisible(true);
            navigationView.getMenu().getItem(13).setVisible(true);
        } else if (id == R.id.nav_add_manage_client) {
            addLeadSpFragment();
            drawer.closeDrawer(GravityCompat.START);

            setDefaultManageTask();
            setDefaultExpenses();
            setDefaultManageSalesPersonTarget();
            setDefaultManageSalesPerson();
            navigationView.getMenu().getItem(12).setVisible(true);
            navigationView.getMenu().getItem(13).setVisible(true);
        } else if (id == R.id.nav_manage_tasks) {
            setDefaultExpenses();
            setDefaultManageSalesPersonTarget();
            setDefaultManageSalesPerson();
            setDefaultManageClient();
            navigationView.getMenu().getItem(10).setVisible(true);
            navigationView.getMenu().getItem(9).setVisible(true);
            navigationView.getMenu().getItem(8).setVisible(true);
        } else if (id == R.id.nav_workinghours_attendance) {

            attendanceFragment();
            drawer.closeDrawer(GravityCompat.START);
            setDefaultManageTask();
            setDefaultExpenses();
            setDefaultManageSalesPersonTarget();
            setDefaultManageSalesPerson();
            setDefaultManageClient();
        } else if (id == R.id.nav_view_collection)    //manager View Total Collection
        {
            setDefaultManageTask();
            setDefaultExpenses();
            setDefaultManageSalesPersonTarget();
            setDefaultManageSalesPerson();
            setDefaultManageClient();

            totalCollectionManagerFragment();
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_logout) {
            session.logoutUser();
            drawer.closeDrawers();

        } else if (id == R.id.nav_sale_person_dashboard) {
            dashboardFragment();
            drawer.closeDrawer(GravityCompat.START);

        } else if (id == R.id.nav_manage_salesperson) {
            navigationView.getMenu().setGroupVisible(R.id.manager_option, false);
            navigationView.getMenu().setGroupVisible(R.id.main_option, false);
            navigationView.getMenu().setGroupVisible(R.id.reports_option, false);
            navigationView.getMenu().setGroupVisible(R.id.main_sales_person_option, false);

            navigationView.getMenu().setGroupVisible(R.id.manager_salesperson_option, true);

            navigationView.getMenu().setGroupVisible(R.id.reassign_request_option, false);
            navigationView.getMenu().setGroupVisible(R.id.managerhead_option ,false);
            navigationView.getMenu().setGroupVisible(R.id.main_head_option, false);
            navigationView.getMenu().setGroupVisible(R.id.managerhead_report_option, false);

            setDefaultVisitTaskSalesPerson();
            setDefaultCallsTaskSalesPerson();
            setDefaultRequestSalesPerson();
            setDefaultSalesPersonExpenses();
            setDefaultLeadSalesPerson();
            setDefaultCustomerFeedbackSalesPerson();

        } else if (id == R.id.nav_manager_salesperson_title) {
            navigationView.getMenu().setGroupVisible(R.id.manager_option, false);
            navigationView.getMenu().setGroupVisible(R.id.main_option, false);
            navigationView.getMenu().setGroupVisible(R.id.reports_option, false);
            navigationView.getMenu().setGroupVisible(R.id.main_sales_person_option, true);

            navigationView.getMenu().setGroupVisible(R.id.manager_salesperson_option, false);

            navigationView.getMenu().setGroupVisible(R.id.reassign_request_option, false);
            navigationView.getMenu().setGroupVisible(R.id.managerhead_option ,false);
            navigationView.getMenu().setGroupVisible(R.id.main_head_option, false);
            navigationView.getMenu().setGroupVisible(R.id.managerhead_report_option, false);


        } else if (id == R.id.nav_attendance_salesperson) {
            attendanceFragment();
            drawer.closeDrawer(GravityCompat.START);

            setDefaultVisitTaskSalesPerson();
            setDefaultCallsTaskSalesPerson();
            setDefaultRequestSalesPerson();
            setDefaultSalesPersonExpenses();
            setDefaultLeadSalesPerson();
            setDefaultCustomerFeedbackSalesPerson();
        } else if (id == R.id.nav_visit_tasks_salesperson) {
            navigationView.getMenu().getItem(29).setVisible(true);
            navigationView.getMenu().getItem(30).setVisible(true);
            setDefaultCallsTaskSalesPerson();
            setDefaultRequestSalesPerson();
            setDefaultSalesPersonExpenses();
            setDefaultLeadSalesPerson();
            setDefaultCustomerFeedbackSalesPerson();
        } else if (id == R.id.nav_view_visit_tasks_salesperson) {
            viewVisitTaskSpFragment();
            drawer.closeDrawer(GravityCompat.START);

            navigationView.getMenu().getItem(29).setVisible(true);
            navigationView.getMenu().getItem(30).setVisible(true);
            setDefaultCallsTaskSalesPerson();
            setDefaultRequestSalesPerson();
            setDefaultSalesPersonExpenses();
            setDefaultLeadSalesPerson();
            setDefaultCustomerFeedbackSalesPerson();

        } else if (id == R.id.nav_add_visit_tasks_salesperson) {
            updateVisitTaskSpFragment();
            drawer.closeDrawer(GravityCompat.START);

            navigationView.getMenu().getItem(29).setVisible(true);
            navigationView.getMenu().getItem(30).setVisible(true);
            setDefaultCallsTaskSalesPerson();
            setDefaultRequestSalesPerson();
            setDefaultSalesPersonExpenses();
            setDefaultLeadSalesPerson();
            setDefaultCustomerFeedbackSalesPerson();
        } else if (id == R.id.nav_call_task_salesperson) {
            navigationView.getMenu().getItem(32).setVisible(true);
            navigationView.getMenu().getItem(33).setVisible(true);
            setDefaultVisitTaskSalesPerson();
            setDefaultRequestSalesPerson();
            setDefaultSalesPersonExpenses();
            setDefaultLeadSalesPerson();
            setDefaultCustomerFeedbackSalesPerson();
        } else if (id == R.id.nav_view_call_task_salesperson) {
            viewSaleCallTaskFragment();
            drawer.closeDrawer(GravityCompat.START);

            navigationView.getMenu().getItem(32).setVisible(true);
            navigationView.getMenu().getItem(33).setVisible(true);
            setDefaultVisitTaskSalesPerson();
            setDefaultRequestSalesPerson();
            setDefaultSalesPersonExpenses();
            setDefaultLeadSalesPerson();
            setDefaultCustomerFeedbackSalesPerson();
        } else if (id == R.id.nav_add_call_task_salesperson) {
            updateSaleCallTaskFragment();
            drawer.closeDrawer(GravityCompat.START);

            navigationView.getMenu().getItem(32).setVisible(true);
            navigationView.getMenu().getItem(33).setVisible(true);
            setDefaultVisitTaskSalesPerson();
            setDefaultRequestSalesPerson();
            setDefaultSalesPersonExpenses();
            setDefaultLeadSalesPerson();
            setDefaultCustomerFeedbackSalesPerson();
        } else if (id == R.id.nav_create_visit_task_salesperson) {
            addVisitTaskSpFragment();
            drawer.closeDrawer(GravityCompat.START);

            setDefaultVisitTaskSalesPerson();
            setDefaultCallsTaskSalesPerson();
            setDefaultRequestSalesPerson();
            setDefaultSalesPersonExpenses();
            setDefaultLeadSalesPerson();
            setDefaultCustomerFeedbackSalesPerson();
        } else if (id == R.id.nav_request_salesperson) {
            navigationView.getMenu().getItem(36).setVisible(true);
            navigationView.getMenu().getItem(37).setVisible(true);
            setDefaultVisitTaskSalesPerson();
            setDefaultCallsTaskSalesPerson();
            setDefaultSalesPersonExpenses();
            setDefaultLeadSalesPerson();
            setDefaultCustomerFeedbackSalesPerson();
        } else if (id == R.id.nav_view_request_salesperson) {
            viewRequestTaskSpFragment();
            drawer.closeDrawer(GravityCompat.START);

            navigationView.getMenu().getItem(36).setVisible(true);
            navigationView.getMenu().getItem(37).setVisible(true);
            setDefaultVisitTaskSalesPerson();
            setDefaultCallsTaskSalesPerson();
            setDefaultSalesPersonExpenses();
            setDefaultLeadSalesPerson();
            setDefaultCustomerFeedbackSalesPerson();
        } else if (id == R.id.nav_add_request_salesperson) {
            addRequestTaskSpFragment();
            drawer.closeDrawer(GravityCompat.START);

            navigationView.getMenu().getItem(36).setVisible(true);
            navigationView.getMenu().getItem(37).setVisible(true);
            setDefaultVisitTaskSalesPerson();
            setDefaultCallsTaskSalesPerson();
            setDefaultSalesPersonExpenses();
            setDefaultLeadSalesPerson();
            setDefaultCustomerFeedbackSalesPerson();
        } else if (id == R.id.nav_lead_salesperson) {
            navigationView.getMenu().getItem(39).setVisible(true);
            navigationView.getMenu().getItem(40).setVisible(true);
            setDefaultVisitTaskSalesPerson();
            setDefaultCallsTaskSalesPerson();
            setDefaultRequestSalesPerson();
            setDefaultSalesPersonExpenses();
            setDefaultCustomerFeedbackSalesPerson();
        } else if (id == R.id.nav_view_lead_salesperson) {
            viewLeadSpFragment();
            drawer.closeDrawer(GravityCompat.START);

            navigationView.getMenu().getItem(39).setVisible(true);
            navigationView.getMenu().getItem(40).setVisible(true);
            setDefaultVisitTaskSalesPerson();
            setDefaultCallsTaskSalesPerson();
            setDefaultRequestSalesPerson();
            setDefaultSalesPersonExpenses();
            setDefaultCustomerFeedbackSalesPerson();
        } else if (id == R.id.nav_add_lead_salesperson) {
            addLeadSpFragment();
            drawer.closeDrawer(GravityCompat.START);

            navigationView.getMenu().getItem(39).setVisible(true);
            navigationView.getMenu().getItem(40).setVisible(true);
            setDefaultVisitTaskSalesPerson();
            setDefaultCallsTaskSalesPerson();
            setDefaultRequestSalesPerson();
            setDefaultSalesPersonExpenses();
            setDefaultCustomerFeedbackSalesPerson();
        } else if (id == R.id.nav_expenses_salesperson) {
            navigationView.getMenu().getItem(42).setVisible(true);
            navigationView.getMenu().getItem(43).setVisible(true);
            setDefaultVisitTaskSalesPerson();
            setDefaultCallsTaskSalesPerson();
            setDefaultRequestSalesPerson();
            setDefaultLeadSalesPerson();
            setDefaultCustomerFeedbackSalesPerson();
        } else if (id == R.id.nav_view_expenses_salesperson) {
            viewTotalExpensesFragmnet();
            drawer.closeDrawer(GravityCompat.START);

            navigationView.getMenu().getItem(42).setVisible(true);
            navigationView.getMenu().getItem(43).setVisible(true);
            setDefaultVisitTaskSalesPerson();
            setDefaultCallsTaskSalesPerson();
            setDefaultRequestSalesPerson();
            setDefaultLeadSalesPerson();
            setDefaultCustomerFeedbackSalesPerson();
        } else if (id == R.id.nav_add_expenses_salesperson) {
            addTotalExpensesFragmnet();
            drawer.closeDrawer(GravityCompat.START);

            navigationView.getMenu().getItem(42).setVisible(true);
            navigationView.getMenu().getItem(43).setVisible(true);
            setDefaultVisitTaskSalesPerson();
            setDefaultCallsTaskSalesPerson();
            setDefaultRequestSalesPerson();
            setDefaultLeadSalesPerson();
            setDefaultCustomerFeedbackSalesPerson();
        } else if (id == R.id.nav_view_totalcollection) {
            totalCollectionManagerFragment();
            drawer.closeDrawer(GravityCompat.START);

            setDefaultVisitTaskSalesPerson();
            setDefaultCallsTaskSalesPerson();
            setDefaultRequestSalesPerson();
            setDefaultSalesPersonExpenses();
            setDefaultLeadSalesPerson();
            setDefaultCustomerFeedbackSalesPerson();
        } else if (id == R.id.nav_customerfeedback_salesperson) {
            setDefaultVisitTaskSalesPerson();
            setDefaultCallsTaskSalesPerson();
            setDefaultRequestSalesPerson();
            setDefaultSalesPersonExpenses();
            setDefaultLeadSalesPerson();
            navigationView.getMenu().getItem(46).setVisible(true);
            navigationView.getMenu().getItem(47).setVisible(true);
        } else if (id == R.id.nav_view_customerfeedback_salesperson) {
            viewCustomerFeedbackFragmnet();
            drawer.closeDrawer(GravityCompat.START);

            setDefaultVisitTaskSalesPerson();
            setDefaultCallsTaskSalesPerson();
            setDefaultRequestSalesPerson();
            setDefaultSalesPersonExpenses();
            setDefaultLeadSalesPerson();
            navigationView.getMenu().getItem(46).setVisible(true);
            navigationView.getMenu().getItem(47).setVisible(true);

        } else if (id == R.id.nav_add_customerfeedback_salesperson) {
            addCustomerFeedbackFragmnet();
            drawer.closeDrawer(GravityCompat.START);

            setDefaultVisitTaskSalesPerson();
            setDefaultCallsTaskSalesPerson();
            setDefaultRequestSalesPerson();
            setDefaultSalesPersonExpenses();
            setDefaultLeadSalesPerson();
            navigationView.getMenu().getItem(46).setVisible(true);
            navigationView.getMenu().getItem(47).setVisible(true);
        } else if (id == R.id.nav_dashboard_head){
            dashboardFragment();
            drawer.closeDrawer(GravityCompat.START);
        }else if (id==R.id.nav_track_sales_person_head){
            drawer.closeDrawer(GravityCompat.START);
            Intent trackSlaesTrackingIntent = new Intent(NavigationDrawerActivity.this, TrackSalesPersonActivity.class);
            startActivity(trackSlaesTrackingIntent);
        }else if (id==R.id.nav_manager_head){
            setDefaultManagerHeadManageDrawer();

            setDefaultManagerViewTasksDrawer();
        }else if (id==R.id.nav_managerhead_title){
            navigationView.getMenu().setGroupVisible(R.id.manager_option, false);
            navigationView.getMenu().setGroupVisible(R.id.main_option, false);
            navigationView.getMenu().setGroupVisible(R.id.reports_option, false);
            navigationView.getMenu().setGroupVisible(R.id.reassign_request_option, false);
            navigationView.getMenu().setGroupVisible(R.id.managerhead_option ,false);
            navigationView.getMenu().setGroupVisible(R.id.main_head_option, true);
            navigationView.getMenu().setGroupVisible(R.id.managerhead_report_option, false);
            navigationView.getMenu().setGroupVisible(R.id.main_sales_person_option, false);

            navigationView.getMenu().setGroupVisible(R.id.manager_salesperson_option, false);

            //setDefaultManagerViewTasksDrawer();
        }
        else if (id==R.id.nav_reports_head){
            setDefaultManagerHeadReportDrawer();
        }else if (id==R.id.nav_reports_managerhead_title){

            navigationView.getMenu().setGroupVisible(R.id.manager_option, false);
            navigationView.getMenu().setGroupVisible(R.id.main_option, false);
            navigationView.getMenu().setGroupVisible(R.id.reports_option, false);
            navigationView.getMenu().setGroupVisible(R.id.reassign_request_option, false);
            navigationView.getMenu().setGroupVisible(R.id.managerhead_option ,false);
            navigationView.getMenu().setGroupVisible(R.id.main_head_option, false);
            navigationView.getMenu().setGroupVisible(R.id.managerhead_report_option, true);
            navigationView.getMenu().setGroupVisible(R.id.main_sales_person_option, false);
            navigationView.getMenu().setGroupVisible(R.id.manager_salesperson_option, false);

        }else if (id==R.id.nav_attendance_managerhead){

            viewAttendanceReportFragmnet();
            drawer.closeDrawer(GravityCompat.START);
        }else if (id==R.id.nav_visits_managerhead){

        }else if (id==R.id.nav_expenses_managerhead){

        }else if (id==R.id.nav_view_users_managerhead){
            setDefaultManagerViewTasksDrawer();

            viewUserManagerHeadFragment();
            drawer.closeDrawer(GravityCompat.START);
        }else if (id==R.id.nav_view_client_managerhead){
            setDefaultManagerViewTasksDrawer();

            viewClientManagerFragment();
            drawer.closeDrawer(GravityCompat.START);

        }else if (id==R.id.nav_view_salescalls_tasks_managerhead){
            viewSaleCallTaskFragment();
            drawer.closeDrawer(GravityCompat.START);
        }else if (id==R.id.nav_view_meeting_tasks_managerhead){
            viewMeetingTaskFragment();
            drawer.closeDrawer(GravityCompat.START);
        }else if (id==R.id.nav_view_tasks_managerhead){

            navigationView.getMenu().getItem(67).setVisible(true);
            navigationView.getMenu().getItem(68).setVisible(true);
        }else if (id==R.id.nav_workinghours_attendance_managerhead){
            setDefaultManagerViewTasksDrawer();

            attendanceFragment();
            drawer.closeDrawer(GravityCompat.START);
        }

        return true;
    }

    private void setReportManagerDefault() {
        navigationView.getMenu().setGroupVisible(R.id.manager_option, false);
        navigationView.getMenu().setGroupVisible(R.id.reports_option, true);
        navigationView.getMenu().setGroupVisible(R.id.main_option, false);
        navigationView.getMenu().setGroupVisible(R.id.reassign_request_option, false);
        navigationView.getMenu().setGroupVisible(R.id.managerhead_option ,false);
        navigationView.getMenu().setGroupVisible(R.id.main_head_option, false);
        navigationView.getMenu().setGroupVisible(R.id.managerhead_report_option, false);

    }

    private void getDefaultReassignManager() {
        navigationView.getMenu().setGroupVisible(R.id.manager_option, false);
        navigationView.getMenu().setGroupVisible(R.id.main_option, false);
        navigationView.getMenu().setGroupVisible(R.id.reports_option, false);
        navigationView.getMenu().setGroupVisible(R.id.reassign_request_option, true);
        navigationView.getMenu().setGroupVisible(R.id.managerhead_option ,false);
        navigationView.getMenu().setGroupVisible(R.id.main_head_option, false);
        navigationView.getMenu().setGroupVisible(R.id.managerhead_report_option, false);

    }

    private void setDefaultDrawer() {
        navigationView.getMenu().setGroupVisible(R.id.manager_option, false);
        navigationView.getMenu().setGroupVisible(R.id.main_option, true);
        navigationView.getMenu().setGroupVisible(R.id.reports_option, false);
        navigationView.getMenu().setGroupVisible(R.id.reassign_request_option, false);
        navigationView.getMenu().setGroupVisible(R.id.managerhead_option ,false);
        navigationView.getMenu().setGroupVisible(R.id.main_head_option, false);
        navigationView.getMenu().setGroupVisible(R.id.managerhead_report_option, false);

    }

    private void setDefaultManagerHeadManageDrawer(){
        navigationView.getMenu().setGroupVisible(R.id.manager_option, false);
        navigationView.getMenu().setGroupVisible(R.id.main_option, false);
        navigationView.getMenu().setGroupVisible(R.id.reports_option, false);
        navigationView.getMenu().setGroupVisible(R.id.reassign_request_option, false);
        navigationView.getMenu().setGroupVisible(R.id.managerhead_option ,true);
        navigationView.getMenu().setGroupVisible(R.id.main_head_option, false);
        navigationView.getMenu().setGroupVisible(R.id.managerhead_report_option, false);

        navigationView.getMenu().setGroupVisible(R.id.main_sales_person_option, false);
        navigationView.getMenu().setGroupVisible(R.id.manager_salesperson_option, false);

    }

    private void setDefaultManagerHeadReportDrawer(){
        navigationView.getMenu().setGroupVisible(R.id.manager_option, false);
        navigationView.getMenu().setGroupVisible(R.id.main_option, false);
        navigationView.getMenu().setGroupVisible(R.id.reports_option, false);
        navigationView.getMenu().setGroupVisible(R.id.reassign_request_option, false);
        navigationView.getMenu().setGroupVisible(R.id.managerhead_option ,false);
        navigationView.getMenu().setGroupVisible(R.id.main_head_option, false);
        navigationView.getMenu().setGroupVisible(R.id.managerhead_report_option, true);

        navigationView.getMenu().setGroupVisible(R.id.main_sales_person_option, false);
        navigationView.getMenu().setGroupVisible(R.id.manager_salesperson_option, false);

    }

    private void setDefaultManagerViewTasksDrawer(){
        navigationView.getMenu().getItem(67).setVisible(false);
        navigationView.getMenu().getItem(68).setVisible(false);
    }

    private void setDefaultVisitTaskSalesPerson() {
        navigationView.getMenu().getItem(29).setVisible(false);
        navigationView.getMenu().getItem(30).setVisible(false);
    }

    private void setDefaultCallsTaskSalesPerson() {
        navigationView.getMenu().getItem(32).setVisible(false);
        navigationView.getMenu().getItem(33).setVisible(false);
    }

    private void setDefaultRequestSalesPerson() {
        navigationView.getMenu().getItem(36).setVisible(false);
        navigationView.getMenu().getItem(37).setVisible(false);
    }

    private void setDefaultLeadSalesPerson() {
        navigationView.getMenu().getItem(39).setVisible(false);
        navigationView.getMenu().getItem(40).setVisible(false);
    }

    private void setDefaultCustomerFeedbackSalesPerson() {
        navigationView.getMenu().getItem(46).setVisible(false);
        navigationView.getMenu().getItem(47).setVisible(false);
    }

    private void setDefaultSalesPersonExpenses() {
        navigationView.getMenu().getItem(42).setVisible(false);
        navigationView.getMenu().getItem(43).setVisible(false);
    }

    private void setDefaultExpenses() {
        navigationView.getMenu().getItem(21).setVisible(false);
        navigationView.getMenu().getItem(22).setVisible(false);
    }

    private void setDefaultManageSalesPersonTarget() {
        navigationView.getMenu().getItem(18).setVisible(false);
        navigationView.getMenu().getItem(19).setVisible(false);
    }

    private void setDefaultManageSalesPerson() {
        navigationView.getMenu().getItem(15).setVisible(false);
        navigationView.getMenu().getItem(16).setVisible(false);
    }

    private void setDefaultManageClient() {
        navigationView.getMenu().getItem(12).setVisible(false);
        navigationView.getMenu().getItem(13).setVisible(false);
    }

    private void setDefaultManageTask() {
        navigationView.getMenu().getItem(10).setVisible(false);
        navigationView.getMenu().getItem(9).setVisible(false);
        navigationView.getMenu().getItem(8).setVisible(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reportHeading_rl:
                menu_rl.setVisibility(View.VISIBLE);
                reportHeading_rl.setVisibility(View.GONE);
                setDefaultDrawer();
                break;

            case R.id.menu_rl:

                break;

        }
    }

    public void dashboardFragment() {
        navigationView.getMenu().getItem(0).setChecked(true);

        DashboardFragment dfragment = new DashboardFragment();
        FragmentTransaction dtransaction = getSupportFragmentManager().beginTransaction();
        dtransaction.replace(R.id.fragment_Container, dfragment);
        dtransaction.commit();
    }

    public void targetFragment() {
        navigationView.getMenu().getItem(0).setChecked(true);

        TargetFragment tfragment = new TargetFragment();
        FragmentTransaction dtransaction = getSupportFragmentManager().beginTransaction();
        dtransaction.replace(R.id.fragment_Container, tfragment);
        dtransaction.commit();
    }

    public void callPendingFragment() {
        navigationView.getMenu().getItem(0).setChecked(true);

        CallsPendingNotificationFragment cpfragment = new CallsPendingNotificationFragment();
        FragmentTransaction dtransaction = getSupportFragmentManager().beginTransaction();
        dtransaction.replace(R.id.fragment_Container, cpfragment);
        dtransaction.commit();
    }

    public void visitPendingFragment() {
        navigationView.getMenu().getItem(0).setChecked(true);

        VisitPendingNotificationFragment vpfragment = new VisitPendingNotificationFragment();
        FragmentTransaction dtransaction = getSupportFragmentManager().beginTransaction();
        dtransaction.replace(R.id.fragment_Container, vpfragment);
        dtransaction.commit();
    }

    public void myProfileFragment() {
        navigationView.getMenu().getItem(0).setChecked(true);

        MyProfileFragment mpfragment = new MyProfileFragment();
        FragmentTransaction dtransaction = getSupportFragmentManager().beginTransaction();
        dtransaction.replace(R.id.fragment_Container, mpfragment);
        dtransaction.commit();
    }

    public void attendanceFragment() {

        AttendanceManagerFragment attendancefragment = new AttendanceManagerFragment();
        FragmentTransaction atransaction = getSupportFragmentManager().beginTransaction();
        atransaction.replace(R.id.fragment_Container, attendancefragment);
        atransaction.commit();
    }

    public void viewMeetingTaskFragment() {
        ViewMeetingTaskManagerFragment vfragment = new ViewMeetingTaskManagerFragment();
        FragmentTransaction atransaction = getSupportFragmentManager().beginTransaction();
        atransaction.replace(R.id.fragment_Container, vfragment);
        atransaction.commit();
    }

    public void addMeetingSaleCallTaskFragment() {
        AddMeetingTaskFragment addfragment = new AddMeetingTaskFragment();
        FragmentTransaction atransaction = getSupportFragmentManager().beginTransaction();
        atransaction.replace(R.id.fragment_Container, addfragment);
        atransaction.commit();
    }

    public void viewTargetManagerFragment() {
        ViewTargetManagerFragment targetfragment = new ViewTargetManagerFragment();
        FragmentTransaction atransaction = getSupportFragmentManager().beginTransaction();
        atransaction.replace(R.id.fragment_Container, targetfragment);
        atransaction.commit();
    }

    public void addTargetManagerFragment() {
        AddTargetManagerFragment atfragment = new AddTargetManagerFragment();
        FragmentTransaction atransaction = getSupportFragmentManager().beginTransaction();
        atransaction.replace(R.id.fragment_Container, atfragment);
        atransaction.commit();
    }

    public void viewSaleCallTaskFragment() {
        ViewSalesCallTaskFragment vsfragment = new ViewSalesCallTaskFragment();
        FragmentTransaction atransaction = getSupportFragmentManager().beginTransaction();
        atransaction.replace(R.id.fragment_Container, vsfragment);
        atransaction.commit();
    }

    public void updateSaleCallTaskFragment() {
        UpdateSaleCallTaskFragment updateSaleCallTaskFragment = new UpdateSaleCallTaskFragment();
        FragmentTransaction atransaction = getSupportFragmentManager().beginTransaction();
        atransaction.replace(R.id.fragment_Container, updateSaleCallTaskFragment);
        atransaction.commit();
    }

    public void addLeadSpFragment() {
        AddLeadSpFragment addLeadSpFragment = new AddLeadSpFragment();
        FragmentTransaction atransaction = getSupportFragmentManager().beginTransaction();
        atransaction.replace(R.id.fragment_Container, addLeadSpFragment);
        atransaction.commit();
    }

    public void viewLeadSpFragment() {
        ViewLeadSpFragment viewLeadSpFragment = new ViewLeadSpFragment();
        FragmentTransaction atransaction = getSupportFragmentManager().beginTransaction();
        atransaction.replace(R.id.fragment_Container, viewLeadSpFragment);
        atransaction.commit();
    }

    public void viewVisitTaskSpFragment() {
        ViewVisitTaskSpFragment vspfragment = new ViewVisitTaskSpFragment();
        FragmentTransaction atransaction = getSupportFragmentManager().beginTransaction();
        atransaction.replace(R.id.fragment_Container, vspfragment);
        atransaction.commit();
    }

    public void updateVisitTaskSpFragment() {
        VisitTaskUpdateSpFragment uspfragment = new VisitTaskUpdateSpFragment();
        FragmentTransaction atransaction = getSupportFragmentManager().beginTransaction();
        atransaction.replace(R.id.fragment_Container, uspfragment);
        atransaction.commit();
    }

    public void addVisitTaskSpFragment() {
        AddVisitTaskSpFragment avtspfragment = new AddVisitTaskSpFragment();
        FragmentTransaction atransaction = getSupportFragmentManager().beginTransaction();
        atransaction.replace(R.id.fragment_Container, avtspfragment);
        atransaction.commit();
    }

    public void viewRequestTaskSpFragment() {
        RequestViewFragment rvfragment = new RequestViewFragment();
        FragmentTransaction atransaction = getSupportFragmentManager().beginTransaction();
        atransaction.replace(R.id.fragment_Container, rvfragment);
        atransaction.commit();
    }

    public void addRequestTaskSpFragment() {
        RequestAddFragment rafragment = new RequestAddFragment();
        FragmentTransaction atransaction = getSupportFragmentManager().beginTransaction();
        atransaction.replace(R.id.fragment_Container, rafragment);
        atransaction.commit();
    }

    public void viewTotalExpensesFragmnet() {
        ViewTotalExpensesFragment tefragment = new ViewTotalExpensesFragment();
        FragmentTransaction atransaction = getSupportFragmentManager().beginTransaction();
        atransaction.replace(R.id.fragment_Container, tefragment);
        atransaction.commit();
    }

    private void addTotalExpensesFragmnet() {
        AddTotalExpensesFragment atfragment = new AddTotalExpensesFragment();
        FragmentTransaction atransaction = getSupportFragmentManager().beginTransaction();
        atransaction.replace(R.id.fragment_Container, atfragment);
        atransaction.commit();
    }

    private void viewCustomerFeedbackFragmnet() {
        ViewCustomerFeedbackFragment vcffragment = new ViewCustomerFeedbackFragment();
        FragmentTransaction atransaction = getSupportFragmentManager().beginTransaction();
        atransaction.replace(R.id.fragment_Container, vcffragment);
        atransaction.commit();
    }

    private void addCustomerFeedbackFragmnet() {
        AddCustomerfeedbackFragment addCffragment = new AddCustomerfeedbackFragment();
        FragmentTransaction atransaction = getSupportFragmentManager().beginTransaction();
        atransaction.replace(R.id.fragment_Container, addCffragment);
        atransaction.commit();
    }

    public void totalCollectionManagerFragment() {
        ViewTotalCollectionFragment vfragment = new ViewTotalCollectionFragment();
        FragmentTransaction atransaction = getSupportFragmentManager().beginTransaction();
        atransaction.replace(R.id.fragment_Container, vfragment);
        atransaction.commit();
    }

    public void viewClientManagerFragment() {
        ViewClientManagerFragment vcmFragment = new ViewClientManagerFragment();
        FragmentTransaction atransaction = getSupportFragmentManager().beginTransaction();
        atransaction.replace(R.id.fragment_Container, vcmFragment);
        atransaction.commit();
    }

    public void viewSalesPersonManagerFragment() {
        ViewSalesPersonDetailsFragment vspFragment = new ViewSalesPersonDetailsFragment();
        FragmentTransaction atransaction = getSupportFragmentManager().beginTransaction();
        atransaction.replace(R.id.fragment_Container, vspFragment);
        atransaction.commit();
    }

    public void addSalesPersonManagerFragment() {
        AddSalesPersonManagerFragment aspFragment = new AddSalesPersonManagerFragment();
        FragmentTransaction atransaction = getSupportFragmentManager().beginTransaction();
        atransaction.replace(R.id.fragment_Container, aspFragment);
        atransaction.commit();
    }

    public void viewAttendanceReportFragmnet() {
        AttendanceReportFragment arfragment = new AttendanceReportFragment();
        FragmentTransaction atransaction = getSupportFragmentManager().beginTransaction();
        atransaction.replace(R.id.fragment_Container, arfragment);
        atransaction.commit();
    }

    public void viewAllVisitReportFragmnet() {
        AllVisitReportFragment avfragment = new AllVisitReportFragment();
        FragmentTransaction atransaction = getSupportFragmentManager().beginTransaction();
        atransaction.replace(R.id.fragment_Container, avfragment);
        atransaction.commit();
    }

    public void viewDoneVisitReportFragmnet() {
        ViewDoneVisitReportFragment vdfragment = new ViewDoneVisitReportFragment();
        FragmentTransaction atransaction = getSupportFragmentManager().beginTransaction();
        atransaction.replace(R.id.fragment_Container, vdfragment);
        atransaction.commit();

    }

    public void viewPendingVisitReportFragmnet() {
        ViewPendingVisitReportFragment vpfragment = new ViewPendingVisitReportFragment();
        FragmentTransaction atransaction = getSupportFragmentManager().beginTransaction();
        atransaction.replace(R.id.fragment_Container, vpfragment);
        atransaction.commit();
    }

    public void viewAllCallReportFragmnet() {
        AllCallReportFragment alfragment = new AllCallReportFragment();
        FragmentTransaction atransaction = getSupportFragmentManager().beginTransaction();
        atransaction.replace(R.id.fragment_Container, alfragment);
        atransaction.commit();
    }

    public void viewCallDoneReportFragment() {
        CallDoneReportFragment callDoneReportFragment = new CallDoneReportFragment();
        FragmentTransaction atransaction = getSupportFragmentManager().beginTransaction();
        atransaction.replace(R.id.fragment_Container, callDoneReportFragment);
        atransaction.commit();
    }

    public void viewCallPendingReportFragment() {
        CallPendingReportFragment callPendingReportFragment = new CallPendingReportFragment();
        FragmentTransaction atransaction = getSupportFragmentManager().beginTransaction();
        atransaction.replace(R.id.fragment_Container, callPendingReportFragment);
        atransaction.commit();
    }

    public void viewExpensesReportFragment() {
        ViewExpensesReportFragment viewExpensesReportFragment = new ViewExpensesReportFragment();
        FragmentTransaction atransaction = getSupportFragmentManager().beginTransaction();
        atransaction.replace(R.id.fragment_Container, viewExpensesReportFragment);
        atransaction.commit();
    }

    public void assignReassignRequestManager() {
        AddReassignedManagerFragment addReassignedManagerFragment = new AddReassignedManagerFragment();
        FragmentTransaction atransaction = getSupportFragmentManager().beginTransaction();
        atransaction.replace(R.id.fragment_Container, addReassignedManagerFragment);
        atransaction.commit();
    }

    public void viewReassignRequestManager() {
        ViewReassignManagerFragment viewReassignManagerFragment = new ViewReassignManagerFragment();
        FragmentTransaction atransaction = getSupportFragmentManager().beginTransaction();
        atransaction.replace(R.id.fragment_Container, viewReassignManagerFragment);
        atransaction.commit();
    }

    public void viewUserManagerHeadFragment() {
        ViewUserManagerHeadFragment vufragment = new ViewUserManagerHeadFragment();
        FragmentTransaction atransaction = getSupportFragmentManager().beginTransaction();
        atransaction.replace(R.id.fragment_Container, vufragment);
        atransaction.commit();
    }

    public void requestManagerNotificationFragment() {
        ViewRequestNotificationFragment vrfragment = new ViewRequestNotificationFragment();
        FragmentTransaction dtransaction = getSupportFragmentManager().beginTransaction();
        dtransaction.replace(R.id.fragment_Container, vrfragment);
        dtransaction.commit();
    }

    public void viewClientManagerNotificationFragment() {
        ViewClientNotificationManagerFragment vcnfragment = new ViewClientNotificationManagerFragment();
        FragmentTransaction dtransaction = getSupportFragmentManager().beginTransaction();
        dtransaction.replace(R.id.fragment_Container, vcnfragment);
        dtransaction.commit();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (mCurrentLocation == null) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }

        if (!mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (userTypePref.equals("Sales Executive")) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (userTypePref.equals("Sales Executive")) {
            isPlayServicesAvailable(this);
            startUpdatesHandler();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        if (mCurrentLocation != null) {
            latitude = mCurrentLocation.getLatitude();
            longitude = mCurrentLocation.getLongitude();
            startLocationTracking();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private boolean isPlayServicesAvailable(Context context) {
        int resultCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            GoogleApiAvailability.getInstance().getErrorDialog((Activity) context, resultCode, 2).show();
            return false;
        }
        return true;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CHECK_SETTINGS) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    startLocationUpdates();
                    break;
                case Activity.RESULT_CANCELED:
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    mRequestingLocationUpdates = false;
                } else {
                    showRationalDialog();
                }
            }
        }
    }

    public void startUpdatesHandler() {
        if (!isPlayServicesAvailable(this)) return;
        if (!mRequestingLocationUpdates) {
            mRequestingLocationUpdates = true;
        } else {
            return;
        }

        if (Build.VERSION.SDK_INT < 23) {
            startLocationUpdates();
            return;
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                showRationalDialog();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        }
    }

    private void showRationalDialog() {
        new AlertDialog.Builder(this)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(NavigationDrawerActivity.this,
                                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mRequestingLocationUpdates = false;
                    }
                })
                .setCancelable(false)
                .setMessage("Enable Location Service")
                .show();
    }

    private void startLocationUpdates() {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();

                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        if (ContextCompat.checkSelfPermission(NavigationDrawerActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, NavigationDrawerActivity.this);
                        }
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(NavigationDrawerActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (userTypePref.equals("Sales Executive")) {
            mGoogleApiClient.disconnect();
        }

    }
}
