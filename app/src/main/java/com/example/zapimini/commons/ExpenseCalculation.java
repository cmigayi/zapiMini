package com.example.zapimini.commons;

import com.example.zapimini.data.Expense;
import java.util.List;

public class ExpenseCalculation {
    double amount = 0.0;

    public double getTotalExpenseAmount(List<Expense> expenselist){
        try{
            for(int i=0; i<expenselist.size(); i++){
                Expense expense = expenselist.get(i);
                amount = amount + expense.getAmount();
            }
        }catch(Exception e){
            amount = 0.0;
        }
        return amount;
    }
}
