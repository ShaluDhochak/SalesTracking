package com.sales.tracking.salestracking.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sales.tracking.salestracking.R;


public class VisitTaskMeetingFragment extends Fragment {

    View view;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_visit_task_meeting, container, false);
        // Inflate the layout for this fragment
        return view;
    }


}
