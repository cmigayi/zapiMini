package com.example.zapimini.commons;

import java.text.DecimalFormat;

public class MoneyUtils {

    public String AddMoneyFormat(double amount){
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        return formatter.format(amount);
    }
}
