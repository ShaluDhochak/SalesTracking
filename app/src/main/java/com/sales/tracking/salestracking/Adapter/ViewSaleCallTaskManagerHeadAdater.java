package com.sales.tracking.salestracking.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sales.tracking.salestracking.Bean.SalesCallTaskManagerBean;
import com.sales.tracking.salestracking.Fragment.ViewSalesCallTaskFragment;
import com.sales.tracking.salestracking.R;

import java.util.List;

public class ViewSaleCallTaskManagerHeadAdater extends RecyclerView.Adapter<ViewSaleCallTaskManagerHeadAdater.MyViewHolder> {
    private List<SalesCallTaskManagerBean.All_Service_Calls_Mgr> tasksList;
    Context context;
    ViewSalesCallTaskFragment viewSalesCallTaskFragment;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView timeVisitTaskMeetingHeading_mgrhead_tv,timeValueVisitTaskMeeting_mgrhead_tv, dateValueVisitTaskMeeting_mgrhead_tv;
        ImageView plusVisitTaskMeeting_mgrhead_iv;

        MyViewHolder(View view) {
            super(view);

            dateValueVisitTaskMeeting_mgrhead_tv = (TextView) view.findViewById(R.id.dateValueVisitTaskMeeting_mgrhead_tv);
            timeValueVisitTaskMeeting_mgrhead_tv = (TextView) view.findViewById(R.id.timeValueVisitTaskMeeting_mgrhead_tv);
            timeVisitTaskMeetingHeading_mgrhead_tv = (TextView) view.findViewById(R.id.timeVisitTaskMeetingHeading_mgrhead_tv);

            plusVisitTaskMeeting_mgrhead_iv = (ImageView) view.findViewById(R.id.plusVisitTaskMeeting_mgrhead_iv);

        }
    }

    public ViewSaleCallTaskManagerHeadAdater(Context context,List<SalesCallTaskManagerBean.All_Service_Calls_Mgr> tasksList, ViewSalesCallTaskFragment viewSalesCallTaskFragment) {
        this.tasksList = tasksList;
        this.context = context;
        this.viewSalesCallTaskFragment = viewSalesCallTaskFragment;
    }

    @Override
    public ViewSaleCallTaskManagerHeadAdater.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.visit_taskmeeting_mgrhead_list, parent, false);
        return new ViewSaleCallTaskManagerHeadAdater.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewSaleCallTaskManagerHeadAdater.MyViewHolder holder, final int position) {
        SalesCallTaskManagerBean.All_Service_Calls_Mgr bean = tasksList.get(position);

        holder.timeVisitTaskMeetingHeading_mgrhead_tv.setText("Client Name");
        holder.timeValueVisitTaskMeeting_mgrhead_tv.setText(bean.getLead_company());

        String date = bean.getService_createddt();
        String[] date1 = date.split(" ");
        holder.dateValueVisitTaskMeeting_mgrhead_tv.setText(date1[0]);

        holder.plusVisitTaskMeeting_mgrhead_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewSalesCallTaskFragment.getViewSaleCallManagerTask(tasksList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return tasksList.size();
    }

}



