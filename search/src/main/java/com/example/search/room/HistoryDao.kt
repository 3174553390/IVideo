package com.example.search.room

import androidx.room.*

@Dao
interface HistoryDao {
    @Insert
    fun insert(history: HistoryBean): Long

    @Update
    fun update(history: HistoryBean)

    @Delete
    fun delete(history: HistoryBean)

    @Query("select * from history")
    fun getAllHistory(): List<HistoryBean>

    @Query("delete from history where title = :title1 ")
    fun deleteByTom(title1: String): Int

}