package com.sales.tracking.salestracking.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sales.tracking.salestracking.Bean.ManagerBean;
import com.sales.tracking.salestracking.Bean.ViewUserManagerHeadBean;
import com.sales.tracking.salestracking.Fragment.ViewClientManagerFragment;
import com.sales.tracking.salestracking.Fragment.ViewUserManagerHeadFragment;
import com.sales.tracking.salestracking.R;

import java.util.List;

public class ManagerUsersManagerHeadAdapter extends RecyclerView.Adapter<ManagerUsersManagerHeadAdapter.MyViewHolder> {
    private List<ViewUserManagerHeadBean.Users> requestsList;
    Context context;
    ViewUserManagerHeadFragment viewUserManagerHeadFragment;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView cfClientCompanyName_MgrHead_tv, cfClientCompanyNameHeading_MgrHead_tv;
        ImageView plusCfClientCompanyName_MgrHead_iv;

        MyViewHolder(View view) {
            super(view);

            cfClientCompanyName_MgrHead_tv = (TextView) view.findViewById(R.id.cfClientCompanyName_MgrHead_tv);
            cfClientCompanyNameHeading_MgrHead_tv = (TextView) view.findViewById(R.id.cfClientCompanyNameHeading_MgrHead_tv);

            plusCfClientCompanyName_MgrHead_iv = (ImageView) view.findViewById(R.id.plusCfClientCompanyName_MgrHead_iv);

        }
    }

    public ManagerUsersManagerHeadAdapter(Context context,List<ViewUserManagerHeadBean.Users> requestsList, ViewUserManagerHeadFragment viewUserManagerHeadFragment) {
        this.requestsList = requestsList;
        this.context = context;
        this.viewUserManagerHeadFragment = viewUserManagerHeadFragment;
    }

    @Override
    public ManagerUsersManagerHeadAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customer_feedback_mgrhead_layout, parent, false);
        return new ManagerUsersManagerHeadAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ManagerUsersManagerHeadAdapter.MyViewHolder holder, final int position) {
        ViewUserManagerHeadBean.Users bean = requestsList.get(position);

        holder.cfClientCompanyName_MgrHead_tv.setText(bean.getUser_name());
        holder.cfClientCompanyNameHeading_MgrHead_tv.setText("Name");

        holder.plusCfClientCompanyName_MgrHead_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewUserManagerHeadFragment.getUserData(requestsList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return requestsList.size();
    }

}


