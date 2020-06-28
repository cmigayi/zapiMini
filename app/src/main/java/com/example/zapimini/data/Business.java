package com.example.zapimini.data;

import com.google.gson.annotations.SerializedName;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "business")
public class Business {
    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name = "user_id")
    @SerializedName("user_id")
    int userId;

    @ColumnInfo(name = "name")
    @SerializedName("name")
    String name;

    @ColumnInfo(name = "description")
    @SerializedName("description")
    String description;

    @ColumnInfo(name = "currency")
    String currency;

    @ColumnInfo(name = "date_time")
    @SerializedName("date_time")
    String dateTime;

    public Business(int id, int userId, String name, String description,
                    String currency, String dateTime) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.currency = currency;
        this.dateTime = dateTime;
    }

    @Ignore
    public Business() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

}
