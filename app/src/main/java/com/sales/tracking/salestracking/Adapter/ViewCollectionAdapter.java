package com.sales.tracking.salestracking.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sales.tracking.salestracking.Bean.CollectionListBean;
import com.sales.tracking.salestracking.Bean.DashboardSalesPersonBean;
import com.sales.tracking.salestracking.Fragment.ViewTotalCollectionFragment;
import com.sales.tracking.salestracking.R;

import java.util.List;

public class ViewCollectionAdapter extends RecyclerView.Adapter<ViewCollectionAdapter.MyViewHolder> {
    private List<CollectionListBean.Collections> tasksList;
    ViewTotalCollectionFragment viewTotalCollectionFragment;
    Context context;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView dateValueCollection_tv,salesPersonValueCollection_tv;
        ImageView deleteCollection_iv, plusCollection_iv;

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

    public ViewCollectionAdapter(Context context, List<CollectionListBean.Collections> tasksList, ViewTotalCollectionFragment viewTotalCollectionFragment) {
        this.tasksList = tasksList;
        this.context = context;
        this.viewTotalCollectionFragment = viewTotalCollectionFragment;
    }

    @Override
    public ViewCollectionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_collection_list, parent, false);
        return new ViewCollectionAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewCollectionAdapter.MyViewHolder holder, final int position) {
        CollectionListBean.Collections bean = tasksList.get(position);

        holder.salesPersonValueCollection_tv.setText(bean.getCollection_amount());
        holder.dateValueCollection_tv.setText(bean.getCollection_date());

        holder.viewCollectionDetails_cv.setVisibility(View.GONE);
        holder.collectionDetails_cv.setVisibility(View.VISIBLE);
        holder.plusCollection_iv.setVisibility(View.GONE);

        holder.deleteCollection_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewTotalCollectionFragment.getDeleteTotalCollection(tasksList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasksList.size();
    }

}
