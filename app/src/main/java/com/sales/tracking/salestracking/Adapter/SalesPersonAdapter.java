package com.sales.tracking.salestracking.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sales.tracking.salestracking.Bean.ExpencesSpBean;
import com.sales.tracking.salestracking.Bean.ManagerUserBean;
import com.sales.tracking.salestracking.Fragment.ViewSalesPersonDetailsFragment;
import com.sales.tracking.salestracking.Fragment.ViewTotalExpensesFragment;
import com.sales.tracking.salestracking.R;

import java.util.List;

public class SalesPersonAdapter  extends RecyclerView.Adapter<SalesPersonAdapter.MyViewHolder> {
    private List<ManagerUserBean.Manager_Users> personList;
    ViewSalesPersonDetailsFragment viewSalesPersonDetailsFragment;
    Context context;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView cfClientCompanyName_tv, cfClientCompanyNameHeading_tv;
        ImageView plusCfClientCompanyName_iv,deleteCfClientCompanyName_iv;

        MyViewHolder(View view) {
            super(view);

            cfClientCompanyNameHeading_tv = (TextView) view.findViewById(R.id.cfClientCompanyNameHeading_tv);
            cfClientCompanyName_tv = (TextView) view.findViewById(R.id.cfClientCompanyName_tv);

            deleteCfClientCompanyName_iv = (ImageView) view.findViewById(R.id.deleteCfClientCompanyName_iv);
            plusCfClientCompanyName_iv = (ImageView) view.findViewById(R.id.plusCfClientCompanyName_iv);

        }
    }

    public SalesPersonAdapter(Context context, List<ManagerUserBean.Manager_Users> personList, ViewSalesPersonDetailsFragment viewSalesPersonDetailsFragment) {
        this.personList = personList;
        this.context = context;
        this.viewSalesPersonDetailsFragment = viewSalesPersonDetailsFragment;
    }

    @Override
    public SalesPersonAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customer_feedback_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SalesPersonAdapter.MyViewHolder holder, final int position) {
        ManagerUserBean.Manager_Users bean = personList.get(position);

        holder.cfClientCompanyName_tv.setText(bean.getUser_name());
        holder.cfClientCompanyNameHeading_tv.setText("Name");

        holder.plusCfClientCompanyName_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    viewSalesPersonDetailsFragment.getClientData(personList.get(position));
            }
        });

        holder.deleteCfClientCompanyName_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //    viewSalesPersonDetailsFragment.deleteClientData(personList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return personList.size();
    }

}

