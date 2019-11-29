package com.nishantboro.splititeasy;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

// prevents from having two rows with the same values for the "GroupName" column
@Entity(indices = {@Index(value = {"GroupName"},
        unique = true)})
public class GroupEntity {
    GroupEntity(String gName) {
        this.gName = gName;
    }

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "GroupName")
    public String gName;

    @ColumnInfo(name = "GroupCurrency")
    public String gCurrency;
}