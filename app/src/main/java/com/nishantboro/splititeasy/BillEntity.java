package com.nishantboro.splititeasy;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = MemberEntity.class,
parentColumns = "Id",
childColumns = "MemberId",
onDelete = ForeignKey.CASCADE,
onUpdate = ForeignKey.CASCADE),
indices = {@Index(name="MemberIdIndex",value = {"MemberId"})})
public class BillEntity {
    BillEntity(int mid, String item, String cost, String currency, String gName,String paidBy) {
        this.mid = mid;
        this.item = item;
        this.cost = cost;
        this.currency = currency;
        this.gName = gName;
        this.paidBy = paidBy;
    }
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Id")
    public int id;

    @ColumnInfo(name = "MemberId")
    public int mid;

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