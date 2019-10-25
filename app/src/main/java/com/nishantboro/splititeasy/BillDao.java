package com.nishantboro.splititeasy;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BillDao {
    @Query("SELECT * FROM billentity")
    List<BillEntity> getAll();

    @Insert
    void insert(BillEntity user);

    @Delete
    void delete(BillEntity user);
}
