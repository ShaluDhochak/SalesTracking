package com.sales.tracking.salestracking.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sales.tracking.salestracking.Bean.AttendanceManagerBean;
import com.sales.tracking.salestracking.Bean.RequestSpBean;
import com.sales.tracking.salestracking.Fragment.AttendanceManagerFragment;
import com.sales.tracking.salestracking.Fragment.RequestViewFragment;
import com.sales.tracking.salestracking.R;

import java.util.List;

public class RequestSpAdapter  extends RecyclerView.Adapter<RequestSpAdapter.MyViewHolder> {
    private List<RequestSpBean.Salesperson_requests> requestsList;
    Context context;
    RequestViewFragment requestViewFragment;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView dateValueAttendance_tv,nameValueAttendance_tv, nameAttendanceHeading_tv;
        ImageView plusAttendance_iv,editAttendance_iv, deleteAttendance_iv;

        MyViewHolder(View view) {
            super(view);

            nameValueAttendance_tv = (TextView) view.findViewById(R.id.nameValueAttendance_tv);
            dateValueAttendance_tv = (TextView) view.findViewById(R.id.dateValueAttendance_tv);
            nameAttendanceHeading_tv = (TextView) view.findViewById(R.id.nameAttendanceHeading_tv);

            plusAttendance_iv = (ImageView) view.findViewById(R.id.plusAttendance_iv);
            editAttendance_iv = (ImageView) view.findViewById(R.id.editAttendance_iv);
            deleteAttendance_iv = (ImageView) view.findViewById(R.id.deleteAttendance_iv);

        }
    }

    public RequestSpAdapter(Context context,List<RequestSpBean.Salesperson_requests> requestsList, RequestViewFragment requestViewFragment) {
        this.requestsList = requestsList;
        this.context = context;
        this.requestViewFragment = requestViewFragment;
    }

    @Override
    public RequestSpAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.attendance_list_layout, parent, false);
        return new RequestSpAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RequestSpAdapter.MyViewHolder holder, final int position) {
        RequestSpBean.Salesperson_requests bean = requestsList.get(position);

        holder.dateValueAttendance_tv.setText(bean.getRequest_dt());
        holder.nameAttendanceHeading_tv.setText("Type");
        holder.nameValueAttendance_tv.setText(bean.getRequest_type());

        holder.deleteAttendance_iv.setVisibility(View.VISIBLE);

        holder.plusAttendance_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestViewFragment.getRequestData(requestsList.get(position));
            }
        });

        holder.deleteAttendance_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestViewFragment.deleteRequestDetails(requestsList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return requestsList.size();
    }

}

