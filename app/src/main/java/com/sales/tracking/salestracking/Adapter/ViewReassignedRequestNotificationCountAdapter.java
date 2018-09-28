package com.sales.tracking.salestracking.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sales.tracking.salestracking.Bean.ReassignRequestNotificationBean;
import com.sales.tracking.salestracking.Bean.VieqRequestReassignedBean;
import com.sales.tracking.salestracking.Fragment.ViewReassignManagerFragment;
import com.sales.tracking.salestracking.R;

import java.util.List;

public class ViewReassignedRequestNotificationCountAdapter  extends RecyclerView.Adapter<ViewReassignedRequestNotificationCountAdapter.MyViewHolder> {
    private List<ReassignRequestNotificationBean.Select_Pending_Requests> tasksList;

    Context context;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView visitStatusRequestNotification_tv,visitStatusDateRequestNotification_tv, requestByStatusRequestNotification_tv;

        MyViewHolder(View view) {
            super(view);

           visitStatusRequestNotification_tv = (TextView) view.findViewById(R.id.visitStatusRequestNotification_tv);
            visitStatusDateRequestNotification_tv = (TextView) view.findViewById(R.id.visitStatusDateRequestNotification_tv);
            requestByStatusRequestNotification_tv = (TextView) view.findViewById(R.id.requestByStatusRequestNotification_tv);

        }
    }

    public ViewReassignedRequestNotificationCountAdapter(Context context, List<ReassignRequestNotificationBean.Select_Pending_Requests> tasksList) {
        this.tasksList = tasksList;
        this.context = context;
    }

    @Override
    public ViewReassignedRequestNotificationCountAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.visit_notification_layout, parent, false);
        return new ViewReassignedRequestNotificationCountAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewReassignedRequestNotificationCountAdapter.MyViewHolder holder, final int position) {
        ReassignRequestNotificationBean.Select_Pending_Requests bean = tasksList.get(position);

        holder.visitStatusRequestNotification_tv.setText( "Reassign " + bean.getRequest_type());

        String date = bean.getRequest_createddt();
        String[] date1 = date.split(" ");
        holder.visitStatusDateRequestNotification_tv.setText(date1[0]);
        holder.requestByStatusRequestNotification_tv.setText("Request by " + bean.getUser_name());

    }

    @Override
    public int getItemCount() {
        return tasksList.size();
    }

}
