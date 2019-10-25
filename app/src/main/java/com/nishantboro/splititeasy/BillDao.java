package com.nishantboro.splititeasy;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BillDao {
    @Query("SELECT * FROM billentity WHERE GroupName= :gName")
    LiveData<List<BillEntity>> getAll(String gName);

    @Insert
    void insert(BillEntity bill);

    @Delete
    void delete(BillEntity bill);

    @Update
    void update(BillEntity bill);

    @Query("DELETE FROM billentity WHERE GroupName= :gName")
    void deleteAll(String gName);
}
