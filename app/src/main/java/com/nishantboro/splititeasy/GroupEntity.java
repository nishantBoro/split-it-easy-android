package com.nishantboro.splititeasy;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"GroupName"},
        unique = true)})
public class GroupEntity {
    GroupEntity(String gName) {
        this.gName = gName;
    }


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Id")
    public int gid;

    @ColumnInfo(name = "GroupName")
    public String gName;
}