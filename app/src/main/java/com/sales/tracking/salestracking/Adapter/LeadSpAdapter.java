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
        TextView dateValueAttendance_tv,nameValueAttendance_tv, nameAttendanceHeading_tv, dateAttendanceHeading_tv;
        ImageView plusAttendance_iv,editAttendance_iv, deleteAttendance_iv;
        RelativeLayout nameAttendance_rl;

        MyViewHolder(View view) {
            super(view);

            nameAttendance_rl = (RelativeLayout) view.findViewById(R.id.nameAttendance_rl);

            dateAttendanceHeading_tv = (TextView) view.findViewById(R.id.dateAttendanceHeading_tv);
            dateValueAttendance_tv = (TextView) view.findViewById(R.id.dateValueAttendance_tv);

            plusAttendance_iv = (ImageView) view.findViewById(R.id.plusAttendance_iv);
            editAttendance_iv = (ImageView) view.findViewById(R.id.editAttendance_iv);
            deleteAttendance_iv = (ImageView) view.findViewById(R.id.deleteAttendance_iv);
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
                .inflate(R.layout.attendance_list_layout, parent, false);
        return new LeadSpAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final LeadSpAdapter.MyViewHolder holder, final int position) {
        LeadSpBean.Leads bean = requestsList.get(position);

        holder.dateAttendanceHeading_tv.setText("Client Company Name");
        holder.dateValueAttendance_tv.setText(bean.getLead_company());

        holder.nameAttendance_rl.setVisibility(View.GONE);
        holder.deleteAttendance_iv.setVisibility(View.GONE);

        holder.plusAttendance_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewLeadSpFragment.getLeadData(requestsList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return requestsList.size();
    }

}

