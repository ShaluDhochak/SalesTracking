package com.sales.tracking.salestracking.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sales.tracking.salestracking.Bean.SalesCallTaskSpBean;
import com.sales.tracking.salestracking.Bean.VisitTaskSpBean;
import com.sales.tracking.salestracking.Fragment.CallsPendingNotificationFragment;
import com.sales.tracking.salestracking.Fragment.VisitPendingNotificationFragment;
import com.sales.tracking.salestracking.R;

import java.util.List;

public class CallPendingAdapter  extends RecyclerView.Adapter<CallPendingAdapter.MyViewHolder> {
    private List<SalesCallTaskSpBean.Sp_All_Service_Calls> tasksList;
    Context context;
    CallsPendingNotificationFragment viewVisitTaskSpFragment;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView dateValueVisitTaskMeeting_tv,timeValueVisitTaskMeeting_tv, timeVisitTaskMeetingHeading_tv;
        ImageView plusVisitTaskMeeting_iv,editVisitTaskMeeting_iv, deleteVisitTaskMeeting_iv;

        CardView visitTaskMeetingDetails_cv;

        MyViewHolder(View view) {
            super(view);

            visitTaskMeetingDetails_cv= (CardView) view.findViewById(R.id.visitTaskMeetingDetails_cv);

            timeValueVisitTaskMeeting_tv = (TextView) view.findViewById(R.id.timeValueVisitTaskMeeting_tv);
            dateValueVisitTaskMeeting_tv = (TextView) view.findViewById(R.id.dateValueVisitTaskMeeting_tv);
            timeVisitTaskMeetingHeading_tv = (TextView) view.findViewById(R.id.timeVisitTaskMeetingHeading_tv);

            plusVisitTaskMeeting_iv = (ImageView) view.findViewById(R.id.plusVisitTaskMeeting_iv);
            editVisitTaskMeeting_iv = (ImageView) view.findViewById(R.id.editVisitTaskMeeting_iv);
            deleteVisitTaskMeeting_iv = (ImageView) view.findViewById(R.id.deleteVisitTaskMeeting_iv);

        }
    }

    public CallPendingAdapter(Context context,List<SalesCallTaskSpBean.Sp_All_Service_Calls> tasksList, CallsPendingNotificationFragment viewVisitTaskSpFragment) {
        this.tasksList = tasksList;
        this.context = context;
        this.viewVisitTaskSpFragment = viewVisitTaskSpFragment;
    }


    @Override
    public CallPendingAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.visit_taskmeeting_list, parent, false);
        return new CallPendingAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CallPendingAdapter.MyViewHolder holder, final int position) {
        SalesCallTaskSpBean.Sp_All_Service_Calls bean = tasksList.get(position);

        if (bean.getService_status().equals("Pending")){
            holder.visitTaskMeetingDetails_cv.setVisibility(View.VISIBLE);
            holder.timeVisitTaskMeetingHeading_tv.setText("Client Name");
            String date = bean.getService_createddt();
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
        }else {
            holder.visitTaskMeetingDetails_cv.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return tasksList.size();
    }

}



