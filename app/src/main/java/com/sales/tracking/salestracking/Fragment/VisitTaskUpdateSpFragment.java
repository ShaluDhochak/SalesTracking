package com.sales.tracking.salestracking.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.sales.tracking.salestracking.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class VisitTaskUpdateSpFragment extends Fragment {

    View view;

    @BindView(R.id.vtaskUpdateVisitTaskSp_sp)
    Spinner vtaskUpdateVisitTaskSp_sp;

    @BindView(R.id.statusUpdateVisitTaskSp_sp)
    Spinner statusUpdateVisitTaskSp_sp;

    @BindView(R.id.commentUpdateVisitTaskSp_et)
    EditText commentUpdateVisitTaskSp_et;

    @BindView(R.id.dateUpdateVisitTaskSp_tv)
    TextView dateUpdateVisitTaskSp_tv;

    @BindView(R.id.timeUpdateVisitTaskSp_tv)
    TextView timeUpdateVisitTaskSp_tv;

    @BindView(R.id.submitUpdateVisitTaskSp_btn)
    Button submitUpdateVisitTaskSp_btn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_visit_task_update_sp, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        initialiseUI();
    }

    private void initialiseUI(){

    }

    @OnClick(R.id.submitUpdateVisitTaskSp_btn)
    private void updateVisitTask(){

    }
}
