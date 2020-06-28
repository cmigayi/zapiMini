package com.example.zapimini.data;

import com.google.gson.annotations.SerializedName;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "bank_transactions")
public class BankTransaction {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "business_id")
    @SerializedName("business_id")
    private int businessId;

    @ColumnInfo(name = "user_id")
    @SerializedName("user_id")
    private int userId;

    @ColumnInfo(name = "type")
    @SerializedName("type")
    private String type;

    @ColumnInfo(name = "amount")
    @SerializedName("amount")
    private double amount;

    @ColumnInfo(name = "balance")
    @SerializedName("balance")
    private double balance;

    @ColumnInfo(name = "date_time")
    @SerializedName("date_time")
    private String dateTime;

    public BankTransaction(int id, int businessId, int userId,
                           String type, double amount, double balance, String dateTime) {
        this.id = id;
        this.businessId = businessId;
        this.userId = userId;
        this.type = type;
        this.amount = amount;
        this.balance = balance;
        this.dateTime = dateTime;
    }

    @Ignore
    public BankTransaction() {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "BankTransaction{" +
                "id=" + id +
                ", businessId=" + businessId +
                ", userId=" + userId +
                ", type='" + type + '\'' +
                ", amount=" + amount +
                ", balance=" + balance +
                ", dateTime='" + dateTime + '\'' +
                '}';
    }
}
