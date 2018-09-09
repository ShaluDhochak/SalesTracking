package com.sales.tracking.salestracking.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sales.tracking.salestracking.Bean.CustomerFeedbackBean;
import com.sales.tracking.salestracking.Bean.LeadSpBean;
import com.sales.tracking.salestracking.Fragment.ViewCustomerFeedbackFragment;
import com.sales.tracking.salestracking.Fragment.ViewLeadSpFragment;
import com.sales.tracking.salestracking.R;

import java.util.List;

public class CustomerFeedbackAdapter  extends RecyclerView.Adapter<CustomerFeedbackAdapter.MyViewHolder> {
    private List<CustomerFeedbackBean.Customer_Feedback> requestsList;
    Context context;
    ViewCustomerFeedbackFragment viewCustomerFeedbackFragment;

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

    public CustomerFeedbackAdapter(Context context,List<CustomerFeedbackBean.Customer_Feedback> requestsList, ViewCustomerFeedbackFragment viewCustomerFeedbackFragment) {
        this.requestsList = requestsList;
        this.context = context;
        this.viewCustomerFeedbackFragment = viewCustomerFeedbackFragment;
    }

    @Override
    public CustomerFeedbackAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customer_feedback_layout, parent, false);
        return new CustomerFeedbackAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CustomerFeedbackAdapter.MyViewHolder holder, final int position) {
        CustomerFeedbackBean.Customer_Feedback bean = requestsList.get(position);

        holder.cfClientCompanyName_tv.setText(bean.getLead_company());

        holder.deleteCfClientCompanyName_iv.setVisibility(View.VISIBLE);

        holder.plusCfClientCompanyName_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewCustomerFeedbackFragment.getCustomerFeedbackData(requestsList.get(position));
            }
        });

        holder.deleteCfClientCompanyName_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewCustomerFeedbackFragment.deleteCustomerFeedbackData(requestsList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return requestsList.size();
    }

}

