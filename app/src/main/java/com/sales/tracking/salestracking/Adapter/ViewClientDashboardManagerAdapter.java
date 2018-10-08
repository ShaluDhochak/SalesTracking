package com.sales.tracking.salestracking.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sales.tracking.salestracking.Activity.CallDashboardCountActivity;
import com.sales.tracking.salestracking.Activity.ClientDashboardCountActivity;
import com.sales.tracking.salestracking.Bean.ClientDashboardBean;
import com.sales.tracking.salestracking.Bean.ManagerBean;
import com.sales.tracking.salestracking.Fragment.ViewClientNotificationManagerFragment;
import com.sales.tracking.salestracking.R;

import java.util.ArrayList;
import java.util.List;

public class ViewClientDashboardManagerAdapter  extends RecyclerView.Adapter<ViewClientDashboardManagerAdapter.MyViewHolder> {
    private List<ClientDashboardBean.Clients> requestsList;
    Context context;

    public ViewClientDashboardManagerAdapter(Context context, ArrayList<ClientDashboardBean.Clients> clients) {
        this.requestsList = clients;
        this.context = context;
    }

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

                ((ClientDashboardCountActivity)context).getEditClientNotifiData(requestsList.get(position));
                //viewClientManagerFragment.getEditClientNotifiData(requestsList.get(position));
            }
        });
        holder.plusCfClientCompanyName_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ClientDashboardCountActivity)context).getClientNotifiData(requestsList.get(position));

               // viewClientManagerFragment.getClientNotifiData(requestsList.get(position));
            }
        });

        holder.deleteCfClientCompanyName_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ClientDashboardCountActivity)context).deleteClientNotifiData(requestsList.get(position));

              //  viewClientManagerFragment.deleteClientNotifiData(requestsList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return requestsList.size();
    }

}

