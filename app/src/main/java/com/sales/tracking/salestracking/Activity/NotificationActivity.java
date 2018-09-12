package com.sales.tracking.salestracking.Activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.astuetz.PagerSlidingTabStrip;
import com.sales.tracking.salestracking.Adapter.NotificationFragmentPageAdapter;
import com.sales.tracking.salestracking.Fragment.CallsPendingNotificationFragment;
import com.sales.tracking.salestracking.Fragment.TargetFragment;
import com.sales.tracking.salestracking.Fragment.TrackSalesPersonActivity;
import com.sales.tracking.salestracking.Fragment.ViewSalesCallTaskFragment;
import com.sales.tracking.salestracking.Fragment.ViewVisitTaskSpFragment;
import com.sales.tracking.salestracking.Fragment.VisitPendingNotificationFragment;
import com.sales.tracking.salestracking.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotificationActivity extends AppCompatActivity {

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.tabs)
    PagerSlidingTabStrip tabs;

    NotificationFragmentPageAdapter notificationFragmentPageAdapter;

    @BindView(R.id.drawerIcon_iv)
    ImageView drawerIcon_iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);
        initialiseUI();
    }

    private void initialiseUI(){

      //  viewPager = (ViewPager) findViewById(R.id.viewPager);
      //  tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);

        TargetFragment targetFragment = new TargetFragment();
        CallsPendingNotificationFragment callsPending = new CallsPendingNotificationFragment();
        VisitPendingNotificationFragment visitPending = new VisitPendingNotificationFragment();

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(targetFragment);
        fragmentList.add(callsPending);
        fragmentList.add(visitPending);

        notificationFragmentPageAdapter = new NotificationFragmentPageAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(notificationFragmentPageAdapter);
        tabs.setViewPager(viewPager);

    }

    @OnClick(R.id.drawerIcon_iv)
    public void navigationDrawer(){
        Intent backIntent  =new Intent(NotificationActivity.this, NavigationDrawerActivity.class);
        backIntent.putExtra("drawer_Open", "open_track_sales" );
        startActivity(backIntent);
    }



}
