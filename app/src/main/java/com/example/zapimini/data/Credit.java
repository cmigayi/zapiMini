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

    @ColumnInfo(name = "description")
    String description;

    @ColumnInfo(name = "amount")
    double amount;

    @ColumnInfo(name = "type")
    String type;

    @ColumnInfo(name = "date_time")
    String dateTime;

    public Credit(int id, int userId, int businessId, String name, String phone,
                  String description, double amount, String type, String dateTime) {
        this.id = id;
        this.userId = userId;
        this.businessId = businessId;
        this.name = name;
        this.phone = phone;
        this.description = description;
        this.amount = amount;
        this.type = type;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", type='" + type + '\'' +
                ", dateTime='" + dateTime + '\'' +
                '}';
    }
}
