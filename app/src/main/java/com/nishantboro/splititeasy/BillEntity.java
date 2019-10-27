package com.nishantboro.splititeasy;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class BillEntity {
    BillEntity(String item, String cost, String currency, String gName,String paidBy) {
        this.item = item;
        this.cost = cost;
        this.currency = currency;
        this.gName = gName;
        this.paidBy = paidBy;
    }
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "Item")
    public String item;

    @ColumnInfo(name = "PaidBy")
    public String paidBy;

    @ColumnInfo(name = "Cost")
    public String cost;

    @ColumnInfo(name = "Currency")
    public String currency;

    @ColumnInfo(name = "GroupName")
    public String gName;
}