package com.example.zapimini.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "cash_ups")
public class CashUp {

    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name = "user_id")
    @SerializedName("user_id")
    int userId;

    @ColumnInfo(name = "business_id")
    @SerializedName("business_id")
    int businessId;

    @ColumnInfo(name = "amount")
    double amount;

    @ColumnInfo(name = "date_time")
    @SerializedName("date_time")
    String dateTime;

    public CashUp(int id, int userId, int businessId, double amount, String dateTime) {
        this.id = id;
        this.userId = userId;
        this.businessId = businessId;
        this.amount = amount;
        this.dateTime = dateTime;
    }

    @Ignore
    public CashUp() {
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
