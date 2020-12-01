package com.example.zapimini.commons;

import com.example.zapimini.data.Credit;

import java.util.List;

public class CreditCalculation {
    double amount = 0.0, balance = 0.0;

    public double getTotalCreditAmount(List<Credit> creditlist){
        try{
            for(int i=0; i<creditlist.size(); i++){
                Credit credit = creditlist.get(i);
                amount = amount + credit.getAmount();
            }
        }catch(Exception e){
            amount = 0.0;
        }
        return amount;
    }

    public double getCreditBalance(double creditAmount, double paidAmount){
        balance = creditAmount - paidAmount;
        return balance;
    }

}
