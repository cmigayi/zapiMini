package com.example.zapimini.commons;

import com.example.zapimini.data.BankTransaction;

public class BankCalculation {

    public double getBalanceFromPreviousTransactions(
            BankTransaction previousBankTransaction, double amount, String currentType){
        double balance = 0.0;
        try{
            if(previousBankTransaction != null){
                balance = previousBankTransaction.getBalance();
            }
            switch(currentType){
                case "Deposit":
                    balance = balance + amount;
                    break;
                case "Withdraw":
                    balance = balance - amount;
                    break;
            }
        }catch(Exception e){
            balance = balance + amount;
        }
        return balance;
    }
}
