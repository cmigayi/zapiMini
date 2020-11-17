package com.example.zapimini.commons;

import com.example.zapimini.data.CashUp;
import com.example.zapimini.data.Expense;

import java.util.List;

public class CashUpCalculation {
    double amount = 0.0;

    public double getTotalCashUpAmount(List<CashUp> cashUpList){
        try{
            for(int i=0; i<cashUpList.size(); i++){
                CashUp cashUp = cashUpList.get(i);
                amount = amount + cashUp.getAmount();
            }
        }catch(Exception e){
            amount = 0.0;
        }
        return amount;
    }
}
