package com.sales.tracking.salestracking.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sales.tracking.salestracking.Activity.CallDashboardCountActivity;
import com.sales.tracking.salestracking.Activity.ClientDashboardCountActivity;
import com.sales.tracking.salestracking.Activity.VisitDashboardCountActivity;
import com.sales.tracking.salestracking.Bean.DashboardManagerBean;
import com.sales.tracking.salestracking.R;

import java.util.List;

public class DashboardManagerAdapter extends RecyclerView.Adapter<DashboardManagerAdapter.MyViewHolder> {
    private List<DashboardManagerBean.Dashboard_Count> tasksList;
    Context context;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView todayMeetingDashboardManager_tv,weeklyMeetingDashboardManager_tv, tillDateMeeting_tv;
        TextView todayCallsDashboardManager_tv,weeklyCallsDashboardManager_tv,tillDateCalls_tv;
        TextView todayLeadsDashboardManager_tv,weeklyLeadsDashboardManager_tv,tillDateLeads_tv;
        TextView nameDashboardHeading_tv;

        TextView nameMeetingDashboardHeading_tv,nameCallsDashboardHeading_tv,nameLeadsDashboardHeading_tv;


        MyViewHolder(View view) {
            super(view);

            nameMeetingDashboardHeading_tv = (TextView) view.findViewById(R.id.nameMeetingDashboardHeading_tv);
            nameCallsDashboardHeading_tv = (TextView) view.findViewById(R.id.nameCallsDashboardHeading_tv);
            nameLeadsDashboardHeading_tv = (TextView) view.findViewById(R.id.nameLeadsDashboardHeading_tv);

            nameDashboardHeading_tv = (TextView) view.findViewById(R.id.nameDashboardHeading_tv);
            todayMeetingDashboardManager_tv = (TextView) view.findViewById(R.id.todayMeetingDashboardManager_tv);
            weeklyMeetingDashboardManager_tv = (TextView) view.findViewById(R.id.weeklyMeetingDashboardManager_tv);
            tillDateMeeting_tv = (TextView) view.findViewById(R.id.tillDateMeeting_tv);

            todayCallsDashboardManager_tv = (TextView) view.findViewById(R.id.todayCallsDashboardManager_tv);
            weeklyCallsDashboardManager_tv = (TextView) view.findViewById(R.id.weeklyCallsDashboardManager_tv);
            tillDateCalls_tv = (TextView) view.findViewById(R.id.tillDateCalls_tv);

            todayLeadsDashboardManager_tv = (TextView) view.findViewById(R.id.todayLeadsDashboardManager_tv);
            weeklyLeadsDashboardManager_tv = (TextView) view.findViewById(R.id.weeklyLeadsDashboardManager_tv);
            tillDateLeads_tv = (TextView) view.findViewById(R.id.tillDateLeads_tv);

        }
    }

    public DashboardManagerAdapter(Context context, List<DashboardManagerBean.Dashboard_Count> tasksList) {
        this.tasksList = tasksList;
        this.context = context;
    }

    @Override
    public DashboardManagerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.manager_dashboard_layout, parent, false);
        return new DashboardManagerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DashboardManagerAdapter.MyViewHolder holder, final int position) {
        final DashboardManagerBean.Dashboard_Count bean = tasksList.get(position);

        String name = bean.getUser_name();
        String[] fname1 = name.split( " ");

        holder.nameDashboardHeading_tv.setText(fname1[0]);
        holder.todayMeetingDashboardManager_tv.setText(bean.getMeetings_today());
        holder.weeklyMeetingDashboardManager_tv.setText(bean.getMeetings_week());
        holder.tillDateMeeting_tv.setText(bean.getMeetings_till_date());

        holder.todayCallsDashboardManager_tv.setText(bean.getCalls_today());
        holder.weeklyCallsDashboardManager_tv.setText(bean.getCalls_week());
        holder.tillDateCalls_tv.setText(bean.getCalls_till_date());

        holder.todayLeadsDashboardManager_tv.setText(bean.getLeads_today());
        holder.weeklyLeadsDashboardManager_tv.setText(bean.getLeads_week());
        holder.tillDateLeads_tv.setText(bean.getLeads_till_date());

        holder.nameMeetingDashboardHeading_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VisitDashboardCountActivity.class);
                intent.putExtra("position",position);
                intent.putExtra("heading", "New Leads");
                intent.putExtra("bean", tasksList.get(position));
                //  intent.putParcelableArrayListExtra("arrayList", );
                context.startActivity(intent);
            }
        });

                holder.nameCallsDashboardHeading_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, CallDashboardCountActivity.class);
                        intent.putExtra("position",position);
                        intent.putExtra("heading", "New Leads");
                        intent.putExtra("bean", tasksList.get(position));
                        //  intent.putParcelableArrayListExtra("arrayList", );
                        context.startActivity(intent);
                    }
                });

                        holder.nameLeadsDashboardHeading_tv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, ClientDashboardCountActivity.class);
                                intent.putExtra("position",position);
                                intent.putExtra("heading", "New Leads");
                                intent.putExtra("bean", tasksList.get(position));
                                //  intent.putParcelableArrayListExtra("arrayList", );
                                context.startActivity(intent);
                            }
                        });


    }

    @Override
    public int getItemCount() {
        return tasksList.size();
    }

}
