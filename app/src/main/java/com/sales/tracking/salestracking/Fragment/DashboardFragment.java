package com.sales.tracking.salestracking.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sales.tracking.salestracking.R;

import butterknife.BindView;


public class DashboardFragment extends Fragment {

    @BindView(R.id.dashboardDetail_rv)
    RecyclerView dashboardDetail_rv;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        initialiseUI();
        return view;
    }

    private void initialiseUI(){

    }

}
