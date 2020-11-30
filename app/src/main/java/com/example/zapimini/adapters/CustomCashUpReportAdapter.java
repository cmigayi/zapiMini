package com.example.zapimini.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.example.zapimini.R;
import com.example.zapimini.commons.DateTimeUtils;
import com.example.zapimini.commons.MoneyUtils;
import com.example.zapimini.data.CashUp;
import com.example.zapimini.databinding.CustomCashUpReportItemBinding;

import java.util.List;

public class CustomCashUpReportAdapter extends
        RecyclerView.Adapter<CustomCashUpReportAdapter.ViewHolder>{
    final static String mCustomCashUpReportAdapter = "CustomCashUpReportAdapter";
    Context context;
    List<CashUp> cashUpList;
    CustomCashUpReportAdapter.OnCashUpReportListener onCashUpReportListener;
    LayoutInflater layoutInflater;

    public CustomCashUpReportAdapter(Context context, List<CashUp> cashUpList,
                                     CustomCashUpReportAdapter.OnCashUpReportListener onCashUpReportListener) {
        this.context = context;
        this.cashUpList = cashUpList;
        this.onCashUpReportListener = onCashUpReportListener;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CustomCashUpReportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CustomCashUpReportItemBinding binding = DataBindingUtil.inflate(
                layoutInflater, R.layout.custom_cash_up_report_item, parent, false);
        CustomCashUpReportAdapter.ViewHolder viewHolder =
                new CustomCashUpReportAdapter.ViewHolder(binding, onCashUpReportListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomCashUpReportAdapter.ViewHolder holder, int position) {
        CashUp cashUp = cashUpList.get(position);
        String dateWithoutTime = new DateTimeUtils().removeTimeInDateTime(cashUp.getDateTime());
        cashUp.setDateTime(dateWithoutTime);
        holder.binding.setCashup(cashUp);
        holder.binding.amount.setText(new MoneyUtils().AddMoneyFormat(cashUp.getAmount()));

        if(cashUp.getCreditId() != -1){
            holder.binding.creditTicker.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return cashUpList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CustomCashUpReportItemBinding binding;
        CustomCashUpReportAdapter.OnCashUpReportListener onCashUpReportListener;

        public ViewHolder(CustomCashUpReportItemBinding itemView,
                          CustomCashUpReportAdapter.OnCashUpReportListener onCashUpReportListener) {
            super(itemView.getRoot());
            binding = itemView;
            this.onCashUpReportListener = onCashUpReportListener;

            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onCashUpReportListener.onCashUpReportClick(getAdapterPosition());
        }
    }

    public interface OnCashUpReportListener{
        void onCashUpReportClick(int position);
    }
}
