package com.sales.tracking.salestracking.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sales.tracking.salestracking.Bean.VieqRequestReassignedBean;
import com.sales.tracking.salestracking.Fragment.ViewReassignManagerFragment;
import com.sales.tracking.salestracking.Fragment.ViewRequestNotificationFragment;
import com.sales.tracking.salestracking.R;

import java.util.List;

public class ViewRequestNotificationDetailsAdapter extends RecyclerView.Adapter<ViewRequestNotificationDetailsAdapter.MyViewHolder> {
    private List<VieqRequestReassignedBean.Mgr_sp_requests> tasksList;
    ViewRequestNotificationFragment viewRequestNotificationFragment;
    Context context;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView dateValueCollection_tv,salesPersonValueCollection_tv, salesPersonCollectionHeading_tv;
        ImageView deleteCollection_iv, plusCollection_iv;
        CardView collectionDetails_cv;

        MyViewHolder(View view) {
            super(view);

            collectionDetails_cv = (CardView) view.findViewById(R.id.collectionDetails_cv);

            salesPersonCollectionHeading_tv = (TextView) view.findViewById(R.id.salesPersonCollectionHeading_tv);
            dateValueCollection_tv = (TextView) view.findViewById(R.id.dateValueCollection_tv);
            salesPersonValueCollection_tv = (TextView) view.findViewById(R.id.salesPersonValueCollection_tv);

            deleteCollection_iv = (ImageView) view.findViewById(R.id.deleteCollection_iv);
            plusCollection_iv = (ImageView) view.findViewById(R.id.plusCollection_iv);
        }
    }

    public ViewRequestNotificationDetailsAdapter(Context context, List<VieqRequestReassignedBean.Mgr_sp_requests> tasksList,  ViewRequestNotificationFragment viewRequestNotificationFragment) {
        this.tasksList = tasksList;
        this.context = context;
        this.viewRequestNotificationFragment = viewRequestNotificationFragment;
    }

    @Override
    public ViewRequestNotificationDetailsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_collection_list, parent, false);
        return new ViewRequestNotificationDetailsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewRequestNotificationDetailsAdapter.MyViewHolder holder, final int position) {
        VieqRequestReassignedBean.Mgr_sp_requests bean = tasksList.get(position);

        holder.salesPersonValueCollection_tv.setText(bean.getUser_name());

        holder.salesPersonCollectionHeading_tv.setText("Request By");
        holder.dateValueCollection_tv.setText(bean.getRequest_dt());

        holder.collectionDetails_cv.setVisibility(View.VISIBLE);
        holder.deleteCollection_iv.setVisibility(View.GONE);


        holder.plusCollection_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewRequestNotificationFragment.getViewRequestNotificationData(tasksList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasksList.size();
    }

}
