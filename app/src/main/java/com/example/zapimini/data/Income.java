package com.example.zapimini.data;

import com.google.gson.annotations.SerializedName;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "incomes")
public class Income {
    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name = "user_id")
    @SerializedName("user_id")
    int userId;

    @ColumnInfo(name = "business_id")
    @SerializedName("business_id")
    int businessId;

    @ColumnInfo(name = "gross_amount")
    @SerializedName("gross_amount")
    double grossAmount;

    @ColumnInfo(name = "total_expense")
    @SerializedName("total_expense")
    double totalExpense;

    @ColumnInfo(name = "net_amount")
    @SerializedName("net_amount")
    double netAmount;

    @ColumnInfo(name = "date_time")
    @SerializedName("date_time")
    String dateTime;

    public Income(int id, int userId, int businessId, double grossAmount,
                  double totalExpense, double netAmount, String dateTime) {
        this.id = id;
        this.userId = userId;
        this.businessId = businessId;
        this.grossAmount = grossAmount;
        this.totalExpense = totalExpense;
        this.netAmount = netAmount;
        this.dateTime = dateTime;
    }

    @Ignore
    public Income() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBusinessId() {
        return businessId;
    }

    public void setBusinessId(int businessId) {
        this.businessId = businessId;
    }

    public double getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(double grossAmount) {
        this.grossAmount = grossAmount;
    }

    public double getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(double totalExpense) {
        this.totalExpense = totalExpense;
    }

    public double getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(double netAmount) {
        this.netAmount = netAmount;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

}
