package com.example.zapimini.commons;

import com.example.zapimini.data.Income;

import java.util.List;

public class IncomeCalculation {
    double grossAmount = 0.0, netAmount = 0.0;

    public double getNetIncome(double grossIncome, double totalExpense){
        return grossIncome - totalExpense;
    }

    public double getTotalNetAmount(List<Income> incomelist){
        try{
            for(int i=0; i<incomelist.size(); i++){
                Income income = incomelist.get(i);
                netAmount = netAmount + income.getNetAmount();
            }
        }catch(Exception e){
            netAmount = 0.0;
        }
        return netAmount;
    }
}
