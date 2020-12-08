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

    // This will apply when credit amount is repaid, otherwise the id will remain -1
    @ColumnInfo(name = "credit_id")
    @SerializedName("credit_id")
    int creditId;

    @ColumnInfo(name = "amount")
    double amount;

    @ColumnInfo(name = "description")
    String description;

    @ColumnInfo(name = "payment_mode")
    String paymentMode;

    @ColumnInfo(name = "date_time")
    @SerializedName("date_time")
    String dateTime;

    public CashUp(int id, int userId, int businessId, int creditId, double amount,
                  String description, String paymentMode, String dateTime) {
        this.id = id;
        this.userId = userId;
        this.businessId = businessId;
        this.creditId = creditId;
        this.amount = amount;
        this.description = description;
        this.paymentMode = paymentMode;
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

    public int getCreditId() {
        return creditId;
    }

    public void setCreditId(int creditId) {
        this.creditId = creditId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "CashUp{" +
                "id=" + id +
                ", userId=" + userId +
                ", businessId=" + businessId +
                ", creditId=" + creditId +
                ", amount=" + amount +
                ", description=" + description +
                ", paymentMode=" + paymentMode +
                ", dateTime='" + dateTime + '\'' +
                '}';
    }
}
