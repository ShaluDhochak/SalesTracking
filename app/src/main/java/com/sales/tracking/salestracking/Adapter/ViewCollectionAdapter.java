package com.sales.tracking.salestracking.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sales.tracking.salestracking.Bean.DashboardSalesPersonBean;
import com.sales.tracking.salestracking.R;

import java.util.List;

public class ViewCollectionAdapter extends RecyclerView.Adapter<ViewCollectionAdapter.MyViewHolder> {
    private List<DashboardSalesPersonBean.sp_meetings_today> tasksList;
    Context context;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView dateValueCollection_tv,salesPersonValueCollection_tv;
        ImageView deleteCollection_iv,plusCollection_iv, minusViewLead_iv;

        CardView viewCollectionDetails_cv,collectionDetails_cv;

        MyViewHolder(View view) {
            super(view);

            viewCollectionDetails_cv = (CardView) view.findViewById(R.id.viewCollectionDetails_cv);
            collectionDetails_cv = (CardView) view.findViewById(R.id.collectionDetails_cv);

            dateValueCollection_tv = (TextView) view.findViewById(R.id.dateValueCollection_tv);
            salesPersonValueCollection_tv = (TextView) view.findViewById(R.id.salesPersonValueCollection_tv);

            deleteCollection_iv = (ImageView) view.findViewById(R.id.deleteCollection_iv);
            plusCollection_iv = (ImageView) view.findViewById(R.id.plusCollection_iv);
        }
    }

    public ViewCollectionAdapter(Context context,List<DashboardSalesPersonBean.sp_meetings_today> tasksList) {
        this.tasksList = tasksList;
        this.context = context;
    }

    @Override
    public ViewCollectionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_collection_list, parent, false);
        return new ViewCollectionAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewCollectionAdapter.MyViewHolder holder, final int position) {
        DashboardSalesPersonBean.sp_meetings_today bean = tasksList.get(position);

        holder.salesPersonValueCollection_tv.setText(bean.getLead_company());

        String date = bean.getVisit_datetime();
        String[] date1 = date.split(" ");
        holder.dateValueCollection_tv.setText(date1[0]);

        String time = date1[1];
        holder.dateValueCollection_tv.setText(time);
        holder.viewCollectionDetails_cv.setVisibility(View.GONE);
           holder.collectionDetails_cv.setVisibility(View.VISIBLE);

        holder.plusCollection_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.viewCollectionDetails_cv.setVisibility(View.VISIBLE);
                holder.collectionDetails_cv.setVisibility(View.GONE);

            }
        });

        holder.minusViewLead_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.viewCollectionDetails_cv.setVisibility(View.GONE);
                holder.collectionDetails_cv.setVisibility(View.VISIBLE);

            }
        });

        holder.deleteCollection_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }

    @Override
    public int getItemCount() {
        return tasksList.size();
    }

}
