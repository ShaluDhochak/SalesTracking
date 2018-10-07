package com.sales.tracking.salestracking.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sales.tracking.salestracking.Bean.ClientDashboardBean;
import com.sales.tracking.salestracking.Bean.ManagerBean;
import com.sales.tracking.salestracking.Fragment.ViewClientNotificationManagerFragment;
import com.sales.tracking.salestracking.R;

import java.util.List;

public class ViewClientDashboardManagerAdapter  extends RecyclerView.Adapter<ViewClientDashboardManagerAdapter.MyViewHolder> {
    private List<ClientDashboardBean.Clients> requestsList;
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

    public ViewClientDashboardManagerAdapter(Context context,List<ClientDashboardBean.Clients> requestsList, ViewClientNotificationManagerFragment viewClientManagerFragment) {
        this.requestsList = requestsList;
        this.context = context;
        this.viewClientManagerFragment = viewClientManagerFragment;
    }

    @Override
    public ViewClientDashboardManagerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customer_feedback_layout, parent, false);
        return new ViewClientDashboardManagerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewClientDashboardManagerAdapter.MyViewHolder holder, final int position) {
        ClientDashboardBean.Clients bean = requestsList.get(position);

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

