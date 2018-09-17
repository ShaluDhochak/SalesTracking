package com.sales.tracking.salestracking.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sales.tracking.salestracking.Bean.AttendanceManagerBean;
import com.sales.tracking.salestracking.Bean.ManagerReportBean;
import com.sales.tracking.salestracking.Fragment.AttendanceManagerFragment;
import com.sales.tracking.salestracking.Fragment.AttendanceReportFragment;
import com.sales.tracking.salestracking.R;

import java.util.List;

public class AttendanceReportManagerAddapter extends RecyclerView.Adapter<AttendanceReportManagerAddapter.MyViewHolder> {
    private List<ManagerReportBean.Sp_Att_Advsearch> attendanceList;
    Context context;
    AttendanceReportFragment attendanceReportFragment;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView dateValueAttendance_tv,nameValueAttendance_tv;
        ImageView plusAttendance_iv,editAttendance_iv, deleteAttendance_iv;

        MyViewHolder(View view) {
            super(view);

            nameValueAttendance_tv = (TextView) view.findViewById(R.id.nameValueAttendance_tv);
            dateValueAttendance_tv = (TextView) view.findViewById(R.id.dateValueAttendance_tv);

            plusAttendance_iv = (ImageView) view.findViewById(R.id.plusAttendance_iv);
            editAttendance_iv = (ImageView) view.findViewById(R.id.editAttendance_iv);
            deleteAttendance_iv = (ImageView) view.findViewById(R.id.deleteAttendance_iv);

        }
    }

    public AttendanceReportManagerAddapter(Context context,List<ManagerReportBean.Sp_Att_Advsearch> attendanceList, AttendanceReportFragment attendanceReportFragment) {
        this.attendanceList = attendanceList;
        this.context = context;
        this.attendanceReportFragment = attendanceReportFragment;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.attendance_list_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        ManagerReportBean.Sp_Att_Advsearch bean = attendanceList.get(position);

        String date = bean.getAtten_in_datetime();
        String[] date1 = date.split(" ");
        holder.dateValueAttendance_tv.setText(date1[0]);
        holder.nameValueAttendance_tv.setText(bean.getUser_name());
        holder.editAttendance_iv.setVisibility(View.GONE);

        holder.plusAttendance_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  attendanceReportFragment.getAttendanceData(attendanceList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return attendanceList.size();
    }

}


