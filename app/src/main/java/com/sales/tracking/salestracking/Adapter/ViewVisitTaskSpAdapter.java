package com.sales.tracking.salestracking.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sales.tracking.salestracking.Bean.TaskMeetingBean;
import com.sales.tracking.salestracking.Bean.VisitTaskSpBean;
import com.sales.tracking.salestracking.Fragment.ViewMeetingTaskManagerFragment;
import com.sales.tracking.salestracking.Fragment.ViewVisitTaskSpFragment;
import com.sales.tracking.salestracking.R;

import java.util.List;

public class ViewVisitTaskSpAdapter  extends RecyclerView.Adapter<ViewVisitTaskSpAdapter.MyViewHolder> {
    private List<VisitTaskSpBean.Single_sp_all_Meetings> tasksList;
    Context context;
    ViewVisitTaskSpFragment viewVisitTaskSpFragment;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView dateValueVisitTaskMeeting_tv,timeValueVisitTaskMeeting_tv, timeVisitTaskMeetingHeading_tv;
        ImageView plusVisitTaskMeeting_iv,editVisitTaskMeeting_iv, deleteVisitTaskMeeting_iv;

        MyViewHolder(View view) {
            super(view);

            timeValueVisitTaskMeeting_tv = (TextView) view.findViewById(R.id.timeValueVisitTaskMeeting_tv);
            dateValueVisitTaskMeeting_tv = (TextView) view.findViewById(R.id.dateValueVisitTaskMeeting_tv);
            timeVisitTaskMeetingHeading_tv = (TextView) view.findViewById(R.id.timeVisitTaskMeetingHeading_tv);

            plusVisitTaskMeeting_iv = (ImageView) view.findViewById(R.id.plusVisitTaskMeeting_iv);
            editVisitTaskMeeting_iv = (ImageView) view.findViewById(R.id.editVisitTaskMeeting_iv);
            deleteVisitTaskMeeting_iv = (ImageView) view.findViewById(R.id.deleteVisitTaskMeeting_iv);

        }
    }

    public ViewVisitTaskSpAdapter(Context context,List<VisitTaskSpBean.Single_sp_all_Meetings> tasksList, ViewVisitTaskSpFragment viewVisitTaskSpFragment) {
        this.tasksList = tasksList;
        this.context = context;
        this.viewVisitTaskSpFragment = viewVisitTaskSpFragment;
    }

    @Override
    public ViewVisitTaskSpAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.visit_taskmeeting_list, parent, false);
        return new ViewVisitTaskSpAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewVisitTaskSpAdapter.MyViewHolder holder, final int position) {
        VisitTaskSpBean.Single_sp_all_Meetings bean = tasksList.get(position);

        holder.timeVisitTaskMeetingHeading_tv.setText("Client Name");
        String date = bean.getVisit_datetime();
        String[] date1 = date.split(" ");
        holder.dateValueVisitTaskMeeting_tv.setText(date1[0]);

        String time = date1[1];
        holder.timeValueVisitTaskMeeting_tv.setText(bean.getLead_company());

        holder.editVisitTaskMeeting_iv.setVisibility(View.GONE);
        holder.deleteVisitTaskMeeting_iv.setVisibility(View.GONE);

        holder.plusVisitTaskMeeting_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewVisitTaskSpFragment.getViewVisitSpTask(tasksList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return tasksList.size();
    }

}


