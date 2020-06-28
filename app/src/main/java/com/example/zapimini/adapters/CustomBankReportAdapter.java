package com.example.zapimini.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.zapimini.R;
import com.example.zapimini.commons.DateTimeUtils;
import com.example.zapimini.commons.MoneyUtils;
import com.example.zapimini.data.BankTransaction;
import com.example.zapimini.databinding.CustomBankReportItemBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class CustomBankReportAdapter extends
        RecyclerView.Adapter<CustomBankReportAdapter.ViewHolder> {
    final static String mCustomBankReportAdapter = "CustomBankReportAdapter";
    Context context;
    List<BankTransaction> bankTransactionlist;
    OnBankReportListener onBankReportListener;
    LayoutInflater layoutInflater;

    public CustomBankReportAdapter(Context context, List<BankTransaction> bankTransactionlist,
                                   OnBankReportListener onBankReportListener) {
        this.context = context;
        this.bankTransactionlist = bankTransactionlist;
        this.onBankReportListener = onBankReportListener;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CustomBankReportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CustomBankReportItemBinding binding = DataBindingUtil.inflate(
                layoutInflater, R.layout.custom_bank_report_item, parent, false);
        CustomBankReportAdapter.ViewHolder viewHolder =
                new CustomBankReportAdapter.ViewHolder(binding, onBankReportListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomBankReportAdapter.ViewHolder holder, int position) {
        BankTransaction bankTransaction = bankTransactionlist.get(position);
        String dateWithoutTime = new DateTimeUtils().removeTimeInDateTime(bankTransaction.getDateTime());
        bankTransaction.setDateTime(dateWithoutTime);
        setChangeTextColorBasedOnBankTransactionType(bankTransaction, holder);
        holder.binding.setBankTransaction(bankTransaction);
        holder.binding.amount.setText(
                "Amount: ksh."+new MoneyUtils().AddMoneyFormat(bankTransaction.getAmount()));
        holder.binding.balance.setText(
                "Amount: ksh."+new MoneyUtils().AddMoneyFormat(bankTransaction.getBalance()));
    }

    @Override
    public int getItemCount() {
        return bankTransactionlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CustomBankReportItemBinding binding;
        OnBankReportListener onBankReportListener;

        public ViewHolder(CustomBankReportItemBinding itemView,
                          OnBankReportListener onBankReportListener) {
            super(itemView.getRoot());
            binding = itemView;
            this.onBankReportListener = onBankReportListener;

            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onBankReportListener.onBankReportClick(getAdapterPosition());
        }
    }

    public interface OnBankReportListener{
        void onBankReportClick(int position);
    }

    public void setChangeTextColorBasedOnBankTransactionType(BankTransaction bankTransaction,
                                              ViewHolder holder){
        if(bankTransaction.getType().equals("Deposit")){
            holder.binding.amount.setTextColor(Color.parseColor("#1dc235"));
        }else{
            holder.binding.amount.setTextColor(Color.parseColor("#ff0000"));
        }

    }
}
