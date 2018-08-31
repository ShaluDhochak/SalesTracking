package com.sales.tracking.salestracking.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sales.tracking.salestracking.Bean.DashboardSalesPersonBean;
import com.sales.tracking.salestracking.R;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TodaysTaskSalesPersonAdapter extends RecyclerView.Adapter<TodaysTaskSalesPersonAdapter.MyViewHolder> {
    private List<DashboardSalesPersonBean.sp_meetings_today> tasksList;
    Context context;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView clientNameValueTodaysTask_tv,timeValueTodaysTask_tv, dateValueTodaysTask_tv;
        ImageView plusTodaysTask_iv,minusTodaysTask_iv;

        MyViewHolder(View view) {
            super(view);
            clientNameValueTodaysTask_tv = (TextView) view.findViewById(R.id.clientNameValueTodaysTask_tv);
            timeValueTodaysTask_tv = (TextView) view.findViewById(R.id.timeValueTodaysTask_tv);
            dateValueTodaysTask_tv = (TextView) view.findViewById(R.id.dateValueTodaysTask_tv);

            plusTodaysTask_iv = (ImageView) view.findViewById(R.id.plusTodaysTask_iv);
            minusTodaysTask_iv = (ImageView) view.findViewById(R.id.minusTodaysTask_iv);
        }
    }

    public TodaysTaskSalesPersonAdapter(Context context,List<DashboardSalesPersonBean.sp_meetings_today> tasksList) {
        this.tasksList = tasksList;
        this.context = context;
    }

    @Override
    public TodaysTaskSalesPersonAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sales_person_today_task_layout, parent, false);
        return new TodaysTaskSalesPersonAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TodaysTaskSalesPersonAdapter.MyViewHolder holder, final int position) {
        DashboardSalesPersonBean.sp_meetings_today bean = tasksList.get(position);

        holder.clientNameValueTodaysTask_tv.setText(bean.getLead_company());

        String date = bean.getVisit_datetime();
        String[] date1 = date.split(" ");
        holder.dateValueTodaysTask_tv.setText(date1[0]);

        String time = date1[1];
        holder.timeValueTodaysTask_tv.setText(time);

        holder.minusTodaysTask_iv.setVisibility(View.GONE);

        holder.plusTodaysTask_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.plusTodaysTask_iv.setVisibility(View.GONE);
                holder.minusTodaysTask_iv.setVisibility(View.VISIBLE);
            }
        });


        holder.minusTodaysTask_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.plusTodaysTask_iv.setVisibility(View.VISIBLE);
                holder.minusTodaysTask_iv.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasksList.size();
    }

}
