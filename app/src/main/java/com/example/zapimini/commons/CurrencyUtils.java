package com.example.zapimini.commons;

public class CurrencyUtils {
    String [] currencies = {"Kenya, Ksh", "Uganda, Ugs"};
    String[] currencyUnits = {"Ksh", "Ugs"};

    public String[] getCurrencies(){
        return currencies;
    }

    public String getCurrencyUnitOfSelectedCurrency(String currency){
        String unit = null;
        for(int i = 0; i<currencies.length; i++){
            if(currencies[i].equals(currency)){
                unit = getSelectedCurrencyUnit(i);
            }
        }
        return unit;
    }

    public int getSelectedCurrencyUnitPosition(String unit){
        int pos = -1;
        for(int i = 0; i<currencyUnits.length; i++){
            if(currencyUnits[i].equals(unit)){
                pos = i;
            }
        }
        return pos;
    }

    public String getSelectedCurrencyUnit(int pos){
        return currencyUnits[pos];
    }
}
