package com.example.zapimini.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.zapimini.R;
import com.example.zapimini.commons.DateTimeUtils;
import com.example.zapimini.commons.MoneyUtils;
import com.example.zapimini.data.Expense;
import com.example.zapimini.databinding.CustomExpenseReportItemBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class CustomExpenseReportAdapter extends
        RecyclerView.Adapter<CustomExpenseReportAdapter.ViewHolder> {
    final static String mCustomExpenseReportAdapter = "CustomExpenseReportAdapter";
    Context context;
    List<Expense> expenselist;
    OnExpenseReportListener onExpenseReportListener;
    LayoutInflater layoutInflater;

    public CustomExpenseReportAdapter(Context context, List<Expense> expenselist,
                                      OnExpenseReportListener onExpenseReportListener) {
        this.context = context;
        this.expenselist = expenselist;
        this.onExpenseReportListener = onExpenseReportListener;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CustomExpenseReportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CustomExpenseReportItemBinding binding = DataBindingUtil.inflate(
                layoutInflater, R.layout.custom_expense_report_item, parent, false);
        CustomExpenseReportAdapter.ViewHolder viewHolder =
                new CustomExpenseReportAdapter.ViewHolder(binding, onExpenseReportListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomExpenseReportAdapter.ViewHolder holder, int position) {
        Expense expense = expenselist.get(position);
        // String dateWithoutTime = new DateTimeUtils().removeTimeInDateTime(expense.getDateTime());
        String dateWithTime = expense.getDateTime();
        expense.setDateTime(dateWithTime);
        holder.binding.setExpense(expense);
        holder.binding.amount.setText(new MoneyUtils().AddMoneyFormat(expense.getAmount()));
        if(expense.getCreditId() > 0){
            holder.binding.creditStatusTv.setText("Paid credit");
            holder.binding.creditStatusTv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return expenselist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CustomExpenseReportItemBinding binding;
        OnExpenseReportListener onExpenseReportListener;

        public ViewHolder(CustomExpenseReportItemBinding itemView,
                          OnExpenseReportListener onExpenseReportListener) {
            super(itemView.getRoot());
            binding = itemView;
            this.onExpenseReportListener = onExpenseReportListener;

            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onExpenseReportListener.onExpenseReportClick(getAdapterPosition());
        }
    }

    public interface OnExpenseReportListener{
        void onExpenseReportClick(int position);
    }
}
