package com.example.ivideo.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DanMuDao {
    @Insert
    void insertDanMu(DanMu danMu);
    @Query("select * from danmu")
    List<DanMu> selectAll();
}
