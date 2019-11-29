package com.nishantboro.splititeasy;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface GroupDao {
    @Query("SELECT * FROM groupentity")
    LiveData<List<GroupEntity>> getAll();

    @Query("SELECT * FROM groupentity")
    List<GroupEntity> getAllNonLive();

    @Query("SELECT GroupCurrency FROM groupentity WHERE GroupName= :gName")
    LiveData<String> getGroupCurrency(String gName);

    @Query("SELECT GroupCurrency FROM groupentity WHERE GroupName= :gName")
    String getGroupCurrencyNonLive(String gName);

    @Insert
    void insert(GroupEntity member);

    @Delete
    void delete(GroupEntity member);

    @Update
    void update(GroupEntity member);

    @Query("DELETE FROM groupentity")
    void deleteAll();
}
