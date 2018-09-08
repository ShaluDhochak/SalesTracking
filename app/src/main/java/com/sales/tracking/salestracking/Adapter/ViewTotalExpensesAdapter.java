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
import com.sales.tracking.salestracking.Bean.ExpencesSpBean;
import com.sales.tracking.salestracking.Fragment.ViewTotalCollectionFragment;
import com.sales.tracking.salestracking.Fragment.ViewTotalExpensesFragment;
import com.sales.tracking.salestracking.R;

import java.util.List;

public class ViewTotalExpensesAdapter  extends RecyclerView.Adapter<ViewTotalExpensesAdapter.MyViewHolder> {
    private List<ExpencesSpBean.Salesperson_Expenses> tasksList;
    ViewTotalExpensesFragment viewTotalExpensesFragment;
    Context context;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView dateValueCollection_tv,salesPersonValueCollection_tv, salesPersonCollectionHeading_tv;
        ImageView deleteCollection_iv, plusCollection_iv;

        CardView viewCollectionDetails_cv,collectionDetails_cv;

        MyViewHolder(View view) {
            super(view);

            viewCollectionDetails_cv = (CardView) view.findViewById(R.id.viewCollectionDetails_cv);
            collectionDetails_cv = (CardView) view.findViewById(R.id.collectionDetails_cv);

            salesPersonCollectionHeading_tv = (TextView) view.findViewById(R.id.salesPersonCollectionHeading_tv);
            dateValueCollection_tv = (TextView) view.findViewById(R.id.dateValueCollection_tv);
            salesPersonValueCollection_tv = (TextView) view.findViewById(R.id.salesPersonValueCollection_tv);

            deleteCollection_iv = (ImageView) view.findViewById(R.id.deleteCollection_iv);
            plusCollection_iv = (ImageView) view.findViewById(R.id.plusCollection_iv);
        }
    }

    public ViewTotalExpensesAdapter(Context context, List<ExpencesSpBean.Salesperson_Expenses> tasksList, ViewTotalExpensesFragment viewTotalExpensesFragment) {
        this.tasksList = tasksList;
        this.context = context;
        this.viewTotalExpensesFragment = viewTotalExpensesFragment;
    }

    @Override
    public ViewTotalExpensesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_collection_list, parent, false);
        return new ViewTotalExpensesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewTotalExpensesAdapter.MyViewHolder holder, final int position) {
        ExpencesSpBean.Salesperson_Expenses bean = tasksList.get(position);

        holder.salesPersonValueCollection_tv.setText(bean.getExpcat_name());

        holder.salesPersonCollectionHeading_tv.setText("Category");

        String date = bean.getExpense_date();
        String[] date1 = date.split(" ");
        holder.dateValueCollection_tv.setText(date1[0]);

        holder.viewCollectionDetails_cv.setVisibility(View.GONE);
        holder.collectionDetails_cv.setVisibility(View.VISIBLE);

        holder.deleteCollection_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //viewTotalCollectionFragment.getDeleteTotalCollection(tasksList.get(position));
            }
        });

        holder.plusCollection_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // viewTotalExpensesFragment.get(tasksList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasksList.size();
    }

}
