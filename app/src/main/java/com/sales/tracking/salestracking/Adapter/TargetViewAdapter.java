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
import com.sales.tracking.salestracking.Bean.TargetManagerBean;
import com.sales.tracking.salestracking.Fragment.ViewTargetManagerFragment;
import com.sales.tracking.salestracking.Fragment.ViewTotalExpensesFragment;
import com.sales.tracking.salestracking.R;

import java.util.List;

public class TargetViewAdapter  extends RecyclerView.Adapter<TargetViewAdapter.MyViewHolder> {
    private List<TargetManagerBean.Target> tasksList;
    ViewTargetManagerFragment viewTargetManagerFragment;
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

    public TargetViewAdapter(Context context, List<TargetManagerBean.Target> tasksList, ViewTargetManagerFragment viewTargetManagerFragment) {
        this.tasksList = tasksList;
        this.context = context;
        this.viewTargetManagerFragment = viewTargetManagerFragment;
    }

    @Override
    public TargetViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_collection_list, parent, false);
        return new TargetViewAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TargetViewAdapter.MyViewHolder holder, final int position) {
        TargetManagerBean.Target bean = tasksList.get(position);

        holder.salesPersonValueCollection_tv.setText(bean.getUser_name());

        holder.salesPersonCollectionHeading_tv.setText("Sales Person");

        String date = bean.getTarget_createdon();
        String[] date1 = date.split(" ");
        holder.dateValueCollection_tv.setText(date1[0]);

        holder.collectionDetails_cv.setVisibility(View.VISIBLE);

        holder.deleteCollection_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewTargetManagerFragment.deleteTargetData(tasksList.get(position));
            }
        });

        holder.plusCollection_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewTargetManagerFragment.getTargetData(tasksList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasksList.size();
    }

}
