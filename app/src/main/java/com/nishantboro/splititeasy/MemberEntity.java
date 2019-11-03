package com.nishantboro.splititeasy;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class MemberEntity {
    MemberEntity(String name, String gName) {
        this.name = name;
        this.gName = gName;
    }
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Id")
    public int id;

    @ColumnInfo(name = "GroupName")
    public String gName;

    @ColumnInfo(name = "MemberName")
    public String name;

    @ColumnInfo(name = "MemberAvatar")
    public int mAvatar;

    public void setId(int id) {
        this.id = id;
    }

    public void setMAvatar(int mAvatar) {
        this.mAvatar = mAvatar;
    }
}