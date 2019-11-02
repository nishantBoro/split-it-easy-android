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
    // Query used for displaying all expenses/bills in Expenses fragment for a particular group
    @Query("SELECT * FROM billentity WHERE GroupName= :gName")
    LiveData<List<BillEntity>> getAll(String gName);

    /* Query used for calculating balances in Balances fragment for a particular group.
       Gets all the bills/expenses for a given(mid) member of the group*/
    @Query("SELECT * FROM billentity WHERE GroupName= :gName AND MemberId= :mid")
    List<BillEntity> getAllMemberBills(String gName,int mid);

    @Insert
    void insert(BillEntity bill);

    @Delete
    void delete(BillEntity bill);

    @Update
    void update(BillEntity bill);

    @Query("DELETE FROM billentity WHERE GroupName= :gName")
    void deleteAll(String gName);
}
