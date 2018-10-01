package com.sales.tracking.salestracking.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sales.tracking.salestracking.Bean.ManagerBean;
import com.sales.tracking.salestracking.Fragment.ViewClientManagerFragment;
import com.sales.tracking.salestracking.R;

import java.util.List;

public class ManagerClientManagerHeadAdapter  extends RecyclerView.Adapter<ManagerClientManagerHeadAdapter.MyViewHolder> {
    private List<ManagerBean.clients> requestsList;
    Context context;
    ViewClientManagerFragment viewClientManagerFragment;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView cfClientCompanyName_MgrHead_tv;
        ImageView plusCfClientCompanyName_MgrHead_iv;

        MyViewHolder(View view) {
            super(view);

            cfClientCompanyName_MgrHead_tv = (TextView) view.findViewById(R.id.cfClientCompanyName_MgrHead_tv);

            plusCfClientCompanyName_MgrHead_iv = (ImageView) view.findViewById(R.id.plusCfClientCompanyName_MgrHead_iv);

        }
    }

    public ManagerClientManagerHeadAdapter(Context context,List<ManagerBean.clients> requestsList, ViewClientManagerFragment viewClientManagerFragment) {
        this.requestsList = requestsList;
        this.context = context;
        this.viewClientManagerFragment = viewClientManagerFragment;
    }

    @Override
    public ManagerClientManagerHeadAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customer_feedback_mgrhead_layout, parent, false);
        return new ManagerClientManagerHeadAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ManagerClientManagerHeadAdapter.MyViewHolder holder, final int position) {
        ManagerBean.clients bean = requestsList.get(position);

        holder.cfClientCompanyName_MgrHead_tv.setText(bean.getLead_company());

        holder.plusCfClientCompanyName_MgrHead_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewClientManagerFragment.getClientData(requestsList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return requestsList.size();
    }

}

