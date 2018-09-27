package com.sales.tracking.salestracking.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sales.tracking.salestracking.Bean.LeadSpBean;
import com.sales.tracking.salestracking.Bean.RequestSpBean;
import com.sales.tracking.salestracking.Fragment.RequestViewFragment;
import com.sales.tracking.salestracking.Fragment.ViewLeadSpFragment;
import com.sales.tracking.salestracking.R;

import java.util.List;

public class LeadSpAdapter  extends RecyclerView.Adapter<LeadSpAdapter.MyViewHolder> {
    private List<LeadSpBean.Leads> requestsList;
    Context context;
    ViewLeadSpFragment viewLeadSpFragment;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView cfClientCompanyName_tv;
        ImageView plusCfClientCompanyName_iv,deleteCfClientCompanyName_iv, editCfClientCompanyName_iv;

        MyViewHolder(View view) {
            super(view);

            cfClientCompanyName_tv = (TextView) view.findViewById(R.id.cfClientCompanyName_tv);

            deleteCfClientCompanyName_iv = (ImageView) view.findViewById(R.id.deleteCfClientCompanyName_iv);
            plusCfClientCompanyName_iv = (ImageView) view.findViewById(R.id.plusCfClientCompanyName_iv);
            editCfClientCompanyName_iv = (ImageView) view.findViewById(R.id.editCfClientCompanyName_iv);

        }
    }

    public LeadSpAdapter(Context context,List<LeadSpBean.Leads> requestsList, ViewLeadSpFragment viewLeadSpFragment) {
        this.requestsList = requestsList;
        this.context = context;
        this.viewLeadSpFragment = viewLeadSpFragment;
    }

    @Override
    public LeadSpAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customer_feedback_layout, parent, false);
        return new LeadSpAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final LeadSpAdapter.MyViewHolder holder, final int position) {
        LeadSpBean.Leads bean = requestsList.get(position);

        holder.cfClientCompanyName_tv.setText(bean.getLead_company());

        holder.editCfClientCompanyName_iv.setVisibility(View.GONE);

        holder.plusCfClientCompanyName_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewLeadSpFragment.getLeadData(requestsList.get(position));
            }
        });

        holder.deleteCfClientCompanyName_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewLeadSpFragment.deleteLeadData(requestsList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return requestsList.size();
    }

}

