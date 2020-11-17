package com.example.zapimini.commons;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class MoneyUtils {

    public String AddMoneyFormat(double amount){
        Locale locale = new Locale("en", "KE");
        /* Format amount in US format which is the default */
        NumberFormat formatter = NumberFormat.getCurrencyInstance(locale);
        return formatter.format(amount);
    }
}
