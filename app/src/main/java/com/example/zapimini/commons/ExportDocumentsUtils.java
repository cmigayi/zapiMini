package com.example.zapimini.commons;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.example.zapimini.data.BankTransaction;
import com.example.zapimini.data.CashUp;
import com.example.zapimini.data.Credit;
import com.example.zapimini.data.Expense;
import com.example.zapimini.data.Income;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import androidx.core.content.FileProvider;

public class ExportDocumentsUtils {
    Context context;
    StringBuilder data;

    public ExportDocumentsUtils(Context context) {
        this.context = context;
        data = new StringBuilder();
    }

    public void expenseCSVFile(List<Expense> expenseList){
        data.append("DateTime,Item,Amount");
        for(int i = 0; i<expenseList.size(); i++){
            Expense expense = expenseList.get(i);
            data.append("\n"+expense.getDateTime()+","+expense.getItem()+","+
                    expense.getAmount());
        }
        csvFileExporter("expensesdata.csv", "ExpensesData");
    }

    public void bankTransactionCSVFile(List<BankTransaction> bankTransactionList){
        data.append("DateTime,Amount,TransactionType,Balance");
        for(int i = 0; i<bankTransactionList.size(); i++){
            BankTransaction bankTransaction = bankTransactionList.get(i);
            data.append("\n"+bankTransaction.getDateTime()+","+bankTransaction.getAmount()+","+
                    bankTransaction.getType()+","+bankTransaction.getBalance());
        }
        csvFileExporter("banktransactiondata.csv", "BankTransactionsData");
    }

    public void incomeCSVFile(List<Income> incomeList){
        data.append("DateTime,Gross Amount,Total Expense,Net amount");
        for(int i = 0; i<incomeList.size(); i++){
            Income income = incomeList.get(i);
            data.append("\n"+income.getDateTime()+","+income.getGrossAmount()+","+
                    income.getTotalExpense()+","+income.getNetAmount());
        }
        csvFileExporter("incomedata.csv", "IncomeData");
    }

    public void creditCSVFile(List<Credit> creditList){
        data.append("DateTime,Name,Amount,Phone,type");
        for(int i = 0; i<creditList.size(); i++){
            Credit credit = creditList.get(i);
            data.append("\n"+credit.getDateTime()+","+credit.getName()+","+
                    credit.getAmount()+","+credit.getPhone()+","+credit.getType());
        }
        csvFileExporter("creditdata.csv", "CreditData");
    }

    public void cashUpCSVFile(List<CashUp> cashUpList){
        data.append("DateTime,Name,Amount,Phone,type");
        for(int i = 0; i<cashUpList.size(); i++){
            CashUp cashUp = cashUpList.get(i);
            data.append("\n"+cashUp.getDateTime()+cashUp.getAmount());
        }
        csvFileExporter("cashupdata.csv", "CashUpData");
    }

    private void csvFileExporter(String fileNameWithExtension, String docName){
        try{
            //saving the file into device
            FileOutputStream out = context.openFileOutput(fileNameWithExtension, Context.MODE_PRIVATE);
            out.write((data.toString()).getBytes());
            out.close();

            //exporting
            File filelocation = new File(context.getFilesDir(), fileNameWithExtension);
            Uri path = FileProvider.getUriForFile(context, "com.example.zapimini.fileprovider", filelocation);
            Intent fileIntent = new Intent(Intent.ACTION_SEND);
            fileIntent.setType("text/csv");
            fileIntent.putExtra(Intent.EXTRA_SUBJECT, docName);
            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            fileIntent.putExtra(Intent.EXTRA_STREAM, path);
            context.startActivity(Intent.createChooser(fileIntent, "Send mail"));
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
