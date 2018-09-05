package com.sales.tracking.salestracking.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.sales.tracking.salestracking.Fragment.AttendanceManagerFragment;
import com.sales.tracking.salestracking.Fragment.DashboardFragment;
import com.sales.tracking.salestracking.Fragment.TrackSalesPersonActivity;
import com.sales.tracking.salestracking.Fragment.ViewMeetingTaskManagerFragment;
import com.sales.tracking.salestracking.Fragment.ViewSalesCallTaskFragment;
import com.sales.tracking.salestracking.Fragment.ViewTotalCollectionFragment;
import com.sales.tracking.salestracking.R;
import com.sales.tracking.salestracking.Utility.SessionManagement;

import butterknife.ButterKnife;

public class NavigationDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener{

    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    TextView toolbar_title;
    TextView nav_user, userEmailHeading_tv;

    //header recyclerview
    RelativeLayout reportHeading_rl, menu_rl;
    SessionManagement session;

    SharedPreferences sharedPref;
    String userNamePref, userEmailPref, userTypePref;

    String drawer_Open = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        ButterKnife.bind(this);
        initialiseUI();
    }

    private void initialiseUI(){
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(NavigationDrawerActivity.this);
        userNamePref = sharedPref.getString("user_name", "");
        userEmailPref = sharedPref.getString("user_email", "");
        userTypePref = sharedPref.getString("user_type", "");

        session = new SessionManagement(getApplicationContext());

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

        View hView =  navigationView.getHeaderView(0);
        nav_user = (TextView)hView.findViewById(R.id.usernameHeading_tv);
        userEmailHeading_tv = (TextView) hView.findViewById(R.id.userEmailHeading_tv);

        reportHeading_rl = (RelativeLayout) hView.findViewById(R.id.reportHeading_rl);
        reportHeading_rl.setOnClickListener(this);

        menu_rl = (RelativeLayout) hView.findViewById(R.id.menu_rl);
        //  menu_rl.setOnClickListener(this);

        nav_user.setText(userNamePref);
        userEmailHeading_tv.setText(userEmailPref);

        dashboardFragment();

        setDrawerFromActivity();
        navigationView.setNavigationItemSelectedListener(this);

        if (userTypePref.equals("Sales Executive")){
            navigationView.getMenu().setGroupVisible(R.id.main_sales_person_option, true);
            navigationView.getMenu().setGroupVisible(R.id.main_option, false);
        }else if (userTypePref.equals("Sales Manager")){
            navigationView.getMenu().setGroupVisible(R.id.main_sales_person_option, false);
            navigationView.getMenu().setGroupVisible(R.id.main_option, true);
        }

    }

    private void setDrawerFromActivity(){
        drawer_Open = getIntent().getStringExtra("drawer_Open");
    try {
        if (drawer_Open.equals("open_track_sales")) {
            drawer.openDrawer(GravityCompat.START);
        } else {

        }
    }catch(Exception e){ }
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
       // getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_dashboard)
        {
            dashboardFragment();
            drawer.closeDrawer(GravityCompat.START);
        }
        else if (id == R.id.nav_track_sales_person)
        {
            drawer.closeDrawer(GravityCompat.START);
            Intent trackSlaesTrackingIntent = new Intent(NavigationDrawerActivity.this, TrackSalesPersonActivity.class);
            startActivity(trackSlaesTrackingIntent);
        }
        else if (id == R.id.nav_manager)
        {
            navigationView.getMenu().setGroupVisible(R.id.manager_option, true);
            navigationView.getMenu().setGroupVisible(R.id.main_option, false);
            navigationView.getMenu().setGroupVisible(R.id.reports_option, false);

            setDefaultManageTask();
            setDefaultExpenses();
            setDefaultManageSalesPersonTarget();
            setDefaultManageSalesPerson();
            setDefaultManageClient();

            drawer.openDrawer(GravityCompat.START);
        }
        else if (id == R.id.nav_reassignment_request)
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if (id == R.id.nav_reports)
        {
            menu_rl.setVisibility(View.VISIBLE);
            reportHeading_rl.setVisibility(View.GONE);
            navigationView.getMenu().setGroupVisible(R.id.manager_option, false);
            navigationView.getMenu().setGroupVisible(R.id.reports_option, true);
            navigationView.getMenu().setGroupVisible(R.id.main_option, false);
        }
        else if (id == R.id.nav_profile)
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if (id == R.id.nav_manager_title)
        {
            setDefaultDrawer();
        }
        else if (id ==R.id.nav_Reports_title)
        {
            setDefaultDrawer();
        }
        else if (id == R.id.nav_expenses)
        {
            setDefaultManageTask();
            setDefaultManageSalesPersonTarget();
            setDefaultManageSalesPerson();
            setDefaultManageClient();
            navigationView.getMenu().getItem(21).setVisible(true);
            navigationView.getMenu().getItem(22).setVisible(true);
        }else if(id==R.id.nav_view_meeting_tasks){
            viewMeetingTaskFragment();
            drawer.closeDrawer(GravityCompat.START);
            setDefaultManageTask();
            setDefaultExpenses();
            setDefaultManageSalesPersonTarget();
            setDefaultManageSalesPerson();
            setDefaultManageClient();
        }else if (id==R.id.nav_view_salescalls_tasks){
            viewSaleCallTaskFragment();
            drawer.closeDrawer(GravityCompat.START);
            setDefaultManageTask();
            setDefaultExpenses();
            setDefaultManageSalesPersonTarget();
            setDefaultManageSalesPerson();
            setDefaultManageClient();
        }
        else if (id==R.id.nav_manage_sales_person_target)
        {
            setDefaultExpenses();
            setDefaultManageTask();
            setDefaultManageSalesPerson();
            setDefaultManageClient();
            navigationView.getMenu().getItem(18).setVisible(true);
            navigationView.getMenu().getItem(19).setVisible(true);
        }
        else if (id==R.id.nav_manage_sales_person)
        {
            setDefaultExpenses();
            setDefaultManageTask();
            setDefaultManageSalesPersonTarget();
            setDefaultManageClient();
            navigationView.getMenu().getItem(15).setVisible(true);
            navigationView.getMenu().getItem(16).setVisible(true);
        }
        else if (id==R.id.nav_manage_client)
        {
            setDefaultManageTask();
            setDefaultExpenses();
            setDefaultManageSalesPersonTarget();
            setDefaultManageSalesPerson();
            navigationView.getMenu().getItem(12).setVisible(true);
            navigationView.getMenu().getItem(13).setVisible(true);
        }
        else if (id==R.id.nav_manage_tasks)
        {
            setDefaultExpenses();
            setDefaultManageSalesPersonTarget();
            setDefaultManageSalesPerson();
            setDefaultManageClient();
            navigationView.getMenu().getItem(10).setVisible(true);
            navigationView.getMenu().getItem(9).setVisible(true);
            navigationView.getMenu().getItem(8).setVisible(true);
        }
        else if(id==R.id.nav_workinghours_attendance)
        {

            attendanceFragment();
            drawer.closeDrawer(GravityCompat.START);
            setDefaultManageTask();
            setDefaultExpenses();
            setDefaultManageSalesPersonTarget();
            setDefaultManageSalesPerson();
            setDefaultManageClient();
        }
        else if (id==R.id.nav_view_collection)    //manager View Total Collection
        {
            setDefaultManageTask();
            setDefaultExpenses();
            setDefaultManageSalesPersonTarget();
            setDefaultManageSalesPerson();
            setDefaultManageClient();

            totalCollectionManagerFragment();
            drawer.closeDrawer(GravityCompat.START);
        }
        else if (id==R.id.nav_logout)
        {
            session.logoutUser();
            drawer.closeDrawers();

        }
        else if (id==R.id.nav_manage_salesperson)
        {
            navigationView.getMenu().setGroupVisible(R.id.manager_option, false);
            navigationView.getMenu().setGroupVisible(R.id.main_option, false);
            navigationView.getMenu().setGroupVisible(R.id.reports_option, false);
            navigationView.getMenu().setGroupVisible(R.id.main_sales_person_option, false);

            navigationView.getMenu().setGroupVisible(R.id.manager_salesperson_option, true);
            setDefaultVisitTaskSalesPerson();
            setDefaultCallsTaskSalesPerson();
            setDefaultRequestSalesPerson();
            setDefaultSalesPersonExpenses();
            setDefaultLeadSalesPerson();
            setDefaultCustomerFeedbackSalesPerson();

        }
        else if (id==R.id.nav_manager_salesperson_title)
        {
            navigationView.getMenu().setGroupVisible(R.id.manager_option, false);
            navigationView.getMenu().setGroupVisible(R.id.main_option, false);
            navigationView.getMenu().setGroupVisible(R.id.reports_option, false);
            navigationView.getMenu().setGroupVisible(R.id.main_sales_person_option, true);

            navigationView.getMenu().setGroupVisible(R.id.manager_salesperson_option, false);

        }
        else if(id==R.id.nav_attendance_salesperson)
        {
            setDefaultVisitTaskSalesPerson();
            setDefaultCallsTaskSalesPerson();
            setDefaultRequestSalesPerson();
            setDefaultSalesPersonExpenses();
            setDefaultLeadSalesPerson();
            setDefaultCustomerFeedbackSalesPerson();
        }
        else if(id==R.id.nav_visit_tasks_salesperson)
        {
            navigationView.getMenu().getItem(29).setVisible(true);
            navigationView.getMenu().getItem(30).setVisible(true);
            setDefaultCallsTaskSalesPerson();
            setDefaultRequestSalesPerson();
            setDefaultSalesPersonExpenses();
            setDefaultLeadSalesPerson();
            setDefaultCustomerFeedbackSalesPerson();
        }
        else if(id==R.id.nav_view_visit_tasks_salesperson)
        {

        }
        else if(id==R.id.nav_add_visit_tasks_salesperson)
        {

        }
        else if(id==R.id.nav_call_task_salesperson)
        {

            navigationView.getMenu().getItem(32).setVisible(true);
            navigationView.getMenu().getItem(33).setVisible(true);
            setDefaultVisitTaskSalesPerson();
            setDefaultRequestSalesPerson();
            setDefaultSalesPersonExpenses();
            setDefaultLeadSalesPerson();
            setDefaultCustomerFeedbackSalesPerson();
        }
        else if(id==R.id.nav_view_call_task_salesperson)
        {

        }
        else if(id==R.id.nav_add_call_task_salesperson)
        {

        }
        else if(id==R.id.nav_create_visit_task_salesperson)
        {
            setDefaultVisitTaskSalesPerson();
            setDefaultCallsTaskSalesPerson();
            setDefaultRequestSalesPerson();
            setDefaultSalesPersonExpenses();
            setDefaultLeadSalesPerson();
            setDefaultCustomerFeedbackSalesPerson();
        }
        else if(id==R.id.nav_request_salesperson)
        {
            navigationView.getMenu().getItem(36).setVisible(true);
            navigationView.getMenu().getItem(37).setVisible(true);
            setDefaultVisitTaskSalesPerson();
            setDefaultCallsTaskSalesPerson();
            setDefaultSalesPersonExpenses();
            setDefaultLeadSalesPerson();
            setDefaultCustomerFeedbackSalesPerson();
        }
        else if(id==R.id.nav_view_request_salesperson)
        {

        }
        else if(id==R.id.nav_add_request_salesperson)
        {

        }
        else if(id==R.id.nav_lead_salesperson)
        {
            navigationView.getMenu().getItem(39).setVisible(true);
            navigationView.getMenu().getItem(40).setVisible(true);
            setDefaultVisitTaskSalesPerson();
            setDefaultCallsTaskSalesPerson();
            setDefaultRequestSalesPerson();
            setDefaultSalesPersonExpenses();
            setDefaultCustomerFeedbackSalesPerson();
        }else if(id==R.id.nav_view_lead_salesperson)
        {

        }else if(id==R.id.nav_add_lead_salesperson)
        {

        }else if(id==R.id.nav_expenses_salesperson)
        {
            navigationView.getMenu().getItem(42).setVisible(true);
            navigationView.getMenu().getItem(43).setVisible(true);
            setDefaultVisitTaskSalesPerson();
            setDefaultCallsTaskSalesPerson();
            setDefaultRequestSalesPerson();
            setDefaultLeadSalesPerson();
            setDefaultCustomerFeedbackSalesPerson();
        }
        else if (id==R.id.nav_view_expenses_salesperson)
        {

        }
        else if (id==R.id.nav_add_expenses_salesperson)
        {

        }
        else if(id==R.id.nav_view_totalcollection)
        {
            setDefaultVisitTaskSalesPerson();
            setDefaultCallsTaskSalesPerson();
            setDefaultRequestSalesPerson();
            setDefaultSalesPersonExpenses();
            setDefaultLeadSalesPerson();
            setDefaultCustomerFeedbackSalesPerson();
        }
        else if (id==R.id.nav_customerfeedback_salesperson)
        {
            setDefaultVisitTaskSalesPerson();
            setDefaultCallsTaskSalesPerson();
            setDefaultRequestSalesPerson();
            setDefaultSalesPersonExpenses();
            setDefaultLeadSalesPerson();
            navigationView.getMenu().getItem(46).setVisible(true);
            navigationView.getMenu().getItem(47).setVisible(true);
        }
        else if (id==R.id.nav_view_customerfeedback_salesperson)
        {

        }else if(id==R.id.nav_add_customerfeedback_salesperson)
        {

        }

        return true;
    }

    private void setDefaultDrawer(){
        navigationView.getMenu().setGroupVisible(R.id.manager_option, false);
        navigationView.getMenu().setGroupVisible(R.id.main_option, true);
        navigationView.getMenu().setGroupVisible(R.id.reports_option, false);
    }

    private void setDefaultVisitTaskSalesPerson(){
        navigationView.getMenu().getItem(29).setVisible(false);
        navigationView.getMenu().getItem(30).setVisible(false);
    }

    private void setDefaultCallsTaskSalesPerson(){
        navigationView.getMenu().getItem(32).setVisible(false);
        navigationView.getMenu().getItem(33).setVisible(false);
    }

    private void setDefaultRequestSalesPerson(){
        navigationView.getMenu().getItem(36).setVisible(false);
        navigationView.getMenu().getItem(37).setVisible(false);
    }

    private void setDefaultLeadSalesPerson(){
        navigationView.getMenu().getItem(39).setVisible(false);
        navigationView.getMenu().getItem(40).setVisible(false);
    }

    private void setDefaultCustomerFeedbackSalesPerson(){
        navigationView.getMenu().getItem(46).setVisible(false);
        navigationView.getMenu().getItem(47).setVisible(false);
    }

    private void setDefaultSalesPersonExpenses(){

        navigationView.getMenu().getItem(42).setVisible(false);
        navigationView.getMenu().getItem(43).setVisible(false);
    }

    private void setDefaultExpenses(){

        navigationView.getMenu().getItem(21).setVisible(false);
        navigationView.getMenu().getItem(22).setVisible(false);
    }

    private void setDefaultManageSalesPersonTarget(){
        navigationView.getMenu().getItem(18).setVisible(false);
        navigationView.getMenu().getItem(19).setVisible(false);
    }

    private void setDefaultManageSalesPerson(){
        navigationView.getMenu().getItem(15).setVisible(false);
        navigationView.getMenu().getItem(16).setVisible(false);
    }

    private void setDefaultManageClient(){
        navigationView.getMenu().getItem(12).setVisible(false);
        navigationView.getMenu().getItem(13).setVisible(false);
    }

    private void setDefaultManageTask(){
        navigationView.getMenu().getItem(10).setVisible(false);
        navigationView.getMenu().getItem(9).setVisible(false);
        navigationView.getMenu().getItem(8).setVisible(false);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
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

    public void attendanceFragment(){
      //  navigationView.getMenu().getItem(0).setChecked(true);

        AttendanceManagerFragment attendancefragment = new AttendanceManagerFragment();
        FragmentTransaction atransaction = getSupportFragmentManager().beginTransaction();
        atransaction.replace(R.id.fragment_Container, attendancefragment);
        atransaction.commit();
    }

    public void viewMeetingTaskFragment(){
        ViewMeetingTaskManagerFragment vfragment = new ViewMeetingTaskManagerFragment();
        FragmentTransaction atransaction = getSupportFragmentManager().beginTransaction();
        atransaction.replace(R.id.fragment_Container, vfragment);
        atransaction.commit();
    }

    public void viewSaleCallTaskFragment(){
        ViewSalesCallTaskFragment vsfragment = new ViewSalesCallTaskFragment();
        FragmentTransaction atransaction = getSupportFragmentManager().beginTransaction();
        atransaction.replace(R.id.fragment_Container, vsfragment);
        atransaction.commit();
    }

    public void totalCollectionManagerFragment(){
        ViewTotalCollectionFragment vfragment = new ViewTotalCollectionFragment();
        FragmentTransaction atransaction = getSupportFragmentManager().beginTransaction();
        atransaction.replace(R.id.fragment_Container, vfragment);
        atransaction.commit();
    }
}
