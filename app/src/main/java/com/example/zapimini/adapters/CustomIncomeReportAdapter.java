package com.example.zapimini.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.example.zapimini.R;
import com.example.zapimini.commons.DateTimeUtils;
import com.example.zapimini.commons.MoneyUtils;
import com.example.zapimini.data.Income;
import com.example.zapimini.databinding.CustomIncomeReportItemBinding;


import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class CustomIncomeReportAdapter extends
        RecyclerView.Adapter<CustomIncomeReportAdapter.ViewHolder> {

    final static String mCustomIncomeReportAdapter = "CustomIncomeReportAdapter";
    Context context;
    List<Income> incomelist;
    LayoutInflater layoutInflater;

    public CustomIncomeReportAdapter(Context context, List<Income> incomelist) {
        this.context = context;
        this.incomelist = incomelist;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CustomIncomeReportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CustomIncomeReportItemBinding binding = DataBindingUtil.inflate(
                layoutInflater, R.layout.custom_income_report_item, parent, false);
        CustomIncomeReportAdapter.ViewHolder viewHolder = new CustomIncomeReportAdapter.ViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomIncomeReportAdapter.ViewHolder holder, int position) {
        Income income = incomelist.get(position);
        String dateWithoutTime = new DateTimeUtils().removeTimeInDateTime(income.getDateTime());
        income.setDateTime(dateWithoutTime);
        holder.binding.setIncome(income);
        holder.binding.grossAmount.setText(
                "Gross: ksh."+new MoneyUtils().AddMoneyFormat(income.getGrossAmount()));
        holder.binding.totalExpense.setText(
                "Expense: ksh."+new MoneyUtils().AddMoneyFormat(income.getTotalExpense()));
        holder.binding.netAmount.setText(
                "Net: ksh."+new MoneyUtils().AddMoneyFormat(income.getNetAmount()));
    }

    @Override
    public int getItemCount() {
        return incomelist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomIncomeReportItemBinding binding;

        public ViewHolder(CustomIncomeReportItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
