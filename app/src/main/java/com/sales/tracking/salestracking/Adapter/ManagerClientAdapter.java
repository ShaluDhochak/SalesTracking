package com.sales.tracking.salestracking.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sales.tracking.salestracking.Bean.LeadSpBean;
import com.sales.tracking.salestracking.Bean.ManagerBean;
import com.sales.tracking.salestracking.Fragment.ViewClientManagerFragment;
import com.sales.tracking.salestracking.Fragment.ViewLeadSpFragment;
import com.sales.tracking.salestracking.R;

import java.util.List;

public class ManagerClientAdapter  extends RecyclerView.Adapter<ManagerClientAdapter.MyViewHolder> {
    private List<ManagerBean.clients> requestsList;
    Context context;
    ViewClientManagerFragment viewClientManagerFragment;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView cfClientCompanyName_tv;
        ImageView plusCfClientCompanyName_iv,deleteCfClientCompanyName_iv;

        MyViewHolder(View view) {
            super(view);

            cfClientCompanyName_tv = (TextView) view.findViewById(R.id.cfClientCompanyName_tv);

            deleteCfClientCompanyName_iv = (ImageView) view.findViewById(R.id.deleteCfClientCompanyName_iv);
            plusCfClientCompanyName_iv = (ImageView) view.findViewById(R.id.plusCfClientCompanyName_iv);

        }
    }

    public ManagerClientAdapter(Context context,List<ManagerBean.clients> requestsList, ViewClientManagerFragment viewClientManagerFragment) {
        this.requestsList = requestsList;
        this.context = context;
        this.viewClientManagerFragment = viewClientManagerFragment;
    }

    @Override
    public ManagerClientAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customer_feedback_layout, parent, false);
        return new ManagerClientAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ManagerClientAdapter.MyViewHolder holder, final int position) {
        ManagerBean.clients bean = requestsList.get(position);

        holder.cfClientCompanyName_tv.setText(bean.getLead_company());

        holder.plusCfClientCompanyName_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewClientManagerFragment.getClientData(requestsList.get(position));
            }
        });

        holder.deleteCfClientCompanyName_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewClientManagerFragment.deleteClientData(requestsList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return requestsList.size();
    }

}

