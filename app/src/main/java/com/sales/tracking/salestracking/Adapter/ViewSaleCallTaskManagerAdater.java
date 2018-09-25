package com.sales.tracking.salestracking.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sales.tracking.salestracking.Bean.SalesCallTaskManagerBean;
import com.sales.tracking.salestracking.Bean.SalesCallTaskSpBean;
import com.sales.tracking.salestracking.Fragment.ViewSalesCallTaskFragment;
import com.sales.tracking.salestracking.R;

import java.util.List;

public class ViewSaleCallTaskManagerAdater  extends RecyclerView.Adapter<ViewSaleCallTaskManagerAdater.MyViewHolder> {
    private List<SalesCallTaskManagerBean.All_Service_Calls_Mgr> tasksList;
    Context context;
    ViewSalesCallTaskFragment viewSalesCallTaskFragment;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView dateValueVisitTaskMeeting_tv,timeValueVisitTaskMeeting_tv, timeVisitTaskMeetingHeading_tv;
        ImageView plusVisitTaskMeeting_iv,editVisitTaskMeeting_iv, deleteVisitTaskMeeting_iv;

        MyViewHolder(View view) {
            super(view);

            timeVisitTaskMeetingHeading_tv = (TextView) view.findViewById(R.id.timeVisitTaskMeetingHeading_tv);
            timeValueVisitTaskMeeting_tv = (TextView) view.findViewById(R.id.timeValueVisitTaskMeeting_tv);
            dateValueVisitTaskMeeting_tv = (TextView) view.findViewById(R.id.dateValueVisitTaskMeeting_tv);

            plusVisitTaskMeeting_iv = (ImageView) view.findViewById(R.id.plusVisitTaskMeeting_iv);
            editVisitTaskMeeting_iv = (ImageView) view.findViewById(R.id.editVisitTaskMeeting_iv);
            deleteVisitTaskMeeting_iv = (ImageView) view.findViewById(R.id.deleteVisitTaskMeeting_iv);

        }
    }

    public ViewSaleCallTaskManagerAdater(Context context,List<SalesCallTaskManagerBean.All_Service_Calls_Mgr> tasksList, ViewSalesCallTaskFragment viewSalesCallTaskFragment) {
        this.tasksList = tasksList;
        this.context = context;
        this.viewSalesCallTaskFragment = viewSalesCallTaskFragment;
    }

    @Override
    public ViewSaleCallTaskManagerAdater.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.visit_taskmeeting_list, parent, false);
        return new ViewSaleCallTaskManagerAdater.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewSaleCallTaskManagerAdater.MyViewHolder holder, final int position) {
        SalesCallTaskManagerBean.All_Service_Calls_Mgr bean = tasksList.get(position);

        holder.timeVisitTaskMeetingHeading_tv.setText("Client Name");
        holder.timeValueVisitTaskMeeting_tv.setText(bean.getLead_company());

        String date = bean.getService_createddt();
        String[] date1 = date.split(" ");
        holder.dateValueVisitTaskMeeting_tv.setText(date1[0]);

        holder.editVisitTaskMeeting_iv.setVisibility(View.VISIBLE);
        holder.deleteVisitTaskMeeting_iv.setVisibility(View.VISIBLE);

        holder.plusVisitTaskMeeting_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewSalesCallTaskFragment.getViewSaleCallManagerTask(tasksList.get(position));
            }
        });


        holder.deleteVisitTaskMeeting_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewSalesCallTaskFragment.getDeleteSaleCallManagerTask(tasksList.get(position));
            }
        });

        holder.editVisitTaskMeeting_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewSalesCallTaskFragment.getEditViewSaleCallManagerTask(tasksList.get(position));

            }
        });
    }

    @Override
    public int getItemCount() {
        return tasksList.size();
    }

}



