package com.nishantboro.splititeasy;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MemberDao {
    @Query("SELECT * FROM memberentity WHERE GroupName= :gName")
    LiveData<List<MemberEntity>> getAll(String gName);

    @Query("SELECT * FROM memberentity WHERE GroupName= :gName")
    List<MemberEntity> getAllNonLive(String gName);

    @Insert
    void insert(MemberEntity member);

    @Delete
    void delete(MemberEntity member);

    @Update
    void update(MemberEntity member);

    @Query("DELETE FROM memberentity WHERE GroupName= :gName")
    void deleteAll(String gName);
}
