package com.sales.tracking.salestracking.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.sales.tracking.salestracking.Fragment.CallsPendingNotificationFragment;
import com.sales.tracking.salestracking.Fragment.TargetFragment;
import com.sales.tracking.salestracking.Fragment.ViewSalesCallTaskFragment;
import com.sales.tracking.salestracking.Fragment.ViewVisitTaskSpFragment;
import com.sales.tracking.salestracking.Fragment.VisitPendingNotificationFragment;

import java.util.List;

public class NotificationFragmentPageAdapter  extends FragmentPagerAdapter {
    List<Fragment> mFragmentList;

    public NotificationFragmentPageAdapter(FragmentManager fm, List<Fragment> mFragmentList) {
        super(fm);
        this.mFragmentList = mFragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new TargetFragment();
                break;
            case 1:
                fragment = new CallsPendingNotificationFragment();
                break;
            case 2 :
                fragment = new VisitPendingNotificationFragment();
                break;
        }
        return fragment;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position){
        String title = "";
        switch(position){
            case 0 :
                title = "Target";
                break;
            case 1 :
                title = "Calls Pending";
                break;
            case 2 :
                title = "Visit Pending";
                break;

        }
        return title;
    }
}
