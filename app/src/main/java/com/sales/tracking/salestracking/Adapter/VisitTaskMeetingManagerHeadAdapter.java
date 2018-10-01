package com.sales.tracking.salestracking.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sales.tracking.salestracking.Bean.TaskMeetingBean;
import com.sales.tracking.salestracking.Fragment.ViewMeetingTaskManagerFragment;
import com.sales.tracking.salestracking.R;

import java.util.List;

public class VisitTaskMeetingManagerHeadAdapter extends RecyclerView.Adapter<VisitTaskMeetingManagerHeadAdapter.MyViewHolder> {
    private List<TaskMeetingBean.All_Meetings_Mgr> tasksList;
    Context context;
    ViewMeetingTaskManagerFragment viewMeetingTaskManagerFragment;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView timeValueVisitTaskMeeting_mgrhead_tv, dateValueVisitTaskMeeting_mgrhead_tv;
        ImageView plusVisitTaskMeeting_mgrhead_iv;

        MyViewHolder(View view) {
            super(view);

            dateValueVisitTaskMeeting_mgrhead_tv = (TextView) view.findViewById(R.id.dateValueVisitTaskMeeting_mgrhead_tv);
            timeValueVisitTaskMeeting_mgrhead_tv = (TextView) view.findViewById(R.id.timeValueVisitTaskMeeting_mgrhead_tv);

            plusVisitTaskMeeting_mgrhead_iv = (ImageView) view.findViewById(R.id.plusVisitTaskMeeting_mgrhead_iv);

        }
    }

    public VisitTaskMeetingManagerHeadAdapter(Context context,List<TaskMeetingBean.All_Meetings_Mgr> tasksList, ViewMeetingTaskManagerFragment viewMeetingTaskManagerFragment) {
        this.tasksList = tasksList;
        this.context = context;
        this.viewMeetingTaskManagerFragment = viewMeetingTaskManagerFragment;
    }

    @Override
    public VisitTaskMeetingManagerHeadAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.visit_taskmeeting_mgrhead_list, parent, false);
        return new VisitTaskMeetingManagerHeadAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final VisitTaskMeetingManagerHeadAdapter.MyViewHolder holder, final int position) {
        TaskMeetingBean.All_Meetings_Mgr bean = tasksList.get(position);

        String date = bean.getVisit_datetime();
        String[] date1 = date.split(" ");
        holder.dateValueVisitTaskMeeting_mgrhead_tv.setText(date1[0]);

        String time = date1[1];
        holder.timeValueVisitTaskMeeting_mgrhead_tv.setText(convertIn12Hours(time));

        holder.plusVisitTaskMeeting_mgrhead_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewMeetingTaskManagerFragment.getViewMeetingTask(tasksList.get(position));
            }
        });


    }

    private String convertIn12Hours(String time){

        String timeToDisplay = "";
        String[] timeArray = time.split(":");
        Integer hours = Integer.parseInt(timeArray[0]);

        if(hours > 12){
            timeToDisplay = (24 - hours) + ":" +  timeArray[1] + " PM";
        }else{
            timeToDisplay = timeArray[0] + ":" + timeArray[1] + " AM";
        }

        return timeToDisplay;
    }

    @Override
    public int getItemCount() {
        return tasksList.size();
    }

}



