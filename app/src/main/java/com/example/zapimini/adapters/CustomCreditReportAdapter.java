package com.example.zapimini.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zapimini.R;
import com.example.zapimini.commons.DateTimeUtils;
import com.example.zapimini.commons.MoneyUtils;
import com.example.zapimini.data.Credit;
import com.example.zapimini.databinding.CustomCreditReportItemBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class CustomCreditReportAdapter extends
        RecyclerView.Adapter<CustomCreditReportAdapter.ViewHolder> {
    final static String mCustomCreditReportAdapter = "CustomCreditReportAdapter";
    Context context;
    List<Credit> creditlist;
    OnCreditReportListener onCreditReportListener;
    LayoutInflater layoutInflater;

    public CustomCreditReportAdapter(Context context, List<Credit> creditlist,
                                      OnCreditReportListener onCreditReportListener) {
        this.context = context;
        this.creditlist = creditlist;
        this.onCreditReportListener = onCreditReportListener;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CustomCreditReportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CustomCreditReportItemBinding binding = DataBindingUtil.inflate(
                layoutInflater, R.layout.custom_credit_report_item, parent, false);
        CustomCreditReportAdapter.ViewHolder viewHolder =
                new CustomCreditReportAdapter.ViewHolder(binding, onCreditReportListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomCreditReportAdapter.ViewHolder holder, int position) {
        Credit credit = creditlist.get(position);
        // String dateWithoutTime = new DateTimeUtils().removeTimeInDateTime(credit.getDateTime());
        String dateWithTime = credit.getDateTime();
        double balance = credit.getBalance();

        credit.setDateTime(dateWithTime);
        holder.binding.setCredit(credit);
        holder.binding.amount.setText(new MoneyUtils().AddMoneyFormat(credit.getAmount()));
        holder.binding.paid.setText("Paid: "+new MoneyUtils().AddMoneyFormat(credit.getPaidAmount()));
        holder.binding.balance.setText("Bal: "+new MoneyUtils().AddMoneyFormat(credit.getBalance()));
        if(balance == 0.0){
            holder.binding.fullyPaidTv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return creditlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CustomCreditReportItemBinding binding;
        OnCreditReportListener onCreditReportListener;

        public ViewHolder(CustomCreditReportItemBinding itemView,
                          OnCreditReportListener onCreditReportListener) {
            super(itemView.getRoot());
            binding = itemView;
            this.onCreditReportListener = onCreditReportListener;

            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onCreditReportListener.onCreditReportClick(getAdapterPosition());
        }
    }

    public interface OnCreditReportListener{
        void onCreditReportClick(int position);
    }
}
