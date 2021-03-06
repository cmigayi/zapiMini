package com.example.zapimini.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "credits")
public class Credit {
    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name = "user_id")
    int userId;

    @ColumnInfo(name = "business_id")
    int businessId;

    @ColumnInfo(name = "name")
    String name;

    @ColumnInfo(name = "phone")
    String phone;

    @ColumnInfo(name = "amount")
    double amount;

    @ColumnInfo(name = "paid_amount")
    double paidAmount;

    @ColumnInfo(name = "balance")
    double balance;

    @ColumnInfo(name = "type")
    String type;

    @ColumnInfo(name = "credit_status")
    String creditStatus;

    @ColumnInfo(name = "date_time")
    String dateTime;

    public Credit(int id, int userId, int businessId, String name, String phone, double amount,
                  double paidAmount, double balance, String type, String creditStatus, String dateTime) {
        this.id = id;
        this.userId = userId;
        this.businessId = businessId;
        this.name = name;
        this.phone = phone;
        this.amount = amount;
        this.paidAmount = paidAmount;
        this.balance = balance;
        this.type = type;
        this.creditStatus = creditStatus;
        this.dateTime = dateTime;
    }

    @Ignore
    public Credit() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreditStatus() {
        return creditStatus;
    }

    public void setCreditStatus(String creditStatus) {
        this.creditStatus = creditStatus;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "Credit{" +
                "id=" + id +
                ", userId=" + userId +
                ", businessId=" + businessId +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", amount=" + amount +
                ", paidAmount=" + paidAmount +
                ", balance=" + balance +
                ", type='" + type + '\'' +
                ", creditStatus='" + creditStatus + '\'' +
                ", dateTime='" + dateTime + '\'' +
                '}';
    }
}
