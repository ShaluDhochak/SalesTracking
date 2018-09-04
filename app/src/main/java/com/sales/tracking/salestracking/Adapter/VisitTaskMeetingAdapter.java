package com.sales.tracking.salestracking.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sales.tracking.salestracking.Bean.DashboardSalesPersonBean;
import com.sales.tracking.salestracking.R;

import java.util.List;

public class VisitTaskMeetingAdapter  extends RecyclerView.Adapter<VisitTaskMeetingAdapter.MyViewHolder> {
    private List<DashboardSalesPersonBean.sp_meetings_today> tasksList;
    Context context;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView dateValueVisitTaskMeeting_tv,timeValueVisitTaskMeeting_tv;
        ImageView plusVisitTaskMeeting_iv,editVisitTaskMeeting_iv, deleteVisitTaskMeeting_iv;

        MyViewHolder(View view) {
            super(view);

            timeValueVisitTaskMeeting_tv = (TextView) view.findViewById(R.id.timeValueVisitTaskMeeting_tv);
            dateValueVisitTaskMeeting_tv = (TextView) view.findViewById(R.id.dateValueVisitTaskMeeting_tv);

            plusVisitTaskMeeting_iv = (ImageView) view.findViewById(R.id.plusVisitTaskMeeting_iv);
            editVisitTaskMeeting_iv = (ImageView) view.findViewById(R.id.editVisitTaskMeeting_iv);
            deleteVisitTaskMeeting_iv = (ImageView) view.findViewById(R.id.deleteVisitTaskMeeting_iv);

        }
    }

    public VisitTaskMeetingAdapter(Context context,List<DashboardSalesPersonBean.sp_meetings_today> tasksList) {
        this.tasksList = tasksList;
        this.context = context;
    }

    @Override
    public VisitTaskMeetingAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.visit_taskmeeting_list, parent, false);
        return new VisitTaskMeetingAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final VisitTaskMeetingAdapter.MyViewHolder holder, final int position) {
        DashboardSalesPersonBean.sp_meetings_today bean = tasksList.get(position);

        String date = bean.getVisit_datetime();
        String[] date1 = date.split(" ");
        holder.dateValueVisitTaskMeeting_tv.setText(date1[0]);

        String time = date1[1];
        holder.timeValueVisitTaskMeeting_tv.setText(time);

        holder.editVisitTaskMeeting_iv.setVisibility(View.VISIBLE);

        holder.plusVisitTaskMeeting_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        holder.deleteVisitTaskMeeting_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.editVisitTaskMeeting_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return tasksList.size();
    }

}


