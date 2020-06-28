package com.example.zapimini.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.zapimini.R;
import com.example.zapimini.databinding.CustomReportItemBinding;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class CustomReportAdapter extends
        RecyclerView.Adapter<CustomReportAdapter.ViewHolder> {
    final static String mCustomTripReportAdapter = "CustomTripReportAdapter";
    Context context;
    String[] reportlist;
    int[] reportIconList;
    String[] reportDescriptionList;
    OnReportListener onReportListener;
    LayoutInflater layoutInflater;

    public CustomReportAdapter(Context context, String[] reportlist,
                               int[] reportIconList, String[] reportDescriptionList,
                               OnReportListener onReportListener) {
        this.context = context;
        this.reportlist = reportlist;
        this.reportIconList = reportIconList;
        this.reportDescriptionList = reportDescriptionList;
        this.onReportListener = onReportListener;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CustomReportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CustomReportItemBinding binding = DataBindingUtil.inflate(
                layoutInflater, R.layout.custom_report_item, parent, false);
        CustomReportAdapter.ViewHolder viewHolder =
                new CustomReportAdapter.ViewHolder(binding, onReportListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomReportAdapter.ViewHolder holder, int position) {
        holder.binding.reportItem.setText(reportlist[position]);
        holder.binding.reportIcon.setImageResource(reportIconList[position]);
        holder.binding.reportItemDescription.setText(reportDescriptionList[position]);
    }

    @Override
    public int getItemCount() {
        return reportlist.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CustomReportItemBinding binding;
        OnReportListener onReportListener;

        public ViewHolder(CustomReportItemBinding itemView, OnReportListener onReportListener) {
            super(itemView.getRoot());
            binding = itemView;
            binding.getRoot().setOnClickListener(this);
            this.onReportListener = onReportListener;
        }

        @Override
        public void onClick(View v) {
            onReportListener.onReportClick(getAdapterPosition());
        }
    }

    public interface OnReportListener{
        void onReportClick(int position);
    }
}
