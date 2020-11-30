package com.example.zapimini.data;

import com.google.gson.annotations.SerializedName;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "expenses")
public class Expense {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "business_id")
    @SerializedName("business_id")
    private int businessId;

    @ColumnInfo(name = "user_id")
    @SerializedName("user_id")
    private int userId;

    @ColumnInfo(name = "item")
    @SerializedName("item")
    private String item;

    @ColumnInfo(name = "amount")
    @SerializedName("amount")
    private double amount;

    @ColumnInfo(name = "date_time")
    @SerializedName("date_time")
    private String dateTime;

    public Expense(int id, int businessId, int userId, String item, double amount, String dateTime) {
        this.id = id;
        this.businessId = businessId;
        this.userId = userId;
        this.item = item;
        this.amount = amount;
        this.dateTime = dateTime;
    }

    @Ignore
    public Expense() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBusinessId() {
        return businessId;
    }

    public void setBusinessId(int businessId) {
        this.businessId = businessId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
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

    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", businessId=" + businessId +
                ", userId=" + userId +
                ", item='" + item + '\'' +
                ", amount=" + amount +
                ", dateTime='" + dateTime + '\'' +
                '}';
    }
}
