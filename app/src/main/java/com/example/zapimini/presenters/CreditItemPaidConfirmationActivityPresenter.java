package com.example.zapimini.presenters;

import com.example.zapimini.data.Credit;
import com.example.zapimini.localDatabases.CreditLocalDb;
import com.example.zapimini.views.CreditItemPaidConfirmationActivityView;

public class CreditItemPaidConfirmationActivityPresenter {
    final static String mCreditItemPaidConfirmationActivityPresenter= "CreditPaidConfirmation";
    CreditItemPaidConfirmationActivityView view;
    CreditLocalDb repository;

    public CreditItemPaidConfirmationActivityPresenter(
            CreditItemPaidConfirmationActivityView view, CreditLocalDb repository) {
        this.view = view;
        this.repository = repository;
    }

    public void clearReceivable(Credit credit){
        // Add to cashup, should indicate "settled date" in cashup report

        // Add to income

        // update credit to paid
    }

    public void clearPayable(Credit credit){
        // Indicate origin of the money

        // Add to expense

        // Subtract from income

        // update credit to paid
    }
}
