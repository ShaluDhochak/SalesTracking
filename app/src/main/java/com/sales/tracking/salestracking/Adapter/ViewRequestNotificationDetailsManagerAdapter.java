package com.sales.tracking.salestracking.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sales.tracking.salestracking.Bean.ManagerBean;
import com.sales.tracking.salestracking.Bean.VieqRequestReassignedBean;
import com.sales.tracking.salestracking.Fragment.ViewClientManagerFragment;
import com.sales.tracking.salestracking.Fragment.ViewClientNotificationManagerFragment;
import com.sales.tracking.salestracking.Fragment.ViewRequestNotificationFragment;
import com.sales.tracking.salestracking.R;

import java.util.List;

public class ViewRequestNotificationDetailsManagerAdapter  extends RecyclerView.Adapter<ViewRequestNotificationDetailsManagerAdapter.MyViewHolder> {
    private List<ManagerBean.clients> requestsList;
    Context context;
    ViewClientNotificationManagerFragment viewClientManagerFragment;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView cfClientCompanyName_tv;
        ImageView plusCfClientCompanyName_iv,deleteCfClientCompanyName_iv, editCfClientCompanyName_iv;

        MyViewHolder(View view) {
            super(view);

            cfClientCompanyName_tv = (TextView) view.findViewById(R.id.cfClientCompanyName_tv);

            editCfClientCompanyName_iv = (ImageView) view.findViewById(R.id.editCfClientCompanyName_iv);
            deleteCfClientCompanyName_iv = (ImageView) view.findViewById(R.id.deleteCfClientCompanyName_iv);
            plusCfClientCompanyName_iv = (ImageView) view.findViewById(R.id.plusCfClientCompanyName_iv);

        }
    }

    public ViewRequestNotificationDetailsManagerAdapter(Context context,List<ManagerBean.clients> requestsList, ViewClientNotificationManagerFragment viewClientManagerFragment) {
        this.requestsList = requestsList;
        this.context = context;
        this.viewClientManagerFragment = viewClientManagerFragment;
    }

    @Override
    public ViewRequestNotificationDetailsManagerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customer_feedback_layout, parent, false);
        return new ViewRequestNotificationDetailsManagerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewRequestNotificationDetailsManagerAdapter.MyViewHolder holder, final int position) {
        ManagerBean.clients bean = requestsList.get(position);

        holder.cfClientCompanyName_tv.setText(bean.getLead_company());

        holder.editCfClientCompanyName_iv.setVisibility(View.VISIBLE);
        holder.editCfClientCompanyName_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewClientManagerFragment.getEditClientNotifiData(requestsList.get(position));
            }
        });
        holder.plusCfClientCompanyName_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewClientManagerFragment.getClientNotifiData(requestsList.get(position));
            }
        });

        holder.deleteCfClientCompanyName_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewClientManagerFragment.deleteClientNotifiData(requestsList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return requestsList.size();
    }

}

